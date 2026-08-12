# Current Detection ⚡

A modern, premium Android application designed to track and monitor electricity availability (load shedding) in your building using local Wi-Fi networks as power identifiers. 

Developed by **MD YEAMIN TALUKDER** (GitHub: [https://github.com/Yeamin-Talukder](https://github.com/Yeamin-Talukder)).

---

## 📱 How It Works (The Core Concept)

`Current Detection` leverages the presence or absence of specific Wi-Fi networks in your building to deduce whether electrical power is available:

```
[ Power is ON ]  ───►  Building routers are powered  ───►  Wi-Fi signals are broadcasted  ───► [ CURRENT ON ]
[ Power is OFF ] ───►  Building routers go offline  ───►  Wi-Fi signals disappear        ───► [ CURRENT OFF ]
```

By registering your building's Wi-Fi network(s) as **Current Identifiers**, the app monitors their availability in the background and records outages and restoration sessions in real time.

---

## 🛠️ Hybrid Detection Architecture

To achieve the best balance between **real-time accuracy** and **battery efficiency**, the app uses a dual-stage hybrid detection architecture inside the `PowerDetectionEngine`:

```
                    [ Start Detection Cycle ]
                               │
                               ▼
               Check Currently Connected BSSID
                               │
            ┌──────────────────┴──────────────────┐
            ▼ (Matches Saved BSSID)               ▼ (No Match / Disconnected)
      [ CURRENT ON ]                        Perform Wi-Fi Scan
     (Scan skipped to                       (Check nearby networks)
      save battery!)                               │
                                                   ▼
                                         Match Scanned BSSIDs
                                                   │
                                ┌──────────────────┴──────────────────┐
                                ▼ (Match Found)                       ▼ (No Match)
                          [ CURRENT ON ]                      [ POSSIBLE CURRENT OFF ]
                                                                      │
                                                                      ▼
                                                            Observe Period (30s)
                                                                      │
                                                              ┌───────┴───────┐
                                                              ▼ (Confirmed)   ▼ (Aborted)
                                                        [ CURRENT OFF ]    [ CURRENT ON ]
```

1. **Active Connection Check**: The engine first queries the active Wi-Fi connection's BSSID. If it matches a saved identifier, the state is marked as **CURRENT ON** immediately, skipping the expensive Wi-Fi scan entirely.
2. **Nearby Scan**: If there is no match or the device is disconnected, the engine initiates a Wi-Fi scan.
3. **BSSID Matching**: Scanned networks are matched using BSSID (MAC Address) instead of SSID to ensure high-security mapping and prevent spoofing.
4. **State Confirmation**: To filter out momentary router restarts or transient disconnects, a transition to **CURRENT OFF** requires a 30-second observation window.

---

## 🎨 Premium Modern UI Theme

The app follows a custom dark-first theme:
* **Emerald Green (`#2ECC71`)**: Primary / Current ON / Selected Items / Active Navigation.
* **Dark Blue-Gray (`#2C3E50`)**: Cards / Supporting Surfaces / Dialog Backgrounds.
* **Yellow (`#F1C40F`)**: Warnings / Unknown or Unchecked States.
* **Red (`#C0392B`)**: Current OFF / Outages / Delete Actions.

---

## ✨ Features & Navigation

* **⚡ Home Tab**:
  * **Dynamic Status Indicator**: Displays a pulsating emerald green glow when power is ON, and a warning red when power is OFF.
  * **State Timer**: Live counter measuring power uptime or downtime since the last event (timer calculations start from your actual installation time, ignoring pre-install periods).
  * **Interactive Timeline**: Real-time gradient timeline displaying power states across 24 hours. Features a grey "UNKNOWN" block representing the time before the app was installed.
  * **Network breakdown**: Displays which of your identifiers are **ACTIVE**, **OFFLINE**, or **NOT SCANNED** (for skipped scans).
* **📊 History Tab**:
  * Groups outages by day (Today, Yesterday, Date).
  * Outage log with startTime, endTime, and durations.
  * Per-day power bar visualizing state intervals.
  * Calculated availability percentage (ignores pre-installation times).
* **📶 Networks (Checkers) Tab**:
  * Dynamic list of registered Power Checkers.
  * Scan & add nearby Wi-Fi networks in your building.
* **⚙️ Settings Tab**:
  * Toggle background monitoring, outage alerts, daily summaries.
  * About Developer section referencing **MD YEAMIN TALUKDER**.

---

## 🚀 Setup & Build Instructions

### Prerequisites
* Android Studio (Koala or newer)
* Android SDK 34 (Android 14)
* Gradle 8.4+

### Compilation
Clone the repository and compile the project using Gradle:
```bash
./gradlew.bat compileDebugKotlin
```

### Permissions Required
The app requires the following system permissions to operate background detection:
* `ACCESS_FINE_LOCATION` & `ACCESS_COARSE_LOCATION` (Required by Android to scan Wi-Fi networks)
* `POST_NOTIFICATIONS` (To alert you of power outages)
* `FOREGROUND_SERVICE` (To run background monitoring)

---

## 📄 License
This project is proprietary and built for personal use. All rights reserved.

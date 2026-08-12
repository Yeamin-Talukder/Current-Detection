# ⚡ Current Detection

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" />
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" />
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" />
  <img src="https://img.shields.io/badge/Min%20SDK-26-2ECC71?style=for-the-badge" />
</p>

A **premium, dark-themed Android app** that monitors electricity availability (load shedding) in your building — using nearby Wi-Fi networks as power indicators. No hardware sensors needed.

Built by **MD YEAMIN TALUKDER** → [github.com/Yeamin-Talukder](https://github.com/Yeamin-Talukder)

---

## 💡 The Core Idea

When electricity is available, your building's routers stay powered and broadcast Wi-Fi signals.  
When a power cut happens, those routers shut down and their Wi-Fi signals disappear.

```
Power ON  → Building routers active  → Wi-Fi detected  → 🟢 CURRENT ON
Power OFF → Building routers offline → Wi-Fi gone      → 🔴 CURRENT OFF
```

By registering specific building Wi-Fi networks as **Current Identifiers**, the app continuously monitors their presence and automatically logs every outage and restoration event.

> ⚠️ **Important:** Do NOT add Wi-Fi networks from routers connected to IPS or UPS power backup. Those routers remain online during outages and will give false readings. Only register routers that run directly on main electricity.

---

## 🛠️ Architecture

The app follows **MVVM with Clean Architecture** and is structured into clear layers:

```
┌─────────────────────────────────────────────────────────────────┐
│                     UI Layer (Jetpack Compose)                   │
│   HomeScreen │ HistoryScreen │ SettingsScreen │ OnboardingScreen│
└────────────────────────┬────────────────────────────────────────┘
                         │ observes StateFlows
┌────────────────────────▼────────────────────────────────────────┐
│                        ViewModel Layer                           │
│       HomeViewModel │ HistoryViewModel │ SettingsViewModel       │
└────────────────────────┬────────────────────────────────────────┘
                         │ uses
┌────────────────────────▼────────────────────────────────────────┐
│                       Domain / Engine Layer                      │
│   PowerDetectionEngine │ EventManager │ PowerMonitoringManager   │
└──────────┬─────────────┴──────────────────────────┬────────────┘
           │ calls                                   │ reads
┌──────────▼───────────┐              ┌──────────────▼────────────┐
│     WiFi Layer       │              │       Data Layer           │
│   WifiScannerImpl    │              │  Room DB (PowerEventDao,  │
│   (BSSID scanning)   │              │  NetworkDao) + DataStore   │
└──────────────────────┘              └───────────────────────────┘
```

### Key Components

| Component | Description |
|---|---|
| `PowerDetectionEngine` | Hybrid BSSID detection: checks connected Wi-Fi first, falls back to full scan |
| `EventManager` | Singleton state machine: debounces transitions (30s OFF confirmation, 15s ON confirmation) |
| `PowerMonitoringManager` | Runs the detection loop every 30s inside a `CoroutineScope` |
| `PowerMonitoringService` | Android `ForegroundService` keeping monitoring alive in the background |
| `DailySummaryWorker` | `WorkManager` job scheduled at 11:59 PM daily for the summary notification |
| `AppNotificationManager` | Checks notification preferences before firing alerts |
| `SettingsManager` | All persistent settings via Jetpack `DataStore` |

---

## 🔍 Hybrid Detection Flow

```
[ Detection Cycle starts every 30 seconds ]
               │
               ▼
  ┌─── Check Active Wi-Fi BSSID ───┐
  │                                │
  ▼ (matches saved identifier)     ▼ (no match / disconnected)
[ CURRENT ON ]               Perform Wi-Fi Scan
(scan skipped, saves battery)       │
                                    ▼
                          Match all scanned BSSIDs
                                    │
                      ┌─────────────┴──────────────┐
                      ▼ (match found)              ▼ (no match)
                [ CURRENT ON ]          [ POSSIBLE CURRENT OFF ]
                                                    │
                                         Wait 30s confirmation
                                                    │
                                        ┌───────────┴──────────┐
                                        ▼ (still no match)     ▼ (match found)
                                  [ CURRENT OFF ]         [ CURRENT ON ]
```

- **BSSID matching** (not SSID) prevents spoofing by same-name networks
- **30s confirmation window** prevents false positives from brief router reboots
- **Battery-efficient**: skips expensive scans when already connected to an identifier

---

## ✨ Features

### 🏠 Home Tab
- **Live status card** — animated green glow (ON) or red alert (OFF)
- **State duration counter** — counts up from actual first install time (no pre-install assumption)
- **24-hour power timeline** — interactive gradient bar; grey block shown for pre-install "UNKNOWN" period
- **Network breakdown** — each identifier shows: ACTIVE / OFFLINE / NOT SCANNED
- **Scan animation** — shows Idle → Checking Connected → Scanning → Matching → Done phases

### 📊 History Tab
- Groups outage records by **Today / Yesterday / Date**
- Per-day **mini power bar** (green ON, red OFF, grey UNKNOWN)
- **Availability %** calculated per day, ignoring pre-install time
- Expandable **outage log** with start time, end time, and duration
- Summary chips: total ON time / OFF time / monitored time

### 📶 Networks Tab
- List of registered Power Checkers
- Scan and add nearby Wi-Fi networks by BSSID

### ⚙️ Settings Tab
| Setting | Functionality |
|---|---|
| Enable Monitoring | Starts / stops the `PowerMonitoringService` foreground service |
| Outage Alerts | Gates `showPowerOffAlert()` in `AppNotificationManager` |
| Power Restored Alerts | Gates `showPowerOnAlert()` in `AppNotificationManager` |
| Daily Summary | Schedules / cancels a `WorkManager` `PeriodicWorkRequest` at 11:59 PM |
| Export History | Generates CSV and opens the Android Share Sheet |
| Clear History | Deletes all `PowerEventEntity` records from Room |
| User Manual | Opens the animated onboarding guide from within the app |

### 📖 User Manual (Onboarding)
4-page animated guide:
1. **Welcome** — pulsing lightning bolt with layered green glow
2. **How It Works** — auto-cycling: Router ON → Wi-Fi Off → Recorded
3. **⚠️ IPS/UPS Warning** — pulsing amber warning icon; explains why backup-powered routers break detection
4. **Add Networks** — ripple scan animation with networks sliding in one by one

---

## 🎨 Design System

| Token | Value | Role |
|---|---|---|
| Primary | `#2ECC71` | Power ON, active elements, primary buttons |
| Surface | `#2C3E50` | Cards, navigation, dialogs |
| Warning | `#F1C40F` | Unknown state, IPS warning |
| Danger | `#C0392B` | Power OFF, delete, error |
| Background | `#0D1B24` | App background (deeper dark) |

All animations use `FastOutSlowInEasing` or `spring()` for a smooth, premium feel. No excessive neon or glow effects.

---

## 🚀 Setup & Build

### Prerequisites
- Android Studio Koala (2024.1) or newer
- Android SDK 34
- JDK 17

### Clone & Build
```bash
git clone https://github.com/Yeamin-Talukder/current-detection.git
cd current-detection
./gradlew assembleDebug
```

### Run on Device
```bash
./gradlew installDebug
```

### Required Permissions
| Permission | Why |
|---|---|
| `ACCESS_FINE_LOCATION` | Mandatory for Wi-Fi scanning on Android 6+ |
| `ACCESS_COARSE_LOCATION` | Fallback location for scan |
| `POST_NOTIFICATIONS` | Power alerts and daily summary |
| `FOREGROUND_SERVICE` | Keep monitoring alive in background |
| `FOREGROUND_SERVICE_LOCATION` | Required for location-type foreground service |

---

## 📁 Project Structure

```
app/src/main/java/com/currentdetection/
├── data/
│   ├── local/
│   │   ├── AppDatabase.kt          # Room database
│   │   ├── SettingsManager.kt      # DataStore preferences
│   │   ├── PowerEventDao.kt        # Outage event queries
│   │   ├── NetworkDao.kt           # Saved network queries
│   │   └── entities/               # Room entities
│   └── repository/                 # Repository implementations
├── domain/
│   └── repository/                 # Interfaces
├── engine/
│   ├── PowerDetectionEngine.kt     # BSSID hybrid detection logic
│   ├── EventManager.kt             # State machine & confirmation delays
│   ├── PowerMonitoringManager.kt   # Polling loop & notification triggers
│   ├── PowerMonitoringService.kt   # Android ForegroundService
│   ├── AppNotificationManager.kt   # Notification builder + preference gating
│   └── DailySummaryWorker.kt       # WorkManager daily report job
├── ui/
│   ├── home/                       # Home screen + ViewModel
│   ├── history/                    # History screen + ViewModel
│   ├── settings/                   # Settings screen + ViewModel
│   ├── checkers/                   # Add/view Power Checkers
│   ├── onboarding/                 # 4-page animated user manual
│   ├── navigation/                 # AppNavigation + Screen routes
│   ├── main/                       # Bottom nav host
│   └── theme/                      # Colors, Typography, Theme
└── wifi/
    └── WifiScannerImpl.kt          # Wi-Fi scan + BSSID extraction
```

---

## ⚠️ Limitations

- Requires Android Wi-Fi scanning permission (location) — this is an Android platform requirement, not collected by this app
- Wi-Fi scan results may be throttled by Android (typically 4 scans / 2 minutes) on Android 9+
- Detection accuracy depends on registering the correct routers (main power only, no IPS/UPS)

---

## 📄 License

This project is for personal and educational use. All rights reserved © MD YEAMIN TALUKDER.

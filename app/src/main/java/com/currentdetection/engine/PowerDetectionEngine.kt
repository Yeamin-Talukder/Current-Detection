package com.currentdetection.engine

import com.currentdetection.domain.models.Network
import com.currentdetection.wifi.WifiScanner
import kotlinx.coroutines.flow.first

data class DetectionResult(
    val state: PowerState,
    val detectedBssids: Set<String>,
    val scanPerformed: Boolean
)

class PowerDetectionEngine(
    private val wifiScanner: WifiScanner
) {
    suspend fun detectPowerState(registeredNetworks: List<Network>): DetectionResult {
        if (registeredNetworks.isEmpty()) {
            return DetectionResult(PowerState.UNKNOWN, emptySet(), scanPerformed = false)
        }

        // 1. Check the connected Wi-Fi BSSID to save battery
        val connectedBssid = wifiScanner.getConnectedBssid()
        if (connectedBssid != null) {
            val isConnectedToTarget = registeredNetworks.any { it.bssid.equals(connectedBssid, ignoreCase = true) }
            if (isConnectedToTarget) {
                return DetectionResult(PowerState.POWER_ON, setOf(connectedBssid.lowercase()), scanPerformed = false)
            }
        }

        // 2. Not connected to a known network, perform a full scan
        val scanResults = wifiScanner.scanNearbyNetworks().first()
        
        // 3. Match scanned BSSIDs with registered BSSIDs
        val registeredBssids = registeredNetworks.map { it.bssid.lowercase() }
        val detectedBssids = scanResults.map { it.bssid.lowercase() }.intersect(registeredBssids.toSet())
        val foundMatch = detectedBssids.isNotEmpty()

        return if (foundMatch) {
            DetectionResult(PowerState.POWER_ON, detectedBssids, scanPerformed = true)
        } else {
            DetectionResult(PowerState.POSSIBLE_POWER_OFF, emptySet(), scanPerformed = true)
        }
    }
}

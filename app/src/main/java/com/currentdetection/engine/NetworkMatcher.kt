package com.currentdetection.engine

import com.currentdetection.domain.models.Network
import com.currentdetection.wifi.ScanResultItem

data class MatchResult(
    val detectedNetworks: List<Network>,
    val totalRegistered: Int,
    val detectionCount: Int
)

class NetworkMatcher {

    /**
     * Compares scanned BSSIDs against the enabled registered BSSIDs.
     * Performs case-insensitive matching for BSSIDs to ensure reliability.
     */
    fun match(scannedNetworks: List<ScanResultItem>, registeredNetworks: List<Network>): MatchResult {
        // Only consider networks that are marked as enabled by the user
        val enabledNetworks = registeredNetworks.filter { it.enabled }
        
        // Normalize scanned BSSIDs to uppercase
        val scannedBssids = scannedNetworks.map { it.bssid.uppercase() }.toSet()

        // Filter targeting networks by checking if their BSSID exists in the scan results
        val detected = enabledNetworks.filter { registered ->
            registered.bssid.uppercase() in scannedBssids
        }

        return MatchResult(
            detectedNetworks = detected,
            totalRegistered = enabledNetworks.size,
            detectionCount = detected.size
        )
    }
}

package com.currentdetection.wifi

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Build
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

data class ScanResultItem(
    val ssid: String,
    val bssid: String,
    val level: Int
)

interface WifiScanner {
    fun scanNearbyNetworks(): Flow<List<ScanResultItem>>
    fun getConnectedBssid(): String?
}

class WifiScannerImpl(private val context: Context) : WifiScanner {

    private val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    @Suppress("DEPRECATION")
    @SuppressLint("MissingPermission")
    override fun scanNearbyNetworks(): Flow<List<ScanResultItem>> = callbackFlow {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) {
                    val results = getScanResults()
                    trySend(results)
                }
            }
        }

        val filter = IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        
        // Android 14+ compatibility
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            context.registerReceiver(receiver, filter)
        }

        val success = wifiManager.startScan()
        if (!success) {
            // Scan failed to start (likely throttled), immediately send cached results
            trySend(getScanResults())
        }

        awaitClose {
            context.unregisterReceiver(receiver)
        }
    }

    @Suppress("DEPRECATION")
    private fun getScanResults(): List<ScanResultItem> {
        return try {
            wifiManager.scanResults.mapNotNull {
                // Return even hidden networks if BSSID is present, but keep filter for SSID if requested.
                // For "verdict" logic, BSSID is more reliable.
                val ssid = if (it.SSID.isNullOrBlank()) "Hidden Network" else it.SSID
                ScanResultItem(ssid, it.BSSID ?: "", it.level)
            }
        } catch (e: SecurityException) {
            emptyList()
        }
    }

    @Suppress("DEPRECATION")
    override fun getConnectedBssid(): String? {
        return try {
            val info = wifiManager.connectionInfo
            val bssid = info?.bssid
            if (bssid != null && bssid != "02:00:00:00:00:00" && bssid != "<unknown ssid>") {
                bssid
            } else {
                null
            }
        } catch (e: SecurityException) {
            null
        }
    }
}

package com.currentdetection.engine

import android.content.Context
import com.currentdetection.domain.repository.NetworkRepository
import com.currentdetection.wifi.WifiScanner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PowerMonitoringManager(
    private val context: Context,
    private val wifiScanner: WifiScanner,
    private val networkRepository: NetworkRepository,
    private val eventManager: EventManager,
    private val notificationManager: AppNotificationManager,
    private val networkMatcher: NetworkMatcher = NetworkMatcher(),
    private val powerStateEngine: PowerStateEngine = PowerStateEngine()
) {
    private var monitoringJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO)
    private var previousState: PowerState = PowerState.UNKNOWN

    fun startMonitoring() {
        if (monitoringJob?.isActive == true) return

        monitoringJob = scope.launch {
            // Observe the EventManager state to trigger notifications
            launch {
                eventManager.currentState.collect { state ->
                    if (previousState != state) {
                        if (state == PowerState.POWER_OFF && previousState != PowerState.UNKNOWN) {
                            notificationManager.showPowerOffAlert()
                        } else if (state == PowerState.POWER_ON && previousState == PowerState.POWER_OFF) {
                            notificationManager.showPowerOnAlert("Calculated duration") // We will update this later with actual duration
                        }
                        previousState = state
                    }
                }
            }

            // Polling loop to accommodate Wi-Fi scan throttling and scan initiation
            while (true) {
                performScan()
                delay(30_000) // 30 seconds interval between forced scans
            }
        }
    }

    fun stopMonitoring() {
        monitoringJob?.cancel()
        monitoringJob = null
    }

    private suspend fun performScan() {
        try {
            val registeredNetworks = networkRepository.getAllNetworks().first()
            if (registeredNetworks.isEmpty()) {
                eventManager.processNewState(PowerState.UNKNOWN)
                return
            }

            // For simplicity, we just collect one result and process it
            val scanResults = wifiScanner.scanNearbyNetworks().first()
            val matchResult = networkMatcher.match(scanResults, registeredNetworks)
            val newState = powerStateEngine.determineState(matchResult, scanSuccessful = true)

            eventManager.processNewState(
                newState = newState,
                activeCheckerCount = matchResult.detectionCount,
                totalCheckerCount = matchResult.totalRegistered
            )

        } catch (e: Exception) {
            eventManager.processNewState(PowerState.UNKNOWN)
        }
    }
}

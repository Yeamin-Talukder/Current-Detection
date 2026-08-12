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
    private val powerDetectionEngine: PowerDetectionEngine = PowerDetectionEngine(wifiScanner)
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
                performDetection()
                delay(30_000) // 30 seconds interval between checks
            }
        }
    }

    fun stopMonitoring() {
        monitoringJob?.cancel()
        monitoringJob = null
    }

    private suspend fun performDetection() {
        try {
            val registeredNetworks = networkRepository.getAllNetworks().first()
            if (registeredNetworks.isEmpty()) {
                eventManager.processNewState(PowerState.UNKNOWN)
                return
            }

            val detectionResult = powerDetectionEngine.detectPowerState(registeredNetworks)
            val detectedState = detectionResult.state
            
            // Map POSSIBLE_POWER_OFF to POWER_OFF for the event manager, which handles the confirmation delays
            val eventState = if (detectedState == PowerState.POSSIBLE_POWER_OFF) PowerState.POWER_OFF else detectedState

            eventManager.processNewState(
                newState = eventState,
                activeCheckerCount = detectionResult.detectedBssids.size,
                totalCheckerCount = registeredNetworks.size,
                detectedBssids = detectionResult.detectedBssids,
                scanPerformed = detectionResult.scanPerformed
            )

        } catch (e: Exception) {
            eventManager.processNewState(PowerState.UNKNOWN, detectedBssids = emptySet(), scanPerformed = false)
        }
    }
}

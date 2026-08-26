package com.currentdetection.engine

import android.content.Context
import com.currentdetection.data.local.SettingsManager
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
    private val settingsManager: SettingsManager,
    private val powerDetectionEngine: PowerDetectionEngine = PowerDetectionEngine(wifiScanner)
) {
    private var monitoringJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO)
    private var previousState: PowerState = PowerState.UNKNOWN

    fun startMonitoring() {
        if (monitoringJob?.isActive == true) return

        monitoringJob = scope.launch {
            // Restore persisted state before starting the loop
            // (handles process death and device reboot)
            eventManager.restorePersistedState()

            // If the app starts in Away Mode, post the persistent away notification
            // and wait for the user to return — don't do detection polls
            if (eventManager.isAwayMode.value) {
                notificationManager.showAwayModeNotification()
            }

            // Observe state changes to trigger notifications
            launch {
                eventManager.currentState.collect { state ->
                    if (previousState != state) {
                        when {
                            state == PowerState.POWER_OFF && previousState != PowerState.UNKNOWN -> {
                                notificationManager.showPowerOffAlert()
                            }
                            state == PowerState.POWER_ON && previousState == PowerState.POWER_OFF -> {
                                // Calculate and show power-restored notification with duration
                                val activeOutageDuration = try {
                                    // Duration is stored in the event; approximate from confirmed-on time
                                    val confirmedOn = eventManager.confirmedOnSinceMs.value
                                    if (confirmedOn > 0L) System.currentTimeMillis() - confirmedOn else 0L
                                } catch (e: Exception) { 0L }
                                notificationManager.showPowerOnAlert(formatDurationForNotification(activeOutageDuration))
                            }
                        }
                        previousState = state
                    }
                }
            }

            // Polling loop — respects Away Mode
            while (true) {
                val isAway = eventManager.isAwayMode.value
                val isMonitoringEnabled = settingsManager.monitoringEnabledFlow.first()

                when {
                    isAway -> {
                        // User is away — do nothing, just wait
                        settingsManager.setLastHeartbeatTime(System.currentTimeMillis())
                        delay(5_000)
                    }
                    isMonitoringEnabled -> {
                        settingsManager.setLastHeartbeatTime(System.currentTimeMillis())
                        performDetection()
                        delay(30_000)
                    }
                    else -> {
                        eventManager.processNewState(PowerState.UNKNOWN)
                        delay(30_000)
                    }
                }
            }
        }
    }

    fun stopMonitoring() {
        monitoringJob?.cancel()
        monitoringJob = null
    }

    /**
     * Called by the NotificationActionReceiver when the user taps "I'm Back".
     * Exits away mode and immediately performs a detection scan.
     */
    suspend fun handleReturnHome() {
        val awayDurationMs = eventManager.exitAwayMode()
        notificationManager.cancelAwayModeNotification()

        // Do an immediate detection scan instead of waiting 30s
        val isMonitoringEnabled = settingsManager.monitoringEnabledFlow.first()
        if (isMonitoringEnabled) {
            performDetection()
        }

        // Show the return summary notification
        if (awayDurationMs > 0L) {
            notificationManager.showReturnSummaryNotification(awayDurationMs)
        }
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

            // Map POSSIBLE_POWER_OFF to POWER_OFF for the event manager
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

    private fun formatDurationForNotification(ms: Long): String {
        val totalSeconds = ms / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        return if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
    }
}

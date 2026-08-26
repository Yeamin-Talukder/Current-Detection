package com.currentdetection.engine

import com.currentdetection.data.local.SettingsManager
import com.currentdetection.data.local.PowerEventDao
import com.currentdetection.data.local.entities.PowerEventEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first

class EventManager private constructor(
    private val powerEventDao: PowerEventDao,
    private val settingsManager: SettingsManager,
    private val powerOffConfirmationMs: Long = 30_000L,
    private val powerOnConfirmationMs: Long = 15_000L
) {
    companion object {
        @Volatile
        private var INSTANCE: EventManager? = null

        fun getInstance(powerEventDao: PowerEventDao, settingsManager: SettingsManager): EventManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: EventManager(powerEventDao, settingsManager).also { INSTANCE = it }
            }
        }
    }

    private val _currentState = MutableStateFlow(PowerState.UNKNOWN)
    val currentState: StateFlow<PowerState> = _currentState.asStateFlow()

    private val _detectedBssids = MutableStateFlow<Set<String>>(emptySet())
    val detectedBssids: StateFlow<Set<String>> = _detectedBssids.asStateFlow()

    private val _scanPerformed = MutableStateFlow(false)
    val scanPerformed: StateFlow<Boolean> = _scanPerformed.asStateFlow()

    /** Timestamp when power was last confirmed ON. 0L = unknown. */
    private val _confirmedOnSinceMs = MutableStateFlow(0L)
    val confirmedOnSinceMs: StateFlow<Long> = _confirmedOnSinceMs.asStateFlow()

    /** True while the user is explicitly away from home. */
    private val _isAwayMode = MutableStateFlow(false)
    val isAwayMode: StateFlow<Boolean> = _isAwayMode.asStateFlow()

    /** Timestamp when Away Mode started. 0L if not away. */
    private val _awayStartTimeMs = MutableStateFlow(0L)
    val awayStartTimeMs: StateFlow<Long> = _awayStartTimeMs.asStateFlow()

    // In-memory pending state (also persisted to DataStore for process-death survival)
    private var pendingState: PowerState? = null
    private var pendingStateStartTime: Long = 0L

    /**
     * Restores persisted state on EventManager creation.
     * Must be called once from a coroutine before the monitoring loop starts.
     */
    suspend fun restorePersistedState() {
        // Restore away mode
        val isAway = settingsManager.isAwayFlow.first()
        val awayStart = settingsManager.awayStartTimeFlow.first()
        _isAwayMode.value = isAway
        _awayStartTimeMs.value = awayStart

        // Restore last confirmed power state
        val confirmedStateName = settingsManager.confirmedStateNameFlow.first()
        val restoredState = when (confirmedStateName) {
            "POWER_ON" -> PowerState.POWER_ON
            "POWER_OFF" -> PowerState.POWER_OFF
            else -> PowerState.UNKNOWN
        }
        _currentState.value = restoredState

        // Restore pending state window so confirmation doesn't restart from scratch
        val pendingName = settingsManager.pendingStateNameFlow.first()
        val pendingStartTime = settingsManager.pendingStateStartTimeFlow.first()
        if (pendingName.isNotEmpty() && pendingStartTime > 0L) {
            pendingState = when (pendingName) {
                "POWER_ON" -> PowerState.POWER_ON
                "POWER_OFF" -> PowerState.POWER_OFF
                else -> null
            }
            pendingStateStartTime = pendingStartTime
        }

        // Handle completely unmonitored gaps (phone off, service killed)
        val lastHeartbeat = settingsManager.lastHeartbeatTimeFlow.first()
        val now = System.currentTimeMillis()
        if (lastHeartbeat > 0L && (now - lastHeartbeat) > 120_000L) {
            // Check if the app died during an active outage
            val activeOutage = powerEventDao.getActiveOutageEvent()
            if (activeOutage != null && activeOutage.startTime < lastHeartbeat) {
                // End the outage at the last known heartbeat
                powerEventDao.updateEvent(activeOutage.copy(endTime = lastHeartbeat, duration = lastHeartbeat - activeOutage.startTime))
            }
            
            // Check if there is already an active away gap
            val activeAway = powerEventDao.getActiveGapEvent()
            if (activeAway == null) {
                // Retroactively record this unmonitored time as an Away Period
                powerEventDao.insertEvent(PowerEventEntity(
                    startTime = lastHeartbeat,
                    endTime = now, // It ended just now
                    duration = now - lastHeartbeat,
                    detectedCheckerCount = 0,
                    totalCheckerCount = 0,
                    isUnknownGap = true
                ))
            }
            
            if (!isAway) {
                // If not explicitly away, reset states to force a fresh detection immediately
                pendingState = null
                pendingStateStartTime = 0L
                _currentState.value = PowerState.UNKNOWN
                settingsManager.setConfirmedStateName("UNKNOWN")
            }
        }

        // Restore last power-on time
        val savedOn = settingsManager.lastPowerOnTimeFlow.first()
        if (savedOn > 0L) {
            _confirmedOnSinceMs.value = savedOn
        }
    }

    suspend fun processNewState(
        newState: PowerState,
        currentTimeMs: Long = System.currentTimeMillis(),
        activeCheckerCount: Int = 0,
        totalCheckerCount: Int = 0,
        detectedBssids: Set<String> = emptySet(),
        scanPerformed: Boolean = false
    ) {
        _detectedBssids.value = detectedBssids
        _scanPerformed.value = scanPerformed
        val current = _currentState.value

        if (newState == current) {
            // State remains the same, clear any pending transitions
            pendingState = null
            pendingStateStartTime = 0L
            settingsManager.clearPendingState()
            return
        }

        if (newState == PowerState.UNKNOWN) {
            // Transitions to UNKNOWN are handled immediately (failure to monitor)
            _currentState.value = PowerState.UNKNOWN
            pendingState = null
            pendingStateStartTime = 0L
            settingsManager.clearPendingState()
            settingsManager.setConfirmedStateName("UNKNOWN")
            return
        }

        // If the current state is UNKNOWN, transition to first known state immediately
        if (current == PowerState.UNKNOWN) {
            _currentState.value = newState
            pendingState = null
            pendingStateStartTime = 0L
            settingsManager.clearPendingState()
            settingsManager.setConfirmedStateName(newState.name)
            handleStateTransition(current, newState, currentTimeMs, activeCheckerCount, totalCheckerCount)
            return
        }

        // If we have a pending state but the new state contradicts it, reset the pending state
        if (pendingState != newState) {
            pendingState = newState
            pendingStateStartTime = currentTimeMs
            // Persist immediately so it survives process death
            settingsManager.setPendingStateName(newState.name)
            settingsManager.setPendingStateStartTime(currentTimeMs)
        }

        val elapsedTime = currentTimeMs - pendingStateStartTime
        val confirmationPeriod = if (newState == PowerState.POWER_OFF) powerOffConfirmationMs else powerOnConfirmationMs

        if (elapsedTime >= confirmationPeriod) {
            val previousState = _currentState.value
            _currentState.value = newState
            pendingState = null
            pendingStateStartTime = 0L
            settingsManager.clearPendingState()
            settingsManager.setConfirmedStateName(newState.name)
            handleStateTransition(previousState, newState, currentTimeMs, activeCheckerCount, totalCheckerCount)
        }
    }

    private suspend fun handleStateTransition(
        previousState: PowerState,
        newState: PowerState,
        currentTimeMs: Long,
        activeCheckerCount: Int,
        totalCheckerCount: Int
    ) {
        if (newState == PowerState.POWER_OFF) {
            // Start of a new confirmed outage — only if no active outage event already exists
            val activeEvent = powerEventDao.getActiveOutageEvent()
            if (activeEvent == null) {
                val newEvent = PowerEventEntity(
                    startTime = currentTimeMs,
                    endTime = null,
                    duration = null,
                    detectedCheckerCount = activeCheckerCount,
                    totalCheckerCount = totalCheckerCount,
                    isUnknownGap = false
                )
                powerEventDao.insertEvent(newEvent)
            }
        } else if (newState == PowerState.POWER_ON) {
            // End of a confirmed outage
            val activeEvent = powerEventDao.getActiveOutageEvent()
            if (activeEvent != null) {
                val duration = currentTimeMs - activeEvent.startTime
                val updatedEvent = activeEvent.copy(
                    endTime = currentTimeMs,
                    duration = duration
                )
                powerEventDao.updateEvent(updatedEvent)
            }
            // Record when power was confirmed ON
            _confirmedOnSinceMs.value = currentTimeMs
            settingsManager.setLastPowerOnTime(currentTimeMs)
        }
    }

    // ─── Away Mode ─────────────────────────────────────────────────────────────

    /**
     * Called when the user explicitly leaves home.
     * Records an open-ended "unknown gap" event and pauses monitoring context.
     * [startTimeMs] is when the user actually tapped "I left home".
     */
    suspend fun enterAwayMode(startTimeMs: Long = System.currentTimeMillis()) {
        // Close any active confirmed outage as a gap instead (we don't know what happened)
        val activeOutage = powerEventDao.getActiveOutageEvent()
        if (activeOutage != null) {
            // Convert the outage to an unknown gap — we can't confirm it was real power-off since user left
            powerEventDao.updateEvent(
                activeOutage.copy(isUnknownGap = true)
            )
        } else {
            // No active outage — just record a new gap event starting now
            powerEventDao.insertEvent(
                PowerEventEntity(
                    startTime = startTimeMs,
                    endTime = null,
                    duration = null,
                    detectedCheckerCount = 0,
                    totalCheckerCount = 0,
                    isUnknownGap = true
                )
            )
        }

        // Persist away state
        settingsManager.setAwayMode(true, startTimeMs)
        settingsManager.setMonitoringEnabled(false)

        // Update in-memory
        _isAwayMode.value = true
        _awayStartTimeMs.value = startTimeMs
        _currentState.value = PowerState.UNKNOWN

        // Clear pending confirmation windows — not relevant while away
        pendingState = null
        pendingStateStartTime = 0L
        settingsManager.clearPendingState()
        settingsManager.setConfirmedStateName("UNKNOWN")
    }

    /**
     * Called when the user explicitly returns home.
     * Closes the open gap event and re-enables monitoring.
     * Returns the duration (ms) of the away period, for the return summary notification.
     */
    suspend fun exitAwayMode(returnTimeMs: Long = System.currentTimeMillis()): Long {
        val awayStart = _awayStartTimeMs.value.takeIf { it > 0L }
            ?: settingsManager.awayStartTimeFlow.first()

        val awayDuration = if (awayStart > 0L) returnTimeMs - awayStart else 0L

        // Close the open gap event
        val activeGap = powerEventDao.getActiveGapEvent()
        if (activeGap != null) {
            powerEventDao.updateEvent(
                activeGap.copy(
                    endTime = returnTimeMs,
                    duration = returnTimeMs - activeGap.startTime
                )
            )
        }

        // Clear away state and re-enable monitoring
        settingsManager.setAwayMode(false)
        settingsManager.setMonitoringEnabled(true)

        // Update in-memory
        _isAwayMode.value = false
        _awayStartTimeMs.value = 0L

        return awayDuration
    }

    suspend fun cancelActiveOutageAsUnknown() {
        val activeEvent = powerEventDao.getActiveOutageEvent()
        if (activeEvent != null) {
            powerEventDao.deleteEvent(activeEvent)
        }
        _currentState.value = PowerState.UNKNOWN
        pendingState = null
        pendingStateStartTime = 0L
        settingsManager.clearPendingState()
        settingsManager.setConfirmedStateName("UNKNOWN")
    }
}

package com.currentdetection.engine

import com.currentdetection.data.local.PowerEventDao
import com.currentdetection.data.local.SettingsManager
import com.currentdetection.data.local.entities.PowerEventEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EventManager private constructor(
    private val powerEventDao: PowerEventDao,
    private val settingsManager: SettingsManager? = null,
    private val powerOffConfirmationMs: Long = 30_000L,
    private val powerOnConfirmationMs: Long = 15_000L
) {
    companion object {
        @Volatile
        private var INSTANCE: EventManager? = null

        fun getInstance(powerEventDao: PowerEventDao, settingsManager: SettingsManager? = null): EventManager {
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

    private var pendingState: PowerState? = null
    private var pendingStateStartTime: Long = 0L

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
            return
        }

        if (newState == PowerState.UNKNOWN) {
            // Transitions to UNKNOWN are handled immediately as they represent a failure to monitor
            _currentState.value = PowerState.UNKNOWN
            pendingState = null
            pendingStateStartTime = 0L
            return
        }

        // If the current state is UNKNOWN, we transition to the first known state immediately
        // to make the app feel functional as soon as the first successful scan completes.
        if (current == PowerState.UNKNOWN) {
            _currentState.value = newState
            pendingState = null
            pendingStateStartTime = 0L
            handleStateTransition(current, newState, currentTimeMs, activeCheckerCount, totalCheckerCount)
            return
        }

        // If we have a pending state but the new state contradicts it, reset the pending state
        if (pendingState != newState) {
            pendingState = newState
            pendingStateStartTime = currentTimeMs
        }

        val elapsedTime = currentTimeMs - pendingStateStartTime
        val confirmationPeriod = if (newState == PowerState.POWER_OFF) powerOffConfirmationMs else powerOnConfirmationMs

        if (elapsedTime >= confirmationPeriod) {
            // Confirm the state transition after the required observation period
            val previousState = _currentState.value
            _currentState.value = newState
            pendingState = null
            pendingStateStartTime = 0L

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
            // Start of a new outage
            val activeEvent = powerEventDao.getActiveOutageEvent()
            if (activeEvent == null) {
                val newEvent = PowerEventEntity(
                    startTime = currentTimeMs,
                    endTime = null,
                    duration = null,
                    detectedCheckerCount = activeCheckerCount,
                    totalCheckerCount = totalCheckerCount
                )
                powerEventDao.insertEvent(newEvent)
            }
        } else if (newState == PowerState.POWER_ON) {
            // End of an outage
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
            settingsManager?.setLastPowerOnTime(currentTimeMs)
        }
    }
}

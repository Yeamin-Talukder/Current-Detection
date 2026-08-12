package com.currentdetection.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currentdetection.data.local.PowerEventDao
import com.currentdetection.data.local.SettingsManager
import com.currentdetection.data.local.entities.NetworkEntity
import com.currentdetection.data.local.entities.PowerEventEntity
import com.currentdetection.domain.repository.NetworkRepository
import com.currentdetection.engine.EventManager
import com.currentdetection.engine.PowerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

data class PowerStats(
    val availabilityPercentage: Float = 0f,
    val totalOnTimeMs: Long = 0L,
    val totalOffTimeMs: Long = 0L,
    val outageCount: Int = 0,
    val longestOutageMs: Long = 0L,
    val averageOutageMs: Long = 0L,
    val peakOutagePeriod: String = "N/A"
)

enum class NetworkScanState {
    ACTIVE,
    OFFLINE,
    NOT_SCANNED
}

data class NetworkStatus(
    val name: String,
    val state: NetworkScanState
) {
    val isActive: Boolean get() = state == NetworkScanState.ACTIVE
}

data class OnSession(
    val startMs: Long,
    val endMs: Long
) {
    val durationMs: Long get() = endMs - startMs
}

// Describes the multi-step scan animation shown to the user
sealed class ScanPhase {
    object Idle : ScanPhase()
    object CheckingConnected : ScanPhase()
    object ScanningNearby : ScanPhase()
    data class MatchingBssids(val networks: List<NetworkEntity>) : ScanPhase()
    object Done : ScanPhase()
}

class HomeViewModel(
    private val eventManager: EventManager,
    private val powerEventDao: PowerEventDao,
    private val networkRepository: NetworkRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {

    val powerState: StateFlow<PowerState> = eventManager.currentState

    val isMonitoringEnabled: StateFlow<Boolean> = settingsManager.monitoringEnabledFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val activeOutageEvent: StateFlow<PowerEventEntity?> = powerEventDao.getActiveOutageEventFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val registeredNetworks: StateFlow<List<NetworkEntity>> = networkRepository.getAllNetworks()
        .map { it.map { domain -> NetworkEntity(id = domain.id, displayName = domain.displayName, ssid = domain.ssid, bssid = domain.bssid, enabled = domain.enabled, createdAt = domain.createdAt) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _scanCountdown = MutableStateFlow(30)
    val scanCountdown = _scanCountdown.asStateFlow()

    private val _scanPhase = MutableStateFlow<ScanPhase>(ScanPhase.Idle)
    val scanPhase = _scanPhase.asStateFlow()

    // Derived for backward compat
    val isScanning: StateFlow<Boolean> = _scanPhase
        .map { it !is ScanPhase.Idle && it !is ScanPhase.Done }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _activeOutageDurationMs = MutableStateFlow(0L)
    val activeOutageDurationMs = _activeOutageDurationMs.asStateFlow()

    val lastPowerOnTime: StateFlow<Long> = settingsManager.lastPowerOnTimeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    val firstRunTime: StateFlow<Long> = settingsManager.firstRunTimeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    val networkBreakdown: StateFlow<List<NetworkStatus>> = combine(
        registeredNetworks,
        eventManager.detectedBssids,
        eventManager.scanPerformed
    ) { networks, detected, scanPerformed ->
        networks.map { network ->
            val bssidLower = network.bssid.lowercase()
            val state = when {
                detected.contains(bssidLower) -> NetworkScanState.ACTIVE
                !scanPerformed -> NetworkScanState.NOT_SCANNED
                else -> NetworkScanState.OFFLINE
            }
            NetworkStatus(network.displayName, state)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val todayEvents: StateFlow<List<PowerEventEntity>> = powerEventDao.getAllEvents()
        .map { events ->
            val todayStart = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
            events.filter { it.startTime >= todayStart || (it.endTime ?: Long.MAX_VALUE) >= todayStart }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val powerStats: StateFlow<PowerStats> = todayEvents.map { events ->
        calculateStats(events)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PowerStats())

    /** Recent power-ON sessions (gaps between outages) */
    val recentOnSessions: StateFlow<List<OnSession>> = todayEvents.map { events ->
        buildOnSessions(events)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Ensure first-run time is persisted
        viewModelScope.launch {
            settingsManager.initFirstRunTime()
            // Restore last power-on time from prefs into EventManager if not yet set
            val saved = settingsManager.lastPowerOnTimeFlow.first()
            if (saved > 0L && eventManager.confirmedOnSinceMs.value == 0L) {
                // Reflect saved value (EventManager is in-memory; populate from prefs on boot)
                // We expose it indirectly via the duration timer below
            }
        }

        // Countdown + multi-phase scan animation loop
        viewModelScope.launch {
            while (true) {
                if (_scanCountdown.value > 0) {
                    _scanCountdown.value -= 1
                    delay(1000)
                } else {
                    // Play multi-phase animation
                    val networks = registeredNetworks.value

                    _scanPhase.value = ScanPhase.CheckingConnected
                    delay(700)

                    _scanPhase.value = ScanPhase.ScanningNearby
                    delay(1200)

                    _scanPhase.value = ScanPhase.MatchingBssids(networks)
                    delay(1400)

                    _scanPhase.value = ScanPhase.Done
                    delay(400)

                    _scanPhase.value = ScanPhase.Idle
                    _scanCountdown.value = 30
                }
            }
        }

        // Live duration timer
        // Live duration timer
        viewModelScope.launch {
            while (true) {
                val activeEvent = activeOutageEvent.value
                val currentState = powerState.value

                if (currentState == PowerState.POWER_OFF) {
                    if (activeEvent != null) {
                        _activeOutageDurationMs.value = System.currentTimeMillis() - activeEvent.startTime
                    } else {
                        // Fallback: if flow hasn't emitted yet but we are POWER_OFF, query DB directly
                        val directActiveEvent = powerEventDao.getActiveOutageEvent()
                        if (directActiveEvent != null) {
                            _activeOutageDurationMs.value = System.currentTimeMillis() - directActiveEvent.startTime
                        } else {
                            _activeOutageDurationMs.value = 0L
                        }
                    }
                } else if (currentState == PowerState.POWER_ON) {
                    // Priority: use EventManager's in-memory confirmed timestamp,
                    // then SettingsManager persisted value
                    val confirmedOn = eventManager.confirmedOnSinceMs.value
                    if (confirmedOn > 0L) {
                        _activeOutageDurationMs.value = System.currentTimeMillis() - confirmedOn
                    } else {
                        val savedOn = lastPowerOnTime.value
                        if (savedOn > 0L) {
                            _activeOutageDurationMs.value = System.currentTimeMillis() - savedOn
                        } else {
                            _activeOutageDurationMs.value = 0L
                        }
                    }
                } else {
                    _activeOutageDurationMs.value = 0L
                }
                delay(1000)
            }
        }
    }

    private fun buildOnSessions(events: List<PowerEventEntity>): List<OnSession> {
        if (events.isEmpty()) return emptyList()
        val now = System.currentTimeMillis()
        val todayStart = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val startOfMonitoringToday = maxOf(todayStart, firstRunTime.value)

        val completedOutages = events
            .filter { it.endTime != null }
            .sortedBy { it.startTime }

        val sessions = mutableListOf<OnSession>()

        // ON session before first outage
        if (completedOutages.isNotEmpty()) {
            val firstOutageStart = completedOutages.first().startTime
            if (firstOutageStart > startOfMonitoringToday) {
                sessions.add(OnSession(startOfMonitoringToday, firstOutageStart))
            }
        }

        // ON sessions between consecutive outages
        for (i in 0 until completedOutages.size - 1) {
            val end = completedOutages[i].endTime!!
            val nextStart = completedOutages[i + 1].startTime
            if (nextStart > end) {
                sessions.add(OnSession(end, nextStart))
            }
        }

        // ON session after last completed outage (up to now, if power is currently ON)
        if (completedOutages.isNotEmpty()) {
            val lastEnd = completedOutages.last().endTime!!
            if (powerState.value == PowerState.POWER_ON) {
                sessions.add(OnSession(lastEnd, now))
            }
        }

        return sessions.sortedByDescending { it.endMs }.take(5)
    }

    private fun calculateStats(events: List<PowerEventEntity>): PowerStats {
        val now = System.currentTimeMillis()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val todayStart = calendar.timeInMillis

        val startOfMonitoringToday = maxOf(todayStart, firstRunTime.value)
        val totalTimePassedToday = now - startOfMonitoringToday
        var totalOffTime = 0L
        var longestOutage = 0L

        events.forEach { event ->
            val start = maxOf(event.startTime, todayStart)
            val end = minOf(event.endTime ?: now, now)
            val duration = end - start
            if (duration > 0) {
                totalOffTime += duration
                if (duration > longestOutage) longestOutage = duration
            }
        }

        val totalOnTime = maxOf(0, totalTimePassedToday - totalOffTime)
        val availability = if (totalTimePassedToday > 0) (totalOnTime.toFloat() / totalTimePassedToday.toFloat()) * 100f else 100f

        return PowerStats(
            availabilityPercentage = availability,
            totalOnTimeMs = totalOnTime,
            totalOffTimeMs = totalOffTime,
            outageCount = events.size,
            longestOutageMs = longestOutage,
            averageOutageMs = if (events.isNotEmpty()) totalOffTime / events.size else 0L,
            peakOutagePeriod = "6 PM - 11 PM"
        )
    }
}

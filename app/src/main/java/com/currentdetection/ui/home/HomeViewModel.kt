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
    val totalAwayTimeMs: Long = 0L,
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

    // ─── Away Mode ────────────────────────────────────────────────────────────

    /** True while the user is explicitly away from home. */
    val isAwayMode: StateFlow<Boolean> = eventManager.isAwayMode

    /** Timestamp when Away Mode was activated. 0L if not away. */
    val awayStartTime: StateFlow<Long> = eventManager.awayStartTimeMs

    /** Live counter — how many milliseconds the user has been away. */
    private val _awayDurationMs = MutableStateFlow(0L)
    val awayDurationMs: StateFlow<Long> = _awayDurationMs.asStateFlow()

    // ─── Events & State ───────────────────────────────────────────────────────

    val activeOutageEvent: StateFlow<PowerEventEntity?> = powerEventDao.getActiveOutageEventFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val activeGapEvent: StateFlow<PowerEventEntity?> = powerEventDao.getActiveGapEventFlow()
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

    /** All events today (outages + away gaps combined for timeline rendering). */
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

    /** Only confirmed outage events (no away gaps) — used for stats and outage list. */
    val todayOutages: StateFlow<List<PowerEventEntity>> = todayEvents
        .map { events -> events.filter { !it.isUnknownGap } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val powerStats: StateFlow<PowerStats> = todayEvents.map { events ->
        calculateStats(events)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PowerStats())

    /** Recent power-ON sessions (gaps between outages and away periods) */
    val recentOnSessions: StateFlow<List<OnSession>> = todayEvents.map { events ->
        buildOnSessions(events)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Ensure first-run time is persisted
        viewModelScope.launch {
            settingsManager.initFirstRunTime()
            val saved = settingsManager.lastPowerOnTimeFlow.first()
            if (saved > 0L && eventManager.confirmedOnSinceMs.value == 0L) {
                // Reflect saved value (EventManager is in-memory; populate from prefs on boot)
            }
        }

        // Countdown + multi-phase scan animation loop
        viewModelScope.launch {
            while (true) {
                val isAway = isAwayMode.value
                if (!isMonitoringEnabled.value || isAway) {
                    _scanPhase.value = ScanPhase.Idle
                    delay(1000)
                    continue
                }

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

        // Live outage duration timer
        viewModelScope.launch {
            while (true) {
                val activeEvent = activeOutageEvent.value
                val currentState = powerState.value

                if (currentState == PowerState.POWER_OFF) {
                    if (activeEvent != null) {
                        _activeOutageDurationMs.value = System.currentTimeMillis() - activeEvent.startTime
                    } else {
                        val directActiveEvent = powerEventDao.getActiveOutageEvent()
                        if (directActiveEvent != null) {
                            _activeOutageDurationMs.value = System.currentTimeMillis() - directActiveEvent.startTime
                        } else {
                            _activeOutageDurationMs.value = 0L
                        }
                    }
                } else if (currentState == PowerState.POWER_ON) {
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

        // Live away duration timer
        viewModelScope.launch {
            while (true) {
                val awayStart = awayStartTime.value
                if (isAwayMode.value && awayStart > 0L) {
                    _awayDurationMs.value = System.currentTimeMillis() - awayStart
                } else {
                    _awayDurationMs.value = 0L
                }
                delay(1000)
            }
        }
    }

    // ─── Away Mode Actions ────────────────────────────────────────────────────

    fun enterAwayMode() {
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            eventManager.enterAwayMode()
        }
    }

    fun returnHome() {
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            eventManager.exitAwayMode()
            // Reset countdown so the next scan happens right after the animation
            _scanCountdown.value = 3
        }
    }

    // ─── Legacy / Other Actions ───────────────────────────────────────────────

    fun markCurrentOutageAsUnknown() {
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            // Prefer entering Away Mode now instead of just cancelling the outage
            eventManager.enterAwayMode()
        }
    }

    fun startMonitoring() {
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            settingsManager.setMonitoringEnabled(true)
        }
    }

    // ─── Stats Helpers ────────────────────────────────────────────────────────

    private fun buildOnSessions(events: List<PowerEventEntity>): List<OnSession> {
        val now = System.currentTimeMillis()
        val todayStart = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val startOfMonitoringToday = maxOf(todayStart, firstRunTime.value)
        val sessions = mutableListOf<OnSession>()

        if (events.isEmpty()) {
            if (powerState.value == PowerState.POWER_ON && now > startOfMonitoringToday) {
                sessions.add(OnSession(startOfMonitoringToday, now))
            }
            return sessions
        }

        val completedOutages = events
            .filter { it.endTime != null }
            .sortedBy { it.startTime }

        // ON session before first outage
        if (completedOutages.isNotEmpty()) {
            val firstOutageStart = completedOutages.first().startTime
            if (firstOutageStart > startOfMonitoringToday) {
                sessions.add(OnSession(startOfMonitoringToday, firstOutageStart))
            }
        } else {
            val firstEvent = events.minByOrNull { it.startTime }
            if (firstEvent != null && firstEvent.startTime > startOfMonitoringToday) {
                sessions.add(OnSession(startOfMonitoringToday, firstEvent.startTime))
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
        val activeEvent = events.firstOrNull { it.endTime == null }
        if (completedOutages.isNotEmpty()) {
            val lastEnd = completedOutages.last().endTime!!
            if (activeEvent == null && powerState.value == PowerState.POWER_ON && now > lastEnd) {
                sessions.add(OnSession(lastEnd, now))
            }
        } else if (activeEvent != null && activeEvent.startTime > startOfMonitoringToday) {
            // There's only an active event, and we added the session before it.
        } else if (activeEvent == null && powerState.value == PowerState.POWER_ON) {
            // Should be covered by events.isEmpty, but just in case
            val maxEnd = events.mapNotNull { it.endTime }.maxOrNull() ?: startOfMonitoringToday
            if (now > maxEnd) sessions.add(OnSession(maxEnd, now))
        }

        return sessions.sortedByDescending { it.endMs } // Return all
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
        var totalAwayTime = 0L
        var longestOutage = 0L
        var outageCount = 0

        events.forEach { event ->
            val start = maxOf(event.startTime, todayStart)
            val end = minOf(event.endTime ?: now, now)
            val duration = end - start
            if (duration > 0) {
                if (event.isUnknownGap) {
                    totalAwayTime += duration
                } else {
                    totalOffTime += duration
                    if (duration > longestOutage) longestOutage = duration
                    outageCount++
                }
            }
        }

        val totalMonitoredTime = maxOf(0L, totalTimePassedToday - totalAwayTime)
        val totalOnTime = maxOf(0L, totalMonitoredTime - totalOffTime)
        val availability = if (totalMonitoredTime > 0) (totalOnTime.toFloat() / totalMonitoredTime.toFloat()) * 100f else 100f

        return PowerStats(
            availabilityPercentage = availability,
            totalOnTimeMs = totalOnTime,
            totalOffTimeMs = totalOffTime,
            totalAwayTimeMs = totalAwayTime,
            outageCount = outageCount,
            longestOutageMs = longestOutage,
            averageOutageMs = if (outageCount > 0) totalOffTime / outageCount else 0L,
            peakOutagePeriod = "6 PM - 11 PM"
        )
    }
}

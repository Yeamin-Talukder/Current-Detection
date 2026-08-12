package com.currentdetection.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currentdetection.data.local.PowerEventDao
import com.currentdetection.data.local.SettingsManager
import com.currentdetection.data.local.entities.NetworkEntity
import com.currentdetection.data.local.entities.PowerEventEntity
import com.currentdetection.domain.repository.NetworkRepository
import com.currentdetection.engine.EventManager
import com.currentdetection.engine.NetworkMatcher
import com.currentdetection.engine.PowerState
import com.currentdetection.wifi.WifiScanner
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
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

data class NetworkStatus(
    val name: String,
    val isActive: Boolean
)

class HomeViewModel(
    private val eventManager: EventManager,
    private val powerEventDao: PowerEventDao,
    private val networkRepository: NetworkRepository,
    private val settingsManager: SettingsManager,
    private val wifiScanner: WifiScanner,
    private val networkMatcher: NetworkMatcher = NetworkMatcher()
) : ViewModel() {

    // Central source of truth for power state
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

    private val _isScanning = MutableStateFlow(false)
    val isScanning = _isScanning.asStateFlow()

    private val _activeOutageDurationMs = MutableStateFlow(0L)
    val activeOutageDurationMs = _activeOutageDurationMs.asStateFlow()

    private val _lastDetectedBssids = MutableStateFlow<Set<String>>(emptySet())
    private val _hasPerformedScan = MutableStateFlow(false)
    
    /**
     * VERDICT LOGIC:
     * Combines registered networks with the latest scan results to determine 
     * the status of each individual router/checker.
     */
    val networkBreakdown: StateFlow<List<NetworkStatus>> = combine(
        registeredNetworks,
        _lastDetectedBssids,
        _hasPerformedScan,
        powerState
    ) { networks, detected, hasScanned, state ->
        networks.map { network ->
            val isActive = if (hasScanned) {
                // Use normalized BSSID comparison for verdict
                network.bssid.uppercase() in detected
            } else {
                // Before first scan, use global state as fallback
                state == PowerState.POWER_ON
            }
            NetworkStatus(network.displayName, isActive)
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

    init {
        // Monitoring loop: Runs the countdown and triggers the functional scan
        viewModelScope.launch {
            while (true) {
                if (_scanCountdown.value > 0) {
                    _scanCountdown.value -= 1
                    delay(1000)
                } else {
                    performFunctionalScan()
                    _scanCountdown.value = 30
                }
            }
        }

        // Live timer for active outages
        viewModelScope.launch {
            while (true) {
                val activeEvent = activeOutageEvent.value
                val currentState = powerState.value

                if (currentState == PowerState.POWER_OFF && activeEvent != null) {
                    _activeOutageDurationMs.value = System.currentTimeMillis() - activeEvent.startTime
                } else {
                    _activeOutageDurationMs.value = 0L
                }
                delay(1000)
            }
        }
    }

    /**
     * Functional Scan Process:
     * 1. Fetches Targeting Networks (what we are looking for).
     * 2. Scans Environment (Wi-Fi scanning).
     * 3. Compares Results (Matching logic).
     * 4. Issues Verdict (Updates UI and Global State).
     */
    private suspend fun performFunctionalScan() {
        _isScanning.value = true
        try {
            // 1. Get Targeting Networks from Repository
            val targetingNetworks = networkRepository.getAllNetworks().first()
            if (targetingNetworks.isEmpty()) {
                eventManager.processNewState(PowerState.UNKNOWN)
                return
            }

            // 2. Perform Environment Scan
            // Wait for first emission from scanner with 15s timeout
            val environmentNetworks = withTimeoutOrNull(15000) {
                wifiScanner.scanNearbyNetworks().first()
            } ?: emptyList()

            // 3. Compare targeting vs environment
            val matchResult = networkMatcher.match(environmentNetworks, targetingNetworks)
            
            // 4. Update individual verdicts (Normalized to Uppercase)
            val detectedBssids = matchResult.detectedNetworks.map { it.bssid.uppercase() }.toSet()
            _lastDetectedBssids.value = detectedBssids
            _hasPerformedScan.value = true
            
            // 5. Update global state verdict
            val newState = if (matchResult.detectionCount > 0) PowerState.POWER_ON else PowerState.POWER_OFF
            
            // Central engine handles persistence and confirmation (anti-flap)
            eventManager.processNewState(
                newState = newState,
                activeCheckerCount = matchResult.detectionCount,
                totalCheckerCount = matchResult.totalRegistered
            )
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            _isScanning.value = false
        }
    }

    private fun calculateStats(events: List<PowerEventEntity>): PowerStats {
        val now = System.currentTimeMillis()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val todayStart = calendar.timeInMillis
        
        val totalTimePassedToday = now - todayStart
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

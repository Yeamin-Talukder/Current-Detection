package com.currentdetection.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currentdetection.data.local.PowerEventDao
import com.currentdetection.data.local.entities.PowerEventEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar

data class StatisticsData(
    val outagesCount: Int,
    val totalOffMs: Long,
    val totalOnMs: Long,
    val averageOutageMs: Long,
    val longestOutageMs: Long,
    val availabilityPercent: Int,
    val weeklyChartData: List<Float> // 7 days (Mon-Sun) hours/mins mapped to floats
)

class StatisticsViewModel(
    powerEventDao: PowerEventDao
) : ViewModel() {

    val statsToday: StateFlow<StatisticsData?> = powerEventDao.getAllEvents()
        .map { calculateStats(it, "Today") }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val statsWeekly: StateFlow<StatisticsData?> = powerEventDao.getAllEvents()
        .map { calculateStats(it, "Weekly") }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val statsMonthly: StateFlow<StatisticsData?> = powerEventDao.getAllEvents()
        .map { calculateStats(it, "Monthly") }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private fun calculateStats(events: List<PowerEventEntity>, period: String): StatisticsData? {
        if (events.isEmpty()) return null

        val calendar = Calendar.getInstance()
        val now = calendar.timeInMillis
        
        when (period) {
            "Today" -> {
                calendar.set(Calendar.HOUR_OF_DAY, 0); calendar.set(Calendar.MINUTE, 0); calendar.set(Calendar.SECOND, 0)
            }
            "Weekly" -> {
                calendar.add(Calendar.DAY_OF_YEAR, -7)
            }
            "Monthly" -> {
                calendar.add(Calendar.DAY_OF_YEAR, -30)
            }
        }
        val startTime = calendar.timeInMillis
        
        val filteredEvents = events.filter { it.startTime >= startTime }
        if (filteredEvents.isEmpty()) return null

        var totalOff = 0L
        var longest = 0L
        filteredEvents.forEach { event ->
            val dur = event.duration ?: (now - event.startTime)
            totalOff += dur
            if (dur > longest) longest = dur
        }
        
        val avg = if (filteredEvents.isNotEmpty()) totalOff / filteredEvents.size else 0L
        val monitoredTime = now - startTime
        val totalOn = monitoredTime - totalOff
        val availability = if (monitoredTime > 0) ((totalOn.toDouble() / monitoredTime) * 100).toInt() else 100
        
        // Mocking weekly chart data for 7 days based on the total filtered for demo
        val chartData = listOf(
            (totalOff * 0.1f).toFloat(), (totalOff * 0.2f).toFloat(), 
            (totalOff * 0.15f).toFloat(), (totalOff * 0.05f).toFloat(), 
            (totalOff * 0.3f).toFloat(), 0f, (totalOff * 0.2f).toFloat()
        )

        return StatisticsData(
            outagesCount = filteredEvents.size,
            totalOffMs = totalOff,
            totalOnMs = totalOn,
            averageOutageMs = avg,
            longestOutageMs = longest,
            availabilityPercent = availability,
            weeklyChartData = chartData
        )
    }
}

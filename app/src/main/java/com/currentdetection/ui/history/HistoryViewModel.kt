package com.currentdetection.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currentdetection.data.local.PowerEventDao
import com.currentdetection.data.local.SettingsManager
import com.currentdetection.data.local.entities.PowerEventEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class DailyReport(
    val dateLabel: String,          // e.g. "Today", "Yesterday", "12 Aug"
    val dateMs: Long,               // midnight of that day
    val outages: List<PowerEventEntity>,
    val totalOutageMs: Long,
    val monitoredMs: Long,          // actual monitored time that day (excludes pre-install)
    val totalOnTimeMs: Long,
    val availabilityPct: Float,
    val isFirstDay: Boolean         // day of first install
)

class HistoryViewModel(
    private val powerEventDao: PowerEventDao,
    private val settingsManager: SettingsManager
) : ViewModel() {

    val dailyReports: StateFlow<List<DailyReport>> = combine(
        powerEventDao.getAllEvents(),
        settingsManager.firstRunTimeFlow
    ) { events, firstRunTime ->
        buildDailyReports(events, firstRunTime)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private fun buildDailyReports(
        events: List<PowerEventEntity>,
        firstRunTime: Long
    ): List<DailyReport> {
        if (events.isEmpty() && firstRunTime == 0L) return emptyList()

        val now = System.currentTimeMillis()
        val dayFormat = SimpleDateFormat("d MMM", Locale.getDefault())

        // Determine the range of days: from first event or firstRunTime to today
        val earliest = minOf(
            events.minOfOrNull { it.startTime } ?: now,
            if (firstRunTime > 0L) firstRunTime else now
        )
        val earliestDayStart = getDayStart(earliest)
        val todayStart = getDayStart(now)

        // Build a list of all days from earliest to today
        val days = mutableListOf<Long>()
        var cursor = earliestDayStart
        while (cursor <= todayStart) {
            days.add(cursor)
            cursor += 86_400_000L // +1 day
        }

        return days.reversed().map { dayStart ->
            val dayEnd = dayStart + 86_400_000L
            val dayNow = minOf(dayEnd, now)

            // First day of install — monitored time starts at firstRunTime
            val isFirstDay = firstRunTime in dayStart until dayEnd
            val monitoringStart = if (isFirstDay && firstRunTime > dayStart) firstRunTime else dayStart
            val monitoredMs = maxOf(0L, dayNow - monitoringStart)

            // Outages in this day (completed only for history)
            val dayOutages = events.filter { event ->
                val evEnd = event.endTime ?: Long.MAX_VALUE
                event.startTime < dayEnd && evEnd > dayStart && event.endTime != null
            }.sortedBy { it.startTime }

            var totalOutageMs = 0L
            dayOutages.forEach { event ->
                val start = maxOf(event.startTime, dayStart)
                val end = minOf(event.endTime!!, dayNow)
                totalOutageMs += maxOf(0L, end - start)
            }

            val totalOnTimeMs = maxOf(0L, monitoredMs - totalOutageMs)
            val availabilityPct = if (monitoredMs > 0L)
                (totalOnTimeMs.toFloat() / monitoredMs.toFloat()) * 100f
            else 0f

            val dateLabel = when (dayStart) {
                todayStart -> "Today"
                todayStart - 86_400_000L -> "Yesterday"
                else -> dayFormat.format(Date(dayStart))
            }

            DailyReport(
                dateLabel = dateLabel,
                dateMs = dayStart,
                outages = dayOutages,
                totalOutageMs = totalOutageMs,
                monitoredMs = monitoredMs,
                totalOnTimeMs = totalOnTimeMs,
                availabilityPct = availabilityPct,
                isFirstDay = isFirstDay
            )
        }.filter { it.monitoredMs > 0L || it.outages.isNotEmpty() }
    }

    private fun getDayStart(timeMs: Long): Long {
        return Calendar.getInstance().apply {
            timeInMillis = timeMs
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }
}

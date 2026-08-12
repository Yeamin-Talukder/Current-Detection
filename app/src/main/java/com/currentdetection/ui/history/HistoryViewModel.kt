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
    val dateLabel: String,
    val dateMs: Long,               // midnight of that day
    val completedOutages: List<PowerEventEntity>, // outages with endTime
    val activeOutage: PowerEventEntity?,          // ongoing outage (null if none)
    val totalOutageMs: Long,        // includes partial active outage up to now
    val monitoredMs: Long,          // actual monitored time (excludes pre-install unknown)
    val totalOnTimeMs: Long,
    val availabilityPct: Float,
    val isFirstDay: Boolean,        // day of first install
    val isToday: Boolean
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
        val todayStart = getDayStart(now)

        // Determine earliest day to show
        val earliest = minOf(
            events.minOfOrNull { it.startTime } ?: now,
            if (firstRunTime > 0L) firstRunTime else now
        )
        val earliestDayStart = getDayStart(earliest)

        // Build day list earliest → today
        val days = mutableListOf<Long>()
        var cursor = earliestDayStart
        while (cursor <= todayStart) {
            days.add(cursor)
            cursor += 86_400_000L
        }

        return days.reversed().map { dayStart ->
            val dayEnd = dayStart + 86_400_000L
            val dayNow = minOf(dayEnd, now)
            val isToday = dayStart == todayStart

            // Install-day handling
            val isFirstDay = firstRunTime > 0L && firstRunTime in dayStart until dayEnd
            val monitoringStart = if (isFirstDay && firstRunTime > dayStart) firstRunTime else dayStart
            val monitoredMs = maxOf(0L, dayNow - monitoringStart)

            // Completed outages that overlap this day
            val completedOutages = events.filter { event ->
                event.endTime != null &&
                event.startTime < dayEnd &&
                event.endTime > dayStart
            }.sortedBy { it.startTime }

            // Active (ongoing) outage for today only
            val activeOutage = if (isToday) {
                events.firstOrNull { it.endTime == null }
            } else null

            // Sum outage time within this day window
            var totalOutageMs = 0L
            (completedOutages + listOfNotNull(activeOutage)).forEach { event ->
                val eStart = maxOf(event.startTime, monitoringStart)
                val eEnd = minOf(event.endTime ?: dayNow, dayNow)
                if (eEnd > eStart) totalOutageMs += eEnd - eStart
            }

            val totalOnTimeMs = maxOf(0L, monitoredMs - totalOutageMs)
            val availabilityPct = if (monitoredMs > 0L)
                (totalOnTimeMs.toFloat() / monitoredMs.toFloat() * 100f).coerceIn(0f, 100f)
            else 0f

            val dateLabel = when (dayStart) {
                todayStart -> "Today"
                todayStart - 86_400_000L -> "Yesterday"
                else -> dayFormat.format(Date(dayStart))
            }

            DailyReport(
                dateLabel = dateLabel,
                dateMs = dayStart,
                completedOutages = completedOutages,
                activeOutage = activeOutage,
                totalOutageMs = totalOutageMs,
                monitoredMs = monitoredMs,
                totalOnTimeMs = totalOnTimeMs,
                availabilityPct = availabilityPct,
                isFirstDay = isFirstDay,
                isToday = isToday
            )
        }.filter { it.monitoredMs > 0L || it.completedOutages.isNotEmpty() || it.activeOutage != null }
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

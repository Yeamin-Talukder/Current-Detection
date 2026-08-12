package com.currentdetection.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currentdetection.data.local.PowerEventDao
import com.currentdetection.data.local.entities.PowerEventEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar

class HistoryViewModel(
    powerEventDao: PowerEventDao
) : ViewModel() {

    val historyEvents: StateFlow<Map<String, List<PowerEventEntity>>> = powerEventDao.getAllEvents()
        .map { events ->
            val calendar = Calendar.getInstance()
            val todayStart = getStartOfDay(calendar)
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            val yesterdayStart = getStartOfDay(calendar)
            
            // Further grouping could be added for "This Week", "This Month", etc.
            // Keeping simple with Today, Yesterday, and Older for demonstration.
            val grouped = events.filter { it.endTime != null }.groupBy { event ->
                when {
                    event.startTime >= todayStart -> "Today"
                    event.startTime >= yesterdayStart -> "Yesterday"
                    else -> "Older"
                }
            }
            // Ensure ordering of keys: Today, Yesterday, Older
            val sortedMap = mutableMapOf<String, List<PowerEventEntity>>()
            if (grouped.containsKey("Today")) sortedMap["Today"] = grouped["Today"]!!
            if (grouped.containsKey("Yesterday")) sortedMap["Yesterday"] = grouped["Yesterday"]!!
            if (grouped.containsKey("Older")) sortedMap["Older"] = grouped["Older"]!!
            
            sortedMap
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    private fun getStartOfDay(calendar: Calendar): Long {
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}

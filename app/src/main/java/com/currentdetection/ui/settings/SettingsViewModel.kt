package com.currentdetection.ui.settings

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currentdetection.data.local.PowerEventDao
import com.currentdetection.data.local.SettingsManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SettingsViewModel(
    private val settingsManager: SettingsManager,
    private val powerEventDao: PowerEventDao
) : ViewModel() {

    val monitoringEnabled = settingsManager.monitoringEnabledFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)
    val outageNotifications = settingsManager.outageNotificationsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)
    val powerRestoredNotifications = settingsManager.powerRestoredNotificationsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)
    val dailySummary = settingsManager.dailySummaryFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    /**
     * Toggle monitoring on/off: saves the preference AND starts/stops the service.
     */
    fun toggleMonitoring(context: Context, enabled: Boolean) {
        viewModelScope.launch {
            settingsManager.setMonitoringEnabled(enabled)
            val intent = Intent(context, com.currentdetection.engine.PowerMonitoringService::class.java)
            if (enabled) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    context.startForegroundService(intent)
                } else {
                    context.startService(intent)
                }
            } else {
                context.stopService(intent)
            }
        }
    }

    fun toggleOutageNotifications(enabled: Boolean) {
        viewModelScope.launch { settingsManager.setOutageNotifications(enabled) }
    }

    fun togglePowerRestoredNotifications(enabled: Boolean) {
        viewModelScope.launch { settingsManager.setPowerRestoredNotifications(enabled) }
    }

    fun toggleDailySummary(context: Context, enabled: Boolean) {
        viewModelScope.launch {
            settingsManager.setDailySummary(enabled)
            // Schedule or cancel the WorkManager daily summary job
            val workManager = androidx.work.WorkManager.getInstance(context)
            if (enabled) {
                val midnight = java.util.Calendar.getInstance().apply {
                    add(java.util.Calendar.DAY_OF_MONTH, 1)
                    set(java.util.Calendar.HOUR_OF_DAY, 23)
                    set(java.util.Calendar.MINUTE, 59)
                    set(java.util.Calendar.SECOND, 0)
                }.timeInMillis
                val initialDelay = midnight - System.currentTimeMillis()

                val request = androidx.work.PeriodicWorkRequestBuilder<
                    com.currentdetection.engine.DailySummaryWorker>(
                    1, java.util.concurrent.TimeUnit.DAYS
                )
                    .setInitialDelay(initialDelay, java.util.concurrent.TimeUnit.MILLISECONDS)
                    .build()
                workManager.enqueueUniquePeriodicWork(
                    "daily_summary",
                    androidx.work.ExistingPeriodicWorkPolicy.REPLACE,
                    request
                )
            } else {
                workManager.cancelUniqueWork("daily_summary")
            }
        }
    }

    fun clearHistory() {
        viewModelScope.launch { powerEventDao.clearAllEvents() }
    }

    fun exportHistory(context: Context) {
        viewModelScope.launch {
            val events = powerEventDao.getAllEventsList()
            if (events.isEmpty()) return@launch
            try {
                val file = File(context.cacheDir, "power_history.csv")
                val writer = file.bufferedWriter()
                writer.write("Start Time,End Time,Duration (ms)\n")
                val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                events.forEach {
                    val start = format.format(Date(it.startTime))
                    val end = it.endTime?.let { e -> format.format(Date(e)) } ?: "Ongoing"
                    val duration = it.endTime?.let { e -> (e - it.startTime).toString() } ?: "0"
                    writer.write("$start,$end,$duration\n")
                }
                writer.close()

                val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/csv"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(intent, "Export Power History"))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

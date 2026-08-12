package com.currentdetection.engine

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.currentdetection.data.local.AppDatabase
import com.currentdetection.data.local.SettingsManager
import kotlinx.coroutines.flow.first
import java.util.Calendar

class DailySummaryWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val settingsManager = SettingsManager(applicationContext)
        val dailySummaryEnabled = settingsManager.dailySummaryFlow.first()
        
        if (!dailySummaryEnabled) {
            return Result.success()
        }

        val database = AppDatabase.getDatabase(applicationContext)
        val dao = database.powerEventDao()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis

        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val endOfDay = calendar.timeInMillis

        val events = dao.getEventsInRange(startOfDay, endOfDay).first()
        
        var totalOutageMs = 0L
        var longestOutageMs = 0L
        val outageCount = events.size
        
        for (event in events) {
            val dur = event.duration ?: 0L
            totalOutageMs += dur
            if (dur > longestOutageMs) {
                longestOutageMs = dur
            }
        }

        val totalMonitoredMs = 24 * 60 * 60 * 1000L
        val availability = if (totalMonitoredMs > 0) {
            val onTime = totalMonitoredMs - totalOutageMs
            ((onTime.toDouble() / totalMonitoredMs) * 100).toInt()
        } else 100

        val totalOutageStr = formatDurationShort(totalOutageMs)
        val longestOutageStr = formatDurationShort(longestOutageMs)

        val notificationManager = AppNotificationManager(applicationContext)
        
        val notification = androidx.core.app.NotificationCompat.Builder(applicationContext, AppNotificationManager.ALERT_CHANNEL_ID)
            .setContentTitle("⚡ Current Detection — Daily Report")
            .setStyle(androidx.core.app.NotificationCompat.BigTextStyle()
                .bigText("Outages: $outageCount\nTotal outage: $totalOutageStr\nLongest outage: $longestOutageStr\nAvailability: $availability%"))
            .setSmallIcon(com.currentdetection.R.mipmap.ic_launcher)
            .build()
            
        val androidNotificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        androidNotificationManager.notify(2002, notification)

        return Result.success()
    }

    private fun formatDurationShort(millis: Long): String {
        val totalMins = millis / 60000
        val h = totalMins / 60
        val m = totalMins % 60
        return if (h > 0) "${h}h ${m}m" else "${m}m"
    }
}

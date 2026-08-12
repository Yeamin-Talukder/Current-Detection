package com.currentdetection.engine

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.currentdetection.R
import com.currentdetection.data.local.SettingsManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class AppNotificationManager(private val context: Context) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val settingsManager = SettingsManager(context)

    init {
        createChannels()
    }

    private fun createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                SERVICE_CHANNEL_ID,
                "Monitoring Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply { description = "Background monitoring indicator" }

            val alertChannel = NotificationChannel(
                ALERT_CHANNEL_ID,
                "Power Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = "Notifies when power goes out or is restored" }

            val summaryChannel = NotificationChannel(
                SUMMARY_CHANNEL_ID,
                "Daily Summary",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = "Daily power availability report" }

            notificationManager.createNotificationChannel(serviceChannel)
            notificationManager.createNotificationChannel(alertChannel)
            notificationManager.createNotificationChannel(summaryChannel)
        }
    }

    fun getServiceNotification(): android.app.Notification {
        return NotificationCompat.Builder(context, SERVICE_CHANNEL_ID)
            .setContentTitle("Current Detection Active")
            .setContentText("Monitoring electricity status...")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setOngoing(true)
            .build()
    }

    fun showPowerOffAlert() {
        // Respect the outage notification preference
        val enabled = runBlocking { settingsManager.outageNotificationsFlow.first() }
        if (!enabled) return

        val notification = NotificationCompat.Builder(context, ALERT_CHANNEL_ID)
            .setContentTitle("🔴 Load Shedding Detected")
            .setContentText("Power appears to be unavailable in your building.")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(ALERT_NOTIFICATION_ID, notification)
    }

    fun showPowerOnAlert(durationText: String) {
        // Respect the power restored notification preference
        val enabled = runBlocking { settingsManager.powerRestoredNotificationsFlow.first() }
        if (!enabled) return

        val notification = NotificationCompat.Builder(context, ALERT_CHANNEL_ID)
            .setContentTitle("🟢 Power Restored")
            .setContentText("Electricity is back. Outage duration: $durationText")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(ALERT_NOTIFICATION_ID + 1, notification)
    }

    companion object {
        const val SERVICE_CHANNEL_ID = "service_channel"
        const val ALERT_CHANNEL_ID = "alert_channel"
        const val SUMMARY_CHANNEL_ID = "summary_channel"
        const val ALERT_NOTIFICATION_ID = 1001
    }
}

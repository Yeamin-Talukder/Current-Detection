package com.currentdetection.engine

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.currentdetection.R

class AppNotificationManager(private val context: Context) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createChannels()
    }

    private fun createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                SERVICE_CHANNEL_ID,
                "Monitoring Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val alertChannel = NotificationChannel(
                ALERT_CHANNEL_ID,
                "Power Alerts",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(serviceChannel)
            notificationManager.createNotificationChannel(alertChannel)
        }
    }

    fun getServiceNotification(): android.app.Notification {
        return NotificationCompat.Builder(context, SERVICE_CHANNEL_ID)
            .setContentTitle("Current Detection Active")
            .setContentText("Monitoring electricity status...")
            .setSmallIcon(R.mipmap.ic_launcher) // Use app icon for now
            .setOngoing(true)
            .build()
    }

    fun showPowerOffAlert() {
        val notification = NotificationCompat.Builder(context, ALERT_CHANNEL_ID)
            .setContentTitle("\uD83D\uDD34 Load Shedding Detected")
            .setContentText("Power appears to be unavailable.")
            .setSmallIcon(R.mipmap.ic_launcher)
            .build()
        notificationManager.notify(ALERT_NOTIFICATION_ID, notification)
    }

    fun showPowerOnAlert(durationText: String) {
        val notification = NotificationCompat.Builder(context, ALERT_CHANNEL_ID)
            .setContentTitle("\uD83D\uDFE2 Power Restored")
            .setContentText("Electricity appears to be available again. Outage duration: $durationText")
            .setSmallIcon(R.mipmap.ic_launcher)
            .build()
        notificationManager.notify(ALERT_NOTIFICATION_ID, notification)
    }

    companion object {
        const val SERVICE_CHANNEL_ID = "service_channel"
        const val ALERT_CHANNEL_ID = "alert_channel"
        const val ALERT_NOTIFICATION_ID = 1001
    }
}

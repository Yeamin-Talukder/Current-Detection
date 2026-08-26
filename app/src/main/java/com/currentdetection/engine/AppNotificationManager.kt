package com.currentdetection.engine

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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

            val awayChannel = NotificationChannel(
                AWAY_CHANNEL_ID,
                "Away Mode",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shown while you're away from home"
                setShowBadge(false)
            }

            notificationManager.createNotificationChannel(serviceChannel)
            notificationManager.createNotificationChannel(alertChannel)
            notificationManager.createNotificationChannel(summaryChannel)
            notificationManager.createNotificationChannel(awayChannel)
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

        val markUnknownIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = NotificationActionReceiver.ACTION_MARK_UNKNOWN
        }
        val markUnknownPendingIntent = PendingIntent.getBroadcast(
            context, 0, markUnknownIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, ALERT_CHANNEL_ID)
            .setContentTitle("🔴 Load Shedding Detected")
            .setContentText("Power appears to be unavailable in your building.")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .addAction(0, "🏠 I Left Home", markUnknownPendingIntent)
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

    /**
     * Shows a persistent notification while the user is in Away Mode.
     * Includes an "I'm Back" action button for easy one-tap return.
     */
    fun showAwayModeNotification() {
        val iAmBackIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = NotificationActionReceiver.ACTION_I_AM_BACK
        }
        val iAmBackPendingIntent = PendingIntent.getBroadcast(
            context, REQUEST_CODE_I_AM_BACK, iAmBackIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, AWAY_CHANNEL_ID)
            .setContentTitle("🏠 You're Away From Home")
            .setContentText("Monitoring paused. Tap when you return.")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setOngoing(true)
            .setSilent(true)
            .addAction(0, "✅ I'm Back!", iAmBackPendingIntent)
            .build()

        notificationManager.notify(AWAY_NOTIFICATION_ID, notification)
    }

    /**
     * Dismisses the persistent Away Mode notification.
     */
    fun cancelAwayModeNotification() {
        notificationManager.cancel(AWAY_NOTIFICATION_ID)
    }

    /**
     * Shows a brief summary notification after the user returns home.
     * Describes how long they were away.
     */
    fun showReturnSummaryNotification(awayDurationMs: Long) {
        val durationText = formatDuration(awayDurationMs)
        val notification = NotificationCompat.Builder(context, ALERT_CHANNEL_ID)
            .setContentTitle("👋 Welcome Back!")
            .setContentText("You were away for $durationText. Monitoring has resumed.")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(RETURN_SUMMARY_NOTIFICATION_ID, notification)
    }

    private fun formatDuration(ms: Long): String {
        val totalSeconds = ms / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        return if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
    }

    companion object {
        const val SERVICE_CHANNEL_ID = "service_channel"
        const val ALERT_CHANNEL_ID = "alert_channel"
        const val SUMMARY_CHANNEL_ID = "summary_channel"
        const val AWAY_CHANNEL_ID = "away_channel"

        const val ALERT_NOTIFICATION_ID = 1001
        const val AWAY_NOTIFICATION_ID = 1003
        const val RETURN_SUMMARY_NOTIFICATION_ID = 1004

        private const val REQUEST_CODE_I_AM_BACK = 200
    }
}

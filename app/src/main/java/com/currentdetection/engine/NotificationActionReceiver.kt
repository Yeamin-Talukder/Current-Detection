package com.currentdetection.engine

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.currentdetection.data.local.AppDatabase
import com.currentdetection.data.local.SettingsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val database = AppDatabase.getDatabase(context)
        val settingsManager = SettingsManager(context)
        val eventManager = EventManager.getInstance(database.powerEventDao(), settingsManager)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        when (intent.action) {
            ACTION_MARK_UNKNOWN -> {
                // Legacy "I left home" quick action — triggers full Away Mode
                CoroutineScope(Dispatchers.IO).launch {
                    eventManager.enterAwayMode()
                    // Post the persistent away notification so user can easily return
                    val appNotificationManager = AppNotificationManager(context)
                    appNotificationManager.showAwayModeNotification()
                    // Dismiss the power-off alert
                    notificationManager.cancel(AppNotificationManager.ALERT_NOTIFICATION_ID)
                }
            }
            ACTION_I_AM_BACK -> {
                // User is returning home from Away Mode
                CoroutineScope(Dispatchers.IO).launch {
                    val awayDurationMs = eventManager.exitAwayMode()
                    val appNotificationManager = AppNotificationManager(context)
                    appNotificationManager.cancelAwayModeNotification()
                    if (awayDurationMs > 0L) {
                        appNotificationManager.showReturnSummaryNotification(awayDurationMs)
                    }
                }
            }
        }
    }

    companion object {
        const val ACTION_MARK_UNKNOWN = "com.currentdetection.action.MARK_UNKNOWN"
        const val ACTION_I_AM_BACK = "com.currentdetection.action.I_AM_BACK"
    }
}

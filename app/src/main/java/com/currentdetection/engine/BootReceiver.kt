package com.currentdetection.engine

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.currentdetection.data.local.SettingsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Restarts the monitoring service after:
 * - Device boot / restart
 * - App package replacement (update install)
 *
 * This closes the gap where START_STICKY cannot survive a full device reboot.
 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED &&
            intent.action != Intent.ACTION_MY_PACKAGE_REPLACED
        ) return

        val settingsManager = SettingsManager(context)

        CoroutineScope(Dispatchers.IO).launch {
            val monitoringEnabled = settingsManager.monitoringEnabledFlow.first()
            val isAway = settingsManager.isAwayFlow.first()

            // Restart the service if monitoring was active before the reboot.
            // The service will pick up away-mode state via EventManager.restorePersistedState().
            if (monitoringEnabled || isAway) {
                val serviceIntent = Intent(context, PowerMonitoringService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(serviceIntent)
                } else {
                    context.startService(serviceIntent)
                }
            }
        }
    }
}

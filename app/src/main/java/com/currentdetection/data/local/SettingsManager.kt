package com.currentdetection.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsManager(private val context: Context) {

    val monitoringEnabledFlow: Flow<Boolean> = context.dataStore.data.map { it[MONITORING_ENABLED] ?: true }
    val outageNotificationsFlow: Flow<Boolean> = context.dataStore.data.map { it[OUTAGE_NOTIFICATIONS] ?: true }
    val powerRestoredNotificationsFlow: Flow<Boolean> = context.dataStore.data.map { it[POWER_RESTORED_NOTIFICATIONS] ?: true }
    val dailySummaryFlow: Flow<Boolean> = context.dataStore.data.map { it[DAILY_SUMMARY] ?: false }

    suspend fun setMonitoringEnabled(enabled: Boolean) {
        context.dataStore.edit { it[MONITORING_ENABLED] = enabled }
    }

    suspend fun setOutageNotifications(enabled: Boolean) {
        context.dataStore.edit { it[OUTAGE_NOTIFICATIONS] = enabled }
    }

    suspend fun setPowerRestoredNotifications(enabled: Boolean) {
        context.dataStore.edit { it[POWER_RESTORED_NOTIFICATIONS] = enabled }
    }

    suspend fun setDailySummary(enabled: Boolean) {
        context.dataStore.edit { it[DAILY_SUMMARY] = enabled }
    }

    companion object {
        val MONITORING_ENABLED = booleanPreferencesKey("monitoring_enabled")
        val OUTAGE_NOTIFICATIONS = booleanPreferencesKey("outage_notifications")
        val POWER_RESTORED_NOTIFICATIONS = booleanPreferencesKey("power_restored_notifications")
        val DAILY_SUMMARY = booleanPreferencesKey("daily_summary")
    }
}

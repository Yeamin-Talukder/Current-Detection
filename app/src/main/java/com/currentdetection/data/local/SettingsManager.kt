package com.currentdetection.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsManager(private val context: Context) {

    val monitoringEnabledFlow: Flow<Boolean> = context.dataStore.data.map { it[MONITORING_ENABLED] ?: true }
    val outageNotificationsFlow: Flow<Boolean> = context.dataStore.data.map { it[OUTAGE_NOTIFICATIONS] ?: true }
    val powerRestoredNotificationsFlow: Flow<Boolean> = context.dataStore.data.map { it[POWER_RESTORED_NOTIFICATIONS] ?: true }
    val dailySummaryFlow: Flow<Boolean> = context.dataStore.data.map { it[DAILY_SUMMARY] ?: false }

    /** Whether onboarding has been completed. */
    val onboardingCompletedFlow: Flow<Boolean> = context.dataStore.data.map { it[ONBOARDING_COMPLETED] ?: false }

    /** The very first time this app was launched (set once, never changed). */
    val firstRunTimeFlow: Flow<Long> = context.dataStore.data.map { it[FIRST_RUN_TIME] ?: 0L }

    /** Timestamp when power state was last confirmed ON. */
    val lastPowerOnTimeFlow: Flow<Long> = context.dataStore.data.map { it[LAST_POWER_ON_TIME] ?: 0L }

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

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { it[ONBOARDING_COMPLETED] = completed }
    }

    /** Call once on first launch to record install time. */
    suspend fun initFirstRunTime() {
        val existing = context.dataStore.data.first()[FIRST_RUN_TIME] ?: 0L
        if (existing == 0L) {
            context.dataStore.edit { it[FIRST_RUN_TIME] = System.currentTimeMillis() }
        }
    }

    /** Call whenever power transitions to confirmed ON. */
    suspend fun setLastPowerOnTime(timeMs: Long) {
        context.dataStore.edit { it[LAST_POWER_ON_TIME] = timeMs }
    }

    companion object {
        val MONITORING_ENABLED = booleanPreferencesKey("monitoring_enabled")
        val OUTAGE_NOTIFICATIONS = booleanPreferencesKey("outage_notifications")
        val POWER_RESTORED_NOTIFICATIONS = booleanPreferencesKey("power_restored_notifications")
        val DAILY_SUMMARY = booleanPreferencesKey("daily_summary")
        val FIRST_RUN_TIME = longPreferencesKey("first_run_time")
        val LAST_POWER_ON_TIME = longPreferencesKey("last_power_on_time")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    }
}

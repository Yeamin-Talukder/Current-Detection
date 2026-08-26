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

    // ─── Away Mode ─────────────────────────────────────────────────────────────

    /** True while the user is explicitly in Away Mode (left home). */
    val isAwayFlow: Flow<Boolean> = context.dataStore.data.map { it[IS_AWAY] ?: false }

    /** The timestamp (ms) when Away Mode was activated. 0L if not away. */
    val awayStartTimeFlow: Flow<Long> = context.dataStore.data.map { it[AWAY_START_TIME] ?: 0L }

    // ─── State Persistence for Survival Across Process Death ───────────────────

    /**
     * The timestamp when the current pending state started being observed.
     * Persisted so the confirmation window doesn't restart after process death.
     */
    val pendingStateStartTimeFlow: Flow<Long> = context.dataStore.data.map { it[PENDING_STATE_START_TIME] ?: 0L }

    /**
     * The name of the currently pending power state (before confirmation delay).
     * "POWER_ON", "POWER_OFF", or "" if no pending state.
     */
    val pendingStateNameFlow: Flow<String> = context.dataStore.data.map { it[PENDING_STATE_NAME] ?: "" }

    /**
     * The name of the last confirmed power state.
     * "POWER_ON", "POWER_OFF", "UNKNOWN", or "".
     */
    val confirmedStateNameFlow: Flow<String> = context.dataStore.data.map { it[CONFIRMED_STATE_NAME] ?: "" }

    // ─── Mutators ──────────────────────────────────────────────────────────────
    
    /** The timestamp of the last active monitoring cycle. Used to detect app death. */
    val lastHeartbeatTimeFlow: Flow<Long> = context.dataStore.data.map { it[LAST_HEARTBEAT_TIME] ?: 0L }

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

    // ─── Away Mode Mutators ────────────────────────────────────────────────────

    suspend fun setAwayMode(isAway: Boolean, startTimeMs: Long = 0L) {
        context.dataStore.edit {
            it[IS_AWAY] = isAway
            it[AWAY_START_TIME] = if (isAway) startTimeMs else 0L
        }
    }

    // ─── Pending State Mutators ────────────────────────────────────────────────

    suspend fun setPendingStateStartTime(timeMs: Long) {
        context.dataStore.edit { it[PENDING_STATE_START_TIME] = timeMs }
    }

    suspend fun setPendingStateName(name: String) {
        context.dataStore.edit { it[PENDING_STATE_NAME] = name }
    }

    suspend fun setConfirmedStateName(name: String) {
        context.dataStore.edit { it[CONFIRMED_STATE_NAME] = name }
    }

    suspend fun clearPendingState() {
        context.dataStore.edit {
            it.remove(PENDING_STATE_NAME)
            it.remove(PENDING_STATE_START_TIME)
        }
    }

    suspend fun setLastHeartbeatTime(timeMs: Long) {
        context.dataStore.edit { it[LAST_HEARTBEAT_TIME] = timeMs }
    }

    companion object {
        val MONITORING_ENABLED = booleanPreferencesKey("monitoring_enabled")
        val OUTAGE_NOTIFICATIONS = booleanPreferencesKey("outage_notifications")
        val POWER_RESTORED_NOTIFICATIONS = booleanPreferencesKey("power_restored_notifications")
        val DAILY_SUMMARY = booleanPreferencesKey("daily_summary")
        val FIRST_RUN_TIME = longPreferencesKey("first_run_time")
        val LAST_POWER_ON_TIME = longPreferencesKey("last_power_on_time")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")

        // Away Mode
        val IS_AWAY = booleanPreferencesKey("is_away")
        val AWAY_START_TIME = longPreferencesKey("away_start_time")

        // State persistence
        val PENDING_STATE_START_TIME = longPreferencesKey("pending_state_start_time")
        val PENDING_STATE_NAME = androidx.datastore.preferences.core.stringPreferencesKey("pending_state_name")
        val CONFIRMED_STATE_NAME = androidx.datastore.preferences.core.stringPreferencesKey("confirmed_state_name")
        val LAST_HEARTBEAT_TIME = longPreferencesKey("last_heartbeat_time")
    }
}

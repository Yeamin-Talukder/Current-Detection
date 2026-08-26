package com.currentdetection.data.local;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0015\n\u0002\u0010\u0002\n\u0002\b\u0019\u0018\u0000 <2\u00020\u0001:\u0001<B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010#\u001a\u00020$H\u0086@\u00a2\u0006\u0002\u0010%J\u000e\u0010&\u001a\u00020$H\u0086@\u00a2\u0006\u0002\u0010%J \u0010\'\u001a\u00020$2\u0006\u0010(\u001a\u00020\u000e2\b\b\u0002\u0010)\u001a\u00020\u0007H\u0086@\u00a2\u0006\u0002\u0010*J\u0016\u0010+\u001a\u00020$2\u0006\u0010,\u001a\u00020\u000bH\u0086@\u00a2\u0006\u0002\u0010-J\u0016\u0010.\u001a\u00020$2\u0006\u0010/\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u00100J\u0016\u00101\u001a\u00020$2\u0006\u00102\u001a\u00020\u0007H\u0086@\u00a2\u0006\u0002\u00103J\u0016\u00104\u001a\u00020$2\u0006\u00102\u001a\u00020\u0007H\u0086@\u00a2\u0006\u0002\u00103J\u0016\u00105\u001a\u00020$2\u0006\u0010/\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u00100J\u0016\u00106\u001a\u00020$2\u0006\u00107\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u00100J\u0016\u00108\u001a\u00020$2\u0006\u0010/\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u00100J\u0016\u00109\u001a\u00020$2\u0006\u0010,\u001a\u00020\u000bH\u0086@\u00a2\u0006\u0002\u0010-J\u0016\u0010:\u001a\u00020$2\u0006\u00102\u001a\u00020\u0007H\u0086@\u00a2\u0006\u0002\u00103J\u0016\u0010;\u001a\u00020$2\u0006\u0010/\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u00100R\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\tR\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\tR\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\tR\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\tR\u0017\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\tR\u0017\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\tR\u0017\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\tR\u0017\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\tR\u0017\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\tR\u0017\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\tR\u0017\u0010!\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\t\u00a8\u0006="}, d2 = {"Lcom/currentdetection/data/local/SettingsManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "awayStartTimeFlow", "Lkotlinx/coroutines/flow/Flow;", "", "getAwayStartTimeFlow", "()Lkotlinx/coroutines/flow/Flow;", "confirmedStateNameFlow", "", "getConfirmedStateNameFlow", "dailySummaryFlow", "", "getDailySummaryFlow", "firstRunTimeFlow", "getFirstRunTimeFlow", "isAwayFlow", "lastHeartbeatTimeFlow", "getLastHeartbeatTimeFlow", "lastPowerOnTimeFlow", "getLastPowerOnTimeFlow", "monitoringEnabledFlow", "getMonitoringEnabledFlow", "onboardingCompletedFlow", "getOnboardingCompletedFlow", "outageNotificationsFlow", "getOutageNotificationsFlow", "pendingStateNameFlow", "getPendingStateNameFlow", "pendingStateStartTimeFlow", "getPendingStateStartTimeFlow", "powerRestoredNotificationsFlow", "getPowerRestoredNotificationsFlow", "clearPendingState", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "initFirstRunTime", "setAwayMode", "isAway", "startTimeMs", "(ZJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setConfirmedStateName", "name", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setDailySummary", "enabled", "(ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setLastHeartbeatTime", "timeMs", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setLastPowerOnTime", "setMonitoringEnabled", "setOnboardingCompleted", "completed", "setOutageNotifications", "setPendingStateName", "setPendingStateStartTime", "setPowerRestoredNotifications", "Companion", "app_debug"})
public final class SettingsManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.lang.Boolean> monitoringEnabledFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.lang.Boolean> outageNotificationsFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.lang.Boolean> powerRestoredNotificationsFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.lang.Boolean> dailySummaryFlow = null;
    
    /**
     * Whether onboarding has been completed.
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.lang.Boolean> onboardingCompletedFlow = null;
    
    /**
     * The very first time this app was launched (set once, never changed).
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.lang.Long> firstRunTimeFlow = null;
    
    /**
     * Timestamp when power state was last confirmed ON.
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.lang.Long> lastPowerOnTimeFlow = null;
    
    /**
     * True while the user is explicitly in Away Mode (left home).
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.lang.Boolean> isAwayFlow = null;
    
    /**
     * The timestamp (ms) when Away Mode was activated. 0L if not away.
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.lang.Long> awayStartTimeFlow = null;
    
    /**
     * The timestamp when the current pending state started being observed.
     * Persisted so the confirmation window doesn't restart after process death.
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.lang.Long> pendingStateStartTimeFlow = null;
    
    /**
     * The name of the currently pending power state (before confirmation delay).
     * "POWER_ON", "POWER_OFF", or "" if no pending state.
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.lang.String> pendingStateNameFlow = null;
    
    /**
     * The name of the last confirmed power state.
     * "POWER_ON", "POWER_OFF", "UNKNOWN", or "".
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.lang.String> confirmedStateNameFlow = null;
    
    /**
     * The timestamp of the last active monitoring cycle. Used to detect app death.
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.lang.Long> lastHeartbeatTimeFlow = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> MONITORING_ENABLED = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> OUTAGE_NOTIFICATIONS = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> POWER_RESTORED_NOTIFICATIONS = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> DAILY_SUMMARY = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Long> FIRST_RUN_TIME = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Long> LAST_POWER_ON_TIME = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> ONBOARDING_COMPLETED = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> IS_AWAY = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Long> AWAY_START_TIME = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Long> PENDING_STATE_START_TIME = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> PENDING_STATE_NAME = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> CONFIRMED_STATE_NAME = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Long> LAST_HEARTBEAT_TIME = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.currentdetection.data.local.SettingsManager.Companion Companion = null;
    
    public SettingsManager(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Boolean> getMonitoringEnabledFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Boolean> getOutageNotificationsFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Boolean> getPowerRestoredNotificationsFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Boolean> getDailySummaryFlow() {
        return null;
    }
    
    /**
     * Whether onboarding has been completed.
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Boolean> getOnboardingCompletedFlow() {
        return null;
    }
    
    /**
     * The very first time this app was launched (set once, never changed).
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Long> getFirstRunTimeFlow() {
        return null;
    }
    
    /**
     * Timestamp when power state was last confirmed ON.
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Long> getLastPowerOnTimeFlow() {
        return null;
    }
    
    /**
     * True while the user is explicitly in Away Mode (left home).
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Boolean> isAwayFlow() {
        return null;
    }
    
    /**
     * The timestamp (ms) when Away Mode was activated. 0L if not away.
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Long> getAwayStartTimeFlow() {
        return null;
    }
    
    /**
     * The timestamp when the current pending state started being observed.
     * Persisted so the confirmation window doesn't restart after process death.
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Long> getPendingStateStartTimeFlow() {
        return null;
    }
    
    /**
     * The name of the currently pending power state (before confirmation delay).
     * "POWER_ON", "POWER_OFF", or "" if no pending state.
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.String> getPendingStateNameFlow() {
        return null;
    }
    
    /**
     * The name of the last confirmed power state.
     * "POWER_ON", "POWER_OFF", "UNKNOWN", or "".
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.String> getConfirmedStateNameFlow() {
        return null;
    }
    
    /**
     * The timestamp of the last active monitoring cycle. Used to detect app death.
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Long> getLastHeartbeatTimeFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setMonitoringEnabled(boolean enabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setOutageNotifications(boolean enabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setPowerRestoredNotifications(boolean enabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setDailySummary(boolean enabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setOnboardingCompleted(boolean completed, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Call once on first launch to record install time.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object initFirstRunTime(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Call whenever power transitions to confirmed ON.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setLastPowerOnTime(long timeMs, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setAwayMode(boolean isAway, long startTimeMs, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setPendingStateStartTime(long timeMs, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setPendingStateName(@org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setConfirmedStateName(@org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object clearPendingState(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setLastHeartbeatTime(long timeMs, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0016\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0007R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u0007R\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0007R\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\f0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0007R\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0007R\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0007R\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\f0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0007R\u0017\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\f0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0007R\u0017\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\f0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0007R\u0017\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\t0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0007R\u0017\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0007R\u0017\u0010 \u001a\b\u0012\u0004\u0012\u00020\f0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0007\u00a8\u0006\""}, d2 = {"Lcom/currentdetection/data/local/SettingsManager$Companion;", "", "()V", "AWAY_START_TIME", "Landroidx/datastore/preferences/core/Preferences$Key;", "", "getAWAY_START_TIME", "()Landroidx/datastore/preferences/core/Preferences$Key;", "CONFIRMED_STATE_NAME", "", "getCONFIRMED_STATE_NAME", "DAILY_SUMMARY", "", "getDAILY_SUMMARY", "FIRST_RUN_TIME", "getFIRST_RUN_TIME", "IS_AWAY", "getIS_AWAY", "LAST_HEARTBEAT_TIME", "getLAST_HEARTBEAT_TIME", "LAST_POWER_ON_TIME", "getLAST_POWER_ON_TIME", "MONITORING_ENABLED", "getMONITORING_ENABLED", "ONBOARDING_COMPLETED", "getONBOARDING_COMPLETED", "OUTAGE_NOTIFICATIONS", "getOUTAGE_NOTIFICATIONS", "PENDING_STATE_NAME", "getPENDING_STATE_NAME", "PENDING_STATE_START_TIME", "getPENDING_STATE_START_TIME", "POWER_RESTORED_NOTIFICATIONS", "getPOWER_RESTORED_NOTIFICATIONS", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> getMONITORING_ENABLED() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> getOUTAGE_NOTIFICATIONS() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> getPOWER_RESTORED_NOTIFICATIONS() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> getDAILY_SUMMARY() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.Long> getFIRST_RUN_TIME() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.Long> getLAST_POWER_ON_TIME() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> getONBOARDING_COMPLETED() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> getIS_AWAY() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.Long> getAWAY_START_TIME() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.Long> getPENDING_STATE_START_TIME() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> getPENDING_STATE_NAME() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> getCONFIRMED_STATE_NAME() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.Long> getLAST_HEARTBEAT_TIME() {
            return null;
        }
    }
}
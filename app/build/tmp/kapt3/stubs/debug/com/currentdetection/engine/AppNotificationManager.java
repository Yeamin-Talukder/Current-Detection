package com.currentdetection.engine;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u0000 \u00182\u00020\u0001:\u0001\u0018B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\t\u001a\u00020\nJ\b\u0010\u000b\u001a\u00020\nH\u0002J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J\u0006\u0010\u0010\u001a\u00020\u0011J\u0006\u0010\u0012\u001a\u00020\nJ\u0006\u0010\u0013\u001a\u00020\nJ\u000e\u0010\u0014\u001a\u00020\n2\u0006\u0010\u0015\u001a\u00020\rJ\u000e\u0010\u0016\u001a\u00020\n2\u0006\u0010\u0017\u001a\u00020\u000fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/currentdetection/engine/AppNotificationManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "notificationManager", "Landroid/app/NotificationManager;", "settingsManager", "Lcom/currentdetection/data/local/SettingsManager;", "cancelAwayModeNotification", "", "createChannels", "formatDuration", "", "ms", "", "getServiceNotification", "Landroid/app/Notification;", "showAwayModeNotification", "showPowerOffAlert", "showPowerOnAlert", "durationText", "showReturnSummaryNotification", "awayDurationMs", "Companion", "app_debug"})
public final class AppNotificationManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final android.app.NotificationManager notificationManager = null;
    @org.jetbrains.annotations.NotNull()
    private final com.currentdetection.data.local.SettingsManager settingsManager = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String SERVICE_CHANNEL_ID = "service_channel";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ALERT_CHANNEL_ID = "alert_channel";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String SUMMARY_CHANNEL_ID = "summary_channel";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String AWAY_CHANNEL_ID = "away_channel";
    public static final int ALERT_NOTIFICATION_ID = 1001;
    public static final int AWAY_NOTIFICATION_ID = 1003;
    public static final int RETURN_SUMMARY_NOTIFICATION_ID = 1004;
    private static final int REQUEST_CODE_I_AM_BACK = 200;
    @org.jetbrains.annotations.NotNull()
    public static final com.currentdetection.engine.AppNotificationManager.Companion Companion = null;
    
    public AppNotificationManager(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    private final void createChannels() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.app.Notification getServiceNotification() {
        return null;
    }
    
    public final void showPowerOffAlert() {
    }
    
    public final void showPowerOnAlert(@org.jetbrains.annotations.NotNull()
    java.lang.String durationText) {
    }
    
    /**
     * Shows a persistent notification while the user is in Away Mode.
     * Includes an "I'm Back" action button for easy one-tap return.
     */
    public final void showAwayModeNotification() {
    }
    
    /**
     * Dismisses the persistent Away Mode notification.
     */
    public final void cancelAwayModeNotification() {
    }
    
    /**
     * Shows a brief summary notification after the user returns home.
     * Describes how long they were away.
     */
    public final void showReturnSummaryNotification(long awayDurationMs) {
    }
    
    private final java.lang.String formatDuration(long ms) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/currentdetection/engine/AppNotificationManager$Companion;", "", "()V", "ALERT_CHANNEL_ID", "", "ALERT_NOTIFICATION_ID", "", "AWAY_CHANNEL_ID", "AWAY_NOTIFICATION_ID", "REQUEST_CODE_I_AM_BACK", "RETURN_SUMMARY_NOTIFICATION_ID", "SERVICE_CHANNEL_ID", "SUMMARY_CHANNEL_ID", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}
package com.currentdetection.engine;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u0000 \u000f2\u00020\u0001:\u0001\u000fB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0007\u001a\u00020\bH\u0002J\u0006\u0010\t\u001a\u00020\nJ\u0006\u0010\u000b\u001a\u00020\bJ\u000e\u0010\f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/currentdetection/engine/AppNotificationManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "notificationManager", "Landroid/app/NotificationManager;", "createChannels", "", "getServiceNotification", "Landroid/app/Notification;", "showPowerOffAlert", "showPowerOnAlert", "durationText", "", "Companion", "app_debug"})
public final class AppNotificationManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final android.app.NotificationManager notificationManager = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String SERVICE_CHANNEL_ID = "service_channel";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ALERT_CHANNEL_ID = "alert_channel";
    public static final int ALERT_NOTIFICATION_ID = 1001;
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/currentdetection/engine/AppNotificationManager$Companion;", "", "()V", "ALERT_CHANNEL_ID", "", "ALERT_NOTIFICATION_ID", "", "SERVICE_CHANNEL_ID", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}
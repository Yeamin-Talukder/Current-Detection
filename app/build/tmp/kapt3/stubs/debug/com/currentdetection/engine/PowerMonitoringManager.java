package com.currentdetection.engine;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B?\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u000f\u00a2\u0006\u0002\u0010\u0010J\u0010\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aH\u0002J\u000e\u0010\u001b\u001a\u00020\u001cH\u0086@\u00a2\u0006\u0002\u0010\u001dJ\u000e\u0010\u001e\u001a\u00020\u001cH\u0082@\u00a2\u0006\u0002\u0010\u001dJ\u0006\u0010\u001f\u001a\u00020\u001cJ\u0006\u0010 \u001a\u00020\u001cR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006!"}, d2 = {"Lcom/currentdetection/engine/PowerMonitoringManager;", "", "context", "Landroid/content/Context;", "wifiScanner", "Lcom/currentdetection/wifi/WifiScanner;", "networkRepository", "Lcom/currentdetection/domain/repository/NetworkRepository;", "eventManager", "Lcom/currentdetection/engine/EventManager;", "notificationManager", "Lcom/currentdetection/engine/AppNotificationManager;", "settingsManager", "Lcom/currentdetection/data/local/SettingsManager;", "powerDetectionEngine", "Lcom/currentdetection/engine/PowerDetectionEngine;", "(Landroid/content/Context;Lcom/currentdetection/wifi/WifiScanner;Lcom/currentdetection/domain/repository/NetworkRepository;Lcom/currentdetection/engine/EventManager;Lcom/currentdetection/engine/AppNotificationManager;Lcom/currentdetection/data/local/SettingsManager;Lcom/currentdetection/engine/PowerDetectionEngine;)V", "monitoringJob", "Lkotlinx/coroutines/Job;", "previousState", "Lcom/currentdetection/engine/PowerState;", "scope", "Lkotlinx/coroutines/CoroutineScope;", "formatDurationForNotification", "", "ms", "", "handleReturnHome", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "performDetection", "startMonitoring", "stopMonitoring", "app_debug"})
public final class PowerMonitoringManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.currentdetection.wifi.WifiScanner wifiScanner = null;
    @org.jetbrains.annotations.NotNull()
    private final com.currentdetection.domain.repository.NetworkRepository networkRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.currentdetection.engine.EventManager eventManager = null;
    @org.jetbrains.annotations.NotNull()
    private final com.currentdetection.engine.AppNotificationManager notificationManager = null;
    @org.jetbrains.annotations.NotNull()
    private final com.currentdetection.data.local.SettingsManager settingsManager = null;
    @org.jetbrains.annotations.NotNull()
    private final com.currentdetection.engine.PowerDetectionEngine powerDetectionEngine = null;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job monitoringJob;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope scope = null;
    @org.jetbrains.annotations.NotNull()
    private com.currentdetection.engine.PowerState previousState = com.currentdetection.engine.PowerState.UNKNOWN;
    
    public PowerMonitoringManager(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.currentdetection.wifi.WifiScanner wifiScanner, @org.jetbrains.annotations.NotNull()
    com.currentdetection.domain.repository.NetworkRepository networkRepository, @org.jetbrains.annotations.NotNull()
    com.currentdetection.engine.EventManager eventManager, @org.jetbrains.annotations.NotNull()
    com.currentdetection.engine.AppNotificationManager notificationManager, @org.jetbrains.annotations.NotNull()
    com.currentdetection.data.local.SettingsManager settingsManager, @org.jetbrains.annotations.NotNull()
    com.currentdetection.engine.PowerDetectionEngine powerDetectionEngine) {
        super();
    }
    
    public final void startMonitoring() {
    }
    
    public final void stopMonitoring() {
    }
    
    /**
     * Called by the NotificationActionReceiver when the user taps "I'm Back".
     * Exits away mode and immediately performs a detection scan.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object handleReturnHome(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object performDetection(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.String formatDurationForNotification(long ms) {
        return null;
    }
}
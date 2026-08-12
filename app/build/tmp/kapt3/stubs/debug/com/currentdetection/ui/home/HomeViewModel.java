package com.currentdetection.ui.home;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u008e\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B7\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\r\u00a2\u0006\u0002\u0010\u000eJ\u0016\u00104\u001a\u00020+2\f\u00105\u001a\b\u0012\u0004\u0012\u00020\u001f0$H\u0002J\u000e\u00106\u001a\u000207H\u0082@\u00a2\u0006\u0002\u00108R\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00130\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00130\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00170\u00160\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00110\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0019\u0010\u001e\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001f0\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u001dR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00130\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u001dR\u0017\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00130\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u001dR\u001d\u0010#\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020%0$0\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\u001dR\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\'\u001a\b\u0012\u0004\u0012\u00020(0\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010\u001dR\u0017\u0010*\u001a\b\u0012\u0004\u0012\u00020+0\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010\u001dR\u001d\u0010-\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020.0$0\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010\u001dR\u0017\u00100\u001a\b\u0012\u0004\u0012\u00020\u00190\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b1\u0010\u001dR\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u00102\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001f0$0\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u0010\u001dR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00069"}, d2 = {"Lcom/currentdetection/ui/home/HomeViewModel;", "Landroidx/lifecycle/ViewModel;", "eventManager", "Lcom/currentdetection/engine/EventManager;", "powerEventDao", "Lcom/currentdetection/data/local/PowerEventDao;", "networkRepository", "Lcom/currentdetection/domain/repository/NetworkRepository;", "settingsManager", "Lcom/currentdetection/data/local/SettingsManager;", "wifiScanner", "Lcom/currentdetection/wifi/WifiScanner;", "networkMatcher", "Lcom/currentdetection/engine/NetworkMatcher;", "(Lcom/currentdetection/engine/EventManager;Lcom/currentdetection/data/local/PowerEventDao;Lcom/currentdetection/domain/repository/NetworkRepository;Lcom/currentdetection/data/local/SettingsManager;Lcom/currentdetection/wifi/WifiScanner;Lcom/currentdetection/engine/NetworkMatcher;)V", "_activeOutageDurationMs", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_hasPerformedScan", "", "_isScanning", "_lastDetectedBssids", "", "", "_scanCountdown", "", "activeOutageDurationMs", "Lkotlinx/coroutines/flow/StateFlow;", "getActiveOutageDurationMs", "()Lkotlinx/coroutines/flow/StateFlow;", "activeOutageEvent", "Lcom/currentdetection/data/local/entities/PowerEventEntity;", "getActiveOutageEvent", "isMonitoringEnabled", "isScanning", "networkBreakdown", "", "Lcom/currentdetection/ui/home/NetworkStatus;", "getNetworkBreakdown", "powerState", "Lcom/currentdetection/engine/PowerState;", "getPowerState", "powerStats", "Lcom/currentdetection/ui/home/PowerStats;", "getPowerStats", "registeredNetworks", "Lcom/currentdetection/data/local/entities/NetworkEntity;", "getRegisteredNetworks", "scanCountdown", "getScanCountdown", "todayEvents", "getTodayEvents", "calculateStats", "events", "performFunctionalScan", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class HomeViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.currentdetection.engine.EventManager eventManager = null;
    @org.jetbrains.annotations.NotNull()
    private final com.currentdetection.data.local.PowerEventDao powerEventDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.currentdetection.domain.repository.NetworkRepository networkRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.currentdetection.data.local.SettingsManager settingsManager = null;
    @org.jetbrains.annotations.NotNull()
    private final com.currentdetection.wifi.WifiScanner wifiScanner = null;
    @org.jetbrains.annotations.NotNull()
    private final com.currentdetection.engine.NetworkMatcher networkMatcher = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.currentdetection.engine.PowerState> powerState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isMonitoringEnabled = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.currentdetection.data.local.entities.PowerEventEntity> activeOutageEvent = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.currentdetection.data.local.entities.NetworkEntity>> registeredNetworks = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Integer> _scanCountdown = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> scanCountdown = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isScanning = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isScanning = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Long> _activeOutageDurationMs = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Long> activeOutageDurationMs = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.Set<java.lang.String>> _lastDetectedBssids = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _hasPerformedScan = null;
    
    /**
     * VERDICT LOGIC:
     * Combines registered networks with the latest scan results to determine 
     * the status of each individual router/checker.
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.currentdetection.ui.home.NetworkStatus>> networkBreakdown = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.currentdetection.data.local.entities.PowerEventEntity>> todayEvents = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.currentdetection.ui.home.PowerStats> powerStats = null;
    
    public HomeViewModel(@org.jetbrains.annotations.NotNull()
    com.currentdetection.engine.EventManager eventManager, @org.jetbrains.annotations.NotNull()
    com.currentdetection.data.local.PowerEventDao powerEventDao, @org.jetbrains.annotations.NotNull()
    com.currentdetection.domain.repository.NetworkRepository networkRepository, @org.jetbrains.annotations.NotNull()
    com.currentdetection.data.local.SettingsManager settingsManager, @org.jetbrains.annotations.NotNull()
    com.currentdetection.wifi.WifiScanner wifiScanner, @org.jetbrains.annotations.NotNull()
    com.currentdetection.engine.NetworkMatcher networkMatcher) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.currentdetection.engine.PowerState> getPowerState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isMonitoringEnabled() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.currentdetection.data.local.entities.PowerEventEntity> getActiveOutageEvent() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.currentdetection.data.local.entities.NetworkEntity>> getRegisteredNetworks() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> getScanCountdown() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isScanning() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Long> getActiveOutageDurationMs() {
        return null;
    }
    
    /**
     * VERDICT LOGIC:
     * Combines registered networks with the latest scan results to determine 
     * the status of each individual router/checker.
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.currentdetection.ui.home.NetworkStatus>> getNetworkBreakdown() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.currentdetection.data.local.entities.PowerEventEntity>> getTodayEvents() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.currentdetection.ui.home.PowerStats> getPowerStats() {
        return null;
    }
    
    /**
     * Functional Scan Process:
     * 1. Fetches Targeting Networks (what we are looking for).
     * 2. Scans Environment (Wi-Fi scanning).
     * 3. Compares Results (Matching logic).
     * 4. Issues Verdict (Updates UI and Global State).
     */
    private final java.lang.Object performFunctionalScan(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final com.currentdetection.ui.home.PowerStats calculateStats(java.util.List<com.currentdetection.data.local.entities.PowerEventEntity> events) {
        return null;
    }
}
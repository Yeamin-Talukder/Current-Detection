package com.currentdetection.ui.home;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0084\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u001c\u0010@\u001a\b\u0012\u0004\u0012\u0002030)2\f\u0010A\u001a\b\u0012\u0004\u0012\u00020\u00150)H\u0002J\u0016\u0010B\u001a\u0002002\f\u0010A\u001a\b\u0012\u0004\u0012\u00020\u00150)H\u0002J\u0006\u0010C\u001a\u00020DJ\u0006\u0010E\u001a\u00020DJ\u0006\u0010F\u001a\u00020DJ\u0006\u0010G\u001a\u00020DR\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0013\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00150\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0017\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\r0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0017R\u0019\u0010\u001a\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00150\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0017R\u0017\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\r0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0017R\u0017\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\r0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0017R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010 \u001a\b\u0012\u0004\u0012\u00020\r0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0017R\u0017\u0010\"\u001a\b\u0012\u0004\u0012\u00020#0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0017R\u0017\u0010$\u001a\b\u0012\u0004\u0012\u00020#0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u0017R\u0017\u0010%\u001a\b\u0012\u0004\u0012\u00020#0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u0017R\u0017\u0010&\u001a\b\u0012\u0004\u0012\u00020\r0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010\u0017R\u001d\u0010(\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020*0)0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010\u0017R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010,\u001a\b\u0012\u0004\u0012\u00020-0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010\u0017R\u0017\u0010/\u001a\b\u0012\u0004\u0012\u0002000\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b1\u0010\u0017R\u001d\u00102\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002030)0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b4\u0010\u0017R\u001d\u00105\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002060)0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b7\u0010\u0017R\u0017\u00108\u001a\b\u0012\u0004\u0012\u00020\u00100\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b9\u0010\u0017R\u0017\u0010:\u001a\b\u0012\u0004\u0012\u00020\u00120\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b;\u0010\u0017R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010<\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00150)0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b=\u0010\u0017R\u001d\u0010>\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00150)0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b?\u0010\u0017\u00a8\u0006H"}, d2 = {"Lcom/currentdetection/ui/home/HomeViewModel;", "Landroidx/lifecycle/ViewModel;", "eventManager", "Lcom/currentdetection/engine/EventManager;", "powerEventDao", "Lcom/currentdetection/data/local/PowerEventDao;", "networkRepository", "Lcom/currentdetection/domain/repository/NetworkRepository;", "settingsManager", "Lcom/currentdetection/data/local/SettingsManager;", "(Lcom/currentdetection/engine/EventManager;Lcom/currentdetection/data/local/PowerEventDao;Lcom/currentdetection/domain/repository/NetworkRepository;Lcom/currentdetection/data/local/SettingsManager;)V", "_activeOutageDurationMs", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_awayDurationMs", "_scanCountdown", "", "_scanPhase", "Lcom/currentdetection/ui/home/ScanPhase;", "activeGapEvent", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/currentdetection/data/local/entities/PowerEventEntity;", "getActiveGapEvent", "()Lkotlinx/coroutines/flow/StateFlow;", "activeOutageDurationMs", "getActiveOutageDurationMs", "activeOutageEvent", "getActiveOutageEvent", "awayDurationMs", "getAwayDurationMs", "awayStartTime", "getAwayStartTime", "firstRunTime", "getFirstRunTime", "isAwayMode", "", "isMonitoringEnabled", "isScanning", "lastPowerOnTime", "getLastPowerOnTime", "networkBreakdown", "", "Lcom/currentdetection/ui/home/NetworkStatus;", "getNetworkBreakdown", "powerState", "Lcom/currentdetection/engine/PowerState;", "getPowerState", "powerStats", "Lcom/currentdetection/ui/home/PowerStats;", "getPowerStats", "recentOnSessions", "Lcom/currentdetection/ui/home/OnSession;", "getRecentOnSessions", "registeredNetworks", "Lcom/currentdetection/data/local/entities/NetworkEntity;", "getRegisteredNetworks", "scanCountdown", "getScanCountdown", "scanPhase", "getScanPhase", "todayEvents", "getTodayEvents", "todayOutages", "getTodayOutages", "buildOnSessions", "events", "calculateStats", "enterAwayMode", "", "markCurrentOutageAsUnknown", "returnHome", "startMonitoring", "app_debug"})
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
    private final kotlinx.coroutines.flow.StateFlow<com.currentdetection.engine.PowerState> powerState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isMonitoringEnabled = null;
    
    /**
     * True while the user is explicitly away from home.
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isAwayMode = null;
    
    /**
     * Timestamp when Away Mode was activated. 0L if not away.
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Long> awayStartTime = null;
    
    /**
     * Live counter — how many milliseconds the user has been away.
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Long> _awayDurationMs = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Long> awayDurationMs = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.currentdetection.data.local.entities.PowerEventEntity> activeOutageEvent = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.currentdetection.data.local.entities.PowerEventEntity> activeGapEvent = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.currentdetection.data.local.entities.NetworkEntity>> registeredNetworks = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Integer> _scanCountdown = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> scanCountdown = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.currentdetection.ui.home.ScanPhase> _scanPhase = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.currentdetection.ui.home.ScanPhase> scanPhase = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isScanning = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Long> _activeOutageDurationMs = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Long> activeOutageDurationMs = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Long> lastPowerOnTime = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Long> firstRunTime = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.currentdetection.ui.home.NetworkStatus>> networkBreakdown = null;
    
    /**
     * All events today (outages + away gaps combined for timeline rendering).
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.currentdetection.data.local.entities.PowerEventEntity>> todayEvents = null;
    
    /**
     * Only confirmed outage events (no away gaps) — used for stats and outage list.
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.currentdetection.data.local.entities.PowerEventEntity>> todayOutages = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.currentdetection.ui.home.PowerStats> powerStats = null;
    
    /**
     * Recent power-ON sessions (gaps between outages and away periods)
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.currentdetection.ui.home.OnSession>> recentOnSessions = null;
    
    public HomeViewModel(@org.jetbrains.annotations.NotNull()
    com.currentdetection.engine.EventManager eventManager, @org.jetbrains.annotations.NotNull()
    com.currentdetection.data.local.PowerEventDao powerEventDao, @org.jetbrains.annotations.NotNull()
    com.currentdetection.domain.repository.NetworkRepository networkRepository, @org.jetbrains.annotations.NotNull()
    com.currentdetection.data.local.SettingsManager settingsManager) {
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
    
    /**
     * True while the user is explicitly away from home.
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isAwayMode() {
        return null;
    }
    
    /**
     * Timestamp when Away Mode was activated. 0L if not away.
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Long> getAwayStartTime() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Long> getAwayDurationMs() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.currentdetection.data.local.entities.PowerEventEntity> getActiveOutageEvent() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.currentdetection.data.local.entities.PowerEventEntity> getActiveGapEvent() {
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
    public final kotlinx.coroutines.flow.StateFlow<com.currentdetection.ui.home.ScanPhase> getScanPhase() {
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
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Long> getLastPowerOnTime() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Long> getFirstRunTime() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.currentdetection.ui.home.NetworkStatus>> getNetworkBreakdown() {
        return null;
    }
    
    /**
     * All events today (outages + away gaps combined for timeline rendering).
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.currentdetection.data.local.entities.PowerEventEntity>> getTodayEvents() {
        return null;
    }
    
    /**
     * Only confirmed outage events (no away gaps) — used for stats and outage list.
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.currentdetection.data.local.entities.PowerEventEntity>> getTodayOutages() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.currentdetection.ui.home.PowerStats> getPowerStats() {
        return null;
    }
    
    /**
     * Recent power-ON sessions (gaps between outages and away periods)
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.currentdetection.ui.home.OnSession>> getRecentOnSessions() {
        return null;
    }
    
    public final void enterAwayMode() {
    }
    
    public final void returnHome() {
    }
    
    public final void markCurrentOutageAsUnknown() {
    }
    
    public final void startMonitoring() {
    }
    
    private final java.util.List<com.currentdetection.ui.home.OnSession> buildOnSessions(java.util.List<com.currentdetection.data.local.entities.PowerEventEntity> events) {
        return null;
    }
    
    private final com.currentdetection.ui.home.PowerStats calculateStats(java.util.List<com.currentdetection.data.local.entities.PowerEventEntity> events) {
        return null;
    }
}
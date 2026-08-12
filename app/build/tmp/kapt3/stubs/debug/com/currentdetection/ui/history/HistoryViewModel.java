package com.currentdetection.ui.history;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J$\u0010\r\u001a\b\u0012\u0004\u0012\u00020\n0\t2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\t2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002J\u0010\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u0011H\u0002R\u001d\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/currentdetection/ui/history/HistoryViewModel;", "Landroidx/lifecycle/ViewModel;", "powerEventDao", "Lcom/currentdetection/data/local/PowerEventDao;", "settingsManager", "Lcom/currentdetection/data/local/SettingsManager;", "(Lcom/currentdetection/data/local/PowerEventDao;Lcom/currentdetection/data/local/SettingsManager;)V", "dailyReports", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/currentdetection/ui/history/DailyReport;", "getDailyReports", "()Lkotlinx/coroutines/flow/StateFlow;", "buildDailyReports", "events", "Lcom/currentdetection/data/local/entities/PowerEventEntity;", "firstRunTime", "", "getDayStart", "timeMs", "app_debug"})
public final class HistoryViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.currentdetection.data.local.PowerEventDao powerEventDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.currentdetection.data.local.SettingsManager settingsManager = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.currentdetection.ui.history.DailyReport>> dailyReports = null;
    
    public HistoryViewModel(@org.jetbrains.annotations.NotNull()
    com.currentdetection.data.local.PowerEventDao powerEventDao, @org.jetbrains.annotations.NotNull()
    com.currentdetection.data.local.SettingsManager settingsManager) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.currentdetection.ui.history.DailyReport>> getDailyReports() {
        return null;
    }
    
    private final java.util.List<com.currentdetection.ui.history.DailyReport> buildDailyReports(java.util.List<com.currentdetection.data.local.entities.PowerEventEntity> events, long firstRunTime) {
        return null;
    }
    
    private final long getDayStart(long timeMs) {
        return 0L;
    }
}
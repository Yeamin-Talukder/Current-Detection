package com.currentdetection.ui.statistics;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J \u0010\u000e\u001a\u0004\u0018\u00010\u00072\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u00102\u0006\u0010\u0012\u001a\u00020\u0013H\u0002R\u0019\u0010\u0005\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0019\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\tR\u0019\u0010\f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\t\u00a8\u0006\u0014"}, d2 = {"Lcom/currentdetection/ui/statistics/StatisticsViewModel;", "Landroidx/lifecycle/ViewModel;", "powerEventDao", "Lcom/currentdetection/data/local/PowerEventDao;", "(Lcom/currentdetection/data/local/PowerEventDao;)V", "statsMonthly", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/currentdetection/ui/statistics/StatisticsData;", "getStatsMonthly", "()Lkotlinx/coroutines/flow/StateFlow;", "statsToday", "getStatsToday", "statsWeekly", "getStatsWeekly", "calculateStats", "events", "", "Lcom/currentdetection/data/local/entities/PowerEventEntity;", "period", "", "app_debug"})
public final class StatisticsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.currentdetection.ui.statistics.StatisticsData> statsToday = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.currentdetection.ui.statistics.StatisticsData> statsWeekly = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.currentdetection.ui.statistics.StatisticsData> statsMonthly = null;
    
    public StatisticsViewModel(@org.jetbrains.annotations.NotNull()
    com.currentdetection.data.local.PowerEventDao powerEventDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.currentdetection.ui.statistics.StatisticsData> getStatsToday() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.currentdetection.ui.statistics.StatisticsData> getStatsWeekly() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.currentdetection.ui.statistics.StatisticsData> getStatsMonthly() {
        return null;
    }
    
    private final com.currentdetection.ui.statistics.StatisticsData calculateStats(java.util.List<com.currentdetection.data.local.entities.PowerEventEntity> events, java.lang.String period) {
        return null;
    }
}
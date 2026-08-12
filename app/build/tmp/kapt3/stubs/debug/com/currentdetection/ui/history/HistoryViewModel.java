package com.currentdetection.ui.history;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002R)\u0010\u0005\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\b\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0011"}, d2 = {"Lcom/currentdetection/ui/history/HistoryViewModel;", "Landroidx/lifecycle/ViewModel;", "powerEventDao", "Lcom/currentdetection/data/local/PowerEventDao;", "(Lcom/currentdetection/data/local/PowerEventDao;)V", "historyEvents", "Lkotlinx/coroutines/flow/StateFlow;", "", "", "", "Lcom/currentdetection/data/local/entities/PowerEventEntity;", "getHistoryEvents", "()Lkotlinx/coroutines/flow/StateFlow;", "getStartOfDay", "", "calendar", "Ljava/util/Calendar;", "app_debug"})
public final class HistoryViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.Map<java.lang.String, java.util.List<com.currentdetection.data.local.entities.PowerEventEntity>>> historyEvents = null;
    
    public HistoryViewModel(@org.jetbrains.annotations.NotNull()
    com.currentdetection.data.local.PowerEventDao powerEventDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.Map<java.lang.String, java.util.List<com.currentdetection.data.local.entities.PowerEventEntity>>> getHistoryEvents() {
        return null;
    }
    
    private final long getStartOfDay(java.util.Calendar calendar) {
        return 0L;
    }
}
package com.currentdetection.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001c\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bJ\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0002\u00a8\u0006\u000e"}, d2 = {"Lcom/currentdetection/utils/DataExporter;", "", "()V", "exportCSV", "", "context", "Landroid/content/Context;", "events", "", "Lcom/currentdetection/data/local/entities/PowerEventEntity;", "formatDuration", "", "millis", "", "app_debug"})
public final class DataExporter {
    @org.jetbrains.annotations.NotNull()
    public static final com.currentdetection.utils.DataExporter INSTANCE = null;
    
    private DataExporter() {
        super();
    }
    
    public final void exportCSV(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.util.List<com.currentdetection.data.local.entities.PowerEventEntity> events) {
    }
    
    private final java.lang.String formatDuration(long millis) {
        return null;
    }
}
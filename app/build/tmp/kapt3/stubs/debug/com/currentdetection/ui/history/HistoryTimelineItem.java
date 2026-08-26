package com.currentdetection.ui.history;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0003\u0007\b\tB\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002R\u0012\u0010\u0003\u001a\u00020\u0004X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006\u0082\u0001\u0003\n\u000b\f\u00a8\u0006\r"}, d2 = {"Lcom/currentdetection/ui/history/HistoryTimelineItem;", "", "()V", "sortTime", "", "getSortTime", "()J", "Away", "Outage", "PowerOn", "Lcom/currentdetection/ui/history/HistoryTimelineItem$Away;", "Lcom/currentdetection/ui/history/HistoryTimelineItem$Outage;", "Lcom/currentdetection/ui/history/HistoryTimelineItem$PowerOn;", "app_debug"})
public abstract class HistoryTimelineItem {
    
    private HistoryTimelineItem() {
        super();
    }
    
    public abstract long getSortTime();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u00d6\u0003J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001J\t\u0010\u0013\u001a\u00020\u0014H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\bX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0015"}, d2 = {"Lcom/currentdetection/ui/history/HistoryTimelineItem$Away;", "Lcom/currentdetection/ui/history/HistoryTimelineItem;", "event", "Lcom/currentdetection/data/local/entities/PowerEventEntity;", "(Lcom/currentdetection/data/local/entities/PowerEventEntity;)V", "getEvent", "()Lcom/currentdetection/data/local/entities/PowerEventEntity;", "sortTime", "", "getSortTime", "()J", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
    public static final class Away extends com.currentdetection.ui.history.HistoryTimelineItem {
        @org.jetbrains.annotations.NotNull()
        private final com.currentdetection.data.local.entities.PowerEventEntity event = null;
        private final long sortTime = 0L;
        
        public Away(@org.jetbrains.annotations.NotNull()
        com.currentdetection.data.local.entities.PowerEventEntity event) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.currentdetection.data.local.entities.PowerEventEntity getEvent() {
            return null;
        }
        
        @java.lang.Override()
        public long getSortTime() {
            return 0L;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.currentdetection.data.local.entities.PowerEventEntity component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.currentdetection.ui.history.HistoryTimelineItem.Away copy(@org.jetbrains.annotations.NotNull()
        com.currentdetection.data.local.entities.PowerEventEntity event) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u00d6\u0003J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001J\t\u0010\u0013\u001a\u00020\u0014H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\bX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0015"}, d2 = {"Lcom/currentdetection/ui/history/HistoryTimelineItem$Outage;", "Lcom/currentdetection/ui/history/HistoryTimelineItem;", "event", "Lcom/currentdetection/data/local/entities/PowerEventEntity;", "(Lcom/currentdetection/data/local/entities/PowerEventEntity;)V", "getEvent", "()Lcom/currentdetection/data/local/entities/PowerEventEntity;", "sortTime", "", "getSortTime", "()J", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
    public static final class Outage extends com.currentdetection.ui.history.HistoryTimelineItem {
        @org.jetbrains.annotations.NotNull()
        private final com.currentdetection.data.local.entities.PowerEventEntity event = null;
        private final long sortTime = 0L;
        
        public Outage(@org.jetbrains.annotations.NotNull()
        com.currentdetection.data.local.entities.PowerEventEntity event) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.currentdetection.data.local.entities.PowerEventEntity getEvent() {
            return null;
        }
        
        @java.lang.Override()
        public long getSortTime() {
            return 0L;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.currentdetection.data.local.entities.PowerEventEntity component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.currentdetection.ui.history.HistoryTimelineItem.Outage copy(@org.jetbrains.annotations.NotNull()
        com.currentdetection.data.local.entities.PowerEventEntity event) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0005J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\f\u001a\u00020\u0003H\u00c6\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u00d6\u0003J\t\u0010\u0012\u001a\u00020\u0013H\u00d6\u0001J\t\u0010\u0014\u001a\u00020\u0015H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0014\u0010\b\u001a\u00020\u0003X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0007\u00a8\u0006\u0016"}, d2 = {"Lcom/currentdetection/ui/history/HistoryTimelineItem$PowerOn;", "Lcom/currentdetection/ui/history/HistoryTimelineItem;", "startMs", "", "endMs", "(JJ)V", "getEndMs", "()J", "sortTime", "getSortTime", "getStartMs", "component1", "component2", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
    public static final class PowerOn extends com.currentdetection.ui.history.HistoryTimelineItem {
        private final long startMs = 0L;
        private final long endMs = 0L;
        private final long sortTime = 0L;
        
        public PowerOn(long startMs, long endMs) {
        }
        
        public final long getStartMs() {
            return 0L;
        }
        
        public final long getEndMs() {
            return 0L;
        }
        
        @java.lang.Override()
        public long getSortTime() {
            return 0L;
        }
        
        public final long component1() {
            return 0L;
        }
        
        public final long component2() {
            return 0L;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.currentdetection.ui.history.HistoryTimelineItem.PowerOn copy(long startMs, long endMs) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
}
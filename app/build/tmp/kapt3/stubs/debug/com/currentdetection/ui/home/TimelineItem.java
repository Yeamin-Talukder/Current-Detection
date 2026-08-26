package com.currentdetection.ui.home;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0003\u0007\b\tB\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002R\u0012\u0010\u0003\u001a\u00020\u0004X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006\u0082\u0001\u0003\n\u000b\f\u00a8\u0006\r"}, d2 = {"Lcom/currentdetection/ui/home/TimelineItem;", "", "()V", "sortTime", "", "getSortTime", "()J", "Away", "Outage", "PowerOn", "Lcom/currentdetection/ui/home/TimelineItem$Away;", "Lcom/currentdetection/ui/home/TimelineItem$Outage;", "Lcom/currentdetection/ui/home/TimelineItem$PowerOn;", "app_debug"})
public abstract class TimelineItem {
    
    private TimelineItem() {
        super();
    }
    
    public abstract long getSortTime();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u00d6\u0003J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001J\t\u0010\u0013\u001a\u00020\u0014H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\bX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0015"}, d2 = {"Lcom/currentdetection/ui/home/TimelineItem$Away;", "Lcom/currentdetection/ui/home/TimelineItem;", "event", "Lcom/currentdetection/data/local/entities/PowerEventEntity;", "(Lcom/currentdetection/data/local/entities/PowerEventEntity;)V", "getEvent", "()Lcom/currentdetection/data/local/entities/PowerEventEntity;", "sortTime", "", "getSortTime", "()J", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
    public static final class Away extends com.currentdetection.ui.home.TimelineItem {
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
        public final com.currentdetection.ui.home.TimelineItem.Away copy(@org.jetbrains.annotations.NotNull()
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u00d6\u0003J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001J\t\u0010\u0013\u001a\u00020\u0014H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\bX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0015"}, d2 = {"Lcom/currentdetection/ui/home/TimelineItem$Outage;", "Lcom/currentdetection/ui/home/TimelineItem;", "event", "Lcom/currentdetection/data/local/entities/PowerEventEntity;", "(Lcom/currentdetection/data/local/entities/PowerEventEntity;)V", "getEvent", "()Lcom/currentdetection/data/local/entities/PowerEventEntity;", "sortTime", "", "getSortTime", "()J", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
    public static final class Outage extends com.currentdetection.ui.home.TimelineItem {
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
        public final com.currentdetection.ui.home.TimelineItem.Outage copy(@org.jetbrains.annotations.NotNull()
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u00d6\u0003J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001J\t\u0010\u0013\u001a\u00020\u0014H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\bX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0015"}, d2 = {"Lcom/currentdetection/ui/home/TimelineItem$PowerOn;", "Lcom/currentdetection/ui/home/TimelineItem;", "session", "Lcom/currentdetection/ui/home/OnSession;", "(Lcom/currentdetection/ui/home/OnSession;)V", "getSession", "()Lcom/currentdetection/ui/home/OnSession;", "sortTime", "", "getSortTime", "()J", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
    public static final class PowerOn extends com.currentdetection.ui.home.TimelineItem {
        @org.jetbrains.annotations.NotNull()
        private final com.currentdetection.ui.home.OnSession session = null;
        private final long sortTime = 0L;
        
        public PowerOn(@org.jetbrains.annotations.NotNull()
        com.currentdetection.ui.home.OnSession session) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.currentdetection.ui.home.OnSession getSession() {
            return null;
        }
        
        @java.lang.Override()
        public long getSortTime() {
            return 0L;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.currentdetection.ui.home.OnSession component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.currentdetection.ui.home.TimelineItem.PowerOn copy(@org.jetbrains.annotations.NotNull()
        com.currentdetection.ui.home.OnSession session) {
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
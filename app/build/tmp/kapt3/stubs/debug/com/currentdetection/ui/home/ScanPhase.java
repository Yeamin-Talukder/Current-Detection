package com.currentdetection.ui.home;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0005\u0003\u0004\u0005\u0006\u0007B\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u0005\b\t\n\u000b\f\u00a8\u0006\r"}, d2 = {"Lcom/currentdetection/ui/home/ScanPhase;", "", "()V", "CheckingConnected", "Done", "Idle", "MatchingBssids", "ScanningNearby", "Lcom/currentdetection/ui/home/ScanPhase$CheckingConnected;", "Lcom/currentdetection/ui/home/ScanPhase$Done;", "Lcom/currentdetection/ui/home/ScanPhase$Idle;", "Lcom/currentdetection/ui/home/ScanPhase$MatchingBssids;", "Lcom/currentdetection/ui/home/ScanPhase$ScanningNearby;", "app_debug"})
public abstract class ScanPhase {
    
    private ScanPhase() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/currentdetection/ui/home/ScanPhase$CheckingConnected;", "Lcom/currentdetection/ui/home/ScanPhase;", "()V", "app_debug"})
    public static final class CheckingConnected extends com.currentdetection.ui.home.ScanPhase {
        @org.jetbrains.annotations.NotNull()
        public static final com.currentdetection.ui.home.ScanPhase.CheckingConnected INSTANCE = null;
        
        private CheckingConnected() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/currentdetection/ui/home/ScanPhase$Done;", "Lcom/currentdetection/ui/home/ScanPhase;", "()V", "app_debug"})
    public static final class Done extends com.currentdetection.ui.home.ScanPhase {
        @org.jetbrains.annotations.NotNull()
        public static final com.currentdetection.ui.home.ScanPhase.Done INSTANCE = null;
        
        private Done() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/currentdetection/ui/home/ScanPhase$Idle;", "Lcom/currentdetection/ui/home/ScanPhase;", "()V", "app_debug"})
    public static final class Idle extends com.currentdetection.ui.home.ScanPhase {
        @org.jetbrains.annotations.NotNull()
        public static final com.currentdetection.ui.home.ScanPhase.Idle INSTANCE = null;
        
        private Idle() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0013\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\u0002\u0010\u0005J\u000f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\u0019\u0010\t\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0001J\u0013\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u00d6\u0003J\t\u0010\u000e\u001a\u00020\u000fH\u00d6\u0001J\t\u0010\u0010\u001a\u00020\u0011H\u00d6\u0001R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\u0012"}, d2 = {"Lcom/currentdetection/ui/home/ScanPhase$MatchingBssids;", "Lcom/currentdetection/ui/home/ScanPhase;", "networks", "", "Lcom/currentdetection/data/local/entities/NetworkEntity;", "(Ljava/util/List;)V", "getNetworks", "()Ljava/util/List;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
    public static final class MatchingBssids extends com.currentdetection.ui.home.ScanPhase {
        @org.jetbrains.annotations.NotNull()
        private final java.util.List<com.currentdetection.data.local.entities.NetworkEntity> networks = null;
        
        public MatchingBssids(@org.jetbrains.annotations.NotNull()
        java.util.List<com.currentdetection.data.local.entities.NetworkEntity> networks) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.currentdetection.data.local.entities.NetworkEntity> getNetworks() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.currentdetection.data.local.entities.NetworkEntity> component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.currentdetection.ui.home.ScanPhase.MatchingBssids copy(@org.jetbrains.annotations.NotNull()
        java.util.List<com.currentdetection.data.local.entities.NetworkEntity> networks) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/currentdetection/ui/home/ScanPhase$ScanningNearby;", "Lcom/currentdetection/ui/home/ScanPhase;", "()V", "app_debug"})
    public static final class ScanningNearby extends com.currentdetection.ui.home.ScanPhase {
        @org.jetbrains.annotations.NotNull()
        public static final com.currentdetection.ui.home.ScanPhase.ScanningNearby INSTANCE = null;
        
        private ScanningNearby() {
        }
    }
}
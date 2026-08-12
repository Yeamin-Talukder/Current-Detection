package com.currentdetection.engine;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\"\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u0006\u00a8\u0006\n"}, d2 = {"Lcom/currentdetection/engine/NetworkMatcher;", "", "()V", "match", "Lcom/currentdetection/engine/MatchResult;", "scannedNetworks", "", "Lcom/currentdetection/wifi/ScanResultItem;", "registeredNetworks", "Lcom/currentdetection/domain/models/Network;", "app_debug"})
public final class NetworkMatcher {
    
    public NetworkMatcher() {
        super();
    }
    
    /**
     * Compares scanned BSSIDs against the enabled registered BSSIDs.
     * Performs case-insensitive matching for BSSIDs to ensure reliability.
     */
    @org.jetbrains.annotations.NotNull()
    public final com.currentdetection.engine.MatchResult match(@org.jetbrains.annotations.NotNull()
    java.util.List<com.currentdetection.wifi.ScanResultItem> scannedNetworks, @org.jetbrains.annotations.NotNull()
    java.util.List<com.currentdetection.domain.models.Network> registeredNetworks) {
        return null;
    }
}
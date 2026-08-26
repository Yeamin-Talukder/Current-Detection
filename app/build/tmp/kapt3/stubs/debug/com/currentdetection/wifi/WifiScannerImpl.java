package com.currentdetection.wifi;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\n\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0016J\u000e\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u0002J\u0014\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n0\rH\u0017R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/currentdetection/wifi/WifiScannerImpl;", "Lcom/currentdetection/wifi/WifiScanner;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "wifiManager", "Landroid/net/wifi/WifiManager;", "getConnectedBssid", "", "getScanResults", "", "Lcom/currentdetection/wifi/ScanResultItem;", "scanNearbyNetworks", "Lkotlinx/coroutines/flow/Flow;", "app_debug"})
public final class WifiScannerImpl implements com.currentdetection.wifi.WifiScanner {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final android.net.wifi.WifiManager wifiManager = null;
    
    public WifiScannerImpl(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @java.lang.Override()
    @kotlin.Suppress(names = {"DEPRECATION"})
    @android.annotation.SuppressLint(value = {"MissingPermission"})
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.currentdetection.wifi.ScanResultItem>> scanNearbyNetworks() {
        return null;
    }
    
    @kotlin.Suppress(names = {"DEPRECATION"})
    private final java.util.List<com.currentdetection.wifi.ScanResultItem> getScanResults() {
        return null;
    }
    
    @java.lang.Override()
    @kotlin.Suppress(names = {"DEPRECATION"})
    @org.jetbrains.annotations.Nullable()
    public java.lang.String getConnectedBssid() {
        return null;
    }
}
package com.currentdetection.ui.navigation;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0003\t\n\u000bB\u0017\b\u0004\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0005R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007\u0082\u0001\u0003\f\r\u000e\u00a8\u0006\u000f"}, d2 = {"Lcom/currentdetection/ui/navigation/BottomNavScreen;", "", "route", "", "title", "(Ljava/lang/String;Ljava/lang/String;)V", "getRoute", "()Ljava/lang/String;", "getTitle", "Home", "Networks", "Settings", "Lcom/currentdetection/ui/navigation/BottomNavScreen$Home;", "Lcom/currentdetection/ui/navigation/BottomNavScreen$Networks;", "Lcom/currentdetection/ui/navigation/BottomNavScreen$Settings;", "app_debug"})
public abstract class BottomNavScreen {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String route = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String title = null;
    
    private BottomNavScreen(java.lang.String route, java.lang.String title) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getRoute() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTitle() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/currentdetection/ui/navigation/BottomNavScreen$Home;", "Lcom/currentdetection/ui/navigation/BottomNavScreen;", "()V", "app_debug"})
    public static final class Home extends com.currentdetection.ui.navigation.BottomNavScreen {
        @org.jetbrains.annotations.NotNull()
        public static final com.currentdetection.ui.navigation.BottomNavScreen.Home INSTANCE = null;
        
        private Home() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/currentdetection/ui/navigation/BottomNavScreen$Networks;", "Lcom/currentdetection/ui/navigation/BottomNavScreen;", "()V", "app_debug"})
    public static final class Networks extends com.currentdetection.ui.navigation.BottomNavScreen {
        @org.jetbrains.annotations.NotNull()
        public static final com.currentdetection.ui.navigation.BottomNavScreen.Networks INSTANCE = null;
        
        private Networks() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/currentdetection/ui/navigation/BottomNavScreen$Settings;", "Lcom/currentdetection/ui/navigation/BottomNavScreen;", "()V", "app_debug"})
    public static final class Settings extends com.currentdetection.ui.navigation.BottomNavScreen {
        @org.jetbrains.annotations.NotNull()
        public static final com.currentdetection.ui.navigation.BottomNavScreen.Settings INSTANCE = null;
        
        private Settings() {
        }
    }
}
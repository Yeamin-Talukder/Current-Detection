package com.currentdetection.engine;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\"\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0006\u0018\u0000 *2\u00020\u0001:\u0001*B/\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\tJ6\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\r2\u0006\u0010\"\u001a\u00020\r2\u0006\u0010#\u001a\u00020\u00072\u0006\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020%H\u0082@\u00a2\u0006\u0002\u0010\'JN\u0010(\u001a\u00020 2\u0006\u0010\"\u001a\u00020\r2\b\b\u0002\u0010#\u001a\u00020\u00072\b\b\u0002\u0010$\u001a\u00020%2\b\b\u0002\u0010&\u001a\u00020%2\u000e\b\u0002\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f2\b\b\u0002\u0010\u001d\u001a\u00020\u0012H\u0086@\u00a2\u0006\u0002\u0010)R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00070\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000f0\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00070\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0017\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\r0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0016R\u001d\u0010\u0019\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000f0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0016R\u0010\u0010\u001b\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00120\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0016R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006+"}, d2 = {"Lcom/currentdetection/engine/EventManager;", "", "powerEventDao", "Lcom/currentdetection/data/local/PowerEventDao;", "settingsManager", "Lcom/currentdetection/data/local/SettingsManager;", "powerOffConfirmationMs", "", "powerOnConfirmationMs", "(Lcom/currentdetection/data/local/PowerEventDao;Lcom/currentdetection/data/local/SettingsManager;JJ)V", "_confirmedOnSinceMs", "Lkotlinx/coroutines/flow/MutableStateFlow;", "_currentState", "Lcom/currentdetection/engine/PowerState;", "_detectedBssids", "", "", "_scanPerformed", "", "confirmedOnSinceMs", "Lkotlinx/coroutines/flow/StateFlow;", "getConfirmedOnSinceMs", "()Lkotlinx/coroutines/flow/StateFlow;", "currentState", "getCurrentState", "detectedBssids", "getDetectedBssids", "pendingState", "pendingStateStartTime", "scanPerformed", "getScanPerformed", "handleStateTransition", "", "previousState", "newState", "currentTimeMs", "activeCheckerCount", "", "totalCheckerCount", "(Lcom/currentdetection/engine/PowerState;Lcom/currentdetection/engine/PowerState;JIILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "processNewState", "(Lcom/currentdetection/engine/PowerState;JIILjava/util/Set;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_debug"})
public final class EventManager {
    @org.jetbrains.annotations.NotNull()
    private final com.currentdetection.data.local.PowerEventDao powerEventDao = null;
    @org.jetbrains.annotations.Nullable()
    private final com.currentdetection.data.local.SettingsManager settingsManager = null;
    private final long powerOffConfirmationMs = 0L;
    private final long powerOnConfirmationMs = 0L;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.currentdetection.engine.EventManager INSTANCE;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.currentdetection.engine.PowerState> _currentState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.currentdetection.engine.PowerState> currentState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.Set<java.lang.String>> _detectedBssids = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.Set<java.lang.String>> detectedBssids = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _scanPerformed = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> scanPerformed = null;
    
    /**
     * Timestamp when power was last confirmed ON. 0L = unknown.
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Long> _confirmedOnSinceMs = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Long> confirmedOnSinceMs = null;
    @org.jetbrains.annotations.Nullable()
    private com.currentdetection.engine.PowerState pendingState;
    private long pendingStateStartTime = 0L;
    @org.jetbrains.annotations.NotNull()
    public static final com.currentdetection.engine.EventManager.Companion Companion = null;
    
    private EventManager(com.currentdetection.data.local.PowerEventDao powerEventDao, com.currentdetection.data.local.SettingsManager settingsManager, long powerOffConfirmationMs, long powerOnConfirmationMs) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.currentdetection.engine.PowerState> getCurrentState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.Set<java.lang.String>> getDetectedBssids() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getScanPerformed() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Long> getConfirmedOnSinceMs() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object processNewState(@org.jetbrains.annotations.NotNull()
    com.currentdetection.engine.PowerState newState, long currentTimeMs, int activeCheckerCount, int totalCheckerCount, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> detectedBssids, boolean scanPerformed, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object handleStateTransition(com.currentdetection.engine.PowerState previousState, com.currentdetection.engine.PowerState newState, long currentTimeMs, int activeCheckerCount, int totalCheckerCount, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\tR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/currentdetection/engine/EventManager$Companion;", "", "()V", "INSTANCE", "Lcom/currentdetection/engine/EventManager;", "getInstance", "powerEventDao", "Lcom/currentdetection/data/local/PowerEventDao;", "settingsManager", "Lcom/currentdetection/data/local/SettingsManager;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.currentdetection.engine.EventManager getInstance(@org.jetbrains.annotations.NotNull()
        com.currentdetection.data.local.PowerEventDao powerEventDao, @org.jetbrains.annotations.Nullable()
        com.currentdetection.data.local.SettingsManager settingsManager) {
            return null;
        }
    }
}
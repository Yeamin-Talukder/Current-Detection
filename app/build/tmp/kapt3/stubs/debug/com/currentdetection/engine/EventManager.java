package com.currentdetection.engine;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0005\u0018\u00002\u00020\u0001B!\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0007J6\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\n2\u0006\u0010\u0014\u001a\u00020\n2\u0006\u0010\u0015\u001a\u00020\u00052\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0017H\u0082@\u00a2\u0006\u0002\u0010\u0019J4\u0010\u001a\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\n2\b\b\u0002\u0010\u0015\u001a\u00020\u00052\b\b\u0002\u0010\u0016\u001a\u00020\u00172\b\b\u0002\u0010\u0018\u001a\u00020\u0017H\u0086@\u00a2\u0006\u0002\u0010\u001bR\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\n0\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0010\u0010\u000f\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/currentdetection/engine/EventManager;", "", "powerEventDao", "Lcom/currentdetection/data/local/PowerEventDao;", "powerOffConfirmationMs", "", "powerOnConfirmationMs", "(Lcom/currentdetection/data/local/PowerEventDao;JJ)V", "_currentState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/currentdetection/engine/PowerState;", "currentState", "Lkotlinx/coroutines/flow/StateFlow;", "getCurrentState", "()Lkotlinx/coroutines/flow/StateFlow;", "pendingState", "pendingStateStartTime", "handleStateTransition", "", "previousState", "newState", "currentTimeMs", "activeCheckerCount", "", "totalCheckerCount", "(Lcom/currentdetection/engine/PowerState;Lcom/currentdetection/engine/PowerState;JIILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "processNewState", "(Lcom/currentdetection/engine/PowerState;JIILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class EventManager {
    @org.jetbrains.annotations.NotNull()
    private final com.currentdetection.data.local.PowerEventDao powerEventDao = null;
    private final long powerOffConfirmationMs = 0L;
    private final long powerOnConfirmationMs = 0L;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.currentdetection.engine.PowerState> _currentState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.currentdetection.engine.PowerState> currentState = null;
    @org.jetbrains.annotations.Nullable()
    private com.currentdetection.engine.PowerState pendingState;
    private long pendingStateStartTime = 0L;
    
    public EventManager(@org.jetbrains.annotations.NotNull()
    com.currentdetection.data.local.PowerEventDao powerEventDao, long powerOffConfirmationMs, long powerOnConfirmationMs) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.currentdetection.engine.PowerState> getCurrentState() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object processNewState(@org.jetbrains.annotations.NotNull()
    com.currentdetection.engine.PowerState newState, long currentTimeMs, int activeCheckerCount, int totalCheckerCount, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object handleStateTransition(com.currentdetection.engine.PowerState previousState, com.currentdetection.engine.PowerState newState, long currentTimeMs, int activeCheckerCount, int totalCheckerCount, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}
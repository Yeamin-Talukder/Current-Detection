package com.currentdetection.engine;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\b\u00a8\u0006\t"}, d2 = {"Lcom/currentdetection/engine/PowerStateEngine;", "", "()V", "determineState", "Lcom/currentdetection/engine/PowerState;", "matchResult", "Lcom/currentdetection/engine/MatchResult;", "scanSuccessful", "", "app_debug"})
public final class PowerStateEngine {
    
    public PowerStateEngine() {
        super();
    }
    
    /**
     * Determines the power state based on the match result.
     *
     * POWER_ON: One or more registered networks are reliably detected.
     * POWER_OFF: Zero enabled Power Checkers are detected (out of those registered).
     * UNKNOWN: No registered networks at all, or scanner failed to provide results.
     */
    @org.jetbrains.annotations.NotNull()
    public final com.currentdetection.engine.PowerState determineState(@org.jetbrains.annotations.Nullable()
    com.currentdetection.engine.MatchResult matchResult, boolean scanSuccessful) {
        return null;
    }
}
package com.currentdetection.engine

enum class PowerState {
    POWER_ON,
    POWER_OFF,
    UNKNOWN
}

class PowerStateEngine {

    /**
     * Determines the power state based on the match result.
     * 
     * POWER_ON: One or more registered networks are reliably detected.
     * POWER_OFF: Zero enabled Power Checkers are detected (out of those registered).
     * UNKNOWN: No registered networks at all, or scanner failed to provide results.
     */
    fun determineState(matchResult: MatchResult?, scanSuccessful: Boolean): PowerState {
        if (!scanSuccessful || matchResult == null) {
            return PowerState.UNKNOWN
        }

        if (matchResult.totalRegistered == 0) {
            return PowerState.UNKNOWN
        }

        return if (matchResult.detectionCount > 0) {
            PowerState.POWER_ON
        } else {
            PowerState.POWER_OFF
        }
    }
}

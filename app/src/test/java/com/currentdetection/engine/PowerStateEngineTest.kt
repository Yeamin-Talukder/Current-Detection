package com.currentdetection.engine

import com.currentdetection.data.local.PowerEventDao
import com.currentdetection.data.local.entities.PowerEventEntity
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.*

class PowerStateEngineTest {

    @Test
    fun `Scenario 1 - All ON, Expected POWER_ON`() {
        val engine = PowerStateEngine()
        val matchResult = MatchResult(emptyList(), totalRegistered = 5, detectionCount = 5)
        
        val state = engine.determineState(matchResult, scanSuccessful = true)
        assertEquals(PowerState.POWER_ON, state)
    }

    @Test
    fun `Scenario 2 - All OFF, Expected POWER_OFF`() {
        val engine = PowerStateEngine()
        val matchResult = MatchResult(emptyList(), totalRegistered = 5, detectionCount = 0)
        
        val state = engine.determineState(matchResult, scanSuccessful = true)
        assertEquals(PowerState.POWER_OFF, state)
    }

    @Test
    fun `Scenario 3 - Partial ON, Expected POWER_ON`() {
        val engine = PowerStateEngine()
        val matchResult = MatchResult(emptyList(), totalRegistered = 5, detectionCount = 1) // C is ON
        
        val state = engine.determineState(matchResult, scanSuccessful = true)
        assertEquals(PowerState.POWER_ON, state)
    }

    @Test
    fun `Scenario 4 - Temporary Scan Failure, Expected UNKNOWN`() {
        val engine = PowerStateEngine()
        val matchResult = MatchResult(emptyList(), totalRegistered = 5, detectionCount = 0)
        
        // Simulating scan failure
        val state = engine.determineState(matchResult, scanSuccessful = false)
        assertEquals(PowerState.UNKNOWN, state)
    }

    @Test
    fun `Scenario 5 - ON to OFF to ON EventManager Transition`() = runTest {
        val mockDao = mock(PowerEventDao::class.java)
        
        // Setup mock to return a dummy active event when requested after OFF -> ON
        val dummyActiveEvent = PowerEventEntity(1, startTime = 1000L, endTime = null, duration = null, 0, 5)
        `when`(mockDao.getActiveOutageEvent()).thenReturn(null, dummyActiveEvent)

        val eventManager = EventManager(mockDao, powerOffConfirmationMs = 1000, powerOnConfirmationMs = 1000)

        // Initial State (UNKNOWN)
        assertEquals(PowerState.UNKNOWN, eventManager.currentState.value)

        // 1. Transition to ON (Instantly or after confirmation? Need confirmation)
        eventManager.processNewState(PowerState.POWER_ON, currentTimeMs = 0L)
        eventManager.processNewState(PowerState.POWER_ON, currentTimeMs = 1500L) // > 1000ms
        assertEquals(PowerState.POWER_ON, eventManager.currentState.value)

        // 2. Transition to OFF (starts outage)
        eventManager.processNewState(PowerState.POWER_OFF, currentTimeMs = 2000L)
        assertEquals(PowerState.POWER_ON, eventManager.currentState.value) // Still ON due to confirmation wait

        eventManager.processNewState(PowerState.POWER_OFF, currentTimeMs = 3500L)
        assertEquals(PowerState.POWER_OFF, eventManager.currentState.value) // Confirmed OFF

        // Verify start outage DB call
        verify(mockDao, times(1)).insertEvent(any(PowerEventEntity::class.java))

        // 3. Transition back to ON
        eventManager.processNewState(PowerState.POWER_ON, currentTimeMs = 4000L)
        assertEquals(PowerState.POWER_OFF, eventManager.currentState.value) // Still OFF due to wait

        eventManager.processNewState(PowerState.POWER_ON, currentTimeMs = 5500L)
        assertEquals(PowerState.POWER_ON, eventManager.currentState.value) // Confirmed ON

        // Verify end outage DB call
        verify(mockDao, times(1)).updateEvent(any(PowerEventEntity::class.java))
    }
}

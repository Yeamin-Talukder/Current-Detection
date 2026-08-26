package com.currentdetection.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.currentdetection.data.local.entities.PowerEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PowerEventDao {
    @Query("SELECT * FROM power_events ORDER BY startTime DESC")
    fun getAllEvents(): Flow<List<PowerEventEntity>>

    /** Returns the active (open-ended) confirmed power-OFF outage event, if any. */
    @Query("SELECT * FROM power_events WHERE endTime IS NULL AND isUnknownGap = 0 ORDER BY startTime DESC LIMIT 1")
    suspend fun getActiveOutageEvent(): PowerEventEntity?

    /** Flow version for UI observation. */
    @Query("SELECT * FROM power_events WHERE endTime IS NULL AND isUnknownGap = 0 ORDER BY startTime DESC LIMIT 1")
    fun getActiveOutageEventFlow(): Flow<PowerEventEntity?>

    /** Returns any open-ended away-gap event (user left home, state unknown). */
    @Query("SELECT * FROM power_events WHERE endTime IS NULL AND isUnknownGap = 1 ORDER BY startTime DESC LIMIT 1")
    suspend fun getActiveGapEvent(): PowerEventEntity?

    /** Flow version for UI observation. */
    @Query("SELECT * FROM power_events WHERE endTime IS NULL AND isUnknownGap = 1 ORDER BY startTime DESC LIMIT 1")
    fun getActiveGapEventFlow(): Flow<PowerEventEntity?>

    @Query("SELECT * FROM power_events WHERE startTime >= :startTime AND startTime < :endTime ORDER BY startTime DESC")
    fun getEventsInRange(startTime: Long, endTime: Long): Flow<List<PowerEventEntity>>

    @Query("SELECT * FROM power_events ORDER BY startTime DESC")
    suspend fun getAllEventsList(): List<PowerEventEntity>

    @Query("DELETE FROM power_events")
    suspend fun clearAllEvents()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: PowerEventEntity): Long

    @Update
    suspend fun updateEvent(event: PowerEventEntity)

    @Delete
    suspend fun deleteEvent(event: PowerEventEntity)
}

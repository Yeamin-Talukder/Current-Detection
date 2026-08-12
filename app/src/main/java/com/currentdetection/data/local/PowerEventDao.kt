package com.currentdetection.data.local

import androidx.room.Dao
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

    @Query("SELECT * FROM power_events WHERE endTime IS NULL ORDER BY startTime DESC LIMIT 1")
    suspend fun getActiveOutageEvent(): PowerEventEntity?

    @Query("SELECT * FROM power_events WHERE endTime IS NULL ORDER BY startTime DESC LIMIT 1")
    fun getActiveOutageEventFlow(): Flow<PowerEventEntity?>

    @Query("SELECT * FROM power_events WHERE startTime >= :startTime AND startTime < :endTime ORDER BY startTime DESC")
    fun getEventsInRange(startTime: Long, endTime: Long): Flow<List<PowerEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: PowerEventEntity): Long

    @Update
    suspend fun updateEvent(event: PowerEventEntity)
}

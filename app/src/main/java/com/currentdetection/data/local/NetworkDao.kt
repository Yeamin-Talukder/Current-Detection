package com.currentdetection.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.currentdetection.data.local.entities.NetworkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NetworkDao {
    @Query("SELECT * FROM networks ORDER BY createdAt DESC")
    fun getAllNetworks(): Flow<List<NetworkEntity>>

    @Query("SELECT * FROM networks WHERE enabled = 1")
    fun getEnabledNetworks(): Flow<List<NetworkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNetwork(network: NetworkEntity): Long

    @Update
    suspend fun updateNetwork(network: NetworkEntity)

    @Delete
    suspend fun deleteNetwork(network: NetworkEntity)
}

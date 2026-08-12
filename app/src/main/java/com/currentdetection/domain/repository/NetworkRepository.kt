package com.currentdetection.domain.repository

import com.currentdetection.domain.models.Network
import kotlinx.coroutines.flow.Flow

interface NetworkRepository {
    fun getAllNetworks(): Flow<List<Network>>
    fun getEnabledNetworks(): Flow<List<Network>>
    suspend fun insertNetwork(network: Network): Long
    suspend fun updateNetwork(network: Network)
    suspend fun deleteNetwork(network: Network)
}

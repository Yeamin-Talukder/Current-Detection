package com.currentdetection.data.repository

import com.currentdetection.data.local.NetworkDao
import com.currentdetection.data.local.entities.NetworkEntity
import com.currentdetection.domain.models.Network
import com.currentdetection.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NetworkRepositoryImpl(private val dao: NetworkDao) : NetworkRepository {

    override fun getAllNetworks(): Flow<List<Network>> {
        return dao.getAllNetworks().map { entities -> entities.map { it.toDomain() } }
    }

    override fun getEnabledNetworks(): Flow<List<Network>> {
        return dao.getEnabledNetworks().map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun insertNetwork(network: Network): Long {
        return dao.insertNetwork(network.toEntity())
    }

    override suspend fun updateNetwork(network: Network) {
        dao.updateNetwork(network.toEntity())
    }

    override suspend fun deleteNetwork(network: Network) {
        dao.deleteNetwork(network.toEntity())
    }

    private fun NetworkEntity.toDomain() = Network(
        id = id,
        displayName = displayName,
        ssid = ssid,
        bssid = bssid,
        enabled = enabled,
        createdAt = createdAt
    )

    private fun Network.toEntity() = NetworkEntity(
        id = id,
        displayName = displayName,
        ssid = ssid,
        bssid = bssid,
        enabled = enabled,
        createdAt = createdAt
    )
}

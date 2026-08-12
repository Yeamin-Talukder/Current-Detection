package com.currentdetection.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "networks")
data class NetworkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val displayName: String,
    val ssid: String,
    val bssid: String,
    val enabled: Boolean,
    val createdAt: Long
)

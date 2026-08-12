package com.currentdetection.domain.models

data class Network(
    val id: Long = 0,
    val displayName: String,
    val ssid: String,
    val bssid: String,
    val enabled: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

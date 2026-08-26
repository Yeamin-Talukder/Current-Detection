package com.currentdetection.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "power_events")
data class PowerEventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val startTime: Long,
    val endTime: Long?,
    val duration: Long?, // in milliseconds
    val detectedCheckerCount: Int,
    val totalCheckerCount: Int,
    /**
     * When true, this event represents an "away gap" — a period where the user explicitly
     * left home, so we don't know the actual power state. These are shown differently in
     * the UI (grey/hatched) versus confirmed outages (red).
     */
    val isUnknownGap: Boolean = false
)

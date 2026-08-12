package com.currentdetection.engine

import com.currentdetection.domain.models.Network
import com.currentdetection.wifi.ScanResultItem
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkMatcherTest {

    @Test
    fun `test matching networks with single match`() {
        val matcher = NetworkMatcher()
        val registered = listOf(
            Network(1, "A", "SSID_A", "AA:BB"),
            Network(2, "B", "SSID_B", "CC:DD")
        )
        val scanned = listOf(
            ScanResultItem("SSID_A", "AA:BB", -50),
            ScanResultItem("Unknown", "XX:YY", -60)
        )

        val result = matcher.match(scanned, registered)

        assertEquals(1, result.detectionCount)
        assertEquals(2, result.totalRegistered)
        assertEquals("AA:BB", result.detectedNetworks.first().bssid)
    }

    @Test
    fun `test matching ignores disabled networks`() {
        val matcher = NetworkMatcher()
        val registered = listOf(
            Network(1, "A", "SSID_A", "AA:BB", enabled = false),
            Network(2, "B", "SSID_B", "CC:DD", enabled = true)
        )
        val scanned = listOf(
            ScanResultItem("SSID_A", "AA:BB", -50),
            ScanResultItem("SSID_B", "CC:DD", -60)
        )

        val result = matcher.match(scanned, registered)

        assertEquals(1, result.detectionCount) // Only B should be matched
        assertEquals(1, result.totalRegistered) // Only B is active
    }
}

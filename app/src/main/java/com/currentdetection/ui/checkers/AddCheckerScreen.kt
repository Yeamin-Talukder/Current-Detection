package com.currentdetection.ui.checkers

import android.Manifest
import android.content.Context
import android.net.wifi.WifiManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.currentdetection.data.local.AppDatabase
import com.currentdetection.data.local.entities.NetworkEntity
import com.currentdetection.ui.components.pressClickEffect
import com.currentdetection.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class WifiNetworkItem(
    val ssid: String,
    val bssid: String,
    val signal: String,
    val signalLevel: Int, // -100 to 0 dBm
    val isConnected: Boolean
)

@Composable
fun ScanningAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "scan_anim")

    val wave1Alpha by infiniteTransition.animateFloat(
        initialValue = 0.8f, targetValue = 0f,
        animationSpec = infiniteRepeatable(tween(1800, easing = LinearEasing), RepeatMode.Restart),
        label = "w1"
    )
    val wave1Scale by infiniteTransition.animateFloat(
        initialValue = 0.4f, targetValue = 1.4f,
        animationSpec = infiniteRepeatable(tween(1800, easing = LinearEasing), RepeatMode.Restart),
        label = "w1s"
    )
    val wave2Alpha by infiniteTransition.animateFloat(
        initialValue = 0.8f, targetValue = 0f,
        animationSpec = infiniteRepeatable(tween(1800, 600, easing = LinearEasing), RepeatMode.Restart),
        label = "w2"
    )
    val wave2Scale by infiniteTransition.animateFloat(
        initialValue = 0.4f, targetValue = 1.4f,
        animationSpec = infiniteRepeatable(tween(1800, 600, easing = LinearEasing), RepeatMode.Restart),
        label = "w2s"
    )
    val wave3Alpha by infiniteTransition.animateFloat(
        initialValue = 0.8f, targetValue = 0f,
        animationSpec = infiniteRepeatable(tween(1800, 1200, easing = LinearEasing), RepeatMode.Restart),
        label = "w3"
    )
    val wave3Scale by infiniteTransition.animateFloat(
        initialValue = 0.4f, targetValue = 1.4f,
        animationSpec = infiniteRepeatable(tween(1800, 1200, easing = LinearEasing), RepeatMode.Restart),
        label = "w3s"
    )

    var dots by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) { delay(500); dots = (dots + 1) % 4 }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(40.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(100.dp)) {
            // Ripple waves
            listOf(wave1Scale to wave1Alpha, wave2Scale to wave2Alpha, wave3Scale to wave3Alpha).forEach { (scale, alpha) ->
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .graphicsLayer { scaleX = scale; scaleY = scale; this.alpha = alpha }
                        .clip(CircleShape)
                        .background(PrimaryGreen.copy(alpha = 0.15f))
                )
            }
            // Center icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(SurfaceColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Wifi, contentDescription = null, tint = PrimaryGreen, modifier = Modifier.size(26.dp))
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Scanning" + ".".repeat(dots),
            color = PrimaryGreen,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Looking for nearby networks",
            color = MutedText,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCheckerScreen(onBack: () -> Unit, onCheckerAdded: () -> Unit) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var selectedNetwork by remember { mutableStateOf<WifiNetworkItem?>(null) }
    var checkerName by remember { mutableStateOf("") }
    var networks by remember { mutableStateOf(listOf<WifiNetworkItem>()) }
    var isScanning by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    val dao = remember { AppDatabase.getDatabase(context).networkDao() }

    // Get currently connected BSSID
    val wifiManager = remember {
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }
    val connectedBssid = remember {
        try {
            val info = wifiManager.connectionInfo
            val bssid = info?.bssid
            if (bssid != null && bssid != "02:00:00:00:00:00" && bssid != "<unknown ssid>") bssid else null
        } catch (e: SecurityException) { null }
    }

    val scanWifi = {
        isScanning = true
        coroutineScope.launch {
            wifiManager.startScan()
            delay(1500)
            val results = wifiManager.scanResults
            networks = results.mapNotNull { result ->
                val ssid = if (result.SSID.isNullOrEmpty()) "Hidden Network" else result.SSID
                val bssid = result.BSSID ?: return@mapNotNull null
                val signalLevel = result.level
                val signal = when {
                    signalLevel >= -50 -> "Strong"
                    signalLevel >= -70 -> "Medium"
                    else -> "Weak"
                }
                val isConn = connectedBssid != null && bssid.equals(connectedBssid, ignoreCase = true)
                WifiNetworkItem(ssid, bssid, signal, signalLevel, isConn)
            }.distinctBy { it.bssid }.sortedByDescending { it.signalLevel }
            isScanning = false
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.entries.all { it.value }
        if (granted) scanWifi() else isScanning = false
    }

    LaunchedEffect(Unit) {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (permissions.all { ContextCompat.checkSelfPermission(context, it) == android.content.pm.PackageManager.PERMISSION_GRANTED }) {
            scanWifi()
        } else {
            permissionLauncher.launch(permissions)
        }
    }

    Scaffold(containerColor = BackgroundColor) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Add Identifier",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        "Select your building's Wi-Fi network",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedText
                    )
                }
                IconButton(
                    onClick = { scanWifi() },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceColor)
                ) {
                    val rotation by animateFloatAsState(
                        targetValue = if (isScanning) 360f else 0f,
                        animationSpec = if (isScanning) infiniteRepeatable(tween(1000, easing = LinearEasing)) else tween(300),
                        label = "refresh_rotation"
                    )
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = if (isScanning) PrimaryGreen else MutedText,
                        modifier = Modifier.graphicsLayer { rotationZ = rotation }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            when {
                isScanning -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        ScanningAnimation()
                    }
                }
                networks.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("📡", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("No networks found", color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Check Wi-Fi is enabled and try again", color = MutedText, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
                else -> {
                    Text(
                        "Nearby Networks (${networks.size})",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MutedText,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        itemsIndexed(networks) { index, network ->
                            AnimatedVisibility(
                                visible = true,
                                enter = slideInVertically(
                                    initialOffsetY = { it / 2 },
                                    animationSpec = tween(350, delayMillis = index * 40)
                                ) + fadeIn(tween(350, delayMillis = index * 40))
                            ) {
                                NetworkScanCard(
                                    network = network,
                                    onSelect = {
                                        selectedNetwork = network
                                        checkerName = if (network.ssid == "Hidden Network") "" else network.ssid
                                        showDialog = true
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showDialog && selectedNetwork != null) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = {
                    Text(
                        "Name this Identifier",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column {
                        // Network info chip
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(BackgroundColor)
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Wifi, contentDescription = null, tint = PrimaryGreen, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(selectedNetwork!!.ssid, color = Color.White, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                                Text(selectedNetwork!!.bssid, color = MutedText, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Display Name", color = MutedText, style = MaterialTheme.typography.labelSmall, letterSpacing = 1.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = checkerName,
                            onValueChange = { checkerName = it },
                            placeholder = { Text("e.g. Main Router", color = MutedText) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = PrimaryGreen,
                                unfocusedBorderColor = CardBorderColor,
                                cursorColor = PrimaryGreen
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val entity = NetworkEntity(
                                    displayName = checkerName.ifEmpty { selectedNetwork!!.ssid.ifEmpty { "Router" } },
                                    ssid = selectedNetwork!!.ssid,
                                    bssid = selectedNetwork!!.bssid,
                                    enabled = true,
                                    createdAt = System.currentTimeMillis()
                                )
                                dao.insertNetwork(entity)
                                showDialog = false
                                onCheckerAdded()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Save", color = BackgroundColor, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel", color = MutedText)
                    }
                },
                containerColor = SurfaceColor,
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

@Composable
fun NetworkScanCard(network: WifiNetworkItem, onSelect: () -> Unit) {
    val signalColor = when {
        network.signalLevel >= -50 -> PrimaryGreen
        network.signalLevel >= -70 -> PowerUnknown
        else -> PowerOff
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .pressClickEffect(onSelect),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (network.isConnected) SurfaceColor else SurfaceColor
        ),
        border = BorderStroke(
            1.dp,
            if (network.isConnected) PrimaryGreen.copy(alpha = 0.3f) else CardBorderColor.copy(alpha = 0.2f)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left signal-color accent
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(72.dp)
                    .clip(RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp))
                    .background(signalColor.copy(alpha = 0.6f))
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Wifi,
                    contentDescription = null,
                    tint = signalColor,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            network.ssid,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                        if (network.isConnected) {
                            ConnectedBadge()
                        }
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        network.bssid,
                        color = MutedText.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(horizontalAlignment = Alignment.End) {
                    SignalBars(signalLevel = network.signalLevel)
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        network.signal,
                        style = MaterialTheme.typography.labelSmall,
                        color = signalColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun SignalBars(signalLevel: Int) {
    val bars = when {
        signalLevel >= -50 -> 4
        signalLevel >= -65 -> 3
        signalLevel >= -75 -> 2
        else -> 1
    }
    val color = when {
        signalLevel >= -50 -> PrimaryGreen
        signalLevel >= -65 -> PrimaryGreen.copy(alpha = 0.8f)
        signalLevel >= -75 -> PowerUnknown
        else -> PowerOff
    }

    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        repeat(4) { index ->
            val barHeight = (8 + index * 4).dp
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(barHeight)
                    .clip(RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp))
                    .background(if (index < bars) color else MutedText.copy(alpha = 0.25f))
            )
        }
    }
}
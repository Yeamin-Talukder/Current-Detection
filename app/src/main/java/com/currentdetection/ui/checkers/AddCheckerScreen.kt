package com.currentdetection.ui.checkers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.currentdetection.data.local.AppDatabase
import com.currentdetection.data.local.entities.NetworkEntity
import com.currentdetection.ui.components.PrimaryButton
import com.currentdetection.ui.components.pressClickEffect
import com.currentdetection.ui.theme.BackgroundColor
import com.currentdetection.ui.theme.PrimaryGreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ScanningAnimation() {
    var dots by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while(true) {
            delay(400)
            dots = (dots + 1) % 4
        }
    }

    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scan_pulse"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(32.dp)
    ) {
        Text(
            text = "📡",
            fontSize = 48.sp,
            modifier = Modifier.graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Scanning" + ".".repeat(dots),
            color = PrimaryGreen,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCheckerScreen(onBack: () -> Unit, onCheckerAdded: () -> Unit) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var selectedNetwork by remember { mutableStateOf<Pair<String, String>?>(null) }
    var checkerName by remember { mutableStateOf("") }

    var networks by remember { mutableStateOf(listOf<Triple<String, String, String>>()) }
    var isScanning by remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()
    val dao = remember { AppDatabase.getDatabase(context).networkDao() }

    val scanWifi = {
        isScanning = true
        coroutineScope.launch {
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            wifiManager.startScan()
            delay(1500) // Simulated delay for animation effect
            val results = wifiManager.scanResults
            networks = results.map { result ->
                val signalStr = when {
                    result.level >= -50 -> "Strong"
                    result.level >= -70 -> "Medium"
                    else -> "Weak"
                }
                val ssid = if (result.SSID.isNullOrEmpty()) "Hidden Network" else result.SSID
                Triple(ssid, result.BSSID ?: "", signalStr)
            }.distinctBy { it.second }
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
        if (permissions.all { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }) {
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
            Text(
                "Add Current Identifier",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButton(
                text = "Scan for Networks",
                onClick = { scanWifi() }
            )
            Spacer(modifier = Modifier.height(24.dp))

            if (isScanning) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    ScanningAnimation()
                }
            } else if (networks.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No networks found.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                Text(
                    "Nearby Networks",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(networks.size) { index ->
                        val network = networks[index]
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateContentSize()
                                .pressClickEffect {
                                    selectedNetwork = network.first to network.second
                                    checkerName = "" // Reset name field
                                    showDialog = true
                                },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, PrimaryGreen.copy(alpha = 0.2f)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    AnimatedWifiIcon(tint = PrimaryGreen)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(network.first, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.titleMedium)
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text("ADD", color = PrimaryGreen, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(network.second, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Signal: ${network.third}", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }

        if (showDialog && selectedNetwork != null) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Add Network", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onSurface) },
                text = {
                    Column {
                        Text("Network:", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                        Text(selectedNetwork!!.first, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("BSSID:", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                        Text(selectedNetwork!!.second, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Name", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                        OutlinedTextField(
                            value = checkerName,
                            onValueChange = { checkerName = it },
                            placeholder = { Text("Main Router") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            coroutineScope.launch {
                                val entity = NetworkEntity(
                                    displayName = checkerName.ifEmpty { "Main Router" },
                                    ssid = selectedNetwork!!.first,
                                    bssid = selectedNetwork!!.second,
                                    enabled = true,
                                    createdAt = System.currentTimeMillis()
                                )
                                dao.insertNetwork(entity)
                                showDialog = false
                                onCheckerAdded()
                            }
                        }
                    ) {
                        Text("Save", color = PrimaryGreen, style = MaterialTheme.typography.titleMedium)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}
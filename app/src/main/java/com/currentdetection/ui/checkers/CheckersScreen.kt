package com.currentdetection.ui.checkers

import android.net.wifi.WifiManager
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.outlined.WifiFind
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.currentdetection.data.local.AppDatabase
import com.currentdetection.data.local.entities.NetworkEntity
import com.currentdetection.ui.components.PrimaryButton
import com.currentdetection.ui.components.pressClickEffect
import com.currentdetection.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun AnimatedWifiIcon(modifier: Modifier = Modifier, tint: Color = PrimaryGreen) {
    val infiniteTransition = rememberInfiniteTransition(label = "wifi_pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wifi_alpha"
    )
    Icon(
        Icons.Default.Wifi,
        contentDescription = "Wi-Fi",
        modifier = modifier,
        tint = tint.copy(alpha = alpha)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckersScreen(onBack: () -> Unit, onAddChecker: () -> Unit) {
    val context = LocalContext.current
    val dao = remember { AppDatabase.getDatabase(context).networkDao() }
    val networks by dao.getAllNetworks().collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()

    val wifiScanner = remember { com.currentdetection.wifi.WifiScannerImpl(context) }
    val connectedBssid = remember { wifiScanner.getConnectedBssid() }

    Scaffold(
        containerColor = BackgroundColor,
        floatingActionButton = {
            if (networks.isNotEmpty()) {
                FloatingActionButton(
                    onClick = onAddChecker,
                    containerColor = PrimaryGreen,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.pressClickEffect(onAddChecker)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = BackgroundColor)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                androidx.compose.material3.IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(36.dp).offset(x = (-8).dp)
                ) {
                    androidx.compose.material3.Icon(
                        androidx.compose.material.icons.Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    "Current Identifiers",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Wi-Fi networks used to detect\nwhether current is available.",
                style = MaterialTheme.typography.bodyMedium,
                color = MutedText
            )
            Spacer(modifier = Modifier.height(24.dp))

            if (networks.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Floating animation
                    val infiniteTransition = rememberInfiniteTransition(label = "float")
                    val offsetY by infiniteTransition.animateFloat(
                        initialValue = -8f,
                        targetValue = 8f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(2000, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "float_y"
                    )

                    Icon(
                        Icons.Outlined.WifiFind,
                        contentDescription = null,
                        tint = MutedText,
                        modifier = Modifier
                            .size(64.dp)
                            .offset(y = offsetY.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No Networks Added",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Add Wi-Fi networks from your\nbuilding to use them as\nCurrent Identifiers.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MutedText
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    PrimaryButton(
                        text = "+ Add Network",
                        onClick = onAddChecker,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    itemsIndexed(networks, key = { _, item -> item.id }) { index, network ->
                        val isConnected = connectedBssid != null &&
                                network.bssid.equals(connectedBssid, ignoreCase = true)

                        AnimatedVisibility(
                            visible = true,
                            enter = slideInVertically(
                                initialOffsetY = { it / 2 },
                                animationSpec = tween(400, delayMillis = index * 60)
                            ) + fadeIn(tween(400, delayMillis = index * 60))
                        ) {
                            CheckerCard(
                                network = network,
                                isConnected = isConnected,
                                onDelete = {
                                    coroutineScope.launch { dao.deleteNetwork(network) }
                                },
                                onEdit = { newName ->
                                    coroutineScope.launch { dao.updateNetwork(network.copy(displayName = newName)) }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CheckerCard(
    network: NetworkEntity,
    isConnected: Boolean,
    onDelete: () -> Unit,
    onEdit: (String) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var editName by remember { mutableStateOf(network.displayName) }

    val borderColor = if (isConnected) PrimaryGreen.copy(alpha = 0.3f) else CardBorderColor.copy(alpha = 0.2f)
    val accentColor = if (isConnected) PrimaryGreen else MutedText.copy(alpha = 0.4f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .pressClickEffect { },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Left accent bar
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(if (isConnected) 100.dp else 90.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                    .background(accentColor)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, end = 8.dp, top = 14.dp, bottom = 14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Wifi,
                        contentDescription = null,
                        tint = if (isConnected) PrimaryGreen else MutedText,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        network.displayName,
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Box {
                        IconButton(onClick = { showMenu = true }, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More", tint = MutedText, modifier = Modifier.size(18.dp))
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier.background(SurfaceLighter)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Edit Name", color = Color.White) },
                                onClick = {
                                    showMenu = false
                                    editName = network.displayName
                                    showEditDialog = true
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Delete", color = PowerOff) },
                                onClick = {
                                    showMenu = false
                                    onDelete()
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            network.ssid,
                            color = MutedText,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            network.bssid,
                            color = MutedText.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    if (isConnected) {
                        ConnectedBadge()
                    }
                }
            }
        }
    }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = {
                Text("Edit Name", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            },
            text = {
                OutlinedTextField(
                    value = editName,
                    onValueChange = { editName = it },
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
            },
            confirmButton = {
                TextButton(onClick = {
                    onEdit(editName)
                    showEditDialog = false
                }) {
                    Text("Save", color = PrimaryGreen, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel", color = MutedText)
                }
            },
            containerColor = SurfaceColor,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
fun ConnectedBadge() {
    val infiniteTransition = rememberInfiniteTransition(label = "badge_pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "badge_alpha"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(PrimaryGreen.copy(alpha = 0.1f * alpha))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(PrimaryGreen.copy(alpha = alpha))
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            "CONNECTED",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = PrimaryGreen.copy(alpha = alpha),
            letterSpacing = 1.sp,
            fontSize = 10.sp
        )
    }
}

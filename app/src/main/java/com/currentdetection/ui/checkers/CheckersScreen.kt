package com.currentdetection.ui.checkers

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.currentdetection.data.local.AppDatabase
import com.currentdetection.data.local.entities.NetworkEntity
import com.currentdetection.ui.components.PrimaryButton
import com.currentdetection.ui.components.pressClickEffect
import com.currentdetection.ui.theme.BackgroundColor
import com.currentdetection.ui.theme.PrimaryGreen
import kotlinx.coroutines.launch

@Composable
fun AnimatedWifiIcon(modifier: Modifier = Modifier, tint: Color = MaterialTheme.colorScheme.primary) {
    val infiniteTransition = rememberInfiniteTransition(label = "wifi_pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
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

    Scaffold(
        containerColor = BackgroundColor,
        floatingActionButton = {
            if (networks.isNotEmpty()) {
                FloatingActionButton(
                    onClick = onAddChecker,
                    containerColor = PrimaryGreen,
                    modifier = Modifier.pressClickEffect(onAddChecker)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Power Checker", tint = Color(0xFF121212)) // Dark icon
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
            Text(
                "Current Identifiers",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Wi-Fi networks used to detect\nwhether current is available.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(24.dp))

            if (networks.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AnimatedWifiIcon(
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No Networks Added",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Add Wi-Fi networks from your\nbuilding to use them as\nCurrent Identifiers.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(networks, key = { it.id }) { network ->
                        CheckerCard(
                            network = network,
                            onDelete = {
                                coroutineScope.launch {
                                    dao.deleteNetwork(network)
                                }
                            },
                            onEdit = { newName ->
                                coroutineScope.launch {
                                    dao.updateNetwork(network.copy(displayName = newName))
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CheckerCard(network: NetworkEntity, onDelete: () -> Unit, onEdit: (String) -> Unit) {
    var showMenu by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var editName by remember { mutableStateOf(network.displayName) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .pressClickEffect { }, // Empty click just for the press animation
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, PrimaryGreen.copy(alpha = 0.2f)), // Subtle green highlight
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AnimatedWifiIcon(tint = PrimaryGreen)
                Spacer(modifier = Modifier.width(12.dp))
                Text(network.displayName, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.weight(1f))
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit Name", color = MaterialTheme.colorScheme.onSurface) },
                            onClick = {
                                showMenu = false
                                editName = network.displayName
                                showEditDialog = true
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete", color = MaterialTheme.colorScheme.error) },
                            onClick = {
                                showMenu = false
                                onDelete()
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(network.ssid, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(network.bssid, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
        }
    }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit Name", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.headlineMedium) },
            text = {
                OutlinedTextField(
                    value = editName,
                    onValueChange = { editName = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    onEdit(editName)
                    showEditDialog = false
                }) {
                    Text("Save", color = PrimaryGreen, style = MaterialTheme.typography.titleMedium)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

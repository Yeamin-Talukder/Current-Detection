package com.currentdetection.ui.settings

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.currentdetection.data.local.AppDatabase
import com.currentdetection.data.local.SettingsManager
import com.currentdetection.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onManageCheckers: () -> Unit, onExportHistory: () -> Unit = {}) {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val settingsManager = SettingsManager(context)
    val viewModel = viewModel<SettingsViewModel>(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel(settingsManager, database.powerEventDao()) as T
        }
    })

    val monitoringEnabled by viewModel.monitoringEnabled.collectAsState()
    val outageNotifications by viewModel.outageNotifications.collectAsState()
    val powerRestoredNotifications by viewModel.powerRestoredNotifications.collectAsState()
    val dailySummary by viewModel.dailySummary.collectAsState()

    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Settings",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            "Configure your monitoring",
                            style = MaterialTheme.typography.bodySmall,
                            color = MutedText
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundColor,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 32.dp, top = 8.dp)
        ) {
            // ─── Monitoring ─────────────────────────────
            item {
                SettingsSectionHeader("Monitoring", Icons.Outlined.Radar)
            }
            item {
                SettingsCard {
                    SwitchSettingRow(
                        icon = Icons.Outlined.Radar,
                        title = "Enable Monitoring",
                        subtitle = "Continuously check power status",
                        checked = monitoringEnabled,
                        onCheckedChange = { viewModel.toggleMonitoring(it) }
                    )
                    SettingsDivider()
                    InfoSettingRow(
                        icon = Icons.Outlined.Timer,
                        title = "Scan Interval",
                        value = "30 seconds"
                    )
                    SettingsDivider()
                    InfoSettingRow(
                        icon = Icons.Outlined.CheckCircle,
                        title = "Power-Off Confirmation",
                        value = "30 seconds"
                    )
                    SettingsDivider()
                    InfoSettingRow(
                        icon = Icons.Outlined.CheckCircle,
                        title = "Power-On Confirmation",
                        value = "15 seconds"
                    )
                }
            }

            // ─── Networks ───────────────────────────────
            item {
                SettingsSectionHeader("Networks", Icons.Outlined.Wifi)
            }
            item {
                SettingsCard {
                    ActionSettingRow(
                        icon = Icons.Outlined.Wifi,
                        title = "Manage Power Checkers",
                        subtitle = "Add or remove Wi-Fi identifiers",
                        onClick = onManageCheckers
                    )
                }
            }

            // ─── Notifications ──────────────────────────
            item {
                SettingsSectionHeader("Notifications", Icons.Outlined.Notifications)
            }
            item {
                SettingsCard {
                    SwitchSettingRow(
                        icon = Icons.Outlined.NotificationsOff,
                        title = "Outage Alerts",
                        subtitle = "Notify when power goes out",
                        checked = outageNotifications,
                        onCheckedChange = { viewModel.toggleOutageNotifications(it) }
                    )
                    SettingsDivider()
                    SwitchSettingRow(
                        icon = Icons.Outlined.Notifications,
                        title = "Power Restored Alerts",
                        subtitle = "Notify when power comes back",
                        checked = powerRestoredNotifications,
                        onCheckedChange = { viewModel.togglePowerRestoredNotifications(it) }
                    )
                    SettingsDivider()
                    SwitchSettingRow(
                        icon = Icons.Outlined.Summarize,
                        title = "Daily Summary",
                        subtitle = "Receive a daily power report",
                        checked = dailySummary,
                        onCheckedChange = { viewModel.toggleDailySummary(it) }
                    )
                }
            }

            // ─── Data ────────────────────────────────────
            item {
                SettingsSectionHeader("Data", Icons.Outlined.Storage)
            }
            item {
                SettingsCard {
                    ActionSettingRow(
                        icon = Icons.Outlined.FileDownload,
                        title = "Export History",
                        subtitle = "Save outage log as CSV",
                        onClick = onExportHistory
                    )
                    SettingsDivider()
                    ActionSettingRow(
                        icon = Icons.Outlined.DeleteOutline,
                        title = "Clear History",
                        subtitle = "Remove all recorded outages",
                        titleColor = PowerOff,
                        onClick = { }
                    )
                }
            }

            // ─── About ───────────────────────────────────
            item {
                SettingsSectionHeader("About", Icons.Outlined.Info)
            }
            item {
                SettingsCard {
                    InfoSettingRow(
                        icon = Icons.Outlined.Info,
                        title = "Version",
                        value = "1.0.0"
                    )
                    SettingsDivider()
                    InfoSettingRow(
                        icon = Icons.Outlined.Bolt,
                        title = "Detection Method",
                        value = "Hybrid BSSID Scan"
                    )
                }
            }

            // ─── Developer ───────────────────────────────
            item {
                SettingsSectionHeader("Developer", Icons.Outlined.Person)
            }
            item {
                val uriHandler = androidx.compose.ui.platform.LocalUriHandler.current
                SettingsCard {
                    InfoSettingRow(
                        icon = Icons.Outlined.Person,
                        title = "Developer Name",
                        value = "MD YEAMIN TALUKDER"
                    )
                    SettingsDivider()
                    ActionSettingRow(
                        icon = Icons.Outlined.Code,
                        title = "GitHub Profile",
                        subtitle = "github.com/Yeamin-Talukder",
                        onClick = { uriHandler.openUri("https://github.com/Yeamin-Talukder") }
                    )
                }
            }
            item {
                Text(
                    text = "Current Detection estimates electricity availability using registered Wi-Fi networks. It does not directly measure electrical power.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MutedText.copy(alpha = 0.7f),
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun SettingsSectionHeader(title: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = PrimaryGreen,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = PrimaryGreen,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )
    }
}

@Composable
fun SettingsCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(4.dp), content = content)
    }
}

@Composable
fun SettingsDivider() {
    Divider(
        modifier = Modifier.padding(horizontal = 16.dp),
        color = SubtleDivider.copy(alpha = 0.4f),
        thickness = 0.5.dp
    )
}

@Composable
fun SwitchSettingRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(if (checked) PrimaryGreen.copy(alpha = 0.1f) else SurfaceLighter.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = if (checked) PrimaryGreen else MutedText, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, color = Color.White)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MutedText)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = BackgroundColor,
                checkedTrackColor = PrimaryGreen,
                uncheckedThumbColor = MutedText,
                uncheckedTrackColor = SurfaceLighter
            )
        )
    }
}

@Composable
fun InfoSettingRow(icon: ImageVector, title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(SurfaceLighter.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = MutedText, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(14.dp))
        Text(title, style = MaterialTheme.typography.bodyMedium, color = Color.White, modifier = Modifier.weight(1f))
        Text(value, style = MaterialTheme.typography.bodyMedium, color = MutedText, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun ActionSettingRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    titleColor: Color = Color.White,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (titleColor == PowerOff) PowerOff.copy(alpha = 0.1f) else SurfaceLighter.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = if (titleColor == PowerOff) PowerOff else MutedText, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, color = titleColor)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MutedText)
            }
            Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = MutedText, modifier = Modifier.size(18.dp))
        }
    }
}

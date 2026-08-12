package com.currentdetection.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.currentdetection.data.local.AppDatabase
import com.currentdetection.data.local.SettingsManager

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
        topBar = {
            TopAppBar(title = { Text("Settings") })
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item { SettingsCategory("Monitoring") }
            item { SwitchSettingItem("Monitoring enabled", monitoringEnabled) { viewModel.toggleMonitoring(it) } }
            item { TextSettingItem("Scan interval", "1 minute") }
            item { TextSettingItem("Power-off confirmation", "30 seconds") }
            item { TextSettingItem("Power-on confirmation", "15 seconds") }

            item { SettingsCategory("Power Checkers") }
            item { 
                TextButton(
                    onClick = onManageCheckers,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("Manage Power Checkers")
                }
            }

            item { SettingsCategory("Notifications") }
            item { SwitchSettingItem("Outage notifications", outageNotifications) { viewModel.toggleOutageNotifications(it) } }
            item { SwitchSettingItem("Power restored notifications", powerRestoredNotifications) { viewModel.togglePowerRestoredNotifications(it) } }
            item { SwitchSettingItem("Daily summary", dailySummary) { viewModel.toggleDailySummary(it) } }

            item { SettingsCategory("Data") }
            item { 
                TextButton(onClick = onExportHistory, modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text("Export history")
                }
            }
            item { 
                TextButton(onClick = { }, modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text("Clear history", color = MaterialTheme.colorScheme.error)
                }
            }

            item { SettingsCategory("About") }
            item { TextSettingItem("About Current Detection", "Version 1.0") }
            item { TextSettingItem("How detection works", "Learn more") }
            item { 
                Text(
                    text = "Current Detection estimates electricity availability using registered Wi-Fi networks. It does not directly measure electrical power.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp)
                )
            }
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun SettingsCategory(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
    )
}

@Composable
fun SwitchSettingItem(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun TextSettingItem(title: String, subtitle: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(title, style = MaterialTheme.typography.bodyLarge)
        Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

package com.currentdetection.ui.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.currentdetection.data.local.AppDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen() {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val viewModel = viewModel<StatisticsViewModel>(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            return StatisticsViewModel(database.powerEventDao()) as T
        }
    })

    val statsToday by viewModel.statsToday.collectAsState()
    val statsWeekly by viewModel.statsWeekly.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Statistics") }) }
    ) { innerPadding ->
        if (statsToday == null && statsWeekly == null) {
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Not Enough Data", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Continue monitoring to generate\nstatistics.", textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                TabRow(selectedTabIndex = selectedTab) {
                    Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Today") })
                    Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Weekly") })
                    Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("Monthly") })
                }
                
                LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    val currentStats = when(selectedTab) {
                        0 -> statsToday
                        1 -> statsWeekly
                        else -> statsWeekly // Fallback for demo
                    }

                    if (currentStats != null) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Availability", fontWeight = FontWeight.Bold)
                                    Text("${currentStats.availabilityPercent}%", style = MaterialTheme.typography.displayMedium, color = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.height(16.dp))
                                    SummaryRow("Outages", "${currentStats.outagesCount}")
                                    SummaryRow("Total OFF", formatDuration(currentStats.totalOffMs))
                                    SummaryRow("Total ON", formatDuration(currentStats.totalOnMs))
                                    SummaryRow("Average outage", formatDuration(currentStats.averageOutageMs))
                                    SummaryRow("Longest outage", formatDuration(currentStats.longestOutageMs))
                                }
                            }
                        }

                        if (selectedTab > 0) {
                            item {
                                Text("Weekly Overview", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))
                                SimpleBarChart(data = currentStats.weeklyChartData)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontWeight = FontWeight.Bold)
    }
}

private fun formatDuration(millis: Long): String {
    val totalMins = millis / 60000
    val h = totalMins / 60
    val m = totalMins % 60
    return if (h > 0) "${h}h ${m}m" else "${m}m"
}

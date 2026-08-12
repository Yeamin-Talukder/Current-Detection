package com.currentdetection.ui.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.currentdetection.data.local.AppDatabase
import com.currentdetection.data.local.entities.PowerEventEntity
import com.currentdetection.ui.theme.PowerOff
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HistoryScreen() {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val viewModel = viewModel<HistoryViewModel>(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            return HistoryViewModel(database.powerEventDao()) as T
        }
    })

    val historyMap by viewModel.historyEvents.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("History") })
        }
    ) { innerPadding ->
        if (historyMap.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No Outages Recorded",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Current Detection will automatically\nrecord load-shedding events when\nmonitoring is active.",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                historyMap.forEach { (header, events) ->
                    stickyHeader {
                        Text(
                            text = header,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background)
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    items(events, key = { it.id }) { event ->
                        HistoryEventCard(event)
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryEventCard(event: PowerEventEntity) {
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val startTimeStr = timeFormat.format(Date(event.startTime))
    val endTimeStr = event.endTime?.let { timeFormat.format(Date(it)) } ?: "Ongoing"
    
    val durationStr = event.duration?.let { formatDuration(it) } ?: "Ongoing"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🔴", modifier = Modifier.padding(end = 8.dp))
                Text(
                    text = "$startTimeStr → $endTimeStr",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = durationStr,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 24.dp)
            )
        }
    }
}

private fun formatDuration(millis: Long): String {
    val totalMins = millis / 60000
    val h = totalMins / 60
    val m = totalMins % 60
    return if (h > 0) "${h} hours ${m} minutes" else "${m} minutes"
}

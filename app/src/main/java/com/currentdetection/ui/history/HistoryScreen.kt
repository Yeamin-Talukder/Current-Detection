package com.currentdetection.ui.history

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.currentdetection.data.local.AppDatabase
import com.currentdetection.data.local.SettingsManager
import com.currentdetection.data.local.entities.PowerEventEntity
import com.currentdetection.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max

@Composable
fun HistoryScreen() {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val settingsManager = remember { SettingsManager(context) }

    val viewModel = viewModel<HistoryViewModel>(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return HistoryViewModel(database.powerEventDao(), settingsManager) as T
            }
        }
    )

    val reports by viewModel.dailyReports.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .statusBarsPadding()
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                "Power History",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                "Daily electricity availability records",
                style = MaterialTheme.typography.bodySmall,
                color = MutedText
            )
        }

        if (reports.isEmpty()) {
            HistoryEmptyState()
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(reports, key = { it.dateMs }) { report ->
                    HistoryDayCard(report)
                }
                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }
}

@Composable
fun HistoryDayCard(report: DailyReport) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .animateContentSize(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
        border = BorderStroke(1.dp, CardBorderColor.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {

            // ── Header row ──────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Date badge
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(PowerOn.copy(alpha = 0.15f), PowerOn.copy(alpha = 0.05f))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.CalendarMonth,
                        contentDescription = null,
                        tint = PowerOn,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        report.dateLabel,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        "${report.outages.size} outage${if (report.outages.size != 1) "s" else ""}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedText
                    )
                }
                // Availability badge
                val availColor = when {
                    report.availabilityPct >= 80f -> PowerOn
                    report.availabilityPct >= 50f -> PowerUnknown
                    else -> PowerOff
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(availColor.copy(alpha = 0.12f))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        "${report.availabilityPct.toInt()}%",
                        fontWeight = FontWeight.Bold,
                        color = availColor,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ── Power timeline bar ──────────────────────────────
            DayPowerBar(report)

            Spacer(modifier = Modifier.height(10.dp))

            // ── Stats row ───────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MiniStatChip(
                    "ON",
                    formatDuration(report.totalOnTimeMs),
                    PowerOn,
                    modifier = Modifier.weight(1f)
                )
                MiniStatChip(
                    "OFF",
                    formatDuration(report.totalOutageMs),
                    PowerOff,
                    modifier = Modifier.weight(1f)
                )
                MiniStatChip(
                    "MONITORED",
                    formatDuration(report.monitoredMs),
                    MutedText,
                    modifier = Modifier.weight(1f)
                )
            }

            // ── Expandable outage list ─────────────────────────
            AnimatedVisibility(
                visible = expanded && report.outages.isNotEmpty(),
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = CardBorderColor.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "OUTAGE LOG",
                        style = MaterialTheme.typography.labelSmall,
                        color = MutedText,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    report.outages.forEach { event ->
                        OutageRow(event)
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }

            // Expand indicator
            if (report.outages.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                        contentDescription = null,
                        tint = MutedText,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DayPowerBar(report: DailyReport) {
    val now = System.currentTimeMillis()
    val dayStart = report.dateMs
    val dayEnd = dayStart + 86_400_000L
    val dayNow = minOf(dayEnd, now)
    val dayRange = dayEnd - dayStart

    // Pre-monitoring block width fraction
    val preMonitorFraction = if (report.isFirstDay && report.monitoredMs < dayRange) {
        (dayEnd - dayStart - report.monitoredMs).toFloat() / dayRange
    } else 0f

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(16.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        // Base: green (power ON)
        drawRoundRect(
            color = PowerOn.copy(alpha = 0.7f),
            size = size,
            cornerRadius = CornerRadius(8.dp.toPx())
        )

        // Grey UNKNOWN block before monitoring started
        if (preMonitorFraction > 0f) {
            drawRoundRect(
                color = Color(0xFF546E7A),
                size = Size(size.width * preMonitorFraction, size.height),
                cornerRadius = CornerRadius(8.dp.toPx())
            )
        }

        // Red outage blocks
        report.outages.forEach { event ->
            val eStart = max(event.startTime, dayStart)
            val eEnd = minOf(event.endTime ?: dayNow, dayNow)
            if (eEnd > eStart) {
                val startX = ((eStart - dayStart).toFloat() / dayRange) * size.width
                val endX = ((eEnd - dayStart).toFloat() / dayRange) * size.width
                drawRoundRect(
                    color = PowerOff,
                    topLeft = Offset(startX, 0f),
                    size = Size(max(4f, endX - startX), size.height),
                    cornerRadius = CornerRadius(4.dp.toPx())
                )
            }
        }

        // Future grey (rest of today)
        if (dayNow < dayEnd) {
            val futureStartX = ((dayNow - dayStart).toFloat() / dayRange) * size.width
            drawRoundRect(
                color = Color(0xFF37474F),
                topLeft = Offset(futureStartX, 0f),
                size = Size(size.width - futureStartX, size.height),
                cornerRadius = CornerRadius(8.dp.toPx())
            )
        }
    }

    // Time labels below bar
    Spacer(modifier = Modifier.height(4.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        listOf("12A", "6A", "12P", "6P", "12A").forEach {
            Text(it, fontSize = 9.sp, color = MutedText.copy(alpha = 0.6f))
        }
    }

    // Legend
    Spacer(modifier = Modifier.height(6.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        LegendDot(PowerOn, "Power ON")
        LegendDot(PowerOff, "Power OFF")
        if (report.isFirstDay) LegendDot(Color(0xFF546E7A), "Unknown")
    }
}

@Composable
fun LegendDot(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(label, fontSize = 10.sp, color = MutedText)
    }
}

@Composable
fun MiniStatChip(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(color.copy(alpha = 0.07f))
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = color)
        Text(label, fontSize = 9.sp, color = MutedText, letterSpacing = 1.sp)
    }
}

@Composable
fun OutageRow(event: PowerEventEntity) {
    val timeFmt = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val start = timeFmt.format(Date(event.startTime))
    val end = event.endTime?.let { timeFmt.format(Date(it)) } ?: "Ongoing"
    val dur = event.endTime?.let { formatDuration(it - event.startTime) } ?: "—"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(PowerOff.copy(alpha = 0.06f))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(PowerOff)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            "$start → $end",
            color = Color.White,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Text(
            dur,
            color = PowerOff,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun HistoryEmptyState() {
    val infiniteTransition = rememberInfiniteTransition(label = "empty_pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f, targetValue = 1.05f,
        animationSpec = infiniteRepeatable(tween(1800, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulse_scale"
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Outlined.HistoryEdu,
            contentDescription = null,
            modifier = Modifier.size((64 * scale).dp),
            tint = PowerOn.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            "No History Yet",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Daily power records will appear\nhere once monitoring begins.",
            style = MaterialTheme.typography.bodySmall,
            color = MutedText,
            textAlign = TextAlign.Center
        )
    }
}

private fun formatDuration(millis: Long): String {
    val totalSeconds = millis / 1000
    val h = totalSeconds / 3600
    val m = (totalSeconds % 3600) / 60
    return if (h > 0) "${h}h ${m}m" else "${m}m"
}

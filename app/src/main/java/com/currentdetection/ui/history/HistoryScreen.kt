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
    val database = remember { AppDatabase.getDatabase(context) }
    val settingsManager = remember { SettingsManager(context) }

    val viewModel = viewModel<HistoryViewModel>(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T =
                HistoryViewModel(database.powerEventDao(), settingsManager) as T
        }
    )

    val reports by viewModel.dailyReports.collectAsState()
    // Recompose every minute so live durations update
    var tick by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(60_000)
            tick = System.currentTimeMillis()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .statusBarsPadding()
    ) {
        // ── Header ──────────────────────────────────────────────
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
            // Compute all-time summary inline
            val totalOutages = reports.sumOf { it.completedOutages.size + if (it.activeOutage != null) 1 else 0 }
            val totalOnMs = reports.sumOf { it.totalOnTimeMs }
            val totalOffMs = reports.sumOf { it.totalOutageMs }
            val avgAvail = if (reports.isNotEmpty()) reports.map { it.availabilityPct }.average().toFloat() else 0f
            val longestMs = (reports.flatMap { r ->
                r.completedOutages + listOfNotNull(r.activeOutage)
            }.maxOfOrNull { it.duration ?: (if (it.endTime == null) System.currentTimeMillis() - it.startTime else 0L) } ?: 0L)

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // All-time summary card
                item {
                    AllTimeSummaryCard(
                        dayCount = reports.size,
                        totalOutages = totalOutages,
                        totalOnMs = totalOnMs,
                        totalOffMs = totalOffMs,
                        longestOutageMs = longestMs,
                        avgAvailability = avgAvail
                    )
                }

                // Per-day cards
                items(reports, key = { it.dateMs }) { report ->
                    HistoryDayCard(report, tick)
                }

                item { Spacer(modifier = Modifier.height(32.dp)) }
            }
        }
    }
}

// ─── ALL-TIME SUMMARY CARD ──────────────────────────────────────
@Composable
fun AllTimeSummaryCard(
    dayCount: Int,
    totalOutages: Int,
    totalOnMs: Long,
    totalOffMs: Long,
    longestOutageMs: Long,
    avgAvailability: Float
) {
    val availColor = when {
        avgAvailability >= 80f -> PowerOn
        avgAvailability >= 50f -> PowerUnknown
        else -> PowerOff
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
        border = BorderStroke(1.dp, PowerOn.copy(alpha = 0.15f))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.BarChart, contentDescription = null, tint = PowerOn, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("All-Time Overview", fontWeight = FontWeight.Bold, color = Color.White, style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(availColor.copy(alpha = 0.12f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        "${avgAvailability.toInt()}% avg",
                        color = availColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SummaryStatBox("DAYS", "$dayCount", MutedText, Modifier.weight(1f))
                SummaryStatBox("OUTAGES", "$totalOutages", PowerOff, Modifier.weight(1f))
                SummaryStatBox("LONGEST", formatDuration(longestOutageMs), PowerOff, Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SummaryStatBox("TOTAL ON", formatDuration(totalOnMs), PowerOn, Modifier.weight(1f))
                SummaryStatBox("TOTAL OFF", formatDuration(totalOffMs), PowerOff, Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun SummaryStatBox(label: String, value: String, color: Color, modifier: Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(color.copy(alpha = 0.06f))
            .padding(vertical = 10.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, fontWeight = FontWeight.Bold, color = color, fontSize = 14.sp)
        Text(label, color = MutedText, fontSize = 8.sp, letterSpacing = 1.sp)
    }
}

// ─── DAY CARD ───────────────────────────────────────────────────
@Composable
fun HistoryDayCard(report: DailyReport, tick: Long) {
    var expanded by remember { mutableStateOf(report.isToday) }
    val hasActiveOutage = report.activeOutage != null
    val allOutages = report.completedOutages + listOfNotNull(report.activeOutage)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .animateContentSize(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
        border = BorderStroke(1.dp,
            if (hasActiveOutage) PowerOff.copy(alpha = 0.3f)
            else CardBorderColor.copy(alpha = 0.2f)
        )
    ) {
        Column(modifier = Modifier.padding(18.dp)) {

            // ── Header row ──────────────────────────────────────
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (hasActiveOutage)
                                Brush.linearGradient(listOf(PowerOff.copy(alpha = 0.2f), PowerOff.copy(alpha = 0.08f)))
                            else
                                Brush.linearGradient(listOf(PowerOn.copy(alpha = 0.15f), PowerOn.copy(alpha = 0.05f)))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (hasActiveOutage) Icons.Outlined.FlashOff else Icons.Outlined.CalendarMonth,
                        contentDescription = null,
                        tint = if (hasActiveOutage) PowerOff else PowerOn,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            report.dateLabel,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        if (hasActiveOutage) {
                            Spacer(modifier = Modifier.width(6.dp))
                            // Live pulsing red dot
                            val infiniteTransition = rememberInfiniteTransition(label = "live_dot")
                            val pulseAlpha by infiniteTransition.animateFloat(
                                initialValue = 0.5f, targetValue = 1f,
                                animationSpec = infiniteRepeatable(tween(700), RepeatMode.Reverse),
                                label = "pulse"
                            )
                            Box(
                                modifier = Modifier.size(8.dp).clip(CircleShape).background(PowerOff.copy(alpha = pulseAlpha))
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("LIVE", color = PowerOff, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        }
                    }
                    Text(
                        buildString {
                            append("${allOutages.size} outage${if (allOutages.size != 1) "s" else ""}")
                            if (hasActiveOutage) append(" • outage ongoing")
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = if (hasActiveOutage) PowerOff.copy(alpha = 0.8f) else MutedText
                    )
                }
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
                    Text("${report.availabilityPct.toInt()}%", fontWeight = FontWeight.Bold, color = availColor, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ── Power timeline bar ──────────────────────────────
            DayPowerBar(report, tick)

            Spacer(modifier = Modifier.height(10.dp))

            // ── Stats row ───────────────────────────────────────
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MiniStatChip("ON", formatDuration(report.totalOnTimeMs), PowerOn, Modifier.weight(1f))
                MiniStatChip("OFF", formatDuration(report.totalOutageMs), PowerOff, Modifier.weight(1f))
                MiniStatChip("MONITORED", formatDuration(report.monitoredMs), MutedText, Modifier.weight(1f))
            }

            // ── Expandable outage list ─────────────────────────
            AnimatedVisibility(
                visible = expanded && allOutages.isNotEmpty(),
                enter = fadeIn(tween(250)) + expandVertically(tween(300, easing = FastOutSlowInEasing)),
                exit = fadeOut(tween(200)) + shrinkVertically(tween(250))
            ) {
                Column {
                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = CardBorderColor.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("OUTAGE LOG", style = MaterialTheme.typography.labelSmall, color = MutedText, letterSpacing = 2.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    allOutages.forEach { event ->
                        OutageRow(event, tick)
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }

            // Expand indicator
            if (allOutages.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
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

// ─── POWER BAR ──────────────────────────────────────────────────
@Composable
fun DayPowerBar(report: DailyReport, tick: Long) {
    val now = if (report.isToday) System.currentTimeMillis() else report.dateMs + 86_400_000L
    val dayStart = report.dateMs
    val dayEnd = dayStart + 86_400_000L
    val dayNow = minOf(dayEnd, now)
    val dayRange = dayEnd - dayStart

    // How much of the bar is "UNKNOWN" (before monitoring started)
    val preMonitorFraction = if (report.isFirstDay && report.monitoredMs < dayRange) {
        (dayEnd - dayStart - report.monitoredMs).toFloat() / dayRange
    } else 0f

    val allOutages = report.completedOutages + listOfNotNull(report.activeOutage)

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(18.dp)
            .clip(RoundedCornerShape(9.dp))
    ) {
        // Base — green (power ON within monitored window)
        val greenStartX = preMonitorFraction * size.width
        if (greenStartX > 0f) {
            drawRoundRect(
                color = Color(0xFF546E7A),
                size = Size(greenStartX, size.height),
                cornerRadius = CornerRadius(9.dp.toPx())
            )
        }
        drawRoundRect(
            brush = Brush.horizontalGradient(
                listOf(PowerOn.copy(alpha = 0.65f), PowerOn.copy(alpha = 0.85f)),
                startX = greenStartX, endX = size.width
            ),
            topLeft = Offset(greenStartX, 0f),
            size = Size(size.width - greenStartX, size.height),
            cornerRadius = CornerRadius(9.dp.toPx())
        )

        // Red outage blocks
        allOutages.forEach { event ->
            val eStart = maxOf(event.startTime, dayStart).toFloat()
            val eEnd = minOf(event.endTime ?: dayNow, dayNow).toFloat()
            if (eEnd > eStart) {
                val x0 = ((eStart - dayStart) / dayRange) * size.width
                val x1 = ((eEnd - dayStart) / dayRange) * size.width
                drawRoundRect(
                    color = PowerOff,
                    topLeft = Offset(x0, 0f),
                    size = Size(max(6f, x1 - x0), size.height),
                    cornerRadius = CornerRadius(4.dp.toPx())
                )
            }
        }

        // Future (rest of today — dark grey)
        if (report.isToday && dayNow < dayEnd) {
            val futureX = ((dayNow - dayStart).toFloat() / dayRange) * size.width
            drawRoundRect(
                color = Color(0xFF263238),
                topLeft = Offset(futureX, 0f),
                size = Size(size.width - futureX, size.height),
                cornerRadius = CornerRadius(9.dp.toPx())
            )
        }
    }

    Spacer(modifier = Modifier.height(4.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        listOf("12A", "6A", "12P", "6P", "12A").forEach {
            Text(it, fontSize = 9.sp, color = MutedText.copy(alpha = 0.6f))
        }
    }

    // Legend
    Spacer(modifier = Modifier.height(6.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
        LegendDot(PowerOn, "Power ON")
        LegendDot(PowerOff, "Power OFF")
        if (report.isFirstDay) LegendDot(Color(0xFF546E7A), "Unknown")
        if (report.isToday) LegendDot(Color(0xFF263238), "Future")
    }
}

@Composable
fun LegendDot(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
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

// ─── OUTAGE ROW ─────────────────────────────────────────────────
@Composable
fun OutageRow(event: PowerEventEntity, tick: Long) {
    val timeFmt = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val start = timeFmt.format(Date(event.startTime))
    val isOngoing = event.endTime == null
    val end = if (isOngoing) "Ongoing" else timeFmt.format(Date(event.endTime!!))
    val durationMs = if (isOngoing) System.currentTimeMillis() - event.startTime else event.duration ?: 0L
    val dur = formatDuration(durationMs)

    val infiniteTransition = rememberInfiniteTransition(label = "outage_row")
    val bgAlpha by if (isOngoing) {
        infiniteTransition.animateFloat(
            initialValue = 0.05f, targetValue = 0.12f,
            animationSpec = infiniteRepeatable(tween(900, easing = FastOutSlowInEasing), RepeatMode.Reverse),
            label = "bg_alpha"
        )
    } else {
        infiniteTransition.animateFloat(
            initialValue = 0.06f, targetValue = 0.06f,
            animationSpec = infiniteRepeatable(tween(10000), RepeatMode.Restart),
            label = "static_bg"
        )
    }
    val dotAlpha by if (isOngoing) {
        infiniteTransition.animateFloat(
            initialValue = 0.5f, targetValue = 1f,
            animationSpec = infiniteRepeatable(tween(700), RepeatMode.Reverse),
            label = "dot_pulse"
        )
    } else {
        infiniteTransition.animateFloat(
            initialValue = 0.7f, targetValue = 0.7f,
            animationSpec = infiniteRepeatable(tween(10000), RepeatMode.Restart),
            label = "static_dot"
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(PowerOff.copy(alpha = bgAlpha))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(PowerOff.copy(alpha = dotAlpha)))
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                "$start → $end",
                color = Color.White,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium
            )
            if (isOngoing) {
                Text("Still ongoing…", color = PowerOff, fontSize = 10.sp)
            }
        }
        Text(dur, color = PowerOff, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
    }
}

// ─── EMPTY STATE ────────────────────────────────────────────────
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
        Text("No History Yet", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
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
    if (millis <= 0L) return "0m"
    val totalSeconds = millis / 1000
    val h = totalSeconds / 3600
    val m = (totalSeconds % 3600) / 60
    return if (h > 0) "${h}h ${m}m" else "${m}m"
}

package com.currentdetection.ui.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.currentdetection.data.local.AppDatabase
import com.currentdetection.data.local.SettingsManager
import com.currentdetection.data.local.entities.PowerEventEntity
import com.currentdetection.data.repository.NetworkRepositoryImpl
import com.currentdetection.engine.EventManager
import com.currentdetection.engine.PowerState
import com.currentdetection.ui.components.pressClickEffect
import com.currentdetection.ui.theme.*
import com.currentdetection.wifi.WifiScannerImpl
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max
import kotlin.math.min

@Composable
fun HomeScreen(onAddChecker: () -> Unit, onSettingsClick: () -> Unit) {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val networkRepository = NetworkRepositoryImpl(database.networkDao())
    val eventManager = EventManager(database.powerEventDao())
    val settingsManager = SettingsManager(context)
    val wifiScanner = remember { WifiScannerImpl(context) }

    val viewModel = viewModel<HomeViewModel>(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(
                eventManager,
                database.powerEventDao(),
                networkRepository,
                settingsManager,
                wifiScanner
            ) as T
        }
    })

    val powerState by viewModel.powerState.collectAsState()
    val isMonitoringActive by viewModel.isMonitoringEnabled.collectAsState()
    val registeredNetworks by viewModel.registeredNetworks.collectAsState()
    val scanCountdown by viewModel.scanCountdown.collectAsState()
    val isScanning by viewModel.isScanning.collectAsState()
    val activeOutageDurationMs by viewModel.activeOutageDurationMs.collectAsState()
    val networkBreakdown by viewModel.networkBreakdown.collectAsState()
    val stats by viewModel.powerStats.collectAsState()
    val todayEvents by viewModel.todayEvents.collectAsState()

    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            HomeHeader(
                isMonitoringActive = isMonitoringActive,
                onSettingsClick = onSettingsClick
            )
        }
    ) { innerPadding ->
        if (registeredNetworks.isEmpty()) {
            EmptyState(onAddChecker, modifier = Modifier.padding(innerPadding))
        } else {
            HomeScreenContent(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                powerState = powerState,
                scanCountdown = scanCountdown,
                isScanning = isScanning,
                outageDurationMs = activeOutageDurationMs,
                networkBreakdown = networkBreakdown,
                stats = stats,
                todayEvents = todayEvents
            )
        }
    }
}

@Composable
fun HomeHeader(isMonitoringActive: Boolean, onSettingsClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Current Detection",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Building Power Monitor",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onSettingsClick) {
                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        MonitoringIndicator(isActive = isMonitoringActive)
    }
}

@Composable
fun MonitoringIndicator(isActive: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "monitoring_pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(if (isActive) PowerOn.copy(alpha = alpha) else Color.Gray)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = if (isActive) "● Monitoring Active" else "● Monitoring Paused",
            style = MaterialTheme.typography.labelSmall,
            color = if (isActive) PowerOn else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun HomeScreenContent(
    modifier: Modifier,
    powerState: PowerState,
    scanCountdown: Int,
    isScanning: Boolean,
    outageDurationMs: Long,
    networkBreakdown: List<NetworkStatus>,
    stats: PowerStats,
    todayEvents: List<PowerEventEntity>
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        StatusCard(powerState, outageDurationMs, scanCountdown, isScanning)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        NetworkStatusSummary(networkBreakdown)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        NetworkBreakdownList(networkBreakdown)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        PowerTimelineSection(todayEvents)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        PowerSummarySection(stats)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        RecentLoadShedding(todayEvents)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        PowerAnalysisCard(stats)
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun StatusCard(state: PowerState, durationMs: Long, countdown: Int, isScanning: Boolean) {
    val color = when (state) {
        PowerState.POWER_ON -> PowerOn
        PowerState.POWER_OFF -> PowerOff
        PowerState.UNKNOWN -> PowerUnknown
    }

    val infiniteTransition = rememberInfiniteTransition(label = "status_glow")
    val glowScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor)
    ) {
        Column(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "CURRENT STATUS",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Box(contentAlignment = Alignment.Center) {
                // Glow/Pulse
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .scale(glowScale)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.15f))
                )
                
                Text(
                    text = if (state == PowerState.POWER_ON) "🟢" else if (state == PowerState.POWER_OFF) "🔴" else "🟡",
                    fontSize = 52.sp
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            AnimatedContent(
                targetState = state,
                transitionSpec = {
                    fadeIn(tween(300)) with fadeOut(tween(300))
                },
                label = "status_title"
            ) { targetState ->
                Text(
                    text = if (targetState == PowerState.POWER_OFF) "CURRENT OFF" else if (targetState == PowerState.POWER_ON) "CURRENT ON" else "STATUS UNKNOWN",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            
            Text(
                text = if (state == PowerState.POWER_OFF) "Load Shedding" else "Electricity Available",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            if (state == PowerState.POWER_OFF) {
                Text("OFF for", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    formatDuration(durationMs),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            } else {
                ScanCountdownTimer(countdown, isScanning)
            }
        }
    }
}

@Composable
fun ScanCountdownTimer(secondsLeft: Int, isScanning: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (isScanning) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val infiniteTransition = rememberInfiniteTransition(label = "wifi_pulse")
                val alpha by infiniteTransition.animateFloat(
                    initialValue = 0.3f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(800, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "wifi_alpha"
                )
                Icon(
                    Icons.Default.Wifi,
                    contentDescription = null,
                    tint = PowerUnknown.copy(alpha = alpha),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Scanning Networks...", color = PowerUnknown, fontWeight = FontWeight.Medium)
            }
        } else {
            Text("Next network scan in", color = MaterialTheme.colorScheme.onSurfaceVariant)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(80.dp)) {
                // Circular progress that fills up
                val progress = (30f - secondsLeft.toFloat()) / 30f
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxSize(),
                    color = PowerOn,
                    strokeWidth = 6.dp,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeCap = StrokeCap.Round
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        String.format("%02d", secondsLeft),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        "seconds",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Composable
fun NetworkStatusSummary(breakdown: List<NetworkStatus>) {
    val activeCount = breakdown.count { it.isActive }
    val inactiveCount = breakdown.size - activeCount

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Network Status",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SummarySmallCard(
                title = "ACTIVE",
                count = activeCount,
                color = PowerOn,
                modifier = Modifier.weight(1f)
            )
            SummarySmallCard(
                title = "NOT DETECTED",
                count = inactiveCount,
                color = PowerOff,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "${breakdown.size} Total Networks",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SummarySmallCard(title: String, count: Int, color: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(
                targetState = count,
                transitionSpec = {
                    (slideInVertically { it } + fadeIn(tween(400))) with (slideOutVertically { -it } + fadeOut(tween(400)))
                },
                label = "count_anim"
            ) { targetCount ->
                Text(
                    targetCount.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Text(title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(12.dp))
            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(color))
        }
    }
}

@Composable
fun NetworkBreakdownList(networks: List<NetworkStatus>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Current Identifiers",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(12.dp))
        networks.forEach { network ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .animateContentSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(if (network.isActive) PowerOn else PowerOff))
                Spacer(modifier = Modifier.width(16.dp))
                Text(network.name, color = Color.White, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
                Text(
                    if (network.isActive) "ACTIVE" else "OFFLINE",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (network.isActive) PowerOn else PowerOff
                )
            }
        }
    }
}

@Composable
fun PowerTimelineSection(events: List<PowerEventEntity>) {
    var selectedEvent by remember { mutableStateOf<PowerEventEntity?>(null) }
    
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Today's Power Timeline",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(SurfaceColor, RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .pointerInput(events) {
                    detectTapGestures { offset ->
                        val todayStart = Calendar.getInstance().apply {
                            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
                        }.timeInMillis
                        val dayMillis = 24 * 60 * 60 * 1000L
                        val tappedTime = todayStart + (offset.x / size.width) * dayMillis
                        
                        selectedEvent = events.find { event ->
                            val end = event.endTime ?: System.currentTimeMillis()
                            tappedTime >= event.startTime && tappedTime <= end
                        }
                    }
                }
        ) {
            TimelineCanvas(events)
            CurrentTimeIndicator()
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("12 AM", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("12 PM", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("12 AM", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }

    if (selectedEvent != null) {
        OutageDetailDialog(event = selectedEvent!!, onDismiss = { selectedEvent = null })
    }
}

@Composable
fun TimelineCanvas(events: List<PowerEventEntity>) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val todayStart = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        val dayMillis = 24 * 60 * 60 * 1000L
        
        // Default Background: ON (Green)
        drawRect(color = PowerOn, size = size)
        
        // Draw Outages: OFF (Red)
        events.forEach { event ->
            val eventStartToday = max(event.startTime, todayStart)
            val eventEndToday = min(event.endTime ?: System.currentTimeMillis(), todayStart + dayMillis)
            
            if (eventStartToday < eventEndToday) {
                val startX = ((eventStartToday - todayStart).toFloat() / dayMillis) * size.width
                val endX = ((eventEndToday - todayStart).toFloat() / dayMillis) * size.width
                drawRect(
                    color = PowerOff,
                    topLeft = Offset(startX, 0f),
                    size = Size(max(2f, endX - startX), size.height)
                )
            }
        }
    }
}

@Composable
fun CurrentTimeIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "indicator")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1200), RepeatMode.Reverse),
        label = "alpha"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val calendar = Calendar.getInstance()
        val minutesSinceStart = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)
        val progress = minutesSinceStart.toFloat() / (24 * 60f)
        val xPos = progress * size.width
        
        // Vertical glowing line
        drawLine(
            color = Color.White.copy(alpha = alpha),
            start = Offset(xPos, 0f),
            end = Offset(xPos, size.height),
            strokeWidth = 3.dp.toPx()
        )
        
        // Handle/Now indicator
        drawCircle(
            color = PowerOn,
            radius = 7.dp.toPx(),
            center = Offset(xPos, size.height / 2),
            style = Stroke(width = 2.dp.toPx())
        )
        drawCircle(
            color = Color.White,
            radius = 4.dp.toPx(),
            center = Offset(xPos, size.height / 2)
        )
    }
}

@Composable
fun PowerSummarySection(stats: PowerStats) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor)
    ) {
        Column(modifier = Modifier.padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Today's Summary", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
            
            Spacer(modifier = Modifier.height(28.dp))
            
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(140.dp)) {
                // Availability Donut Chart
                CircularProgressIndicator(
                    progress = { stats.availabilityPercentage / 100f },
                    modifier = Modifier.fillMaxSize(),
                    color = PowerOn,
                    strokeWidth = 12.dp,
                    trackColor = PowerOff,
                    strokeCap = StrokeCap.Round
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "${stats.availabilityPercentage.toInt()}%",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text("Availability", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            
            Spacer(modifier = Modifier.height(36.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                SummaryStatItem("ON TIME", formatDurationSimple(stats.totalOnTimeMs), PowerOn)
                SummaryStatItem("OFF TIME", formatDurationSimple(stats.totalOffTimeMs), PowerOff)
                SummaryStatItem("OUTAGES", stats.outageCount.toString(), Color.White)
            }
        }
    }
}

@Composable
fun SummaryStatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, letterSpacing = 1.sp)
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
fun RecentLoadShedding(events: List<PowerEventEntity>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Recent Load Shedding",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        if (events.isEmpty()) {
            Text("No outages recorded today.", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(vertical = 8.dp))
        } else {
            // Show latest 3 events, excluding current one if needed or showing all today
            events.filter { it.endTime != null }.take(3).forEach { event ->
                RecentEventItem(event)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun RecentEventItem(event: PowerEventEntity) {
    val timeFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val startTime = timeFormatter.format(Date(event.startTime))
    val endTime = event.endTime?.let { timeFormatter.format(Date(it)) } ?: "Ongoing"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(PowerOff))
            Spacer(modifier = Modifier.width(20.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("$startTime → $endTime", color = Color.White, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyLarge)
                Text("Duration: ${formatDurationSimple(event.duration ?: 0)}", 
                    style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text("TODAY", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PowerAnalysisCard(stats: PowerStats) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor)
    ) {
        Column(modifier = Modifier.padding(28.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("✨", fontSize = 22.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Power Analysis", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(24.dp))
            
            AnalysisItem("⚡", "${stats.availabilityPercentage.toInt()}% availability today")
            AnalysisItem("🔴", "${stats.outageCount} outages recorded")
            AnalysisItem("⏱", "Longest outage: ${formatDurationSimple(stats.longestOutageMs)}")
            AnalysisItem("🕐", "Peak outage period: ${stats.peakOutagePeriod}")
        }
    }
}

@Composable
fun AnalysisItem(icon: String, text: String) {
    Row(modifier = Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(icon, fontSize = 20.sp)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge, color = Color.White)
    }
}

@Composable
fun OutageDetailDialog(event: PowerEventEntity, onDismiss: () -> Unit) {
    val timeFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val start = timeFormatter.format(Date(event.startTime))
    val end = event.endTime?.let { timeFormatter.format(Date(it)) } ?: "Ongoing"
    val duration = formatDurationSimple(event.duration ?: (System.currentTimeMillis() - event.startTime))

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceColor)
        ) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Outage Details", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = PowerOff)
                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                
                DetailRowItem("Started", start)
                DetailRowItem("Restored", end)
                DetailRowItem("Duration", duration)
            }
        }
    }
}

@Composable
fun DetailRowItem(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
fun EmptyState(onAddChecker: () -> Unit, modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("📡", fontSize = 72.sp)
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "No Power Checkers Added",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Add Wi-Fi networks from your building\nto start monitoring electricity availability.",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 40.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onAddChecker,
            colors = ButtonDefaults.buttonColors(containerColor = PowerOn),
            modifier = Modifier.pressClickEffect(onAddChecker).height(50.dp).padding(horizontal = 24.dp)
        ) {
            Text("Add Power Checker", color = BackgroundColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

private fun formatDuration(millis: Long): String {
    val totalSeconds = millis / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

private fun formatDurationSimple(millis: Long): String {
    val totalSeconds = millis / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    if (hours > 0) return "${hours}h ${minutes}m"
    return "${minutes}m"
}

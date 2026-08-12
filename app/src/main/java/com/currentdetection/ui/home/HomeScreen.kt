package com.currentdetection.ui.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.outlined.ElectricBolt
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.TrendingDown
import androidx.compose.material.icons.outlined.WifiFind
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.currentdetection.data.local.entities.NetworkEntity
import com.currentdetection.data.local.entities.PowerEventEntity
import com.currentdetection.data.repository.NetworkRepositoryImpl
import com.currentdetection.engine.EventManager
import com.currentdetection.engine.PowerState
import com.currentdetection.ui.components.pressClickEffect
import com.currentdetection.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max
import kotlin.math.min

@Composable
fun HomeScreen(onAddChecker: () -> Unit, onSettingsClick: () -> Unit) {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val networkRepository = NetworkRepositoryImpl(database.networkDao())
    val settingsManager = SettingsManager(context)
    val eventManager = EventManager.getInstance(database.powerEventDao(), settingsManager)

    val viewModel = viewModel<HomeViewModel>(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(
                eventManager,
                database.powerEventDao(),
                networkRepository,
                settingsManager
            ) as T
        }
    })

    val powerState by viewModel.powerState.collectAsState()
    val isMonitoringActive by viewModel.isMonitoringEnabled.collectAsState()
    val registeredNetworks by viewModel.registeredNetworks.collectAsState()
    val scanCountdown by viewModel.scanCountdown.collectAsState()
    val scanPhase by viewModel.scanPhase.collectAsState()
    val activeOutageDurationMs by viewModel.activeOutageDurationMs.collectAsState()
    val networkBreakdown by viewModel.networkBreakdown.collectAsState()
    val stats by viewModel.powerStats.collectAsState()
    val todayEvents by viewModel.todayEvents.collectAsState()
    val recentOnSessions by viewModel.recentOnSessions.collectAsState()
    val firstRunTime by viewModel.firstRunTime.collectAsState()

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
                scanPhase = scanPhase,
                outageDurationMs = activeOutageDurationMs,
                networkBreakdown = networkBreakdown,
                stats = stats,
                todayEvents = todayEvents,
                recentOnSessions = recentOnSessions,
                firstRunTime = firstRunTime
            )
        }
    }
}

@Composable
fun HomeHeader(isMonitoringActive: Boolean, onSettingsClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
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
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isActive) PrimaryGreen.copy(alpha = 0.08f) else Color.Gray.copy(alpha = 0.08f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(if (isActive) PowerOn.copy(alpha = alpha) else Color.Gray)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = if (isActive) "Monitoring Active" else "Monitoring Paused",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = if (isActive) PowerOn else MutedText
        )
    }
}

@Composable
fun HomeScreenContent(
    modifier: Modifier,
    powerState: PowerState,
    scanCountdown: Int,
    scanPhase: ScanPhase,
    outageDurationMs: Long,
    networkBreakdown: List<NetworkStatus>,
    stats: PowerStats,
    todayEvents: List<PowerEventEntity>,
    recentOnSessions: List<OnSession>,
    firstRunTime: Long = 0L
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        StatusCard(powerState, outageDurationMs, scanCountdown, scanPhase)

        Spacer(modifier = Modifier.height(20.dp))

        NetworkStatusSummary(networkBreakdown)

        Spacer(modifier = Modifier.height(20.dp))

        NetworkBreakdownList(networkBreakdown)

        Spacer(modifier = Modifier.height(20.dp))

        PowerTimelineSection(todayEvents, firstRunTime)

        Spacer(modifier = Modifier.height(20.dp))

        PowerSummarySection(stats)

        Spacer(modifier = Modifier.height(20.dp))

        RecentLoadShedding(todayEvents)

        if (recentOnSessions.isNotEmpty()) {
            Spacer(modifier = Modifier.height(20.dp))
            RecentOnSessions(recentOnSessions)
        }

        Spacer(modifier = Modifier.height(20.dp))

        PowerAnalysisCard(stats)

        Spacer(modifier = Modifier.height(32.dp))
    }
}

// ─── STATUS CARD ────────────────────────────────────────────────
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun StatusCard(state: PowerState, durationMs: Long, countdown: Int, scanPhase: ScanPhase) {
    val statusColor by animateColorAsState(
        targetValue = when (state) {
            PowerState.POWER_ON -> PowerOn
            PowerState.POWER_OFF -> PowerOff
            PowerState.POSSIBLE_POWER_OFF -> PowerUnknown
            PowerState.UNKNOWN -> PowerUnknown
        },
        animationSpec = tween(600),
        label = "status_color"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "status_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.08f,
        targetValue = 0.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )
    val dotScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot_scale"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
        border = BorderStroke(1.dp, statusColor.copy(alpha = 0.15f))
    ) {
        Column(
            modifier = Modifier
                .padding(28.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "CURRENT STATUS",
                style = MaterialTheme.typography.labelSmall,
                color = MutedText,
                letterSpacing = 3.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(28.dp))

            Box(contentAlignment = Alignment.Center) {
                Box(modifier = Modifier.size(80.dp).scale(dotScale * 1.1f).clip(CircleShape).background(statusColor.copy(alpha = glowAlpha * 0.3f)))
                Box(modifier = Modifier.size(60.dp).scale(dotScale).clip(CircleShape).background(statusColor.copy(alpha = glowAlpha * 0.6f)))
                Box(modifier = Modifier.size(28.dp).clip(CircleShape).background(statusColor).shadow(8.dp, CircleShape, ambientColor = statusColor, spotColor = statusColor))
            }

            Spacer(modifier = Modifier.height(20.dp))

            AnimatedContent(
                targetState = state,
                transitionSpec = {
                    (fadeIn(tween(400)) + scaleIn(tween(400), initialScale = 0.95f)) togetherWith
                            (fadeOut(tween(300)) + scaleOut(tween(300), targetScale = 1.05f))
                },
                label = "status_title"
            ) { targetState ->
                Text(
                    text = when (targetState) {
                        PowerState.POWER_ON -> "CURRENT ON"
                        PowerState.POWER_OFF -> "CURRENT OFF"
                        PowerState.POSSIBLE_POWER_OFF -> "CHECKING..."
                        PowerState.UNKNOWN -> "UNKNOWN"
                    },
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = when (state) {
                    PowerState.POWER_ON -> "Electricity Available"
                    PowerState.POWER_OFF -> "Load Shedding"
                    PowerState.POSSIBLE_POWER_OFF -> "Verifying Status"
                    PowerState.UNKNOWN -> "Waiting for Data"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MutedText
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (state == PowerState.POWER_OFF) {
                Text("OFF for", color = MutedText, style = MaterialTheme.typography.bodySmall)
                Text(
                    formatDuration(durationMs),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = PowerOff
                )
                Spacer(modifier = Modifier.height(20.dp))
                ScanPhaseDisplay(countdown, scanPhase, accentColor = PowerOff)
            } else if (state == PowerState.POWER_ON) {
                ScanPhaseDisplay(countdown, scanPhase)
                Spacer(modifier = Modifier.height(20.dp))
                Text("ON for", color = MutedText, style = MaterialTheme.typography.bodySmall)
                Text(
                    formatDuration(durationMs),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = PowerOn
                )
            } else {
                ScanPhaseDisplay(countdown, scanPhase)
            }
        }
    }
}


// ─── SCAN PHASE DISPLAY ─────────────────────────────────────────
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ScanPhaseDisplay(secondsLeft: Int, phase: ScanPhase, accentColor: Color = PrimaryGreen) {
    AnimatedContent(
        targetState = phase,
        transitionSpec = {
            (fadeIn(tween(300)) + slideInVertically { it / 3 }) togetherWith
                    (fadeOut(tween(200)) + slideOutVertically { -it / 3 })
        },
        label = "scan_phase"
    ) { currentPhase ->
        when (currentPhase) {
            is ScanPhase.Idle -> {
                // Normal countdown circle
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Next scan in", color = MutedText, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(72.dp)) {
                        val progress = (30f - secondsLeft.toFloat()) / 30f
                        CircularProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxSize(),
                            color = accentColor,
                            strokeWidth = 4.dp,
                            trackColor = SurfaceLighter,
                            strokeCap = StrokeCap.Round
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(String.format("%02d", secondsLeft), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("sec", style = MaterialTheme.typography.labelSmall, color = MutedText, fontSize = 10.sp)
                        }
                    }
                }
            }
            is ScanPhase.CheckingConnected -> {
                ScanPhaseRow(
                    icon = Icons.Default.Wifi,
                    text = "Checking connected network...",
                    color = accentColor,
                    pulsing = true
                )
            }
            is ScanPhase.ScanningNearby -> {
                // Ripple rings + wifi icon
                val infiniteTransition = rememberInfiniteTransition(label = "ripple")
                val ring1Scale by infiniteTransition.animateFloat(0.5f, 1.5f, infiniteRepeatable(tween(1000, easing = LinearEasing), RepeatMode.Restart), label = "r1")
                val ring1Alpha by infiniteTransition.animateFloat(0.7f, 0f, infiniteRepeatable(tween(1000, easing = LinearEasing), RepeatMode.Restart), label = "a1")
                val ring2Scale by infiniteTransition.animateFloat(0.5f, 1.5f, infiniteRepeatable(tween(1000, 350, easing = LinearEasing), RepeatMode.Restart), label = "r2")
                val ring2Alpha by infiniteTransition.animateFloat(0.7f, 0f, infiniteRepeatable(tween(1000, 350, easing = LinearEasing), RepeatMode.Restart), label = "a2")

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(56.dp)) {
                        Box(modifier = Modifier.size(48.dp).scale(ring1Scale).clip(CircleShape).background(accentColor.copy(alpha = ring1Alpha * 0.3f)))
                        Box(modifier = Modifier.size(48.dp).scale(ring2Scale).clip(CircleShape).background(accentColor.copy(alpha = ring2Alpha * 0.3f)))
                        Icon(Icons.Default.Wifi, contentDescription = null, tint = accentColor, modifier = Modifier.size(26.dp))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Scanning nearby networks...", color = accentColor, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
                }
            }
            is ScanPhase.MatchingBssids -> {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(BackgroundColor.copy(alpha = 0.6f))
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.WifiFind, contentDescription = null, tint = accentColor, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Matching identifiers...", style = MaterialTheme.typography.labelSmall, color = accentColor, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    currentPhase.networks.forEachIndexed { index, network ->
                        val visible = remember { mutableStateOf(false) }
                        LaunchedEffect(network.id) {
                            kotlinx.coroutines.delay(index * 180L)
                            visible.value = true
                        }
                        AnimatedVisibility(
                            visible = visible.value,
                            enter = fadeIn(tween(250)) + slideInHorizontally { -it / 2 }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Wifi, contentDescription = null, tint = MutedText, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(network.displayName, color = Color.White.copy(alpha = 0.9f), style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                                // Animate a check mark appearing
                                val checkVisible = remember { mutableStateOf(false) }
                                LaunchedEffect(network.id) {
                                    kotlinx.coroutines.delay(index * 180L + 400L)
                                    checkVisible.value = true
                                }
                                AnimatedVisibility(visible = checkVisible.value, enter = fadeIn(tween(300)) + scaleIn(tween(300))) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = accentColor, modifier = Modifier.size(14.dp))
                                }
                            }
                        }
                    }
                }
            }
            is ScanPhase.Done -> {
                ScanPhaseRow(icon = Icons.Default.CheckCircle, text = "Scan complete", color = accentColor, pulsing = false)
            }
        }
    }
}

@Composable
fun ScanPhaseRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, color: Color, pulsing: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "row_pulse")
    val alpha by if (pulsing) infiniteTransition.animateFloat(
        initialValue = 0.5f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(600, easing = LinearEasing), RepeatMode.Reverse),
        label = "alpha"
    ) else remember { mutableStateOf(1f) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.08f))
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Icon(icon, contentDescription = null, tint = color.copy(alpha = alpha), modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Text(text, color = color, fontWeight = FontWeight.Medium, style = MaterialTheme.typography.bodyMedium)
    }
}


// ─── NETWORK STATUS SUMMARY ────────────────────────────────────
@Composable
fun NetworkStatusSummary(breakdown: List<NetworkStatus>) {
    val activeCount = breakdown.count { it.state == NetworkScanState.ACTIVE }
    val offlineCount = breakdown.count { it.state == NetworkScanState.OFFLINE }
    val notScannedCount = breakdown.count { it.state == NetworkScanState.NOT_SCANNED }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        SummarySmallCard(
            title = "ACTIVE",
            count = activeCount,
            color = PowerOn,
            modifier = Modifier.weight(1f)
        )
        SummarySmallCard(
            title = "OFFLINE",
            count = offlineCount,
            color = PowerOff,
            modifier = Modifier.weight(1f)
        )
        SummarySmallCard(
            title = "UNCHECKED",
            count = notScannedCount,
            color = MutedText,
            modifier = Modifier.weight(1f)
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SummarySmallCard(title: String, count: Int, color: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
        border = BorderStroke(1.dp, CardBorderColor.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(
                targetState = count,
                transitionSpec = {
                    (slideInVertically { it } + fadeIn(tween(300))) togetherWith (slideOutVertically { -it } + fadeOut(tween(300)))
                },
                label = "count_anim"
            ) { targetCount ->
                Text(
                    targetCount.toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, style = MaterialTheme.typography.labelSmall, color = MutedText, letterSpacing = 1.sp)
        }
    }
}

// ─── NETWORK BREAKDOWN LIST ─────────────────────────────────────
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
        networks.forEachIndexed { index, network ->
            val accentColor = when (network.state) {
                NetworkScanState.ACTIVE -> PowerOn
                NetworkScanState.OFFLINE -> PowerOff.copy(alpha = 0.5f)
                NetworkScanState.NOT_SCANNED -> MutedText
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceColor),
                border = BorderStroke(1.dp, accentColor.copy(alpha = 0.15f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 0.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left accent bar
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(56.dp)
                            .clip(RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp))
                            .background(accentColor)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        Icons.Default.Wifi,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        network.name,
                        color = Color.White,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        when (network.state) {
                            NetworkScanState.ACTIVE -> "ACTIVE"
                            NetworkScanState.OFFLINE -> "OFFLINE"
                            NetworkScanState.NOT_SCANNED -> "NOT SCANNED"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
            if (index < networks.size - 1) {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

// ─── POWER TIMELINE ─────────────────────────────────────────────
@Composable
fun PowerTimelineSection(events: List<PowerEventEntity>, firstRunTime: Long = 0L) {
    var selectedEvent by remember { mutableStateOf<PowerEventEntity?>(null) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Today's Power Timeline",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceColor),
            border = BorderStroke(1.dp, CardBorderColor.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(10.dp))
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
                    TimelineCanvas(events, firstRunTime)
                    CurrentTimeIndicator()
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("12 AM", style = MaterialTheme.typography.labelSmall, color = MutedText, fontSize = 10.sp)
                    Text("6 AM", style = MaterialTheme.typography.labelSmall, color = MutedText, fontSize = 10.sp)
                    Text("12 PM", style = MaterialTheme.typography.labelSmall, color = MutedText, fontSize = 10.sp)
                    Text("6 PM", style = MaterialTheme.typography.labelSmall, color = MutedText, fontSize = 10.sp)
                    Text("12 AM", style = MaterialTheme.typography.labelSmall, color = MutedText, fontSize = 10.sp)
                }

                // Show legend if there's an unknown block today
                val todayStart = remember {
                    Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
                    }.timeInMillis
                }
                if (firstRunTime > todayStart) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        LegendItem(PowerOn, "Power ON")
                        LegendItem(PowerOff, "Power OFF")
                        LegendItem(Color(0xFF546E7A), "Unknown")
                    }
                }
            }
        }
    }

    if (selectedEvent != null) {
        OutageDetailDialog(event = selectedEvent!!, onDismiss = { selectedEvent = null })
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
        Spacer(modifier = Modifier.width(4.dp))
        Text(label, fontSize = 10.sp, color = MutedText)
    }
}

@Composable
fun TimelineCanvas(events: List<PowerEventEntity>, firstRunTime: Long = 0L) {
    val infiniteTransition = rememberInfiniteTransition(label = "timeline_glow")
    val gradientShift by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(5000, easing = LinearEasing), RepeatMode.Restart),
        label = "gradient_shift"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val todayStart = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        val dayMillis = 24 * 60 * 60 * 1000L

        // Base gradient for power ON (only from monitoring start)
        val monitoringStart = if (firstRunTime > todayStart) firstRunTime else todayStart
        val monitoringFraction = (monitoringStart - todayStart).toFloat() / dayMillis
        val greenStartX = monitoringFraction * size.width

        // Grey UNKNOWN block before monitoring
        if (greenStartX > 0f) {
            drawRoundRect(
                color = androidx.compose.ui.graphics.Color(0xFF546E7A),
                size = Size(greenStartX, size.height),
                cornerRadius = CornerRadius(10.dp.toPx())
            )
        }

        // Green block from monitoring start onwards
        drawRoundRect(
            brush = Brush.horizontalGradient(
                colors = listOf(PowerOn.copy(alpha = 0.6f), PowerOn.copy(alpha = 0.9f), PowerOn.copy(alpha = 0.6f)),
                startX = gradientShift, endX = gradientShift + size.width
            ),
            topLeft = Offset(greenStartX, 0f),
            size = Size(size.width - greenStartX, size.height),
            cornerRadius = CornerRadius(10.dp.toPx())
        )

        // Red blocks = outages
        events.forEach { event ->
            val eventStartToday = max(event.startTime, todayStart)
            val eventEndToday = min(event.endTime ?: System.currentTimeMillis(), todayStart + dayMillis)

            if (eventStartToday < eventEndToday) {
                val startX = ((eventStartToday - todayStart).toFloat() / dayMillis) * size.width
                val endX = ((eventEndToday - todayStart).toFloat() / dayMillis) * size.width
                drawRoundRect(
                    color = PowerOff,
                    topLeft = Offset(startX, 0f),
                    size = Size(max(4f, endX - startX), size.height),
                    cornerRadius = CornerRadius(4.dp.toPx())
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
        animationSpec = infiniteRepeatable(tween(1000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "alpha"
    )

    // Trigger recomposition every minute to update the line position
    var currentTime by remember { mutableStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(60_000L) // 1 minute
            currentTime = System.currentTimeMillis()
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val calendar = Calendar.getInstance().apply { timeInMillis = currentTime }
        val minutesSinceStart = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)
        val progress = minutesSinceStart.toFloat() / (24 * 60f)
        val xPos = progress * size.width

        // Outer glow dot
        drawCircle(
            color = Color.White.copy(alpha = alpha * 0.4f),
            radius = 12.dp.toPx(),
            center = Offset(xPos, size.height / 2)
        )
        // Solid line
        drawLine(
            color = Color.White,
            start = Offset(xPos, 0f),
            end = Offset(xPos, size.height),
            strokeWidth = 2.dp.toPx()
        )
        // Core dot
        drawCircle(
            color = Color.White,
            radius = 4.dp.toPx(),
            center = Offset(xPos, size.height / 2)
        )
    }
}

// ─── POWER SUMMARY ──────────────────────────────────────────────
@Composable
fun PowerSummarySection(stats: PowerStats) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
        border = BorderStroke(1.dp, CardBorderColor.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "TODAY'S SUMMARY",
                style = MaterialTheme.typography.labelSmall,
                color = MutedText,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Animated availability ring
            val animatedProgress by animateFloatAsState(
                targetValue = stats.availabilityPercentage / 100f,
                animationSpec = tween(1200, easing = FastOutSlowInEasing),
                label = "progress"
            )

            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(130.dp)) {
                CircularProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.fillMaxSize(),
                    color = PowerOn,
                    strokeWidth = 10.dp,
                    trackColor = SurfaceLighter,
                    strokeCap = StrokeCap.Round
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "${stats.availabilityPercentage.toInt()}%",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text("Uptime", style = MaterialTheme.typography.labelSmall, color = MutedText)
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                SummaryStatItem("ON", formatDurationSimple(stats.totalOnTimeMs), PowerOn, Icons.Outlined.ElectricBolt)
                SummaryStatItem("OFF", formatDurationSimple(stats.totalOffTimeMs), PowerOff, Icons.Outlined.TrendingDown)
                SummaryStatItem("OUTAGES", stats.outageCount.toString(), Color.White, Icons.Outlined.Schedule)
            }
        }
    }
}

@Composable
fun SummaryStatItem(label: String, value: String, color: Color, icon: ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = color.copy(alpha = 0.7f), modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.height(6.dp))
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = color)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MutedText, letterSpacing = 1.sp)
    }
}

// ─── RECENT LOAD SHEDDING ───────────────────────────────────────
@Composable
fun RecentLoadShedding(events: List<PowerEventEntity>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Recent Outages",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (events.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceColor)
            ) {
                Text(
                    "No outages recorded today ✨",
                    color = MutedText,
                    modifier = Modifier.padding(20.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            events.filter { it.endTime != null }.take(3).forEachIndexed { index, event ->
                AnimatedVisibility(
                    visible = true,
                    enter = slideInVertically(
                        initialOffsetY = { it / 2 },
                        animationSpec = tween(400, delayMillis = index * 80)
                    ) + fadeIn(tween(400, delayMillis = index * 80))
                ) {
                    RecentEventItem(event)
                }
                if (index < 2) Spacer(modifier = Modifier.height(8.dp))
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
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
        border = BorderStroke(1.dp, PowerOff.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Red accent bar
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(60.dp)
                    .clip(RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp))
                    .background(PowerOff)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 14.dp)
            ) {
                Text(
                    "$startTime → $endTime",
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    "Duration: ${formatDurationSimple(event.duration ?: 0)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MutedText
                )
            }
            Text(
                "TODAY",
                style = MaterialTheme.typography.labelSmall,
                color = MutedText,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(end = 16.dp)
            )
        }
    }
}

// ─── RECENT POWER SESSIONS ──────────────────────────────────────
@Composable
fun RecentOnSessions(sessions: List<OnSession>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Recent Power Sessions",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(12.dp))

        sessions.forEachIndexed { index, session ->
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(
                    initialOffsetY = { it / 2 },
                    animationSpec = tween(400, delayMillis = index * 80)
                ) + fadeIn(tween(400, delayMillis = index * 80))
            ) {
                SessionEventItem(session)
            }
            if (index < sessions.lastIndex) Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun SessionEventItem(session: OnSession) {
    val timeFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val startTime = timeFormatter.format(Date(session.startMs))
    val endTime = timeFormatter.format(Date(session.endMs))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
        border = BorderStroke(1.dp, PowerOn.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Green accent bar
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(60.dp)
                    .clip(RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp))
                    .background(PowerOn)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 14.dp)
            ) {
                Text(
                    "$startTime → $endTime",
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    "Available for: ${formatDurationSimple(session.durationMs)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MutedText
                )
            }
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = PowerOn,
                modifier = Modifier.padding(end = 16.dp).size(20.dp)
            )
        }
    }
}

// ─── POWER ANALYSIS ─────────────────────────────────────────────
@Composable
fun PowerAnalysisCard(stats: PowerStats) {
    val infiniteTransition = rememberInfiniteTransition(label = "analysis_pulse")
    val boltAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bolt_alpha"
    )
    val boltScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bolt_scale"
    )

    val animatedAvailability by animateFloatAsState(
        targetValue = stats.availabilityPercentage / 100f,
        animationSpec = tween(1400, easing = FastOutSlowInEasing),
        label = "avail"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
        border = BorderStroke(1.dp, PrimaryGreen.copy(alpha = 0.12f))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {

            // ── Header row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .scale(boltScale)
                        .clip(RoundedCornerShape(12.dp))
                        .background(PrimaryGreen.copy(alpha = boltAlpha * 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.ElectricBolt,
                        contentDescription = null,
                        tint = PrimaryGreen.copy(alpha = boltAlpha),
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text("Power Analysis", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Today's electricity summary", style = MaterialTheme.typography.bodySmall, color = MutedText)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Availability bar
            AnalysisBarItem(
                icon = Icons.Outlined.ElectricBolt,
                label = "Availability",
                value = "${stats.availabilityPercentage.toInt()}%",
                progress = animatedAvailability,
                barColor = PrimaryGreen
            )
            Spacer(modifier = Modifier.height(16.dp))

            val animatedOnTime by animateFloatAsState(
                targetValue = if ((stats.totalOnTimeMs + stats.totalOffTimeMs) > 0L)
                    stats.totalOnTimeMs.toFloat() / (stats.totalOnTimeMs + stats.totalOffTimeMs)
                else 1f,
                animationSpec = tween(1200, easing = FastOutSlowInEasing),
                label = "on_time"
            )
            AnalysisBarItem(
                icon = Icons.Outlined.Schedule,
                label = "On Time",
                value = formatDurationSimple(stats.totalOnTimeMs),
                progress = animatedOnTime,
                barColor = PrimaryGreen.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(16.dp))

            val animatedOffTime by animateFloatAsState(
                targetValue = if ((stats.totalOnTimeMs + stats.totalOffTimeMs) > 0L)
                    stats.totalOffTimeMs.toFloat() / (stats.totalOnTimeMs + stats.totalOffTimeMs)
                else 0f,
                animationSpec = tween(1200, easing = FastOutSlowInEasing),
                label = "off_time"
            )
            AnalysisBarItem(
                icon = Icons.Outlined.TrendingDown,
                label = "Off Time",
                value = formatDurationSimple(stats.totalOffTimeMs),
                progress = animatedOffTime,
                barColor = PowerOff
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ── Stats chips row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AnalysisChip(
                    icon = Icons.Outlined.TrendingDown,
                    label = "Outages",
                    value = stats.outageCount.toString(),
                    color = PowerOff,
                    modifier = Modifier.weight(1f)
                )
                AnalysisChip(
                    icon = Icons.Outlined.Schedule,
                    label = "Longest",
                    value = if (stats.longestOutageMs > 0) formatDurationSimple(stats.longestOutageMs) else "—",
                    color = PowerUnknown,
                    modifier = Modifier.weight(1f)
                )
                AnalysisChip(
                    icon = Icons.Outlined.ElectricBolt,
                    label = "Peak",
                    value = "6–11 PM",
                    color = MutedText,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun AnalysisBarItem(
    icon: ImageVector,
    label: String,
    value: String,
    progress: Float,
    barColor: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = barColor, modifier = Modifier.size(15.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(label, style = MaterialTheme.typography.bodySmall, color = MutedText)
            }
            Text(value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = Color.White)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(SurfaceLighter)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(3.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(barColor.copy(alpha = 0.7f), barColor)
                        )
                    )
            )
        }
    }
}

@Composable
fun AnalysisChip(icon: ImageVector, label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(BackgroundColor.copy(alpha = 0.6f))
            .padding(horizontal = 10.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.height(6.dp))
        Text(value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = Color.White)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MutedText, fontSize = 10.sp)
    }
}

// ─── DIALOG ─────────────────────────────────────────────────────
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Outage Details", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = PowerOff)
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = MutedText, modifier = Modifier.size(18.dp))
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))

                DetailRowItem("Started", start)
                DetailRowItem("Restored", end)
                DetailRowItem("Duration", duration)
            }
        }
    }
}

@Composable
fun DetailRowItem(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MutedText, letterSpacing = 1.sp)
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

// ─── EMPTY STATE ────────────────────────────────────────────────
@Composable
fun EmptyState(onAddChecker: () -> Unit, modifier: Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "float")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float_y"
    )

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "📡",
            fontSize = 64.sp,
            modifier = Modifier.offset(y = offsetY.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "No Power Checkers Added",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Add Wi-Fi networks from your building\nto start monitoring electricity.",
            textAlign = TextAlign.Center,
            color = MutedText,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 40.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onAddChecker,
            colors = ButtonDefaults.buttonColors(containerColor = PowerOn),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .pressClickEffect(onAddChecker)
                .height(50.dp)
                .padding(horizontal = 24.dp)
        ) {
            Text("+ Add Power Checker", color = BackgroundColor, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
    }
}

// ─── UTILITY ────────────────────────────────────────────────────
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

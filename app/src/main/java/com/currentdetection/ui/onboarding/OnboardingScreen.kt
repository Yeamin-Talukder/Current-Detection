package com.currentdetection.ui.onboarding

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.currentdetection.ui.theme.*
import kotlinx.coroutines.delay

private val UnknownGrey = Color(0xFF546E7A)
private val WarningAmber = Color(0xFFF1C40F)
private val DangerRed = Color(0xFFC0392B)
private val DarkSurface = Color(0xFF1A252F)

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    var step by remember { mutableIntStateOf(0) }
    val totalSteps = 4

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF1A252F), Color(0xFF0D1B24))))
            .systemBarsPadding()
    ) {
        // Skip button — only on pages that are not the warning page
        if (step != 2) {
            TextButton(
                onClick = onFinish,
                modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
            ) {
                Text("Skip", color = MutedText, fontSize = 14.sp)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.7f))

            // Page content with smooth crossfade transition
            AnimatedContent(
                targetState = step,
                transitionSpec = {
                    if (targetState > initialState) {
                        (fadeIn(tween(400)) + slideInHorizontally(tween(380)) { it / 3 })
                            .togetherWith(fadeOut(tween(250)) + slideOutHorizontally(tween(300)) { -it / 3 })
                    } else {
                        (fadeIn(tween(400)) + slideInHorizontally(tween(380)) { -it / 3 })
                            .togetherWith(fadeOut(tween(250)) + slideOutHorizontally(tween(300)) { it / 3 })
                    }
                },
                label = "onboarding_page"
            ) { targetStep ->
                when (targetStep) {
                    0 -> WelcomePage()
                    1 -> HowItWorksPage()
                    2 -> IpsWarningPage()
                    else -> AddNetworksPage()
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Page indicator dots
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(totalSteps) { index ->
                    val isActive = index == step
                    val isWarning = index == 2
                    val dotColor = when {
                        isActive && isWarning -> WarningAmber
                        isActive -> PowerOn
                        else -> MutedText.copy(alpha = 0.3f)
                    }
                    val width by animateDpAsState(
                        targetValue = if (isActive) 28.dp else 8.dp,
                        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                        label = "dot_width_$index"
                    )
                    Box(
                        modifier = Modifier
                            .height(8.dp)
                            .width(width)
                            .clip(CircleShape)
                            .background(dotColor)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // CTA button — color changes on warning page
            val isWarningPage = step == 2
            val btnColor by animateColorAsState(
                targetValue = if (isWarningPage) WarningAmber else PowerOn,
                animationSpec = tween(400),
                label = "btn_color"
            )
            val btnTextColor by animateColorAsState(
                targetValue = Color(0xFF1A252F),
                animationSpec = tween(300),
                label = "btn_text"
            )

            Button(
                onClick = { if (step < totalSteps - 1) step++ else onFinish() },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = btnColor)
            ) {
                Text(
                    when {
                        step < totalSteps - 1 -> "I Understand, Next"
                        else -> "Get Started"
                    },
                    color = btnTextColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ─── PAGE 0: WELCOME ────────────────────────────────────────────
@Composable
fun WelcomePage() {
    val infiniteTransition = rememberInfiniteTransition(label = "welcome")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.18f, targetValue = 0.45f,
        animationSpec = infiniteRepeatable(tween(2000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "glow"
    )
    val iconScale by infiniteTransition.animateFloat(
        initialValue = 0.95f, targetValue = 1.05f,
        animationSpec = infiniteRepeatable(tween(2200, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "scale"
    )
    val rotation by infiniteTransition.animateFloat(
        initialValue = -7f, targetValue = 7f,
        animationSpec = infiniteRepeatable(tween(3000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "rotation"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Lightning bolt with layered glow rings
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(210.dp)) {
            Box(modifier = Modifier.size((170 * (0.9f + glowAlpha * 0.4f)).dp).clip(CircleShape).background(PowerOn.copy(alpha = glowAlpha * 0.25f)))
            Box(modifier = Modifier.size((130 * (0.9f + glowAlpha * 0.3f)).dp).clip(CircleShape).background(PowerOn.copy(alpha = glowAlpha * 0.4f)))
            Box(modifier = Modifier.size(90.dp).clip(CircleShape).background(PowerOn), contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Filled.FlashOn,
                    contentDescription = null,
                    tint = Color(0xFF1A252F),
                    modifier = Modifier.size(50.dp).scale(iconScale).graphicsLayer { rotationZ = rotation }
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text("Current Detection", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "Know when electricity is available\nin your building — automatically.",
            style = MaterialTheme.typography.bodyLarge,
            color = MutedText,
            textAlign = TextAlign.Center,
            lineHeight = 26.sp
        )
    }
}

// ─── PAGE 1: HOW IT WORKS ───────────────────────────────────────
@Composable
fun HowItWorksPage() {
    var visibleStep by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1600)
            visibleStep = (visibleStep + 1) % 3
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("How It Works", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.height(6.dp))
        Text("Wi-Fi signals reveal if your building has power", style = MaterialTheme.typography.bodySmall, color = MutedText, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(28.dp))

        // Central animated illustration
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(170.dp).clip(CircleShape).background(SurfaceColor)
        ) {
            AnimatedContent(
                targetState = visibleStep,
                transitionSpec = { fadeIn(tween(500)) togetherWith fadeOut(tween(400)) },
                label = "how_step_anim"
            ) { s ->
                when (s) {
                    0 -> HowStepIcon(Icons.Filled.Router, rings = 3, ringsColor = PowerOn, iconTint = PowerOn, label = "Power ON")
                    1 -> HowStepIcon(Icons.Outlined.WifiOff, rings = 0, ringsColor = PowerOff, iconTint = PowerOff, label = "Power OFF")
                    else -> HowStepIcon(Icons.Outlined.AssignmentTurnedIn, rings = 0, ringsColor = PowerOn, iconTint = PowerOn, label = "Recorded!")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            HowStepCard(icon = Icons.Filled.Router, text = "Building Wi-Fi networks are detected", highlighted = visibleStep == 0)
            HowStepCard(icon = Icons.Outlined.WifiOff, text = "During outage, Wi-Fi networks disappear", highlighted = visibleStep == 1)
            HowStepCard(icon = Icons.Outlined.AssignmentTurnedIn, text = "App records the outage automatically", highlighted = visibleStep == 2)
        }
    }
}

@Composable
fun HowStepIcon(icon: ImageVector, rings: Int, ringsColor: Color, iconTint: Color, label: String) {
    val infiniteTransition = rememberInfiniteTransition(label = "ring_pulse")
    val ringScale by infiniteTransition.animateFloat(
        initialValue = 0.75f, targetValue = 1.08f,
        animationSpec = infiniteRepeatable(tween(1100, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "ring_scale"
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(100.dp)) {
            if (rings >= 3) {
                Box(modifier = Modifier.size((82 * ringScale).dp).clip(CircleShape).background(ringsColor.copy(alpha = 0.08f)))
                Box(modifier = Modifier.size((62 * ringScale).dp).clip(CircleShape).background(ringsColor.copy(alpha = 0.14f)))
            }
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(48.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, color = iconTint, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}

@Composable
fun HowStepCard(icon: ImageVector, text: String, highlighted: Boolean) {
    val bgColor by animateColorAsState(
        targetValue = if (highlighted) PowerOn.copy(alpha = 0.11f) else SurfaceColor,
        animationSpec = tween(400),
        label = "step_bg"
    )
    val iconTint by animateColorAsState(
        targetValue = if (highlighted) PowerOn else MutedText,
        animationSpec = tween(400),
        label = "icon_tint"
    )
    val textColor by animateColorAsState(
        targetValue = if (highlighted) Color.White else MutedText,
        animationSpec = tween(400),
        label = "text_color"
    )
    val scale by animateFloatAsState(
        targetValue = if (highlighted) 1.01f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "card_scale"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(iconTint.copy(alpha = 0.12f)), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
        }
        Text(text, color = textColor, style = MaterialTheme.typography.bodySmall,
            fontWeight = if (highlighted) FontWeight.SemiBold else FontWeight.Normal,
            modifier = Modifier.weight(1f))
    }
}

// ─── PAGE 2: IPS/UPS WARNING ────────────────────────────────────
@Composable
fun IpsWarningPage() {
    val infiniteTransition = rememberInfiniteTransition(label = "warning_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.92f, targetValue = 1.04f,
        animationSpec = infiniteRepeatable(tween(1200, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulse_scale"
    )
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f, targetValue = 0.55f,
        animationSpec = infiniteRepeatable(tween(1200, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "glow_alpha"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Warning icon with amber glow
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(190.dp)) {
            Box(modifier = Modifier.size((150 * pulseScale).dp).clip(CircleShape).background(WarningAmber.copy(alpha = glowAlpha * 0.3f)))
            Box(modifier = Modifier.size((110 * pulseScale).dp).clip(CircleShape).background(WarningAmber.copy(alpha = glowAlpha * 0.5f)))
            Box(modifier = Modifier.size(80.dp).clip(CircleShape).background(WarningAmber), contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.Warning, contentDescription = null, tint = Color(0xFF1A252F), modifier = Modifier.size(44.dp).scale(pulseScale))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("⚠️ Important Warning", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = WarningAmber, textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(16.dp))

        // Warning cards
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            WarningCard(
                icon = Icons.Outlined.BatteryChargingFull,
                title = "Do NOT add IPS/UPS networks",
                body = "Wi-Fi routers connected to IPS or UPS power backup will stay online even during a power cut. This will fool the app into thinking power is available when it is not.",
                color = WarningAmber
            )
            WarningCard(
                icon = Icons.Outlined.PowerOff,
                title = "Use routers that run on main power only",
                body = "Only register Wi-Fi networks from routers that shut down when main electricity goes out. These are the only reliable Current Identifiers.",
                color = PowerOn
            )
            WarningCard(
                icon = Icons.Outlined.CheckCircle,
                title = "How to check",
                body = "During a known outage, if the Wi-Fi is still visible, that router has backup power — do not use it as an identifier.",
                color = Color(0xFF90A4AE)
            )
        }
    }
}

@Composable
fun WarningCard(icon: ImageVector, title: String, body: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(color.copy(alpha = 0.09f))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(modifier = Modifier.size(34.dp).clip(CircleShape).background(color.copy(alpha = 0.14f)), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold, color = color, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(3.dp))
            Text(body, color = MutedText, style = MaterialTheme.typography.bodySmall, lineHeight = 18.sp)
        }
    }
}

// ─── PAGE 3: ADD NETWORKS ───────────────────────────────────────
@Composable
fun AddNetworksPage() {
    val networks = listOf("Building_WiFi", "Office_Net", "Floor2_AP")
    val visibleCount = remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(750)
            if (visibleCount.intValue < networks.size) {
                visibleCount.intValue++
            } else {
                delay(1800)
                visibleCount.intValue = 0
            }
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "scan_pulse")
    val ring1Alpha by infiniteTransition.animateFloat(
        initialValue = 0.04f, targetValue = 0.38f,
        animationSpec = infiniteRepeatable(tween(1400, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "r1"
    )
    val ring1Scale by infiniteTransition.animateFloat(
        initialValue = 0.55f, targetValue = 1.22f,
        animationSpec = infiniteRepeatable(tween(1400, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "r1s"
    )
    val ring2Alpha by infiniteTransition.animateFloat(
        initialValue = 0.04f, targetValue = 0.28f,
        animationSpec = infiniteRepeatable(tween(1400, 450, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "r2"
    )
    val ring2Scale by infiniteTransition.animateFloat(
        initialValue = 0.55f, targetValue = 1.42f,
        animationSpec = infiniteRepeatable(tween(1400, 450, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "r2s"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Add Power Checkers", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.height(6.dp))
        Text("Select building Wi-Fi routers that run on main power only", style = MaterialTheme.typography.bodySmall, color = MutedText, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(28.dp))

        // Scanning animation
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(120.dp)) {
            Box(modifier = Modifier.size((90 * ring2Scale).dp).clip(CircleShape).background(PowerOn.copy(alpha = ring2Alpha)))
            Box(modifier = Modifier.size((70 * ring1Scale).dp).clip(CircleShape).background(PowerOn.copy(alpha = ring1Alpha)))
            Box(modifier = Modifier.size(60.dp).clip(CircleShape).background(PowerOn), contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.Wifi, contentDescription = null, tint = Color(0xFF1A252F), modifier = Modifier.size(30.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Networks appearing one by one
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            networks.forEachIndexed { index, name ->
                AnimatedVisibility(
                    visible = index < visibleCount.intValue,
                    enter = slideInHorizontally(tween(400, easing = FastOutSlowInEasing)) { -it } + fadeIn(tween(350)),
                    exit = fadeOut(tween(250))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceColor)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(Icons.Default.Wifi, contentDescription = null, tint = PowerOn, modifier = Modifier.size(20.dp))
                        Text(name, color = Color.White, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                        Text("ADD", color = PowerOn, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

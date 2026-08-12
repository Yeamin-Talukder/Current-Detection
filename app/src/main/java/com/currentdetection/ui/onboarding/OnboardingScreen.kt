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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.currentdetection.ui.theme.*
import kotlinx.coroutines.delay

private val DarkBlue = Color(0xFF1A252F)
private val UnknownGrey = Color(0xFF546E7A)

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    var step by remember { mutableIntStateOf(0) }
    val totalSteps = 3

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF1A252F), Color(0xFF0D1B24))
                )
            )
            .systemBarsPadding()
    ) {
        // Skip button
        TextButton(
            onClick = onFinish,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text("Skip", color = MutedText, fontSize = 14.sp)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.8f))

            // Animated page content
            AnimatedContent(
                targetState = step,
                transitionSpec = {
                    (slideInHorizontally { it } + fadeIn(tween(350))) togetherWith
                            (slideOutHorizontally { -it } + fadeOut(tween(250)))
                },
                label = "onboarding_page"
            ) { targetStep ->
                when (targetStep) {
                    0 -> WelcomePage()
                    1 -> HowItWorksPage()
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
                    val width by animateDpAsState(
                        targetValue = if (isActive) 24.dp else 8.dp,
                        animationSpec = spring(stiffness = Spring.StiffnessMedium),
                        label = "dot_width"
                    )
                    Box(
                        modifier = Modifier
                            .height(8.dp)
                            .width(width)
                            .clip(CircleShape)
                            .background(if (isActive) PowerOn else MutedText.copy(alpha = 0.3f))
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // CTA button
            Button(
                onClick = { if (step < totalSteps - 1) step++ else onFinish() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PowerOn)
            ) {
                Text(
                    if (step < totalSteps - 1) "Next" else "Get Started",
                    color = Color(0xFF1A252F),
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
        initialValue = 0.2f, targetValue = 0.5f,
        animationSpec = infiniteRepeatable(tween(1500, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "glow"
    )
    val iconScale by infiniteTransition.animateFloat(
        initialValue = 0.95f, targetValue = 1.05f,
        animationSpec = infiniteRepeatable(tween(1800, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "scale"
    )
    val rotation by infiniteTransition.animateFloat(
        initialValue = -8f, targetValue = 8f,
        animationSpec = infiniteRepeatable(tween(2400, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "rotation"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Lightning bolt icon with glow rings
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp)) {
            // Outer glow ring
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)
                    .background(PowerOn.copy(alpha = glowAlpha * 0.3f))
            )
            // Mid glow ring
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(PowerOn.copy(alpha = glowAlpha * 0.5f))
            )
            // Icon background
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(PowerOn),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.FlashOn,
                    contentDescription = null,
                    tint = Color(0xFF1A252F),
                    modifier = Modifier
                        .size(50.dp)
                        .scale(iconScale)
                        .graphicsLayer { rotationZ = rotation }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Current Detection",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            "Know when electricity is available\nin your building — automatically.",
            style = MaterialTheme.typography.bodyLarge,
            color = MutedText,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
    }
}

// ─── PAGE 1: HOW IT WORKS ───────────────────────────────────────
@Composable
fun HowItWorksPage() {
    var visibleStep by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1400)
            visibleStep = (visibleStep + 1) % 3
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "How It Works",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Wi-Fi tells us if your power is on",
            style = MaterialTheme.typography.bodyMedium,
            color = MutedText
        )
        Spacer(modifier = Modifier.height(36.dp))

        // Central animated illustration
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
                .background(SurfaceColor)
        ) {
            AnimatedContent(
                targetState = visibleStep,
                transitionSpec = {
                    fadeIn(tween(400)) togetherWith fadeOut(tween(300))
                },
                label = "how_step_anim"
            ) { s ->
                when (s) {
                    0 -> HowStepIcon(
                        icon = Icons.Filled.Router,
                        rings = 3,
                        ringsColor = PowerOn,
                        iconTint = PowerOn,
                        label = "Power ON"
                    )
                    1 -> HowStepIcon(
                        icon = Icons.Outlined.WifiOff,
                        rings = 0,
                        ringsColor = PowerOff,
                        iconTint = PowerOff,
                        label = "Power OFF"
                    )
                    else -> HowStepIcon(
                        icon = Icons.Outlined.EventNote,
                        rings = 0,
                        ringsColor = PowerOn,
                        iconTint = PowerOn,
                        label = "Recorded!"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Step explanation cards
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            HowStepCard(
                icon = Icons.Filled.Router,
                text = "Building Wi-Fi networks are detected",
                highlighted = visibleStep == 0
            )
            HowStepCard(
                icon = Icons.Outlined.WifiOff,
                text = "During outage, Wi-Fi networks disappear",
                highlighted = visibleStep == 1
            )
            HowStepCard(
                icon = Icons.Outlined.EventNote,
                text = "App records the outage automatically",
                highlighted = visibleStep == 2
            )
        }
    }
}

@Composable
fun HowStepIcon(
    icon: ImageVector,
    rings: Int,
    ringsColor: Color,
    iconTint: Color,
    label: String
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ring_pulse")
    val ringScale by infiniteTransition.animateFloat(
        initialValue = 0.7f, targetValue = 1.1f,
        animationSpec = infiniteRepeatable(tween(900, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "ring_scale"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(100.dp)) {
            if (rings >= 3) {
                Box(
                    modifier = Modifier
                        .size((80 * ringScale).dp)
                        .clip(CircleShape)
                        .background(ringsColor.copy(alpha = 0.1f))
                )
                Box(
                    modifier = Modifier
                        .size((60 * ringScale).dp)
                        .clip(CircleShape)
                        .background(ringsColor.copy(alpha = 0.15f))
                )
            }
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(48.dp))
        }
        Text(label, color = iconTint, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}

@Composable
fun HowStepCard(icon: ImageVector, text: String, highlighted: Boolean) {
    val bgColor by animateColorAsState(
        targetValue = if (highlighted) PowerOn.copy(alpha = 0.12f) else SurfaceColor,
        animationSpec = tween(300),
        label = "step_bg"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(if (highlighted) PowerOn else SurfaceColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (highlighted) Color(0xFF1A252F) else MutedText,
                modifier = Modifier.size(18.dp)
            )
        }
        Text(
            text,
            color = if (highlighted) Color.White else MutedText,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if (highlighted) FontWeight.SemiBold else FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )
    }
}

// ─── PAGE 2: ADD NETWORKS ───────────────────────────────────────
@Composable
fun AddNetworksPage() {
    val networks = listOf("Building_WiFi", "Office_Net", "Floor2_AP")
    val visibleCount = remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(700)
            if (visibleCount.intValue < networks.size) {
                visibleCount.intValue++
            } else {
                delay(1500)
                visibleCount.intValue = 0
            }
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "scan_pulse")
    val ring1Alpha by infiniteTransition.animateFloat(
        initialValue = 0.05f, targetValue = 0.4f,
        animationSpec = infiniteRepeatable(tween(1200, easing = LinearOutSlowInEasing), RepeatMode.Reverse),
        label = "r1"
    )
    val ring1Scale by infiniteTransition.animateFloat(
        initialValue = 0.5f, targetValue = 1.2f,
        animationSpec = infiniteRepeatable(tween(1200, easing = LinearOutSlowInEasing), RepeatMode.Reverse),
        label = "r1s"
    )
    val ring2Alpha by infiniteTransition.animateFloat(
        initialValue = 0.05f, targetValue = 0.3f,
        animationSpec = infiniteRepeatable(tween(1200, 400, easing = LinearOutSlowInEasing), RepeatMode.Reverse),
        label = "r2"
    )
    val ring2Scale by infiniteTransition.animateFloat(
        initialValue = 0.5f, targetValue = 1.4f,
        animationSpec = infiniteRepeatable(tween(1200, 400, easing = LinearOutSlowInEasing), RepeatMode.Reverse),
        label = "r2s"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "Add Power Checkers",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Select building Wi-Fi networks",
            style = MaterialTheme.typography.bodyMedium,
            color = MutedText
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Scanning animation
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(120.dp)) {
            Box(
                modifier = Modifier
                    .size((90 * ring2Scale).dp)
                    .clip(CircleShape)
                    .background(PowerOn.copy(alpha = ring2Alpha))
            )
            Box(
                modifier = Modifier
                    .size((70 * ring1Scale).dp)
                    .clip(CircleShape)
                    .background(PowerOn.copy(alpha = ring1Alpha))
            )
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(PowerOn),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Wifi,
                    contentDescription = null,
                    tint = Color(0xFF1A252F),
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Networks appearing one by one
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            networks.forEachIndexed { index, name ->
                AnimatedVisibility(
                    visible = index < visibleCount.intValue,
                    enter = slideInHorizontally { -it } + fadeIn(tween(400)),
                    exit = fadeOut(tween(200))
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
                        Icon(
                            Icons.Default.Wifi,
                            contentDescription = null,
                            tint = PowerOn,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            name,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            "ADD",
                            color = PowerOn,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

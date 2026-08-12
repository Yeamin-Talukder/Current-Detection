package com.currentdetection.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryGreen,
    onPrimary = Color(0xFF121212), // Dark text for primary buttons
    primaryContainer = PrimaryGreen,
    onPrimaryContainer = Color(0xFF121212),
    secondary = SurfaceColor, // Secondary buttons use this
    onSecondary = Color.White,
    background = BackgroundColor, // Very dark blue-gray
    onBackground = Color.White, // Light text for dark mode
    surface = SurfaceColor, // Card backgrounds
    onSurface = Color.White, // Text on cards
    surfaceVariant = SurfaceColor, // For dialogs etc
    onSurfaceVariant = Color(0xFFB0BEC5), // Subtle gray text
    error = PowerOff,
    onError = Color.White
)

@Composable
fun CurrentDetectionTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DarkColorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}

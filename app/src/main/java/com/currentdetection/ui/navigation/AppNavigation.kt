package com.currentdetection.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.currentdetection.ui.onboarding.OnboardingScreen
import com.currentdetection.ui.main.MainScreen
import com.currentdetection.ui.checkers.AddCheckerScreen
import com.currentdetection.data.local.SettingsManager
import com.currentdetection.ui.theme.BackgroundColor
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager(context) }
    val onboardingCompletedState = settingsManager.onboardingCompletedFlow.collectAsState(initial = null)
    val onboardingCompleted = onboardingCompletedState.value

    if (onboardingCompleted == null) {
        Box(modifier = Modifier.fillMaxSize().background(BackgroundColor))
        return
    }

    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController, 
        startDestination = if (onboardingCompleted) Screen.Main.route else Screen.Onboarding.route,
        enterTransition = { fadeIn(animationSpec = tween(300)) + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300)) },
        exitTransition = { fadeOut(animationSpec = tween(300)) + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300)) },
        popEnterTransition = { fadeIn(animationSpec = tween(300)) + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300)) },
        popExitTransition = { fadeOut(animationSpec = tween(300)) + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300)) }
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(onFinish = {
                scope.launch {
                    settingsManager.setOnboardingCompleted(true)
                }
                navController.navigate(Screen.Main.route) {
                    popUpTo(Screen.Onboarding.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Main.route) {
            MainScreen(
                onNavigateToAddChecker = { navController.navigate(Screen.AddChecker.route) }
            )
        }
        composable(Screen.AddChecker.route) {
            AddCheckerScreen(
                onBack = { navController.popBackStack() },
                onCheckerAdded = { navController.popBackStack() }
            )
        }
    }
}

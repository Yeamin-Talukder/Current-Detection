package com.currentdetection.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.currentdetection.ui.onboarding.OnboardingScreen
import com.currentdetection.ui.main.MainScreen
import com.currentdetection.ui.checkers.AddCheckerScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController, 
        startDestination = Screen.Onboarding.route,
        enterTransition = { fadeIn(animationSpec = tween(300)) + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300)) },
        exitTransition = { fadeOut(animationSpec = tween(300)) + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300)) },
        popEnterTransition = { fadeIn(animationSpec = tween(300)) + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300)) },
        popExitTransition = { fadeOut(animationSpec = tween(300)) + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300)) }
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(onFinish = {
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

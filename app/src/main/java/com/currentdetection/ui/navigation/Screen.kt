package com.currentdetection.ui.navigation

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Main : Screen("main") // Hosts the bottom nav
    object Checkers : Screen("checkers")
    object AddChecker : Screen("add_checker")
    object UserManual : Screen("user_manual")
}

sealed class BottomNavScreen(val route: String, val title: String) {
    object Home : BottomNavScreen("home", "Home")
    object Networks : BottomNavScreen("networks", "Networks")
    object History : BottomNavScreen("history", "History")
    object Settings : BottomNavScreen("settings", "Settings")
}

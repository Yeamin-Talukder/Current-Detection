package com.currentdetection.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.currentdetection.ui.home.HomeScreen
import com.currentdetection.ui.checkers.CheckersScreen
import com.currentdetection.ui.settings.SettingsScreen
import com.currentdetection.ui.navigation.BottomNavScreen
import com.currentdetection.ui.theme.BackgroundColor
import com.currentdetection.ui.theme.PrimaryGreen
import com.currentdetection.engine.PowerMonitoringService

@Composable
fun MainScreen(onNavigateToAddChecker: () -> Unit) {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Request permissions required for Wi-Fi scanning and Background Monitoring
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val locationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || 
                              permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (locationGranted) {
            val serviceIntent = Intent(context, PowerMonitoringService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent)
            } else {
                context.startService(serviceIntent)
            }
        }
    }

    LaunchedEffect(Unit) {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        
        val missingPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }
        
        if (missingPermissions.isNotEmpty()) {
            permissionLauncher.launch(missingPermissions.toTypedArray())
        } else {
            // Start background service to ensure monitoring remains active
            val serviceIntent = Intent(context, PowerMonitoringService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent)
            } else {
                context.startService(serviceIntent)
            }
        }
    }

    val items = listOf(
        BottomNavScreen.Home to Icons.Filled.Home,
        BottomNavScreen.Networks to Icons.Filled.Wifi,
        BottomNavScreen.Settings to Icons.Filled.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = BackgroundColor,
                contentColor = Color.White
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { (screen, icon) ->
                    NavigationBarItem(
                        icon = { Icon(icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryGreen,
                            selectedTextColor = PrimaryGreen,
                            indicatorColor = Color.Transparent,
                            unselectedIconColor = Color(0xFFB0BEC5),
                            unselectedTextColor = Color(0xFFB0BEC5)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavScreen.Home.route, // Return to Home as default
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavScreen.Home.route) {
                HomeScreen(
                    onAddChecker = onNavigateToAddChecker,
                    onSettingsClick = {
                        navController.navigate(BottomNavScreen.Settings.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable(BottomNavScreen.Networks.route) {
                CheckersScreen(
                    onBack = { },
                    onAddChecker = onNavigateToAddChecker
                )
            }
            composable(BottomNavScreen.Settings.route) {
                SettingsScreen(onManageCheckers = { navController.navigate(BottomNavScreen.Networks.route) })
            }
        }
    }
}

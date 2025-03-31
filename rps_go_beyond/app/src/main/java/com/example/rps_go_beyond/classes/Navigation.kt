package com.example.rps_go_beyond.classes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.rps_go_beyond.screens.*
import com.example.rps_go_beyond.MainActivity

sealed class NavigationItem(var route: String, var icon: ImageVector, var title: String) {
    data object Home : NavigationItem("home", Icons.Outlined.Home, "Home")
    data object Game : NavigationItem("game", Icons.Outlined.PlayArrow, "Play")
    data object Info : NavigationItem("info", Icons.Outlined.Info, "Info")
}

@Composable
fun Navigation(navController: NavHostController, activity: MainActivity) {
    NavHost(navController, startDestination = NavigationItem.Home.route) {
        composable(NavigationItem.Home.route) {
            HomeScreen(activity, navController)
        }
        composable(NavigationItem.Game.route) {
            GameScreen(activity, navController)
        }
        composable(NavigationItem.Info.route) {
            InfoScreen(activity, navController)
        }
        // Tutaj jak trzeba by było podawać konkretne id do elementów na liście,
        // a nie da się dać konkretnego elementu/listy po prostu przez navHosta
//        composable(NavigationItem.AddEquipment.route+"/{tag}") { backStackEntry ->
//            val tag = backStackEntry.arguments?.getString("tag")
//                ?: throw IllegalArgumentException("No tag provided")
//            AddEquipmentScreen(activity, navController, tag)
//        }
    }
}


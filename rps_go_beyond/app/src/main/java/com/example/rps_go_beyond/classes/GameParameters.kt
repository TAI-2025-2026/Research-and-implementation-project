package com.example.rps_go_beyond.classes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class GameParameters(var version: String, var icon: ImageVector, var details: String) {
    data object AI : GameParameters("AI", Icons.Outlined.Settings, "Game against a hard player. \nPlaying against AI that learns your strategy.")
    data object Random : GameParameters("Random", Icons.Outlined.CheckCircle, "Gestures are selected at random. \nNo strategy needed.")
    data object ThreeGestures : GameParameters("3 gestures", Icons.Outlined.Favorite, "You have the 3 basic gestures. \nRock, paper, scissors.")
    data object FiveGestures : GameParameters("5 gestures", Icons.Outlined.FavoriteBorder, "You have the 5 basic gestures. \nRock, paper, scissors, ...")
    data object SevenGestures : GameParameters("7 gestures", Icons.Outlined.FavoriteBorder, "You have the 7 basic gestures. \nRock, paper, scissors, ...")
}
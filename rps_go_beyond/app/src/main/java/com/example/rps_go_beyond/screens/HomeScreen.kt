package com.example.rps_go_beyond.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rps_go_beyond.MainActivity
import com.example.rps_go_beyond.classes.GameParameters
import com.example.rps_go_beyond.classes.NavigationItem

@Composable
fun HomeScreen (activity: MainActivity, navController: NavHostController) {

    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomNavigationBar(navController) },
        content = { padding ->
            Box(modifier = Modifier
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
            ) {
                GamePreferences(activity, navController)
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {

                },
                containerColor = MaterialTheme.colorScheme.onSecondary,
            ) {
                Icon(
                    imageVector = NavigationItem.Info.icon,
                    contentDescription = "Edit Item",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
    )
}

@Composable
fun GamePreferences (activity: MainActivity, navController: NavHostController) {
    var chosenMode by remember { mutableStateOf(false) }
    var chosenLevel by remember { mutableStateOf(false) }
    var selectedMode by remember { mutableStateOf<GameParameters?>(null) }
    var selectedLevel by remember { mutableStateOf<GameParameters?>(null) }

    val gameModes = listOf(
        GameParameters.ThreeGestures,
        GameParameters.FiveGestures,
        GameParameters.SevenGestures
    )
    val gameLevels = listOf(
        GameParameters.Easy,
        GameParameters.Medium,
        GameParameters.Hard
    )

    // to change -- I don't have the energy today for this
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!chosenMode) {
            items(gameModes) { mode ->
                CardInterface(activity, navController, mode) {
                    selectedMode = mode
                    chosenMode = true
                }
            }
        }
        else {
            item {
                CardInterface(activity, navController, selectedMode!!) {
                    selectedMode = null
                    chosenMode = false
                }
            }

            if (!chosenLevel) {
                items(gameLevels) { level ->
                    CardInterface(activity, navController, level) {
                        selectedLevel = level
                        chosenLevel = true
                    }
                }
            } else {
                item {
                    CardInterface(activity, navController, selectedLevel!!) {
                        selectedLevel = null
                        chosenMode = false
                    }
                }
            }
        }
        if(chosenLevel and chosenMode){
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onBackground.copy(0.4f),
                            shape = MaterialTheme.shapes.medium
                        )
                        .clickable {
                            navController.navigate(NavigationItem.Game.route){
                                navController.graph.startDestinationRoute?.let {
                                    route -> popUpTo(route) {
                                        inclusive = true
                                    }
                                }
                                launchSingleTop = true
                                restoreState = false
                            }
                        },
                    color = MaterialTheme.colorScheme.background.copy(0.4f),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "PLAY!",
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}
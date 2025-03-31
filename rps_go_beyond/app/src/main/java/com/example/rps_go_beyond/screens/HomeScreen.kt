package com.example.rps_go_beyond.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.rps_go_beyond.MainActivity
import com.example.rps_go_beyond.screens.BottomNavigationBar
import com.example.rps_go_beyond.classes.GameParameters
import com.example.rps_go_beyond.classes.NavigationItem
import com.example.rps_go_beyond.screens.TopBar
import kotlinx.coroutines.launch

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
                MainScreen(activity, navController)
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
fun MainScreen (activity: MainActivity, navController: NavHostController) {
    val gameModes = listOf(
        GameParameters.AI,
        GameParameters.Random,
        GameParameters.ThreeGestures,
        GameParameters.FiveGestures,
        GameParameters.SevenGestures
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(gameModes) { mode->
            CardInterface(activity, navController, mode)
        }
    }
}
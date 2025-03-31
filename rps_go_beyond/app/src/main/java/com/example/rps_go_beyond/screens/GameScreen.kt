package com.example.rps_go_beyond.screens

import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.rps_go_beyond.MainActivity
import com.example.rps_go_beyond.classes.NavigationItem
import com.google.common.util.concurrent.ListenableFuture

@Composable
fun GameScreen (activity: MainActivity, navController: NavHostController) {

    Scaffold(
        topBar = { TopBar() },
        content = { padding ->
            Box(modifier = Modifier
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
            ) {
                GameBoxView(activity, navController)
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(NavigationItem.Home.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                inclusive = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = false
                    }
                },
                containerColor = MaterialTheme.colorScheme.onSecondary,
            ) {
                Icon(
                    imageVector = NavigationItem.Home.icon,
                    contentDescription = "Edit Item",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
    )
}

@Composable
fun GameBoxView (activity: MainActivity, navController: NavHostController) {
    val context = LocalContext.current
    val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> = ProcessCameraProvider.getInstance(context)
    var cameraSelector by remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            // Top half content
            Text(
                text = "Top Half",
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondary)
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Middle Row",
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiary)
        ) {
            AndroidView(
                factory = { context ->
                    val previewView = PreviewView(context)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = androidx.camera.core.Preview.Builder().build()

                        preview.setSurfaceProvider(previewView.surfaceProvider)
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(activity, cameraSelector, preview)
                    }, ContextCompat.getMainExecutor(context))

                    previewView
                },
                modifier = Modifier.fillMaxHeight()
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA }) {
                Text("Back Camera")
            }
            Button(onClick = { cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA }) {
                Text("Front Camera")
            }
        }
    }
}
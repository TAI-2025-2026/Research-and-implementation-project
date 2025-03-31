package com.example.rps_go_beyond

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rps_go_beyond.classes.Navigation
import com.example.rps_go_beyond.ui.theme.RPS_Go_BeyondTheme
import com.example.rps_go_beyond.classes.NavigationItem

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (!hasCameraPermission()) {
            requestCameraPermission()
        }

        setupContent()
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
    }

    private fun setupContent() {
        setContent {
            RPS_Go_BeyondTheme {
                navController = rememberNavController()
                Navigation(navController = navController, activity = this)
            }
        }
    }
}

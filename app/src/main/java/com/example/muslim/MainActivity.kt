package com.example.muslim

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.muslim.navigation.AppNavHost
import com.example.muslim.presentation.designSystem.theme.MuslimTheme
import com.example.muslim.presentation.designSystem.theme.Theme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {
    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                // Optionally show a message explaining alarms won't show without this
                Toast.makeText(
                    this,
                    "Notification permission denied. Alarms may not show.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermissionIfNeeded()
        enableEdgeToEdge()
        setContent {
            MuslimTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Theme.colors.background
                ) { innerPadding ->
                    val navController = rememberNavController()
                    AppNavHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }


    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!granted) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}



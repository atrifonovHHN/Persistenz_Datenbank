package com.example.persistenz_datenbanken

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.persistenz_datenbanken.ui.screen.DashboardScreen
import com.example.persistenz_datenbanken.ui.theme.PersistenzDatenbankenTheme

/**
 * The main activity of the application
 * It sets up the UI and handles navigation between different screens
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PersistenzDatenbankenTheme {
                // Surface is the root composable that holds the entire UI
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainNavHost()  // The navigation host for handling screen navigation
                }
            }
        }
    }
}

/**
 * Sets up the navigation graph for the app
 * It defines the different screens and how the user navigates between them
 */
@Composable
fun MainNavHost() {
    val navController = rememberNavController()  // Navigation controller to handle navigation

    // Set up the navigation graph with the start destination
    NavHost(navController = navController, startDestination = "dashboard") {
        // Define the composable for the dashboard screen
        composable("dashboard") {
            DashboardScreen()  // The DashboardScreen is the initial screen shown to the user
        }
    }
}

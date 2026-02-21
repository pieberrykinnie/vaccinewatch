package com.example.vaccinewatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vaccinewatch.navigation.VaccineWatchBottomBar
import com.example.vaccinewatch.ui.reminder.ReminderScreen
import com.example.vaccinewatch.ui.tracker.TrackerScreen
import com.example.vaccinewatch.ui.vaccines.VaccinesScreen
import com.example.vaccinewatch.ui.theme.VaccineWatchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VaccineWatchTheme {
                VaccineWatchApp()
            }
        }
    }
}

@Composable
fun VaccineWatchApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { VaccineWatchBottomBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "reminder",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("reminder") {
                ReminderScreen(
                    onNavigateToTracker = {
                        navController.navigate("tracker") {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable("vaccines") {
                VaccinesScreen()
            }
            composable("tracker") {
                TrackerScreen()
            }
        }
    }
}

package com.example.vaccinewatch.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    data object Reminder : BottomNavItem("reminder", "Reminder", Icons.Default.Notifications)
    data object Vaccines : BottomNavItem("vaccines", "Vaccines", Icons.Default.MedicalServices)
    data object Tracker : BottomNavItem("tracker", "Tracker", Icons.Default.Timeline)
}

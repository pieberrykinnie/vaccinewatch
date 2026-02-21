package com.example.vaccinewatch.ui.reminder

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vaccinewatch.data.MockDataRepository
import com.example.vaccinewatch.ui.components.SectionHeader
import com.example.vaccinewatch.ui.components.VaccineCard

@Composable
fun ReminderScreen(onNavigateToTracker: () -> Unit = {}) {
    val dueVaccines = remember {
        MockDataRepository.getOverdueVaccines() + MockDataRepository.getEligibleVaccines()
    }
    val upcomingVaccines = remember { MockDataRepository.getUpcomingVaccines() }

    var smartwatchEnabled by remember { mutableStateOf(false) }
    var emailEnabled by remember { mutableStateOf(false) }
    var smsEnabled by remember { mutableStateOf(false) }

    LazyColumn(
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // Section 1: Due Vaccinations
        item { SectionHeader("Due Vaccinations") }

        if (dueVaccines.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "You're all caught up! No vaccines are currently due.",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        } else {
            items(dueVaccines, key = { it.id }) { vaccine ->
                VaccineCard(vaccine = vaccine, onClick = {})
            }
        }

        item {
            OutlinedButton(
                onClick = onNavigateToTracker,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Just vaccinated? Track your vaccination!")
            }
        }

        // Section 2: Upcoming Vaccinations
        item {
            Spacer(modifier = Modifier.height(8.dp))
            SectionHeader("Upcoming Vaccinations")
        }

        if (upcomingVaccines.isEmpty()) {
            item {
                Text(
                    text = "No upcoming vaccinations scheduled.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        } else {
            items(upcomingVaccines, key = { it.id }) { vaccine ->
                VaccineCard(vaccine = vaccine, onClick = {})
            }
        }

        // Section 3: Notifications
        item {
            Spacer(modifier = Modifier.height(8.dp))
            SectionHeader("Notifications")
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column {
                    NotificationRow(
                        icon = { Icon(Icons.Default.Watch, contentDescription = null) },
                        label = "Smartwatch",
                        checked = smartwatchEnabled,
                        onCheckedChange = { smartwatchEnabled = it }
                    )
                    NotificationRow(
                        icon = { Icon(Icons.Default.Email, contentDescription = null) },
                        label = "Email",
                        checked = emailEnabled,
                        onCheckedChange = { emailEnabled = it }
                    )
                    NotificationRow(
                        icon = { Icon(Icons.Default.Sms, contentDescription = null) },
                        label = "SMS",
                        checked = smsEnabled,
                        onCheckedChange = { smsEnabled = it }
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationRow(
    icon: @Composable () -> Unit,
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        icon()
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = label, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

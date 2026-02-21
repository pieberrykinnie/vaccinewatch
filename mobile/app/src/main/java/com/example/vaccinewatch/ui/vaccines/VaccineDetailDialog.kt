package com.example.vaccinewatch.ui.vaccines

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vaccinewatch.model.Vaccine
import com.example.vaccinewatch.model.VaccineStatus
import com.example.vaccinewatch.ui.components.StatusBadge
import com.example.vaccinewatch.ui.theme.StatusOverdue
import java.time.format.DateTimeFormatter

@Composable
fun VaccineDetailDialog(vaccine: Vaccine, onDismiss: () -> Unit) {
    val dateFormat = DateTimeFormatter.ofPattern("MMM d, yyyy")

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        },
        title = { Text(vaccine.name) },
        text = {
            Column {
                StatusBadge(vaccine.status)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = vaccine.description,
                    style = MaterialTheme.typography.bodyMedium
                )
                vaccine.nextDoseDate?.let { date ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Next dose: ${date.format(dateFormat)}",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                if (vaccine.status == VaccineStatus.OVERDUE && vaccine.overdueDate != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Overdue since: ${vaccine.overdueDate.format(dateFormat)}",
                        style = MaterialTheme.typography.labelLarge,
                        color = StatusOverdue
                    )
                }
            }
        }
    )
}

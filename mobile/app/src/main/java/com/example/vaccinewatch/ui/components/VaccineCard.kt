package com.example.vaccinewatch.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vaccinewatch.model.Vaccine
import com.example.vaccinewatch.model.VaccineStatus
import com.example.vaccinewatch.ui.theme.VaccineWatchTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun VaccineCard(vaccine: Vaccine, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = vaccine.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                StatusBadge(vaccine.status)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = vaccine.description,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (vaccine.nextDoseDate != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Next dose: ${vaccine.nextDoseDate.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))}",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewVaccineCard() {
    VaccineWatchTheme {
        VaccineCard(
            vaccine = Vaccine(
                id = "1",
                name = "MMR (Measles, Mumps, Rubella)",
                description = "Protects against measles, mumps, and rubella viruses",
                status = VaccineStatus.ELIGIBLE,
                nextDoseDate = LocalDate.now(),
                overdueDate = LocalDate.now().plusDays(30)
            ),
            onClick = {}
        )
    }
}

package com.example.vaccinewatch.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vaccinewatch.model.VaccineStatus
import com.example.vaccinewatch.ui.theme.*

@Composable
fun StatusBadge(status: VaccineStatus, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = statusToColor(status),
        modifier = modifier
    ) {
        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
            if (status == VaccineStatus.CONFIRM_DOCTOR) {
                Text("? ", color = OnPrimary)
            }
            Text(
                text = statusToString(status),
                color = OnPrimary,
            )
        }
    }
}

fun statusToColor(status: VaccineStatus): Color {
    if (status == VaccineStatus.COMPLETED) {
        return StatusCompleted
    } else if (status == VaccineStatus.NOT_YET_DUE) {
        return StatusNotYetDue
    } else if (status == VaccineStatus.ELIGIBLE) {
        return StatusEligible
    } else if (status == VaccineStatus.OVERDUE) {
        return StatusOverdue
    } else if (status == VaccineStatus.NOT_ELIGIBLE) {
        return StatusNotEligible
    } else if (status == VaccineStatus.CONFIRM_DOCTOR) {
        return StatusConfirmDoctor
    } else {
        return StatusIrrelevant
    }
}

fun statusToString(status: VaccineStatus): String {
    if (status == VaccineStatus.COMPLETED) {
        return "Completed"
    } else if (status == VaccineStatus.NOT_YET_DUE) {
        return "Not Yet Due"
    } else if (status == VaccineStatus.ELIGIBLE) {
        return "Eligible"
    } else if (status == VaccineStatus.OVERDUE) {
        return "Overdue"
    } else if (status == VaccineStatus.NOT_ELIGIBLE) {
        return "Not Eligible"
    } else if (status == VaccineStatus.CONFIRM_DOCTOR) {
        return "Confirm with Doctor"
    } else {
        return "Irrelevant"
    }
}

@Preview
@Composable
fun PreviewStatusBadge() {
    VaccineWatchTheme {
        StatusBadge(VaccineStatus.CONFIRM_DOCTOR)
    }
}
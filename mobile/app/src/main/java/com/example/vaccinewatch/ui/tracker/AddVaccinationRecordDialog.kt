package com.example.vaccinewatch.ui.tracker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vaccinewatch.data.MockDataRepository
import com.example.vaccinewatch.model.VaccinationRecord
import java.time.LocalDate
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVaccinationRecordDialog(
    onDismiss: () -> Unit,
    onAdd: (VaccinationRecord) -> Unit
) {
    var vaccineName by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var dateText by remember { mutableStateOf("") }

    val isDateValid = remember(dateText) {
        try {
            LocalDate.parse(dateText)
            true
        } catch (_: Exception) {
            false
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Vaccination Record") },
        confirmButton = {
            TextButton(
                onClick = {
                    val record = VaccinationRecord(
                        id = UUID.randomUUID().toString(),
                        vaccineName = vaccineName,
                        dateAdministered = LocalDate.parse(dateText)
                    )
                    onAdd(record)
                },
                enabled = vaccineName.isNotBlank() && isDateValid
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        text = {
            Column {
                // Vaccine name with dropdown search
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            vaccineName = it
                            expanded = true
                        },
                        label = { Text("Vaccine") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    val filtered = MockDataRepository.vaccines.filter {
                        it.name.contains(searchQuery, ignoreCase = true)
                    }
                    if (filtered.isNotEmpty() && searchQuery.isNotBlank()) {
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            filtered.forEach { vaccine ->
                                DropdownMenuItem(
                                    text = { Text(vaccine.name) },
                                    onClick = {
                                        vaccineName = vaccine.name
                                        searchQuery = vaccine.name
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Date field
                OutlinedTextField(
                    value = dateText,
                    onValueChange = { dateText = it },
                    label = { Text("Date (YYYY-MM-DD)") },
                    isError = dateText.isNotBlank() && !isDateValid,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}

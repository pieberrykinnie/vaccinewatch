package com.example.vaccinewatch.ui.tracker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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

@Composable
fun TrackerScreen() {
    var showAddDialog by remember { mutableStateOf(false) }
    var records by remember { mutableStateOf(MockDataRepository.getHistorySorted()) }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                icon = { Icon(Icons.Default.Add, contentDescription = "Add record") },
                text = { Text("Add Record") }
            )
        }
    ) { innerPadding ->
        if (records.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No vaccination records yet.\nTap + to add your first record.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(innerPadding),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                item { SectionHeader("Immunization History") }
                items(records, key = { it.id }) { record ->
                    VaccinationRecordCard(
                        record = record,
                        onDelete = { records = records.filter { it.id != record.id } }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddVaccinationRecordDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { newRecord ->
                records = (records + newRecord).sortedByDescending { it.dateAdministered }
                showAddDialog = false
            }
        )
    }
}

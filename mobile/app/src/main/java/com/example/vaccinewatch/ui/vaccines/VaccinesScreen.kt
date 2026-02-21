package com.example.vaccinewatch.ui.vaccines

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vaccinewatch.data.MockDataRepository
import com.example.vaccinewatch.model.Vaccine
import com.example.vaccinewatch.model.VaccineStatus
import com.example.vaccinewatch.ui.components.VaccineCard
import com.example.vaccinewatch.ui.components.statusToString

@Composable
fun VaccinesScreen() {
    var selectedFilters by remember { mutableStateOf(setOf<VaccineStatus>()) }
    var showIrrelevant by remember { mutableStateOf(false) }
    var selectedVaccine by remember { mutableStateOf<Vaccine?>(null) }

    val filteredVaccines = remember(selectedFilters, showIrrelevant) {
        val all = MockDataRepository.vaccines
        when {
            selectedFilters.isNotEmpty() -> all.filter { it.status in selectedFilters }
            showIrrelevant -> all
            else -> all.filter { it.status != VaccineStatus.IRRELEVANT }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Filter chips
        LazyRow(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val statuses = VaccineStatus.entries.filter { it != VaccineStatus.IRRELEVANT }
            items(statuses) { status ->
                FilterChip(
                    selected = status in selectedFilters,
                    onClick = {
                        selectedFilters = if (status in selectedFilters) {
                            selectedFilters - status
                        } else {
                            selectedFilters + status
                        }
                    },
                    label = { Text(statusToString(status)) },
                    leadingIcon = if (status in selectedFilters) {
                        { Icon(Icons.Default.Check, contentDescription = null) }
                    } else null
                )
            }
            item {
                FilterChip(
                    selected = showIrrelevant,
                    onClick = { showIrrelevant = !showIrrelevant },
                    label = { Text("Irrelevant") },
                    leadingIcon = if (showIrrelevant) {
                        { Icon(Icons.Default.Check, contentDescription = null) }
                    } else null
                )
            }
        }

        // Vaccine list
        LazyColumn {
            items(filteredVaccines, key = { it.id }) { vaccine ->
                VaccineCard(
                    vaccine = vaccine,
                    onClick = { selectedVaccine = vaccine }
                )
            }
        }
    }

    // Detail dialog
    selectedVaccine?.let { vaccine ->
        VaccineDetailDialog(
            vaccine = vaccine,
            onDismiss = { selectedVaccine = null }
        )
    }
}

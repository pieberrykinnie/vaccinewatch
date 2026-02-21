package com.example.vaccinewatch.model

import java.time.LocalDate

// Represents the vaccine ONLY in relation to that user
data class Vaccine(
    val id: String,
    val name: String,
    val description: String,           // popup
    val status: VaccineStatus,
    val nextDoseDate: LocalDate?,      // null if completed/not eligible/irrelevant
    val overdueDate: LocalDate?,       // null if not applicable
)
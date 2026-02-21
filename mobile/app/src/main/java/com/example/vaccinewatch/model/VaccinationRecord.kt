package com.example.vaccinewatch.model

import java.time.LocalDate

data class VaccinationRecord(
    val id: String,
    val vaccineName: String,
    val dateAdministered: LocalDate,
)
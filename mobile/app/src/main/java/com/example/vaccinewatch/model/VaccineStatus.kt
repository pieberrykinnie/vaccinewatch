package com.example.vaccinewatch.model

enum class VaccineStatus {
    // check the docs for what these mean
    COMPLETED,      // Blue
    NOT_YET_DUE,    // Yellow
    ELIGIBLE,       // Green
    OVERDUE,        // Orange
    NOT_ELIGIBLE,   // Red
    CONFIRM_DOCTOR, // Question mark overlay
    IRRELEVANT      // Grey
}
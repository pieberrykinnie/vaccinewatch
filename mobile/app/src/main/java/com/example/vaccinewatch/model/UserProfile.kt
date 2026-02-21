package com.example.vaccinewatch.model

import java.time.LocalDate

data class UserProfile(
    val dateOfBirth: LocalDate,
    val sexAtBirth: SexAtBirth?,
    val genderIdentity: String,
    val postalCode: String,
    val isFirstNationsResident: Boolean?,

    val isPregnant: Boolean?,
    val estimatedDueDate: LocalDate?,
    val hasImmunocompromising: TriState?,
    val hasSpecializedCare: TriState?,
    val hasChronicIllness: TriState?,

    val isHealthcareWorker: Boolean?,
    val wantsLifestyleReview: Boolean?,
    val planningTravel: Boolean?,

    val smartwatchEnabled: Boolean,
    val emailEnabled: Boolean,
    val emailAddress: String,
    val smsEnabled: Boolean,
    val phoneNumber: String
)

enum class SexAtBirth { MALE, FEMALE, INTERSEX }

enum class TriState { YES, NO, UNSURE }
package com.example.vaccinewatch.data

import com.example.vaccinewatch.model.UserProfile
import com.example.vaccinewatch.model.Vaccine
import com.example.vaccinewatch.model.VaccinationRecord
import com.example.vaccinewatch.model.VaccineStatus
import java.time.LocalDate

object MockDataRepository {

    val vaccines: List<Vaccine> = listOf(
        Vaccine(
            "1",
            "MMR (Measles, Mumps, Rubella)",
            "Protects against measles, mumps, and rubella viruses",
            VaccineStatus.ELIGIBLE,
            LocalDate.now(),
            LocalDate.now().plusDays(30)
        ),
        Vaccine(
            "2",
            "Tdap (Tetanus, Diphtheria, Pertussis)",
            "Booster for tetanus, diphtheria, and whooping cough",
            VaccineStatus.OVERDUE,
            LocalDate.now().minusDays(14),
            LocalDate.now().minusDays(14)
        ),
        Vaccine(
            "3",
            "Influenza (Flu)",
            "Annual seasonal influenza protection",
            VaccineStatus.NOT_YET_DUE,
            LocalDate.now().plusMonths(3),
            null
        ),
        Vaccine(
            "4",
            "COVID-19",
            "Protection against SARS-CoV-2",
            VaccineStatus.COMPLETED,
            null,
            null
        ),
        Vaccine(
            "5",
            "Hepatitis B",
            "Protects against hepatitis B virus",
            VaccineStatus.COMPLETED,
            null,
            null
        ),
        Vaccine(
            "6",
            "HPV (Human Papillomavirus)",
            "Protects against HPV-related cancers",
            VaccineStatus.NOT_ELIGIBLE,
            null,
            null
        ),
        Vaccine(
            "7",
            "Varicella (Chickenpox)",
            "Protection against chickenpox",
            VaccineStatus.COMPLETED,
            null,
            null
        ),
        Vaccine(
            "8",
            "Pneumococcal",
            "Protects against pneumococcal diseases",
            VaccineStatus.CONFIRM_DOCTOR,
            LocalDate.now().plusDays(60),
            null
        ),
        Vaccine(
            "9",
            "Meningococcal",
            "Protects against meningococcal disease",
            VaccineStatus.ELIGIBLE,
            LocalDate.now(),
            LocalDate.now().plusDays(45)
        ),
        Vaccine(
            "10",
            "Yellow Fever",
            "Required for travel to certain regions",
            VaccineStatus.IRRELEVANT,
            null,
            null
        ),
        Vaccine(
            "11",
            "Japanese Encephalitis",
            "For travel to endemic areas in Asia",
            VaccineStatus.IRRELEVANT,
            null,
            null
        ),
        Vaccine(
            "12",
            "Shingles (Herpes Zoster)",
            "Protects against shingles in older adults",
            VaccineStatus.NOT_YET_DUE,
            LocalDate.now().plusYears(5),
            null
        ),
    )

    val vaccinationHistory: List<VaccinationRecord> = listOf(
        VaccinationRecord("r1", "COVID-19", LocalDate.of(2024, 10, 15)),
        VaccinationRecord("r2", "COVID-19", LocalDate.of(2023, 9, 20)),
        VaccinationRecord("r3", "Hepatitis B", LocalDate.of(2020, 3, 10)),
        VaccinationRecord("r4", "Hepatitis B", LocalDate.of(2019, 9, 5)),
        VaccinationRecord("r5", "Hepatitis B", LocalDate.of(2019, 3, 1)),
        VaccinationRecord("r6", "MMR", LocalDate.of(2010, 6, 15)),
        VaccinationRecord("r7", "Varicella", LocalDate.of(2008, 4, 20)),
        VaccinationRecord("r8", "Varicella", LocalDate.of(2005, 7, 10)),
    )

    val defaultProfile: UserProfile = UserProfile(
        dateOfBirth = LocalDate.of(2000, 1, 1),
        sexAtBirth = null,
        genderIdentity = "",
        postalCode = "",
        isFirstNationsResident = null,
        isPregnant = null,
        estimatedDueDate = null,
        hasImmunocompromising = null,
        hasSpecializedCare = null,
        hasChronicIllness = null,
        isHealthcareWorker = null,
        wantsLifestyleReview = null,
        planningTravel = null,
        smartwatchEnabled = false,
        emailEnabled = false,
        emailAddress = "",
        smsEnabled = false,
        phoneNumber = ""
    )

    fun getEligibleVaccines(): List<Vaccine> =
        vaccines.filter { it.status == VaccineStatus.ELIGIBLE }
            .sortedBy { it.nextDoseDate }

    fun getUpcomingVaccines(): List<Vaccine> =
        vaccines.filter { it.status == VaccineStatus.NOT_YET_DUE }
            .sortedBy { it.nextDoseDate }

    fun getOverdueVaccines(): List<Vaccine> =
        vaccines.filter { it.status == VaccineStatus.OVERDUE }
            .sortedBy { it.overdueDate }

    fun getVisibleVaccines(): List<Vaccine> =
        vaccines.filter { it.status != VaccineStatus.IRRELEVANT }

    fun getHistorySorted(): List<VaccinationRecord> =
        vaccinationHistory.sortedByDescending { it.dateAdministered }
}

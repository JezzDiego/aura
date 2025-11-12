package com.example.aura.domain.model

data class MedicationSchedule(
    val medication: Medication,
    val scheduleTimes: List<String>
)
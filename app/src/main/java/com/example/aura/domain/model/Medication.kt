package com.example.aura.domain.model

data class Medication(
    val id: Int,
    val name: String,
    val doseDetails: String,
    val intervalInHours: Int,
    val startTime: String
)
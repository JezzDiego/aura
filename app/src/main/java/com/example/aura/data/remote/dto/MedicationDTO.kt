package com.example.aura.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MedicationDTO(
    val id: String,

    val name: String,

    val dosage: String,

    @SerialName("interval_hours")
    val intervalHours: Int,

    val description: String
)


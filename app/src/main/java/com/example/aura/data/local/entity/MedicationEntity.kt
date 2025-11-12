package com.example.aura.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medication")
data class MedicationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val doseDetails: String,
    val intervalInHours: Int,
    val startTime: String,
    val description: String
)
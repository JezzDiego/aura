package com.example.aura.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "laboratories")
data class LaboratoryEntity(
    @PrimaryKey val id: String,
    val name: String,
    val address: String?,
    val phone: String?,
    val email: String?
)

package com.example.aura.data.local.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity (
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val birthDate: String?,
    val gender: String?,
)
package com.example.aura.data.mapper

import com.example.aura.data.local.entity.MedicationEntity
import com.example.aura.data.remote.dto.MedicationDTO
import com.example.aura.domain.model.Medication

fun Medication.toEntity(): MedicationEntity {
    return MedicationEntity(
        id = this.id,
        name = this.name,
        doseDetails = this.doseDetails,
        intervalInHours = this.intervalInHours,
        startTime = this.startTime
    )
}

fun MedicationEntity.toDomain(): Medication {
    return Medication(
        id = this.id,
        name = this.name,
        doseDetails = this.doseDetails,
        intervalInHours = this.intervalInHours,
        startTime = this.startTime
    )
}

fun MedicationDTO.toDomain(): Medication {
    return Medication(
        id = 0,
        name = this.name,
        doseDetails = this.strength,
        intervalInHours = 0,
        startTime = "00:00"
    )
}
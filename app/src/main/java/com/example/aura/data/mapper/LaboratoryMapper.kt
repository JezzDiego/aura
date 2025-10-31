package com.example.aura.data.mapper

import com.example.aura.data.local.entity.LaboratoryEntity
import com.example.aura.data.remote.dto.LaboratoryDTO
import com.example.aura.domain.model.Laboratory

fun LaboratoryDTO.toDomain(): Laboratory {
    return Laboratory(
        id = id,
        name = name,
        address = address,
        phone = phone,
        email = email
    )
}

fun Laboratory.toEntity(): LaboratoryEntity {
    return LaboratoryEntity(
        id = id,
        name = name,
        address = address,
        phone = phone,
        email = email
    )
}

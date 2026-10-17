package com.example.aura.data.mapper

import com.example.aura.data.local.entity.UserEntity
import com.example.aura.data.remote.dto.UserDTO
import com.example.aura.domain.model.AccessibilityFilter
import com.example.aura.domain.model.AccessibilityProfile
import com.example.aura.domain.model.User

fun UserEntity.toDomain(): User = User(
    id = id,
    name = name,
    email = email,
    profile = profile.toEnumOrNull<AccessibilityProfile>(),
    preferredFilters = preferredFilters.mapNotNull { it.toEnumOrNull<AccessibilityFilter>() }.toSet(),
    createdAt = createdAt,
)

fun User.toEntity(): UserEntity = UserEntity(
    id = id,
    name = name,
    email = email,
    profile = profile?.name,
    preferredFilters = preferredFilters.map { it.name },
    createdAt = createdAt,
)

fun UserDTO.toDomain(): User = User(
    id = id,
    name = name,
    email = email,
    profile = profile.toEnumOrNull<AccessibilityProfile>(),
    preferredFilters = preferredFilters.orEmpty().mapNotNull { it.toEnumOrNull<AccessibilityFilter>() }.toSet(),
)

fun User.toDto(): UserDTO = UserDTO(
    id = id,
    name = name,
    email = email,
    profile = profile?.name,
    preferredFilters = preferredFilters.map { it.name },
)

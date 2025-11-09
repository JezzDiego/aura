package com.example.aura.data.mapper

import com.example.aura.data.local.entity.UserEntity
import com.example.aura.data.remote.dto.UserDTO
import com.example.aura.domain.model.SubscriptionPlan
import com.example.aura.domain.model.User

fun UserDTO.toDomain(): User {
    return User(
        id = id,
        name = name,
        email = email,
        password = password,
        birthDate = birthDate,
        gender = gender,
        healthInsurance = healthInsurance,
        subscriptionPlan = plan.let { subscriptionPlan ->
            when (subscriptionPlan) {
                SubscriptionPlan.FREE -> SubscriptionPlan.FREE
                SubscriptionPlan.PREMIUM -> SubscriptionPlan.PREMIUM
            }
        },
    )
}

fun UserEntity.toEntity(): UserEntity{
    return UserEntity(
        id = id,
        name = name,
        email = email,
        birthDate = birthDate,
        gender = gender,
        healthInsurance = healthInsurance
    )
}

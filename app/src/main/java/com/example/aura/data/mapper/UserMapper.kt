package com.example.aura.data.mapper

import com.example.aura.data.remote.dto.UserDTO
import com.example.aura.domain.model.SubscriptionPlan
import com.example.aura.domain.model.User

fun UserDTO.toDomain(): User {
    return User(
        id = id,
        name = name,
        email = email,
        birthDate = birthDate,
        gender = gender,
        subscriptionPlan = plan.let { subscriptionPlan ->
            when (subscriptionPlan) {
                SubscriptionPlan.FREE -> SubscriptionPlan.FREE
                SubscriptionPlan.PREMIUM -> SubscriptionPlan.PREMIUM
            }
        },
    )
}

fun User.toDto(): UserDTO {
    return UserDTO(
        id = id,
        name = name,
        email = email,
        birthDate = birthDate,
        gender = gender,
        plan = subscriptionPlan.let { subscriptionPlan ->
            when (subscriptionPlan) {
                SubscriptionPlan.FREE -> SubscriptionPlan.FREE
                SubscriptionPlan.PREMIUM -> SubscriptionPlan.PREMIUM
            }
        },
    )
}

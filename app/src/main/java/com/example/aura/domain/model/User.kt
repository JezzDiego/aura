package com.example.aura.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val birthDate: String?,
    val gender: String?,
    val healthInsurance: String?,
    val subscriptionPlan: SubscriptionPlan = SubscriptionPlan.FREE,
)

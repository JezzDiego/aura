package com.example.aura.domain.model

enum class SubscriptionPlan(val limitExams: Int) {
    FREE(20),
    PREMIUM(Int.MAX_VALUE)
}

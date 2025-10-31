package com.example.aura.domain.repository

import com.example.aura.domain.model.SubscriptionPlan

interface SubscriptionRepository {
    suspend fun getCurrentPlan(userId: String): SubscriptionPlan
    suspend fun upgradePlan(userId: String, newPlan: SubscriptionPlan)
    suspend fun isLimitReached(userId: String): Boolean
}

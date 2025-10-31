package com.example.aura.domain.repository

import com.example.aura.domain.model.User

interface UserRepository {

    suspend fun getUserProfile(): User

    suspend fun updateUserProfile(user: User): User

    suspend fun deleteAccount(userId: String)

    suspend fun isUserPremium(): Boolean
}

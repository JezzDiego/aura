package com.example.aura.data.repository

import com.example.aura.data.remote.api.UserApi
import com.example.aura.data.mapper.toDomain
import com.example.aura.data.mapper.toDto
import com.example.aura.domain.model.User
import com.example.aura.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val api: UserApi
) : UserRepository {

    private var cachedUser: User? = null

    override suspend fun getUserProfile(): User = withContext(Dispatchers.IO) {
        cachedUser ?: api.getUserProfile().toDomain().also { cachedUser = it }
    }

    override suspend fun updateUserProfile(user: User): User = withContext(Dispatchers.IO) {
        val updated = api.updateUser(user.toDto()).toDomain()
        cachedUser = updated
        updated
    }

    override suspend fun deleteAccount(userId: String) = withContext(Dispatchers.IO) {
        api.deleteAccount(userId)
        cachedUser = null
    }

    override suspend fun isUserPremium(): Boolean = withContext(Dispatchers.IO) {
        cachedUser?.subscriptionPlan?.name == "PREMIUM"
    }
}

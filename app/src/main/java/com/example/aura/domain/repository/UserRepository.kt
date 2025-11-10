package com.example.aura.domain.repository

import com.example.aura.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun loginUser(email: String, password: String): User?
    suspend fun getAllUsers(): List<User>
    suspend fun logout()
    fun observeUser(): Flow<User?>
}

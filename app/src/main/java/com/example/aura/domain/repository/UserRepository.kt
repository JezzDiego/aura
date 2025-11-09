package com.example.aura.domain.repository

import com.example.aura.domain.model.User

interface UserRepository {
    suspend fun loginUser(email: String, password: String): User?

    suspend fun logout()

}

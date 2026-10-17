package com.example.aura.domain.repository

import com.example.aura.domain.model.AccessibilityFilter
import com.example.aura.domain.model.AccessibilityProfile
import com.example.aura.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    /** Cria (ou recupera) o perfil local da pessoa usuária. */
    suspend fun login(name: String, email: String, profile: AccessibilityProfile?): User

    suspend fun updateProfile(profile: AccessibilityProfile?, preferredFilters: Set<AccessibilityFilter>)

    suspend fun updateName(name: String)

    suspend fun logout()

    fun observeUser(): Flow<User?>
}

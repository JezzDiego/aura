package com.example.aura.data.repository

import com.example.aura.data.local.datasource.UserLocalDataSource
import com.example.aura.data.mapper.toDomain
import com.example.aura.data.mapper.toEntity
import com.example.aura.data.remote.datasource.UserRemoteDataSource
import com.example.aura.domain.model.AccessibilityFilter
import com.example.aura.domain.model.AccessibilityProfile
import com.example.aura.domain.model.User
import com.example.aura.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID

/**
 * O "login" cria um perfil local (sem autenticação remota por enquanto).
 */
class UserRepositoryImpl(
    private val localDS: UserLocalDataSource,
    @Suppress("unused") private val remoteDS: UserRemoteDataSource,
) : UserRepository {

    override suspend fun login(name: String, email: String, profile: AccessibilityProfile?): User =
        withContext(Dispatchers.IO) {
            val existing = localDS.getUser()?.toDomain()
            val user = if (existing != null && existing.email.equals(email, ignoreCase = true)) {
                existing.copy(name = name, profile = profile ?: existing.profile)
            } else {
                User(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    email = email,
                    profile = profile,
                )
            }
            localDS.clearUser()
            localDS.saveUser(user.toEntity())
            user
        }

    override suspend fun updateProfile(
        profile: AccessibilityProfile?,
        preferredFilters: Set<AccessibilityFilter>
    ) = withContext(Dispatchers.IO) {
        localDS.updateProfile(profile?.name, preferredFilters.map { it.name })
    }

    override suspend fun updateName(name: String) = withContext(Dispatchers.IO) {
        localDS.updateName(name)
    }

    override suspend fun logout() = withContext(Dispatchers.IO) {
        localDS.clearUser()
    }

    override fun observeUser(): Flow<User?> = localDS.observeUser().map { it?.toDomain() }
}

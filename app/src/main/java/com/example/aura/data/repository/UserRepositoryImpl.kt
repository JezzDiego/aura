package com.example.aura.data.repository

import com.example.aura.data.local.datasource.UserLocalDataSource
import com.example.aura.data.local.entity.UserEntity
import com.example.aura.data.mapper.toDomain
import com.example.aura.data.remote.datasource.UserRemoteDataSource
import com.example.aura.domain.model.User
import com.example.aura.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val localDS: UserLocalDataSource,
    private val remoteDS: UserRemoteDataSource
) : UserRepository {

    private var cachedUser: User? = null

    override suspend fun loginUser(email: String, password: String): User? {
        return withContext(Dispatchers.IO){
            val users = remoteDS.getUsers()
            val user = users.map {it.toDomain()}
                .find{it.email == email && it.password == password}

            user?.let{
                val entity = UserEntity(
                    id = it.id,
                    name = it.name,
                    email = it.email,
                    birthDate = it.birthDate,
                    gender = it.gender,
                    healthInsurance = it.healthInsurance,
                    profileImageUrl = it.profileImageUrl
                )
                localDS.saveUser(entity)
            }

            user
        }
    }

    override suspend fun getAllUsers(): List<User> = withContext(Dispatchers.IO) {
        remoteDS.getUsers().map { it.toDomain() }
    }

    override suspend fun logout() {
        withContext(Dispatchers.IO) {
            localDS.clearUser()
            cachedUser = null
        }
    }

    override fun observeUser(): Flow<User?> =
        localDS.getUser().map { it?.toDomain() }

}

package com.example.aura.data.repository

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aura.data.local.dao.UserDao
import com.example.aura.data.local.datasource.UserLocalDataSource
import com.example.aura.data.remote.api.UserApi
import com.example.aura.data.mapper.toDomain
import com.example.aura.data.mapper.toDto
import com.example.aura.domain.model.User
import com.example.aura.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val api: UserApi,
    private val localDataSource: UserLocalDataSource
) : UserRepository {

    private var cachedUser: User? = null

    override suspend fun loginUser(email: String, password: String): User? {
        return withContext(Dispatchers.IO){
            val users = api.getUsers()
            val user = users.map {it.toDomain()}
                .find{it.email == email && it.password == password}

            user?.let{
                val entity = com.example.aura.data.local.entity.UserEntity(
                    id = it.id,
                    name = it.name,
                    email = it.email,
                    birthDate = it.birthDate,
                    gender = it.gender,
                    healthInsurance = it.healthInsurance
                )
                localDataSource.saveUser(entity)
            }

            user
        }
    }

    override suspend fun logout() {
        withContext(Dispatchers.IO) {
            localDataSource.clearUser()
            cachedUser = null
        }
    }



}

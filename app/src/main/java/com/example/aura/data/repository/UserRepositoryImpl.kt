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

    override suspend fun loginUser(email: String, password: String): User? {
        return withContext(Dispatchers.IO){
            val users = api.getUsers()
            val user = users.find { it.email == email && it.password == password }
            user?.toDomain()
        }
    }


}

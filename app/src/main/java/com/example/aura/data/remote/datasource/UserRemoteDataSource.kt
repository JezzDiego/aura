package com.example.aura.data.remote.datasource

import com.example.aura.data.remote.api.UserApi
import com.example.aura.data.remote.dto.UserDTO

class UserRemoteDataSource(private val api: UserApi) {

    suspend fun loginUser(email: String, password: String): UserDTO? {
        val users = api.getUsers()
        return TODO()
    }
}

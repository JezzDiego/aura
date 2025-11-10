package com.example.aura.data.remote.datasource

import com.example.aura.data.remote.api.UserApi
import com.example.aura.data.remote.dto.UserDTO

class UserRemoteDataSource(private val api: UserApi) {

    suspend fun getUsers(): List<UserDTO> {
        return api.getUsers()
    }
}

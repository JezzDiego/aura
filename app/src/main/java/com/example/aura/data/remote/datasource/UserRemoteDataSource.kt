package com.example.aura.data.remote.datasource

import com.example.aura.data.remote.api.UserApi
import com.example.aura.data.remote.dto.UserDTO

class UserRemoteDataSource(private val api: UserApi) {

    suspend fun getUserProfile(): UserDTO = api.getUserProfile()

    suspend fun updateUser(user: UserDTO): UserDTO = api.updateUser(user)

    suspend fun deleteAccount(id: String) = api.deleteAccount(id)
}

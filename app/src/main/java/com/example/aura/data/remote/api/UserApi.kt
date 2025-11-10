package com.example.aura.data.remote.api

import com.example.aura.data.remote.dto.UserDTO
import retrofit2.http.*

interface UserApi {
    @GET("users")
    suspend fun getUsers(): List<UserDTO>
}

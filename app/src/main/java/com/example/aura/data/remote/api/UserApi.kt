package com.example.aura.data.remote.api

import com.example.aura.data.remote.dto.UserDTO
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserApi {

    @POST("users")
    suspend fun register(@Body user: UserDTO): UserDTO

    @PUT("users/me")
    suspend fun update(@Body user: UserDTO): UserDTO
}

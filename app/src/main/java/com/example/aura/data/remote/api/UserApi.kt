package com.example.aura.data.remote.api

import com.example.aura.data.remote.dto.UserDTO
import retrofit2.http.*

interface UserApi {

    @GET("user/profile")
    suspend fun getUserProfile(): UserDTO

    @PUT("user/profile")
    suspend fun updateUser(@Body user: UserDTO): UserDTO

    @DELETE("user/{id}")
    suspend fun deleteAccount(@Path("id") id: String)
}

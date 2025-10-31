package com.example.aura.data.remote.api

import com.example.aura.data.remote.dto.LaboratoryDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LaboratoryApi {

    @GET("labs")
    suspend fun getAll(): List<LaboratoryDTO>

    @GET("labs/{id}")
    suspend fun getById(@Path("id") id: String): LaboratoryDTO?

    @GET("labs/search")
    suspend fun searchLabs(@Query("q") query: String): List<LaboratoryDTO>
}

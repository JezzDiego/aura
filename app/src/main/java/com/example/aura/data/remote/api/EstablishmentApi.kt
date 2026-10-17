package com.example.aura.data.remote.api

import com.example.aura.data.remote.dto.EstablishmentDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EstablishmentApi {

    @GET("establishments")
    suspend fun getEstablishments(): List<EstablishmentDTO>

    @GET("establishments/{id}")
    suspend fun getEstablishmentById(@Path("id") id: String): EstablishmentDTO

    @GET("establishments/search")
    suspend fun search(
        @Query("q") query: String,
        @Query("filters") filters: List<String>,
    ): List<EstablishmentDTO>
}

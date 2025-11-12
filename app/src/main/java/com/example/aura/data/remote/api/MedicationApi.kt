package com.example.aura.data.remote.api

import com.example.aura.data.remote.dto.MedicationDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface MedicationApi {

    @GET("api/v1/search")
    suspend fun searchMedications(@Query("q") query: String): List<MedicationDTO>
}
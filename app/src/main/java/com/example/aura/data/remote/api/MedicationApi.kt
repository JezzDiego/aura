package com.example.aura.data.remote.api

import com.example.aura.data.remote.dto.MedicationDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface MedicationApi {

    @GET("Medication")
    suspend fun getAllMedications(): List<MedicationDTO>

    @GET("Medication")
    suspend fun searchMedications(
        @Query("name") name: String
    ): List<MedicationDTO>
}
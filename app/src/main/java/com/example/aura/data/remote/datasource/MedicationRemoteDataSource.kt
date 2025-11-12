package com.example.aura.data.remote.datasource

import com.example.aura.data.remote.api.MedicationApi
import com.example.aura.data.remote.dto.MedicationDTO

class MedicationRemoteDataSource(
    private val medicationApi: MedicationApi
) {
    suspend fun search(query: String): List<MedicationDTO> =
        medicationApi.searchMedications(query)
}
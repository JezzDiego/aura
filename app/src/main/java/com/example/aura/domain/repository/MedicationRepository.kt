package com.example.aura.domain.repository

import com.example.aura.core.ResultWrapper
import com.example.aura.domain.model.Medication
import kotlinx.coroutines.flow.Flow

interface MedicationRepository {

    fun getSavedMedications(): Flow<List<Medication>>

    suspend fun searchMedication(query: String): ResultWrapper<List<Medication>>

    suspend fun addMedication(medication: Medication)

    suspend fun deleteMedication(medication: Medication)
}
package com.example.aura.data.repository

import com.example.aura.core.ResultWrapper
import com.example.aura.data.local.datasource.MedicationLocalDataSource
import com.example.aura.data.mapper.toDomain
import com.example.aura.data.mapper.toEntity
import com.example.aura.data.remote.datasource.MedicationRemoteDataSource
import com.example.aura.domain.model.Medication
import com.example.aura.domain.repository.MedicationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MedicationRepositoryImpl(
    private val localDataSource: MedicationLocalDataSource,
    private val remoteDataSource: MedicationRemoteDataSource
) : MedicationRepository {

    override fun getSavedMedications(): Flow<List<Medication>> {
        return localDataSource.getAllMedications().map { entityList ->
            entityList.map { entity ->
                entity.toDomain()
            }
        }
    }

    override suspend fun searchMedication(query: String): ResultWrapper<List<Medication>> {
        return try {
            val dtoList = remoteDataSource.search(query)
            val domainList = dtoList.map { dto ->
                dto.toDomain()
            }
            ResultWrapper.Success(domainList)
        } catch (e: Exception) {
            ResultWrapper.Error(e)
        }
    }


    override suspend fun addMedication(medication: Medication) {
        localDataSource.save(medication.toEntity())
    }

    override suspend fun deleteMedication(medication: Medication) {
        localDataSource.delete(medication.toEntity())
    }
}
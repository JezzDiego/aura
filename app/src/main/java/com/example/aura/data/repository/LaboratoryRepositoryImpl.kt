package com.example.aura.data.repository

import com.example.aura.data.local.datasource.LaboratoryLocalDataSource
import com.example.aura.data.mapper.toDomain
import com.example.aura.data.remote.datasource.LaboratoryRemoteDataSource
import com.example.aura.domain.model.Laboratory
import com.example.aura.domain.repository.LaboratoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LaboratoryRepositoryImpl(
    private val localDS: LaboratoryLocalDataSource,
    private val remoteDS: LaboratoryRemoteDataSource
) : LaboratoryRepository {

    override suspend fun getAllLaboratories(): List<Laboratory> = withContext(Dispatchers.IO) {
        remoteDS.getAll().map { it.toDomain() }
    }

    override suspend fun getLaboratoryById(id: String): Laboratory? = withContext(Dispatchers.IO) {
        remoteDS.getById(id)?.toDomain()
    }

    override suspend fun searchLaboratories(query: String): List<Laboratory> = withContext(Dispatchers.IO) {
        remoteDS.search(query).map { it.toDomain() }
    }
}

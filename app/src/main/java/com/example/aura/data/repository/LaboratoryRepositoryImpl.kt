package com.example.aura.data.repository

import com.example.aura.data.remote.api.LaboratoryApi
import com.example.aura.data.mapper.toDomain
import com.example.aura.domain.model.Laboratory
import com.example.aura.domain.repository.LaboratoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LaboratoryRepositoryImpl(
    private val api: LaboratoryApi
) : LaboratoryRepository {

    override suspend fun getAllLaboratories(): List<Laboratory> = withContext(Dispatchers.IO) {
        api.getAll().map { it.toDomain() }
    }

    override suspend fun getLaboratoryById(id: String): Laboratory? = withContext(Dispatchers.IO) {
        api.getById(id)?.toDomain()
    }

    override suspend fun searchLaboratories(query: String): List<Laboratory> = withContext(Dispatchers.IO) {
        api.searchLabs(query).map { it.toDomain() }
    }
}

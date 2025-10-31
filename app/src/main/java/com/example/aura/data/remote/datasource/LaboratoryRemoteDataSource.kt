package com.example.aura.data.remote.datasource

import com.example.aura.data.remote.api.LaboratoryApi
import com.example.aura.data.remote.dto.LaboratoryDTO

class LaboratoryRemoteDataSource(private val api: LaboratoryApi) {

    suspend fun getAll(): List<LaboratoryDTO> = api.getAll()

    suspend fun getById(id: String): LaboratoryDTO? = api.getById(id)

    suspend fun search(query: String): List<LaboratoryDTO> = api.searchLabs(query)
}

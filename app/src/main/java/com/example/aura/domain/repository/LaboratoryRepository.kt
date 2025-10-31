package com.example.aura.domain.repository

import com.example.aura.domain.model.Laboratory

interface LaboratoryRepository {

    suspend fun getAllLaboratories(): List<Laboratory>

    suspend fun getLaboratoryById(id: String): Laboratory?

    suspend fun searchLaboratories(query: String): List<Laboratory>
}

package com.example.aura.domain.usecase.laboratory

import com.example.aura.domain.model.Laboratory
import com.example.aura.domain.repository.LaboratoryRepository

class GetLaboratoriesUseCase(private val repository: LaboratoryRepository) {
    suspend operator fun invoke(): List<Laboratory> = repository.getAllLaboratories()
}

class GetLaboratoryByIdUseCase(private val repository: LaboratoryRepository) {
    suspend operator fun invoke(id: String): Laboratory? = repository.getLaboratoryById(id)
}

class SearchLaboratoriesUseCase(private val repository: LaboratoryRepository) {
    suspend operator fun invoke(query: String): List<Laboratory> = repository.searchLaboratories(query)
}

data class LaboratoryUseCases(
    val getLaboratories: GetLaboratoriesUseCase,
    val getById: GetLaboratoryByIdUseCase,
    val searchLaboratories: SearchLaboratoriesUseCase
)

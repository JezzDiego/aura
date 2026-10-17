package com.example.aura.domain.usecase.establishment

import com.example.aura.domain.model.AccessibilityFilter
import com.example.aura.domain.model.Establishment
import com.example.aura.domain.model.ShareLink
import com.example.aura.domain.repository.EstablishmentRepository

class GetEstablishmentsUseCase(private val repository: EstablishmentRepository) {
    suspend operator fun invoke(): List<Establishment> =
        repository.getEstablishments().sortedBy { it.distanceMeters }
}

class GetEstablishmentByIdUseCase(private val repository: EstablishmentRepository) {
    suspend operator fun invoke(id: String): Establishment? = repository.getEstablishmentById(id)
}

class SearchEstablishmentsUseCase(private val repository: EstablishmentRepository) {
    suspend operator fun invoke(
        query: String,
        filters: Set<AccessibilityFilter> = emptySet()
    ): List<Establishment> = repository.searchEstablishments(query.trim(), filters)
}

class GetFavoriteEstablishmentsUseCase(private val repository: EstablishmentRepository) {
    suspend operator fun invoke(): List<Establishment> = repository.getFavorites()
}

class ToggleFavoriteUseCase(private val repository: EstablishmentRepository) {
    suspend operator fun invoke(id: String): Boolean = repository.toggleFavorite(id)
}

class ShareEstablishmentUseCase(private val repository: EstablishmentRepository) {
    suspend operator fun invoke(id: String): ShareLink = repository.shareEstablishment(id)
}

data class EstablishmentUseCases(
    val getEstablishments: GetEstablishmentsUseCase,
    val getEstablishmentById: GetEstablishmentByIdUseCase,
    val searchEstablishments: SearchEstablishmentsUseCase,
    val getFavorites: GetFavoriteEstablishmentsUseCase,
    val toggleFavorite: ToggleFavoriteUseCase,
    val shareEstablishment: ShareEstablishmentUseCase,
)

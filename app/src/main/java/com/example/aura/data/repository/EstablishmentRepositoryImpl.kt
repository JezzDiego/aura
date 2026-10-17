package com.example.aura.data.repository

import com.example.aura.data.local.datasource.EstablishmentLocalDataSource
import com.example.aura.data.mapper.toDomain
import com.example.aura.data.remote.datasource.EstablishmentRemoteDataSource
import com.example.aura.domain.model.AccessibilityFilter
import com.example.aura.domain.model.Establishment
import com.example.aura.domain.model.ShareLink
import com.example.aura.domain.repository.EstablishmentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import java.util.UUID

/**
 * Implementação offline-first. Por enquanto lê e grava apenas no [localDS];
 * o [remoteDS] fica injetado para a futura sincronização com a API.
 */
class EstablishmentRepositoryImpl(
    private val localDS: EstablishmentLocalDataSource,
    @Suppress("unused") private val remoteDS: EstablishmentRemoteDataSource,
) : EstablishmentRepository {

    override suspend fun getEstablishments(): List<Establishment> = withContext(Dispatchers.IO) {
        localDS.getAll().map { it.toDomain() }
    }

    override suspend fun getEstablishmentById(id: String): Establishment? = withContext(Dispatchers.IO) {
        localDS.getById(id)?.toDomain()
    }

    override suspend fun searchEstablishments(
        query: String,
        filters: Set<AccessibilityFilter>
    ): List<Establishment> = withContext(Dispatchers.IO) {
        localDS.search(query)
            .map { it.toDomain() }
            .filter { it.matches(filters) }
    }

    override suspend fun getFavorites(): List<Establishment> = withContext(Dispatchers.IO) {
        localDS.getFavorites().map { it.toDomain() }
    }

    override suspend fun toggleFavorite(id: String): Boolean = withContext(Dispatchers.IO) {
        val current = localDS.getById(id)?.establishment ?: return@withContext false
        val newValue = !current.isFavorite
        localDS.setFavorite(id, newValue)
        newValue
    }

    override suspend fun shareEstablishment(id: String): ShareLink = withContext(Dispatchers.IO) {
        val establishment = localDS.getById(id)?.toDomain()
            ?: throw IllegalArgumentException("Estabelecimento não encontrado")
        val token = UUID.randomUUID().toString().take(8)
        val link = "https://entradalivre.app/local/${establishment.id}?ref=$token"
        val highlights = establishment.highlights.joinToString { it.shortLabel }
        ShareLink(
            id = token,
            establishmentId = id,
            generatedAt = System.currentTimeMillis(),
            linkUrl = link,
            message = buildString {
                append("${establishment.name} — ${establishment.conformityPercent}% de conformidade em acessibilidade")
                append(" (${establishment.conformityLevel.displayName}).\n")
                append("${establishment.address}, ${establishment.city}\n")
                if (highlights.isNotBlank()) append("Destaques: $highlights\n")
                append("Veja no Entrada Livre: $link")
            }
        )
    }

    override suspend fun registerReviewRating(id: String, reviewRating: Double) = withContext(Dispatchers.IO) {
        val current = localDS.getById(id)?.establishment ?: return@withContext
        val newCount = current.reviewCount + 1
        val newRating = ((current.rating * current.reviewCount) + reviewRating) / newCount
        val rounded = String.format(Locale.US, "%.1f", newRating).toDouble()
        localDS.updateRating(id, rounded, newCount)
    }
}

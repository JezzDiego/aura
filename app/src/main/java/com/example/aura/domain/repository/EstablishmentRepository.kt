package com.example.aura.domain.repository

import com.example.aura.domain.model.AccessibilityFilter
import com.example.aura.domain.model.Establishment
import com.example.aura.domain.model.ShareLink

interface EstablishmentRepository {

    suspend fun getEstablishments(): List<Establishment>

    suspend fun getEstablishmentById(id: String): Establishment?

    suspend fun searchEstablishments(query: String, filters: Set<AccessibilityFilter>): List<Establishment>

    suspend fun getFavorites(): List<Establishment>

    /** Alterna o favorito e retorna o novo estado. */
    suspend fun toggleFavorite(id: String): Boolean

    suspend fun shareEstablishment(id: String): ShareLink

    /** Incorpora a nota de um novo relato à média do estabelecimento. */
    suspend fun registerReviewRating(id: String, reviewRating: Double)
}

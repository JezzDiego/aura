package com.example.aura.data.local.datasource

import com.example.aura.data.local.dao.EstablishmentDao
import com.example.aura.data.local.entity.EstablishmentWithDetails
import com.example.aura.data.local.seed.LocalSeedData
import com.example.aura.data.mapper.toEntity
import com.example.aura.data.mapper.toFeatureEntities
import com.example.aura.data.mapper.toPhotoEntities
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Fonte de dados offline dos estabelecimentos.
 * Na primeira leitura popula o banco com [LocalSeedData].
 */
class EstablishmentLocalDataSource(private val dao: EstablishmentDao) {

    private val seedMutex = Mutex()

    private suspend fun ensureSeeded() = seedMutex.withLock {
        if (dao.count() > 0) return@withLock
        val seed = LocalSeedData.establishments
        dao.insertWithDetails(
            establishments = seed.map { it.toEntity() },
            features = seed.flatMap { it.toFeatureEntities() },
            photos = seed.flatMap { it.toPhotoEntities() },
        )
    }

    suspend fun getAll(): List<EstablishmentWithDetails> {
        ensureSeeded()
        return dao.getAll()
    }

    suspend fun getById(id: String): EstablishmentWithDetails? {
        ensureSeeded()
        return dao.getById(id)
    }

    suspend fun search(query: String): List<EstablishmentWithDetails> {
        ensureSeeded()
        return if (query.isBlank()) dao.getAll() else dao.search(query)
    }

    suspend fun getFavorites(): List<EstablishmentWithDetails> {
        ensureSeeded()
        return dao.getFavorites()
    }

    suspend fun setFavorite(id: String, favorite: Boolean) = dao.setFavorite(id, favorite)

    suspend fun updateRating(id: String, rating: Double, reviewCount: Int) =
        dao.updateRating(id, rating, reviewCount)
}

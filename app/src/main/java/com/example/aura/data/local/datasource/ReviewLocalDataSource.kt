package com.example.aura.data.local.datasource

import com.example.aura.data.local.dao.ReviewDao
import com.example.aura.data.local.entity.ReviewEntity
import com.example.aura.data.local.seed.LocalSeedData
import com.example.aura.data.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Fonte de dados offline dos relatos da comunidade.
 * Na primeira leitura popula o banco com os relatos de exemplo de [LocalSeedData].
 */
class ReviewLocalDataSource(private val dao: ReviewDao) {

    private val seedMutex = Mutex()

    private suspend fun ensureSeeded() = seedMutex.withLock {
        if (dao.count() > 0) return@withLock
        dao.insertAll(LocalSeedData.reviews().map { it.toEntity() })
    }

    fun observeRecent(limit: Int): Flow<List<ReviewEntity>> =
        dao.observeRecent(limit).onStart { ensureSeeded() }

    suspend fun getByEstablishment(establishmentId: String): List<ReviewEntity> {
        ensureSeeded()
        return dao.getByEstablishment(establishmentId)
    }

    suspend fun getByAuthor(authorId: String): List<ReviewEntity> = dao.getByAuthor(authorId)

    suspend fun getById(id: String): ReviewEntity? = dao.getById(id)

    suspend fun save(review: ReviewEntity) = dao.insert(review)

    suspend fun updateHelpful(id: String, count: Int, marked: Boolean) = dao.updateHelpful(id, count, marked)
}

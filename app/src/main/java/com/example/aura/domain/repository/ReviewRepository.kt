package com.example.aura.domain.repository

import com.example.aura.domain.model.Review
import kotlinx.coroutines.flow.Flow

interface ReviewRepository {

    fun observeRecentReviews(limit: Int): Flow<List<Review>>

    suspend fun getReviewsByEstablishment(establishmentId: String): List<Review>

    suspend fun getReviewsByAuthor(authorId: String): List<Review>

    suspend fun addReview(review: Review)

    suspend fun toggleHelpful(reviewId: String)

    /** Copia uma foto escolhida pelo usuário para o armazenamento do app e retorna o novo caminho. */
    suspend fun savePhoto(sourceUri: String): String

    suspend fun deletePhoto(path: String)
}

package com.example.aura.domain.usecase.review

import com.example.aura.domain.model.FeatureStatus
import com.example.aura.domain.model.Review
import com.example.aura.domain.model.ReviewChecklistItem
import com.example.aura.domain.repository.EstablishmentRepository
import com.example.aura.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow

class GetRecentReviewsUseCase(private val repository: ReviewRepository) {
    operator fun invoke(limit: Int = 10): Flow<List<Review>> = repository.observeRecentReviews(limit)
}

class GetEstablishmentReviewsUseCase(private val repository: ReviewRepository) {
    suspend operator fun invoke(establishmentId: String): List<Review> =
        repository.getReviewsByEstablishment(establishmentId)
}

class GetUserReviewsUseCase(private val repository: ReviewRepository) {
    suspend operator fun invoke(authorId: String): List<Review> = repository.getReviewsByAuthor(authorId)
}

/**
 * Valida e publica um relato. Lança [IllegalArgumentException] com mensagem amigável
 * quando algum campo obrigatório não foi preenchido.
 */
class SubmitReviewUseCase(
    private val reviewRepository: ReviewRepository,
    private val establishmentRepository: EstablishmentRepository,
) {
    suspend operator fun invoke(review: Review) {
        require(review.establishmentId.isNotBlank()) { "Selecione o estabelecimento avaliado." }
        val missing = ReviewChecklistItem.entries.filter { it !in review.checklist.keys }
        require(missing.isEmpty()) {
            "Responda o checklist: ${missing.joinToString { it.displayName }}."
        }
        require(review.comment.isNotBlank()) { "Descreva seu relato e observações." }
        require(review.comment.length <= Review.MAX_COMMENT_LENGTH) {
            "O relato deve ter no máximo ${Review.MAX_COMMENT_LENGTH} caracteres."
        }
        require(review.photos.size <= Review.MAX_PHOTOS) { "Envie no máximo ${Review.MAX_PHOTOS} fotos." }

        reviewRepository.addReview(review)
        val rating = review.checklist.toRating()
        if (rating > 0) establishmentRepository.registerReviewRating(review.establishmentId, rating)
    }
}

class ToggleReviewHelpfulUseCase(private val repository: ReviewRepository) {
    suspend operator fun invoke(reviewId: String) = repository.toggleHelpful(reviewId)
}

class SaveReviewPhotoUseCase(private val repository: ReviewRepository) {
    suspend operator fun invoke(sourceUri: String): String = repository.savePhoto(sourceUri)
}

class DeleteReviewPhotoUseCase(private val repository: ReviewRepository) {
    suspend operator fun invoke(path: String) = repository.deletePhoto(path)
}

/** Converte o checklist do relato em uma nota de 1 a 5. */
fun Map<ReviewChecklistItem, FeatureStatus>.toRating(): Double {
    val answered = values.filter { it != FeatureStatus.UNKNOWN }
    if (answered.isEmpty()) return 0.0
    val score = answered.sumOf {
        when (it) {
            FeatureStatus.AVAILABLE -> 5.0
            FeatureStatus.PARTIAL -> 3.0
            FeatureStatus.UNAVAILABLE -> 1.0
            FeatureStatus.UNKNOWN -> 0.0
        }
    }
    return score / answered.size
}

data class ReviewUseCases(
    val getRecentReviews: GetRecentReviewsUseCase,
    val getEstablishmentReviews: GetEstablishmentReviewsUseCase,
    val getUserReviews: GetUserReviewsUseCase,
    val submitReview: SubmitReviewUseCase,
    val toggleHelpful: ToggleReviewHelpfulUseCase,
    val savePhoto: SaveReviewPhotoUseCase,
    val deletePhoto: DeleteReviewPhotoUseCase,
)

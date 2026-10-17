package com.example.aura

import com.example.aura.data.local.seed.LocalSeedData
import com.example.aura.domain.model.AccessibilityFilter
import com.example.aura.domain.model.ConformityLevel
import com.example.aura.domain.model.Establishment
import com.example.aura.domain.model.FeatureStatus
import com.example.aura.domain.model.Review
import com.example.aura.domain.model.ReviewChecklistItem
import com.example.aura.domain.model.ShareLink
import com.example.aura.domain.repository.EstablishmentRepository
import com.example.aura.domain.repository.ReviewRepository
import com.example.aura.domain.usecase.review.SubmitReviewUseCase
import com.example.aura.domain.usecase.review.toRating
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class DomainUnitTest {

    private val saborArte = LocalSeedData.establishments.first { it.id == "sabor-arte" }

    @Test
    fun establishment_matchesOnlyAvailableFilters() {
        assertTrue(saborArte.matches(setOf(AccessibilityFilter.STEP_FREE_ENTRANCE, AccessibilityFilter.ADAPTED_RESTROOM)))
        // Cardápio em Braille está "Não disponível" no Sabor & Arte
        assertFalse(saborArte.matches(setOf(AccessibilityFilter.BRAILLE_MATERIALS)))
        assertTrue(saborArte.matches(emptySet()))
    }

    @Test
    fun establishment_highlightsAreLimitedToThree() {
        LocalSeedData.establishments.forEach { assertTrue(it.highlights.size <= 3) }
        assertEquals(
            listOf("Entrada sem degrau", "Banheiro adaptado", "Atendimento em Libras"),
            saborArte.highlights.map { it.shortLabel }
        )
    }

    @Test
    fun conformityLevel_fromPercent() {
        assertEquals(ConformityLevel.FULL, ConformityLevel.fromPercent(94))
        assertEquals(ConformityLevel.PARTIAL, ConformityLevel.fromPercent(78))
        assertEquals(ConformityLevel.LOW, ConformityLevel.fromPercent(40))
    }

    @Test
    fun checklist_toRatingIgnoresUnknown() {
        val rating = mapOf(
            ReviewChecklistItem.ENTRANCE_RAMP to FeatureStatus.AVAILABLE,
            ReviewChecklistItem.ACCESSIBLE_RESTROOM to FeatureStatus.PARTIAL,
            ReviewChecklistItem.LIBRAS_SERVICE to FeatureStatus.UNKNOWN,
            ReviewChecklistItem.INTERNAL_CIRCULATION to FeatureStatus.UNAVAILABLE,
        ).toRating()
        assertEquals(3.0, rating, 0.001)
    }

    @Test
    fun submitReview_requiresCompleteChecklistAndComment() = runBlocking {
        val reviews = FakeReviewRepository()
        val establishments = FakeEstablishmentRepository()
        val useCase = SubmitReviewUseCase(reviews, establishments)

        val incomplete = baseReview().copy(checklist = mapOf(ReviewChecklistItem.ENTRANCE_RAMP to FeatureStatus.AVAILABLE))
        try {
            useCase(incomplete)
            fail("Deveria exigir o checklist completo")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("Banheiro Acessível"))
        }

        try {
            useCase(baseReview().copy(comment = "  "))
            fail("Deveria exigir o relato")
        } catch (_: IllegalArgumentException) {
        }

        useCase(baseReview())
        assertEquals(1, reviews.saved.size)
        assertEquals(5.0, establishments.lastRating!!, 0.001)
    }

    private fun baseReview() = Review(
        id = "r1",
        establishmentId = "sabor-arte",
        establishmentName = "Restaurante Sabor & Arte",
        authorId = "u1",
        authorName = "Jessé Oliveira",
        authorProfile = null,
        comment = "Tudo acessível!",
        checklist = ReviewChecklistItem.entries.associateWith { FeatureStatus.AVAILABLE },
    )

    private class FakeReviewRepository : ReviewRepository {
        val saved = mutableListOf<Review>()
        override fun observeRecentReviews(limit: Int): Flow<List<Review>> = flowOf(saved)
        override suspend fun getReviewsByEstablishment(establishmentId: String) = saved.filter { it.establishmentId == establishmentId }
        override suspend fun getReviewsByAuthor(authorId: String) = saved.filter { it.authorId == authorId }
        override suspend fun addReview(review: Review) { saved += review }
        override suspend fun toggleHelpful(reviewId: String) = Unit
        override suspend fun savePhoto(sourceUri: String) = sourceUri
        override suspend fun deletePhoto(path: String) = Unit
    }

    private class FakeEstablishmentRepository : EstablishmentRepository {
        var lastRating: Double? = null
        override suspend fun getEstablishments(): List<Establishment> = LocalSeedData.establishments
        override suspend fun getEstablishmentById(id: String) = LocalSeedData.establishments.firstOrNull { it.id == id }
        override suspend fun searchEstablishments(query: String, filters: Set<AccessibilityFilter>) =
            LocalSeedData.establishments.filter { it.matches(filters) }
        override suspend fun getFavorites(): List<Establishment> = emptyList()
        override suspend fun toggleFavorite(id: String) = true
        override suspend fun shareEstablishment(id: String) = ShareLink("t", id, 0L, "", "")
        override suspend fun registerReviewRating(id: String, reviewRating: Double) { lastRating = reviewRating }
    }
}

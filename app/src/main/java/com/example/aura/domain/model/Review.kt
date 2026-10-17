package com.example.aura.domain.model

/**
 * Itens obrigatórios do checklist de avaliação feita pela comunidade.
 */
enum class ReviewChecklistItem(val displayName: String) {
    ENTRANCE_RAMP("Entrada e Rampa"),
    ACCESSIBLE_RESTROOM("Banheiro Acessível"),
    LIBRAS_SERVICE("Atendimento em Libras"),
    INTERNAL_CIRCULATION("Espaço interno para circulação"),
}

/**
 * Relato / avaliação de acessibilidade publicado por um usuário.
 */
data class Review(
    val id: String,
    val establishmentId: String,
    val establishmentName: String,
    val authorId: String,
    val authorName: String,
    val authorProfile: AccessibilityProfile?,
    val comment: String,
    val checklist: Map<ReviewChecklistItem, FeatureStatus>,
    val photos: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val helpfulCount: Int = 0,
    val markedHelpful: Boolean = false,
) {
    val authorInitials: String
        get() = authorName.trim().split(" ")
            .filter { it.isNotBlank() }
            .let { parts ->
                when {
                    parts.isEmpty() -> "?"
                    parts.size == 1 -> parts.first().take(2)
                    else -> "${parts.first().first()}${parts.last().first()}"
                }
            }.uppercase()

    companion object {
        const val MAX_COMMENT_LENGTH = 800
        const val MAX_PHOTOS = 3
    }
}

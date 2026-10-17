package com.example.aura.domain.model

/**
 * Estabelecimento mapeado pela comunidade (restaurante, café, cinema, etc.).
 */
data class Establishment(
    val id: String,
    val name: String,
    /** Rótulo curto exibido nos cards, ex: "Gastronomia". */
    val category: String,
    /** Rótulo completo exibido nos detalhes, ex: "Restaurante & Culinária Brasileira". */
    val categoryDetail: String,
    val address: String,
    val neighborhood: String,
    val city: String,
    /** Imagem usada nos cards de listagem. */
    val imageUrl: String?,
    /** Imagem de capa da tela de detalhes (usa [imageUrl] quando nula). */
    val heroImageUrl: String?,
    val rating: Double,
    val reviewCount: Int,
    val distanceMeters: Int,
    /** Rótulo do último mês verificado, ex: "Março/24". */
    val verifiedAt: String,
    val auditorsCount: Int,
    val conformityPercent: Int,
    val summary: String,
    val nearbyTransit: String?,
    val surroundings: String?,
    val latitude: Double,
    val longitude: Double,
    val features: List<AccessibilityFeature> = emptyList(),
    val photos: List<EstablishmentPhoto> = emptyList(),
    val isFavorite: Boolean = false,
) {
    val conformityLevel: ConformityLevel get() = ConformityLevel.fromPercent(conformityPercent)

    val availableFilters: Set<AccessibilityFilter>
        get() = features
            .filter { it.status == FeatureStatus.AVAILABLE }
            .mapNotNull { it.filter }
            .toSet()

    /** Recursos em destaque exibidos nos cards (máx. 3). */
    val highlights: List<AccessibilityFeature>
        get() = features.filter { it.status == FeatureStatus.AVAILABLE && it.highlight }.take(3)

    fun matches(filters: Set<AccessibilityFilter>): Boolean = availableFilters.containsAll(filters)

    fun featureFor(filter: AccessibilityFilter): AccessibilityFeature? =
        features.firstOrNull { it.filter == filter && it.status == FeatureStatus.AVAILABLE }
}

/**
 * Item do checklist de acessibilidade de um estabelecimento.
 */
data class AccessibilityFeature(
    val id: String,
    val establishmentId: String,
    val category: FeatureCategory,
    val title: String,
    /** Versão curta do título usada em chips, ex: "Entrada sem degrau". */
    val shortLabel: String,
    val description: String,
    val status: FeatureStatus,
    /** Texto alternativo para o selo de status, ex: "Mínimo 1,20m". */
    val badge: String? = null,
    val filter: AccessibilityFilter? = null,
    val highlight: Boolean = false,
)

/**
 * Foto de verificação visual enviada por auditores/comunidade.
 */
data class EstablishmentPhoto(
    val id: String,
    val establishmentId: String,
    val imageUrl: String,
    val title: String,
    val description: String,
)

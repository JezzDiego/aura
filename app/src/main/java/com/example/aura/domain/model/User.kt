package com.example.aura.domain.model

/**
 * Perfis / recursos principais utilizados pela pessoa usuária.
 * Cada perfil sugere um conjunto de filtros prioritários.
 */
enum class AccessibilityProfile(
    val displayName: String,
    val shortName: String,
    val suggestedFilters: Set<AccessibilityFilter>,
) {
    WHEELCHAIR(
        "Usuário de Cadeira de Rodas",
        "Cadeira",
        setOf(AccessibilityFilter.STEP_FREE_ENTRANCE, AccessibilityFilter.ADAPTED_RESTROOM, AccessibilityFilter.WIDE_CORRIDORS)
    ),
    REDUCED_MOBILITY(
        "Pessoa com Mobilidade Reduzida",
        "Mobilidade",
        setOf(AccessibilityFilter.STEP_FREE_ENTRANCE, AccessibilityFilter.ADAPTED_RESTROOM, AccessibilityFilter.RESERVED_PARKING)
    ),
    VISUAL(
        "Pessoa com Deficiência Visual",
        "Visual",
        setOf(AccessibilityFilter.TACTILE_FLOOR, AccessibilityFilter.BRAILLE_MATERIALS, AccessibilityFilter.GUIDE_DOG_WELCOME)
    ),
    DEAF(
        "Pessoa Surda / Usuária de Libras",
        "Libras",
        setOf(AccessibilityFilter.LIBRAS_SERVICE, AccessibilityFilter.ACCESSIBLE_VIDEO, AccessibilityFilter.VISUAL_ALERT)
    ),
    NEURODIVERGENT(
        "Pessoa Neurodivergente",
        "Neurodivergente",
        setOf(AccessibilityFilter.VISUAL_ALERT, AccessibilityFilter.ACCESSIBLE_VIDEO)
    ),
    ELDERLY(
        "Pessoa Idosa",
        "Idoso",
        setOf(AccessibilityFilter.STEP_FREE_ENTRANCE, AccessibilityFilter.ADAPTED_RESTROOM, AccessibilityFilter.LOWERED_COUNTER)
    ),
    COMPANION(
        "Acompanhante / Familiar",
        "Acompanhante",
        setOf(AccessibilityFilter.STEP_FREE_ENTRANCE, AccessibilityFilter.ADAPTED_RESTROOM)
    ),
}

data class User(
    val id: String,
    val name: String,
    val email: String,
    val profile: AccessibilityProfile? = null,
    /** Filtros prioritários definidos pela pessoa (sobrescrevem as sugestões do perfil). */
    val preferredFilters: Set<AccessibilityFilter> = emptySet(),
    val createdAt: Long = System.currentTimeMillis(),
) {
    val initials: String
        get() = name.trim().split(" ").filter { it.isNotBlank() }.let { parts ->
            when {
                parts.isEmpty() -> "?"
                parts.size == 1 -> parts.first().take(2)
                else -> "${parts.first().first()}${parts.last().first()}"
            }
        }.uppercase()

    val firstName: String get() = name.trim().substringBefore(" ")

    /** Filtros efetivos para o botão "Filtrar pelo meu perfil". */
    val accessibilityFilters: Set<AccessibilityFilter>
        get() = preferredFilters.ifEmpty { profile?.suggestedFilters.orEmpty() }
}

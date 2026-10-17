package com.example.aura.domain.model

/**
 * Grupos de acessibilidade usados para organizar os filtros e o checklist.
 */
enum class AccessibilityGroup(val displayName: String, val description: String) {
    PHYSICAL(
        displayName = "Acessibilidade Física e Motora",
        description = "Parâmetros essenciais para cadeirantes e pessoas com mobilidade reduzida."
    ),
    HEARING(
        displayName = "Acessibilidade Auditiva e Comunicação",
        description = "Comunicação fluida em Libras e canais audiovisuais inclusivos."
    ),
    VISUAL(
        displayName = "Acessibilidade Visual",
        description = "Informações táteis, leitura facilitada e recepção para cão-guia."
    ),
}

/**
 * Critérios de acessibilidade que podem ser usados como filtro de busca.
 * [shortLabel] é o texto usado nos chips de "Filtros Rápidos".
 */
enum class AccessibilityFilter(
    val group: AccessibilityGroup,
    val displayName: String,
    val shortLabel: String,
) {
    STEP_FREE_ENTRANCE(AccessibilityGroup.PHYSICAL, "Entrada sem degrau ou com rampa acessível", "Rampa / Sem degraus"),
    ADAPTED_RESTROOM(AccessibilityGroup.PHYSICAL, "Banheiro adaptado (barras de apoio, giro 360°)", "Banheiro Acessível"),
    RESERVED_PARKING(AccessibilityGroup.PHYSICAL, "Vagas de estacionamento reservadas", "Estacionamento"),
    WIDE_CORRIDORS(AccessibilityGroup.PHYSICAL, "Corredores largos (>90cm)", "Espaço Amplo para Cadeira"),
    LOWERED_COUNTER(AccessibilityGroup.PHYSICAL, "Balcão de atendimento rebaixado", "Balcão rebaixado"),

    LIBRAS_SERVICE(AccessibilityGroup.HEARING, "Atendimento em Libras (Língua Brasileira de Sinais)", "Atendimento em Libras"),
    ACCESSIBLE_VIDEO(AccessibilityGroup.HEARING, "Cardápio / Materiais informativos em vídeo acessível", "Vídeo acessível"),
    VISUAL_ALERT(AccessibilityGroup.HEARING, "Alerta visual de emergência (luzes estroboscópicas)", "Alerta visual"),

    TACTILE_FLOOR(AccessibilityGroup.VISUAL, "Piso tátil direcional e de alerta", "Piso Tátil"),
    BRAILLE_MATERIALS(AccessibilityGroup.VISUAL, "Cardápio / Materiais em Braille ou fonte ampliada", "Braille / Áudio"),
    GUIDE_DOG_WELCOME(AccessibilityGroup.VISUAL, "Cão-guia bem-vindo (área de hidratação disponível)", "Cão-guia"),
    ;

    companion object {
        /** Filtros exibidos como atalhos na tela inicial (ordem do Figma). */
        val quickFilters = listOf(
            STEP_FREE_ENTRANCE,
            ADAPTED_RESTROOM,
            LIBRAS_SERVICE,
            TACTILE_FLOOR,
            BRAILLE_MATERIALS,
            WIDE_CORRIDORS,
        )
    }
}

/** Situação de um recurso de acessibilidade. */
enum class FeatureStatus(val displayName: String) {
    AVAILABLE("Disponível"),
    PARTIAL("Parcial / Difícil"),
    UNAVAILABLE("Não possui"),
    UNKNOWN("Não sei"),
}

/** Categorias do checklist detalhado de um estabelecimento. */
enum class FeatureCategory(val displayName: String) {
    ACCESS_ENTRY("Acesso e Entrada"),
    INTERNAL_CIRCULATION("Circulação Interna e Mobiliário"),
    ADAPTED_RESTROOM("Sanitário Adaptado"),
    COMMUNICATION("Comunicação e Atendimento"),
    SENSORY("Recursos Sensoriais e Visuais"),
}

/** Nível geral de conformidade calculado a partir do percentual. */
enum class ConformityLevel(val displayName: String, val headline: String) {
    FULL("Plenamente Acessível", "Nível Alto de Acessibilidade"),
    PARTIAL("Parcialmente Acessível", "Nível Médio de Acessibilidade"),
    LOW("Acessibilidade Limitada", "Nível Baixo de Acessibilidade");

    companion object {
        fun fromPercent(percent: Int): ConformityLevel = when {
            percent >= 85 -> FULL
            percent >= 60 -> PARTIAL
            else -> LOW
        }
    }
}

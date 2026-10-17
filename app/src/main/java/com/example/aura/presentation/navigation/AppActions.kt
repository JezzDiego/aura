package com.example.aura.presentation.navigation

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Ações globais disparadas pelo header (disponíveis em qualquer tela via CompositionLocal).
 */
data class AppActions(
    val openAccessibilitySettings: () -> Unit = {},
    val openProfile: () -> Unit = {},
)

val LocalAppActions = staticCompositionLocalOf { AppActions() }

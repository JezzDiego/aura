package com.example.aura.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = RoxoPrimaryDark,
    onPrimary = RoxoOnPrimaryDark,
    primaryContainer = RoxoPrimaryContainerDark,
    onPrimaryContainer = RoxoOnPrimaryContainerDark,

    secondary = RoxoSecondaryDark,
    onSecondary = RoxoOnSecondaryDark,
    secondaryContainer = RoxoSecondaryContainerDark,
    onSecondaryContainer = RoxoOnSecondaryContainerDark,

    tertiary = RoxoTertiaryDark,
    onTertiary = RoxoOnTertiaryDark,
    tertiaryContainer = RoxoTertiaryContainerDark,
    onTertiaryContainer = RoxoOnTertiaryContainerDark,

    background = RoxoBackgroundDark,
    onBackground = RoxoOnBackgroundDark,
    surface = RoxoSurfaceDark,
    onSurface = RoxoOnSurfaceDark,
    outline = RoxoOutlineDark,
)

private val LightColorScheme = lightColorScheme(
    primary = RoxoPrimary,
    onPrimary = RoxoOnPrimary,
    primaryContainer = RoxoPrimaryContainer,
    onPrimaryContainer = RoxoOnPrimaryContainer,

    secondary = RoxoSecondary,
    onSecondary = RoxoOnSecondary,
    secondaryContainer = RoxoSecondaryContainer,
    onSecondaryContainer = RoxoOnSecondaryContainer,

    tertiary = RoxoTertiary,
    onTertiary = RoxoOnTertiary,
    tertiaryContainer = RoxoTertiaryContainer,
    onTertiaryContainer = RoxoOnTertiaryContainer,

    background = RoxoBackground,
    onBackground = RoxoOnBackground,
    surface = RoxoSurface,
    onSurface = RoxoOnSurface,
    outline = RoxoOutline,
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AuraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Force brand colors (no dynamic wallpaper extraction)
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        motionScheme = MotionScheme.expressive(),
        content = content
    )
}

package com.example.aura.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = Color.White,
    primaryContainer = BlueFixed,
    onPrimaryContainer = OnBlueFixed,

    secondary = Teal,
    onSecondary = Color.White,
    secondaryContainer = Cyan,
    onSecondaryContainer = TealText,

    tertiary = BlueBrand,
    onTertiary = Color.White,
    tertiaryContainer = CyanLight,
    onTertiaryContainer = OnCyanLight,

    background = Background,
    onBackground = OnSurface,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceContainerHigh,
    onSurfaceVariant = OnSurfaceVariant,
    surfaceContainerLowest = Surface,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainer = SurfaceContainer,
    surfaceContainerHigh = SurfaceContainerHigh,
    surfaceContainerHighest = SurfaceContainerHighest,
    outline = Outline,
    outlineVariant = OutlineVariant,

    error = Error,
    onError = Color.White,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,
)

private val DarkColorScheme = darkColorScheme(
    primary = BluePrimaryDark,
    onPrimary = OnBluePrimaryDark,
    primaryContainer = BlueContainerDark,
    onPrimaryContainer = OnBlueContainerDark,

    secondary = TealDark,
    onSecondary = OnTealDark,
    secondaryContainer = TealContainerDark,
    onSecondaryContainer = OnTealContainerDark,

    tertiary = BluePrimaryDark,
    onTertiary = OnBluePrimaryDark,
    tertiaryContainer = TealContainerDark,
    onTertiaryContainer = OnTealContainerDark,

    background = BackgroundDark,
    onBackground = OnSurfaceDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceContainerHighDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    surfaceContainerLowest = SurfaceContainerLowestDark,
    surfaceContainerLow = SurfaceContainerLowDark,
    surfaceContainer = SurfaceContainerDark,
    surfaceContainerHigh = SurfaceContainerHighDark,
    surfaceContainerHighest = SurfaceContainerHighestDark,
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark,

    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark,
)

/** Cores extras do design que não existem no ColorScheme do Material 3. */
@Immutable
data class AuraExtraColors(
    val brand: Color,
    val checkboxBorder: Color,
    val progressTrack: Color,
    val isDark: Boolean,
)

val LocalAuraColors = staticCompositionLocalOf {
    AuraExtraColors(BlueBrand, CheckboxBorder, SurfaceContainerHighest, isDark = false)
}

val AuraShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
)

@Composable
fun AuraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val extra = if (darkTheme) {
        AuraExtraColors(BluePrimaryDark, OutlineDark, SurfaceContainerHighestDark, isDark = true)
    } else {
        AuraExtraColors(BlueBrand, CheckboxBorder, SurfaceContainerHighest, isDark = false)
    }

    CompositionLocalProvider(LocalAuraColors provides extra) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = AuraShapes,
            content = content
        )
    }
}

object AuraTheme {
    val extraColors: AuraExtraColors
        @Composable get() = LocalAuraColors.current
}

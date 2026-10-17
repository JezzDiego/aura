package com.example.aura.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.aura.R

// Geist — tipografia usada no Figma "Acessibilidade"
val Geist = FontFamily(
    Font(R.font.geist_regular, FontWeight.Normal),
    Font(R.font.geist_medium, FontWeight.Medium),
    Font(R.font.geist_semibold, FontWeight.SemiBold),
    Font(R.font.geist_bold, FontWeight.Bold),
)

val Typography = Typography(
    // H1 — "Encontre locais com acessibilidade verificada"
    headlineMedium = TextStyle(
        fontFamily = Geist, fontWeight = FontWeight.Bold,
        fontSize = 28.sp, lineHeight = 34.sp, letterSpacing = (-0.28).sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Geist, fontWeight = FontWeight.Bold,
        fontSize = 24.sp, lineHeight = 30.sp, letterSpacing = (-0.24).sp
    ),
    // H2 / H3 — títulos de seção e nome de estabelecimento
    titleLarge = TextStyle(
        fontFamily = Geist, fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp, lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Geist, fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp, lineHeight = 24.sp
    ),
    titleSmall = TextStyle(
        fontFamily = Geist, fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp, lineHeight = 24.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Geist, fontWeight = FontWeight.Normal,
        fontSize = 16.sp, lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Geist, fontWeight = FontWeight.Normal,
        fontSize = 14.sp, lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Geist, fontWeight = FontWeight.Normal,
        fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.24.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Geist, fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.14.sp
    ),
    labelMedium = TextStyle(
        fontFamily = Geist, fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.24.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Geist, fontWeight = FontWeight.Bold,
        fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.6.sp
    ),
)

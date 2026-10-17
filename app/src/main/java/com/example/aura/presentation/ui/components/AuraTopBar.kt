package com.example.aura.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aura.R
import com.example.aura.presentation.navigation.LocalAppActions
import com.example.aura.presentation.ui.theme.AuraTheme

/**
 * Logo "Entrada Livre". No tema claro usa o asset exportado do Figma;
 * no tema escuro recompõe a marca com texto claro para manter o contraste.
 */
@Composable
fun AuraLogo(modifier: Modifier = Modifier, height: Dp = 32.dp) {
    val description = "Entrada Livre"
    if (!AuraTheme.extraColors.isDark) {
        Image(
            painter = painterResource(R.drawable.img_logo_entrada_livre),
            contentDescription = description,
            contentScale = ContentScale.Fit,
            modifier = modifier
                .height(height)
                .width(height * 100f / 32f),
        )
    } else {
        Row(
            modifier = modifier
                .height(height)
                .semantics(mergeDescendants = true) { contentDescription = description },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.ic_logo_mark),
                contentDescription = null,
                modifier = Modifier.size(height * 0.62f),
            )
            Text(
                buildAnnotatedString {
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface)) { append("Entrada") }
                    withStyle(SpanStyle(color = AuraTheme.extraColors.brand)) { append("Livre") }
                },
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = (height.value * 0.4f).sp,
                    letterSpacing = (-0.3).sp,
                ),
            )
        }
    }
}

/**
 * Header fixo das telas: logo, botão de ajustes de acessibilidade e atalho para o perfil.
 */
@Composable
fun AuraTopBar(
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
) {
    val actions = LocalAppActions.current
    Column(
        modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.97f))
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (onBack != null) {
                IconButton(onClick = onBack, modifier = Modifier.padding(end = 4.dp)) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar para a tela anterior",
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
            AuraLogo()
            Box(Modifier.weight(1f))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                SquareIconButton(
                    icon = Icons.Filled.AccessibilityNew,
                    contentDescription = "Ajustes de acessibilidade e contraste",
                    onClick = actions.openAccessibilitySettings,
                    iconSize = 24.dp,
                )
                SquareIconButton(
                    icon = Icons.Outlined.Person,
                    contentDescription = "Abrir perfil e preferências",
                    onClick = actions.openProfile,
                    size = 40.dp,
                    iconSize = 18.dp,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(12.dp),
                )
            }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    }
}

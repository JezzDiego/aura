package com.example.aura.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.aura.presentation.ui.theme.AuraTheme
import com.example.aura.utils.formatRating
import kotlin.math.floor

/** Card branco com cantos de 8dp e sombra suave (padrão das seções do Figma). */
@Composable
fun SectionCard(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surface,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(12.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(8.dp), clip = false)
            .clip(RoundedCornerShape(8.dp))
            .background(color)
            .padding(contentPadding),
        verticalArrangement = verticalArrangement,
        content = content,
    )
}

/** Botão principal azul (48dp, cantos 8dp). */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    enabled: Boolean = true,
    loading: Boolean = false,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    textStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.titleSmall,
) {
    Button(
        onClick = onClick,
        enabled = enabled && !loading,
        modifier = modifier.defaultMinSize(minHeight = 48.dp),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                strokeWidth = 2.dp,
                color = contentColor
            )
            Spacer(Modifier.width(8.dp))
        } else if (leadingIcon != null) {
            Icon(leadingIcon, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(text, style = textStyle, textAlign = TextAlign.Center)
        if (trailingIcon != null) {
            Spacer(Modifier.width(8.dp))
            Icon(trailingIcon, contentDescription = null, modifier = Modifier.size(18.dp))
        }
    }
}

/** Botão secundário com fundo cinza-azulado. */
@Composable
fun TonalButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    contentColor: Color = MaterialTheme.colorScheme.primary,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
) {
    PrimaryButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        containerColor = containerColor,
        contentColor = contentColor,
        textStyle = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
    )
}

/** Botão quadrado só com ícone (48dp) — ex.: acessibilidade, compartilhar, favoritar. */
@Composable
fun SquareIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    iconSize: Dp = 22.dp,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    shape: RoundedCornerShape = RoundedCornerShape(8.dp),
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .size(size)
            .semantics { this.contentDescription = contentDescription },
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(iconSize))
        }
    }
}

/** Etiqueta pequena com ícone (chips dos recursos de acessibilidade). */
@Composable
fun FeatureTag(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = Icons.Outlined.CheckCircle,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    bold: Boolean = false,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(containerColor)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(14.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.let {
                if (bold) it.copy(fontWeight = FontWeight.Bold) else it
            },
            color = contentColor,
        )
    }
}

/** Selo arredondado (ex.: "PERSONALIZAÇÃO ATIVA", "Obrigatório", "Disponível"). */
@Composable
fun Pill(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp),
    uppercase: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
) {
    Row(
        modifier = modifier
            .clip(shape)
            .background(containerColor)
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        if (icon != null) Icon(icon, null, tint = contentColor, modifier = Modifier.size(16.dp))
        Text(
            text = if (uppercase) text.uppercase() else text,
            style = if (uppercase) MaterialTheme.typography.labelSmall else MaterialTheme.typography.labelMedium,
            color = contentColor,
        )
    }
}

/** Cabeçalho de seção (título + subtítulo + ação opcional à direita). */
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    icon: ImageVector? = null,
    trailing: @Composable (RowScope.() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (icon != null) {
                    Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp))
                }
                Text(
                    title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.semantics { heading() },
                )
            }
            if (subtitle != null) {
                Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        trailing?.invoke(this)
    }
}

/** Avatar com iniciais (relatos e perfil). */
@Composable
fun InitialsAvatar(
    initials: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(12.dp))
            .background(containerColor)
            .clearAndSetSemantics { },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            initials,
            style = if (size > 48.dp) MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            else MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = contentColor,
        )
    }
}

/** Estrelas de 0 a 5 com meia estrela. */
@Composable
fun RatingStars(
    rating: Double,
    modifier: Modifier = Modifier,
    starSize: Dp = 18.dp,
    showValue: Boolean = true,
) {
    Row(
        modifier = modifier.semantics(mergeDescendants = true) {
            contentDescription = "Avaliação ${formatRating(rating)} de 5 estrelas"
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        val full = floor(rating).toInt()
        val hasHalf = rating - full >= 0.25 && rating - full < 0.75
        val roundedFull = if (rating - full >= 0.75) full + 1 else full
        repeat(5) { index ->
            val icon = when {
                index < roundedFull -> Icons.Filled.Star
                index == roundedFull && hasHalf -> Icons.AutoMirrored.Filled.StarHalf
                else -> Icons.Filled.StarBorder
            }
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(starSize))
        }
        if (showValue) {
            Spacer(Modifier.width(4.dp))
            Text(
                formatRating(rating),
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

/** Checkbox quadrado no estilo do Figma (24dp, borda 2dp, preenchido em azul). */
@Composable
fun AuraCheckbox(checked: Boolean, modifier: Modifier = Modifier) {
    val primary = MaterialTheme.colorScheme.primary
    Box(
        modifier = modifier
            .size(24.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(if (checked) primary else Color.Transparent)
            .border(
                BorderStroke(2.dp, if (checked) primary else AuraTheme.extraColors.checkboxBorder),
                RoundedCornerShape(3.dp)
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (checked) {
            Icon(Icons.Filled.Check, null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(18.dp))
        }
    }
}

/** Linha clicável de checkbox com ícone e rótulo — acessível como Role.Checkbox. */
@Composable
fun CheckboxRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(role = Role.Checkbox, onClickLabel = if (checked) "Desmarcar" else "Marcar") {
                onCheckedChange(!checked)
            }
            .semantics(mergeDescendants = true) {
                role = Role.Checkbox
                stateDescription = if (checked) "Selecionado" else "Não selecionado"
            }
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AuraCheckbox(checked)
        if (icon != null) {
            Icon(
                icon, null,
                tint = if (checked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
        Text(
            label,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = if (checked) FontWeight.SemiBold else FontWeight.Normal
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
    }
}

/** Ponto colorido + texto (ex.: "● Plenamente Acessível"). */
@Composable
fun StatusDot(text: String, color: Color, modifier: Modifier = Modifier) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(Modifier.size(8.dp).clip(CircleShape).background(color))
        Text(text, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = color)
    }
}

/** Barra de progresso arredondada (percentual de conformidade). */
@Composable
fun ConformityBar(percent: Int, modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxWidth()
            .height(12.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(AuraTheme.extraColors.progressTrack)
            .semantics { contentDescription = "Percentual de conformidade: $percent%" }
    ) {
        Box(
            Modifier
                .fillMaxWidth(percent.coerceIn(0, 100) / 100f)
                .height(12.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(AuraTheme.extraColors.brand)
        )
    }
}

/** Estado vazio / erro genérico. */
@Composable
fun EmptyState(
    title: String,
    message: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHigh),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
        }
        Text(title, style = MaterialTheme.typography.titleSmall, textAlign = TextAlign.Center)
        Text(
            message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        action?.invoke()
    }
}

@Composable
fun LoadingBox(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

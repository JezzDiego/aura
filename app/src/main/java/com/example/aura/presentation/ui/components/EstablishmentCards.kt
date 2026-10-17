package com.example.aura.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.NearMe
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.aura.domain.model.AccessibilityFilter
import com.example.aura.domain.model.ConformityLevel
import com.example.aura.domain.model.Establishment
import com.example.aura.presentation.ui.theme.AuraTheme
import com.example.aura.utils.formatDistance
import com.example.aura.utils.formatRating

@Composable
fun conformityColor(level: ConformityLevel): Color = when (level) {
    ConformityLevel.FULL -> MaterialTheme.colorScheme.primary
    ConformityLevel.PARTIAL -> MaterialTheme.colorScheme.secondary
    ConformityLevel.LOW -> MaterialTheme.colorScheme.error
}

/** Selo translúcido sobre a foto ("Verificado em…", distância). */
@Composable
private fun ImageOverlayBadge(text: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Row(
        modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White.copy(alpha = 0.95f))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(icon, null, tint = Color(0xFF0041A7), modifier = Modifier.size(14.dp))
        Text(
            text,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF161C25),
        )
    }
}

@Composable
private fun RatingBadge(rating: Double) {
    Row(
        Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .semantics(mergeDescendants = true) {
                contentDescription = "Avaliação ${formatRating(rating)} de 5 estrelas"
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(Icons.Filled.Star, null, tint = Color(0xFF7A3E00).takeIf { !AuraTheme.extraColors.isDark }
            ?: MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
        Text(
            formatRating(rating),
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun AddressLine(address: String, suffix: String? = null) {
    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Icon(
            Icons.Outlined.Place, null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 2.dp).size(16.dp)
        )
        Text(
            if (suffix != null) "$address • $suffix" else address,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/**
 * Card de "Estabelecimentos verificados" da tela inicial.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EstablishmentCard(
    establishment: Establishment,
    onOpenDetails: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionCard(modifier = modifier, contentPadding = PaddingValues(0.dp), verticalArrangement = Arrangement.Top) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(176.dp)
        ) {
            AuraImage(
                model = establishment.imageUrl,
                contentDescription = "Foto de ${establishment.name}",
                modifier = Modifier.fillMaxSize(),
            )
            ImageOverlayBadge(
                "Verificado em ${establishment.verifiedAt}",
                Icons.Outlined.Verified,
                Modifier.align(Alignment.TopStart).padding(8.dp),
            )
            ImageOverlayBadge(
                formatDistance(establishment.distanceMeters),
                Icons.Outlined.NearMe,
                Modifier.align(Alignment.BottomEnd).padding(8.dp),
            )
        }
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                Column(Modifier.weight(1f).padding(top = 4.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        establishment.category.uppercase(),
                        style = MaterialTheme.typography.labelMedium.copy(letterSpacing = MaterialTheme.typography.labelSmall.letterSpacing),
                        color = MaterialTheme.colorScheme.secondary,
                    )
                    Text(
                        establishment.name,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    AddressLine(establishment.address)
                }
                RatingBadge(establishment.rating)
            }
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.semantics(mergeDescendants = true) {
                    contentDescription = "Recursos de acessibilidade disponíveis: " +
                        establishment.highlights.joinToString { it.shortLabel }
                },
            ) {
                establishment.highlights.forEach { FeatureTag(it.shortLabel) }
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    "Nível de conformidade:",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                )
                StatusDot(establishment.conformityLevel.displayName, conformityColor(establishment.conformityLevel))
            }
            PrimaryButton(
                text = "Ver detalhes e acessibilidade",
                trailingIcon = Icons.AutoMirrored.Filled.ArrowForward,
                onClick = onOpenDetails,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Ver detalhes e acessibilidade completa de ${establishment.name}" },
            )
        }
    }
}

/**
 * Card de resultado da tela "Buscar Estabelecimentos".
 * Mostra a conformidade com os filtros selecionados (ou os destaques, sem filtros).
 */
@Composable
fun EstablishmentResultCard(
    establishment: Establishment,
    selectedFilters: Set<AccessibilityFilter>,
    onOpenDetails: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val trusted = establishment.auditorsCount >= 8
    SectionCard(
        modifier = modifier.clickable(onClickLabel = "Ver detalhes", onClick = onOpenDetails),
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    establishment.category.uppercase(),
                    style = MaterialTheme.typography.labelMedium.copy(letterSpacing = MaterialTheme.typography.labelSmall.letterSpacing),
                    color = MaterialTheme.colorScheme.secondary,
                )
                Pill(
                    text = "Verificado por ${establishment.auditorsCount} avaliadores",
                    icon = if (trusted) Icons.Outlined.Verified else Icons.Outlined.Info,
                    containerColor = if (trusted) MaterialTheme.colorScheme.secondaryContainer
                    else MaterialTheme.colorScheme.surfaceContainerHigh,
                    contentColor = if (trusted) MaterialTheme.colorScheme.onSecondaryContainer
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(establishment.name, style = MaterialTheme.typography.titleLarge)
                AddressLine(establishment.address, formatDistance(establishment.distanceMeters))
            }
            AuraImage(
                model = establishment.imageUrl,
                contentDescription = "Foto de ${establishment.name}",
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp)),
            )
        }

        val matched = if (selectedFilters.isEmpty()) {
            establishment.highlights.map { it to (it.filter?.icon ?: Icons.Outlined.Verified) }
        } else {
            selectedFilters.mapNotNull { filter ->
                establishment.featureFor(filter)?.let { it to filter.icon }
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                if (selectedFilters.isEmpty()) "Destaques de acessibilidade:"
                else "Conformidade com seus ${selectedFilters.size} filtros:",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            matched.forEach { (feature, icon) ->
                FeatureTag(feature.shortLabel, icon = icon, bold = true)
            }
        }

        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                RatingStars(establishment.rating, starSize = 18.dp)
                Text(
                    " (${establishment.reviewCount} relatos)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            TonalButton(
                text = "Ver detalhes",
                trailingIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                onClick = onOpenDetails,
                modifier = Modifier.semantics {
                    contentDescription = "Ver detalhes de acessibilidade de ${establishment.name}"
                },
            )
        }
    }
}

/** Linha compacta usada em listas secundárias (favoritos, seleção de local). */
@Composable
fun EstablishmentRow(
    establishment: Establishment,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
) {
    Row(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (selected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surfaceContainerLow
            )
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AuraImage(
            model = establishment.imageUrl,
            contentDescription = null,
            modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)),
        )
        Column(Modifier.weight(1f)) {
            Text(establishment.name, style = MaterialTheme.typography.labelLarge, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(
                establishment.address,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Text(
            "${establishment.conformityPercent}%",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = conformityColor(establishment.conformityLevel),
        )
    }
}

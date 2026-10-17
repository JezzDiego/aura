package com.example.aura.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.aura.domain.model.Review
import com.example.aura.utils.formatRelativeTime

/**
 * Card de relato da comunidade ("Últimos relatos da comunidade").
 */
@Composable
fun ReviewCard(
    review: Review,
    onToggleHelpful: () -> Unit,
    modifier: Modifier = Modifier,
    onOpenEstablishment: (() -> Unit)? = null,
    showEstablishment: Boolean = true,
    avatarIndex: Int = 0,
) {
    val avatarColors = if (avatarIndex % 2 == 0) {
        MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
    }
    SectionCard(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Row(
                Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                InitialsAvatar(
                    review.authorInitials,
                    containerColor = avatarColors.first,
                    contentColor = avatarColors.second,
                )
                Column {
                    Text(
                        review.authorName,
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    )
                    review.authorProfile?.let {
                        Text(
                            it.displayName,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            Text(
                formatRelativeTime(review.createdAt),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
            )
        }
        Text(
            "\"${review.comment}\"",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (review.photos.isNotEmpty()) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(review.photos) { photo ->
                    AuraImage(
                        model = photo,
                        contentDescription = "Foto enviada por ${review.authorName}",
                        modifier = Modifier.size(72.dp).clip(RoundedCornerShape(8.dp)),
                    )
                }
            }
        }
        Row(
            Modifier.fillMaxWidth().padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (showEstablishment) {
                Text(
                    "Sobre: ${review.establishmentName}",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .weight(1f)
                        .let { m -> if (onOpenEstablishment != null) m.clickable(onClick = onOpenEstablishment) else m },
                )
            } else {
                Row(Modifier.weight(1f)) {}
            }
            Row(
                Modifier
                    .heightIn(min = 48.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .clickable(role = Role.Button, onClick = onToggleHelpful)
                    .semantics(mergeDescendants = true) {
                        contentDescription = "Marcar este relato como útil, atualmente ${review.helpfulCount} votos"
                        stateDescription = if (review.markedHelpful) "Marcado como útil" else "Não marcado"
                    }
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(
                    if (review.markedHelpful) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                    null,
                    tint = if (review.markedHelpful) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(16.dp),
                )
                Text(
                    "Útil (${review.helpfulCount})",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (review.markedHelpful) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

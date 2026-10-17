package com.example.aura.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Animation
import androidx.compose.material.icons.outlined.Contrast
import androidx.compose.material.icons.outlined.FormatSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

data class AccessibilitySettings(
    val highContrast: Boolean = false,
    val largeText: Boolean = false,
    val reduceMotion: Boolean = false,
)

/** Painel aberto pelo botão de acessibilidade do header. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccessibilitySettingsSheet(
    settings: AccessibilitySettings,
    onHighContrastChange: (Boolean) -> Unit,
    onLargeTextChange: (Boolean) -> Unit,
    onReduceMotionChange: (Boolean) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                "Ajustes de acessibilidade",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.semantics { heading() },
            )
            Text(
                "Adapte a interface às suas necessidades. As preferências ficam salvas neste aparelho.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(8.dp))
            SettingRow(
                icon = Icons.Outlined.Contrast,
                title = "Alto contraste (tema escuro)",
                description = "Fundo escuro com textos claros e cores reforçadas.",
                checked = settings.highContrast,
                onCheckedChange = onHighContrastChange,
            )
            SettingRow(
                icon = Icons.Outlined.FormatSize,
                title = "Texto ampliado",
                description = "Aumenta o tamanho de todos os textos do app em 25%.",
                checked = settings.largeText,
                onCheckedChange = onLargeTextChange,
            )
            SettingRow(
                icon = Icons.Outlined.Animation,
                title = "Reduzir animações",
                description = "Desativa transições e rolagens animadas.",
                checked = settings.reduceMotion,
                onCheckedChange = onReduceMotionChange,
            )
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SettingRow(
    icon: ImageVector,
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    SectionCard(
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .heightIn(min = 64.dp)
                .toggleable(value = checked, role = Role.Switch, onValueChange = onCheckedChange)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.labelLarge)
                Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Switch(
                checked = checked,
                onCheckedChange = null,
                colors = SwitchDefaults.colors(checkedTrackColor = MaterialTheme.colorScheme.primary),
            )
        }
    }
}

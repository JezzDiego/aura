package com.example.aura.presentation.ui.feature_profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Animation
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Contrast
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FormatSize
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.aura.core.OnResumeEffect
import com.example.aura.di.AppContainer
import com.example.aura.domain.model.AccessibilityFilter
import com.example.aura.domain.model.AccessibilityGroup
import com.example.aura.presentation.navigation.destinations.navigateToEstablishmentDetails
import com.example.aura.presentation.navigation.destinations.navigateToLoginScreenWithPopUp
import com.example.aura.presentation.ui.components.CheckboxRow
import com.example.aura.presentation.ui.components.EstablishmentRow
import com.example.aura.presentation.ui.components.InitialsAvatar
import com.example.aura.presentation.ui.components.Pill
import com.example.aura.presentation.ui.components.SectionCard
import com.example.aura.presentation.ui.components.TonalButton
import com.example.aura.presentation.ui.components.icon
import com.example.aura.presentation.ui.feature_login.ProfileChips
import com.example.aura.utils.formatRelativeTime

@Composable
fun ProfileScreen(container: AppContainer, navController: NavHostController) {
    val factory = ProfileViewModelFactory(
        container.userUseCases,
        container.establishmentUseCases,
        container.reviewUseCases,
        container.preferences,
    )
    val viewModel: ProfileViewModel = viewModel(factory = factory)
    val user by viewModel.user.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    val myReviews by viewModel.myReviews.collectAsState()
    val loggedOut by viewModel.loggedOut.collectAsState()
    var editingName by rememberSaveable { mutableStateOf(false) }

    OnResumeEffect { viewModel.refresh() }

    LaunchedEffect(loggedOut) {
        loggedOut?.getContentIfNotHandled()?.let { navController.navigateToLoginScreenWithPopUp() }
    }

    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item("identity") {
            SectionCard {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    InitialsAvatar(user?.initials ?: "?", size = 64.dp)
                    Column(Modifier.weight(1f)) {
                        Text(
                            user?.name ?: "",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.semantics { heading() },
                        )
                        val email = user?.email.orEmpty()
                        Text(
                            email.ifBlank { "Perfil local (sem e-mail)" },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    IconButton(onClick = { editingName = true }) {
                        Icon(Icons.Outlined.Edit, contentDescription = "Editar nome")
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Pill(
                        if (myReviews.size == 1) "1 relato publicado" else "${myReviews.size} relatos publicados",
                        icon = Icons.Outlined.RateReview,
                    )
                    Pill(
                        if (favorites.size == 1) "1 favorito" else "${favorites.size} favoritos",
                        icon = Icons.Outlined.BookmarkBorder,
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }

        item("profile") {
            SectionCard {
                Text(
                    "Meu perfil de acessibilidade",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.semantics { heading() },
                )
                Text(
                    "Usado no botão \"Filtrar pelo meu perfil\" e para contextualizar seus relatos.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                ProfileChips(selected = user?.profile, onSelect = viewModel::selectProfile)
            }
        }

        item("filters") {
            SectionCard {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Outlined.Tune, null, tint = MaterialTheme.colorScheme.primary)
                    Text(
                        "Filtros prioritários",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.semantics { heading() },
                    )
                }
                Text(
                    "Defina uma vez as adaptações prioritárias e receba recomendações imediatas.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                val active = user?.accessibilityFilters.orEmpty()
                AccessibilityGroup.entries.forEach { group ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(top = 8.dp),
                    ) {
                        Icon(group.icon, null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(20.dp))
                        Text(group.displayName, style = MaterialTheme.typography.titleSmall)
                    }
                    AccessibilityFilter.entries.filter { it.group == group }.forEach { filter ->
                        CheckboxRow(
                            label = filter.displayName,
                            checked = filter in active,
                            onCheckedChange = { viewModel.toggleFilter(filter) },
                            icon = filter.icon,
                        )
                    }
                }
            }
        }

        item("settings") {
            SectionCard {
                Text(
                    "Preferências de exibição",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.semantics { heading() },
                )
                SwitchRow(Icons.Outlined.Contrast, "Alto contraste (tema escuro)", settings.highContrast) { viewModel.setHighContrast(it) }
                SwitchRow(Icons.Outlined.FormatSize, "Texto ampliado", settings.largeText) { viewModel.setLargeText(it) }
                SwitchRow(Icons.Outlined.Animation, "Reduzir animações", settings.reduceMotion) { viewModel.setReduceMotion(it) }
            }
        }

        item("favorites") {
            SectionCard {
                Text(
                    "Locais salvos",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.semantics { heading() },
                )
                if (favorites.isEmpty()) {
                    Text(
                        "Toque no marcador nos detalhes de um local para salvá-lo aqui.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                favorites.forEach { establishment ->
                    EstablishmentRow(
                        establishment = establishment,
                        onClick = { navController.navigateToEstablishmentDetails(establishment.id) },
                    )
                }
            }
        }

        if (myReviews.isNotEmpty()) {
            item("my-reviews-header") {
                Text(
                    "Meus relatos",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.semantics { heading() },
                )
            }
            items(myReviews, key = { it.id }) { review ->
                SectionCard(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            review.establishmentName,
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.weight(1f),
                        )
                        Text(
                            formatRelativeTime(review.createdAt),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.outline,
                        )
                    }
                    Text("\"${review.comment}\"", style = MaterialTheme.typography.bodyMedium, maxLines = 3)
                    Text(
                        "Útil para ${review.helpfulCount} pessoas · ${review.photos.size} foto(s)",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        item("logout") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TonalButton(
                    text = "Sair e apagar perfil local",
                    leadingIcon = Icons.AutoMirrored.Outlined.Logout,
                    onClick = viewModel::logout,
                    contentColor = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth(),
                )
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Outlined.WifiOff, null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                    Text(
                        "Modo offline: dados salvos apenas neste aparelho.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }

    if (editingName) {
        EditNameDialog(
            initial = user?.name.orEmpty(),
            onConfirm = {
                viewModel.updateName(it)
                editingName = false
            },
            onDismiss = { editingName = false },
        )
    }
}

@Composable
private fun SwitchRow(icon: ImageVector, title: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .toggleable(value = checked, role = Role.Switch, onValueChange = onChange),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
        Text(title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = null)
    }
}

@Composable
private fun EditNameDialog(initial: String, onConfirm: (String) -> Unit, onDismiss: () -> Unit) {
    var name by remember { mutableStateOf(initial) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar nome") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                singleLine = true,
                label = { Text("Nome") },
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(name) }, enabled = name.isNotBlank()) { Text("Salvar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
    )
}

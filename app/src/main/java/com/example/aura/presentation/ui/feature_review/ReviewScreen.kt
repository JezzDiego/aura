package com.example.aura.presentation.ui.feature_review

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.automirrored.outlined.FactCheck
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.aura.di.AppContainer
import com.example.aura.domain.model.AccessibilityProfile
import com.example.aura.domain.model.Establishment
import com.example.aura.domain.model.FeatureStatus
import com.example.aura.domain.model.Review
import com.example.aura.domain.model.ReviewChecklistItem
import com.example.aura.presentation.ui.components.AuraImage
import com.example.aura.presentation.ui.components.AuraTopBar
import com.example.aura.presentation.ui.components.EstablishmentRow
import com.example.aura.presentation.ui.components.Pill
import com.example.aura.presentation.ui.components.PrimaryButton
import com.example.aura.presentation.ui.components.SectionCard
import com.example.aura.presentation.ui.components.icon
import com.example.aura.presentation.ui.feature_home.SearchField

/**
 * Formulário "Registrar Avaliação de Acessibilidade".
 * Usado como aba do pager ([standalone] = false) e como tela aberta pelos detalhes.
 */
@Composable
fun ReviewScreen(
    container: AppContainer,
    navController: NavHostController,
    initialEstablishmentId: String?,
    standalone: Boolean,
    onPublished: () -> Unit = {},
) {
    val factory = ReviewViewModelFactory(
        container.establishmentUseCases,
        container.reviewUseCases,
        container.userUseCases,
    )
    val viewModel: ReviewViewModel = viewModel(factory = factory)
    val state by viewModel.state.collectAsState()
    val message by viewModel.message.collectAsState()
    val published by viewModel.published.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(initialEstablishmentId) { viewModel.init(initialEstablishmentId) }
    LaunchedEffect(message) { message?.getContentIfNotHandled()?.let { snackbarHostState.showSnackbar(it) } }
    LaunchedEffect(published) {
        published?.getContentIfNotHandled()?.let {
            if (standalone) navController.popBackStack() else onPublished()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { if (standalone) AuraTopBar(onBack = { navController.popBackStack() }) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = if (standalone) androidx.compose.material3.ScaffoldDefaults.contentWindowInsets
        else androidx.compose.foundation.layout.WindowInsets(0),
    ) { padding ->
        ReviewForm(
            state = state,
            viewModel = viewModel,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReviewForm(
    state: ReviewFormState,
    viewModel: ReviewViewModel,
    modifier: Modifier = Modifier,
) {
    var showEstablishmentPicker by rememberSaveable { mutableStateOf(false) }
    val photoPicker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let { viewModel.addPhoto(it.toString()) }
    }

    Column(
        modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Cabeçalho
        SectionCard {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Outlined.VerifiedUser, null, tint = MaterialTheme.colorScheme.primary)
                Text("CONTRIBUIÇÃO CIDADÃ", style = MaterialTheme.typography.labelSmall.copy(fontSize = MaterialTheme.typography.bodyMedium.fontSize), color = MaterialTheme.colorScheme.primary)
            }
            Text(
                "Registrar Avaliação de Acessibilidade",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.semantics { heading() },
            )
            Text(
                "Seu relato ajuda milhares de pessoas a planejarem suas saídas com segurança e autonomia.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        // Local selecionado
        SelectedEstablishmentCard(
            establishment = state.selectedEstablishment,
            showError = state.showErrors && state.selectedEstablishment == null,
            onChange = { showEstablishmentPicker = true },
        )

        // Perfil (opcional)
        SectionCard {
            CardTitle(Icons.Outlined.Groups, "Qual o seu perfil ou recurso principal utilizado?", iconTint = MaterialTheme.colorScheme.secondary)
            Text(
                "Opcional. Ajuda a contextualizar a perspectiva do seu relato na comunidade.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            ProfileDropdown(selected = state.profile, onSelect = viewModel::selectProfile)
        }

        // Checklist
        SectionCard {
            CardTitle(
                Icons.AutoMirrored.Outlined.FactCheck,
                "Checklist de Acessibilidade",
                badge = { Pill("Obrigatório") },
            )
            Text(
                "Selecione a condição encontrada no local para cada item fundamental.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            ReviewChecklistItem.entries.forEachIndexed { index, item ->
                ChecklistQuestion(
                    number = index + 1,
                    item = item,
                    selected = state.checklist[item],
                    showError = state.showErrors && item !in state.checklist,
                    onSelect = { viewModel.setStatus(item, it) },
                )
            }
        }

        // Relato
        SectionCard {
            CardTitle(
                Icons.Outlined.RateReview,
                "Seu relato e observações detalhadas",
                badge = {
                    Pill(
                        "Obrigatório",
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    )
                },
            )
            Text(
                "Descreva detalhes práticos observados durante a visita. Exemplo: rampa escorregadia em dias de chuva, maçanetas tipo alavanca ou portas pesadas.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            val commentError = state.showErrors && state.comment.isBlank()
            TextField(
                value = state.comment,
                onValueChange = viewModel::onCommentChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 128.dp)
                    .then(
                        if (commentError) Modifier.border(2.dp, MaterialTheme.colorScheme.error, RoundedCornerShape(8.dp))
                        else Modifier
                    )
                    .semantics {
                        contentDescription = "Seu relato"
                        if (commentError) error("Descreva seu relato")
                    },
                placeholder = {
                    Text(
                        "Conte detalhes úteis: como estava a conservação da rampa? O banheiro estava trancado? O atendente foi solícito?",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.outline,
                    )
                },
                textStyle = MaterialTheme.typography.bodyLarge,
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
            )
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "${state.comment.length} / ${Review.MAX_COMMENT_LENGTH} caracteres",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                )
                Icon(Icons.Outlined.Visibility, null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(16.dp))
                Spacer(Modifier.size(4.dp))
                Text("Visível publicamente", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
            }
        }

        // Fotos
        SectionCard {
            CardTitle(
                Icons.Outlined.AddPhotoAlternate,
                "Fotos Comprovatórias",
                badge = {
                    Text(
                        "Até ${Review.MAX_PHOTOS} imagens",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
            )
            Text(
                "Fotos da entrada, rampas, corrimãos e banheiros aumentam consideravelmente a confiança de quem pesquisa.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (state.photos.isNotEmpty()) {
                state.photos.chunked(2).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        row.forEach { photo ->
                            val index = state.photos.indexOf(photo) + 1
                            PhotoThumb(
                                uri = photo,
                                label = "Foto $index",
                                onRemove = { viewModel.removePhoto(photo) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }
            Surface(
                onClick = {
                    photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                enabled = state.canAddPhoto && !state.isAddingPhoto,
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                contentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp),
            ) {
                Row(
                    Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (state.isAddingPhoto) {
                        CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                    } else {
                        Icon(Icons.Outlined.AddAPhoto, null)
                    }
                    Spacer(Modifier.size(8.dp))
                    Text(
                        if (state.canAddPhoto) "Adicionar foto da entrada/banheiro" else "Limite de ${Review.MAX_PHOTOS} fotos atingido",
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
            }
        }

        // Publicar
        PrimaryButton(
            text = "Publicar Avaliação Acessível",
            leadingIcon = Icons.AutoMirrored.Outlined.Send,
            onClick = viewModel::submit,
            loading = state.isSubmitting,
            textStyle = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
        )

        Row(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(Icons.Outlined.Shield, null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(20.dp))
            Text(
                "Todas as avaliações são revisadas pela comunidade e ajudam a manter os dados sempre confiáveis.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Spacer(Modifier.height(16.dp))
    }

    if (showEstablishmentPicker) {
        EstablishmentPickerSheet(
            establishments = state.establishments,
            selectedId = state.selectedEstablishment?.id,
            onSelect = {
                viewModel.selectEstablishment(it)
                showEstablishmentPicker = false
            },
            onDismiss = { showEstablishmentPicker = false },
        )
    }
}

@Composable
private fun CardTitle(
    icon: ImageVector,
    title: String,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    badge: (@Composable () -> Unit)? = null,
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(icon, null, tint = iconTint, modifier = Modifier.size(22.dp))
        Text(
            title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .weight(1f)
                .semantics { heading() },
        )
        badge?.invoke()
    }
}

@Composable
private fun SelectedEstablishmentCard(
    establishment: Establishment?,
    showError: Boolean,
    onChange: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .then(
                if (showError) Modifier.border(2.dp, MaterialTheme.colorScheme.error, RoundedCornerShape(8.dp))
                else Modifier
            )
            .padding(16.dp)
            .semantics(mergeDescendants = true) {
                if (showError) error("Selecione o estabelecimento avaliado")
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                if (establishment != null) Icons.Filled.Restaurant else Icons.Outlined.Storefront,
                null,
                tint = MaterialTheme.colorScheme.onPrimary,
            )
        }
        Column(Modifier.weight(1f)) {
            Text(
                if (establishment != null) "Estabelecimento Selecionado" else "Local avaliado",
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = if (showError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary,
            )
            Text(
                establishment?.name ?: "Escolha o local avaliado",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            establishment?.let {
                Text(
                    it.address,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        Surface(
            onClick = onChange,
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .heightIn(min = 48.dp)
                .semantics { contentDescription = "Alterar local avaliado" },
        ) {
            Row(
                Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(Icons.Outlined.Edit, null, modifier = Modifier.size(18.dp))
                Text(
                    if (establishment != null) "Alterar" else "Selecionar",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                )
            }
        }
    }
}

@Composable
private fun ProfileDropdown(selected: AccessibilityProfile?, onSelect: (AccessibilityProfile?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Surface(
            onClick = { expanded = true },
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHighest,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .semantics { contentDescription = "Perfil: ${selected?.displayName ?: "não informado"}. Toque para alterar" },
        ) {
            Row(
                Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                selected?.let {
                    Icon(it.icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.size(8.dp))
                }
                Text(
                    selected?.displayName ?: "Selecione seu perfil (opcional)",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f),
                )
                Icon(Icons.Outlined.ArrowDropDown, null)
            }
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text("Prefiro não informar") },
                onClick = { onSelect(null); expanded = false },
            )
            AccessibilityProfile.entries.forEach { profile ->
                DropdownMenuItem(
                    text = { Text(profile.displayName) },
                    leadingIcon = { Icon(profile.icon, null, tint = MaterialTheme.colorScheme.primary) },
                    trailingIcon = if (profile == selected) {
                        { Icon(Icons.Filled.Check, null) }
                    } else null,
                    onClick = { onSelect(profile); expanded = false },
                )
            }
        }
    }
}

@Composable
private fun ChecklistQuestion(
    number: Int,
    item: ReviewChecklistItem,
    selected: FeatureStatus?,
    showError: Boolean,
    onSelect: (FeatureStatus) -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .then(
                if (showError) Modifier.border(2.dp, MaterialTheme.colorScheme.error, RoundedCornerShape(8.dp))
                else Modifier
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(item.icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            Text(
                "$number. ${item.displayName}",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.semantics { heading() },
            )
        }
        if (showError) {
            Text("Escolha uma opção", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.error)
        }
        Column(Modifier.selectableGroup(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            FeatureStatus.entries.chunked(2).forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    row.forEach { status ->
                        val isSelected = status == selected
                        Row(
                            Modifier
                                .weight(1f)
                                .height(48.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.surfaceContainerHighest
                                )
                                .selectable(selected = isSelected, role = Role.RadioButton, onClick = { onSelect(status) })
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            val color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                            Icon(status.icon, null, tint = color, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.size(8.dp))
                            Text(
                                status.displayName,
                                style = MaterialTheme.typography.labelLarge,
                                color = color,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PhotoThumb(uri: String, label: String, onRemove: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier
            .height(112.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        AuraImage(model = uri, contentDescription = label, modifier = Modifier.fillMaxSize())
        Row(
            Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.White.copy(alpha = 0.9f))
                .padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(Icons.Filled.Check, null, tint = Color(0xFF0041A7), modifier = Modifier.size(14.dp))
            Text(label, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = Color(0xFF161C25))
        }
        Box(
            Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .size(40.dp)
                .clickable(role = Role.Button, onClick = onRemove)
                .semantics { contentDescription = "Remover $label" },
            contentAlignment = Alignment.Center,
        ) {
            Box(
                Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Filled.Close, null, tint = Color(0xFFBA1A1A), modifier = Modifier.size(18.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EstablishmentPickerSheet(
    establishments: List<Establishment>,
    selectedId: String?,
    onSelect: (Establishment) -> Unit,
    onDismiss: () -> Unit,
) {
    var query by remember { mutableStateOf("") }
    val filtered = establishments.filter {
        query.isBlank() || it.name.contains(query, ignoreCase = true) ||
            it.neighborhood.contains(query, ignoreCase = true) ||
            it.category.contains(query, ignoreCase = true)
    }
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
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                "Qual local você visitou?",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.semantics { heading() },
            )
            SearchField(
                value = query,
                onValueChange = { query = it },
                placeholder = "Buscar por nome ou bairro",
                onSearch = {},
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 24.dp),
            ) {
                items(filtered, key = { it.id }) { establishment ->
                    EstablishmentRow(
                        establishment = establishment,
                        selected = establishment.id == selectedId,
                        onClick = { onSelect(establishment) },
                    )
                }
            }
        }
    }
}

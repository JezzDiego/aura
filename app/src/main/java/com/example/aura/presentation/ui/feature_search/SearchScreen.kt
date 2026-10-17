package com.example.aura.presentation.ui.feature_search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.aura.core.OnResumeEffect
import com.example.aura.core.ResultWrapper
import com.example.aura.domain.model.AccessibilityFilter
import com.example.aura.domain.model.AccessibilityGroup
import com.example.aura.domain.model.Establishment
import com.example.aura.domain.model.User
import com.example.aura.presentation.navigation.LocalReduceMotion
import com.example.aura.presentation.navigation.destinations.navigateToEstablishmentDetails
import com.example.aura.presentation.ui.components.AuraImage
import com.example.aura.presentation.ui.components.CheckboxRow
import com.example.aura.presentation.ui.components.EmptyState
import com.example.aura.presentation.ui.components.EstablishmentResultCard
import com.example.aura.presentation.ui.components.EstablishmentRow
import com.example.aura.presentation.ui.components.LoadingBox
import com.example.aura.presentation.ui.components.Pill
import com.example.aura.presentation.ui.components.PrimaryButton
import com.example.aura.presentation.ui.components.SectionCard
import com.example.aura.presentation.ui.components.icon
import com.example.aura.presentation.ui.feature_home.SearchField
import com.example.aura.utils.Constants
import com.example.aura.presentation.ui.theme.AuraTheme

private const val RESULTS_HEADER_INDEX = 3

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    navController: NavHostController,
    onContribute: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    val user by viewModel.user.collectAsState()
    val listState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    val reduceMotion = LocalReduceMotion.current

    OnResumeEffect { viewModel.refresh() }

    LaunchedEffect(state.scrollToResultsSignal) {
        if (state.scrollToResultsSignal > 0 && !state.filtersExpanded) {
            if (reduceMotion) listState.scrollToItem(RESULTS_HEADER_INDEX)
            else listState.animateScrollToItem(RESULTS_HEADER_INDEX)
        }
    }

    val results = (state.results as? ResultWrapper.Success)?.value.orEmpty()

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // 0 — Cabeçalho
        item("header") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Pill(
                        text = if (state.selectedFilters.isEmpty()) "Nenhum filtro ativo" else "Filtros Inclusivos Ativos",
                        icon = Icons.Outlined.Shield,
                        containerColor = if (state.selectedFilters.isEmpty()) MaterialTheme.colorScheme.surfaceContainerHigh
                        else MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = if (state.selectedFilters.isEmpty()) MaterialTheme.colorScheme.onSurfaceVariant
                        else MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        "${state.selectedFilters.size} critérios aplicados",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text(
                    "Buscar Estabelecimentos",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.semantics { heading() },
                )
                Text(
                    "Encontre espaços validados em São Paulo para autonomia motora, sensorial e comunicativa.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        // 1 — Campo de busca
        item("search") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Outlined.Search, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                    Text("Nome, categoria gastronômica/cultural ou bairro", style = MaterialTheme.typography.labelLarge)
                }
                SearchField(
                    value = state.query,
                    onValueChange = viewModel::onQueryChange,
                    placeholder = "Ex: Pinheiros, café, teatro...",
                    leadingIcon = Icons.Outlined.Storefront,
                    onSearch = {
                        focusManager.clearFocus()
                        viewModel.applyFilters()
                    },
                    trailing = if (state.query.isNotEmpty()) {
                        {
                            IconButton(onClick = viewModel::clearQuery) {
                                Icon(Icons.Outlined.Cancel, contentDescription = "Limpar texto da busca")
                            }
                        }
                    } else null,
                )
            }
        }

        // 2 — Painel de filtros
        item("filters") {
            FiltersPanel(
                user = user,
                selected = state.selectedFilters,
                expanded = state.filtersExpanded,
                resultCount = results.size,
                onToggleExpanded = viewModel::toggleFiltersExpanded,
                onToggleFilter = viewModel::toggleFilter,
                onApply = {
                    focusManager.clearFocus()
                    viewModel.applyFilters()
                },
                onUseProfile = viewModel::applyProfileFilters,
                onClear = viewModel::clearFilters,
            )
        }

        // 3 — Cabeçalho dos resultados
        item("results-header") {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(
                        "${results.size} ${if (results.size == 1) "local encontrado" else "locais encontrados"}",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.semantics { heading() },
                    )
                    Text(
                        if (state.query.isBlank()) "Próximos à sua localização" else "Resultados para \"${state.query}\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                ViewModeTabs(state.viewMode, viewModel::setViewMode)
            }
        }

        when (val r = state.results) {
            is ResultWrapper.Loading -> item("loading") { LoadingBox() }
            is ResultWrapper.Error -> item("error") {
                EmptyState(
                    title = "Erro na busca",
                    message = r.throwable.message ?: "Tente novamente.",
                    icon = Icons.Outlined.SearchOff,
                )
            }
            is ResultWrapper.Success -> {
                if (r.value.isEmpty()) {
                    item("empty") {
                        EmptyState(
                            title = "Nenhum local atende a todos os filtros",
                            message = "Tente remover algum critério ou buscar por outro bairro.",
                            icon = Icons.Outlined.SearchOff,
                            action = {
                                TextButton(onClick = viewModel::clearFilters) { Text("Limpar filtros") }
                            },
                        )
                    }
                } else if (state.viewMode == ResultsViewMode.MAP) {
                    item("map") {
                        OfflineMap(
                            establishments = r.value,
                            onOpen = { navController.navigateToEstablishmentDetails(it.id) },
                        )
                    }
                } else {
                    items(r.value, key = { "res-${it.id}" }) { establishment ->
                        EstablishmentResultCard(
                            establishment = establishment,
                            selectedFilters = state.selectedFilters,
                            onOpenDetails = { navController.navigateToEstablishmentDetails(establishment.id) },
                        )
                    }
                }
            }
        }

        item("community") {
            CommunityCard(onContribute = onContribute)
        }
    }
}

@Composable
private fun FiltersPanel(
    user: User?,
    selected: Set<AccessibilityFilter>,
    expanded: Boolean,
    resultCount: Int,
    onToggleExpanded: () -> Unit,
    onToggleFilter: (AccessibilityFilter) -> Unit,
    onApply: () -> Unit,
    onUseProfile: () -> Unit,
    onClear: () -> Unit,
) {
    SectionCard(contentPadding = PaddingValues(0.dp), verticalArrangement = Arrangement.Top) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .clickable(role = Role.Button, onClick = onToggleExpanded)
                .semantics {
                    stateDescription = if (expanded) "Expandido" else "Recolhido"
                }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Outlined.Tune, null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
            }
            Column(Modifier.weight(1f)) {
                Text(
                    "Filtros de Acessibilidade",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.semantics { heading() },
                )
                Text(
                    if (selected.isEmpty()) "Nenhum critério selecionado"
                    else selected.joinToString { it.shortLabel },
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Icon(
                if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                contentDescription = if (expanded) "Recolher filtros" else "Expandir filtros",
            )
        }

        AnimatedVisibility(visible = expanded) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                val profileFilters = user?.accessibilityFilters.orEmpty()
                if (profileFilters.isNotEmpty() || selected.isNotEmpty()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (profileFilters.isNotEmpty()) {
                            TextButton(onClick = onUseProfile) { Text("Usar filtros do meu perfil") }
                        }
                        if (selected.isNotEmpty()) {
                            TextButton(onClick = onClear) { Text("Limpar") }
                        }
                    }
                }
                AccessibilityGroup.entries.forEach { group ->
                    FilterGroup(
                        group = group,
                        filters = AccessibilityFilter.entries.filter { it.group == group },
                        selected = selected,
                        onToggle = onToggleFilter,
                    )
                }
                PrimaryButton(
                    text = "Aplicar Filtros (${selected.size} selecionados) - $resultCount ${if (resultCount == 1) "local" else "locais"}",
                    leadingIcon = Icons.Outlined.CloudUpload,
                    onClick = onApply,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.titleSmall,
                )
            }
        }
    }
}

@Composable
private fun FilterGroup(
    group: AccessibilityGroup,
    filters: List<AccessibilityFilter>,
    selected: Set<AccessibilityFilter>,
    onToggle: (AccessibilityFilter) -> Unit,
) {
    val groupColor = when (group) {
        AccessibilityGroup.PHYSICAL -> MaterialTheme.colorScheme.primary
        AccessibilityGroup.HEARING -> MaterialTheme.colorScheme.secondary
        AccessibilityGroup.VISUAL -> if (AuraTheme.extraColors.isDark) Color(0xFFFFB68A) else Color(0xFFA14A00)
    }
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(group.icon, null, tint = groupColor, modifier = Modifier.size(22.dp))
            Text(
                group.displayName,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.semantics { heading() },
            )
        }
        Text(
            group.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(4.dp))
        filters.forEach { filter ->
            CheckboxRow(
                label = filter.displayName,
                checked = filter in selected,
                onCheckedChange = { onToggle(filter) },
                icon = filter.icon,
            )
        }
    }
}

@Composable
private fun ViewModeTabs(mode: ResultsViewMode, onChange: (ResultsViewMode) -> Unit) {
    Row(
        Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        ViewModeTab("Lista", Icons.AutoMirrored.Outlined.List, mode == ResultsViewMode.LIST) { onChange(ResultsViewMode.LIST) }
        ViewModeTab("Mapa", Icons.Outlined.Map, mode == ResultsViewMode.MAP) { onChange(ResultsViewMode.MAP) }
    }
}

@Composable
private fun ViewModeTab(label: String, icon: ImageVector, selected: Boolean, onClick: () -> Unit) {
    val color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
    Row(
        Modifier
            .heightIn(min = 40.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(if (selected) MaterialTheme.colorScheme.surface else Color.Transparent)
            .clickable(role = Role.Tab, onClick = onClick)
            .semantics { stateDescription = if (selected) "Selecionado" else "Não selecionado" }
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Icon(icon, null, tint = color, modifier = Modifier.size(16.dp))
        Text(label, style = MaterialTheme.typography.labelMedium, color = color)
    }
}

/** Mapa estático (offline) com a lista compacta de resultados. */
@Composable
private fun OfflineMap(establishments: List<Establishment>, onOpen: (Establishment) -> Unit) {
    SectionCard {
        Box(
            Modifier
                .fillMaxWidth()
                .aspectRatio(1.4f)
                .clip(RoundedCornerShape(8.dp))
        ) {
            AuraImage(
                model = Constants.drawableUri("img_mapa_sao_paulo"),
                contentDescription = "Mapa ilustrativo da região de São Paulo",
                modifier = Modifier.fillMaxSize(),
            )
            Pill(
                text = "Mapa offline — ${establishments.size} locais na região",
                icon = Icons.Outlined.Map,
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                contentColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.align(Alignment.BottomStart).padding(8.dp),
            )
        }
        establishments.forEach { establishment ->
            EstablishmentRow(establishment = establishment, onClick = { onOpen(establishment) })
        }
    }
}

@Composable
private fun CommunityCard(onContribute: () -> Unit) {
    SectionCard(color = MaterialTheme.colorScheme.surfaceContainerLow, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Outlined.Groups, null, tint = MaterialTheme.colorScheme.secondary)
            Text(
                "Colaboração da Comunidade",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.semantics { heading() },
            )
        }
        Text(
            "Sentiu falta de dados sobre calçadas, guias ou intérpretes? Você pode adicionar uma rápida avaliação em menos de 2 minutos.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(
            Modifier
                .heightIn(min = 48.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable(role = Role.Button, onClick = onContribute)
                .semantics { contentDescription = "Contribuir com nova avaliação" },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                "Contribuir com nova avaliação",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
            )
            Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
        }
    }
}

package com.example.aura.presentation.ui.feature_home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.HowToReg
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.aura.core.ResultWrapper
import com.example.aura.core.OnResumeEffect
import com.example.aura.di.AppContainer
import com.example.aura.domain.model.AccessibilityFilter
import com.example.aura.domain.model.User
import com.example.aura.presentation.navigation.BottomNavBarItem
import com.example.aura.presentation.navigation.destinations.navigateToEstablishmentDetails
import com.example.aura.presentation.ui.components.EmptyState
import com.example.aura.presentation.ui.components.EstablishmentCard
import com.example.aura.presentation.ui.components.LoadingBox
import com.example.aura.presentation.ui.components.Pill
import com.example.aura.presentation.ui.components.PrimaryButton
import com.example.aura.presentation.ui.components.ReviewCard
import com.example.aura.presentation.ui.components.SectionCard
import com.example.aura.presentation.ui.components.SectionHeader
import com.example.aura.presentation.ui.components.quickIcon

@Composable
fun HomeScreen(
    container: AppContainer,
    navController: NavHostController,
    onSearch: (query: String, filters: Set<AccessibilityFilter>) -> Unit,
    swipeNavigate: (BottomNavBarItem) -> Unit,
) {
    val factory = HomeViewModelFactory(
        container.establishmentUseCases,
        container.reviewUseCases,
        container.userUseCases,
    )
    val viewModel: HomeViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()
    val reviews by viewModel.reviews.collectAsState()
    val user by viewModel.user.collectAsState()
    val query by viewModel.query.collectAsState()
    var showAllReviews by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    OnResumeEffect { viewModel.refresh() }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        item("welcome") {
            WelcomeSearchCard(
                query = query,
                onQueryChange = viewModel::onQueryChange,
                onSearch = {
                    focusManager.clearFocus()
                    onSearch(query, emptySet())
                },
            )
        }

        item("quick-filters") {
            QuickFilters(onFilterClick = { onSearch("", setOf(it)) })
        }

        item("persona") {
            PersonaBanner(
                user = user,
                onFilterByProfile = {
                    val filters = user?.accessibilityFilters.orEmpty()
                    if (filters.isEmpty()) swipeNavigate(BottomNavBarItem.ProfileNavBarItem)
                    else onSearch("", filters)
                },
            )
        }

        item("verified-header") {
            SectionHeader(
                title = "Estabelecimentos verificados",
                subtitle = "Próximos à sua localização atual",
                trailing = {
                    Pill(
                        text = "São Paulo, SP",
                        icon = Icons.Outlined.MyLocation,
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(4.dp),
                    )
                }
            )
        }

        when (val state = uiState) {
            is ResultWrapper.Loading -> item("loading") { LoadingBox() }
            is ResultWrapper.Error -> item("error") {
                EmptyState(
                    title = "Não foi possível carregar os locais",
                    message = state.throwable.message ?: "Tente novamente.",
                    icon = Icons.Outlined.SearchOff,
                    action = { PrimaryButton("Tentar novamente", onClick = viewModel::refresh) },
                )
            }
            is ResultWrapper.Success -> {
                items(state.value, key = { "est-${it.id}" }) { establishment ->
                    EstablishmentCard(
                        establishment = establishment,
                        onOpenDetails = { navController.navigateToEstablishmentDetails(establishment.id) },
                    )
                }
            }
        }

        item("reviews-header") {
            SectionHeader(
                title = "Últimos relatos da comunidade",
                subtitle = "Experiências reais compartilhadas",
                trailing = {
                    Row(
                        Modifier
                            .heightIn(min = 48.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { showAllReviews = !showAllReviews }
                            .padding(horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            if (showAllReviews) "Ver menos" else "Ver todos",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight, null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            )
        }

        val visibleReviews = if (showAllReviews) reviews else reviews.take(2)
        itemsIndexed(visibleReviews, key = { _, review -> "rev-${review.id}" }) { index, review ->
            ReviewCard(
                review = review,
                avatarIndex = index,
                onToggleHelpful = { viewModel.toggleHelpful(review.id) },
                onOpenEstablishment = { navController.navigateToEstablishmentDetails(review.establishmentId) },
            )
        }
    }
}

@Composable
private fun WelcomeSearchCard(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
) {
    SectionCard {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Outlined.VerifiedUser, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
            Text(
                "MAPEAMENTO CIDADÃO 100% AUDITADO",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Text(
            "Encontre locais com acessibilidade verificada",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.semantics { heading() },
        )
        Text(
            "Autonomia e segurança para navegar pela cidade com informações precisas e avaliadas por quem vive a acessibilidade no dia a dia.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Column(Modifier.padding(top = 8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Buscar restaurante, café, cinema ou endereço...", style = MaterialTheme.typography.labelLarge)
            SearchField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = "Ex: Café com rampa, Av. Paulista...",
                onSearch = onSearch,
            )
            PrimaryButton(
                text = "Pesquisar",
                leadingIcon = Icons.Outlined.Tune,
                onClick = onSearch,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Confirmar pesquisa de estabelecimentos" },
            )
        }
    }
}

@Composable
fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector = Icons.Outlined.Search,
    trailing: (@Composable () -> Unit)? = null,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp),
        singleLine = true,
        placeholder = {
            Text(placeholder, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.outline)
        },
        textStyle = MaterialTheme.typography.bodyLarge,
        leadingIcon = { Icon(leadingIcon, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
        trailingIcon = trailing,
        shape = RoundedCornerShape(8.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
    )
}

@Composable
private fun QuickFilters(onFilterClick: (AccessibilityFilter) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Row(
                Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(Icons.Outlined.FilterList, null, tint = MaterialTheme.colorScheme.primary)
                Text(
                    "Filtros Rápidos",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.semantics { heading() },
                )
            }
            Text(
                "Selecione para filtrar",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Box(Modifier.fillMaxWidth()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 4.dp),
                modifier = Modifier.semantics { contentDescription = "Filtros prioritários de acessibilidade" },
            ) {
                items(AccessibilityFilter.quickFilters, key = { it.name }) { filter ->
                    Surface(
                        onClick = { onFilterClick(filter) },
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        modifier = Modifier.height(48.dp),
                    ) {
                        Row(
                            Modifier.padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Icon(filter.quickIcon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                            Text(filter.shortLabel, style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PersonaBanner(user: User?, onFilterByProfile: () -> Unit) {
    val onBanner = MaterialTheme.colorScheme.onSecondaryContainer
    SectionCard(color = MaterialTheme.colorScheme.secondaryContainer, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Pill(
            text = if (user?.profile != null) "Personalização ativa" else "Personalize sua busca",
            icon = Icons.Outlined.AutoAwesome,
            containerColor = onBanner.copy(alpha = 0.1f),
            contentColor = onBanner,
            shape = RoundedCornerShape(6.dp),
            uppercase = true,
        )
        Text(
            "Sua experiência, no seu ritmo",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = onBanner,
            modifier = Modifier.semantics { heading() },
        )
        val profile = user?.profile
        Text(
            if (profile != null) {
                "Perfil: ${profile.displayName}. Filtros prioritários: " +
                    user.accessibilityFilters.joinToString { it.shortLabel } + "."
            } else {
                "Defina uma vez as adaptações prioritárias para o seu perfil e receba recomendações imediatas com status em tempo real."
            },
            style = MaterialTheme.typography.bodyMedium,
            color = onBanner,
        )
        PrimaryButton(
            text = if (profile != null) "Filtrar pelo meu perfil de acessibilidade" else "Definir meu perfil de acessibilidade",
            leadingIcon = Icons.Outlined.HowToReg,
            onClick = onFilterByProfile,
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            textStyle = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(top = 4.dp).fillMaxWidth(),
        )
    }
}

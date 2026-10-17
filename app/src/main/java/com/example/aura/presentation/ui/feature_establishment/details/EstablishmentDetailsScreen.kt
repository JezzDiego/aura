package com.example.aura.presentation.ui.feature_establishment.details

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.automirrored.filled.Accessible
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Directions
import androidx.compose.material.icons.outlined.DirectionsSubway
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.aura.core.OnResumeEffect
import com.example.aura.core.ResultWrapper
import com.example.aura.di.AppContainer
import com.example.aura.domain.model.AccessibilityFeature
import com.example.aura.domain.model.Establishment
import com.example.aura.domain.model.EstablishmentPhoto
import com.example.aura.domain.model.FeatureCategory
import com.example.aura.domain.model.FeatureStatus
import com.example.aura.presentation.navigation.LocalReduceMotion
import com.example.aura.presentation.navigation.destinations.navigateToReview
import com.example.aura.presentation.ui.components.AuraImage
import com.example.aura.presentation.ui.components.AuraTopBar
import com.example.aura.presentation.ui.components.ConformityBar
import com.example.aura.presentation.ui.components.EmptyState
import com.example.aura.presentation.ui.components.LoadingBox
import com.example.aura.presentation.ui.components.Pill
import com.example.aura.presentation.ui.components.PrimaryButton
import com.example.aura.presentation.ui.components.ReviewCard
import com.example.aura.presentation.ui.components.SectionCard
import com.example.aura.presentation.ui.components.SquareIconButton
import com.example.aura.presentation.ui.components.icon
import com.example.aura.presentation.ui.theme.AuraTheme
import com.example.aura.utils.Constants
import kotlinx.coroutines.launch

@Composable
fun EstablishmentDetailsScreen(
    container: AppContainer,
    navController: NavHostController,
    establishmentId: String,
) {
    val factory = EstablishmentDetailsViewModelFactory(container.establishmentUseCases, container.reviewUseCases)
    val viewModel: EstablishmentDetailsViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()
    val reviews by viewModel.reviews.collectAsState()
    val shareEvent by viewModel.shareEvent.collectAsState()
    val message by viewModel.message.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(establishmentId) { viewModel.load(establishmentId) }
    OnResumeEffect { viewModel.refresh() }

    LaunchedEffect(shareEvent) {
        shareEvent?.getContentIfNotHandled()?.let { link ->
            val send = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, link.message)
            }
            context.startActivity(Intent.createChooser(send, "Compartilhar local"))
        }
    }
    LaunchedEffect(message) {
        message?.getContentIfNotHandled()?.let { snackbarHostState.showSnackbar(it) }
    }

    val establishment = (uiState as? ResultWrapper.Success)?.value

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { AuraTopBar(onBack = { navController.popBackStack() }) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (establishment != null) {
                QuickActionsBar(
                    isFavorite = establishment.isFavorite,
                    name = establishment.name,
                    onToggleFavorite = viewModel::toggleFavorite,
                    onReview = { navController.navigateToReview(establishment.id) },
                )
            }
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (val state = uiState) {
                is ResultWrapper.Loading -> LoadingBox()
                is ResultWrapper.Error -> EmptyState(
                    title = "Não foi possível carregar o local",
                    message = state.throwable.message ?: "Tente novamente.",
                    icon = Icons.Outlined.SearchOff,
                )
                is ResultWrapper.Success -> {
                    val value = state.value
                    if (value == null) {
                        EmptyState(
                            title = "Local não encontrado",
                            message = "Este estabelecimento não está mais disponível.",
                            icon = Icons.Outlined.SearchOff,
                        )
                    } else {
                        DetailsContent(
                            establishment = value,
                            reviews = reviews,
                            onDirections = {
                                val uri = Uri.parse(
                                    "geo:${value.latitude},${value.longitude}?q=" +
                                        Uri.encode("${value.address}, ${value.city}")
                                )
                                try {
                                    context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                                } catch (_: ActivityNotFoundException) {
                                    scope.launch { snackbarHostState.showSnackbar("Nenhum aplicativo de mapas encontrado") }
                                }
                            },
                            onShare = viewModel::share,
                            onToggleHelpful = viewModel::toggleHelpful,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DetailsContent(
    establishment: Establishment,
    reviews: List<com.example.aura.domain.model.Review>,
    onDirections: () -> Unit,
    onShare: () -> Unit,
    onToggleHelpful: (String) -> Unit,
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val reduceMotion = LocalReduceMotion.current
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    val categories = establishment.features.groupBy { it.category }
        .toList()
        .sortedBy { it.first.ordinal }

    // Índices dos itens da LazyColumn para a navegação por seções
    val checklistIndex = 3
    val galleryIndex = checklistIndex + 1 + categories.size
    val reviewsIndex = galleryIndex + 1 + establishment.photos.size
    val locationIndex = reviewsIndex + 1 + reviews.size

    fun scrollTo(state: LazyListState, index: Int) {
        scope.launch { if (reduceMotion) state.scrollToItem(index) else state.animateScrollToItem(index) }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // 0 — Imagem principal
        item("hero") { HeroImage(establishment) }

        // 1 — Identificação
        item("identity") {
            Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Pill(establishment.categoryDetail, icon = Icons.Filled.Restaurant)
                    Pill(
                        "${establishment.neighborhood} • ${establishment.city.substringBefore(" - ")}",
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                    )
                }
                Text(
                    establishment.name,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.semantics { heading() },
                )
                Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Outlined.LocationOn, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                    Text(
                        "${establishment.address}, ${establishment.city}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    PrimaryButton(
                        text = "Como chegar",
                        leadingIcon = Icons.Outlined.Directions,
                        onClick = onDirections,
                        modifier = Modifier.semantics {
                            contentDescription = "Como chegar ao ${establishment.name} usando GPS externo"
                        },
                    )
                    SquareIconButton(
                        icon = Icons.Outlined.Share,
                        contentDescription = "Compartilhar informações deste local",
                        onClick = onShare,
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    )
                }
            }
        }

        // 2 — Selo geral + abas de seção
        item("seal") {
            Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                AccessibilitySeal(establishment)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val tabs = listOf("Checklist Completo", "Fotos de Acessibilidade", "Relatos", "Localização")
                    itemsIndexed(tabs) { index, label ->
                        val selected = index == selectedTab
                        Surface(
                            onClick = {
                                selectedTab = index
                                scrollTo(listState, listOf(checklistIndex, galleryIndex, reviewsIndex, locationIndex)[index])
                            },
                            shape = RoundedCornerShape(8.dp),
                            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHigh,
                            contentColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.height(40.dp),
                        ) {
                            Box(Modifier.padding(horizontal = 16.dp), contentAlignment = Alignment.Center) {
                                Text(label, style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold))
                            }
                        }
                    }
                }
            }
        }

        // 3 — Checklist
        item("checklist-header") {
            Column(Modifier.padding(horizontal = 16.dp, vertical = 4.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "Recursos de Acessibilidade Verificados",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.semantics { heading() },
                )
                Text(
                    "Conferência in loco realizada pela comunidade e auditores independentes.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        categories.forEachIndexed { index, (category, features) ->
            item("cat-${category.name}") {
                CategoryCard(index + 1, category, features, Modifier.padding(horizontal = 16.dp))
            }
        }

        // Galeria
        item("gallery-header") {
            Column(Modifier.padding(horizontal = 16.dp, vertical = 4.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Galeria de Verificação Visual",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f).semantics { heading() },
                    )
                    Text(
                        "${establishment.photos.size} registros",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text(
                    if (establishment.photos.isEmpty()) "Ainda não há fotos de verificação para este local. Envie a primeira ao avaliar!"
                    else "Fotos tiradas pela comunidade e auditores para verificação de detalhes estruturais.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        establishment.photos.forEach { photo ->
            item("photo-${photo.id}") { PhotoCard(photo, Modifier.padding(horizontal = 16.dp)) }
        }

        // Relatos
        item("reviews-header") {
            Column(Modifier.padding(horizontal = 16.dp, vertical = 4.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "Relatos da comunidade",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.semantics { heading() },
                )
                Text(
                    if (reviews.isEmpty()) "Nenhum relato ainda. Seja a primeira pessoa a avaliar este local."
                    else "${reviews.size} relato(s) publicados sobre este local.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        reviews.forEachIndexed { index, review ->
            item("review-${review.id}") {
                ReviewCard(
                    review = review,
                    avatarIndex = index,
                    showEstablishment = false,
                    onToggleHelpful = { onToggleHelpful(review.id) },
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        }

        // Localização
        item("location") {
            LocationSection(establishment, Modifier.padding(horizontal = 16.dp))
        }
    }
}

@Composable
private fun HeroImage(establishment: Establishment) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(256.dp)
    ) {
        AuraImage(
            model = establishment.heroImageUrl ?: establishment.imageUrl,
            contentDescription = "Fachada de ${establishment.name}",
            modifier = Modifier.fillMaxSize(),
        )
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color.Transparent, MaterialTheme.colorScheme.background.copy(alpha = 0.6f))
                    )
                )
        )
        Row(
            Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = 0.95f))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(Icons.Filled.Verified, null, tint = Color(0xFF0D57D5), modifier = Modifier.size(18.dp))
            Text(
                "Auditado em ${establishment.verifiedAt}",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF161C25),
            )
        }
    }
}

@Composable
private fun AccessibilitySeal(establishment: Establishment) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(16.dp)
            .semantics(mergeDescendants = true) {},
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(AuraTheme.extraColors.brand),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.AutoMirrored.Filled.Accessible, null, tint = MaterialTheme.colorScheme.surface, modifier = Modifier.size(24.dp))
            }
            Column(Modifier.weight(1f)) {
                Text(
                    "AVALIAÇÃO TÉCNICA COLETIVA",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    establishment.conformityLevel.headline,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${establishment.conformityPercent}%",
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 26.sp),
                    color = MaterialTheme.colorScheme.primary,
                )
                Text("conforme", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        ConformityBar(establishment.conformityPercent)
        Text(establishment.summary, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun CategoryCard(
    number: Int,
    category: FeatureCategory,
    features: List<AccessibilityFeature>,
    modifier: Modifier = Modifier,
) {
    SectionCard(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(category.icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp))
            Text(
                "$number. ${category.displayName}",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.semantics { heading() },
            )
        }
        features.forEach { FeatureItem(it) }
    }
}

@Composable
private fun FeatureItem(feature: AccessibilityFeature) {
    val unavailable = feature.status == FeatureStatus.UNAVAILABLE
    val partial = feature.status == FeatureStatus.PARTIAL
    val containerColor = when {
        unavailable -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
        else -> MaterialTheme.colorScheme.surfaceContainerLow
    }
    val iconBg = when {
        unavailable -> MaterialTheme.colorScheme.error
        partial -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.primary
    }
    val badgeText = feature.badge ?: when (feature.status) {
        FeatureStatus.AVAILABLE -> "Disponível"
        FeatureStatus.PARTIAL -> "Parcial"
        FeatureStatus.UNAVAILABLE -> "Não disponível"
        FeatureStatus.UNKNOWN -> "Sem informação"
    }
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(containerColor)
            .padding(12.dp)
            .semantics(mergeDescendants = true) {
                contentDescription = "${feature.title}. ${feature.status.displayName}. ${feature.badge ?: ""}. ${feature.description}"
            },
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            Modifier
                .padding(top = 2.dp)
                .size(32.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                when {
                    unavailable -> Icons.Filled.Close
                    partial -> Icons.Filled.PriorityHigh
                    else -> Icons.Filled.Check
                },
                null,
                tint = MaterialTheme.colorScheme.surface,
                modifier = Modifier.size(18.dp),
            )
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(feature.title, style = MaterialTheme.typography.labelLarge, modifier = Modifier.weight(1f))
                Pill(
                    badgeText,
                    shape = RoundedCornerShape(2.dp),
                    containerColor = if (unavailable) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer,
                    contentColor = if (unavailable) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onPrimaryContainer,
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                )
            }
            Text(feature.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun PhotoCard(photo: EstablishmentPhoto, modifier: Modifier = Modifier) {
    SectionCard(modifier = modifier, contentPadding = PaddingValues(0.dp), verticalArrangement = Arrangement.Top) {
        AuraImage(
            model = photo.imageUrl,
            contentDescription = photo.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(192.dp),
        )
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(Icons.Outlined.PhotoCamera, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 2.dp).size(18.dp))
                Text(
                    photo.title,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            Text(photo.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun LocationSection(establishment: Establishment, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "Localização & Entorno",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.semantics { heading() },
        )
        establishment.surroundings?.let {
            Text(it, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Box(
            Modifier
                .fillMaxWidth()
                .height(176.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            AuraImage(
                model = Constants.drawableUri("img_mapa_sao_paulo"),
                contentDescription = "Mapa da região de ${establishment.neighborhood}",
                modifier = Modifier.fillMaxSize(),
            )
            establishment.nearbyTransit?.let { transit ->
                Row(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(12.dp)
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(4.dp))
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(Icons.Outlined.DirectionsSubway, null, tint = MaterialTheme.colorScheme.primary)
                    Text(transit, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}

@Composable
private fun QuickActionsBar(
    isFavorite: Boolean,
    name: String,
    onToggleFavorite: () -> Unit,
    onReview: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SquareIconButton(
                icon = if (isFavorite) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                contentDescription = if (isFavorite) "Remover $name dos favoritos" else "Salvar $name nos favoritos",
                onClick = onToggleFavorite,
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            )
            PrimaryButton(
                text = "Avaliar este local",
                leadingIcon = Icons.Outlined.RateReview,
                onClick = onReview,
                modifier = Modifier
                    .weight(1f)
                    .semantics { contentDescription = "Abrir formulário para avaliar este local" },
            )
        }
    }
}

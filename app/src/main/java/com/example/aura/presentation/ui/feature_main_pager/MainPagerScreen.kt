package com.example.aura.presentation.ui.feature_main_pager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.aura.di.AppContainer
import com.example.aura.presentation.navigation.BottomNavBarItem
import com.example.aura.presentation.navigation.LocalAppActions
import com.example.aura.presentation.navigation.LocalReduceMotion
import com.example.aura.presentation.ui.components.AuraTopBar
import com.example.aura.presentation.ui.feature_home.HomeScreen
import com.example.aura.presentation.ui.feature_profile.ProfileScreen
import com.example.aura.presentation.ui.feature_review.ReviewScreen
import com.example.aura.presentation.ui.feature_search.SearchScreen
import com.example.aura.presentation.ui.feature_search.SearchViewModel
import com.example.aura.presentation.ui.feature_search.SearchViewModelFactory
import kotlinx.coroutines.launch

val bottomNavBarItems = BottomNavBarItem.items

@Composable
fun MainPagerScreen(
    container: AppContainer,
    navController: NavHostController,
    initialTab: BottomNavBarItem = BottomNavBarItem.HomeNavBarItem,
) {
    val initialIndex = bottomNavBarItems.indexOf(initialTab).coerceAtLeast(0)
    var selectedItem by remember { mutableStateOf(bottomNavBarItems[initialIndex]) }
    val pageState = rememberPagerState(initialPage = initialIndex) { bottomNavBarItems.size }
    val coroutineScope = rememberCoroutineScope()
    val reduceMotion = LocalReduceMotion.current

    // ViewModel de busca compartilhado: a tela Início dispara buscas que aparecem na aba Buscar.
    val searchViewModel: SearchViewModel = viewModel(
        factory = SearchViewModelFactory(container.establishmentUseCases, container.userUseCases)
    )

    fun navigate(toItem: BottomNavBarItem) {
        selectedItem = toItem
        val targetIndex = bottomNavBarItems.indexOf(toItem)
        coroutineScope.launch {
            if (reduceMotion) pageState.scrollToPage(targetIndex) else pageState.animateScrollToPage(targetIndex)
        }
    }

    val parentActions = LocalAppActions.current
    val actions = remember(parentActions) {
        parentActions.copy(openProfile = { navigate(BottomNavBarItem.ProfileNavBarItem) })
    }

    CompositionLocalProvider(LocalAppActions provides actions) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = { AuraTopBar() },
            bottomBar = {
                AuraBottomBar(
                    selectedItem = selectedItem,
                    onItemChanged = { item -> navigate(item) }
                )
            }
        ) { paddingValues ->
            HorizontalPager(
                state = pageState,
                userScrollEnabled = false,
                beyondViewportPageCount = 1,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            ) { page ->
                when (bottomNavBarItems[page]) {
                    BottomNavBarItem.HomeNavBarItem -> HomeScreen(
                        container = container,
                        navController = navController,
                        onSearch = { query, filters ->
                            searchViewModel.applyExternalSearch(query, filters)
                            navigate(BottomNavBarItem.SearchNavBarItem)
                        },
                        swipeNavigate = { navigate(it) },
                    )

                    BottomNavBarItem.SearchNavBarItem -> SearchScreen(
                        viewModel = searchViewModel,
                        navController = navController,
                        onContribute = { navigate(BottomNavBarItem.ReviewNavBarItem) },
                    )

                    BottomNavBarItem.ReviewNavBarItem -> ReviewScreen(
                        container = container,
                        navController = navController,
                        initialEstablishmentId = null,
                        standalone = false,
                        onPublished = { navigate(BottomNavBarItem.HomeNavBarItem) },
                    )

                    BottomNavBarItem.ProfileNavBarItem -> ProfileScreen(
                        container = container,
                        navController = navController,
                    )
                }
            }
        }
    }
}

/** Barra de navegação inferior ("Navegação Principal" no Figma). */
@Composable
fun AuraBottomBar(
    selectedItem: BottomNavBarItem,
    onItemChanged: (BottomNavBarItem) -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .shadow(12.dp)
            .background(MaterialTheme.colorScheme.surface)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(horizontal = 8.dp)
                .selectableGroup(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            bottomNavBarItems.forEach { item ->
                val selected = item.key == selectedItem.key
                val color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(vertical = 8.dp, horizontal = 4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .selectable(selected = selected, role = Role.Tab, onClick = { onItemChanged(item) })
                        .semantics { contentDescription = item.description },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
                ) {
                    Icon(item.icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
                    Text(
                        item.label,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold
                        ),
                        color = color,
                    )
                }
            }
        }
    }
}

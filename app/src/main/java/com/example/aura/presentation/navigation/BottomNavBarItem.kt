package com.example.aura.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.ManageAccounts
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavBarItem(
    val key: String,
    val label: String,
    val description: String,
    val icon: ImageVector,
) {
    object HomeNavBarItem : BottomNavBarItem(
        key = "home",
        label = "Início",
        description = "Início e explorar locais",
        icon = Icons.Outlined.Explore,
    )

    object SearchNavBarItem : BottomNavBarItem(
        key = "search",
        label = "Buscar",
        description = "Buscar estabelecimentos com filtros",
        icon = Icons.Outlined.Search,
    )

    object ReviewNavBarItem : BottomNavBarItem(
        key = "review",
        label = "Avaliar",
        description = "Adicionar avaliação de acessibilidade",
        icon = Icons.Outlined.RateReview,
    )

    object ProfileNavBarItem : BottomNavBarItem(
        key = "profile",
        label = "Perfil",
        description = "Perfil e preferências de acessibilidade",
        icon = Icons.Outlined.ManageAccounts,
    )

    companion object {
        val items: List<BottomNavBarItem>
            get() = listOf(HomeNavBarItem, SearchNavBarItem, ReviewNavBarItem, ProfileNavBarItem)

        fun fromKey(key: String?): BottomNavBarItem = items.firstOrNull { it.key == key } ?: HomeNavBarItem
    }
}

package com.example.aura.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.aura.presentation.icons.House
import com.example.aura.presentation.navigation.destinations.homeRoute
import com.example.aura.presentation.navigation.destinations.newsRoute
import com.example.aura.presentation.navigation.destinations.profileRoute
import com.example.aura.presentation.navigation.destinations.settingsRoute

sealed class BottomNavBarItem(
    val label: String,
    val icon: ImageVector,
    val route: String,
) {
    object HomeNavBarItem : BottomNavBarItem(
        label = "Início",
        icon = House,
        route = homeRoute,
    )
    object ExamNavBarItem : BottomNavBarItem(
        label = "Exames",
        icon = Icons.AutoMirrored.Outlined.ReceiptLong,
        route = profileRoute,
    )

    object ProfileNavBarItem : BottomNavBarItem(
        label = "Perfil",
        icon = Icons.Outlined.AccountCircle,
        route = profileRoute,
    )
    object SettingsNavBarItem : BottomNavBarItem(
        label = "Config.",
        icon = Icons.Outlined.Settings,
        route = settingsRoute,
    )

    object NewsNavBarItem : BottomNavBarItem(
        label = "Notícias",
        icon = Icons.Outlined.Newspaper,
        route = newsRoute
    )
}
package com.example.aura.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.aura.presentation.icons.House
import com.example.aura.presentation.navigation.destinations.homeRoute
import com.example.aura.presentation.navigation.destinations.profileRoute

sealed class BottomNavBarItem(
    val label: String,
    val icon: ImageVector,
    val route: String,
) {
    object HomeNavBarItem : BottomNavBarItem(
        label = "Home",
        icon = House,
        route = homeRoute,
    )

    object ProfileNavBarItem : BottomNavBarItem(
        label = "Perfil",
        icon = Icons.Outlined.AccountCircle,
        route = profileRoute,
    )
}
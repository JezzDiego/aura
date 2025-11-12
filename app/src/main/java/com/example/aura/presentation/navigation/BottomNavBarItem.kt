package com.example.aura.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.aura.presentation.icons.House

sealed class BottomNavBarItem(
    val label: String,
    val icon: ImageVector,
) {
    object HomeNavBarItem : BottomNavBarItem(
        label = "Início",
        icon = House,
    )
    object ExamNavBarItem : BottomNavBarItem(
        label = "Exames",
        icon = Icons.AutoMirrored.Outlined.ReceiptLong,
    )
    object ProfileNavBarItem : BottomNavBarItem(
        label = "Perfil",
        icon = Icons.Outlined.AccountCircle,
    )
}
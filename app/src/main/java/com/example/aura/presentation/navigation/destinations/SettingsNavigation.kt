package com.example.aura.presentation.navigation.destinations

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.aura.presentation.ui.feature_profile.ProfileScreen

const val settingsRoute = "settingsRoute"


fun NavGraphBuilder.settingsScreen(navController: NavController) {
    composable(settingsRoute) {
        ProfileScreen()
    }
}

fun NavController.navigateToSettingsScreen() {
    navigate(settingsRoute)
}
package com.example.aura.presentation.navigation.destinations

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aura.presentation.ui.feature_settings.SettingsScreen
import com.example.aura.presentation.ui.feature_settings.SettingsViewModel

const val settingsRoute = "settingsRoute"


fun NavGraphBuilder.settingsScreen(navController: NavController) {
    composable(settingsRoute) {
        val vm: SettingsViewModel = viewModel()
        SettingsScreen(viewModel = vm)
    }
}

fun NavController.navigateToSettingsScreen() {
    navigate(settingsRoute)
}
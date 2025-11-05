package com.example.aura.presentation.navigation.destinations

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.aura.di.AppContainer
import com.example.aura.presentation.ui.feature_home.HomeScreen

const val homeRoute = "homeRoute"


fun NavGraphBuilder.homeScreen(navController: NavController, container: AppContainer) {
    composable(homeRoute) {
        HomeScreen(container = container)
    }
}

fun NavController.navigateToHomeScreen() {
    navigate(homeRoute)
}
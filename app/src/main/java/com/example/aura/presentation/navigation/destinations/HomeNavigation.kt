package com.example.aura.presentation.navigation.destinations

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.aura.di.AppContainer
import com.example.aura.presentation.navigation.BottomNavBarItem
import com.example.aura.presentation.ui.feature_home.HomeScreen

const val homeRoute = "homeRoute"


fun NavGraphBuilder.homeScreen(
    container: AppContainer,
    swipeNavigate: (BottomNavBarItem) -> Unit,
    onNavigateToAddExam: () -> Unit
) {
    composable(homeRoute) {
        HomeScreen(
            container = container,
            swipeNavigate = swipeNavigate,
            onNavigateToAddExam = onNavigateToAddExam
        )
    }
}

fun NavController.navigateToHomeScreen() {
    navigate(homeRoute)
}
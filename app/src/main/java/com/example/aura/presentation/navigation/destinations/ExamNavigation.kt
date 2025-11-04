package com.example.aura.presentation.navigation.destinations

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.aura.presentation.ui.feature_profile.ProfileScreen

const val examRoute = "examRoute"


fun NavGraphBuilder.examScreen(navController: NavController) {
    composable(examRoute) {
        ProfileScreen()
    }
}

fun NavController.navigateToExamScreen() {
    navigate(examRoute)
}
package com.example.aura.presentation.navigation.destinations

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.aura.presentation.ui.feature_profile.ProfileScreen

const val profileRoute = "profileRoute"


fun NavGraphBuilder.profileScreen(navController: NavController) {
    composable(profileRoute) {
        ProfileScreen()
    }
}

fun NavController.navigateToProfileScreen() {
    navigate(profileRoute)
}
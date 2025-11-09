package com.example.aura.presentation.navigation.destinations

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.aura.presentation.ui.feature_profile.ProfileScreen
import com.example.aura.di.AppContainer

const val profileRoute = "profileRoute"


fun NavGraphBuilder.profileScreen(navController: NavController, container: AppContainer) {
    composable(profileRoute) {
        ProfileScreen(container)
    }
}

fun NavController.navigateToProfileScreen() {
    navigate(profileRoute)
}
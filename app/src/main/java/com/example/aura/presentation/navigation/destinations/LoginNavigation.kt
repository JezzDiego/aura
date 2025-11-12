package com.example.aura.presentation.navigation.destinations

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.aura.di.AppContainer
import com.example.aura.presentation.ui.feature_login.LoginScreen

const val loginRoute = "loginRoute"


fun NavGraphBuilder.loginScreen(container: AppContainer, navController: NavHostController) {
    composable(loginRoute) {
        LoginScreen(container, navController)
    }
}

fun NavController.navigateToLoginScreenWithPopUp() {
    navigate(loginRoute) {
        popUpTo(0) {
            inclusive = true
        }
    }
}
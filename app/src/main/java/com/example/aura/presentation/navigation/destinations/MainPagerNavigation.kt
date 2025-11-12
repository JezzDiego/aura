package com.example.aura.presentation.navigation.destinations

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.aura.di.AppContainer
import com.example.aura.presentation.ui.feature_main_pager.MainPagerScreen

const val mainPagerRoute = "mainPagerRoute"


fun NavGraphBuilder.mainPagerScreen(container: AppContainer, navController: NavHostController) {
    composable(mainPagerRoute) {
        MainPagerScreen(container, navController)
    }
}

fun NavController.navigateToMainPagerScreenWithPopUpToLoginScreen() {
    navigate(mainPagerRoute) {
        popUpTo(loginRoute) {
            inclusive = true
        }
    }
}
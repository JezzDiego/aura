package com.example.aura.presentation.navigation.destinations

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.aura.di.AppContainer
import com.example.aura.presentation.navigation.BottomNavBarItem
import com.example.aura.presentation.ui.feature_main_pager.MainPagerScreen

const val mainPagerBase = "mainPagerRoute"
const val mainPagerRoute = "$mainPagerBase?tab={tab}"

fun NavGraphBuilder.mainPagerScreen(container: AppContainer, navController: NavHostController) {
    composable(
        route = mainPagerRoute,
        arguments = listOf(navArgument("tab") {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) { backStackEntry ->
        val initialTab = BottomNavBarItem.fromKey(backStackEntry.arguments?.getString("tab"))
        MainPagerScreen(container, navController, initialTab)
    }
}

fun NavController.navigateToMainPagerScreenWithPopUpToLoginScreen() {
    navigate(mainPagerBase) {
        popUpTo(loginRoute) {
            inclusive = true
        }
    }
}

/** Volta para o pager principal (limpando a pilha) já na aba desejada. */
fun NavController.navigateToMainPagerTab(tab: BottomNavBarItem) {
    navigate("$mainPagerBase?tab=${tab.key}") {
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
    }
}

package com.example.aura.presentation.navigation.destinations

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.aura.di.AppContainer
import com.example.aura.presentation.ui.feature_news.details.NewsDetailsScreen

const val newsDetailsRoute = "newsDetailsRoute/{newsId}"
const val newsDetailsBase = "newsDetailsRoute"

fun NavGraphBuilder.newsDetailsScreen(container: AppContainer, navController: NavHostController) {
    composable(
        route = newsDetailsRoute,
        arguments = listOf(navArgument("newsId") { type = NavType.StringType })
    ) { backStackEntry ->
        val newsId = backStackEntry.arguments?.getString("newsId") ?: ""
        NewsDetailsScreen(container, navController, newsId)
    }
}

fun NavController.navigateToNewsDetailsScreen(newsId: String) {
    navigate("$newsDetailsBase/$newsId")
}
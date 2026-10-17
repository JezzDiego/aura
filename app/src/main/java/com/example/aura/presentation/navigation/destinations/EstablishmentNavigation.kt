package com.example.aura.presentation.navigation.destinations

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.aura.di.AppContainer
import com.example.aura.presentation.ui.feature_establishment.details.EstablishmentDetailsScreen
import com.example.aura.presentation.ui.feature_review.ReviewScreen

const val establishmentDetailsBase = "establishmentDetailsRoute"
const val establishmentDetailsRoute = "$establishmentDetailsBase/{establishmentId}"

const val reviewBase = "reviewRoute"
const val reviewRoute = "$reviewBase?establishmentId={establishmentId}"

fun NavGraphBuilder.establishmentDetailsScreen(container: AppContainer, navController: NavHostController) {
    composable(
        route = establishmentDetailsRoute,
        arguments = listOf(navArgument("establishmentId") { type = NavType.StringType })
    ) { backStackEntry ->
        val id = backStackEntry.arguments?.getString("establishmentId").orEmpty()
        EstablishmentDetailsScreen(container, navController, id)
    }
}

/** Formulário "Avaliar este local" aberto a partir dos detalhes (fora do pager). */
fun NavGraphBuilder.reviewScreen(container: AppContainer, navController: NavHostController) {
    composable(
        route = reviewRoute,
        arguments = listOf(navArgument("establishmentId") {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) { backStackEntry ->
        val id = backStackEntry.arguments?.getString("establishmentId")
        ReviewScreen(
            container = container,
            navController = navController,
            initialEstablishmentId = id,
            standalone = true,
        )
    }
}

fun NavController.navigateToEstablishmentDetails(establishmentId: String) {
    navigate("$establishmentDetailsBase/$establishmentId")
}

fun NavController.navigateToReview(establishmentId: String?) {
    navigate(if (establishmentId != null) "$reviewBase?establishmentId=$establishmentId" else reviewBase)
}

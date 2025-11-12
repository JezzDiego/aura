package com.example.aura.presentation.navigation.destinations

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.aura.di.AppContainer
import com.example.aura.presentation.ui.feature_medication.MedicationScreen
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut

const val medicationRoute = "medicationRoute"


fun NavGraphBuilder.medicationScreen(navController: NavController, container: AppContainer) {
    composable(
        medicationRoute,
        enterTransition = {
            fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(300))
        }
    ) {
        MedicationScreen(navController = navController, container = container)
    }
}

fun NavController.navigateToMedicationScreen() {
    navigate(medicationRoute)
}
package com.example.aura.presentation.navigation.destinations

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.aura.di.AppContainer
import com.example.aura.presentation.ui.feature_medication.MedicationScreen

const val medicationRoute = "medicationRoute"

fun NavGraphBuilder.medicationScreen(navController: NavController, container: AppContainer) {
    composable(medicationRoute) {
        MedicationScreen(navController = navController, container = container)
    }
}

fun NavController.navigateToMedicationScreen() {
    navigate(medicationRoute)
}
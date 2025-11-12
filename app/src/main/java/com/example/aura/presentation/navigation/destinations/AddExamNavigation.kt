package com.example.aura.presentation.navigation.destinations

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.aura.di.AppContainer
import com.example.aura.presentation.ui.feature_add_exam.AddExamScreen

const val addExamRoute = "addExamRoute"

fun NavGraphBuilder.addExamScreen(navController: NavHostController, container: AppContainer) {
    composable(addExamRoute) {
        AddExamScreen(container = container, navController = navController)
    }
}

fun NavController.navigateToAddExamScreen() {
    navigate(addExamRoute)
}

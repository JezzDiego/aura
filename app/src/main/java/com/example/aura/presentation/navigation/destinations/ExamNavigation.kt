package com.example.aura.presentation.navigation.destinations

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.aura.di.AppContainer
import com.example.aura.presentation.ui.feature_exam.ExamScreen

const val examRoute = "examRoute"


fun NavGraphBuilder.examScreen(navController: NavController, container: AppContainer) {
    composable(examRoute) {
        ExamScreen(container)
    }
}

fun NavController.navigateToExamScreen() {
    navigate(examRoute)
}
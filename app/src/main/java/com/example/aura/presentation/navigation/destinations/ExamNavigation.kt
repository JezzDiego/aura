package com.example.aura.presentation.navigation.destinations

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.aura.di.AppContainer
import com.example.aura.presentation.ui.feature_exam.details.ExamDetailsScreen

const val examDetailsRoute = "examDetailsRoute"


fun NavGraphBuilder.examDetailsScreen(container: AppContainer, navController: NavHostController) {
    composable(examDetailsRoute) {
        ExamDetailsScreen(container)
    }
}

fun NavController.navigateToExamDetailsScreen() {
    navigate(examDetailsRoute)
}
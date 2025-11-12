package com.example.aura.presentation.navigation.destinations

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.aura.di.AppContainer
import com.example.aura.presentation.ui.feature_exam.ExamScreen

const val examRoute = "examRoute"

fun NavGraphBuilder.examScreen(
    container: AppContainer,
    onNavigateToAddExam: () -> Unit
) {

    composable(examRoute) {
        ExamScreen(
            container = container,
            onNavigateToAddExam = onNavigateToAddExam
        )
    }
}

fun NavController.navigateToExamScreen() {
    navigate(examRoute)
}
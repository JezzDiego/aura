package com.example.aura.presentation.navigation.destinations

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import com.example.aura.di.AppContainer
import com.example.aura.presentation.ui.feature_exam.add_exam.AddExamScreen
import com.example.aura.presentation.ui.feature_exam.details.ExamDetailsScreen

const val addExamRoute = "addExamRoute"
const val examDetailsBase = "examDetailsRoute"
const val examDetailsRoute = "examDetailsRoute/{examId}"

fun NavGraphBuilder.addExamScreen(container: AppContainer, navController: NavHostController) {
    composable(addExamRoute) {
        AddExamScreen(container, navController)
    }
}

fun NavController.navigateToAddExamScreen() {
    navigate(addExamRoute)
}

fun NavGraphBuilder.examDetailsScreen(container: AppContainer, navController: NavHostController) {
    composable(
        route = examDetailsRoute,
        arguments = listOf(navArgument("examId") { type = NavType.StringType })
    ) { backStackEntry ->
        val examId = backStackEntry.arguments?.getString("examId") ?: ""
        ExamDetailsScreen(container, navController, examId)
    }
}

fun NavController.navigateToExamDetailsScreen(examId: String) {
    navigate("$examDetailsBase/$examId")
}
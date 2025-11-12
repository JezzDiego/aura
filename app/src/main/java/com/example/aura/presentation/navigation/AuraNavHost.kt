package com.example.aura.presentation.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.aura.di.AppContainer
import com.example.aura.presentation.navigation.destinations.addExamScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import com.example.aura.presentation.navigation.destinations.examDetailsScreen
import com.example.aura.presentation.navigation.destinations.loginScreen
import com.example.aura.presentation.navigation.destinations.mainPagerScreen
import com.example.aura.presentation.navigation.destinations.loginRoute
import com.example.aura.presentation.navigation.destinations.mainPagerRoute
import com.example.aura.presentation.navigation.destinations.medicationScreen
import com.example.aura.presentation.navigation.destinations.newsDetailsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuraNavHost(navController: NavHostController, container: AppContainer) {

    var startDestination by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val user = withContext(Dispatchers.IO) {
            container.userUseCases.getLocalUser().firstOrNull()
        }

        startDestination = if (user != null) mainPagerRoute else loginRoute
    }

    startDestination?.let {
        NavHost(
            navController = navController,
            startDestination = it,
        ) {

            loginScreen(container, navController)
            mainPagerScreen(container, navController)

            addExamScreen(container, navController)
            examDetailsScreen(container, navController)

            newsDetailsScreen(container, navController)

            medicationScreen(container, navController)
        }
    }
}
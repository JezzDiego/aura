package com.example.aura.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.aura.di.AppContainer
import com.example.aura.presentation.navigation.destinations.establishmentDetailsScreen
import com.example.aura.presentation.navigation.destinations.loginRoute
import com.example.aura.presentation.navigation.destinations.loginScreen
import com.example.aura.presentation.navigation.destinations.mainPagerRoute
import com.example.aura.presentation.navigation.destinations.mainPagerScreen
import com.example.aura.presentation.navigation.destinations.navigateToMainPagerTab
import com.example.aura.presentation.navigation.destinations.reviewScreen
import com.example.aura.presentation.ui.components.AccessibilitySettings
import com.example.aura.presentation.ui.components.AccessibilitySettingsSheet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AuraNavHost(navController: NavHostController, container: AppContainer) {

    var startDestination by remember { mutableStateOf<String?>(null) }
    var showAccessibilitySheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val highContrast by container.preferences.isDarkMode.collectAsState(initial = false)
    val largeText by container.preferences.isLargeText.collectAsState(initial = false)
    val reduceMotion by container.preferences.isReduceMotion.collectAsState(initial = false)

    LaunchedEffect(Unit) {
        val user = withContext(Dispatchers.IO) {
            container.userUseCases.getLocalUser().firstOrNull()
        }
        startDestination = if (user != null) mainPagerRoute else loginRoute
    }

    val actions = remember(navController) {
        AppActions(
            openAccessibilitySettings = { showAccessibilitySheet = true },
            openProfile = { navController.navigateToMainPagerTab(BottomNavBarItem.ProfileNavBarItem) },
        )
    }

    CompositionLocalProvider(
        LocalAppActions provides actions,
        LocalReduceMotion provides reduceMotion,
    ) {
        Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            startDestination?.let {
                NavHost(
                    navController = navController,
                    startDestination = it,
                    enterTransition = { if (reduceMotion) EnterTransition.None else fadeIn() },
                    exitTransition = { if (reduceMotion) ExitTransition.None else fadeOut() },
                ) {
                    loginScreen(container, navController)

                    mainPagerScreen(container, navController)

                    establishmentDetailsScreen(container, navController)

                    reviewScreen(container, navController)
                }
            }
        }

        if (showAccessibilitySheet) {
            AccessibilitySettingsSheet(
                settings = AccessibilitySettings(highContrast, largeText, reduceMotion),
                onHighContrastChange = { scope.launch { container.preferences.setDarkMode(it) } },
                onLargeTextChange = { scope.launch { container.preferences.setLargeText(it) } },
                onReduceMotionChange = { scope.launch { container.preferences.setReduceMotion(it) } },
                onDismiss = { showAccessibilitySheet = false },
            )
        }
    }
}

/** true quando a pessoa pediu para reduzir animações. */
val LocalReduceMotion = androidx.compose.runtime.staticCompositionLocalOf { false }

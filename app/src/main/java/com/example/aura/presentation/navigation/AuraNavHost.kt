package com.example.aura.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.aura.presentation.ui.feature_exam.ExamScreen
import com.example.aura.presentation.ui.feature_home.HomeScreen
import com.example.aura.presentation.ui.feature_profile.ProfileScreen
import kotlinx.serialization.Serializable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aura.presentation.ui.components.SearchBar
import com.example.aura.presentation.ui.feature_login.LoginScreen
import com.example.aura.presentation.ui.feature_settings.SettingsScreen
import kotlinx.coroutines.launch
import com.example.aura.di.AppContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

@Serializable
object LoginRoute

@Serializable
object HomeRoute

@Serializable
object ExamRoute

val bottomNavBarItems = listOf(
    BottomNavBarItem.HomeNavBarItem,
    BottomNavBarItem.ExamNavBarItem,
    BottomNavBarItem.ProfileNavBarItem,
    BottomNavBarItem.SettingsNavBarItem,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuraNavHost(navController: NavHostController, container: AppContainer) {

    var startDestination by remember { mutableStateOf<Any?>(null) }

    LaunchedEffect(Unit) {
        val user = withContext(Dispatchers.IO) {
            container.userDao.getUser().firstOrNull()
        }

        startDestination = if (user != null) HomeRoute else LoginRoute

    }

    startDestination?.let {
        NavHost(navController = navController, startDestination = it) {

            composable<LoginRoute> {
                LoginScreen(
                    container = container,
                    onLoginSuccess = {
                        navController.navigate(HomeRoute) {
                            popUpTo(LoginRoute) {
                                inclusive = true
                            }
                        }


                    }
                )
            }

            composable<HomeRoute> {
                var selectedItem by remember {
                    val item = bottomNavBarItems.first()
                    mutableStateOf(item)
                }

                val pageState = rememberPagerState {
                    bottomNavBarItems.size
                }

                LaunchedEffect(selectedItem) {
                    val currentIndex = bottomNavBarItems.indexOf(selectedItem)
                    pageState.animateScrollToPage(currentIndex)
                }

                LaunchedEffect(pageState.targetPage) {
                    selectedItem = bottomNavBarItems[pageState.targetPage]
                }

                val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()


                Scaffold(
                    topBar = {
                        TopAppBar(
                            modifier = Modifier,
                            title = {},
                            actions = {
                                Row(
                                    modifier = Modifier
                                        .padding(
                                            top = 12.dp,
                                            bottom = 4.dp
                                        )
                                        .background(
                                            color = Color.Transparent
                                        )
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Absolute.Center
                                ) {
                                    SearchBar()
                                }
                            },
                            scrollBehavior = scrollBehavior
                        )
                    },

                    bottomBar = {
                        BottomNavBar(
                            selectedItem = selectedItem,
                            onItemChanged = { item ->
                                selectedItem = item
                            }
                        )
                    }
                ) { paddingValues ->

                    Surface(
                        modifier = Modifier
                            .padding(paddingValues)
                    ) {
                        HorizontalPager(pageState) { page ->
                            val item = bottomNavBarItems[page]
                            val coroutineScope = rememberCoroutineScope()

                            fun swipeNavigate(toItem: BottomNavBarItem) {
                                val targetIndex = bottomNavBarItems.indexOf(toItem)
                                coroutineScope.launch {
                                    pageState.animateScrollToPage(targetIndex)
                                }

                            }

                            when (item) {
                                BottomNavBarItem.HomeNavBarItem -> HomeScreen(
                                    container = container,
                                    swipeNavigate = { toItem -> swipeNavigate(toItem) }
                                )

                                BottomNavBarItem.ExamNavBarItem -> ExamScreen(
                                    container = container
                                )

                                BottomNavBarItem.ProfileNavBarItem -> ProfileScreen(container)
                                BottomNavBarItem.SettingsNavBarItem -> SettingsScreen(
                                    navController = navController,
                                    userRepository = container.userRepository,
                                    onLogoutSucess = {
                                        navController.navigate(LoginRoute) {
                                            popUpTo(0) {
                                                inclusive = true
                                            }
                                        }


                                    }
                                )
                            }
                        }
                    }

                }
            }

            composable<ExamRoute> {
                ExamScreen(container = container)
            }
        }
    }
}

@Composable
fun BottomNavBar(
    selectedItem: BottomNavBarItem,
    onItemChanged: (BottomNavBarItem) -> Unit,
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ){
        bottomNavBarItems.forEach { item ->
            NavigationBarItem(
                selected = selectedItem.label == item.label, onClick = {
                    onItemChanged(item)
                },
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = "",
                        modifier = Modifier
                            .size(
                                22.dp
                            )
                    )
                },
                label = { Text(item.label, fontSize = 12.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    }
}

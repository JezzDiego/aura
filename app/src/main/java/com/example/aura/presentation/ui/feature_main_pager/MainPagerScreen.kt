package com.example.aura.presentation.ui.feature_main_pager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.aura.di.AppContainer
import com.example.aura.presentation.navigation.BottomNavBarItem
import com.example.aura.presentation.ui.components.FullScreenSearchBar
import com.example.aura.presentation.ui.feature_exam.ExamScreen
import com.example.aura.presentation.ui.feature_home.HomeScreen
import com.example.aura.presentation.ui.feature_profile.ProfileScreen
import kotlinx.coroutines.launch

val bottomNavBarItems = listOf(
    BottomNavBarItem.HomeNavBarItem,
    BottomNavBarItem.ExamNavBarItem,
    BottomNavBarItem.ProfileNavBarItem,
)

@Composable
fun MainPagerScreen(container: AppContainer, navController: NavHostController) {
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

    Scaffold(
        topBar = {
            SearchTopAppBar(container, navController)
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
                        swipeNavigate = { toItem -> swipeNavigate(toItem) },
                        navController = navController
                    )

                    BottomNavBarItem.ExamNavBarItem -> ExamScreen(
                        container = container,
                        navController = navController
                    )

                    BottomNavBarItem.ProfileNavBarItem -> ProfileScreen(
                        container = container,
                        navController = navController
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopAppBar(container: AppContainer, navController: NavHostController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

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
                FullScreenSearchBar(container, navController)
            }
        },
        scrollBehavior = scrollBehavior
    )
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

package com.example.aura.presentation.navigation

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.aura.presentation.ui.feature_home.HomeScreen
import com.example.aura.presentation.ui.feature_profile.ProfileScreen
import kotlinx.serialization.Serializable

@Serializable
object NavPager

val bottomNavBarItems = listOf(
    BottomNavBarItem.HomeNavBarItem,
    BottomNavBarItem.ProfileNavBarItem,
)

@Composable
fun AuraNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavPager) {
        composable<NavPager> {
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
                        when (item) {
                            BottomNavBarItem.HomeNavBarItem -> HomeScreen()
                            BottomNavBarItem.ProfileNavBarItem -> ProfileScreen()
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun BottomNavBar(
    selectedItem: BottomNavBarItem,
    onItemChanged: (BottomNavBarItem) -> Unit,
) {
    NavigationBar{
        bottomNavBarItems.forEach { item ->
            NavigationBarItem(
                selected = selectedItem.label == item.label, onClick = {
                    Log.i("TAG", item.label)
                    onItemChanged(item)
                },
                icon = {
                    Icon(
                        item.icon, contentDescription = ""
                    )
                },
                label = { Text(item.label) }
            )
        }
    }
}

    package com.example.aura.presentation.navigation.destinations


    import androidx.navigation.NavController
    import androidx.navigation.NavGraphBuilder
    import androidx.navigation.compose.composable
    import com.example.aura.di.AppContainer
    import com.example.aura.presentation.ui.feature_news.NewsScreen

    const val newsRoute = "newsRoute"

    fun NavGraphBuilder.newsScreen(navController: NavController, container: AppContainer){
        composable(newsRoute) {
            NewsScreen(navController, container)
        }
    }

    fun NavController.navigateToNewsScreen(){
        navigate(newsRoute)
    }
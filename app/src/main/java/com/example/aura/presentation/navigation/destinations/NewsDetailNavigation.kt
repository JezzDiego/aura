import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.aura.di.AppContainer
import com.example.aura.presentation.ui.feature_news_detail.NewsDetailScreen
import kotlinx.serialization.Serializable


@Serializable
data class NewsDetailRoute(
    val articleID: String
)

fun NavGraphBuilder.newsDetailNavigation(navController: NavController, container: AppContainer) {
    composable<NewsDetailRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<NewsDetailRoute>()
        NewsDetailScreen(articleID = route.articleID, navController = navController, container)
    }
}

fun NavController.navigateToNewsDetailScreen(articleID: String){
    navigate(NewsDetailRoute(articleID = articleID))
}
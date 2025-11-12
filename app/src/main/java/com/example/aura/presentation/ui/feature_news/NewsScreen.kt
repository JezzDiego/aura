package com.example.aura.presentation.ui.feature_news

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.aura.core.ResultWrapper
import com.example.aura.di.AppContainer
import com.example.aura.presentation.ui.components.NewsCard
import com.google.android.material.loadingindicator.LoadingIndicator
import navigateToNewsDetailScreen


enum class ArticleFilter{
    ALL,
    SAVED
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(navController: NavController, container: AppContainer){
    val factory = NewsViewModelFactory(container.articleUserCases)
    val viewModel: ArticlesViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()

    var currentFilter by remember {mutableStateOf(ArticleFilter.ALL)}

    LaunchedEffect(currentFilter) {
        when(currentFilter){
            ArticleFilter.ALL -> viewModel.loadAllArticlesAPI()
            ArticleFilter.SAVED -> viewModel.loadAllArticlesDAO()
        }
    }

    val backupEnabled = remember { mutableStateOf(true) }
    val darkThemeEnabled = remember {mutableStateOf(false)}

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ){
            when(uiState){
                is ResultWrapper.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        LoadingIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 24.dp)
                        )
                    }
                }
                is ResultWrapper.Success -> {
                    val articles = (uiState as ResultWrapper.Success).value

                    if (articles.isEmpty()) {
                        Column(
                            Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            FilterButtonsRow(currentFilter) {newFilter ->
                                currentFilter = newFilter
                            }
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(
                                    text = "Nenhuma notícia encontrada",
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }

                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(bottom = 24.dp),
                            content = {
                                item{
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ){
                                        Text(
                                            text = "Notícias",
                                            style = MaterialTheme.typography.titleLarge,
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                    }
                                    Spacer(Modifier.height(24.dp))

                                    FilterButtonsRow(currentFilter) {newFilter ->
                                        currentFilter = newFilter
                                    }
                                    Spacer(Modifier.height(16.dp))

                                }
                                items(articles) { article ->
                                    NewsCard(article, Modifier.clickable{
                                        navController.navigateToNewsDetailScreen(article.id)
                                    })
                                }
                            }
                        )
                    }
                }

                is ResultWrapper.Error -> {
                    val err = (uiState as ResultWrapper.Error).throwable
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Erro ao carregar Notícias:",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = err.localizedMessage ?: "Erro desconhecido",
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.refresh() }) {
                            Text(text = "Tentar novamente")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterButtonsRow(
    currentFilter: ArticleFilter,
    onFilterSelected: (ArticleFilter) -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterButton(
            text = "Todas as Notícias",
            isSelected = currentFilter == ArticleFilter.ALL,
            onClick = {onFilterSelected(ArticleFilter.ALL)},
            modifier = Modifier.weight(1f)
        )
        FilterButton(
            text = "Notícias Salvas",
            isSelected = currentFilter == ArticleFilter.SAVED,
            onClick = {onFilterSelected(ArticleFilter.SAVED)},
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun FilterButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Button(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)

    ) {
        Text(text, fontWeight = FontWeight.Medium)
    }
}
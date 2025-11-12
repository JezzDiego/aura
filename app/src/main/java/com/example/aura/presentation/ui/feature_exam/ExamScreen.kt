package com.example.aura.presentation.ui.feature_exam

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.aura.core.ResultWrapper
import com.example.aura.data.local.db.Converters
import com.example.aura.di.AppContainer
import com.example.aura.domain.model.Category
import com.example.aura.presentation.navigation.destinations.navigateToExamDetailsScreen
import com.example.aura.presentation.ui.components.ExamCard
import com.example.aura.presentation.ui.components.ExamItem
import com.example.aura.utils.formatDate

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterialApi::class)
@Composable
fun ExamScreen(
    container: AppContainer,
    navController: NavHostController
) {
    val factory = ExamViewModelFactory(container.examUseCases)
    val viewModel: ExamViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = false,
        onRefresh = { viewModel.refresh() }
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Meus Exames",
                    style = MaterialTheme.typography.titleLargeEmphasized,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Card(
                    onClick = { /* TODO */ },
                    shape = MaterialTheme.shapes.small,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "novo",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "NOVO",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(start = 6.dp)
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
            ) {
                when (uiState) {
                    is ResultWrapper.Loading -> {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            LoadingIndicator(
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(vertical = 24.dp)
                            )
                        }
                    }

                    is ResultWrapper.Success -> {
                        val exams = (uiState as ResultWrapper.Success).value

                        if (exams.isEmpty()) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(
                                    text = "Nenhum exame encontrado",
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize(),
                                content = {
                                    items(exams) { exam ->
                                        ExamCard(
                                            onClick = {
                                                navController.navigateToExamDetailsScreen(
                                                    examId = exam.id
                                                )
                                            },
                                            exam = ExamItem(
                                                title = exam.title,
                                                subtitle = exam.laboratory?.name
                                                    ?: "Laboratório desconhecido",
                                                date = formatDate(
                                                    exam.date
                                                ),
                                                tag = exam.category.displayName
                                            )
                                        )
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
                                text = "Erro ao carregar exames:",
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

                // Pull-to-refresh indicator overlay
                PullRefreshIndicator(
                    refreshing = false,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter).padding(top = 8.dp),
                    contentColor = MaterialTheme.colorScheme.primary,
                    backgroundColor = MaterialTheme.colorScheme.surface
                )
            }
        }
    }
}

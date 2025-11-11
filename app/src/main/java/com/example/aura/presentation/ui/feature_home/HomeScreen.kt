package com.example.aura.presentation.ui.feature_home

import androidx.compose.foundation.background
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.CardColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aura.di.AppContainer
import com.example.aura.core.ResultWrapper
import com.example.aura.presentation.navigation.BottomNavBarItem
import com.example.aura.utils.formatDate

data class ActionItem(val icon: ImageVector, val title: String, val subtitle: String, val navigate: () -> Unit = {})

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    container: AppContainer,
    swipeNavigate: (BottomNavBarItem) -> Unit = {}
) {
    val factory = HomeViewModelFactory(
        container.examUseCases,
        container.userUseCases
    )
    val viewModel: HomeViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()
    val userState by viewModel.uiUserState.collectAsState()

    val actions = listOf(
        ActionItem(
            Icons.Default.Folder,
            title = "Ver Exames", "Acesse seu histórico completo",
            navigate = { swipeNavigate(BottomNavBarItem.ExamNavBarItem) }
        ),
        ActionItem(
            Icons.Default.Description,
            title = "Adicionar Novo",
            subtitle = "Faça upload de um novo resultado",
        ),
        ActionItem(
            Icons.AutoMirrored.Filled.ShowChart,
            title = "Tendências e Relatórios",
            subtitle = "Visualize seus dados de saúde"
        )
    )

    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                when (userState) {
                    is ResultWrapper.Loading -> {
                        LoadingIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }

                    is ResultWrapper.Success -> {
                        val user = (userState as ResultWrapper.Success).value
                        val split = user?.name?.split(" ")
                        val name = "${split?.firstOrNull()} ${split?.lastOrNull()}"


                        Text(
                            text = "Olá, $name",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    else -> {
                        Text(
                            text = "Olá",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    actions.forEach { action ->
                        ActionCard(action)
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = "Últimos Exames",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

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
                        val display = exams.take(4)
                        val cardAspect = 170f / 120f

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            for (row in 0..1) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                ) {
                                    for (col in 0..1) {
                                        val idx = row * 2 + col
                                        val exam = display.getOrNull(idx)

                                        Box(modifier = Modifier.weight(1f)) {
                                            if (exam != null) {
                                                ExamCard(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .aspectRatio(cardAspect),
                                                    title = exam.title,
                                                    date = formatDate(exam.date),
                                                )
                                            } else {
                                                Spacer(modifier = Modifier
                                                    .fillMaxWidth()
                                                    .aspectRatio(cardAspect))
                                            }
                                        }
                                    }
                                }
                            }
                        }
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
        }
    }
}

@Composable
private fun ActionCard(item: ActionItem) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        onClick = { item.navigate() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun ExamCard(
    modifier: Modifier = Modifier,
    title: String,
    date: String,
    color: CardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
    Card(
        shape = RoundedCornerShape(18.dp),
        modifier = modifier,
        colors = color,
        onClick = { /* TODO: ação ao clicar */ }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.padding(12.dp)) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(start = 16.dp, bottom = 14.dp, end = 16.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.SemiBold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
                )
            }
        }
    }
}

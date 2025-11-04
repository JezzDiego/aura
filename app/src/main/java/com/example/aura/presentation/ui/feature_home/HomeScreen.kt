package com.example.aura.presentation.ui.feature_home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.abs

data class ActionItem(val icon: ImageVector, val title: String, val subtitle: String)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val listState = rememberLazyListState()

    val nestedScrollConnection = remember(listState) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (source != NestedScrollSource.UserInput) return Offset.Zero
                // só interessam gestos horizontais
                if (abs(available.x) <= abs(available.y)) return Offset.Zero

                val layoutInfo = listState.layoutInfo
                val totalItems = layoutInfo.totalItemsCount
                val visible = layoutInfo.visibleItemsInfo

                val canScrollBackward = listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0
                val lastVisibleIndex = visible.lastOrNull()?.index ?: 0
                val canScrollForward = totalItems > 0 && lastVisibleIndex < totalItems - 1

                val scrollingRight = available.x > 0f // dedo movendo pra direita -> conteúdo tenta rolar pra direita (voltar)
                val scrollingLeft = !scrollingRight

                // se a LazyRow PODE rolar na direção do gesto, NÃO consome aqui (deixa a LazyRow processar)
                val lazyCanScrollInDirection = (scrollingRight && canScrollBackward) || (scrollingLeft && canScrollForward)

                return if (lazyCanScrollInDirection) {
                    Offset.Zero
                } else {
                    // se a LazyRow NÃO PODE rolar (está no limite), consome o horizontal para evitar que o pager pai receba o swipe
                    Offset(x = available.x, y = 0f)
                }
            }
        }
    }

    val actions = listOf(
        ActionItem(Icons.Default.Folder, "Ver Exames", "Acesse seu histórico completo"),
        ActionItem(Icons.Default.Description, "Adicionar Novo", "Faça upload de um novo resultado"),
        ActionItem(Icons.AutoMirrored.Filled.ShowChart, "Tendências e Relatórios", "Visualize seus dados de saúde")
    )

    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Olá, Jessé",
                    style = MaterialTheme.typography.headlineMediumEmphasized,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

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

            val exams = listOf(
                Pair("Hemograma", "15/07/2024"),
                Pair("Raio-X Tórax", "02/06/2024"),
                Pair("Ultrassom", "23/11/2024"),
            )

            LazyRow(
                state = listState,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(start = 12.dp, end = 12.dp),
                modifier = Modifier
                    .fillMaxWidth()
//                    .nestedScroll(nestedScrollConnection),
            ) {
                items(exams) { exam ->
                    ExamCard(title = exam.first, date = exam.second)
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
        onClick = { /* TODO: ação ao clicar */ }
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
private fun ExamCard(title: String, date: String) {
    Card(
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .width(170.dp)
            .height(120.dp),
        onClick = { /* TODO: ação ao clicar */ }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer),
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

            Column(modifier = Modifier.padding(start = 16.dp, bottom = 14.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.SemiBold
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

@Preview
@Composable
fun HomeScreenPreview() {
    // Wrap preview with app theme so colors/typography render
    com.example.aura.presentation.ui.theme.AuraTheme {
        HomeScreen()
    }
}

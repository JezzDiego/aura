package com.example.aura.presentation.ui.feature_exam.details

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aura.di.AppContainer
import com.example.aura.core.ResultWrapper
import com.example.aura.utils.formatDate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ExamDetailsScreen(
    container: AppContainer,
    navController: NavHostController,
    examId: String
) {
    val factory = ExamDetailsViewModelFactory(container.examUseCases)
    val viewModel: ExamDetailsViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(examId) {
        viewModel.load(examId)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background),
                    title = {
                        Text(
                            text = uiState.let {
                                when (it) {
                                    is ResultWrapper.Success -> it.value?.title ?: "Detalhes do Exame"
                                    is ResultWrapper.Error -> "Detalhes do Exame"
                                    is ResultWrapper.Loading -> "Carregando..."
                                }
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                        }
                    },
//                    actions = {
//                        IconButton(onClick = { /* TODO: compartilhar */ }) {
//                            Icon(imageVector = Icons.Default.Share, contentDescription = "Compartilhar")
//                        }
//                    },
                )
            },
//            floatingActionButton = {
//                FloatingActionButton(onClick = { /* TODO: gerar PDF */ }) {
//                    Icon(imageVector = Icons.Default.FileDownload, contentDescription = "PDF")
//                }
//            }
        ) { innerPadding ->
            when (uiState) {
                is ResultWrapper.Loading -> {
                    LoadingIndicator()
                }

                is ResultWrapper.Error -> {
                    val err = (uiState as ResultWrapper.Error).throwable
                    Column(modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = err.localizedMessage ?: "Erro ao carregar exame", color = MaterialTheme.colorScheme.error)
                    }
                }

                is ResultWrapper.Success -> {
                    val exam = (uiState as ResultWrapper.Success).value
                    Log.i("ExamDetailsScreen", "Loaded exam id=${exam?.id}")
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))

                        // Info card
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(vertical = 12.dp, horizontal = 24.dp)) {
                                InfoRow(label = "Nome do Exame", value = exam?.title ?: "-")
                                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), thickness = 1.dp)
                                InfoRow(label = "Data", value = exam?.date?.let { formatDate(it) } ?: "-")
                                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), thickness = 1.dp)
                                InfoRow(label = "Laboratório", value = exam?.laboratory?.name ?: "Lab. Desconhecido")
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Results card
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(vertical = 12.dp, horizontal = 24.dp)) {
                                Text(
                                    text = "Resultados",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                val results = exam?.results ?: emptyList()
                                if (results.isEmpty()) {
                                    Text(text = "Sem resultados", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                                } else {

                                    results.map {
                                        ResultItem(
                                            title = it.fieldName,
                                            value = it.resultValue,
                                            range = it.referenceRange ?: "-",
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // History card (with simple chart placeholder)
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                        ) {
                            Column(modifier = Modifier.padding(vertical = 12.dp, horizontal = 24.dp)) {
                                Text(
                                    text = "Histórico de Hemoglobina",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "13.5 g/dL    Últimos 6 meses    -3.2%",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Box(modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)) {
                                    HemoglobinChart(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(6.dp),
                                        lineColor = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

private enum class ResultStatus { OK, WARNING, ERROR }

@Composable
private fun ResultItem(title: String, value: String, range: String, status: ResultStatus = ResultStatus.OK) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Column(modifier = Modifier.padding(start = 4.dp), horizontalAlignment = Alignment.End) {
            val (iconColor, icon) = when (status) {
                ResultStatus.OK -> Pair(MaterialTheme.colorScheme.primary, Icons.Default.CheckCircle)
                ResultStatus.WARNING -> Pair(MaterialTheme.colorScheme.tertiary, Icons.Default.Warning)
                ResultStatus.ERROR -> Pair(MaterialTheme.colorScheme.error, Icons.Default.Close)
            }

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = range,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun HemoglobinChart(modifier: Modifier = Modifier, lineColor: Color = Color.Magenta, stroke: Dp = 3.dp) {
    val points = listOf(0.2f, 0.5f, 0.4f, 0.55f, 0.3f, 0.7f)
    val strokePx = with(LocalDensity.current) { stroke.toPx() }

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val gap = w / (points.size - 1)

        val path = Path()
        for (i in points.indices) {
            val x = i * gap
            val y = h - (points[i] * h * 0.9f) // leave top padding
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }

        // shadow / filled area
        val fillPath = Path().also { it.addPath(path) }
        fillPath.lineTo(w, h)
        fillPath.lineTo(0f, h)
        fillPath.close()
        drawPath(fillPath, color = lineColor.copy(alpha = 0.08f))

        // line
        drawPath(path, color = lineColor, style = Stroke(width = strokePx, cap = StrokeCap.Round))

        // draw small circles on points
        for (i in points.indices) {
            val x = i * gap
            val y = h - (points[i] * h * 0.9f)
            drawCircle(color = lineColor, radius = 4f, center = Offset(x, y))
        }
    }
}
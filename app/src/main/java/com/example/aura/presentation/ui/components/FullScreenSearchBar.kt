package com.example.aura.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.verticalScroll
import com.example.aura.di.AppContainer
import com.example.aura.domain.model.Category
import com.example.aura.domain.model.Exam
import com.example.aura.utils.formatDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenSearchBar(container: AppContainer) {
    val searchBarState = rememberSearchBarState()
    val textFieldState = rememberTextFieldState()
    val scope = rememberCoroutineScope()

    var exams by remember { mutableStateOf(emptyList<Exam>()) }

    LaunchedEffect(Unit) {
        exams = withContext(Dispatchers.IO) {
            container.examUseCases.getExamList()
        }
    }

    val inputField =
        @Composable {
            SearchBarDefaults.InputField(
                modifier = Modifier.fillMaxWidth(),
                searchBarState = searchBarState,
                textFieldState = textFieldState,
                onSearch = { scope.launch { searchBarState.animateToCollapsed() } },
                placeholder = {
                    Text(
                        modifier = Modifier
                            .clearAndSetSemantics{},
                        text = "Busque entre seus exames...",
                    )
                },
                leadingIcon = {
                    if (searchBarState.currentValue == SearchBarValue.Expanded) {
                        TooltipBox(
                            positionProvider =
                                TooltipDefaults.rememberTooltipPositionProvider(
                                    TooltipAnchorPosition.Above
                                ),
                            tooltip = { PlainTooltip { Text("Voltar") } },
                            state = rememberTooltipState(),
                        ) {
                            IconButton(
                                onClick = { scope.launch { searchBarState.animateToCollapsed() } }
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Default.ArrowBack,
                                    contentDescription = "Voltar",
                                )
                            }
                        }
                    } else {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                },
                trailingIcon = {
                    if (textFieldState.text.isNotEmpty() && searchBarState.currentValue == SearchBarValue.Expanded) {
                        Icon(
                            modifier = Modifier
                                .clickable {
                                    textFieldState.clearText()
                                },
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Limpar",
                        )
                    }
                },
            )
        }

    SearchBar(state = searchBarState, inputField = inputField)
    ExpandedFullScreenSearchBar(state = searchBarState, inputField = inputField){
        Column (
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            val query = textFieldState.text.toString().lowercase().trim()

            val filteredExams = exams.filter {
                it.title.lowercase().contains(query) ||
                        it.category.displayName.lowercase().contains(query) ||
                        it.laboratory?.name?.lowercase()?.contains(query) == true ||
                        it.notes?.lowercase()?.contains(query) == true
            }

            if (filteredExams.isEmpty()) {
                Text(text = "Nenhum exame encontrado.")
            } else {
                filteredExams.forEach { exam ->
                     ExamCard(
                            exam = ExamItem(
                                title = exam.title,
                                subtitle = exam.laboratory?.name
                                    ?: "Laboratório desconhecido",
                                date = formatDate(exam.date),
                                tag = exam.category.displayName
                            ),
                        )
                }
            }
        }
    }
}

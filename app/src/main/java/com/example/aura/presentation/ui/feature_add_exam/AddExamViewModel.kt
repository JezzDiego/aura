package com.example.aura.presentation.ui.feature_add_exam

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aura.core.BaseViewModel
import com.example.aura.di.AppContainer
import com.example.aura.domain.model.Category
import com.example.aura.domain.model.Exam
import com.example.aura.domain.model.Laboratory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed class AddExamUiState {
    object Idle : AddExamUiState()
    object Loading : AddExamUiState()
    object Success : AddExamUiState()
    data class Error(val throwable: Throwable) : AddExamUiState()
}

class AddExamViewModel(
    private val container: AppContainer
) : BaseViewModel() {
    private val _uiState = MutableStateFlow<AddExamUiState>(AddExamUiState.Idle)
    val uiState: StateFlow<AddExamUiState> = _uiState

    // Lista de laboratórios disponíveis
    private val _labs = MutableStateFlow<List<Laboratory>>(emptyList())
    val labs: StateFlow<List<Laboratory>> = _labs

    init {
        // Carrega laboratórios ao iniciar
        viewModelScope.launch {
            try {
                val fetched = container.laboratoryUseCases.getLaboratories()
                _labs.value = fetched
            } catch (_: Throwable) {
                // falha silenciosa
            }
        }
    }

    fun createExam(title: String, category: Category, dateStr: String, laboratory: Laboratory?) {
        viewModelScope.launch {
            _uiState.value = AddExamUiState.Loading

            val user = container.userDao.getUser().firstOrNull()
            if (user == null) {
                _uiState.value = AddExamUiState.Error(Throwable("Usuário não encontrado"))
                return@launch
            }

            val date = try {
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dateStr)?.time ?: Date().time
            } catch (e: Exception) {
                Date().time
            }

            executeSafeCall(
                block = {
                    val exam = Exam(
                        id = "",
                        userId = user.id,
                        title = title,
                        category = category,
                        date = date,
                        laboratory = laboratory,
                        results = emptyList(),
                        attachments = emptyList()
                    )
                    container.examUseCases.createExam(exam)
                },
                onSuccess = {
                    _uiState.value = AddExamUiState.Success
                },
                onError = {
                    _uiState.value = AddExamUiState.Error(it)
                }
            )
        }
    }
}

class AddExamViewModelFactory(
    private val container: AppContainer
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddExamViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddExamViewModel(container) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}

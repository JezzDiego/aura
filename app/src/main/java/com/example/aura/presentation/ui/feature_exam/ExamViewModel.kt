package com.example.aura.presentation.ui.feature_exam

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aura.core.BaseViewModel
import com.example.aura.domain.model.Exam
import com.example.aura.domain.usecase.exam.ExamUseCases
import com.example.aura.domain.usecase.exam.GetExamListUseCase
import com.example.aura.core.ResultWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ExamViewModel(private val getExamList: GetExamListUseCase) : BaseViewModel() {
    private val _uiState = MutableStateFlow<ResultWrapper<List<Exam>>>(ResultWrapper.Loading)
    val uiState: StateFlow<ResultWrapper<List<Exam>>> = _uiState

    init {
        load()
    }

    fun refresh() {
        load()
    }

    private fun load() {
        _uiState.value = ResultWrapper.Loading

        executeSafeCall(
            block = {
                getExamList()
            },
            onSuccess = {
                _uiState.value = ResultWrapper.Success(it)
            },
            onError = {
                _uiState.value = ResultWrapper.Error(it)
            }
        )
    }
}

class ExamViewModelFactory(
    private val examUseCases: ExamUseCases
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExamViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExamViewModel(examUseCases.getExamList) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}

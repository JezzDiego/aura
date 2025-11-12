package com.example.aura.presentation.ui.feature_exam.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aura.core.BaseViewModel
import com.example.aura.core.ResultWrapper
import com.example.aura.domain.model.Exam
import com.example.aura.domain.usecase.exam.ExamUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ExamDetailsViewModel(
    private val examUseCases: ExamUseCases
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<ResultWrapper<Exam?>>(ResultWrapper.Loading)
    val uiState: StateFlow<ResultWrapper<Exam?>> = _uiState

    fun load(examId: String) {
        _uiState.value = ResultWrapper.Loading

        executeSafeCall(
            block = {
                examUseCases.getExamById(examId)
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

class ExamDetailsViewModelFactory(
    private val examUseCases: ExamUseCases
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExamDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExamDetailsViewModel(examUseCases) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}

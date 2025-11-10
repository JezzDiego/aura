package com.example.aura.presentation.ui.feature_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aura.core.BaseViewModel
import com.example.aura.domain.model.Exam
import com.example.aura.domain.usecase.exam.ExamUseCases
import com.example.aura.domain.usecase.exam.GetExamListUseCase
import com.example.aura.core.ResultWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel(
    private val examUseCases: ExamUseCases
) : BaseViewModel() {
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
                examUseCases.getExamList()
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

class HomeViewModelFactory(
    private val examUseCases: ExamUseCases
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(examUseCases) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}

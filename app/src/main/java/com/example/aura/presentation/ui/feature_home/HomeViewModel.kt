package com.example.aura.presentation.ui.feature_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aura.core.BaseViewModel
import com.example.aura.domain.model.Exam
import com.example.aura.domain.usecase.exam.ExamUseCases
import com.example.aura.domain.usecase.exam.GetExamListUseCase
import com.example.aura.core.ResultWrapper
import com.example.aura.domain.model.User
import com.example.aura.domain.usecase.user.UserUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    private val examUseCases: ExamUseCases,
    private val userUseCases: UserUseCases
) : BaseViewModel() {

    // expose a reactive StateFlow<User?> backed by the local DB flow (preserves watcher)
    val user = userUseCases.getLocalUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // derive uiState from the user flow to keep a single source of truth
    val uiUserState: StateFlow<ResultWrapper<User?>> = user
        .map<User?, ResultWrapper<User?>> { user -> ResultWrapper.Success(user) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ResultWrapper.Loading)
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
    private val examUseCases: ExamUseCases,
    private val userUseCases: UserUseCases
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(examUseCases, userUseCases) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}

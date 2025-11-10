package com.example.aura.presentation.ui.feature_login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aura.core.BaseViewModel
import com.example.aura.core.ResultWrapper
import com.example.aura.domain.model.User
import com.example.aura.domain.usecase.user.UserUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel (
    private val userUseCases: UserUseCases
): BaseViewModel() {
    private val _uiState = MutableStateFlow<ResultWrapper<User>>(ResultWrapper.Loading)
    val uiState: StateFlow<ResultWrapper<User>> = _uiState

    fun login(email: String, password: String){
        _uiState.value = ResultWrapper.Loading

        executeSafeCall(
            block = {
                userUseCases.loginUser(email, password)
            },
            onSuccess = {
                if (it != null){
                    _uiState.value = ResultWrapper.Success(it)
                }
            },
            onError = {
                _uiState.value = ResultWrapper.Error(it)
            }
        )
    }
}

class LoginViewModelFactory(
    private val userUseCases: UserUseCases
): ViewModelProvider.Factory {
    override fun <T: ViewModel> create (modelClass: Class<T>): T{
        if(modelClass.isAssignableFrom(LoginViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(userUseCases) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}
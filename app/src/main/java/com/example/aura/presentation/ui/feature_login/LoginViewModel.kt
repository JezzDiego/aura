package com.example.aura.presentation.ui.feature_login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aura.core.BaseViewModel
import com.example.aura.core.ResultWrapper
import com.example.aura.domain.model.AccessibilityProfile
import com.example.aura.domain.model.User
import com.example.aura.domain.usecase.user.UserUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LoginFormState(
    val name: String = "",
    val email: String = "",
    val profile: AccessibilityProfile? = null,
)

class LoginViewModel(
    private val userUseCases: UserUseCases
) : BaseViewModel() {

    private val _form = MutableStateFlow(LoginFormState())
    val form: StateFlow<LoginFormState> = _form.asStateFlow()

    private val _uiState = MutableStateFlow<ResultWrapper<User>?>(null)
    val uiState: StateFlow<ResultWrapper<User>?> = _uiState.asStateFlow()

    fun onNameChange(value: String) = _form.update { it.copy(name = value) }
    fun onEmailChange(value: String) = _form.update { it.copy(email = value) }
    fun onProfileChange(value: AccessibilityProfile?) = _form.update {
        it.copy(profile = if (it.profile == value) null else value)
    }

    fun login() {
        val form = _form.value
        _uiState.value = ResultWrapper.Loading
        executeSafeCall(
            block = { userUseCases.loginUser(form.name, form.email, form.profile) },
            onSuccess = { _uiState.value = ResultWrapper.Success(it) },
            onError = { _uiState.value = ResultWrapper.Error(it) }
        )
    }

    fun continueAsGuest() {
        _uiState.value = ResultWrapper.Loading
        executeSafeCall(
            block = { userUseCases.loginUser("Visitante", "", _form.value.profile) },
            onSuccess = { _uiState.value = ResultWrapper.Success(it) },
            onError = { _uiState.value = ResultWrapper.Error(it) }
        )
    }
}

class LoginViewModelFactory(
    private val userUseCases: UserUseCases
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(userUseCases) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}

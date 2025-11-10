package com.example.aura.presentation.ui.feature_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aura.core.BaseViewModel
import com.example.aura.core.ResultWrapper
import com.example.aura.domain.model.User
import com.example.aura.domain.usecase.user.UserUseCases
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ProfileViewModel(
    private val userUseCases: UserUseCases
) : BaseViewModel() {

    // expose a reactive StateFlow<User?> backed by the local DB flow (preserves watcher)
    val user = userUseCases.getLocalUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // derive uiState from the user flow to keep a single source of truth
    val uiState: StateFlow<ResultWrapper<User?>> = user
        .map<User?, ResultWrapper<User?>> { user -> ResultWrapper.Success(user) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ResultWrapper.Loading)
}

class ProfileViewModelFactory(
    private val userUseCases: UserUseCases
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(userUseCases) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}

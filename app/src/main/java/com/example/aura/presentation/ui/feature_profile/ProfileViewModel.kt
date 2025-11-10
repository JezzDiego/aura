package com.example.aura.presentation.ui.feature_profile

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aura.core.BaseViewModel
import com.example.aura.core.ResultWrapper
import com.example.aura.data.local.preferences.AuraPreferences
import com.example.aura.domain.model.User
import com.example.aura.domain.usecase.user.UserUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userUseCases: UserUseCases,
    application: Application
) : BaseViewModel() {
    // expose a reactive StateFlow<User?> backed by the local DB flow (preserves watcher)
    val user = userUseCases.getLocalUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // derive uiState from the user flow to keep a single source of truth
    val uiUserState: StateFlow<ResultWrapper<User?>> = user
        .map<User?, ResultWrapper<User?>> { user -> ResultWrapper.Success(user) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ResultWrapper.Loading)

    private val prefs = AuraPreferences(application)

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _isCloudBackupEnabled = MutableStateFlow(false)
    val isCloudBackupEnabled: StateFlow<Boolean> = _isCloudBackupEnabled.asStateFlow()

    init {
        // observe preferences
        prefs.isDarkMode.onEach { _isDarkMode.value = it }.launchIn(viewModelScope)
        prefs.isCloudBackupEnabled.onEach { _isCloudBackupEnabled.value = it }.launchIn(viewModelScope)
    }

    // actions
    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            prefs.setDarkMode(enabled)
        }
    }

    fun setCloudBackup(enabled: Boolean) {
        viewModelScope.launch {
            prefs.setCloudBackup(enabled)
        }
    }

    fun logout(onSuccess: () -> Unit){
        viewModelScope.launch {
            userUseCases.logoutUser()
            onSuccess()
        }
    }
}

class ProfileViewModelFactory(
    private val userUseCases: UserUseCases,
    private val app: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(userUseCases, app) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}

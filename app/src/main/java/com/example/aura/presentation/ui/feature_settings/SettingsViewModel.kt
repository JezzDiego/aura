package com.example.aura.presentation.ui.feature_settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aura.data.local.preferences.AuraPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = AuraPreferences(application)

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _isCloudBackupEnabled = MutableStateFlow(false)
    val isCloudBackupEnabled: StateFlow<Boolean> = _isCloudBackupEnabled.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        // observe preferences
        prefs.isDarkMode.onEach { _isDarkMode.value = it }.launchIn(viewModelScope)
        prefs.isCloudBackupEnabled.onEach { _isCloudBackupEnabled.value = it }.launchIn(viewModelScope)
    }

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

    /**
     * Example refresh implementation for pull-to-refresh. Replace with real logic if needed.
     */
    fun refresh() {
        if (_isRefreshing.value) return
        viewModelScope.launch {
            _isRefreshing.value = true
            // Simulate a refresh operation; in real code call use-cases / repositories here
            delay(800)
            _isRefreshing.value = false
        }
    }
}


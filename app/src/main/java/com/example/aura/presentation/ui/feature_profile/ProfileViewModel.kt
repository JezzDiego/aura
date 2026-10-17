package com.example.aura.presentation.ui.feature_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aura.core.BaseViewModel
import com.example.aura.core.UiEvent
import com.example.aura.data.local.preferences.AuraPreferences
import com.example.aura.domain.model.AccessibilityFilter
import com.example.aura.domain.model.AccessibilityProfile
import com.example.aura.domain.model.Establishment
import com.example.aura.domain.model.Review
import com.example.aura.domain.model.User
import com.example.aura.domain.usecase.establishment.EstablishmentUseCases
import com.example.aura.domain.usecase.review.ReviewUseCases
import com.example.aura.domain.usecase.user.UserUseCases
import com.example.aura.presentation.ui.components.AccessibilitySettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userUseCases: UserUseCases,
    private val establishmentUseCases: EstablishmentUseCases,
    private val reviewUseCases: ReviewUseCases,
    private val preferences: AuraPreferences,
) : BaseViewModel() {

    val user: StateFlow<User?> = userUseCases.getLocalUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val settings: StateFlow<AccessibilitySettings> = combine(
        preferences.isDarkMode,
        preferences.isLargeText,
        preferences.isReduceMotion,
    ) { dark, large, motion -> AccessibilitySettings(dark, large, motion) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AccessibilitySettings())

    private val _favorites = MutableStateFlow<List<Establishment>>(emptyList())
    val favorites: StateFlow<List<Establishment>> = _favorites.asStateFlow()

    private val _myReviews = MutableStateFlow<List<Review>>(emptyList())
    val myReviews: StateFlow<List<Review>> = _myReviews.asStateFlow()

    private val _loggedOut = MutableStateFlow<UiEvent<Unit>?>(null)
    val loggedOut: StateFlow<UiEvent<Unit>?> = _loggedOut.asStateFlow()

    init {
        viewModelScope.launch {
            user.collect { refreshLists(it) }
        }
    }

    fun refresh() {
        viewModelScope.launch { refreshLists(user.value) }
    }

    private suspend fun refreshLists(user: User?) {
        _favorites.value = runCatching { establishmentUseCases.getFavorites() }.getOrDefault(emptyList())
        _myReviews.value = user?.let {
            runCatching { reviewUseCases.getUserReviews(it.id) }.getOrDefault(emptyList())
        }.orEmpty()
    }

    fun selectProfile(profile: AccessibilityProfile) {
        val current = user.value ?: return
        val newProfile = if (current.profile == profile) null else profile
        viewModelScope.launch {
            // Ao trocar de perfil, os filtros passam a seguir as sugestões do novo perfil
            userUseCases.updateAccessibilityProfile(newProfile, emptySet())
        }
    }

    fun toggleFilter(filter: AccessibilityFilter) {
        val current = user.value ?: return
        val base = current.accessibilityFilters
        val updated = if (filter in base) base - filter else base + filter
        viewModelScope.launch { userUseCases.updateAccessibilityProfile(current.profile, updated) }
    }

    fun updateName(name: String) {
        viewModelScope.launch { runCatching { userUseCases.updateUserName(name) } }
    }

    fun setHighContrast(value: Boolean) = viewModelScope.launch { preferences.setDarkMode(value) }
    fun setLargeText(value: Boolean) = viewModelScope.launch { preferences.setLargeText(value) }
    fun setReduceMotion(value: Boolean) = viewModelScope.launch { preferences.setReduceMotion(value) }

    fun logout() {
        executeSafeCall(
            block = { userUseCases.logoutUser() },
            onSuccess = { _loggedOut.value = UiEvent(Unit) },
        )
    }
}

class ProfileViewModelFactory(
    private val userUseCases: UserUseCases,
    private val establishmentUseCases: EstablishmentUseCases,
    private val reviewUseCases: ReviewUseCases,
    private val preferences: AuraPreferences,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(userUseCases, establishmentUseCases, reviewUseCases, preferences) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}

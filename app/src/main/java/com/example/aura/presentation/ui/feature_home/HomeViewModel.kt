package com.example.aura.presentation.ui.feature_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aura.core.BaseViewModel
import com.example.aura.core.ResultWrapper
import com.example.aura.domain.model.Establishment
import com.example.aura.domain.model.Review
import com.example.aura.domain.model.User
import com.example.aura.domain.usecase.establishment.EstablishmentUseCases
import com.example.aura.domain.usecase.review.ReviewUseCases
import com.example.aura.domain.usecase.user.UserUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val establishmentUseCases: EstablishmentUseCases,
    private val reviewUseCases: ReviewUseCases,
    userUseCases: UserUseCases,
) : BaseViewModel() {

    val user: StateFlow<User?> = userUseCases.getLocalUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val reviews: StateFlow<List<Review>> = reviewUseCases.getRecentReviews(limit = 5)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _uiState = MutableStateFlow<ResultWrapper<List<Establishment>>>(ResultWrapper.Loading)
    val uiState: StateFlow<ResultWrapper<List<Establishment>>> = _uiState.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    init {
        load()
    }

    fun onQueryChange(value: String) {
        _query.value = value
    }

    fun refresh() = load()

    private fun load() {
        if (_uiState.value !is ResultWrapper.Success) _uiState.value = ResultWrapper.Loading
        executeSafeCall(
            block = { establishmentUseCases.getEstablishments() },
            onSuccess = { _uiState.value = ResultWrapper.Success(it) },
            onError = { _uiState.value = ResultWrapper.Error(it) }
        )
    }

    fun toggleHelpful(reviewId: String) {
        viewModelScope.launch { reviewUseCases.toggleHelpful(reviewId) }
    }
}

class HomeViewModelFactory(
    private val establishmentUseCases: EstablishmentUseCases,
    private val reviewUseCases: ReviewUseCases,
    private val userUseCases: UserUseCases,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(establishmentUseCases, reviewUseCases, userUseCases) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}

package com.example.aura.presentation.ui.feature_establishment.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aura.core.BaseViewModel
import com.example.aura.core.ResultWrapper
import com.example.aura.core.UiEvent
import com.example.aura.domain.model.Establishment
import com.example.aura.domain.model.Review
import com.example.aura.domain.model.ShareLink
import com.example.aura.domain.usecase.establishment.EstablishmentUseCases
import com.example.aura.domain.usecase.review.ReviewUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EstablishmentDetailsViewModel(
    private val establishmentUseCases: EstablishmentUseCases,
    private val reviewUseCases: ReviewUseCases,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<ResultWrapper<Establishment?>>(ResultWrapper.Loading)
    val uiState: StateFlow<ResultWrapper<Establishment?>> = _uiState.asStateFlow()

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()

    private val _shareEvent = MutableStateFlow<UiEvent<ShareLink>?>(null)
    val shareEvent: StateFlow<UiEvent<ShareLink>?> = _shareEvent.asStateFlow()

    private val _message = MutableStateFlow<UiEvent<String>?>(null)
    val message: StateFlow<UiEvent<String>?> = _message.asStateFlow()

    private var currentId: String? = null

    fun load(establishmentId: String) {
        currentId = establishmentId
        if (_uiState.value !is ResultWrapper.Success) _uiState.value = ResultWrapper.Loading
        executeSafeCall(
            block = { establishmentUseCases.getEstablishmentById(establishmentId) },
            onSuccess = { _uiState.value = ResultWrapper.Success(it) },
            onError = { _uiState.value = ResultWrapper.Error(it) }
        )
        viewModelScope.launch {
            _reviews.value = runCatching { reviewUseCases.getEstablishmentReviews(establishmentId) }
                .getOrDefault(emptyList())
        }
    }

    fun refresh() {
        currentId?.let { load(it) }
    }

    fun toggleFavorite() {
        val id = currentId ?: return
        viewModelScope.launch {
            val favorite = establishmentUseCases.toggleFavorite(id)
            (_uiState.value as? ResultWrapper.Success)?.value?.let {
                _uiState.value = ResultWrapper.Success(it.copy(isFavorite = favorite))
            }
            _message.value = UiEvent(if (favorite) "Local salvo nos favoritos" else "Local removido dos favoritos")
        }
    }

    fun share() {
        val id = currentId ?: return
        executeSafeCall(
            block = { establishmentUseCases.shareEstablishment(id) },
            onSuccess = { _shareEvent.value = UiEvent(it) },
            onError = { _message.value = UiEvent("Não foi possível compartilhar este local") }
        )
    }

    fun toggleHelpful(reviewId: String) {
        viewModelScope.launch {
            reviewUseCases.toggleHelpful(reviewId)
            currentId?.let { _reviews.value = reviewUseCases.getEstablishmentReviews(it) }
        }
    }
}

class EstablishmentDetailsViewModelFactory(
    private val establishmentUseCases: EstablishmentUseCases,
    private val reviewUseCases: ReviewUseCases,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EstablishmentDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EstablishmentDetailsViewModel(establishmentUseCases, reviewUseCases) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}

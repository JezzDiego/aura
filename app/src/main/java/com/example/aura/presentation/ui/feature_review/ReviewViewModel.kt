package com.example.aura.presentation.ui.feature_review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aura.core.BaseViewModel
import com.example.aura.core.UiEvent
import com.example.aura.domain.model.AccessibilityProfile
import com.example.aura.domain.model.Establishment
import com.example.aura.domain.model.FeatureStatus
import com.example.aura.domain.model.Review
import com.example.aura.domain.model.ReviewChecklistItem
import com.example.aura.domain.model.User
import com.example.aura.domain.usecase.establishment.EstablishmentUseCases
import com.example.aura.domain.usecase.review.ReviewUseCases
import com.example.aura.domain.usecase.user.UserUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class ReviewFormState(
    val establishments: List<Establishment> = emptyList(),
    val selectedEstablishment: Establishment? = null,
    val profile: AccessibilityProfile? = null,
    val checklist: Map<ReviewChecklistItem, FeatureStatus> = emptyMap(),
    val comment: String = "",
    val photos: List<String> = emptyList(),
    val isSubmitting: Boolean = false,
    val isAddingPhoto: Boolean = false,
    val showErrors: Boolean = false,
) {
    val checklistComplete: Boolean get() = ReviewChecklistItem.entries.all { it in checklist }
    val canAddPhoto: Boolean get() = photos.size < Review.MAX_PHOTOS
}

class ReviewViewModel(
    private val establishmentUseCases: EstablishmentUseCases,
    private val reviewUseCases: ReviewUseCases,
    private val userUseCases: UserUseCases,
) : BaseViewModel() {

    private val _state = MutableStateFlow(ReviewFormState())
    val state: StateFlow<ReviewFormState> = _state.asStateFlow()

    private val _message = MutableStateFlow<UiEvent<String>?>(null)
    val message: StateFlow<UiEvent<String>?> = _message.asStateFlow()

    private val _published = MutableStateFlow<UiEvent<String>?>(null)
    /** Emite o id do estabelecimento avaliado quando o relato é publicado. */
    val published: StateFlow<UiEvent<String>?> = _published.asStateFlow()

    private val user: StateFlow<User?> = userUseCases.getLocalUser()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private var initialized = false

    fun init(initialEstablishmentId: String?) {
        if (initialized) return
        initialized = true
        viewModelScope.launch {
            val list = runCatching { establishmentUseCases.getEstablishments() }.getOrDefault(emptyList())
            val currentUser = runCatching { userUseCases.getLocalUser().first() }.getOrNull()
            val selected = list.firstOrNull { it.id == initialEstablishmentId }
            _state.update {
                it.copy(
                    establishments = list,
                    selectedEstablishment = selected ?: it.selectedEstablishment,
                    profile = it.profile ?: currentUser?.profile,
                )
            }
        }
    }

    fun selectEstablishment(establishment: Establishment) {
        _state.update { it.copy(selectedEstablishment = establishment) }
    }

    fun selectProfile(profile: AccessibilityProfile?) {
        _state.update { it.copy(profile = profile) }
    }

    fun setStatus(item: ReviewChecklistItem, status: FeatureStatus) {
        _state.update { it.copy(checklist = it.checklist + (item to status)) }
    }

    fun onCommentChange(value: String) {
        _state.update { it.copy(comment = value.take(Review.MAX_COMMENT_LENGTH)) }
    }

    fun addPhoto(sourceUri: String) {
        if (!_state.value.canAddPhoto) return
        _state.update { it.copy(isAddingPhoto = true) }
        executeSafeCall(
            block = { reviewUseCases.savePhoto(sourceUri) },
            onSuccess = { path -> _state.update { it.copy(photos = it.photos + path, isAddingPhoto = false) } },
            onError = {
                _state.update { it.copy(isAddingPhoto = false) }
                _message.value = UiEvent("Não foi possível adicionar a foto.")
            }
        )
    }

    fun removePhoto(path: String) {
        _state.update { it.copy(photos = it.photos - path) }
        viewModelScope.launch { runCatching { reviewUseCases.deletePhoto(path) } }
    }

    fun submit() {
        val form = _state.value
        val establishment = form.selectedEstablishment
        if (establishment == null || !form.checklistComplete || form.comment.isBlank()) {
            _state.update { it.copy(showErrors = true) }
        }
        val author = user.value
        val review = Review(
            id = UUID.randomUUID().toString(),
            establishmentId = establishment?.id.orEmpty(),
            establishmentName = establishment?.name.orEmpty(),
            authorId = author?.id ?: "anonymous",
            authorName = author?.name ?: "Pessoa da comunidade",
            authorProfile = form.profile,
            comment = form.comment.trim(),
            checklist = form.checklist,
            photos = form.photos,
        )
        _state.update { it.copy(isSubmitting = true) }
        executeSafeCall(
            block = { reviewUseCases.submitReview(review) },
            onSuccess = {
                val establishmentId = review.establishmentId
                _state.update {
                    ReviewFormState(
                        establishments = it.establishments,
                        selectedEstablishment = null,
                        profile = it.profile,
                    )
                }
                _message.value = UiEvent("Avaliação publicada! Obrigado por contribuir.")
                _published.value = UiEvent(establishmentId)
            },
            onError = { error ->
                _state.update { it.copy(isSubmitting = false, showErrors = true) }
                _message.value = UiEvent(error.message ?: "Não foi possível publicar a avaliação.")
            }
        )
    }
}

class ReviewViewModelFactory(
    private val establishmentUseCases: EstablishmentUseCases,
    private val reviewUseCases: ReviewUseCases,
    private val userUseCases: UserUseCases,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReviewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReviewViewModel(establishmentUseCases, reviewUseCases, userUseCases) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}

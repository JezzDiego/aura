package com.example.aura.presentation.ui.feature_medication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aura.core.BaseViewModel
import com.example.aura.core.ResultWrapper
import com.example.aura.domain.model.Medication
import com.example.aura.domain.model.MedicationSchedule
import com.example.aura.domain.usecase.medication.MedicationUseCases
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MedicationUiState(
    val savedMedications: List<MedicationSchedule> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,

    val name: String = "",
    val doseDetails: String = "",
    val interval: String = "",
    val startTime: String = "",
    val description: String = "",

    val searchResults: List<Medication> = emptyList(),
    val isSearching: Boolean = false,

    val medicationForDetails: Medication? = null
)

class MedicationViewModel(
    private val useCases: MedicationUseCases
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(MedicationUiState())
    val uiState: StateFlow<MedicationUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        getSavedMedications()
    }

    private fun getSavedMedications() {
        viewModelScope.launch {
            useCases.getSavedMedications()
                .collect { schedules ->
                    _uiState.update { it.copy(savedMedications = schedules, isLoading = false) }
                }
        }
    }

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300L)
            searchMedications(name)
        }
    }

    private fun searchMedications(query: String) {
        if (query.length < 3) {
            _uiState.update { it.copy(searchResults = emptyList(), isSearching = false) }
            return
        }
        _uiState.update { it.copy(isSearching = true) }
        viewModelScope.launch {
            when (val result = useCases.searchMedications(query)) {
                is ResultWrapper.Success -> {
                    _uiState.update { it.copy(searchResults = result.value, isSearching = false, error = null) }
                }
                is ResultWrapper.Error -> {
                    _uiState.update { it.copy(
                        isSearching = false,
                        error = "Falha ao buscar sugestões: ${result.throwable.localizedMessage}"
                    ) }
                }
                is ResultWrapper.Loading -> {
                    _uiState.update { it.copy(isSearching = true) }
                }
            }
        }
    }

    fun onSuggestionSelected(medication: Medication) {
        _uiState.update {
            it.copy(
                name = medication.name,
                doseDetails = medication.doseDetails,
                interval = medication.intervalInHours.toString(),
                description = medication.description,
                searchResults = emptyList(),
                error = null
            )
        }
    }

    fun onDoseChange(dose: String) {
        _uiState.update { it.copy(doseDetails = dose) }
    }

    fun onIntervalChange(interval: String) {
        _uiState.update { it.copy(interval = interval) }
    }

    fun onStartTimeChange(time: String) {
        _uiState.update { it.copy(startTime = time) }
    }

    fun onAddMedication() {
        val state = _uiState.value
        if (state.name.isBlank() || state.doseDetails.isBlank() || state.interval.isBlank() || state.startTime.isBlank()) {
            _uiState.update { it.copy(error = "Preencha todos os campos") }
            return
        }

        val medication = Medication(
            id = 0,
            name = state.name,
            doseDetails = state.doseDetails,
            intervalInHours = state.interval.toIntOrNull() ?: 8,
            startTime = state.startTime,
            description = state.description
        )

        executeSafeCall(
            block = { useCases.addMedication(medication) },
            onSuccess = {
                _uiState.update {
                    it.copy(name = "", doseDetails = "", interval = "", startTime = "", description = "", error = null)
                }
            },
            onError = { throwable ->
                _uiState.update { it.copy(error = throwable.localizedMessage ?: "Erro ao salvar") }
            }
        )
    }

    fun onDeleteMedication(schedule: MedicationSchedule) {
        executeSafeCall(
            block = { useCases.deleteMedication(schedule.medication) },
            onSuccess = {},
            onError = { throwable ->
                _uiState.update { it.copy(error = throwable.localizedMessage ?: "Erro ao deletar") }
            }
        )
    }

    fun onShowDetails(schedule: MedicationSchedule) {
        _uiState.update { it.copy(medicationForDetails = schedule.medication) }
    }

    fun onDismissDetails() {
        _uiState.update { it.copy(medicationForDetails = null) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

class MedicationViewModelFactory(
    private val useCases: MedicationUseCases
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MedicationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MedicationViewModel(useCases) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aura.core.BaseViewModel
import com.example.aura.domain.model.Medication
import com.example.aura.domain.model.MedicationSchedule
import com.example.aura.domain.usecase.medication.MedicationUseCases
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
    val startTime: String = ""
)

class MedicationViewModel(
    private val useCases: MedicationUseCases
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(MedicationUiState())
    val uiState: StateFlow<MedicationUiState> = _uiState.asStateFlow()

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
            startTime = state.startTime
        )

        executeSafeCall(
            block = {
                useCases.addMedication(medication)
            },
            onSuccess = {
                _uiState.update {
                    it.copy(
                        name = "",
                        doseDetails = "",
                        interval = "",
                        startTime = "",
                        error = null
                    )
                }
            },
            onError = { throwable ->
                _uiState.update { it.copy(error = throwable.localizedMessage ?: "Erro ao salvar") }
            }
        )
    }

    fun onDeleteMedication(schedule: MedicationSchedule) {
        executeSafeCall(
            block = {
                useCases.deleteMedication(schedule.medication)
            },
            onSuccess = {
                // A lista reativa (Flow) cuidará de atualizar a UI automaticamente
            },
            onError = { throwable ->
                _uiState.update { it.copy(error = throwable.localizedMessage ?: "Erro ao deletar") }
            }
        )
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
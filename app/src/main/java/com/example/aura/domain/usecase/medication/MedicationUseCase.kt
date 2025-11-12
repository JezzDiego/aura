package com.example.aura.domain.usecase.medication

import com.example.aura.core.ResultWrapper
import com.example.aura.domain.model.Medication
import com.example.aura.domain.model.MedicationSchedule
import com.example.aura.domain.repository.MedicationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class MedicationUseCases(
    val getSavedMedications: GetSavedMedicationsUseCase,
    val addMedication: AddMedicationUseCase,
    val deleteMedication: DeleteMedicationUseCase,
    val searchMedications: SearchMedicationsUseCase
)

class GetSavedMedicationsUseCase(
    private val repository: MedicationRepository
) {
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    operator fun invoke(): Flow<List<MedicationSchedule>> {
        return repository.getSavedMedications().map { medications ->
            medications.map { medication ->
                MedicationSchedule(
                    medication = medication,
                    scheduleTimes = calculateSchedule(medication.startTime, medication.intervalInHours)
                )
            }
        }
    }

    private fun calculateSchedule(startTimeStr: String, interval: Int): List<String> {
        if (interval <= 0) return listOf(startTimeStr)

        val schedule = mutableListOf<String>()
        try {
            var currentTime = LocalTime.parse(startTimeStr, timeFormatter)
            val dosesPerDay = 24 / interval

            for (i in 0 until dosesPerDay) {
                schedule.add(currentTime.format(timeFormatter))
                currentTime = currentTime.plusHours(interval.toLong())
            }
        } catch (e: Exception) {
            schedule.clear()
            schedule.add(startTimeStr)
        }
        return schedule
    }
}

class AddMedicationUseCase(
    private val repository: MedicationRepository
) {
    suspend operator fun invoke(medication: Medication) {
        repository.addMedication(medication)
    }
}

class DeleteMedicationUseCase(
    private val repository: MedicationRepository
) {
    suspend operator fun invoke(medication: Medication) {
        repository.deleteMedication(medication)
    }
}

class SearchMedicationsUseCase(
    private val repository: MedicationRepository
) {
    suspend operator fun invoke(query: String): ResultWrapper<List<Medication>> {
        return repository.searchMedication(query)
    }
}
package com.example.aura.data.local.datasource

import com.example.aura.data.local.dao.MedicationDao
import com.example.aura.data.local.entity.MedicationEntity
import kotlinx.coroutines.flow.Flow

class MedicationLocalDataSource(
    private val medicationDao: MedicationDao
) {
    fun getAllMedications(): Flow<List<MedicationEntity>> {
        return medicationDao.getSavedMedications()
    }

    suspend fun save(medication: MedicationEntity) {
        medicationDao.insertMedication(medication)
    }

    suspend fun delete(medication: MedicationEntity) {
        medicationDao.deleteMedication(medication)
    }
}
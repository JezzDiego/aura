package com.example.aura.data.local.datasource

import com.example.aura.data.local.dao.LaboratoryDao
import com.example.aura.data.local.entity.LaboratoryEntity

class LaboratoryLocalDataSource(private val dao: LaboratoryDao) {

    suspend fun getAll(): List<LaboratoryEntity> = dao.getAll()

    suspend fun getById(id: String): LaboratoryEntity? = dao.getById(id)

    suspend fun saveAll(labs: List<LaboratoryEntity>) = dao.insertAll(labs)
}

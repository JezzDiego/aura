package com.example.aura.data.local.dao

import androidx.room.*
import com.example.aura.data.local.entity.LaboratoryEntity

@Dao
interface LaboratoryDao {

    @Query("SELECT * FROM laboratories")
    suspend fun getAll(): List<LaboratoryEntity>

    @Query("SELECT * FROM laboratories WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): LaboratoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(labs: List<LaboratoryEntity>)
}

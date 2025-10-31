package com.example.aura.data.local.dao

import androidx.room.*
import com.example.aura.data.local.entity.ExamEntity

@Dao
interface ExamDao {

    @Transaction
    @Query("SELECT * FROM exams")
    suspend fun getAllExams(): List<ExamEntity>

    @Transaction
    @Query("SELECT * FROM exams WHERE id = :id LIMIT 1")
    suspend fun getExamById(id: String): ExamEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exam: ExamEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(exams: List<ExamEntity>)

    @Query("DELETE FROM exams WHERE id = :id")
    suspend fun deleteExam(id: String)

    @Query("DELETE FROM exams")
    suspend fun clearAll()

    @Transaction
    suspend fun clearAndInsertAll(exams: List<ExamEntity>) {
        clearAll()
        insertAll(exams)
    }
}

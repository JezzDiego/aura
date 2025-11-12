package com.example.aura.data.local.dao

import androidx.room.*
import com.example.aura.data.local.entity.ExamEntity
import com.example.aura.data.local.entity.ExamWithDetails
import com.example.aura.data.local.entity.ExamResultEntity
import com.example.aura.data.local.entity.FileAttachmentEntity

@Dao
interface ExamDao {

    @Transaction
    @Query("SELECT * FROM exams")
    suspend fun getAllExams(): List<ExamWithDetails>

    @Transaction
    @Query("SELECT * FROM exams WHERE id = :id LIMIT 1")
    suspend fun getExamById(id: String): ExamWithDetails?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exam: ExamEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(exams: List<ExamEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResults(results: List<ExamResultEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttachments(attachments: List<FileAttachmentEntity>)

    @Query("DELETE FROM exams WHERE id = :id")
    suspend fun deleteExam(id: String)

    @Query("DELETE FROM exams")
    suspend fun clearAll()

    @Transaction
    suspend fun clearAndInsertAll(exams: List<ExamEntity>) {
        clearAll()
        insertAll(exams)
    }

    // Helper para inserir um Exam completo (deve ser chamada por camadas superiores que possuem os entities relacionados)
    @Transaction
    suspend fun insertExamWithDetails(exam: ExamEntity, results: List<ExamResultEntity> = emptyList(), attachments: List<FileAttachmentEntity> = emptyList()) {
        insert(exam)
        if (results.isNotEmpty()) insertResults(results)
        if (attachments.isNotEmpty()) insertAttachments(attachments)
    }
}

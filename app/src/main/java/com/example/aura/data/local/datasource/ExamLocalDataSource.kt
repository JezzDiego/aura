package com.example.aura.data.local.datasource

import com.example.aura.data.local.dao.ExamDao
import com.example.aura.data.local.entity.ExamEntity
import com.example.aura.data.local.entity.ExamWithDetails
import com.example.aura.data.local.entity.ExamResultEntity
import com.example.aura.data.local.entity.FileAttachmentEntity

class ExamLocalDataSource(private val dao: ExamDao) {

    suspend fun getAll(): List<ExamWithDetails> = dao.getAllExams()

    suspend fun getById(id: String): ExamWithDetails? = dao.getExamById(id)

    suspend fun save(exam: ExamEntity) = dao.insert(exam)

    suspend fun saveAll(exams: List<ExamEntity>) = dao.insertAll(exams)

    suspend fun saveWithDetails(exam: ExamEntity, results: List<ExamResultEntity> = emptyList(), attachments: List<FileAttachmentEntity> = emptyList()) {
        dao.insertExamWithDetails(exam, results, attachments)
    }

    suspend fun delete(id: String) = dao.deleteExam(id)

    suspend fun clear() = dao.clearAll()

    suspend fun replaceAll(exams: List<ExamEntity>) = dao.clearAndInsertAll(exams)
}

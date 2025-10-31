package com.example.aura.data.local.datasource

import com.example.aura.data.local.dao.ExamDao
import com.example.aura.data.local.entity.ExamEntity

class ExamLocalDataSource(private val dao: ExamDao) {

    suspend fun getAll(): List<ExamEntity> = dao.getAllExams()

    suspend fun getById(id: String): ExamEntity? = dao.getExamById(id)

    suspend fun save(exam: ExamEntity) = dao.insert(exam)

    suspend fun saveAll(exams: List<ExamEntity>) = dao.insertAll(exams)

    suspend fun delete(id: String) = dao.deleteExam(id)

    suspend fun clear() = dao.clearAll()

    suspend fun replaceAll(exams: List<ExamEntity>) = dao.clearAndInsertAll(exams)
}

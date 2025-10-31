package com.example.aura.data.repository

import com.example.aura.data.local.dao.ExamDao
import com.example.aura.data.remote.api.ExamApi
import com.example.aura.data.mapper.toDomain
import com.example.aura.data.mapper.toEntity
import com.example.aura.domain.model.Exam
import com.example.aura.domain.model.ShareLink
import com.example.aura.domain.repository.ExamRepository
import com.example.aura.core.ResultWrapper
import com.example.aura.data.mapper.toDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class ExamRepositoryImpl(
    private val api: ExamApi,
    private val dao: ExamDao
) : ExamRepository {

    override suspend fun getAllExams(): List<Exam> = withContext(Dispatchers.IO) {
        val localExams = dao.getAllExams()
        if (localExams.isNotEmpty()) {
            localExams.map { it.toDomain() }
        } else {
            // Chama a API e salva no banco
            val apiExams = api.getExams().map { it.toDomain() }
            dao.insertAll(apiExams.map { it.toEntity() })
            apiExams
        }
    }

    override suspend fun getExamById(id: String): Exam? = withContext(Dispatchers.IO) {
        dao.getExamById(id)?.toDomain()
    }

    override suspend fun addExam(exam: Exam) = withContext(Dispatchers.IO) {
        dao.insert(exam.toEntity())
        // Sincroniza com o servidor (fallback silencioso)
        try {
            api.uploadExam(exam.toDto()) // ex: envia DTO
        } catch (e: Exception) {
            e.printStackTrace() // ou fila offline com WorkManager
        }
    }

    override suspend fun deleteExam(id: String) = withContext(Dispatchers.IO) {
        dao.deleteExam(id)
        try {
            api.deleteExam(id)
        } catch (e: Exception) {
            // Se não tiver rede, pode marcar como “pending delete”
        }
    }

    override suspend fun shareExam(id: String): ShareLink = withContext(Dispatchers.IO) {
        val randomToken = UUID.randomUUID().toString()
        ShareLink(
            id = randomToken,
            examId = id,
            generatedAt = System.currentTimeMillis(),
            expiresAt = System.currentTimeMillis() + 24 * 60 * 60 * 1000,
            linkUrl = "https://aura.app/share/$randomToken",
            isActive = true
        )
    }

    override suspend fun syncWithRemote(): List<Exam> = withContext(Dispatchers.IO) {
        val remoteExams = api.getExams().map { it.toDomain() }
        dao.clearAndInsertAll(remoteExams.map { it.toEntity() })
        remoteExams
    }
}

package com.example.aura.data.repository

import com.example.aura.data.local.datasource.ExamLocalDataSource
import com.example.aura.data.mapper.toDomain
import com.example.aura.data.mapper.toEntity
import com.example.aura.domain.model.Exam
import com.example.aura.domain.model.ShareLink
import com.example.aura.domain.repository.ExamRepository
import com.example.aura.data.mapper.toDto
import com.example.aura.data.remote.datasource.ExamRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class ExamRepositoryImpl(
    private val localDS: ExamLocalDataSource,
    private val remoteDS: ExamRemoteDataSource
) : ExamRepository {

    override suspend fun getAllExams(): List<Exam> = withContext(Dispatchers.IO) {
        val localExams = localDS.getAll()
        if (localExams.isNotEmpty()) {
            localExams.map { it.toDomain() }
        } else {
            val apiExams = remoteDS.getExams().map { it.toDomain() }
            localDS.saveAll(apiExams.map { it.toEntity() })
            apiExams
        }
    }

    override suspend fun getExamById(id: String): Exam? = withContext(Dispatchers.IO) {
        localDS.getById(id)?.toDomain()
    }

    override suspend fun addExam(exam: Exam) = withContext(Dispatchers.IO) {
        // Garantir que o exame tenha um ID único antes de salvar localmente
        val examId = if (exam.id.isBlank()) UUID.randomUUID().toString() else exam.id
        val examWithId = if (exam.id == examId) exam else exam.copy(id = examId)

        localDS.save(examWithId.toEntity())
        // Sincroniza com o servidor (fallback silencioso)
        try {
            remoteDS.uploadExam(examWithId.toDto()) // ex: envia DTO
        } catch (e: Exception) {
            e.printStackTrace() // ou fila offline com WorkManager
        }
    }

    override suspend fun deleteExam(id: String) = withContext(Dispatchers.IO) {
        localDS.delete(id)
        try {
            remoteDS.deleteExam(id)
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
        val remoteExams = remoteDS.getExams().map { it.toDomain() }
        localDS.replaceAll(remoteExams.map { it.toEntity() })
        remoteExams
    }
}

package com.example.aura.data.repository

import android.util.Log
import com.example.aura.data.local.datasource.ExamLocalDataSource
import com.example.aura.data.mapper.toDomain
import com.example.aura.data.mapper.toEntity
import com.example.aura.data.mapper.toResultEntities
import com.example.aura.data.mapper.toAttachmentEntities
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
            // salva exam + results + attachments
            apiExams.forEach { exam ->
                localDS.saveWithDetails(exam.toEntity(), exam.toResultEntities(), exam.toAttachmentEntities())
            }
            apiExams
        }
    }

    override suspend fun getExamById(id: String): Exam? = withContext(Dispatchers.IO) {
        val localWrapper = localDS.getById(id)
        val local = localWrapper?.toDomain()
        // if local exists and has results, return it
        if (local != null && local.results.isNotEmpty()) return@withContext local

        // otherwise try remote and save details when found
        val remoteDto = try {
            remoteDS.getExamById(id)
        } catch (_: Exception) {
            null
        }

        if (remoteDto != null) {
            Log.i("ExamRepository", "remoteDto for id=$id -> results size=${remoteDto.results?.size}")
        } else {
            Log.i("ExamRepository", "remoteDto for id=$id is null")
        }

        val remote = remoteDto?.toDomain()
        if (remote != null) {
            // save
            localDS.saveWithDetails(remote.toEntity(), remote.toResultEntities(), remote.toAttachmentEntities())

            // re-query local to confirm
            val afterSave = localDS.getById(id)
            Log.i("ExamRepository", "afterSave local results size=${afterSave?.results?.size}")

            return@withContext remote
        }

        // fallback: return local even if empty or null
        local
    }

    override suspend fun addExam(exam: Exam) = withContext(Dispatchers.IO) {
        localDS.saveWithDetails(exam.toEntity(), exam.toResultEntities(), exam.toAttachmentEntities())
        // Sincroniza com o servidor (fallback silencioso)
        try {
            remoteDS.uploadExam(exam.toDto()) // ex: envia DTO
        } catch (_: Exception) {
            // ignorado
        }
    }

    override suspend fun deleteExam(id: String) = withContext(Dispatchers.IO) {
        localDS.delete(id)
        try {
            remoteDS.deleteExam(id)
        } catch (_: Exception) {
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
        // substitui local: limpar e salvar com detalhes
        // clear and insert all entities (note: replaceAll currently only inserts ExamEntity list)
        // para simplicidade, podemos limpar e re-inserir usando saveWithDetails por exam
        localDS.clear()
        remoteExams.forEach { exam ->
            localDS.saveWithDetails(exam.toEntity(), exam.toResultEntities(), exam.toAttachmentEntities())
        }
        remoteExams
    }
}

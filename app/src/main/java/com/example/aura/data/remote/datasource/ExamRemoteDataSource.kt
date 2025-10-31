package com.example.aura.data.remote.datasource

import com.example.aura.data.remote.api.ExamApi
import com.example.aura.data.remote.dto.ExamDto

class ExamRemoteDataSource(private val api: ExamApi) {

    suspend fun getExams(): List<ExamDto> = api.getExams()

    suspend fun getExamById(id: String): ExamDto = api.getExamById(id)

    suspend fun uploadExam(dto: ExamDto) = api.uploadExam(dto)

    suspend fun deleteExam(id: String) = api.deleteExam(id)
}

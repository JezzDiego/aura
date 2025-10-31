package com.example.aura.domain.repository

import com.example.aura.domain.model.Exam
import com.example.aura.domain.model.ShareLink

interface ExamRepository {

    suspend fun getAllExams(): List<Exam>

    suspend fun getExamById(id: String): Exam?

    suspend fun addExam(exam: Exam)

    suspend fun deleteExam(id: String)

    suspend fun shareExam(id: String): ShareLink

    suspend fun syncWithRemote(): List<Exam>  // sincronização opcional
}

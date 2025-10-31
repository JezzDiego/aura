package com.example.aura.data.remote.api

import com.example.aura.data.remote.dto.ExamDto
import retrofit2.http.*

interface ExamApi {

    @GET("exams")
    suspend fun getExams(): List<ExamDto>

    @GET("exams/{id}")
    suspend fun getExamById(@Path("id") id: String): ExamDto

    @POST("exams")
    suspend fun uploadExam(@Body exam: ExamDto)

    @DELETE("exams/{id}")
    suspend fun deleteExam(@Path("id") id: String)
}

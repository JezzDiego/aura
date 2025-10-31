package com.example.aura.domain.usecase.exam

import com.example.aura.domain.model.Exam
import com.example.aura.domain.model.ShareLink
import com.example.aura.domain.repository.ExamRepository


class GetExamListUseCase(private val repository: ExamRepository) {
    suspend operator fun invoke(): List<Exam> = repository.getAllExams()
}

class GetExamByIdUseCase(private val repository: ExamRepository) {
    suspend operator fun invoke(id: String): Exam? = repository.getExamById(id)
}

class CreateExamUseCase(private val repository: ExamRepository) {
    suspend operator fun invoke(exam: Exam) {
        require(exam.title.isNotBlank()) { "O nome do exame não pode estar vazio" }
        repository.addExam(exam)
    }
}

class DeleteExamUseCase(private val repository: ExamRepository) {
    suspend operator fun invoke(id: String) = repository.deleteExam(id)
}

class ShareExamUseCase(private val repository: ExamRepository) {
    suspend operator fun invoke(examId: String): ShareLink = repository.shareExam(examId)
}

data class ExamUseCases(
    val getExamList: GetExamListUseCase,
    val getExamById: GetExamByIdUseCase,
    val createExam: CreateExamUseCase,
    val deleteExam: DeleteExamUseCase,
    val shareExam: ShareExamUseCase
)

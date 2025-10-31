package com.example.aura.domain.model

data class Exam(
    val id: String,
    val userId: String,
    val title: String,
    val category: Category,
    val date: Long,
    val laboratory: Laboratory?,
    val results: List<ExamResult>,
    val attachments: List<FileAttachment> = emptyList(),
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long? = null
)

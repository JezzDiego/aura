package com.example.aura.domain.model

data class FileAttachment(
    val id: String,
    val examId: String,
    val fileName: String,
    val fileUrl: String?,   // Pode ser local ou remoto
    val fileType: String,   // Ex: "pdf", "jpg"
    val uploadedAt: Long
)

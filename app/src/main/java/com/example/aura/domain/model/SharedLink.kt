package com.example.aura.domain.model

data class ShareLink(
    val id: String,
    val examId: String,
    val generatedAt: Long,
    val expiresAt: Long,
    val linkUrl: String,
    val isActive: Boolean = true
)

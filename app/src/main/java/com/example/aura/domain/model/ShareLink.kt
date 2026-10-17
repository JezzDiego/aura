package com.example.aura.domain.model

data class ShareLink(
    val id: String,
    val establishmentId: String,
    val generatedAt: Long,
    val linkUrl: String,
    val message: String,
)

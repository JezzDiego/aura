package com.example.aura.domain.model

data class ExamResult(
    val fieldName: String,         // Ex: "Glicose"
    val resultValue: String,       // Ex: "94 mg/dL"
    val referenceRange: String?,   // Ex: "70 - 100 mg/dL"
    val unit: String? = null,      // Ex: "mg/dL"
    val interpretation: String? = null // Analise automática, ex: "Normal"
)

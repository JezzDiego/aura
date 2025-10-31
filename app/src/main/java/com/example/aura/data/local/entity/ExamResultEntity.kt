package com.example.aura.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exam_results")
data class ExamResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val examId: String,
    val fieldName: String,
    val resultValue: String,
    val referenceRange: String?,
    val unit: String?,
    val interpretation: String?
)

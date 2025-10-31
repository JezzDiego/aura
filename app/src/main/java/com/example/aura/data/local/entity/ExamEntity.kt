package com.example.aura.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Embedded
import androidx.room.Relation

@Entity(tableName = "exams")
data class ExamEntity(
    @PrimaryKey val id: String,
    val userId : String,
    val title: String,
    val category: String,
    val date: Long,
    val labId: String?,
    val notes: String?
)

// Relacionamento 1:N com resultados
data class ExamWithResults(
    @Embedded val exam: ExamEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "examId"
    )
    val results: List<ExamResultEntity>
)

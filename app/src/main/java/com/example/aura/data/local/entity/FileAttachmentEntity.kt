package com.example.aura.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attachments")
data class FileAttachmentEntity(
    @PrimaryKey val id: String,
    val examId: String,
    val fileName: String,
    val fileUrl: String?,
    val fileType: String,
    val uploadedAt: Long
)

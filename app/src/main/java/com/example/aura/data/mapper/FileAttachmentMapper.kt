package com.example.aura.data.mapper

import com.example.aura.data.remote.dto.FileAttachmentDTO
import com.example.aura.domain.model.FileAttachment

fun FileAttachmentDTO.toDomain(): FileAttachment {
    return FileAttachment(
        id = id,
        examId = examId,
        fileName = fileName,
        fileUrl = fileUrl,
        fileType = fileType,
        uploadedAt = uploadedAt,
    )
}
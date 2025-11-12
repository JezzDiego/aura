package com.example.aura.data.mapper

import com.example.aura.data.local.entity.ExamEntity
import com.example.aura.data.remote.dto.ExamDto
import com.example.aura.data.remote.dto.ExamResultDto
import com.example.aura.data.remote.dto.FileAttachmentDTO
import com.example.aura.data.remote.dto.LaboratoryDTO
import com.example.aura.domain.model.*

fun ExamDto.toDomain(): Exam {
    return Exam(
        id = id,
        title = title,
        category = Category.valueOf(category.uppercase()),
        date = date.toLong(),
        laboratory = laboratory?.let {
            Laboratory(
                id = it.id,
                name = it.name,
                address = it.address,
                phone = it.phone,
                email = it.email
            )
        },
        results = results?.map { it.toDomain() } ?: emptyList(),
        attachments = attachments?.map { it.toDomain() } ?: emptyList(),
        userId = userId,
        createdAt = 1L,
    )
}

fun ExamResultDto.toDomain(): ExamResult {
    return ExamResult(
        fieldName = fieldName,
        resultValue = resultValue,
        referenceRange = referenceRange
    )
}

// Mapper Domain -> Local (pra salvar no banco)
fun Exam.toEntity(): ExamEntity {
    return ExamEntity(
        id = id,
        userId = userId,
        title = title,
        category = category.name,
        date = date,
        labId = laboratory?.id,
        labName = laboratory?.name,
        notes = notes
    )
}

fun Exam.toDto(): ExamDto {
    return ExamDto(
        id = id,
        title = title,
        category = category.name,
        date = date,
        laboratory = laboratory?.let {
            LaboratoryDTO(
                id = it.id,
                name = it.name,
                address = it.address,
                phone = it.phone,
                email = it.email
            )
        },
        results = results.map {
            ExamResultDto(
                fieldName = it.fieldName,
                resultValue = it.resultValue,
                referenceRange = it.referenceRange,
                unit = it.unit,
                interpretation = it.interpretation
            )
        },
        attachments = attachments.map {
            FileAttachmentDTO(
                fileName = it.fileName,
                fileUrl = it.fileUrl,
                id = it.id,
                examId = it.examId,
                fileType = it.fileType,
                uploadedAt = it.uploadedAt
            )
        },
        userId = userId
    )
}

// Mapper Local -> Domain (pra ler do banco)
fun ExamEntity.toDomain(): Exam {
    return Exam(
        id = id,
        title = title,
        category = Category.valueOf(category.uppercase()),
        date = date,
        laboratory = labName?.let { Laboratory(labId ?: it, it, null, null, null) } ?: labId?.let { Laboratory(it, it, null, null, null) },
        results = emptyList(),
        attachments = emptyList(),
        userId = userId,
        notes = notes,
        createdAt = 1L,
    )
}

package com.example.aura.data.mapper

import com.example.aura.data.local.entity.ExamEntity
import com.example.aura.data.local.entity.ExamWithDetails
import com.example.aura.data.local.entity.ExamResultEntity
import com.example.aura.data.local.entity.FileAttachmentEntity
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
        date = date,
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
        referenceRange = referenceRange,
        unit = unit,
        interpretation = interpretation
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
        notes = notes
    )
}

fun Exam.toResultEntities(): List<ExamResultEntity> {
    return results.map {
        ExamResultEntity(
            examId = id,
            fieldName = it.fieldName,
            resultValue = it.resultValue,
            referenceRange = it.referenceRange,
            unit = it.unit,
            interpretation = it.interpretation
        )
    }
}

fun Exam.toAttachmentEntities(): List<FileAttachmentEntity> {
    return attachments.map {
        FileAttachmentEntity(
            id = it.id,
            examId = id,
            fileName = it.fileName,
            fileUrl = it.fileUrl,
            fileType = it.fileType,
            uploadedAt = it.uploadedAt
        )
    }
}

fun Exam.toDto(): ExamDto {
    return ExamDto(
        id = id,
        userId = userId,
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
        }
    )
}

// Mapper Local -> Domain (pra ler do banco)
fun ExamEntity.toDomain(): Exam {
    return Exam(
        id = id,
        userId = userId,
        title = title,
        category = Category.valueOf(category.uppercase()),
        date = date,
        laboratory = labId?.let { Laboratory(it, it, null, null, null) },
        results = emptyList(), // use ExamWithDetails.toDomain para obter resultados
        attachments = emptyList(),
        notes = notes,
        createdAt = 1L,
    )
}

fun ExamWithDetails.toDomain(): Exam {
    val e = exam
    return Exam(
        id = e.id,
        userId = e.userId,
        title = e.title,
        category = Category.valueOf(e.category.uppercase()),
        date = e.date,
        laboratory = e.labId?.let { Laboratory(it, it, null, null, null) },
        results = results.map {
            ExamResult(
                fieldName = it.fieldName,
                resultValue = it.resultValue,
                referenceRange = it.referenceRange,
                unit = it.unit,
                interpretation = it.interpretation
            )
        },
        attachments = attachments.map {
            FileAttachment(
                id = it.id,
                examId = it.examId,
                fileName = it.fileName,
                fileUrl = it.fileUrl,
                fileType = it.fileType,
                uploadedAt = it.uploadedAt
            )
        },
        notes = e.notes,
        createdAt = 1L
    )
}

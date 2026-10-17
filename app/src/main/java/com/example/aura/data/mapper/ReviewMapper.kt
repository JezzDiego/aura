package com.example.aura.data.mapper

import com.example.aura.data.local.entity.ReviewEntity
import com.example.aura.data.remote.dto.ReviewDTO
import com.example.aura.domain.model.AccessibilityProfile
import com.example.aura.domain.model.FeatureStatus
import com.example.aura.domain.model.Review
import com.example.aura.domain.model.ReviewChecklistItem

private fun Map<String, String>.toChecklist(): Map<ReviewChecklistItem, FeatureStatus> =
    mapNotNull { (key, value) ->
        val item = key.toEnumOrNull<ReviewChecklistItem>()
        val status = value.toEnumOrNull<FeatureStatus>()
        if (item != null && status != null) item to status else null
    }.toMap()

private fun Map<ReviewChecklistItem, FeatureStatus>.toRaw(): Map<String, String> =
    entries.associate { it.key.name to it.value.name }

fun ReviewEntity.toDomain(): Review = Review(
    id = id,
    establishmentId = establishmentId,
    establishmentName = establishmentName,
    authorId = authorId,
    authorName = authorName,
    authorProfile = authorProfile.toEnumOrNull<AccessibilityProfile>(),
    comment = comment,
    checklist = checklist.toChecklist(),
    photos = photos,
    createdAt = createdAt,
    helpfulCount = helpfulCount,
    markedHelpful = markedHelpful,
)

fun Review.toEntity(): ReviewEntity = ReviewEntity(
    id = id,
    establishmentId = establishmentId,
    establishmentName = establishmentName,
    authorId = authorId,
    authorName = authorName,
    authorProfile = authorProfile?.name,
    comment = comment,
    checklist = checklist.toRaw(),
    photos = photos,
    createdAt = createdAt,
    helpfulCount = helpfulCount,
    markedHelpful = markedHelpful,
)

fun ReviewDTO.toDomain(): Review = Review(
    id = id,
    establishmentId = establishmentId,
    establishmentName = establishmentName.orEmpty(),
    authorId = authorId,
    authorName = authorName,
    authorProfile = authorProfile.toEnumOrNull<AccessibilityProfile>(),
    comment = comment,
    checklist = checklist.orEmpty().toChecklist(),
    photos = photos.orEmpty(),
    createdAt = createdAt,
    helpfulCount = helpfulCount ?: 0,
)

fun Review.toDto(): ReviewDTO = ReviewDTO(
    id = id,
    establishmentId = establishmentId,
    establishmentName = establishmentName,
    authorId = authorId,
    authorName = authorName,
    authorProfile = authorProfile?.name,
    comment = comment,
    checklist = checklist.toRaw(),
    photos = photos,
    createdAt = createdAt,
    helpfulCount = helpfulCount,
)

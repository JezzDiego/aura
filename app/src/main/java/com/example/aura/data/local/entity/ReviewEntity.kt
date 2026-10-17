package com.example.aura.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "reviews",
    indices = [Index("establishmentId"), Index("authorId")]
)
data class ReviewEntity(
    @PrimaryKey val id: String,
    val establishmentId: String,
    val establishmentName: String,
    val authorId: String,
    val authorName: String,
    val authorProfile: String?,
    val comment: String,
    /** Mapa ReviewChecklistItem -> FeatureStatus (via TypeConverter). */
    val checklist: Map<String, String>,
    val photos: List<String>,
    val createdAt: Long,
    val helpfulCount: Int,
    val markedHelpful: Boolean,
)

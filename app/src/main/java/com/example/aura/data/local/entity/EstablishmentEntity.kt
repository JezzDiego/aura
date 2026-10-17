package com.example.aura.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "establishments")
data class EstablishmentEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val categoryDetail: String,
    val address: String,
    val neighborhood: String,
    val city: String,
    val imageUrl: String?,
    val heroImageUrl: String?,
    val rating: Double,
    val reviewCount: Int,
    val distanceMeters: Int,
    val verifiedAt: String,
    val auditorsCount: Int,
    val conformityPercent: Int,
    val summary: String,
    val nearbyTransit: String?,
    val surroundings: String?,
    val latitude: Double,
    val longitude: Double,
    val isFavorite: Boolean = false,
)

@Entity(
    tableName = "accessibility_features",
    foreignKeys = [
        ForeignKey(
            entity = EstablishmentEntity::class,
            parentColumns = ["id"],
            childColumns = ["establishmentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("establishmentId")]
)
data class AccessibilityFeatureEntity(
    @PrimaryKey val id: String,
    val establishmentId: String,
    val category: String,
    val title: String,
    val shortLabel: String,
    val description: String,
    val status: String,
    val badge: String?,
    val filter: String?,
    val highlight: Boolean,
    val position: Int,
)

@Entity(
    tableName = "establishment_photos",
    foreignKeys = [
        ForeignKey(
            entity = EstablishmentEntity::class,
            parentColumns = ["id"],
            childColumns = ["establishmentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("establishmentId")]
)
data class EstablishmentPhotoEntity(
    @PrimaryKey val id: String,
    val establishmentId: String,
    val imageUrl: String,
    val title: String,
    val description: String,
    val position: Int,
)

/** Estabelecimento + checklist + fotos, retornado pelo DAO com @Transaction. */
data class EstablishmentWithDetails(
    @Embedded val establishment: EstablishmentEntity,
    @Relation(parentColumn = "id", entityColumn = "establishmentId")
    val features: List<AccessibilityFeatureEntity> = emptyList(),
    @Relation(parentColumn = "id", entityColumn = "establishmentId")
    val photos: List<EstablishmentPhotoEntity> = emptyList(),
)

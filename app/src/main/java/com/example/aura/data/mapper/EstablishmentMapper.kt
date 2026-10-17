package com.example.aura.data.mapper

import com.example.aura.data.local.entity.AccessibilityFeatureEntity
import com.example.aura.data.local.entity.EstablishmentEntity
import com.example.aura.data.local.entity.EstablishmentPhotoEntity
import com.example.aura.data.local.entity.EstablishmentWithDetails
import com.example.aura.data.remote.dto.AccessibilityFeatureDTO
import com.example.aura.data.remote.dto.EstablishmentDTO
import com.example.aura.data.remote.dto.EstablishmentPhotoDTO
import com.example.aura.domain.model.AccessibilityFeature
import com.example.aura.domain.model.AccessibilityFilter
import com.example.aura.domain.model.Establishment
import com.example.aura.domain.model.EstablishmentPhoto
import com.example.aura.domain.model.FeatureCategory
import com.example.aura.domain.model.FeatureStatus

internal inline fun <reified T : Enum<T>> String?.toEnumOrNull(): T? =
    this?.let { value -> enumValues<T>().firstOrNull { it.name.equals(value, ignoreCase = true) } }

// ---------- Local -> Domain ----------

fun EstablishmentWithDetails.toDomain(): Establishment = establishment.toDomain(
    features = features.sortedBy { it.position }.map { it.toDomain() },
    photos = photos.sortedBy { it.position }.map { it.toDomain() },
)

fun EstablishmentEntity.toDomain(
    features: List<AccessibilityFeature> = emptyList(),
    photos: List<EstablishmentPhoto> = emptyList(),
): Establishment = Establishment(
    id = id,
    name = name,
    category = category,
    categoryDetail = categoryDetail,
    address = address,
    neighborhood = neighborhood,
    city = city,
    imageUrl = imageUrl,
    heroImageUrl = heroImageUrl,
    rating = rating,
    reviewCount = reviewCount,
    distanceMeters = distanceMeters,
    verifiedAt = verifiedAt,
    auditorsCount = auditorsCount,
    conformityPercent = conformityPercent,
    summary = summary,
    nearbyTransit = nearbyTransit,
    surroundings = surroundings,
    latitude = latitude,
    longitude = longitude,
    features = features,
    photos = photos,
    isFavorite = isFavorite,
)

fun AccessibilityFeatureEntity.toDomain(): AccessibilityFeature = AccessibilityFeature(
    id = id,
    establishmentId = establishmentId,
    category = category.toEnumOrNull<FeatureCategory>() ?: FeatureCategory.ACCESS_ENTRY,
    title = title,
    shortLabel = shortLabel,
    description = description,
    status = status.toEnumOrNull<FeatureStatus>() ?: FeatureStatus.UNKNOWN,
    badge = badge,
    filter = filter.toEnumOrNull<AccessibilityFilter>(),
    highlight = highlight,
)

fun EstablishmentPhotoEntity.toDomain(): EstablishmentPhoto = EstablishmentPhoto(
    id = id,
    establishmentId = establishmentId,
    imageUrl = imageUrl,
    title = title,
    description = description,
)

// ---------- Domain -> Local ----------

fun Establishment.toEntity(): EstablishmentEntity = EstablishmentEntity(
    id = id,
    name = name,
    category = category,
    categoryDetail = categoryDetail,
    address = address,
    neighborhood = neighborhood,
    city = city,
    imageUrl = imageUrl,
    heroImageUrl = heroImageUrl,
    rating = rating,
    reviewCount = reviewCount,
    distanceMeters = distanceMeters,
    verifiedAt = verifiedAt,
    auditorsCount = auditorsCount,
    conformityPercent = conformityPercent,
    summary = summary,
    nearbyTransit = nearbyTransit,
    surroundings = surroundings,
    latitude = latitude,
    longitude = longitude,
    isFavorite = isFavorite,
)

fun Establishment.toFeatureEntities(): List<AccessibilityFeatureEntity> =
    features.mapIndexed { index, feature ->
        AccessibilityFeatureEntity(
            id = feature.id,
            establishmentId = id,
            category = feature.category.name,
            title = feature.title,
            shortLabel = feature.shortLabel,
            description = feature.description,
            status = feature.status.name,
            badge = feature.badge,
            filter = feature.filter?.name,
            highlight = feature.highlight,
            position = index,
        )
    }

fun Establishment.toPhotoEntities(): List<EstablishmentPhotoEntity> =
    photos.mapIndexed { index, photo ->
        EstablishmentPhotoEntity(
            id = photo.id,
            establishmentId = id,
            imageUrl = photo.imageUrl,
            title = photo.title,
            description = photo.description,
            position = index,
        )
    }

// ---------- Remote <-> Domain (contratos prontos para quando houver API) ----------

fun EstablishmentDTO.toDomain(): Establishment = Establishment(
    id = id,
    name = name,
    category = category,
    categoryDetail = categoryDetail ?: category,
    address = address,
    neighborhood = neighborhood.orEmpty(),
    city = city.orEmpty(),
    imageUrl = imageUrl,
    heroImageUrl = heroImageUrl,
    rating = rating ?: 0.0,
    reviewCount = reviewCount ?: 0,
    distanceMeters = distanceMeters ?: 0,
    verifiedAt = verifiedAt.orEmpty(),
    auditorsCount = auditorsCount ?: 0,
    conformityPercent = conformityPercent ?: 0,
    summary = summary.orEmpty(),
    nearbyTransit = nearbyTransit,
    surroundings = surroundings,
    latitude = latitude ?: 0.0,
    longitude = longitude ?: 0.0,
    features = features.orEmpty().map { it.toDomain(id) },
    photos = photos.orEmpty().map { it.toDomain(id) },
)

fun AccessibilityFeatureDTO.toDomain(establishmentId: String): AccessibilityFeature = AccessibilityFeature(
    id = id,
    establishmentId = establishmentId,
    category = category.toEnumOrNull<FeatureCategory>() ?: FeatureCategory.ACCESS_ENTRY,
    title = title,
    shortLabel = shortLabel ?: title,
    description = description.orEmpty(),
    status = status.toEnumOrNull<FeatureStatus>() ?: FeatureStatus.UNKNOWN,
    badge = badge,
    filter = filter.toEnumOrNull<AccessibilityFilter>(),
    highlight = highlight ?: false,
)

fun EstablishmentPhotoDTO.toDomain(establishmentId: String): EstablishmentPhoto = EstablishmentPhoto(
    id = id,
    establishmentId = establishmentId,
    imageUrl = imageUrl,
    title = title.orEmpty(),
    description = description.orEmpty(),
)

fun Establishment.toDto(): EstablishmentDTO = EstablishmentDTO(
    id = id,
    name = name,
    category = category,
    categoryDetail = categoryDetail,
    address = address,
    neighborhood = neighborhood,
    city = city,
    imageUrl = imageUrl,
    heroImageUrl = heroImageUrl,
    rating = rating,
    reviewCount = reviewCount,
    distanceMeters = distanceMeters,
    verifiedAt = verifiedAt,
    auditorsCount = auditorsCount,
    conformityPercent = conformityPercent,
    summary = summary,
    nearbyTransit = nearbyTransit,
    surroundings = surroundings,
    latitude = latitude,
    longitude = longitude,
    features = features.map {
        AccessibilityFeatureDTO(
            id = it.id,
            category = it.category.name,
            title = it.title,
            shortLabel = it.shortLabel,
            description = it.description,
            status = it.status.name,
            badge = it.badge,
            filter = it.filter?.name,
            highlight = it.highlight,
        )
    },
    photos = photos.map { EstablishmentPhotoDTO(it.id, it.imageUrl, it.title, it.description) },
)

package com.example.aura.data.remote.dto

import com.google.gson.annotations.SerializedName

data class EstablishmentDTO(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("category") val category: String,
    @SerializedName("categoryDetail") val categoryDetail: String?,
    @SerializedName("address") val address: String,
    @SerializedName("neighborhood") val neighborhood: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("heroImageUrl") val heroImageUrl: String?,
    @SerializedName("rating") val rating: Double?,
    @SerializedName("reviewCount") val reviewCount: Int?,
    @SerializedName("distanceMeters") val distanceMeters: Int?,
    @SerializedName("verifiedAt") val verifiedAt: String?,
    @SerializedName("auditorsCount") val auditorsCount: Int?,
    @SerializedName("conformityPercent") val conformityPercent: Int?,
    @SerializedName("summary") val summary: String?,
    @SerializedName("nearbyTransit") val nearbyTransit: String?,
    @SerializedName("surroundings") val surroundings: String?,
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("longitude") val longitude: Double?,
    @SerializedName("features") val features: List<AccessibilityFeatureDTO>?,
    @SerializedName("photos") val photos: List<EstablishmentPhotoDTO>?,
)

data class AccessibilityFeatureDTO(
    @SerializedName("id") val id: String,
    @SerializedName("category") val category: String,
    @SerializedName("title") val title: String,
    @SerializedName("shortLabel") val shortLabel: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("status") val status: String,
    @SerializedName("badge") val badge: String?,
    @SerializedName("filter") val filter: String?,
    @SerializedName("highlight") val highlight: Boolean?,
)

data class EstablishmentPhotoDTO(
    @SerializedName("id") val id: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
)

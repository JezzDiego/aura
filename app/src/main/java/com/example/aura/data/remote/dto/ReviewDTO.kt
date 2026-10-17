package com.example.aura.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ReviewDTO(
    @SerializedName("id") val id: String,
    @SerializedName("establishmentId") val establishmentId: String,
    @SerializedName("establishmentName") val establishmentName: String?,
    @SerializedName("authorId") val authorId: String,
    @SerializedName("authorName") val authorName: String,
    @SerializedName("authorProfile") val authorProfile: String?,
    @SerializedName("comment") val comment: String,
    @SerializedName("checklist") val checklist: Map<String, String>?,
    @SerializedName("photos") val photos: List<String>?,
    @SerializedName("createdAt") val createdAt: Long,
    @SerializedName("helpfulCount") val helpfulCount: Int?,
)

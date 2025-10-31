package com.example.aura.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ExamDto(
    @SerializedName("id") val id: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("title") val title: String,
    @SerializedName("category") val category: String,
    @SerializedName("date") val date: Long,
    @SerializedName("laboratory") val laboratory: LaboratoryDTO?,
    @SerializedName("results") val results: List<ExamResultDto>?,
    @SerializedName("attachments") val attachments: List<FileAttachmentDTO>?
)

data class ExamResultDto(
    @SerializedName("fieldName") val fieldName: String,
    @SerializedName("resultValue") val resultValue: String,
    @SerializedName("referenceRange") val referenceRange: String?,
    @SerializedName("unit") val unit: String?,
    @SerializedName("interpretation") val interpretation: String?
)

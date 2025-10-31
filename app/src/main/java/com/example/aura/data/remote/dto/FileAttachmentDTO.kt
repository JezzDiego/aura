package com.example.aura.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FileAttachmentDTO(
    @SerializedName("id") val id: String,
    @SerializedName("examId") val examId: String,
    @SerializedName("fileName") val fileName: String,
    @SerializedName("fileUrl") val fileUrl: String?,
    @SerializedName("fileType") val fileType: String,
    @SerializedName("uploadedAt") val uploadedAt: Long
)

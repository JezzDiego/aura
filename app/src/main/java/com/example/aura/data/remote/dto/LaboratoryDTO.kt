package com.example.aura.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LaboratoryDTO(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("address") val address: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("email") val email: String?
)

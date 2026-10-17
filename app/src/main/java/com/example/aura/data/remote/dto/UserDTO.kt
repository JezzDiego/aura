package com.example.aura.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserDTO(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("profile") val profile: String?,
    @SerializedName("preferredFilters") val preferredFilters: List<String>?,
)

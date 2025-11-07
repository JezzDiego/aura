package com.example.aura.data.remote.dto

import com.example.aura.domain.model.SubscriptionPlan
import com.google.gson.annotations.SerializedName

data class UserDTO(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("birthDate") val birthDate: String?,
    @SerializedName("gender") val gender: String?,
    @SerializedName("plan") val plan: SubscriptionPlan
)

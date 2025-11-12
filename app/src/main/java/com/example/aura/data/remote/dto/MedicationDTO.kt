package com.example.aura.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MedicationDTO(
    @SerialName("id_api")
    val apiId: String,

    @SerialName("nome_medicamento")
    val name: String,

    @SerialName("dosagem_forca")
    val strength: String
)


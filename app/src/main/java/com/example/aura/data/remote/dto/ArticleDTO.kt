package com.example.aura.data.model

import com.google.gson.annotations.SerializedName

data class ArticleDTO(
    @SerializedName("id") val id: String,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("descricao") val descricao: String,
    @SerializedName("fonte") val fonte: String,
    @SerializedName("url") val url: String,
    @SerializedName("imagem") val imagem: String,
    @SerializedName("data_publicacao") val dataPublicacao: String,
    @SerializedName("conteudo") val conteudo: String
)

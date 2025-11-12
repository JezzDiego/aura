package com.example.aura.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "articles")
data class Article(
    @PrimaryKey()
    val id: String,
    val titulo: String,
    val descricao: String,
    val fonte: String,
    val url: String,
    val imagem: String,
    val dataPublicacao: String,
    val conteudo: String
)
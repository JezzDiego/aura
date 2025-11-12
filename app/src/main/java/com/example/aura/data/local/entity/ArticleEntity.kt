package com.example.aura.data.local.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val id: String,
    val titulo: String,
    val descricao: String,
    val fonte: String,
    val url: String,
    val imagem: String,
    val dataPublicacao: String,
    val conteudo: String
)
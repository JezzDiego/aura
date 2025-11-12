package com.example.aura.domain.model

data class Article(
    val id: String,
    val titulo: String,
    val descricao: String,
    val fonte: String,
    val url: String,
    val imagem: String,
    val dataPublicacao: String,
    val conteudo: String
)
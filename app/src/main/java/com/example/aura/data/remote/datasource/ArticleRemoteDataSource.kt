package com.example.aura.data.remote.datasource

import com.example.aura.data.remote.api.ArticlesApi
import com.example.aura.data.remote.dto.ArticleDTO

class ArticleRemoteDataSource(private val api: ArticlesApi){
    suspend fun getAll(): List<ArticleDTO> = api.getArticles()

    suspend fun getById(id: String): ArticleDTO = api.getArticleById(id)
}
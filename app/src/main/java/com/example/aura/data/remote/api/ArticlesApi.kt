package com.example.aura.data.remote.api

import com.example.aura.data.remote.dto.ArticleDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface ArticlesApi{
    @GET("articles")
    suspend fun getArticles(): List<ArticleDTO>

    @GET("articles/{id}")
    suspend fun getArticleById(@Path("id") id: String): ArticleDTO
}
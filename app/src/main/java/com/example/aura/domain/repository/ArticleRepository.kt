package com.example.aura.domain.repository

import com.example.aura.domain.model.Article

interface ArticleRepository {
    suspend fun getAllArticlesAPI(): List<Article>

    suspend fun getAllArticlesDAO(): List<Article>
    suspend fun getArticleById(id: String): Article?
    suspend fun toggleArticleSavedStatus(article: Article): Boolean

    suspend fun isArticleSaved(id: String): Boolean
}
package com.example.aura.data.local.datasource

import com.example.aura.data.local.dao.ArticleDao
import com.example.aura.data.local.entity.ArticleEntity
import com.example.aura.domain.model.Article

class ArticleLocalDataSource(private val dao: ArticleDao) {
    suspend fun getAll(): List<ArticleEntity> = dao.getAllArticles()
    suspend fun getById(id: String): ArticleEntity? = dao.getArticleById(id)
    suspend fun insert(article: ArticleEntity) = dao.insert(article)
    suspend fun delete(article: ArticleEntity) = dao.delete(article)
}
package com.example.aura.data.repository

import com.example.aura.data.local.dao.ArticleDao
import com.example.aura.data.mapper.toDomain
import com.example.aura.data.mapper.toEntity
import com.example.aura.data.remote.api.ArticlesApi
import com.example.aura.domain.model.Article
import com.example.aura.domain.repository.ArticleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ArticleRepositoryImpl(
    private val api: ArticlesApi,
    private val dao: ArticleDao
) : ArticleRepository{

    override suspend fun getAllArticlesAPI(): List<Article> = withContext(Dispatchers.IO){
        val apiArticle = api.getArticles().map {it.toDomain()}
        apiArticle
    }

    override suspend fun getAllArticlesDAO(): List<Article> = withContext(Dispatchers.IO) {
        dao.getAllArticles().map { it.toDomain() }
    }

    override suspend fun getArticleById(id: String): Article? = withContext(Dispatchers.IO) {

        dao.getArticleById(id)?.toDomain() ?: api.getArticlesById(id).toDomain()
    }

    override suspend fun toggleArticleSavedStatus(article: Article): Boolean = withContext(Dispatchers.IO){
        val existingArticle = dao.getArticleById(article.id)

        if(existingArticle != null){
            dao.delete(article.toEntity())
            return@withContext false
        }else{
            dao.insert(article.toEntity())
            return@withContext true
        }
    }

    override suspend fun isArticleSaved(id: String): Boolean = withContext(Dispatchers.IO){
        return@withContext dao.getArticleById(id) != null
    }
}
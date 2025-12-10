package com.example.aura.data.repository

import com.example.aura.data.local.dao.ArticleDao
import com.example.aura.data.local.datasource.ArticleLocalDataSource
import com.example.aura.data.mapper.toDomain
import com.example.aura.data.mapper.toEntity
import com.example.aura.data.remote.api.ArticlesApi
import com.example.aura.data.remote.datasource.ArticleRemoteDataSource
import com.example.aura.domain.model.Article
import com.example.aura.domain.repository.ArticleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ArticleRepositoryImpl(
    private val localDS: ArticleLocalDataSource,
    private val remoteDS: ArticleRemoteDataSource
) : ArticleRepository{

    override suspend fun getAllArticlesAPI(): List<Article> = withContext(Dispatchers.IO){
        val apiArticle = remoteDS.getAll().map {it.toDomain()}
        apiArticle
    }

    override suspend fun getAllArticlesDAO(): List<Article> = withContext(Dispatchers.IO) {
        localDS.getAll().map { it.toDomain() }
    }

    override suspend fun getArticleById(id: String): Article? = withContext(Dispatchers.IO) {

        localDS.getById(id)?.toDomain() ?: remoteDS.getById(id).toDomain()
    }

    override suspend fun toggleArticleSavedStatus(article: Article): Boolean = withContext(Dispatchers.IO){
        val existingArticle = localDS.getById(article.id)

        if(existingArticle != null){
            localDS.delete(article.toEntity())
            return@withContext false
        }else{
            localDS.insert(article.toEntity())
            return@withContext true
        }
    }

    override suspend fun isArticleSaved(id: String): Boolean = withContext(Dispatchers.IO){
        return@withContext localDS.getById(id) != null
    }
}
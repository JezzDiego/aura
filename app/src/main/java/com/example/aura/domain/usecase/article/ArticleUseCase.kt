package com.example.aura.domain.usecase.article

import com.example.aura.domain.model.Article
import com.example.aura.domain.repository.ArticleRepository


class GetArticleListUseCaseAPI(private val repository: ArticleRepository){
    suspend operator fun invoke(): List<Article> = repository.getAllArticlesAPI()
}

class GetArticleListUseCaseDAO(private val repository: ArticleRepository){
    suspend operator fun invoke(): List<Article> = repository.getAllArticlesDAO()
}

class GetArticleByIdUseCase(private val repository: ArticleRepository) {
    suspend operator fun invoke(id: String): Article? = repository.getArticleById(id)
}

class ToggleArticleSavedStatusUseCase(private val repository: ArticleRepository){
    suspend operator fun invoke(article: Article): Boolean = repository.toggleArticleSavedStatus(article)

}

class IsArticleSavedUseCase(private val repository: ArticleRepository){
    suspend operator fun invoke(id: String): Boolean = repository.isArticleSaved(id)

}

data class ArticleUseCases(
    val getArticleListAPI: GetArticleListUseCaseAPI,
    val getArticlesDAO: GetArticleListUseCaseDAO,
    val getArticleById: GetArticleByIdUseCase,
    val toggleArticleSavedStatus: ToggleArticleSavedStatusUseCase,
    val isArticleSaved: IsArticleSavedUseCase
)
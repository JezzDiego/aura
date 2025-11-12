package com.example.aura.presentation.ui.feature_news_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aura.core.BaseViewModel
import com.example.aura.core.ResultWrapper
import com.example.aura.domain.model.Article
import com.example.aura.domain.usecase.article.ArticleUseCases
import com.example.aura.domain.usecase.article.GetArticleByIdUseCase
import com.example.aura.domain.usecase.article.IsArticleSavedUseCase
import com.example.aura.domain.usecase.article.ToggleArticleSavedStatusUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ArticleViewModel(
    private val getArticleById: GetArticleByIdUseCase,
    private val toggleArticleSavedStatus: ToggleArticleSavedStatusUseCase,
    private val isArticleSaved: IsArticleSavedUseCase,
    private val articleId: String
) : BaseViewModel() {

    private val _isSaved = MutableStateFlow(false)
    val isSaved: StateFlow<Boolean> = _isSaved

    private val _uiState = MutableStateFlow<ResultWrapper<Article?>>(ResultWrapper.Loading)
    val uiState: StateFlow<ResultWrapper<Article?>> = _uiState

    init {
        loadArticle()
        checkInitialSavedStatus()
    }

    fun refresh(){
        loadArticle()
        checkInitialSavedStatus()
    }

    private fun checkInitialSavedStatus(){
        executeSafeCall(
            block = {
                isArticleSaved(articleId)
            },
            onSuccess = {isSavedResult ->
                _isSaved.value = isSavedResult
            }
        )
    }

    private fun loadArticle() {
        _uiState.value = ResultWrapper.Loading

        executeSafeCall(
            block = {
                getArticleById(articleId)
            },
            onSuccess = {
                _uiState.value = ResultWrapper.Success(it)
            },
            onError = {
                _uiState.value = ResultWrapper.Error(it)
            }
        )
    }

    fun toggleSaveArticle(article: Article){
        executeSafeCall(
            block = {
                toggleArticleSavedStatus(article)
            },
            onSuccess = { isNowSaved ->
                _isSaved.value = isNowSaved
            }
        )
    }
}

class NewsDetailViewModelFactory(
    private val articleUseCases: ArticleUseCases,
    private val articleId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ArticleViewModel(
                getArticleById = articleUseCases.getArticleById,
                toggleArticleSavedStatus = articleUseCases.toggleArticleSavedStatus,
                isArticleSaved = articleUseCases.isArticleSaved,
                articleId = articleId
            ) as T
        }
        throw IllegalArgumentException("Unknown VM class for NewsDetailViewModel")
    }
}
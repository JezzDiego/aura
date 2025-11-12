package com.example.aura.presentation.ui.feature_news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aura.core.BaseViewModel
import com.example.aura.core.ResultWrapper
import com.example.aura.domain.model.Article
import com.example.aura.domain.usecase.article.ArticleUseCases
import com.example.aura.domain.usecase.article.GetArticleListUseCaseAPI
import com.example.aura.domain.usecase.article.GetArticleListUseCaseDAO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ArticlesViewModel(
    private val getArticleListAPI: GetArticleListUseCaseAPI,
    private val getArticleListDAO: GetArticleListUseCaseDAO
): BaseViewModel(){
    private val _uiState = MutableStateFlow<ResultWrapper<List<Article>>>(ResultWrapper.Loading)
    val uiState: StateFlow<ResultWrapper<List<Article>>> = _uiState

    init {
        loadAllArticlesAPI()
    }

    fun refresh(){
        loadAllArticlesAPI()
    }

    fun loadAllArticlesAPI() {
        _uiState.value = ResultWrapper.Loading

        executeSafeCall(
            block = {
                getArticleListAPI()
            },
            onSuccess = {
                _uiState.value = ResultWrapper.Success(it)
            },
            onError = {
                _uiState.value = ResultWrapper.Error(it)
            }
        )
    }

    fun loadAllArticlesDAO() {
        _uiState.value = ResultWrapper.Loading

        executeSafeCall(
            block = {
                getArticleListDAO()
            },
            onSuccess = {
                _uiState.value = ResultWrapper.Success(it)
            },
            onError = {
                _uiState.value = ResultWrapper.Error(it)
            }
        )
    }
}

class NewsViewModelFactory(
    private val ArticleUseCases: ArticleUseCases
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticlesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ArticlesViewModel(
                getArticleListAPI = ArticleUseCases.getArticleListAPI,
                getArticleListDAO = ArticleUseCases.getArticlesDAO
            ) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}
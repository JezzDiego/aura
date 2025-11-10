package com.example.aura.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)

    protected fun <T> executeSafeCall(
        block: suspend () -> T,
        onSuccess: suspend (T) -> Unit,
        onError: suspend (Throwable) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                onSuccess(block())
            } catch (e: Exception) {
                onError(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}

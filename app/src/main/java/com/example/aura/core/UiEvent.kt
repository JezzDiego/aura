package com.example.aura.core

open class UiEvent<out T>(private val content: T) {
    private var hasBeenHandled = false
    fun getContentIfNotHandled(): T? =
        if (hasBeenHandled) null else {
            hasBeenHandled = true
            content
        }
}

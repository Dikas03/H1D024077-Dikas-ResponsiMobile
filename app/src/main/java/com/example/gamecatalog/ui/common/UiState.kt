package com.example.gamecatalog.ui.common

/**
 * Representasi State-driven UI untuk menangani Recomposition
 */
sealed interface UiState<out T> {
    object Idle : UiState<Nothing>
    object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}

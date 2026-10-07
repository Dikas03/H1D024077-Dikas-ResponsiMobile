package com.example.gamecatalog.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gamecatalog.data.model.GameDetail
import com.example.gamecatalog.data.repository.GameRepository
import com.example.gamecatalog.data.repository.GameRepositoryImpl
import com.example.gamecatalog.ui.common.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface DownloadState {
    object Idle : DownloadState
    data class Downloading(val progress: Float, val downloadedMb: Float, val totalMb: Float) : DownloadState
    data class Completed(val filePath: String) : DownloadState
}

class DetailViewModel(
    private val gameId: Int,
    private val repository: GameRepository = GameRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<GameDetail>>(UiState.Loading)
    val uiState: StateFlow<UiState<GameDetail>> = _uiState.asStateFlow()

    private val _downloadState = MutableStateFlow<DownloadState>(DownloadState.Idle)
    val downloadState: StateFlow<DownloadState> = _downloadState.asStateFlow()

    init {
        fetchGameDetail()
    }

    fun fetchGameDetail() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getGameDetail(gameId)
                .onSuccess { detail ->
                    _uiState.value = UiState.Success(detail)
                }
                .onFailure { error ->
                    _uiState.value = UiState.Error(
                        error.localizedMessage ?: "Terjadi kesalahan saat memuat detail game."
                    )
                }
        }
    }

    fun startDownload(gameName: String) {
        if (_downloadState.value is DownloadState.Downloading) return

        viewModelScope.launch {
            val totalMb = 250f
            var currentMb = 0f
            while (currentMb < totalMb) {
                delay(120)
                currentMb += (8..18).random().toFloat()
                if (currentMb > totalMb) currentMb = totalMb
                val progress = currentMb / totalMb
                _downloadState.value = DownloadState.Downloading(
                    progress = progress,
                    downloadedMb = currentMb,
                    totalMb = totalMb
                )
            }
            delay(300)
            val cleanFileName = gameName.replace("[^a-zA-Z0-9]".toRegex(), "_").lowercase()
            _downloadState.value = DownloadState.Completed("/storage/emulated/0/Download/${cleanFileName}_installer.apk")
        }
    }

    fun resetDownload() {
        _downloadState.value = DownloadState.Idle
    }

    class Factory(private val gameId: Int) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                return DetailViewModel(gameId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

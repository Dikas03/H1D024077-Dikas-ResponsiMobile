package com.example.gamecatalog.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamecatalog.data.model.Game
import com.example.gamecatalog.data.model.GameCategory
import com.example.gamecatalog.data.repository.GameRepository
import com.example.gamecatalog.data.repository.GameRepositoryImpl
import com.example.gamecatalog.ui.common.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: GameRepository = GameRepositoryImpl()
) : ViewModel() {

    val categories = listOf(
        GameCategory("all", "Semua", null),
        GameCategory("action", "Action", "action"),
        GameCategory("rpg", "RPG", "role-playing-games-rpg"),
        GameCategory("shooter", "Shooter", "shooter"),
        GameCategory("adventure", "Petualangan", "adventure"),
        GameCategory("strategy", "Strategi", "strategy"),
        GameCategory("sports", "Olahraga", "sports"),
        GameCategory("racing", "Balapan", "racing"),
        GameCategory("indie", "Indie", "indie"),
        GameCategory("casual", "Kasual", "casual"),
        GameCategory("simulation", "Simulasi", "simulation")
    )

    private val _uiState = MutableStateFlow<UiState<List<Game>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Game>>> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow(categories[0])
    val selectedCategory: StateFlow<GameCategory> = _selectedCategory.asStateFlow()

    private var searchJob: Job? = null

    init {
        fetchGames()
    }

    /**
     * Mengambil daftar game dari repository
     * @param query Pencarian kata kunci (opsional)
     * @param genreSlug Slug kategori/genre (opsional)
     */
    fun fetchGames(
        query: String? = _searchQuery.value,
        genreSlug: String? = _selectedCategory.value.slug
    ) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getGames(query, genreSlug)
                .onSuccess { games ->
                    _uiState.value = UiState.Success(games)
                }
                .onFailure { error ->
                    _uiState.value = UiState.Error(
                        error.localizedMessage ?: "Terjadi kesalahan saat memuat data game."
                    )
                }
        }
    }

    /**
     * Memilih kategori dan memuat ulang list game
     */
    fun onCategorySelected(category: GameCategory) {
        _selectedCategory.value = category
        fetchGames(_searchQuery.value, category.slug)
    }

    /**
     * Memperbarui query pencarian dengan debounce otomatis
     */
    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            fetchGames(newQuery, _selectedCategory.value.slug)
        }
    }

    /**
     * Menghapus query pencarian dan memuat ulang list default
     */
    fun clearSearch() {
        _searchQuery.value = ""
        fetchGames("", _selectedCategory.value.slug)
    }
}

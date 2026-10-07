package com.example.gamecatalog.data.repository

import com.example.gamecatalog.data.model.Game
import com.example.gamecatalog.data.model.GameDetail

interface GameRepository {
    suspend fun getGames(searchQuery: String? = null, genreSlug: String? = null): Result<List<Game>>
    suspend fun getGameDetail(gameId: Int): Result<GameDetail>
}

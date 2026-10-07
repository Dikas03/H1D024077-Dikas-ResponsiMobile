package com.example.gamecatalog.data.repository

import com.example.gamecatalog.data.model.Game
import com.example.gamecatalog.data.model.GameDetail
import com.example.gamecatalog.data.remote.ApiClient
import com.example.gamecatalog.data.remote.RawgApiService
import com.example.gamecatalog.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GameRepositoryImpl(
    private val apiService: RawgApiService = ApiClient.apiService
) : GameRepository {

    override suspend fun getGames(searchQuery: String?, genreSlug: String?): Result<List<Game>> =
        withContext(Dispatchers.IO) {
            try {
                val cleanQuery = searchQuery?.trim()?.ifEmpty { null }
                val cleanGenre = genreSlug?.trim()?.ifEmpty { null }
                val response = apiService.getGames(
                    apiKey = Constants.RAWG_API_KEY,
                    pageSize = 30,
                    search = cleanQuery,
                    genres = cleanGenre
                )
                Result.success(response.results)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun getGameDetail(gameId: Int): Result<GameDetail> =
        withContext(Dispatchers.IO) {
            try {
                val detail = apiService.getGameDetail(
                    gameId = gameId,
                    apiKey = Constants.RAWG_API_KEY
                )
                Result.success(detail)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}

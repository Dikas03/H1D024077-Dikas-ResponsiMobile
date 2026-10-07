package com.example.gamecatalog.data.remote

import com.example.gamecatalog.data.model.GameDetail
import com.example.gamecatalog.data.model.GameListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RawgApiService {

    /**
     * Mengambil daftar game dari RAWG API
     * @param apiKey API Key RAWG
     * @param pageSize Jumlah data per halaman
     * @param search Kata kunci pencarian judul game (opsional)
     */
    @GET("games")
    suspend fun getGames(
        @Query("key") apiKey: String,
        @Query("page_size") pageSize: Int = 30,
        @Query("search") search: String? = null,
        @Query("genres") genres: String? = null
    ): GameListResponse

    /**
     * Mengambil detail lengkap suatu game berdasarkan ID
     * @param gameId ID unik game
     * @param apiKey API Key RAWG
     */
    @GET("games/{id}")
    suspend fun getGameDetail(
        @Path("id") gameId: Int,
        @Query("key") apiKey: String
    ): GameDetail
}

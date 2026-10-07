package com.example.gamecatalog.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data response pembungkus list game dari RAWG API
 */
data class GameListResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("results")
    val results: List<Game>
)

/**
 * Representasi ringkas game untuk ditampilkan pada Home Screen
 */
data class Game(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("released")
    val released: String?, // Format ISO 8601 (YYYY-MM-DD)
    @SerializedName("background_image")
    val backgroundImage: String?,
    @SerializedName("rating")
    val rating: Double, // Rating berupa angka (misal: 4.47)
    @SerializedName("rating_top")
    val ratingTop: Int?,
    @SerializedName("ratings_count")
    val ratingsCount: Int?,
    @SerializedName("metacritic")
    val metacritic: Int?,
    @SerializedName("genres")
    val genres: List<Genre>?
)

/**
 * Detail lengkap game untuk ditampilkan pada Game Detail Screen
 */
data class GameDetail(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String?, // Deskripsi dalam format HTML
    @SerializedName("description_raw")
    val descriptionRaw: String?, // Deskripsi dalam format plain text
    @SerializedName("released")
    val released: String?, // Format ISO 8601 (YYYY-MM-DD)
    @SerializedName("background_image")
    val backgroundImage: String?,
    @SerializedName("rating")
    val rating: Double, // Rating berupa angka
    @SerializedName("rating_top")
    val ratingTop: Int?,
    @SerializedName("ratings_count")
    val ratingsCount: Int?,
    @SerializedName("metacritic")
    val metacritic: Int?,
    @SerializedName("playtime")
    val playtime: Int?,
    @SerializedName("website")
    val website: String?,
    @SerializedName("genres")
    val genres: List<Genre>?,
    @SerializedName("developers")
    val developers: List<Developer>?,
    @SerializedName("publishers")
    val publishers: List<Publisher>?,
    @SerializedName("stores")
    val stores: List<StoreWrapper>? = null
)

/**
 * Representasi kategori/genre game untuk filter menu
 */
data class GameCategory(
    val id: String,
    val name: String,
    val slug: String?
)

data class StoreWrapper(
    @SerializedName("id")
    val id: Int,
    @SerializedName("url")
    val url: String?,
    @SerializedName("store")
    val store: StoreInfo?
)

data class StoreInfo(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("slug")
    val slug: String?
)

data class Genre(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("slug")
    val slug: String?
)

data class Developer(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)

data class Publisher(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)

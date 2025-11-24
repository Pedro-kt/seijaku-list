package com.yumedev.seijakulist.data.remote.models.anime_random

import com.yumedev.seijakulist.data.remote.models.GenreDto
import com.yumedev.seijakulist.data.remote.models.ImagesDto
import com.google.gson.annotations.SerializedName

data class AnimeCardDto(
    @SerializedName("mal_id") val malId: Int,
    val title: String?,
    val images: ImagesDto?,
    val score: Float?,
    val status: String?,
    val genres: List<GenreDto?>,
    val year: Int?,
    val episodes: Int?,
)

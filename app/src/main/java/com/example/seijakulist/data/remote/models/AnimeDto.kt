package com.example.seijakulist.data.remote.models

import com.google.gson.annotations.SerializedName

data class AnimeDto(
    @SerializedName("mal_id") val malId: Int,
    val title: String,
    val images: ImagesDto,
    val synopsis: String,
    val episodes: Int,
    val duration: String,
    val genres: List<GenreDto>,
    val score: Float,
    val status: String
)

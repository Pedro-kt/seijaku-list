package com.example.seijakulist.data.remote.models

import com.google.gson.annotations.SerializedName

data class AnimeDetailResponseDto(
    @SerializedName("data") val data: AnimeDetailDto
)

data class AnimeDetailDto(
    @SerializedName("mal_id") val malId: Int,
    val title: String,
    val images: ImagesDto?,
    val synopsis: String?,
    val episodes: Int?,
    val duration: String?,
    val genres: List<GenreDto?>,
    val score: Float?,
    val status: String?,
    @SerializedName("type") val animeType: String?,
    val aired: AiredDto?,
    @SerializedName("studios") val studios: List<StudiosDto?>
)
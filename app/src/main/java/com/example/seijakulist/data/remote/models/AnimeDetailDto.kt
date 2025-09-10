package com.example.seijakulist.data.remote.models

import com.google.gson.annotations.SerializedName

data class AnimeDetailResponseDto(
    @SerializedName("data") val data: AnimeDetailDto
)

data class AnimeDetailDto(
    @SerializedName("mal_id") val malId: Int,
    val title: String,
    @SerializedName("title_english") val titleEnglish: String?,
    @SerializedName("title_japanese") val titleJapanese: String?,
    val images: ImagesDto?,
    @SerializedName("type") val typeAnime: String?,
    val source: String?,
    val episodes: Int?,
    val status: String?,
    val aired: AiredDto?,
    val duration: String?,
    val rating: String?,
    val score: Float?,
    @SerializedName("scored_by") val scoreBy: Int?,
    val rank: Int?,
    val synopsis: String?,
    val season: String?,
    val year: Int?,
    val studios: List<StudiosDto?>,
    val genres: List<GenreDto?>
)
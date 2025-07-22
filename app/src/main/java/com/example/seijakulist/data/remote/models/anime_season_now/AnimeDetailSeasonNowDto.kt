package com.example.seijakulist.data.remote.models.anime_season_now

import com.example.seijakulist.data.remote.models.AiredDto
import com.example.seijakulist.data.remote.models.GenreDto
import com.example.seijakulist.data.remote.models.ImagesDto
import com.example.seijakulist.data.remote.models.StudiosDto
import com.google.gson.annotations.SerializedName

data class AnimeDetailSeasonNowDto(
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
    @SerializedName("score_by") val scoreBy: Int?,
    val rank: Int?,
    val synopsis: String?,
    val season: String?,
    val year: Int?,
    val studios: List<StudiosDto?>,
    val genres: List<GenreDto?>
)

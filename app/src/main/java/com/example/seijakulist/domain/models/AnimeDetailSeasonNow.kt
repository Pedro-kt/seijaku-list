package com.example.seijakulist.domain.models

import com.example.seijakulist.data.remote.models.AiredDto
import com.example.seijakulist.data.remote.models.GenreDto
import com.example.seijakulist.data.remote.models.ImagesDto
import com.example.seijakulist.data.remote.models.StudiosDto
import com.google.gson.annotations.SerializedName

data class AnimeDetailSeasonNow(
    val malId: Int,
    val title: String,
    val titleEnglish: String,
    val titleJapanese: String,
    val images: String,
    val typeAnime: String,
    val source: String,
    val episodes: Int,
    val status: String,
    val aired: AiredDto?,
    val duration: String,
    val rating: String,
    val score: Float,
    val scoreBy: Int,
    val rank: Int,
    val synopsis: String,
    val season: String,
    val year: Int,
    val studios: List<StudiosDto?>,
    val genres: List<GenreDto?>
)

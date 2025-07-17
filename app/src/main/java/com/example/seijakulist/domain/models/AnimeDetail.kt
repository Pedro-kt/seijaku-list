package com.example.seijakulist.domain.models

import com.example.seijakulist.data.remote.models.AiredDto
import com.example.seijakulist.data.remote.models.GenreDto
import com.example.seijakulist.data.remote.models.StudiosDto

data class AnimeDetail(
    val malId: Int,
    val title: String,
    val imageUrl: String?,
    val score: Float?,
    val synopsis: String?,
    val genres: List<GenreDto?>,
    val episodes: Int?,
    val status: String?,
    val aired: AiredDto?,
    val duration: String?,
    val animeType: String?,
    val studios: List<StudiosDto?>,
)

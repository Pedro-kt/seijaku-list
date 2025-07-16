package com.example.seijakulist.domain.models

import com.example.seijakulist.data.remote.models.GenreDto

data class AnimeDetail(
    val malId: Int,
    val title: String,
    val imageUrl: String?,
    val score: Float?,
    val synopsis: String?,
    val genres: List<GenreDto?>,
    val episodes: Int?,
    val status: String?,
    //val aired: String?,
    val duration: String?,
    val animeType: String?
)

package com.yumedev.seijakulist.domain.models

import com.yumedev.seijakulist.data.remote.models.GenreDto
import com.yumedev.seijakulist.data.remote.models.StudiosDto

data class AnimeDetail(
    val malId: Int,
    val title: String,
    val titleEnglish: String,
    val titleJapanese: String,
    val images: String,
    val typeAnime: String,
    val source: String,
    val episodes: Int,
    val status: String,
    val aired: String,
    val duration: String,
    val rating: String,
    val score: Float,
    val scoreBy: Int,
    val rank: Int,
    val synopsis: String,
    val season: String,
    val year: Any,
    val studios: List<StudiosDto?>,
    val genres: List<GenreDto?>
)

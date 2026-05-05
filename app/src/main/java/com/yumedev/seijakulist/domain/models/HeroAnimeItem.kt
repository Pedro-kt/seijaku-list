package com.yumedev.seijakulist.domain.models

data class HeroAnimeItem(
    val malId: Int,
    val title: String,
    val imageUrl: String,
    val label: String,
    val score: Float? = null,
    val year: String? = null,
    val status: String? = null,
    val genres: List<String> = emptyList(),
    val episodes: Int? = null,
)
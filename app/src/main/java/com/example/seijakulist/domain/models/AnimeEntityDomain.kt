package com.example.seijakulist.domain.models

data class AnimeEntityDomain(
    val malId: Int,
    val title: String,
    val image: String,
    val userScore: Float,
    val userStatus: String,
    val userOpiniun: String,
    val totalEpisodes: Int,
    val episodesWatched: Int,
    val rewatchCount: Int
)

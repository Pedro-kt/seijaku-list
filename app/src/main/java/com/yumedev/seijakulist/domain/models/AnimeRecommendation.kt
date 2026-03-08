package com.yumedev.seijakulist.domain.models

data class AnimeRecommendation(
    val malId: Int,
    val title: String,
    val image: String,
    val votes: Int
)

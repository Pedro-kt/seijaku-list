package com.example.seijakulist.domain.models

data class Anime(
    val malId: Int,
    val title: String,
    val image: String,
    val synopsis: String,
    val score: Float,
)
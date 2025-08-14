package com.example.seijakulist.domain.models

import com.example.seijakulist.data.remote.models.GenreDto
import com.example.seijakulist.data.remote.models.ImagesDto
import com.google.gson.annotations.SerializedName

data class AnimeCard(
    val malId: Int,
    val title: String,
    val images: String,
    val score: Float,
    val status: String,
    val genres: List<GenreDto?>,
    val year: String,
    val episodes: String,
)
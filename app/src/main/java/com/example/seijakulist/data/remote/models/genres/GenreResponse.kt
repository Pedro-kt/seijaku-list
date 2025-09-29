package com.example.seijakulist.data.remote.models.genres

import com.google.gson.annotations.SerializedName

data class GenreResponse(
    val data: List<GenreAnimeDto>
)

data class GenreAnimeDto(
    @SerializedName("mal_id") val malId: Int,
    val name: String,
    val url: String,
    val count: Int
)

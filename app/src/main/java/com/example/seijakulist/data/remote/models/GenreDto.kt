package com.example.seijakulist.data.remote.models

import com.google.gson.annotations.SerializedName

data class GenreDto(
    @SerializedName("mal_id") val malId: Int,
    val name: String
)

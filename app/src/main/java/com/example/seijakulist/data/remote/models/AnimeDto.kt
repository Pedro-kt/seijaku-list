package com.example.seijakulist.data.remote.models

import com.google.gson.annotations.SerializedName

data class AnimeDto(
    @SerializedName("mal_id") val malId: Int,
    val title: String?,
    val images: ImagesDto?,
    val score: Float?,
)

package com.yumedev.seijakulist.data.remote.models

import com.google.gson.annotations.SerializedName

data class AnimeProducerDto(
    @SerializedName("mal_id") val malId: Int?,
    val type: String?,
    val name: String?,
    val url: String?
)

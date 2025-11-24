package com.yumedev.seijakulist.data.remote.models

import com.google.gson.annotations.SerializedName

data class AnimeCharactersDto(
    val character: CharacterDto?,
    @SerializedName("role") val role: String?
)
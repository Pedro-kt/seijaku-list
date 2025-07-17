package com.example.seijakulist.data.remote.models

import com.google.gson.annotations.SerializedName

data class AnimeCharactersResponseDto(
    @SerializedName("data") val data: List<AnimeCharactersDto?>
)

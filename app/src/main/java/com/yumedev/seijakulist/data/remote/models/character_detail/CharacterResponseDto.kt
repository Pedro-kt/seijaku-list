package com.yumedev.seijakulist.data.remote.models.character_detail

import com.google.gson.annotations.SerializedName

data class CharacterResponseDto(
    @SerializedName("data") val data: CharacterDetailDto?
)

package com.example.seijakulist.data.remote.models.character_pictures

import com.example.seijakulist.data.remote.models.AnimeDto
import com.google.gson.annotations.SerializedName

data class CharacterPicturesResponseDto(
    @SerializedName("data") val data: List<CharacterPicturesDto?>
)

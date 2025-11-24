package com.yumedev.seijakulist.data.remote.models.character_pictures

import com.google.gson.annotations.SerializedName

data class CharacterPicturesResponseDto(
    @SerializedName("data") val data: List<CharacterPicturesDto?>
)

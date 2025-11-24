package com.yumedev.seijakulist.data.remote.models

import com.google.gson.annotations.SerializedName

data class ImagesCharactersDto(
    val jpg: CharacterJpgDto?,
    val webp: CharacterWebpDto?
)

data class CharacterJpgDto(
    @SerializedName("image_url") val imageUrl: String?,
)

data class CharacterWebpDto(
    @SerializedName("image_url") val imageUrl: String?,
)

package com.example.seijakulist.data.remote.models.character_detail

import com.google.gson.annotations.SerializedName

data class CharacterDetailImages(
    @SerializedName("jpg") val imageCharacterDetailJpg: CharacterDetailImageJpg? = null,
    @SerializedName("webp") val imageCharacterDetailWebp: CharacterDetailImageWebp? = null
)

data class CharacterDetailImageJpg(
    @SerializedName("image_url") val imageCharacterJpg: String? = null
)

data class CharacterDetailImageWebp(
    @SerializedName("image_url") val imageCharacterWebp: String? = null
)

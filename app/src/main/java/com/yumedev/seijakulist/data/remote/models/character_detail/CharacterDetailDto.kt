package com.yumedev.seijakulist.data.remote.models.character_detail

import com.google.gson.annotations.SerializedName

data class CharacterDetailDto(
    @SerializedName("mal_id") val characterId: Int = 0,
    @SerializedName("images") val images: CharacterDetailImages? = null,
    @SerializedName("name") val nameCharacter: String? = "",
    @SerializedName("name_kanji") val nameKanjiCharacter: String? = "",
    @SerializedName("about") val descriptionCharacter: String? = ""
)

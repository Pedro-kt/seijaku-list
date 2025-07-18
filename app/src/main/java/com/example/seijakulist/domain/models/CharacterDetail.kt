package com.example.seijakulist.domain.models

import com.example.seijakulist.data.remote.models.character_detail.CharacterDetailImages
import com.google.gson.annotations.SerializedName

data class CharacterDetail(
    val characterId: Int = 0,
    val images: String = "",
    val nameCharacter: String = "",
    val nameKanjiCharacter: String = "",
    val descriptionCharacter: String = ""
)

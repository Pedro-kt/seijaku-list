package com.example.seijakulist.data.remote.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.InternalSerializationApi

data class CharacterDto(
    @SerializedName("mal_id") val idCharacter: Int?,
    @SerializedName("images") val imageCharacter: ImagesCharactersDto?,
    @SerializedName("name") val nameCharacter: String?
)

package com.example.seijakulist.domain.models

import com.example.seijakulist.data.remote.models.ImagesCharactersDto
import com.google.gson.annotations.SerializedName

data class AnimeCharactersDetail(
    val idCharacter: Int?,
    val imageCharacter: ImagesCharactersDto?,
    val nameCharacter: String?,
    val role: String?
)

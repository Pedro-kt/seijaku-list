package com.example.seijakulist.domain.models

import com.example.seijakulist.data.remote.models.ImagesCharactersDto

data class AnimeCharactersDetail(
    val idCharacter: Int?,
    val imageCharacter: ImagesCharactersDto?,
    val nameCharacter: String?,
    var role: String
)

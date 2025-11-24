package com.yumedev.seijakulist.domain.models

import com.yumedev.seijakulist.data.remote.models.ImagesCharactersDto

data class AnimeCharactersDetail(
    val idCharacter: Int?,
    val imageCharacter: ImagesCharactersDto?,
    val nameCharacter: String?,
    var role: String
)

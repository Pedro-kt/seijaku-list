package com.example.seijakulist.data.mapper

import com.example.seijakulist.data.remote.models.AnimeCharactersDto
import com.example.seijakulist.domain.models.AnimeCharactersDetail



fun List<AnimeCharactersDto?>.toAnimeCharactersDetail(): List<AnimeCharactersDetail> {
    return this.mapNotNull { dto ->
        if (dto != null) {
            AnimeCharactersDetail(
                idCharacter = dto.character?.idCharacter,
                imageCharacter = dto.character?.imageCharacter,
                nameCharacter = dto.character?.nameCharacter,
                role = dto.role ?: ""
            )
        } else {
            null
        }
    }
}




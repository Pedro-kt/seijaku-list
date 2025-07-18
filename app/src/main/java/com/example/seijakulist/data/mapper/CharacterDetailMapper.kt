package com.example.seijakulist.data.mapper

import com.example.seijakulist.data.remote.models.character_detail.CharacterDetailDto
import com.example.seijakulist.data.remote.models.character_detail.CharacterDetailImages
import com.example.seijakulist.domain.models.CharacterDetail
import com.google.gson.annotations.SerializedName

fun CharacterDetailDto.toCharacterDetail(): CharacterDetail {
    return CharacterDetail(
        characterId = characterId,
        images = this.images?.imageCharacterDetailJpg?.imageCharacterJpg
            ?: images?.imageCharacterDetailWebp?.imageCharacterWebp
            ?: "Imagen predeterminada",
        nameCharacter = nameCharacter ?: "Nombre no encontrado",
        nameKanjiCharacter = nameKanjiCharacter ?: "Kanji no encontrado",
        descriptionCharacter = descriptionCharacter ?: "No hemos podido encontrar una descripcion para este personaje :("
    )
}
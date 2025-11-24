package com.yumedev.seijakulist.data.mapper

import com.yumedev.seijakulist.data.remote.models.character_detail.CharacterDetailDto
import com.yumedev.seijakulist.data.remote.models.character_detail.CharacterDetailImages
import com.yumedev.seijakulist.domain.models.CharacterDetail
import com.google.gson.annotations.SerializedName

fun CharacterDetailDto.toCharacterDetail(): CharacterDetail {
    return CharacterDetail(
        characterId = characterId,
        images = this.images?.imageCharacterDetailJpg?.imageCharacterJpg
            ?: images?.imageCharacterDetailWebp?.imageCharacterWebp
            ?: "Imagen predeterminada",
        nameCharacter = nameCharacter ?: "Nombre no encontrado",
        nameKanjiCharacter = nameKanjiCharacter ?: "N/A",
        descriptionCharacter = descriptionCharacter ?: "No se ha podido encontrar una descripcion para este personaje, lo sentimos"
    )
}
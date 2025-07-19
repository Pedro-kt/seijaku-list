package com.example.seijakulist.data.mapper

import com.example.seijakulist.data.remote.models.character_pictures.CharacterPicturesDto
import com.example.seijakulist.domain.models.CharacterPictures

fun List<CharacterPicturesDto?>.toCharacterPictures(): List<CharacterPictures> {

        return this
            .filterNotNull()
            .map { dto ->
            CharacterPictures(
                characterPictures = dto.characterPictures?.imageUrlCharacterPicture
                    ?: "Imagen predeterminada"
            )
        }
}
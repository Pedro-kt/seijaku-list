package com.yumedev.seijakulist.data.mapper

import com.yumedev.seijakulist.data.remote.models.character_detail.CharacterDetailDto
import com.yumedev.seijakulist.domain.models.AnimeRelationDomain
import com.yumedev.seijakulist.domain.models.CharacterDetail
import com.yumedev.seijakulist.domain.models.MangaRelationDomain
import com.yumedev.seijakulist.domain.models.VoiceActorDomain

fun CharacterDetailDto.toCharacterDetail(): CharacterDetail {
    return CharacterDetail(
        characterId = characterId,
        images = this.images?.imageCharacterDetailJpg?.imageCharacterJpg
            ?: images?.imageCharacterDetailWebp?.imageCharacterWebp
            ?: "Imagen predeterminada",
        nameCharacter = nameCharacter ?: "Nombre no encontrado",
        nameKanjiCharacter = nameKanjiCharacter ?: "N/A",
        descriptionCharacter = descriptionCharacter
            ?: "No se ha podido encontrar una descripcion para este personaje, lo sentimos",
        favorites = favorites,
        nicknames = nicknames?.filterNotNull() ?: emptyList(),
        voiceActors = voiceActors?.mapNotNull { va ->
            val person = va.person ?: return@mapNotNull null
            VoiceActorDomain(
                personId = person.personId,
                name = person.nameCharacter ?: "",
                imageUrl = person.images?.imageCharacterDetailJpg?.imageCharacterJpg
                    ?: person.images?.imageCharacterDetailWebp?.imageCharacterWebp
                    ?: "",
                language = va.nameCharacter ?: ""
            )
        } ?: emptyList(),
        animeRelations = animeRelations?.mapNotNull { ar ->
            val anime = ar.anime ?: return@mapNotNull null
            AnimeRelationDomain(
                malId = anime.malId,
                title = anime.title ?: "",
                imageUrl = anime.images?.imageCharacterDetailJpg?.imageCharacterJpg
                    ?: anime.images?.imageCharacterDetailWebp?.imageCharacterWebp
                    ?: "",
                role = ar.role ?: ""
            )
        } ?: emptyList(),
        mangaRelations = mangaRelations?.mapNotNull { mr ->
            val manga = mr.relation ?: return@mapNotNull null
            MangaRelationDomain(
                malId = manga.malId,
                title = manga.title ?: "",
                imageUrl = manga.images?.imageCharacterDetailJpg?.imageCharacterJpg
                    ?: manga.images?.imageCharacterDetailWebp?.imageCharacterWebp
                    ?: ""
            )
        } ?: emptyList()
    )
}
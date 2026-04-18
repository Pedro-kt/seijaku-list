package com.yumedev.seijakulist.domain.models

data class CharacterDetail(
    val characterId: Int = 0,
    val images: String = "",
    val nameCharacter: String = "",
    val nameKanjiCharacter: String = "",
    val descriptionCharacter: String = "",
    val favorites: Int = 0,
    val nicknames: List<String> = emptyList(),
    val voiceActors: List<VoiceActorDomain> = emptyList(),
    val animeRelations: List<AnimeRelationDomain> = emptyList(),
    val mangaRelations: List<MangaRelationDomain> = emptyList()
)

data class VoiceActorDomain(
    val personId: Int = 0,
    val name: String = "",
    val imageUrl: String = "",
    val language: String = ""
)

data class AnimeRelationDomain(
    val malId: Int = 0,
    val title: String = "",
    val imageUrl: String = "",
    val role: String = ""
)

data class MangaRelationDomain(
    val malId: Int = 0,
    val title: String = "",
    val imageUrl: String = ""
)
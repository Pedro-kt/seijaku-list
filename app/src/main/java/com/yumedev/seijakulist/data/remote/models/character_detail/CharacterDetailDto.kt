package com.yumedev.seijakulist.data.remote.models.character_detail

import com.google.gson.annotations.SerializedName
import com.google.protobuf.LazyStringArrayList.emptyList
import com.yumedev.seijakulist.data.remote.models.AnimeDto

data class CharacterDetailDto(
    @SerializedName("mal_id") val characterId: Int = 0,
    @SerializedName("images") val images: CharacterDetailImages? = null,
    @SerializedName("name") val nameCharacter: String? = "",
    @SerializedName("name_kanji") val nameKanjiCharacter: String? = "",
    @SerializedName("about") val descriptionCharacter: String? = "",
    @SerializedName("favorites") val favorites: Int = 0,
    @SerializedName("url") val urlCharacter: String? = "",
    @SerializedName("voices") val voiceActors: List<VoiceActorDto>?,
    @SerializedName("anime") val animeRelations: List<AnimeRelationDto>?,
    @SerializedName("manga") val mangaRelations: List<MangaRelationDto>?,
    @SerializedName("nicknames") val nicknames: List<String>? = emptyList(),
)

data class VoiceActorDto(
    @SerializedName("person") val person: PersonDto? = null,
    @SerializedName("language") val nameCharacter: String? = "",
)

data class PersonDto(
    @SerializedName("mal_id") val personId: Int = 0,
    @SerializedName("name") val nameCharacter: String? = "",
    @SerializedName("url") val urlCharacter: String? = "",
    @SerializedName("images") val images: CharacterDetailImages? = null,
)

data class AnimeRelationDto(
    @SerializedName("role") val role: String? = "",
    @SerializedName("anime") val anime: AnimeCharacterDto? = null,
)

data class AnimeCharacterDto(
    @SerializedName("mal_id") val malId: Int = 0,
    @SerializedName("images") val images: CharacterDetailImages? = null,
    @SerializedName("title") val title: String? = "",
    @SerializedName("url") val urlAnimeCharacter: String? = "",
)

data class MangaRelationDto(
    @SerializedName("role") val role: String? = "",
    @SerializedName("manga") val relation: MangaCharacterDto? = null,
)

data class MangaCharacterDto(
    @SerializedName("mal_id") val malId: Int = 0,
    @SerializedName("images") val images: CharacterDetailImages? = null,
    @SerializedName("title") val title: String? = "",
    @SerializedName("url") val urlMangaCharacter: String? = "",
)
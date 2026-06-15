package com.yumedev.seijakulist.data.mapper

import com.yumedev.seijakulist.data.remote.graphql.SearchCharactersQuery
import com.yumedev.seijakulist.data.remote.graphql.fragment.CharacterFields
import com.yumedev.seijakulist.domain.models.CharacterCard

/**
 * Mappers para convertir modelos de Apollo GraphQL (AniList) a CharacterCard
 */

/**
 * Convierte SearchCharactersQuery.Character (contiene CharacterFields) a CharacterCard
 */
fun toCharacterCard(character: SearchCharactersQuery.Character): CharacterCard {
    return toCharacterCard(character.characterFields)
}

/**
 * Convierte CharacterFields fragment a CharacterCard
 * Este es el mapper principal que reutilizan los demás
 */
fun toCharacterCard(fields: CharacterFields): CharacterCard {
    return CharacterCard(
        characterId = fields.id,
        name = fields.name?.full ?: fields.name?.native ?: "Sin nombre",
        nameNative = fields.name?.native,
        imageUrl = fields.image?.large ?: fields.image?.medium ?: "",
        favourites = fields.favourites ?: 0,
        gender = fields.gender,
        age = fields.age,
        description = fields.description
    )
}

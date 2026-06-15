package com.yumedev.seijakulist.domain.models

/**
 * Modelo simplificado de personaje para cards de búsqueda
 */
data class CharacterCard(
    val characterId: Int,
    val name: String,
    val nameNative: String?,
    val imageUrl: String,
    val favourites: Int,
    val gender: String?,
    val age: String?,
    val description: String?
)

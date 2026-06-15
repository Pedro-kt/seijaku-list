package com.yumedev.seijakulist.domain.models

import com.yumedev.seijakulist.data.remote.models.AnimeProducerDto
import com.yumedev.seijakulist.data.remote.models.DemographicDto
import com.yumedev.seijakulist.data.remote.models.GenreDto

/**
 * Modelo de dominio para detalles de manga
 * Similar a AnimeDetail pero adaptado para manga
 */
data class MangaDetail(
    val malId: Int,
    val title: String,
    val titleEnglish: String,
    val titleJapanese: String,
    val images: String,
    val typeManga: String,  // MANGA, NOVEL, ONE_SHOT, MANHWA, MANHUA
    val source: String,
    val chapters: Int?,     // Número de capítulos (null si es desconocido)
    val volumes: Int?,      // Número de volúmenes (null si es desconocido)
    val status: String,     // FINISHED, RELEASING, NOT_YET_RELEASED, CANCELLED, HIATUS
    val published: String,  // Rango de fechas de publicación
    val score: Float,
    val scoreBy: Int,
    val rank: Int,
    val synopsis: String,
    val background: String,
    val authors: List<AuthorDto>,      // Autores del manga
    val serializations: List<SerializationDto>, // Revistas/sitios donde se publicó
    val genres: List<GenreDto?>,
    val demographics: List<DemographicDto?>
)

/**
 * Información de autor/artista
 */
data class AuthorDto(
    val malId: Int,
    val name: String,
    val url: String,
    val role: String  // Story, Art, Story & Art
)

/**
 * Información de serialización (revista/publicación)
 */
data class SerializationDto(
    val malId: Int,
    val name: String,
    val url: String
)

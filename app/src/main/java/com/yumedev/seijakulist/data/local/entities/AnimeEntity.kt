package com.yumedev.seijakulist.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animes")
data class AnimeEntity(
    @PrimaryKey val malId: Int,
    val title: String,
    val imageUrl: String?,
    val userScore: Float, // campo nuevo (cambiado de nombre) v2
    val statusUser: String, // campo nuevo v2
    val userOpiniun: String, // campo nuevo v3
    val totalEpisodes: Int, // campo nuevo v5
    val episodesWatched: Int, // campo nuevo v5
    val rewatchCount: Int, // campo nuevo v6
    val genres: String = "", // campo nuevo v7 - géneros separados por comas

    // Campos adicionales del detalle del anime (v8)
    val synopsis: String? = null,
    val titleEnglish: String? = null,
    val titleJapanese: String? = null,
    val studios: String? = null, // separados por comas
    val score: Float? = null, // puntuación de MAL
    val scoreBy: Int? = null, // cantidad de personas que puntuaron
    val typeAnime: String? = null, // TV, Movie, OVA, etc
    val duration: String? = null, // duración por episodio
    val season: String? = null, // temporada (Spring, Summer, etc)
    val year: String? = null, // año de emisión
    val status: String? = null, // estado de emisión (Finished Airing, Currently Airing, etc)
    val aired: String? = null, // fecha de transmisión
    val rank: Int? = null, // ranking en MAL
    val rating: String? = null, // clasificación de edad (PG-13, R+, etc)
    val source: String? = null, // origen (Manga, Light novel, Original, etc)

    // Fechas de seguimiento del usuario (v9)
    val startDate: Long? = null, // fecha de inicio en milisegundos
    val endDate: Long? = null // fecha de finalización en milisegundos
)
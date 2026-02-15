package com.yumedev.seijakulist.domain.models

data class AnimeEntityDomain(
    val malId: Int,
    val title: String,
    val image: String,
    val userScore: Float,
    val userStatus: String,
    val userOpiniun: String,
    val totalEpisodes: Int,
    val episodesWatched: Int,
    val rewatchCount: Int,
    val genres: String = "",

    // Campos adicionales del detalle del anime
    val synopsis: String? = null,
    val titleEnglish: String? = null,
    val titleJapanese: String? = null,
    val studios: String? = null,
    val score: Float? = null,
    val scoreBy: Int? = null,
    val typeAnime: String? = null,
    val duration: String? = null,
    val season: String? = null,
    val year: String? = null,
    val status: String? = null,
    val aired: String? = null,
    val rank: Int? = null,
    val rating: String? = null,
    val source: String? = null,

    // Fechas de seguimiento del usuario
    val startDate: Long? = null,
    val endDate: Long? = null
)

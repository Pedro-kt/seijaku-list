package com.yumedev.seijakulist.domain.models

data class MangaEntityDomain(
    val malId: Int,
    val title: String,
    val titleEnglish: String?,
    val titleJapanese: String?,
    val image: String,
    val bannerImage: String?,

    val typeManga: String,
    val source: String?,
    val chapters: Int?,
    val volumes: Int?,
    val status: String,
    val published: String?,

    val score: Float?,
    val scoreBy: Int?,
    val rank: Int?,

    val synopsis: String?,
    val background: String?,
    val genres: String,
    val demographics: String?,
    val authors: String?,
    val serializations: String?,

    // User data
    val userScore: Float,
    val userStatus: String,
    val userOpinion: String,
    val chaptersRead: Int,
    val volumesRead: Int,
    val rereadCount: Int,

    val startDate: Long?,
    val endDate: Long?,
    val plannedPriority: String?,
    val plannedNote: String?,

    val lastModified: Long
)

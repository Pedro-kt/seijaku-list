package com.yumedev.seijakulist.domain.models

data class AnimeEpisode(
    val malId: Int?,
    val url: String?,
    val title: String?,
    val titleJapanese: String?,
    val titleRomanji: String?,
    val aired: String?,
    val score: Float?,
    val filler: Boolean?,
    val recap: Boolean?,
    val forumUrl: String?
)

data class AnimeEpisodesPagination(
    val lastVisiblePage: Int?,
    val hasNextPage: Boolean?
)

data class AnimeEpisodesResult(
    val episodes: List<AnimeEpisode>,
    val pagination: AnimeEpisodesPagination?
)

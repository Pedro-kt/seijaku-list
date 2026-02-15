package com.yumedev.seijakulist.domain.models

data class AnimeEpisodeDetail(
    val malId: Int?,
    val url: String?,
    val title: String?,
    val titleJapanese: String?,
    val titleRomanji: String?,
    val duration: Int?,
    val aired: String?,
    val filler: Boolean?,
    val recap: Boolean?,
    val synopsis: String?
)

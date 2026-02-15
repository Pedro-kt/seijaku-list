package com.yumedev.seijakulist.data.remote.models.anime_episodes

import com.google.gson.annotations.SerializedName

data class AnimeEpisodesResponseDto(
    @SerializedName("data") val data: List<AnimeEpisodeDto>,
    @SerializedName("pagination") val pagination: EpisodesPaginationDto?
)

data class AnimeEpisodeDto(
    @SerializedName("mal_id") val malId: Int?,
    @SerializedName("url") val url: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("title_japanese") val titleJapanese: String?,
    @SerializedName("title_romanji") val titleRomanji: String?,
    @SerializedName("aired") val aired: String?,
    @SerializedName("score") val score: Float?,
    @SerializedName("filler") val filler: Boolean?,
    @SerializedName("recap") val recap: Boolean?,
    @SerializedName("forum_url") val forumUrl: String?
)

data class EpisodesPaginationDto(
    @SerializedName("last_visible_page") val lastVisiblePage: Int?,
    @SerializedName("has_next_page") val hasNextPage: Boolean?
)

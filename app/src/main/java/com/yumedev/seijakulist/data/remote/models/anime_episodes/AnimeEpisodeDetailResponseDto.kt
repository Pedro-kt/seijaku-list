package com.yumedev.seijakulist.data.remote.models.anime_episodes

import com.google.gson.annotations.SerializedName

data class AnimeEpisodeDetailResponseDto(
    @SerializedName("data") val data: AnimeEpisodeDetailDto?
)

data class AnimeEpisodeDetailDto(
    @SerializedName("mal_id") val malId: Int?,
    @SerializedName("url") val url: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("title_japanese") val titleJapanese: String?,
    @SerializedName("title_romanji") val titleRomanji: String?,
    @SerializedName("duration") val duration: Int?,
    @SerializedName("aired") val aired: String?,
    @SerializedName("filler") val filler: Boolean?,
    @SerializedName("recap") val recap: Boolean?,
    @SerializedName("synopsis") val synopsis: String?
)

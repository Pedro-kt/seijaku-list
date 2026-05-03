package com.yumedev.seijakulist.data.remote.models.watch_episodes

import com.google.gson.annotations.SerializedName
import com.yumedev.seijakulist.data.remote.models.ImagesDto

data class WatchEpisodesResponseDto(
    @SerializedName("data") val data: List<WatchEpisodeEntryDto>
)

data class WatchEpisodeEntryDto(
    @SerializedName("entry") val entry: WatchAnimeEntryDto,
    @SerializedName("episodes") val episodes: List<WatchEpisodeItemDto>?,
    @SerializedName("region_locked") val regionLocked: Boolean?
)

data class WatchAnimeEntryDto(
    @SerializedName("mal_id") val malId: Int,
    @SerializedName("url") val url: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("images") val images: ImagesDto?
)

data class WatchEpisodeItemDto(
    @SerializedName("mal_id") val malId: Int?,
    @SerializedName("url") val url: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("premium") val premium: Boolean?
)
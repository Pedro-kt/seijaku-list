package com.yumedev.seijakulist.data.remote.models.watch_promos

import com.google.gson.annotations.SerializedName
import com.yumedev.seijakulist.data.remote.models.ImagesDto

data class WatchPromosResponseDto(
    @SerializedName("data") val data: List<WatchPromoEntryDto>
)

data class WatchPromoEntryDto(
    @SerializedName("entry") val entry: WatchPromoAnimeDto,
    @SerializedName("trailer") val trailer: WatchPromoTrailerDto?
)

data class WatchPromoAnimeDto(
    @SerializedName("mal_id") val malId: Int,
    @SerializedName("url") val url: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("images") val images: ImagesDto?
)

data class WatchPromoTrailerDto(
    @SerializedName("youtube_id") val youtubeId: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("embed_url") val embedUrl: String?,
    @SerializedName("images") val images: WatchPromoTrailerImagesDto?
)

data class WatchPromoTrailerImagesDto(
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("small_image_url") val smallImageUrl: String?,
    @SerializedName("medium_image_url") val mediumImageUrl: String?,
    @SerializedName("large_image_url") val largeImageUrl: String?,
    @SerializedName("maximum_image_url") val maximumImageUrl: String?
)
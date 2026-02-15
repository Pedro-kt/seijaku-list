package com.yumedev.seijakulist.data.remote.models.anime_videos

import com.google.gson.annotations.SerializedName

data class AnimeVideosResponseDto(
    @SerializedName("data") val data: AnimeVideosDataDto
)

data class AnimeVideosDataDto(
    @SerializedName("promo") val promo: List<PromoDto>?,
    @SerializedName("episodes") val episodes: List<EpisodeVideoDto>?,
    @SerializedName("music_videos") val musicVideos: List<MusicVideoDto>?
)

// Promo / Trailers
data class PromoDto(
    @SerializedName("title") val title: String?,
    @SerializedName("trailer") val trailer: TrailerDto?
)

data class TrailerDto(
    @SerializedName("youtube_id") val youtubeId: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("embed_url") val embedUrl: String?,
    @SerializedName("images") val images: TrailerImagesDto?
)

data class TrailerImagesDto(
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("small_image_url") val smallImageUrl: String?,
    @SerializedName("medium_image_url") val mediumImageUrl: String?,
    @SerializedName("large_image_url") val largeImageUrl: String?,
    @SerializedName("maximum_image_url") val maximumImageUrl: String?
)

// Episodes
data class EpisodeVideoDto(
    @SerializedName("mal_id") val malId: Int?,
    @SerializedName("url") val url: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("episode") val episode: String?,
    @SerializedName("images") val images: EpisodeImagesDto?
)

data class EpisodeImagesDto(
    @SerializedName("jpg") val jpg: EpisodeImageUrlDto?
)

data class EpisodeImageUrlDto(
    @SerializedName("image_url") val imageUrl: String?
)

// Music Videos
data class MusicVideoDto(
    @SerializedName("title") val title: String?,
    @SerializedName("video") val video: MusicVideoInfoDto?,
    @SerializedName("meta") val meta: MusicVideoMetaDto?
)

data class MusicVideoInfoDto(
    @SerializedName("youtube_id") val youtubeId: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("embed_url") val embedUrl: String?,
    @SerializedName("images") val images: TrailerImagesDto?
)

data class MusicVideoMetaDto(
    @SerializedName("title") val title: String?,
    @SerializedName("author") val author: String?
)

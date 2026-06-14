package com.yumedev.seijakulist.data.remote.models

import com.google.gson.annotations.SerializedName

data class TrailerDto(
    @SerializedName("youtube_id") val youtubeId: String?,
    val url: String?,
    @SerializedName("embed_url") val embedUrl: String?,
    val images: TrailerImagesDto?
)

data class TrailerImagesDto(
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("small_image_url") val smallImageUrl: String?,
    @SerializedName("medium_image_url") val mediumImageUrl: String?,
    @SerializedName("large_image_url") val largeImageUrl: String?,
    @SerializedName("maximum_image_url") val maximumImageUrl: String?
)

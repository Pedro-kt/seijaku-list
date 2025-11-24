package com.yumedev.seijakulist.data.remote.models

import com.google.gson.annotations.SerializedName

data class ImagesDto(
    val jpg: JpgDto?,
    val webp: WebpDto?
)

data class JpgDto(
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("small_image_url") val smallImageUrl: String?,
    @SerializedName("large_image_url") val largeImageUrl: String?
)

data class WebpDto(
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("small_image_url") val smallImageUrl: String?,
    @SerializedName("large_image_url") val largeImageUrl: String?
)

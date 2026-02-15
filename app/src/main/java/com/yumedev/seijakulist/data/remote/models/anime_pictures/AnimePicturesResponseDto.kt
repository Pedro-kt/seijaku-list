package com.yumedev.seijakulist.data.remote.models.anime_pictures

import com.google.gson.annotations.SerializedName

data class AnimePicturesResponseDto(
    @SerializedName("data") val data: List<AnimePictureDto>
)

data class AnimePictureDto(
    @SerializedName("jpg") val jpg: AnimePictureUrl?,
    @SerializedName("webp") val webp: AnimePictureUrl?
)

data class AnimePictureUrl(
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("small_image_url") val smallImageUrl: String?,
    @SerializedName("large_image_url") val largeImageUrl: String?
)

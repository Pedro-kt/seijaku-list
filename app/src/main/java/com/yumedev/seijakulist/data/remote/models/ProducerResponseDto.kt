package com.yumedev.seijakulist.data.remote.models

import com.google.gson.annotations.SerializedName

data class ProducerResponseDto(
    val data: ProducerDto,
)

data class ProducerDto(
    @SerializedName("mal_id") val malId: Int,
    val titles: List<TitleDto?>,
    val images: ImageProducerDto?,
    val established: String?,
    val about: String?,
    val external: List<ExternalDto>,
)

data class TitleDto(
    val type: String?,
    val title: String?
)

data class ImageProducerDto(
    val jpg: JpgProducerDto?
)

data class JpgProducerDto(
    @SerializedName("image_url") val imageUrl: String?
)

data class ExternalDto(
    val name: String?,
    val url: String?
)


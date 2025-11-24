package com.yumedev.seijakulist.domain.models

import com.yumedev.seijakulist.data.remote.models.ExternalDto
import com.yumedev.seijakulist.data.remote.models.TitleDto

data class ProducerDetail(
    val malId: Int,
    val titles: List<TitleDto?>,
    val images: String,
    val established: String,
    val about: String,
    val external: List<ExternalDto>
)

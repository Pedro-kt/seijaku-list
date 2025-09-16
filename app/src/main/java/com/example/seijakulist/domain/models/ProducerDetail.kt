package com.example.seijakulist.domain.models

import com.example.seijakulist.data.remote.models.ExternalDto
import com.example.seijakulist.data.remote.models.TitleDto

data class ProducerDetail(
    val malId: Int,
    val titles: List<TitleDto?>,
    val images: String,
    val established: String,
    val about: String,
    val external: List<ExternalDto>
)

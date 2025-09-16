package com.example.seijakulist.data.mapper

import com.example.seijakulist.data.remote.models.ProducerDto
import com.example.seijakulist.domain.models.ProducerDetail

fun ProducerDto.toProducerDetail() : ProducerDetail {
    return ProducerDetail(
        malId = malId,
        titles = titles ?: emptyList(),
        images = images?.jpg?.imageUrl ?: "No encontrado",
        established = established ?: "No encontrado",
        about = about ?: "No encontrado",
        external = external
    )
}
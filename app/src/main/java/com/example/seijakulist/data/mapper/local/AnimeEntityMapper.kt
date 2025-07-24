package com.example.seijakulist.data.mapper.local

import com.example.seijakulist.data.local.entities.AnimeEntity
import com.example.seijakulist.domain.models.Anime

fun Anime.toAnimeEntity(): AnimeEntity {
    return AnimeEntity(
        malId = malId,
        title = title,
        imageUrl = image,
        score = score,
    )
}
fun AnimeEntity.toAnimeDomain(): Anime {
    return Anime(
        malId = malId,
        title = title,
        image = imageUrl ?: "",
        score = score,
    )
}
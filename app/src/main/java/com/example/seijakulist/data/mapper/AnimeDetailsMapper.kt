package com.example.seijakulist.data.mapper

import com.example.seijakulist.data.remote.models.AnimeDetailDto
import com.example.seijakulist.domain.models.AnimeDetail

fun AnimeDetailDto.toAnimeDetails(): AnimeDetail {
    return AnimeDetail(
        malId = malId,
        title = title,
        imageUrl = this.images?.webp?.largeImageUrl
            ?: images?.jpg?.largeImageUrl
            ?: "URL de imagen predeterminada",
        synopsis = synopsis,
        episodes = episodes ?: 0,
        duration = duration,
        genres = genres,
        score = score,
        status = status,
        animeType = animeType,
        aired = aired,
        studios = studios
    )
}
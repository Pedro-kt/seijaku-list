package com.example.seijakulist.data.mapper

import com.example.seijakulist.data.remote.models.AnimeDetailDto
import com.example.seijakulist.domain.models.AnimeDetail

fun AnimeDetailDto.toAnimeDetails(): AnimeDetail {
    return AnimeDetail(
        malId = malId,
        title = title,
        titleEnglish = titleEnglish ?: "No encontrado",
        titleJapanese = titleJapanese ?: "No encontrado",
        images = this.images?.webp?.largeImageUrl
            ?: images?.jpg?.largeImageUrl
            ?: "URL de imagen predeterminada",
        synopsis = synopsis ?: "Synopsis no encontrada",
        episodes = episodes ?: 1,
        duration = duration ?: "Duracion no obtenida",
        genres = genres,
        score = score ?: 0.0f,
        status = status ?: "No encontrado",
        typeAnime = typeAnime ?: "No encontrado",
        aired = aired?.airedString ?: "No encontrado",
        source = source ?: "No encontrado",
        studios = studios,
        rating = rating ?: "No encontrado",
        scoreBy = scoreBy ?: 0,
        rank = rank ?: 0,
        season = season ?: "No encontrado",
        year = year ?: 0
    )
}
package com.yumedev.seijakulist.data.mapper

import com.yumedev.seijakulist.data.remote.models.AnimeDetailDto
import com.yumedev.seijakulist.domain.models.AnimeDetail
import com.yumedev.seijakulist.domain.models.BroadcastInfo
import com.yumedev.seijakulist.domain.models.TrailerInfo

fun AnimeDetailDto.toAnimeDetails(): AnimeDetail {
    return AnimeDetail(
        malId = malId,
        title = title,
        titleEnglish = titleEnglish ?: "No encontrado",
        titleJapanese = titleJapanese ?: "No encontrado",
        images = this.images?.webp?.largeImageUrl
            ?: images?.jpg?.largeImageUrl
            ?: "URL de imagen predeterminada",
        bannerImage = null, // MyAnimeList no proporciona bannerImage
        trailer = trailer?.let {
            TrailerInfo(
                youtubeId = it.youtubeId,
                url = it.url,
                embedUrl = it.embedUrl,
                thumbnailUrl = it.images?.maximumImageUrl
                    ?: it.images?.largeImageUrl
                    ?: it.images?.mediumImageUrl
            )
        },
        synopsis = synopsis ?: "Synopsis no encontrada",
        background = background ?: "No disponible",
        episodes = episodes ?: 1,
        duration = duration ?: "Duracion no obtenida",
        broadcast = broadcast?.let {
            BroadcastInfo(
                day = it.day,
                time = it.time,
                timezone = it.timezone,
                fullString = it.broadcastString
            )
        },
        genres = genres,
        score = score ?: 0.0f,
        status = status ?: "No encontrado",
        typeAnime = typeAnime ?: "No encontrado",
        aired = aired?.airedString ?: "No encontrado",
        source = source ?: "No encontrado",
        producers = producers,
        studios = studios,
        demographics = demographics,
        rating = rating ?: "No encontrado",
        scoreBy = scoreBy ?: 0,
        rank = rank ?: 0,
        season = season ?: "No encontrado",
        year = year ?: "No encontrado"
    )
}
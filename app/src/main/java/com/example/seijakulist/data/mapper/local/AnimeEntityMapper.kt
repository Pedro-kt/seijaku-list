package com.example.seijakulist.data.mapper.local

import com.example.seijakulist.data.local.entities.AnimeEntity
import com.example.seijakulist.domain.models.Anime
import com.example.seijakulist.domain.models.AnimeEntityDomain

fun AnimeEntityDomain.toAnimeEntity(): AnimeEntity {
    return AnimeEntity(
        malId = malId,
        title = title,
        imageUrl = image,
        userScore = userScore,
        statusUser = userStatus,
        userOpiniun = userOpiniun,
        totalEpisodes = totalEpisodes,
        episodesWatched = episodesWatched
    )
}

fun AnimeEntity.toAnimeEntityDomain(): AnimeEntityDomain {
    return AnimeEntityDomain(
        malId = malId,
        title = title,
        image = imageUrl ?: "",
        userScore = userScore,
        userStatus = statusUser,
        userOpiniun = userOpiniun,
        totalEpisodes = totalEpisodes,
        episodesWatched = episodesWatched
    )
}
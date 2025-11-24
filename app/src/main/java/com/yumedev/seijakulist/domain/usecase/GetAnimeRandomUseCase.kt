package com.yumedev.seijakulist.domain.usecase

import com.yumedev.seijakulist.data.repository.AnimeRepository
import com.yumedev.seijakulist.domain.models.Anime
import com.yumedev.seijakulist.domain.models.AnimeCard
import javax.inject.Inject

class GetAnimeRandomUseCase @Inject constructor(
    private val animeRepository: AnimeRepository
) {
    suspend operator fun invoke(): AnimeCard {
        val animeResponse = animeRepository.searchAnimeRandom()

        val animeDto = animeResponse.data

        val animeDomain = AnimeCard(
            malId = animeDto?.malId ?: 0,
            title = animeDto?.title ?: "Título predeterminado",
            images = animeDto?.images?.webp?.largeImageUrl
                ?: animeDto?.images?.jpg?.largeImageUrl
                ?: "URL de imagen predeterminada",
            score = animeDto?.score ?: 0.0f,
            status = animeDto?.status ?: "Sin estado",
            genres = animeDto?.genres ?: emptyList(),
            year = (animeDto?.year ?: "N/A").toString(),
            episodes = (animeDto?.episodes ?: "N/A").toString()
        )

        return animeDomain
    }
}
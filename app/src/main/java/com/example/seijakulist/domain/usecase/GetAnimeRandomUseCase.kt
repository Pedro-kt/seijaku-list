package com.example.seijakulist.domain.usecase

import com.example.seijakulist.data.repository.AnimeRepository
import com.example.seijakulist.domain.models.Anime
import javax.inject.Inject

class GetAnimeRandomUseCase @Inject constructor(
    private val animeRepository: AnimeRepository
) {
    suspend operator fun invoke(): Anime {
        val animeResponse = animeRepository.searchAnimeRandom()

        val animeDto = animeResponse.data

        val animeDomain = Anime(
            malId = animeDto?.malId ?: 0, // Usa animeDto.malId
            title = animeDto?.title ?: "Título predeterminado",
            image = animeDto?.images?.webp?.largeImageUrl
                ?: animeDto?.images?.jpg?.largeImageUrl
                ?: "URL de imagen predeterminada",
            score = animeDto?.score ?: 0.0f
        )

        return animeDomain
    }
}
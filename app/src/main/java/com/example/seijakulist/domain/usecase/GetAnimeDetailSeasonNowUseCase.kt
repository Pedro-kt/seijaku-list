package com.example.seijakulist.domain.usecase

import com.example.seijakulist.data.remote.models.GenreDto
import com.example.seijakulist.data.repository.AnimeRepository
import com.example.seijakulist.domain.models.Anime
import com.example.seijakulist.domain.models.AnimeDetailSeasonNow
import javax.inject.Inject


class GetAnimeDetailSeasonNowUseCase @Inject constructor(
    
    private val animeRepository: AnimeRepository
    
) {
    suspend operator fun invoke(): List<AnimeDetailSeasonNow> {

        val animeResponse = animeRepository.searchAnimeSeasonNow()

        val animeDtoList = animeResponse.data ?: emptyList()

        val animeDomainList: List<AnimeDetailSeasonNow> = animeDtoList.map { dto ->
            AnimeDetailSeasonNow(
                malId = dto!!.malId,
                title = dto.title,
                titleEnglish = dto.titleEnglish ?: "Titulo en ingles no encontrado",
                titleJapanese = dto.titleJapanese ?: "Titulo en japones no encontrado",
                images = dto.images?.jpg?.largeImageUrl
                    ?: dto.images?.webp?.largeImageUrl
                    ?: "Imagen predeterminada",
                typeAnime = dto.typeAnime ?: "tipo de anime no encontrado",
                source = dto.source ?: "No encontrado",
                episodes = dto.episodes ?: 0,
                status = dto.status ?: "No encontrado",
                aired = dto.aired,
                duration = dto.duration ?: "No encontrado",
                rating = dto.rating ?: "No encontrado",
                score = dto.score ?: 0.0f,
                scoreBy = dto.scoreBy ?: 0,
                rank = dto.rank ?: 0,
                synopsis = dto.synopsis ?: "No encontrado",
                season = dto.season ?: "No encontrado",
                year = dto.year ?: 0,
                studios = dto.studios,
                genres = dto.genres
            )
        }

        return animeDomainList
    }

}
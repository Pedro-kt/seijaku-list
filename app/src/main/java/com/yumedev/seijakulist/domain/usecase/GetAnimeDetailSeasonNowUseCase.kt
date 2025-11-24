package com.yumedev.seijakulist.domain.usecase

import com.yumedev.seijakulist.data.remote.models.GenreDto
import com.yumedev.seijakulist.data.repository.AnimeRepository
import com.yumedev.seijakulist.domain.models.Anime
import com.yumedev.seijakulist.domain.models.AnimeDetailSeasonNow
import javax.inject.Inject


class GetAnimeDetailSeasonNowUseCase @Inject constructor(
    
    private val animeRepository: AnimeRepository
    
) {
    suspend operator fun invoke(): List<Anime> {

        val animeResponse = animeRepository.searchAnimeSeasonNow()

        val animeDtoList = animeResponse.data ?: emptyList()

        val animeDomainList: List<Anime> = animeDtoList.map { dto ->
            Anime(
                malId = dto!!.malId,
                title = dto.title ?: "Título predeterminado",
                image = dto.images?.webp?.largeImageUrl
                    ?: dto.images?.jpg?.largeImageUrl
                    ?: "URL de imagen predeterminada",
                score = dto.score ?: 0.0f
            )
        }

        return animeDomainList
    }

}
package com.example.seijakulist.domain.usecase

import com.example.seijakulist.data.repository.AnimeRepository
import com.example.seijakulist.domain.models.Anime
import javax.inject.Inject

class GetTopAnimeUseCase @Inject constructor(

    private val animeRepository: AnimeRepository

) {

    suspend operator fun invoke(): List<Anime> {

        val animeResponse = animeRepository.searchTopAnimes()

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
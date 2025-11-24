package com.yumedev.seijakulist.domain.usecase

import com.yumedev.seijakulist.data.repository.AnimeRepository
import com.yumedev.seijakulist.domain.models.Anime
import javax.inject.Inject

class GetTopAnimeFilterUseCase @Inject constructor(
    private val animeRepository: AnimeRepository
) {
    suspend operator fun invoke(filter: String) : List<Anime> {
        val animeResponse = animeRepository.searchTopAnimeFilter(filter)

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
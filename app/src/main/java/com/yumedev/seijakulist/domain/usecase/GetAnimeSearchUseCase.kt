package com.yumedev.seijakulist.domain.usecase

import com.yumedev.seijakulist.data.repository.AnimeRepository
import com.yumedev.seijakulist.domain.models.Anime
import com.yumedev.seijakulist.domain.models.AnimeCard
import com.yumedev.seijakulist.util.getDisplayYear
import com.yumedev.seijakulist.util.getDisplayEpisodes
import javax.inject.Inject

class GetAnimeSearchUseCase @Inject constructor(

    private val animeRepository: AnimeRepository

) {

    suspend operator fun invoke(query: String, page: Int?, type: String? = null): List<AnimeCard> {

        val animeResponse = animeRepository.searchAnimes(query, page, type)

        val animeDtoList = animeResponse.data

        val animeDomainList: List<AnimeCard> = animeDtoList.map { animeDto ->
            AnimeCard(
                malId = animeDto?.malId ?: 0,
                title = animeDto?.title ?: "Título predeterminado",
                images = animeDto?.images?.webp?.largeImageUrl
                    ?: animeDto?.images?.jpg?.largeImageUrl
                    ?: "URL de imagen predeterminada",
                score = animeDto?.score ?: 0.0f,
                status = animeDto?.status ?: "Sin estado",
                type = animeDto?.type ?: "TV",
                genres = animeDto?.genres ?: emptyList(),
                year = getDisplayYear(animeDto?.year, animeDto?.aired?.airedString),
                episodes = getDisplayEpisodes(animeDto?.episodes, animeDto?.type, animeDto?.status)
            )
        }

        return animeDomainList
    }

}

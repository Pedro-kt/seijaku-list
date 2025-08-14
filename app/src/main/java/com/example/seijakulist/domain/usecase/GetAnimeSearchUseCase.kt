package com.example.seijakulist.domain.usecase

import com.example.seijakulist.data.repository.AnimeRepository
import com.example.seijakulist.domain.models.Anime
import com.example.seijakulist.domain.models.AnimeCard
import javax.inject.Inject

class GetAnimeSearchUseCase @Inject constructor(

    private val animeRepository: AnimeRepository

) {

    suspend operator fun invoke(query: String, page: Int?): List<AnimeCard> {

        val animeResponse = animeRepository.searchAnimes(query, page)

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
                genres = animeDto?.genres ?: emptyList(),
                year = (animeDto?.year ?: "N/A").toString(),
                episodes = (animeDto?.episodes ?: "N/A").toString()
            )
        }

        return animeDomainList
    }

}

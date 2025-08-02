package com.example.seijakulist.domain.usecase

import com.example.seijakulist.data.repository.AnimeRepository
import com.example.seijakulist.domain.models.Anime
import javax.inject.Inject

class GetAnimeSearchUseCase @Inject constructor(

    private val animeRepository: AnimeRepository

) {

    suspend operator fun invoke(query: String, page: Int?): List<Anime> {

        val animeResponse = animeRepository.searchAnimes(query, page)

        val animeDtoList = animeResponse.data ?: emptyList()

        val animeDomainList: List<Anime> = animeDtoList.map { animeDto ->
            Anime(
                malId = animeDto!!.malId,
                title = animeDto.title ?: "Título predeterminado",
                image = animeDto.images?.webp?.largeImageUrl
                    ?: animeDto.images?.jpg?.largeImageUrl
                    ?: animeDto.images?.webp?.imageUrl
                    ?: animeDto.images?.jpg?.imageUrl
                    ?: "URL de imagen predeterminada",
                score = animeDto.score ?: 0.0f
            )
        }

        return animeDomainList
    }

}

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
                malId = animeDto!!.malId, //tener cuidado aca, posiblemente no sea nulo nunca, (pero puede pasar que sea nulo, y generaria un crasheo)
                title = animeDto.title,
                image = animeDto.images?.webp?.largeImageUrl
                    ?: animeDto.images?.jpg?.largeImageUrl
                    ?: "URL de imagen predeterminada",
                synopsis = animeDto.synopsis ?: "Synopsis no disponible",
                score = animeDto.score ?: 0.0f
            )
        }

        return animeDomainList
    }

}

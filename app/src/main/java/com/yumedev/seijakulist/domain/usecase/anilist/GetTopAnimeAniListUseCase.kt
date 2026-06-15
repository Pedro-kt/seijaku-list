package com.yumedev.seijakulist.domain.usecase.anilist

import com.yumedev.seijakulist.data.repository.AnimeAniListRepository
import com.yumedev.seijakulist.domain.models.Anime
import com.yumedev.seijakulist.domain.models.AnimeCard
import javax.inject.Inject

/**
 * UseCase para obtener los mejores anime usando AniList API
 *
 * Obtiene anime ordenados por puntuación y popularidad
 *
 * @param repository Repositorio de AniList
 */
class GetTopAnimeAniListUseCase @Inject constructor(
    private val repository: AnimeAniListRepository
) {
    /**
     * Obtiene lista de los mejores anime
     *
     * @param page Número de página (default: 1)
     * @param perPage Elementos por página (default: 20)
     * @return Lista de Anime (AnimeCard convertido a Anime)
     */
    suspend operator fun invoke(page: Int = 1, perPage: Int = 20): List<Anime> {
        val animeCards = repository.getTopAnime(page = page, perPage = perPage)

        // Convertir AnimeCard a Anime para mantener compatibilidad con ViewModels existentes
        return animeCards.map { card ->
            Anime(
                malId = card.malId,
                title = card.title,
                image = card.images,
                score = card.score
            )
        }
    }

    /**
     * Obtiene lista de los mejores anime como AnimeCard
     *
     * @param page Número de página (default: 1)
     * @param perPage Elementos por página (default: 20)
     * @return Lista de AnimeCard
     */
    suspend fun getAsCards(page: Int = 1, perPage: Int = 20): List<AnimeCard> {
        return repository.getTopAnime(page = page, perPage = perPage)
    }
}

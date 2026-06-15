package com.yumedev.seijakulist.domain.usecase.anilist

import com.yumedev.seijakulist.data.repository.AnimeAniListRepository
import com.yumedev.seijakulist.domain.models.AnimeRecommendation
import javax.inject.Inject

/**
 * UseCase para obtener recomendaciones de un anime usando AniList API
 *
 * @param repository Repositorio de AniList
 */
class GetAnimeRecommendationsAniListUseCase @Inject constructor(
    private val repository: AnimeAniListRepository
) {
    /**
     * Obtiene recomendaciones para un anime
     *
     * @param animeId ID de AniList (opcional)
     * @param malId MyAnimeList ID (opcional, usado como fallback)
     * @return Lista de AnimeRecommendation
     */
    suspend operator fun invoke(
        animeId: Int? = null,
        malId: Int? = null
    ): List<AnimeRecommendation> {
        return repository.getAnimeRecommendations(animeId = animeId, malId = malId)
    }
}

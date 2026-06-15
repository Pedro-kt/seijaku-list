package com.yumedev.seijakulist.domain.usecase.anilist

import com.yumedev.seijakulist.data.repository.AnimeAniListRepository
import com.yumedev.seijakulist.domain.models.AnimeEpisode
import javax.inject.Inject

/**
 * UseCase para obtener episodios de streaming de un anime usando AniList API
 *
 * Nota: AniList proporciona streamingEpisodes (enlaces a sitios de streaming).
 * No incluye información tan detallada como Jikan (score, filler, recap, etc).
 *
 * @param repository Repositorio de AniList
 */
class GetAnimeEpisodesAniListUseCase @Inject constructor(
    private val repository: AnimeAniListRepository
) {
    /**
     * Obtiene episodios de streaming de un anime
     *
     * @param animeId ID de AniList (opcional)
     * @param malId MyAnimeList ID (opcional, usado como fallback)
     * @return Lista de AnimeEpisode con información de streaming
     */
    suspend operator fun invoke(
        animeId: Int? = null,
        malId: Int? = null
    ): List<AnimeEpisode> {
        return repository.getAnimeEpisodesById(animeId = animeId, malId = malId)
    }
}

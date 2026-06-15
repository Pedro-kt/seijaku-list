package com.yumedev.seijakulist.domain.usecase.anilist

import com.yumedev.seijakulist.data.repository.AnimeAniListRepository
import com.yumedev.seijakulist.domain.models.AnimeVideos
import javax.inject.Inject

/**
 * UseCase para obtener videos de un anime usando AniList API
 *
 * Nota: AniList proporciona trailer y episodios de streaming.
 * NO incluye music videos como Jikan.
 *
 * @param repository Repositorio de AniList
 */
class GetAnimeVideosAniListUseCase @Inject constructor(
    private val repository: AnimeAniListRepository
) {
    /**
     * Obtiene videos (trailer y episodios) de un anime
     *
     * @param animeId ID de AniList (opcional)
     * @param malId MyAnimeList ID (opcional, usado como fallback)
     * @return AnimeVideos con trailer y episodios de streaming
     */
    suspend operator fun invoke(
        animeId: Int? = null,
        malId: Int? = null
    ): AnimeVideos {
        return repository.getAnimeVideosById(animeId = animeId, malId = malId)
    }
}

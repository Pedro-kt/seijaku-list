package com.yumedev.seijakulist.domain.usecase.anilist

import com.yumedev.seijakulist.data.repository.AnimeAniListRepository
import com.yumedev.seijakulist.domain.models.AnimeDetail
import javax.inject.Inject

/**
 * UseCase para obtener detalles de un anime usando AniList API
 *
 * @param repository Repositorio de AniList
 */
class GetAnimeDetailAniListUseCase @Inject constructor(
    private val repository: AnimeAniListRepository
) {
    /**
     * Obtiene detalles completos de un anime
     *
     * @param animeId ID de AniList (opcional)
     * @param malId MyAnimeList ID (opcional, usado como fallback)
     * @return AnimeDetail con información completa del anime
     */
    suspend operator fun invoke(animeId: Int? = null, malId: Int? = null): AnimeDetail {
        return repository.getAnimeDetailsById(animeId = animeId, malId = malId)
    }
}

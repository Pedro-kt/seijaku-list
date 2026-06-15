package com.yumedev.seijakulist.domain.usecase.anilist

import com.yumedev.seijakulist.data.repository.AnimeAniListRepository
import com.yumedev.seijakulist.domain.models.MangaDetail
import javax.inject.Inject

/**
 * UseCase para obtener detalles de un manga usando AniList API
 *
 * @param repository Repositorio de AniList
 */
class GetMangaDetailAniListUseCase @Inject constructor(
    private val repository: AnimeAniListRepository
) {
    /**
     * Obtiene los detalles completos de un manga
     *
     * @param mangaId ID de AniList (opcional)
     * @param malId MyAnimeList ID (opcional)
     * @return MangaDetail con información completa del manga
     */
    suspend operator fun invoke(
        mangaId: Int? = null,
        malId: Int? = null
    ): MangaDetail {
        return repository.getMangaDetailsById(mangaId = mangaId, malId = malId)
    }
}

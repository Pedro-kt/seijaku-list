package com.yumedev.seijakulist.domain.usecase.anilist

import com.yumedev.seijakulist.data.repository.AnimeAniListRepository
import com.yumedev.seijakulist.domain.models.AnimeCard
import javax.inject.Inject

/**
 * UseCase para obtener anime que están actualmente en emisión usando AniList API
 *
 * @param repository Repositorio de AniList
 */
class GetAiringAnimeAniListUseCase @Inject constructor(
    private val repository: AnimeAniListRepository
) {
    /**
     * Obtiene anime que están siendo emitidos actualmente
     *
     * @param page Número de página (default: 1)
     * @param perPage Elementos por página (default: 20)
     * @return Lista de AnimeCard de anime en emisión
     */
    suspend operator fun invoke(page: Int = 1, perPage: Int = 20): List<AnimeCard> {
        return repository.getAiringAnime(page = page, perPage = perPage)
    }
}

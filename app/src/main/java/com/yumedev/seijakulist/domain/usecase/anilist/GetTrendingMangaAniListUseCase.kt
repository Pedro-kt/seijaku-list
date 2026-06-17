package com.yumedev.seijakulist.domain.usecase.anilist

import com.yumedev.seijakulist.data.repository.AnimeAniListRepository
import com.yumedev.seijakulist.domain.models.AnimeCard
import javax.inject.Inject

/**
 * UseCase para obtener manga en tendencia usando AniList API
 *
 * @param repository Repositorio de AniList
 */
class GetTrendingMangaAniListUseCase @Inject constructor(
    private val repository: AnimeAniListRepository
) {
    /**
     * Obtiene manga que están en tendencia actualmente
     *
     * @param page Número de página (default: 1)
     * @param perPage Elementos por página (default: 20)
     * @return Lista de AnimeCard ordenados por tendencia
     */
    suspend operator fun invoke(page: Int = 1, perPage: Int = 20): List<AnimeCard> {
        return repository.getTrendingManga(page = page, perPage = perPage)
    }
}

package com.yumedev.seijakulist.domain.usecase.anilist

import com.yumedev.seijakulist.data.remote.graphql.type.MediaFormat
import com.yumedev.seijakulist.data.repository.AnimeAniListRepository
import com.yumedev.seijakulist.domain.models.AnimeCard
import javax.inject.Inject

/**
 * UseCase para obtener manga filtrados por formato usando AniList API
 *
 * @param repository Repositorio de AniList
 */
class GetMangaByFormatAniListUseCase @Inject constructor(
    private val repository: AnimeAniListRepository
) {
    /**
     * Obtiene manga filtrados por formato
     *
     * @param format Formato del manga (MANGA, ONE_SHOT, NOVEL, etc.)
     * @param page Número de página (default: 1)
     * @param perPage Elementos por página (default: 20)
     * @return Lista de AnimeCard del formato especificado
     */
    suspend operator fun invoke(
        format: MediaFormat,
        page: Int = 1,
        perPage: Int = 20
    ): List<AnimeCard> {
        return repository.getMangaByFormat(format = format, page = page, perPage = perPage)
    }
}

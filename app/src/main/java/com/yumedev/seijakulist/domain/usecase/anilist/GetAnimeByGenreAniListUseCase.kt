package com.yumedev.seijakulist.domain.usecase.anilist

import com.yumedev.seijakulist.data.repository.AnimeAniListRepository
import com.yumedev.seijakulist.domain.models.AnimeCard
import javax.inject.Inject

/**
 * UseCase para obtener anime por género usando AniList API
 *
 * @param repository Repositorio de AniList
 */
class GetAnimeByGenreAniListUseCase @Inject constructor(
    private val repository: AnimeAniListRepository
) {
    /**
     * Obtiene anime filtrados por género
     *
     * @param genre Nombre del género (ej: "Action", "Comedy", "Drama")
     * @param page Número de página (default: 1)
     * @param perPage Elementos por página (default: 20)
     * @return Lista de AnimeCard del género especificado
     */
    suspend operator fun invoke(
        genre: String,
        page: Int = 1,
        perPage: Int = 20
    ): List<AnimeCard> {
        return repository.getAnimeByGenre(
            genre = genre,
            page = page,
            perPage = perPage
        )
    }
}

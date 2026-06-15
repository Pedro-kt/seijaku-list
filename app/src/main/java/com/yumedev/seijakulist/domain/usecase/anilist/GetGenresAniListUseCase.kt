package com.yumedev.seijakulist.domain.usecase.anilist

import com.yumedev.seijakulist.data.repository.AnimeAniListRepository
import com.yumedev.seijakulist.domain.models.Genre
import javax.inject.Inject

/**
 * UseCase para obtener la lista de géneros usando AniList API
 *
 * @param repository Repositorio de AniList
 */
class GetGenresAniListUseCase @Inject constructor(
    private val repository: AnimeAniListRepository
) {
    /**
     * Obtiene la lista de todos los géneros disponibles
     *
     * Nota: AniList solo proporciona nombres de géneros.
     * No incluye malId, url, ni count como Jikan.
     *
     * @return Lista de Genre con datos limitados
     */
    suspend operator fun invoke(): List<Genre> {
        return repository.getGenresAnime()
    }
}

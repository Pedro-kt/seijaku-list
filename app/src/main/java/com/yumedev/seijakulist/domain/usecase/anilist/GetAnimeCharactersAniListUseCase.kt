package com.yumedev.seijakulist.domain.usecase.anilist

import com.yumedev.seijakulist.data.repository.AnimeAniListRepository
import com.yumedev.seijakulist.domain.models.AnimeCharactersDetail
import javax.inject.Inject

/**
 * UseCase para obtener personajes de un anime usando AniList API
 *
 * @param repository Repositorio de AniList
 */
class GetAnimeCharactersAniListUseCase @Inject constructor(
    private val repository: AnimeAniListRepository
) {
    /**
     * Obtiene los personajes de un anime
     *
     * @param animeId ID de AniList (opcional)
     * @param malId MyAnimeList ID (opcional, usado como fallback)
     * @return Lista de AnimeCharactersDetail
     */
    suspend operator fun invoke(
        animeId: Int? = null,
        malId: Int? = null
    ): List<AnimeCharactersDetail> {
        return repository.getAnimeCharactersById(animeId = animeId, malId = malId)
    }
}

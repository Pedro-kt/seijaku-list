package com.yumedev.seijakulist.domain.usecase.anilist

import com.yumedev.seijakulist.data.repository.AnimeAniListRepository
import com.yumedev.seijakulist.domain.models.AnimeCharactersDetail
import javax.inject.Inject

/**
 * UseCase para obtener personajes de un manga usando AniList API
 *
 * @param repository Repositorio de AniList
 */
class GetMangaCharactersAniListUseCase @Inject constructor(
    private val repository: AnimeAniListRepository
) {
    /**
     * Obtiene los personajes de un manga
     *
     * @param mangaId ID de AniList (opcional)
     * @param malId MyAnimeList ID (opcional)
     * @return Lista de AnimeCharactersDetail (reutilizado para manga)
     */
    suspend operator fun invoke(
        mangaId: Int? = null,
        malId: Int? = null
    ): List<AnimeCharactersDetail> {
        return repository.getMangaCharactersById(mangaId = mangaId, malId = malId)
    }
}

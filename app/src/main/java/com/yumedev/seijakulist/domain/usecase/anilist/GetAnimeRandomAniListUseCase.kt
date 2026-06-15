package com.yumedev.seijakulist.domain.usecase.anilist

import com.yumedev.seijakulist.data.repository.AnimeAniListRepository
import com.yumedev.seijakulist.domain.models.AnimeCard
import javax.inject.Inject

/**
 * UseCase para obtener un anime aleatorio usando AniList API
 *
 * @param repository Repositorio de AniList
 */
class GetAnimeRandomAniListUseCase @Inject constructor(
    private val repository: AnimeAniListRepository
) {
    /**
     * Obtiene un anime aleatorio
     *
     * @return AnimeCard aleatorio o null si no hay resultados
     */
    suspend operator fun invoke(): AnimeCard? {
        return repository.getAnimeRandom()
    }
}

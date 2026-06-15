package com.yumedev.seijakulist.domain.usecase.anilist

import com.yumedev.seijakulist.data.repository.AnimeAniListRepository
import com.yumedev.seijakulist.domain.models.CharacterDetail
import javax.inject.Inject

/**
 * UseCase para obtener detalles de un personaje usando AniList API
 *
 * @param repository Repositorio de AniList
 */
class GetCharacterDetailAniListUseCase @Inject constructor(
    private val repository: AnimeAniListRepository
) {
    /**
     * Obtiene detalles completos de un personaje
     *
     * @param characterId ID del personaje en AniList
     * @return CharacterDetail con información completa del personaje
     */
    suspend operator fun invoke(characterId: Int): CharacterDetail {
        return repository.getCharacterDetailById(characterId)
    }
}

package com.yumedev.seijakulist.domain.usecase.anilist

import com.yumedev.seijakulist.data.repository.AnimeAniListRepository
import com.yumedev.seijakulist.domain.models.AnimePicture
import javax.inject.Inject

/**
 * UseCase para obtener imágenes de un anime usando AniList API
 *
 * Nota: AniList proporciona coverImage y bannerImage.
 * No tiene galería de imágenes como Jikan.
 *
 * @param repository Repositorio de AniList
 */
class GetAnimePicturesAniListUseCase @Inject constructor(
    private val repository: AnimeAniListRepository
) {
    /**
     * Obtiene imágenes de un anime
     *
     * @param animeId ID de AniList (opcional)
     * @param malId MyAnimeList ID (opcional, usado como fallback)
     * @return Lista de AnimePicture (cover y banner)
     */
    suspend operator fun invoke(
        animeId: Int? = null,
        malId: Int? = null
    ): List<AnimePicture> {
        return repository.getAnimePicturesById(animeId = animeId, malId = malId)
    }
}

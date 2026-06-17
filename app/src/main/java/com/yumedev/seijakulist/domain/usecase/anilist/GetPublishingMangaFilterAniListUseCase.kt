package com.yumedev.seijakulist.domain.usecase.anilist

import com.yumedev.seijakulist.data.remote.graphql.type.MediaFormat
import com.yumedev.seijakulist.data.remote.graphql.type.MediaSort
import com.yumedev.seijakulist.data.remote.graphql.type.MediaStatus
import com.yumedev.seijakulist.data.repository.AnimeAniListRepository
import com.yumedev.seijakulist.domain.models.Anime
import javax.inject.Inject

/**
 * UseCase para obtener Manga en Publicación filtrado por formato usando AniList API
 *
 * @param repository Repositorio de AniList
 */
class GetPublishingMangaFilterAniListUseCase @Inject constructor(
    private val repository: AnimeAniListRepository
) {
    /**
     * Obtiene manga en publicación filtrado por formato
     *
     * @param filter Tipo de formato: "manga", "one_shot", "novel"
     * @param page Número de página (default: 1)
     * @return Lista de Anime en publicación del formato especificado
     */
    suspend operator fun invoke(filter: String, page: Int = 1): List<Anime> {
        // Convertir el filtro string a MediaFormat
        val format = mapFilterToMediaFormat(filter)

        // Obtener manga en publicación usando searchMangaAdvanced
        val mangaCards = repository.searchMangaAdvanced(
            format = format,
            status = MediaStatus.RELEASING,
            sort = listOf(MediaSort.POPULARITY_DESC),
            page = page,
            perPage = 25
        )

        // Convertir AnimeCard a Anime (modelo más simple para HomeScreen)
        return mangaCards.map { card ->
            Anime(
                malId = card.malId,
                title = card.title,
                image = card.images,
                score = card.score
            )
        }
    }

    /**
     * Mapea el filtro string a MediaFormat de AniList para manga
     *
     * @param filter Tipo como string ("manga", "one_shot", etc.)
     * @return MediaFormat correspondiente o null si no coincide
     */
    private fun mapFilterToMediaFormat(filter: String): MediaFormat? {
        return when (filter.lowercase()) {
            "manga" -> MediaFormat.MANGA
            "one_shot" -> MediaFormat.ONE_SHOT
            "novel" -> MediaFormat.NOVEL
            else -> null
        }
    }
}

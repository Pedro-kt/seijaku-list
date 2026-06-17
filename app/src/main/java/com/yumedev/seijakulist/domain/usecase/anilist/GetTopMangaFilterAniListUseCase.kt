package com.yumedev.seijakulist.domain.usecase.anilist

import com.yumedev.seijakulist.data.remote.graphql.type.MediaFormat
import com.yumedev.seijakulist.data.remote.graphql.type.MediaSort
import com.yumedev.seijakulist.data.repository.AnimeAniListRepository
import com.yumedev.seijakulist.domain.models.Anime
import javax.inject.Inject

/**
 * UseCase para obtener Top Manga filtrado por formato usando AniList API
 *
 * @param repository Repositorio de AniList
 */
class GetTopMangaFilterAniListUseCase @Inject constructor(
    private val repository: AnimeAniListRepository
) {
    /**
     * Obtiene top manga filtrado por formato
     *
     * @param filter Tipo de formato: "manga", "one_shot", "novel", "manhwa", "manhua"
     * @param page Número de página (default: 1)
     * @return Lista de Anime ordenados por score descendente
     */
    suspend operator fun invoke(filter: String, page: Int = 1): List<Anime> {
        // Convertir el filtro string a MediaFormat
        val format = mapFilterToMediaFormat(filter)

        // Obtener manga usando searchMangaAdvanced con formato y ordenamiento por score
        val mangaCards = repository.searchMangaAdvanced(
            format = format,
            sort = listOf(MediaSort.SCORE_DESC, MediaSort.POPULARITY_DESC),
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

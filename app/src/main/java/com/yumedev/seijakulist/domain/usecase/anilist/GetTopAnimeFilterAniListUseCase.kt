package com.yumedev.seijakulist.domain.usecase.anilist

import com.yumedev.seijakulist.data.remote.graphql.type.MediaFormat
import com.yumedev.seijakulist.data.remote.graphql.type.MediaSort
import com.yumedev.seijakulist.data.repository.AnimeAniListRepository
import com.yumedev.seijakulist.domain.models.Anime
import com.yumedev.seijakulist.domain.models.AnimeCard
import javax.inject.Inject

/**
 * UseCase para obtener Top Anime filtrado por formato usando AniList API
 *
 * @param repository Repositorio de AniList
 */
class GetTopAnimeFilterAniListUseCase @Inject constructor(
    private val repository: AnimeAniListRepository
) {
    /**
     * Obtiene top anime filtrado por formato
     *
     * @param filter Tipo de formato: "tv", "movie", "ova", "ona", "special", "music"
     * @param page Número de página (default: 1)
     * @return Lista de Anime ordenados por score descendente
     */
    suspend operator fun invoke(filter: String, page: Int = 1): List<Anime> {
        // Convertir el filtro string a MediaFormat
        val format = mapFilterToMediaFormat(filter)

        // Obtener anime usando searchAnimeAdvanced con formato y ordenamiento por score
        val animeCards = repository.searchAnimeAdvanced(
            format = format,
            sort = listOf(MediaSort.SCORE_DESC, MediaSort.POPULARITY_DESC),
            page = page,
            perPage = 25
        )

        // Convertir AnimeCard a Anime (modelo más simple para HomeScreen)
        return animeCards.map { card ->
            Anime(
                malId = card.malId,
                title = card.title,
                image = card.images,
                score = card.score
            )
        }
    }

    /**
     * Mapea el filtro string a MediaFormat de AniList
     *
     * @param filter Tipo como string ("tv", "movie", etc.)
     * @return MediaFormat correspondiente o null si no coincide
     */
    private fun mapFilterToMediaFormat(filter: String): MediaFormat? {
        return when (filter.lowercase()) {
            "tv" -> MediaFormat.TV
            "movie" -> MediaFormat.MOVIE
            "ova" -> MediaFormat.OVA
            "ona" -> MediaFormat.ONA
            "special" -> MediaFormat.SPECIAL
            "music" -> MediaFormat.MUSIC
            else -> null
        }
    }
}

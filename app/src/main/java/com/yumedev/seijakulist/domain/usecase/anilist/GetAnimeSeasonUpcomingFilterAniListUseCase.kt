package com.yumedev.seijakulist.domain.usecase.anilist

import com.yumedev.seijakulist.data.remote.graphql.type.MediaFormat
import com.yumedev.seijakulist.data.remote.graphql.type.MediaSort
import com.yumedev.seijakulist.data.remote.graphql.type.MediaStatus
import com.yumedev.seijakulist.data.repository.AnimeAniListRepository
import com.yumedev.seijakulist.domain.models.Anime
import javax.inject.Inject

/**
 * UseCase para obtener Anime Upcoming filtrado por formato usando AniList API
 *
 * @param repository Repositorio de AniList
 */
class GetAnimeSeasonUpcomingFilterAniListUseCase @Inject constructor(
    private val repository: AnimeAniListRepository
) {
    /**
     * Obtiene upcoming anime filtrado por formato
     *
     * @param filter Tipo de formato: "tv", "movie", "ova", "ona", "special", "music"
     * @param page Número de página (default: 1)
     * @return Lista de Anime con status NOT_YET_RELEASED ordenados por popularidad
     */
    suspend operator fun invoke(filter: String, page: Int = 1): List<Anime> {
        // Convertir el filtro string a MediaFormat
        val format = mapFilterToMediaFormat(filter)

        // Obtener anime usando searchAnimeAdvanced con formato, status y ordenamiento
        val animeCards = repository.searchAnimeAdvanced(
            format = format,
            status = MediaStatus.NOT_YET_RELEASED,
            sort = listOf(MediaSort.POPULARITY_DESC),
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

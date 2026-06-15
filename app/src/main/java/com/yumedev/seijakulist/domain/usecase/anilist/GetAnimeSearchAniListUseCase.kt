package com.yumedev.seijakulist.domain.usecase.anilist

import com.yumedev.seijakulist.data.remote.graphql.type.MediaFormat
import com.yumedev.seijakulist.data.repository.AnimeAniListRepository
import com.yumedev.seijakulist.domain.models.AnimeCard
import javax.inject.Inject

/**
 * UseCase para buscar anime usando AniList API
 *
 * @param repository Repositorio de AniList
 */
class GetAnimeSearchAniListUseCase @Inject constructor(
    private val repository: AnimeAniListRepository
) {
    /**
     * Busca anime por query de texto
     *
     * @param query Texto de búsqueda
     * @param page Número de página (default: 1)
     * @param type Tipo de formato (opcional): "tv", "movie", "ova", "ona", "special", "music"
     * @return Lista de AnimeCard con resultados de búsqueda
     */
    suspend operator fun invoke(
        query: String,
        page: Int = 1,
        type: String? = null
    ): List<AnimeCard> {
        // Convertir el tipo string a MediaFormat si está presente
        val format = type?.let { mapTypeToFormat(it) }

        return if (format != null) {
            // Búsqueda con filtro de formato
            repository.searchAnimeAdvanced(
                query = query,
                format = format,
                page = page
            )
        } else {
            // Búsqueda simple
            repository.searchAnime(
                query = query,
                page = page
            )
        }
    }

    /**
     * Mapea el tipo string a MediaFormat de AniList
     *
     * @param type Tipo como string ("tv", "movie", etc.)
     * @return MediaFormat correspondiente o null
     */
    private fun mapTypeToFormat(type: String): MediaFormat? {
        return when (type.lowercase()) {
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

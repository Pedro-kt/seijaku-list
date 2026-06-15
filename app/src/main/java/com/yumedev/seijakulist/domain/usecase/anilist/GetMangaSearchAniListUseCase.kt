package com.yumedev.seijakulist.domain.usecase.anilist

import com.yumedev.seijakulist.data.remote.graphql.type.MediaFormat
import com.yumedev.seijakulist.data.repository.AnimeAniListRepository
import com.yumedev.seijakulist.domain.models.AnimeCard
import javax.inject.Inject

/**
 * UseCase para buscar manga usando AniList API
 *
 * @param repository Repositorio de AniList
 */
class GetMangaSearchAniListUseCase @Inject constructor(
    private val repository: AnimeAniListRepository
) {
    /**
     * Busca manga por query de texto
     *
     * @param query Texto de búsqueda
     * @param page Número de página (default: 1)
     * @param type Tipo de formato de manga (opcional): "manga", "novel", "one_shot"
     * @return Lista de AnimeCard con resultados de búsqueda (reutilizamos AnimeCard para manga)
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
            repository.searchMangaAdvanced(
                query = query,
                format = format,
                page = page
            )
        } else {
            // Búsqueda simple
            repository.searchManga(
                query = query,
                page = page
            )
        }
    }

    /**
     * Mapea el tipo string a MediaFormat de AniList para manga
     *
     * @param type Tipo como string ("manga", "novel", "one_shot")
     * @return MediaFormat correspondiente o null
     */
    private fun mapTypeToFormat(type: String): MediaFormat? {
        return when (type.lowercase()) {
            "manga" -> MediaFormat.MANGA
            "novel" -> MediaFormat.NOVEL
            "one_shot", "oneshot" -> MediaFormat.ONE_SHOT
            else -> null
        }
    }
}

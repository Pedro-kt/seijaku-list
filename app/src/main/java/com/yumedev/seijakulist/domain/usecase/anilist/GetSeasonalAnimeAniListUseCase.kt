package com.yumedev.seijakulist.domain.usecase.anilist

import com.yumedev.seijakulist.data.remote.graphql.type.MediaSeason
import com.yumedev.seijakulist.data.repository.AnimeAniListRepository
import com.yumedev.seijakulist.domain.models.Anime
import com.yumedev.seijakulist.domain.models.AnimeCard
import java.util.Calendar
import javax.inject.Inject

/**
 * UseCase para obtener anime de temporada usando AniList API
 *
 * Equivalente a GetAnimeDetailSeasonNowUseCase pero usando AniList
 *
 * @param repository Repositorio de AniList
 */
class GetSeasonalAnimeAniListUseCase @Inject constructor(
    private val repository: AnimeAniListRepository
) {
    /**
     * Obtiene anime de la temporada actual
     *
     * @param page Número de página (default: 1)
     * @param perPage Elementos por página (default: 20)
     * @return Lista de Anime de la temporada actual
     */
    suspend operator fun invoke(page: Int = 1, perPage: Int = 20): List<Anime> {
        val (season, year) = getCurrentSeasonAndYear()
        val animeCards = repository.getSeasonalAnime(
            season = season,
            year = year,
            page = page,
            perPage = perPage
        )

        // Convertir AnimeCard a Anime para mantener compatibilidad
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
     * Obtiene anime de la temporada actual como AnimeCard
     *
     * @param page Número de página (default: 1)
     * @param perPage Elementos por página (default: 20)
     * @return Lista de AnimeCard de la temporada actual
     */
    suspend fun getAsCards(page: Int = 1, perPage: Int = 20): List<AnimeCard> {
        val (season, year) = getCurrentSeasonAndYear()
        return repository.getSeasonalAnime(
            season = season,
            year = year,
            page = page,
            perPage = perPage
        )
    }

    /**
     * Obtiene anime de una temporada específica
     *
     * @param season Temporada (WINTER, SPRING, SUMMER, FALL)
     * @param year Año
     * @param page Número de página (default: 1)
     * @param perPage Elementos por página (default: 20)
     * @return Lista de AnimeCard
     */
    suspend fun getBySeason(
        season: MediaSeason,
        year: Int,
        page: Int = 1,
        perPage: Int = 20
    ): List<AnimeCard> {
        return repository.getSeasonalAnime(
            season = season,
            year = year,
            page = page,
            perPage = perPage
        )
    }

    /**
     * Obtiene anime de próxima temporada (upcoming)
     *
     * @param page Número de página (default: 1)
     * @param perPage Elementos por página (default: 20)
     * @return Lista de AnimeCard
     */
    suspend fun getUpcoming(page: Int = 1, perPage: Int = 20): List<AnimeCard> {
        return repository.getUpcomingAnime(page = page, perPage = perPage)
    }

    /**
     * Determina la temporada y año actual
     *
     * @return Par (MediaSeason, Int) con la temporada y año actual
     */
    private fun getCurrentSeasonAndYear(): Pair<MediaSeason, Int> {
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH es 0-based
        val year = calendar.get(Calendar.YEAR)

        val season = when (month) {
            in 1..3 -> MediaSeason.WINTER
            in 4..6 -> MediaSeason.SPRING
            in 7..9 -> MediaSeason.SUMMER
            in 10..12 -> MediaSeason.FALL
            else -> MediaSeason.WINTER
        }

        return Pair(season, year)
    }
}

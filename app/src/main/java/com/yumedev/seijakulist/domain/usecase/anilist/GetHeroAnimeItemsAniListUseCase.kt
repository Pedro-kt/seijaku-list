package com.yumedev.seijakulist.domain.usecase.anilist

import com.yumedev.seijakulist.data.repository.AnimeAniListRepository
import com.yumedev.seijakulist.domain.models.HeroAnimeItem
import javax.inject.Inject

/**
 * UseCase para obtener items de anime para la sección Hero usando AniList API
 *
 * Proporciona diferentes tipos de anime para mostrar en el carousel principal:
 * - Clásicos (top anime)
 * - Próximamente (upcoming)
 * - En tendencia (trending)
 * - Al aire (currently airing)
 * - Recomendaciones
 *
 * @param repository Repositorio de AniList
 */
class GetHeroAnimeItemsAniListUseCase @Inject constructor(
    private val repository: AnimeAniListRepository
) {
    /**
     * Obtiene un anime clásico (top rated) para Hero Section
     *
     * @return HeroAnimeItem o null
     */
    suspend fun getTopClassic(): HeroAnimeItem? {
        return repository.getTopClassicAnime()
    }

    /**
     * Obtiene un anime próximo a estrenarse para Hero Section
     *
     * @return HeroAnimeItem o null
     */
    suspend fun getUpcoming(): HeroAnimeItem? {
        return repository.getUpcomingHeroItem()
    }

    /**
     * Obtiene un anime en tendencia para Hero Section
     *
     * @return HeroAnimeItem o null
     */
    suspend fun getTrending(): HeroAnimeItem? {
        return repository.getTrendingHeroItem()
    }

    /**
     * Obtiene un anime que está actualmente en emisión para Hero Section
     *
     * @return HeroAnimeItem o null
     */
    suspend fun getCurrentlyAiring(): HeroAnimeItem? {
        return repository.getCurrentlyAiringHeroItem()
    }

    /**
     * Obtiene una recomendación basada en un anime específico para Hero Section
     *
     * @param animeId ID del anime base
     * @return HeroAnimeItem o null
     */
    suspend fun getRecommendationFor(animeId: Int): HeroAnimeItem? {
        return repository.getRecommendationForAnime(animeId)
    }

    /**
     * Obtiene una colección mixta de items para Hero Section
     *
     * Obtiene varios tipos de anime y retorna una lista mezclada
     *
     * @return Lista de HeroAnimeItem (no nulos)
     */
    suspend fun getMixedCollection(): List<HeroAnimeItem> {
        val items = mutableListOf<HeroAnimeItem>()

        // Intentar obtener cada tipo (algunos pueden ser null)
        getTopClassic()?.let { items.add(it) }
        getTrending()?.let { items.add(it) }
        getCurrentlyAiring()?.let { items.add(it) }
        getUpcoming()?.let { items.add(it) }

        return items
    }
}

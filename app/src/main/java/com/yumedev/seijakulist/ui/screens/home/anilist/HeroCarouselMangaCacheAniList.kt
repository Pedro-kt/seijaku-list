package com.yumedev.seijakulist.ui.screens.home.anilist

import com.yumedev.seijakulist.common.RequestThrottler
import com.yumedev.seijakulist.data.repository.AnimeAniListRepository
import com.yumedev.seijakulist.data.repository.AnimeLocalRepository
import com.yumedev.seijakulist.domain.models.HeroAnimeItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Cache para el Hero Carousel de Manga usando AniList API
 *
 * Maneja la carga de items para el carousel principal del Home (tab Manga)
 * usando datos de AniList GraphQL API.
 */
@Singleton
class HeroCarouselMangaCacheAniList @Inject constructor(
    private val aniListRepository: AnimeAniListRepository,
    private val localRepository: AnimeLocalRepository,
    private val requestThrottler: RequestThrottler
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _cards = MutableStateFlow<List<HeroAnimeItem>?>(null)
    val cards: StateFlow<List<HeroAnimeItem>?> = _cards.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun preload() {
        if (_cards.value != null || _isLoading.value) return
        loadCards()
    }

    fun retry() {
        // Resetear estado y volver a cargar
        _cards.value = null
        loadCards()
    }

    private fun loadCards() {
        if (_isLoading.value) return
        scope.launch {
            _isLoading.value = true
            try {
                val localAnimes = localRepository.getAllAnimes().first()
                // Buscar manga guardados (typeAnime == "Manga", "Manhwa", "Manhua", "One-shot", "Novel")
                val mangaTypes = listOf("Manga", "Manhwa", "Manhua", "One-shot", "Novel", "Light Novel")
                val readingMangaId = localAnimes
                    .firstOrNull { it.typeAnime in mangaTypes && it.statusUser == "Leyendo" }?.malId
                    ?: localAnimes.firstOrNull { it.typeAnime in mangaTypes && it.statusUser == "Completado" }?.malId

                // Orden de carga: PARA VOS → TENDENCIA → EN PUBLICACIÓN → POPULAR → CLÁSICO
                val results = mutableListOf<HeroAnimeItem>()

                // 1. Recomendación basada en lo que está leyendo
                if (readingMangaId != null) {
                    requestThrottler.throttle {
                        aniListRepository.getRecommendationForManga(readingMangaId)
                    }?.let { results.add(it) }
                }

                // 2. Manga en tendencia
                requestThrottler.throttle {
                    aniListRepository.getTrendingMangaHeroItem()
                }?.let { results.add(it) }

                // 3. Manga en publicación
                requestThrottler.throttle {
                    aniListRepository.getPublishingMangaHeroItem()
                }?.let { results.add(it) }

                // 4. Manga popular
                requestThrottler.throttle {
                    aniListRepository.getPopularMangaHeroItem()
                }?.let { results.add(it) }

                // 5. Manga clásico (top rated)
                requestThrottler.throttle {
                    aniListRepository.getTopClassicManga()
                }?.let { results.add(it) }

                // Asegurarse de que siempre tengamos al menos una card
                if (results.isNotEmpty()) {
                    _cards.value = results
                } else {
                    // Si todas las peticiones fallaron, intentar al menos una como fallback
                    requestThrottler.throttle {
                        aniListRepository.getTopClassicManga()
                    }?.let {
                        _cards.value = listOf(it)
                    }
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
}

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
 * Cache para el Hero Carousel usando AniList API
 *
 * Maneja la carga de items para el carousel principal del Home
 * usando datos de AniList GraphQL API.
 */
@Singleton
class HeroCarouselCacheAniList @Inject constructor(
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
                val watchingId = localAnimes.firstOrNull { it.statusUser == "Viendo" }?.malId
                    ?: localAnimes.firstOrNull { it.statusUser == "Completado" }?.malId

                // Orden de carga: PARA VOS → TENDENCIA → AL AIRE → PRÓXIMAMENTE → CLÁSICO
                val results = mutableListOf<HeroAnimeItem>()

                // 1. Recomendación basada en lo que está viendo
                if (watchingId != null) {
                    requestThrottler.throttle {
                        aniListRepository.getRecommendationForAnime(watchingId)
                    }?.let { results.add(it) }
                }

                // 2. Anime en tendencia
                requestThrottler.throttle {
                    aniListRepository.getTrendingHeroItem()
                }?.let { results.add(it) }

                // 3. Anime actualmente al aire
                requestThrottler.throttle {
                    aniListRepository.getCurrentlyAiringHeroItem()
                }?.let { results.add(it) }

                // 4. Anime próximamente
                requestThrottler.throttle {
                    aniListRepository.getUpcomingHeroItem()
                }?.let { results.add(it) }

                // 5. Anime clásico (top rated)
                requestThrottler.throttle {
                    aniListRepository.getTopClassicAnime()
                }?.let { results.add(it) }

                // Asegurarse de que siempre tengamos al menos una card
                if (results.isNotEmpty()) {
                    _cards.value = results
                } else {
                    // Si todas las peticiones fallaron, intentar al menos una como fallback
                    requestThrottler.throttle {
                        aniListRepository.getTopClassicAnime()
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

package com.yumedev.seijakulist.ui.screens.home

import com.yumedev.seijakulist.common.RequestThrottler
import com.yumedev.seijakulist.data.repository.AnimeLocalRepository
import com.yumedev.seijakulist.data.repository.AnimeRepository
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

@Singleton
class HeroCarouselCache @Inject constructor(
    private val animeRepository: AnimeRepository,
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

                // Orden de carga: PARA VOS → PROMO → NUEVOS EPISODIOS → PRÓXIMAMENTE → CLÁSICO
                // Usando RequestThrottler para respetar el rate limit
                val results = mutableListOf<HeroAnimeItem>()

                // Cargar cada petición de forma secuencial usando el throttler
                if (watchingId != null) {
                    requestThrottler.throttle {
                        animeRepository.getRecommendationForAnime(watchingId)
                    }?.let { results.add(it) }
                }

                requestThrottler.throttle {
                    animeRepository.getWatchRecentPromos()
                }?.let { results.add(it) }

                requestThrottler.throttle {
                    animeRepository.getWatchRecentEpisodes()
                }?.let { results.add(it) }

                requestThrottler.throttle {
                    animeRepository.getUpcomingHeroItem()
                }?.let { results.add(it) }

                requestThrottler.throttle {
                    animeRepository.getTopClassicAnime()
                }?.let { results.add(it) }

                // Asegurarse de que siempre tengamos al menos una card
                if (results.isNotEmpty()) {
                    _cards.value = results
                } else {
                    // Si todas las peticiones fallaron, intentar al menos una como fallback
                    requestThrottler.throttle {
                        animeRepository.getTopClassicAnime()
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
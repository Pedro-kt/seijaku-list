package com.yumedev.seijakulist.ui.screens.home

import com.yumedev.seijakulist.data.repository.AnimeLocalRepository
import com.yumedev.seijakulist.data.repository.AnimeRepository
import com.yumedev.seijakulist.domain.models.HeroAnimeItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

private const val REQUEST_DELAY_MS = 400L // 3 req/seg de Jikan → 400ms de margen

@Singleton
class HeroCarouselCache @Inject constructor(
    private val animeRepository: AnimeRepository,
    private val localRepository: AnimeLocalRepository
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _cards = MutableStateFlow<List<HeroAnimeItem>?>(null)
    val cards: StateFlow<List<HeroAnimeItem>?> = _cards.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun preload() {
        if (_cards.value != null || _isLoading.value) return
        scope.launch {
            _isLoading.value = true
            try {
                val localAnimes = localRepository.getAllAnimes().first()
                val watchingId = localAnimes.firstOrNull { it.statusUser == "Viendo" }?.malId
                    ?: localAnimes.firstOrNull { it.statusUser == "Completado" }?.malId

                // Orden de carga: PARA VOS → PROMO → NUEVOS EPISODIOS → PRÓXIMAMENTE → CLÁSICO
                // Secuencial con delay para respetar el rate limit de Jikan (3 req/seg)
                fun fetch(block: suspend () -> HeroAnimeItem?) = block

                val loaders = buildList {
                    if (watchingId != null) add(fetch { animeRepository.getRecommendationForAnime(watchingId) })
                    add(fetch { animeRepository.getWatchRecentPromos() })
                    add(fetch { animeRepository.getWatchRecentEpisodes() })
                    add(fetch { animeRepository.getUpcomingHeroItem() })
                    add(fetch { animeRepository.getTopClassicAnime() })
                }

                val results = mutableListOf<HeroAnimeItem>()
                loaders.forEachIndexed { index, loader ->
                    if (index > 0) delay(REQUEST_DELAY_MS)
                    runCatching { loader() }.getOrNull()?.let { results.add(it) }
                }

                if (results.isNotEmpty()) _cards.value = results
            } finally {
                _isLoading.value = false
            }
        }
    }
}
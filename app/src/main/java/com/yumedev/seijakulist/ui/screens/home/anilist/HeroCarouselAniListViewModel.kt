package com.yumedev.seijakulist.ui.screens.home.anilist

import androidx.lifecycle.ViewModel
import com.yumedev.seijakulist.domain.models.HeroAnimeItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * ViewModel para el Hero Carousel usando AniList API
 *
 * Muestra un carousel de anime destacados en la parte superior del Home.
 */
@HiltViewModel
class HeroCarouselAniListViewModel @Inject constructor(
    private val cache: HeroCarouselCacheAniList
) : ViewModel() {

    val cards: StateFlow<List<HeroAnimeItem>?> = cache.cards
    val isLoading: StateFlow<Boolean> = cache.isLoading

    init {
        // Si el usuario llega al home sin haber pasado por el splash (edge case), disparamos el preload
        cache.preload()
    }

    fun retry() {
        cache.retry()
    }
}

package com.yumedev.seijakulist.ui.screens.home

import androidx.lifecycle.ViewModel
import com.yumedev.seijakulist.domain.models.HeroAnimeItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HeroCarouselViewModel @Inject constructor(
    private val cache: HeroCarouselCache
) : ViewModel() {

    val cards: StateFlow<List<HeroAnimeItem>?> = cache.cards
    val isLoading: StateFlow<Boolean> = cache.isLoading

    init {
        // Si el usuario llega al home sin haber pasado por el splash (edge case), disparamos el preload
        cache.preload()
    }
}
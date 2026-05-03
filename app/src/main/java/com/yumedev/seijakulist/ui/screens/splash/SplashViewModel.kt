package com.yumedev.seijakulist.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulist.ui.screens.home.HeroCarouselCache
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val heroCarouselCache: HeroCarouselCache
) : ViewModel() {

    // true cuando la carga terminó (con éxito o con error parcial)
    val isReady = combine(
        heroCarouselCache.cards,
        heroCarouselCache.isLoading
    ) { cards, loading ->
        // Listo si terminó de cargar (isLoading false), haya o no datos
        !loading && cards != null
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        heroCarouselCache.preload()
    }
}
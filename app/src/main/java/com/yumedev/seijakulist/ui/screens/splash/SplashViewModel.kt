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
    val isReady = heroCarouselCache.isLoading
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)
        .combine(heroCarouselCache.cards) { loading, cards ->
            // Listo si terminó de cargar, haya o no datos (para evitar bloqueos)
            !loading
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        heroCarouselCache.preload()
    }
}
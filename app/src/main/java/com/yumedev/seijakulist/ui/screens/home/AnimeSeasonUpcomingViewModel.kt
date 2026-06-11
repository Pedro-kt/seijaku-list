package com.yumedev.seijakulist.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulist.common.RequestThrottler
import com.yumedev.seijakulist.domain.models.Anime
import com.yumedev.seijakulist.domain.usecase.GetAnimeSeasonUpcomingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class AnimeSeasonUpcomingViewModel @Inject constructor(
    private val getAnimeSeasonUpcomingUseCase: GetAnimeSeasonUpcomingUseCase,
    private val cache: AnimeSeasonUpcomingCache,
    private val requestThrottler: RequestThrottler
) : ViewModel() {

    private val _animeList = MutableStateFlow<List<Anime>>(emptyList())
    val animeList: StateFlow<List<Anime>> = _animeList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Solo es error crítico si no tenemos cache Y la petición falló
    val isError: StateFlow<Boolean> = _errorMessage.map { error ->
        error != null && _animeList.value.isEmpty()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    init {
        val cached = cache.animeList
        if (cached != null) {
            _animeList.value = cached
            // Si hay cache, cargar en background sin bloquear la UI
            AnimesSeasonUpcoming(silent = true)
        } else {
            // Delay de 1400ms para evitar saturar el rate limit (3ª petición)
            viewModelScope.launch {
                delay(1400)
                AnimesSeasonUpcoming()
            }
        }
    }

    fun AnimesSeasonUpcoming(silent: Boolean = false) {
        viewModelScope.launch {
            if (!silent) {
                _errorMessage.value = null
                _isLoading.value = true
            }

            val result = requestThrottler.throttle {
                getAnimeSeasonUpcomingUseCase()
            }

            if (result != null) {
                val filtered = result.distinctBy { it.malId }
                cache.animeList = filtered
                _animeList.value = filtered
                _errorMessage.value = null
            } else {
                // Solo marcamos error si no tenemos cache previo
                if (_animeList.value.isEmpty()) {
                    _errorMessage.value = "Error al buscar animes"
                }
            }

            if (!silent) {
                _isLoading.value = false
            }
        }
    }

}
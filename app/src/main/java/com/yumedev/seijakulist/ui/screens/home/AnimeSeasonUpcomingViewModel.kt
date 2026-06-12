package com.yumedev.seijakulist.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulist.common.RequestThrottler
import com.yumedev.seijakulist.domain.models.Anime
import com.yumedev.seijakulist.domain.usecase.GetAnimeSeasonUpcomingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import javax.inject.Inject

private const val TAG = "SeasonUpcomingVM"

@HiltViewModel
class AnimeSeasonUpcomingViewModel @Inject constructor(
    private val getAnimeSeasonUpcomingUseCase: GetAnimeSeasonUpcomingUseCase,
    private val cache: AnimeSeasonUpcomingCache,
    private val requestThrottler: RequestThrottler
) : ViewModel() {

    // Inicializar con caché si existe, sino Initial
    private val _uiState = MutableStateFlow<HomeUiState<List<Anime>>>(
        cache.animeList?.takeIf { it.isNotEmpty() }?.let { HomeUiState.Success(it) } ?: HomeUiState.Initial
    )
    val uiState: StateFlow<HomeUiState<List<Anime>>> = _uiState.asStateFlow()

    // Mantener compatibilidad con código existente
    val animeList: StateFlow<List<Anime>> = MutableStateFlow<List<Anime>>(emptyList()).apply {
        viewModelScope.launch {
            _uiState.collect { state ->
                value = state.getDataOrNull() ?: emptyList()
            }
        }
    }

    val isLoading: StateFlow<Boolean> = MutableStateFlow(false).apply {
        viewModelScope.launch {
            _uiState.collect { state ->
                value = state.shouldShowSkeleton()
            }
        }
    }

    val isError: StateFlow<Boolean> = MutableStateFlow(false).apply {
        viewModelScope.launch {
            _uiState.collect { state ->
                value = state is HomeUiState.Error
            }
        }
    }

    init {
        val currentState = _uiState.value
        Log.d(TAG, "init() - Initial state: ${currentState::class.simpleName}, Size: ${currentState.getDataOrNull()?.size ?: 0}")

        if (currentState is HomeUiState.Success) {
            // Ya tenemos datos desde la inicialización → actualizar en background con delay
            Log.d(TAG, "init() - Has cached data, updating in background after delay")
            viewModelScope.launch {
                Log.d(TAG, "init() - Waiting 1400ms before background update...")
                delay(1400) // Evitar saturar el rate limit
                Log.d(TAG, "init() - Starting background update")
                AnimesSeasonUpcoming()
            }
        } else {
            // Sin caché → cargar desde cero con delay
            Log.d(TAG, "init() - No cache, will load after 1400ms delay")
            viewModelScope.launch {
                delay(1400) // Evitar saturar el rate limit (3ª petición)
                Log.d(TAG, "init() - Starting initial load")
                AnimesSeasonUpcoming()
            }
        }
    }

    fun AnimesSeasonUpcoming(silent: Boolean = false) {
        Log.d(TAG, "AnimesSeasonUpcoming() called - silent: $silent")
        viewModelScope.launch {
            // Determinar el próximo estado según el estado actual
            val currentState = _uiState.value
            Log.d(TAG, "AnimesSeasonUpcoming() - Current state: ${currentState::class.simpleName}")

            _uiState.value = when (currentState) {
                is HomeUiState.Success -> {
                    // Ya tenemos datos → modo actualización
                    Log.d(TAG, "AnimesSeasonUpcoming() - Transitioning to Refreshing with ${currentState.data.size} items")
                    HomeUiState.Refreshing(currentState.data)
                }
                is HomeUiState.ErrorWithCache -> {
                    // Tenemos datos en caché con error → actualizar
                    Log.d(TAG, "AnimesSeasonUpcoming() - Transitioning to Refreshing from ErrorWithCache")
                    HomeUiState.Refreshing(currentState.data)
                }
                else -> {
                    // Sin datos → carga inicial
                    Log.d(TAG, "AnimesSeasonUpcoming() - Transitioning to Loading (initial load)")
                    HomeUiState.Loading
                }
            }

            Log.d(TAG, "AnimesSeasonUpcoming() - Making API request...")
            val result = requestThrottler.throttle {
                getAnimeSeasonUpcomingUseCase()
            }

            _uiState.value = if (result != null) {
                val filtered = result.distinctBy { it.malId }
                cache.animeList = filtered
                Log.d(TAG, "AnimesSeasonUpcoming() - Success! Got ${filtered.size} items")
                HomeUiState.Success(filtered)
            } else {
                // Error - verificar si tenemos datos previos
                val currentData = _uiState.value.getDataOrNull()
                if (currentData != null) {
                    Log.w(TAG, "AnimesSeasonUpcoming() - Error but keeping ${currentData.size} cached items")
                    HomeUiState.ErrorWithCache(currentData, "Error al actualizar")
                } else {
                    Log.e(TAG, "AnimesSeasonUpcoming() - Error with no cached data")
                    HomeUiState.Error("Error al buscar animes")
                }
            }

            Log.d(TAG, "AnimesSeasonUpcoming() - Final state: ${_uiState.value::class.simpleName}")
        }
    }
}

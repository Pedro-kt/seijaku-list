package com.yumedev.seijakulist.ui.screens.home.anilist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulist.common.RequestThrottler
import com.yumedev.seijakulist.domain.models.Anime
import com.yumedev.seijakulist.domain.usecase.anilist.GetTrendingMangaAniListUseCase
import com.yumedev.seijakulist.ui.screens.home.HomeUiState
import com.yumedev.seijakulist.ui.screens.home.getDataOrNull
import com.yumedev.seijakulist.ui.screens.home.shouldShowSkeleton
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "TrendingMangaAniListVM"

/**
 * ViewModel para mostrar manga en tendencia usando AniList API
 *
 * Obtiene manga que están actualmente en tendencia.
 */
@HiltViewModel
class TrendingMangaAniListViewModel @Inject constructor(
    private val getTrendingMangaAniListUseCase: GetTrendingMangaAniListUseCase,
    private val cache: TrendingMangaCacheAniList,
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
            // Ya tenemos datos desde la inicialización → actualizar en background
            Log.d(TAG, "init() - Has cached data, updating in background")
            loadTrendingManga()
        } else {
            // Sin caché → cargar desde cero
            Log.d(TAG, "init() - No cache, loading from scratch")
            loadTrendingManga()
        }
    }

    fun loadTrendingManga(silent: Boolean = false) {
        Log.d(TAG, "loadTrendingManga() called - silent: $silent")
        viewModelScope.launch {
            // Determinar el próximo estado según el estado actual
            val currentState = _uiState.value
            Log.d(TAG, "loadTrendingManga() - Current state: ${currentState::class.simpleName}")

            _uiState.value = when (currentState) {
                is HomeUiState.Success -> {
                    // Ya tenemos datos → modo actualización
                    Log.d(TAG, "loadTrendingManga() - Transitioning to Refreshing with ${currentState.data.size} items")
                    HomeUiState.Refreshing(currentState.data)
                }
                is HomeUiState.ErrorWithCache -> {
                    // Tenemos datos en caché con error → actualizar
                    Log.d(TAG, "loadTrendingManga() - Transitioning to Refreshing from ErrorWithCache")
                    HomeUiState.Refreshing(currentState.data)
                }
                else -> {
                    // Sin datos → carga inicial
                    Log.d(TAG, "loadTrendingManga() - Transitioning to Loading (initial load)")
                    HomeUiState.Loading
                }
            }

            Log.d(TAG, "loadTrendingManga() - Making API request...")
            val result = requestThrottler.throttle {
                getTrendingMangaAniListUseCase()
            }

            _uiState.value = if (result != null) {
                // Convertir AnimeCard a Anime
                val animeList = result.map { card ->
                    Anime(
                        malId = card.malId,
                        title = card.title,
                        image = card.images,
                        score = card.score
                    )
                }
                val filtered = animeList.distinctBy { it.malId }
                cache.animeList = filtered
                Log.d(TAG, "loadTrendingManga() - Success! Got ${filtered.size} items")
                HomeUiState.Success(filtered)
            } else {
                // Error - verificar si tenemos datos previos
                val currentData = _uiState.value.getDataOrNull()
                if (currentData != null) {
                    Log.w(TAG, "loadTrendingManga() - Error but keeping ${currentData.size} cached items")
                    HomeUiState.ErrorWithCache(currentData, "Error al actualizar")
                } else {
                    Log.e(TAG, "loadTrendingManga() - Error with no cached data")
                    HomeUiState.Error("Error al cargar los datos")
                }
            }

            Log.d(TAG, "loadTrendingManga() - Final state: ${_uiState.value::class.simpleName}")
        }
    }
}

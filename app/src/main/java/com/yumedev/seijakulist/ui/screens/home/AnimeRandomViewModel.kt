package com.yumedev.seijakulist.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulist.domain.models.AnimeCard
import com.yumedev.seijakulist.domain.usecase.GetAnimeRandomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Cache con scope de aplicación para el anime random.
 * Persiste mientras el proceso de la app esté vivo, así el ViewModel
 * puede recrearse (por navegación) sin volver a hacer el request.
 */
@Singleton
class AnimeRandomCache @Inject constructor() {
    var anime: AnimeCard? = null
}

@HiltViewModel
class AnimeRandomViewModel @Inject constructor(
    private val getAnimeRandomUseCase: GetAnimeRandomUseCase,
    private val cache: AnimeRandomCache
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnimeRandomUiState())
    val uiState: StateFlow<AnimeRandomUiState> = _uiState.asStateFlow()

    init {
        val cached = cache.anime
        if (cached != null) {
            // Ya fue cargado en esta sesión, usar el dato del cache sin request
            _uiState.update {
                it.copy(anime = cached, isLoading = false, isDataLoaded = true)
            }
        } else {
            loadRandomAnime()
        }
    }

    fun loadRandomAnime() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, isDataLoaded = false) }

            try {
                delay(1500)
                var result = getAnimeRandomUseCase()
                var attempts = 0
                val maxAttempts = 20

                while (result.score < 6.0f && attempts < maxAttempts) {
                    delay(1500)
                    result = getAnimeRandomUseCase()
                    attempts++
                }

                cache.anime = result

                _uiState.update {
                    it.copy(
                        anime = result,
                        isDataLoaded = true,
                        isLoading = false,
                        errorMessage = null
                    )
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error al buscar animes: ${e.localizedMessage ?: "Error desconocido"}",
                    )
                }
            }
        }
    }
}

data class AnimeRandomUiState(
    val anime: AnimeCard? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val isDataLoaded: Boolean = false,
)
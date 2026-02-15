package com.yumedev.seijakulist.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulist.domain.models.Anime
import com.yumedev.seijakulist.domain.models.AnimeCard
import com.yumedev.seijakulist.domain.models.CharacterDetail
import com.yumedev.seijakulist.domain.usecase.GetAnimeRandomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeRandomViewModel @Inject constructor(
    private val getAnimeRandomUseCase: GetAnimeRandomUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnimeRandomUiState())
    val uiState: StateFlow<AnimeRandomUiState> = _uiState.asStateFlow()

    init {
        loadRandomAnime()
    }

    fun loadRandomAnime() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, isDataLoaded = false) }

            try {
                // Buscar un anime random con score >= 6.0
                delay(1500)
                var result = getAnimeRandomUseCase()
                var attempts = 0
                val maxAttempts = 20 // Límite de intentos para evitar loop infinito

                // Repetir hasta encontrar un anime con score >= 6.0
                while (result.score < 6.0f && attempts < maxAttempts) {
                    delay(1500)
                    result = getAnimeRandomUseCase()
                    attempts++
                }

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
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isDataLoaded: Boolean = false,
)
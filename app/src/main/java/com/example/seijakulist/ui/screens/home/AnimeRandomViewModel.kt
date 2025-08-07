package com.example.seijakulist.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seijakulist.domain.models.Anime
import com.example.seijakulist.domain.usecase.GetAnimeDetailSeasonNowUseCase
import com.example.seijakulist.domain.usecase.GetAnimeRandomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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

    // 1. Usa una data class para representar el estado de la UI
    private val _uiState = MutableStateFlow(AnimeRandomUiState())
    val uiState: StateFlow<AnimeRandomUiState> = _uiState.asStateFlow()

    // 2. Llama a la función de carga en el bloque init
    init {
        loadRandomAnime()
    }

    fun loadRandomAnime() {
        viewModelScope.launch {
            // Actualiza el estado para indicar que la carga ha iniciado
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                // Llama al use case
                val result = getAnimeRandomUseCase()

                // Actualiza el estado con el anime cargado
                _uiState.update {
                    it.copy(
                        anime = result,
                        isLoading = false,
                        isDataLoaded = true
                    )
                }

            } catch (e: Exception) {
                // Actualiza el estado en caso de error
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error al buscar animes: ${e.localizedMessage ?: "Error desconocido"}"
                    )
                }
            }
        }
    }
}

// Data class para representar el estado de la UI
data class AnimeRandomUiState(
    val anime: Anime? = null, // El anime es nulo hasta que se cargue
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isDataLoaded: Boolean = false
)
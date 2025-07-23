package com.example.seijakulist.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seijakulist.domain.models.Anime
import com.example.seijakulist.domain.usecase.GetAnimeSeasonUpcomingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeSeasonUpcomingViewModel @Inject constructor(

    private val getAnimeSeasonUpcomingUseCase: GetAnimeSeasonUpcomingUseCase

) : ViewModel() {

    private val _animeList = MutableStateFlow<List<Anime>>(emptyList())
    val animeList: StateFlow<List<Anime>> = _animeList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private var isDataLoaded = false

    // ✨ Inicializa la carga cuando el ViewModel se crea por primera vez
    init {
        // Solo carga los animes si no se han cargado antes
        // Esto evita múltiples llamadas si el ViewModel sobrevive a la recomposición
        // o si es la primera vez que se crea.
        if (!isDataLoaded) {
            AnimesSeasonUpcoming()
        }
    }

    fun AnimesSeasonUpcoming() {

        viewModelScope.launch {

            _errorMessage.value = null
            _isLoading.value = true

            try {

                val results = getAnimeSeasonUpcomingUseCase()
                _animeList.value = results
                isDataLoaded = true

            } catch (e: Exception) {

                _errorMessage.value = "Error al buscar animes: ${e.localizedMessage ?: "Error desconocido"}"
                _animeList.value = emptyList()

            } finally {

                _isLoading.value = false

            }
        }
    }

}
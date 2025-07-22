package com.example.seijakulist.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seijakulist.domain.models.Anime
import com.example.seijakulist.domain.models.AnimeDetailSeasonNow
import com.example.seijakulist.domain.usecase.GetAnimeDetailSeasonNowUseCase
import com.example.seijakulist.domain.usecase.GetAnimeSearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeSeasonNowViewModel @Inject constructor(

    private val getAnimeDetailSeasonNowUseCase: GetAnimeDetailSeasonNowUseCase

) : ViewModel() {

    private val _animeList = MutableStateFlow<List<AnimeDetailSeasonNow>>(emptyList())
    val animeList: StateFlow<List<AnimeDetailSeasonNow>> = _animeList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun AnimesSeasonNow() {

        viewModelScope.launch {

            _errorMessage.value = null
            _isLoading.value = true

            try {

                val results = getAnimeDetailSeasonNowUseCase()
                _animeList.value = results

            } catch (e: Exception) {

                _errorMessage.value = "Error al buscar animes: ${e.localizedMessage ?: "Error desconocido"}"
                _animeList.value = emptyList()

            } finally {

                _isLoading.value = false

            }
        }
    }
}
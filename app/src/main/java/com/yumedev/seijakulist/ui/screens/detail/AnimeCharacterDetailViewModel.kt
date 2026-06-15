package com.yumedev.seijakulist.ui.screens.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulist.common.RequestThrottler
import com.yumedev.seijakulist.domain.models.AnimeCharactersDetail
import com.yumedev.seijakulist.domain.usecase.anilist.GetAnimeCharactersAniListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para obtener personajes de un anime usando AniList API
 *
 * @param getAnimeCharactersAniListUseCase Caso de uso para obtener personajes
 * @param requestThrottler Throttler para limitar requests a la API
 */
@HiltViewModel
class AnimeCharacterDetailViewModel @Inject constructor(
    private val getAnimeCharactersAniListUseCase: GetAnimeCharactersAniListUseCase,
    private val requestThrottler: RequestThrottler
) : ViewModel() {

    // State para los personajes del anime
    private val _characters = MutableStateFlow<List<AnimeCharactersDetail>>(emptyList())
    val characters: StateFlow<List<AnimeCharactersDetail>> = _characters.asStateFlow()

    // State para loading
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // State para mensajes de error
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    /**
     * Carga los personajes de un anime desde AniList
     *
     * @param animeId ID del anime (MyAnimeList ID por defecto)
     */
    fun loadAnimeCharacters(animeId: Int) {
        if (_characters.value.isNotEmpty()) return // Ya cargados

        _errorMessage.value = null
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val charactersList = requestThrottler.throttle {
                    getAnimeCharactersAniListUseCase(malId = animeId)
                }

                _characters.value = charactersList?: emptyList()
            } catch (e: Exception) {
                Log.e(TAG, "Error al cargar personajes: ${e.localizedMessage ?: "Error desconocido"}")
                _errorMessage.value = "No se pudieron cargar los personajes del anime"
                _characters.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Limpia el mensaje de error
     */
    fun clearError() {
        _errorMessage.value = null
    }

    /**
     * Reinicia el estado de personajes
     */
    fun resetCharacters() {
        _characters.value = emptyList()
        _isLoading.value = false
        _errorMessage.value = null
    }

    companion object {
        private const val TAG = "AnimeCharacterDetailVM"
    }
}

package com.yumedev.seijakulist.ui.screens.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulist.common.RequestThrottler
import com.yumedev.seijakulist.domain.models.AnimeCharactersDetail
import com.yumedev.seijakulist.domain.usecase.anilist.GetMangaCharactersAniListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para gestionar los personajes de un manga
 *
 * @param savedStateHandle Manejo del estado guardado
 * @param getMangaCharactersAniListUseCase Caso de uso para obtener personajes del manga
 * @param requestThrottler Throttler para limitar requests a la API
 */
@HiltViewModel
class MangaCharacterDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMangaCharactersAniListUseCase: GetMangaCharactersAniListUseCase,
    private val requestThrottler: RequestThrottler
) : ViewModel() {

    // State para los personajes del manga
    private val _characters = MutableStateFlow<List<AnimeCharactersDetail>>(emptyList())
    val characters: StateFlow<List<AnimeCharactersDetail>> = _characters.asStateFlow()

    // State para loading
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // State para mensajes de error
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // ID del manga obtenido de los argumentos de navegación
    private val mangaId: Int = savedStateHandle["mangaId"] ?: 0

    init {
        Log.d(TAG, "MangaCharacterDetailViewModel init - mangaId: $mangaId")
        if (mangaId != 0) {
            loadCharacters(mangaId)
        } else {
            Log.e(TAG, "MangaId is 0! Cannot load characters")
            _errorMessage.value = "ID de manga inválido"
        }
    }

    /**
     * Carga los personajes del manga desde AniList
     *
     * @param mangaId ID del manga (MyAnimeList ID)
     */
    fun loadCharacters(mangaId: Int) {
        Log.d(TAG, "loadCharacters called with mangaId: $mangaId")
        _errorMessage.value = null
        _isLoading.value = true

        viewModelScope.launch {
            try {
                Log.d(TAG, "Fetching manga characters from AniList...")
                val charactersList = requestThrottler.throttle {
                    getMangaCharactersAniListUseCase(malId = mangaId)
                }

                Log.d(TAG, "Characters loaded successfully: ${charactersList?.size ?: 0} characters")
                _characters.value = charactersList ?: emptyList()
            } catch (e: Exception) {
                Log.e(TAG, "Error al cargar personajes: ${e.localizedMessage ?: "Error desconocido"}", e)
                _errorMessage.value = "Error al cargar los personajes"
                _characters.value = emptyList()
            } finally {
                _isLoading.value = false
                Log.d(TAG, "loadCharacters finished. isLoading: false")
            }
        }
    }

    /**
     * Limpia el mensaje de error
     */
    fun clearError() {
        _errorMessage.value = null
    }

    companion object {
        private const val TAG = "MangaCharacterVM"
    }
}

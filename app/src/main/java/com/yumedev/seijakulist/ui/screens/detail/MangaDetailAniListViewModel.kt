package com.yumedev.seijakulist.ui.screens.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulist.common.RequestThrottler
import com.yumedev.seijakulist.domain.models.MangaDetail
import com.yumedev.seijakulist.domain.usecase.anilist.GetMangaDetailAniListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de detalle de manga usando AniList API
 *
 * Proporciona funcionalidad básica para:
 * - Cargar detalles del manga desde AniList
 * - Verificar si el manga está en la lista local del usuario
 * - Agregar manga a la lista local del usuario (futuro)
 *
 * @param savedStateHandle Manejo del estado guardado
 * @param getMangaDetailAniListUseCase Caso de uso para obtener detalles del manga
 * @param requestThrottler Throttler para limitar requests a la API
 */
@HiltViewModel
class MangaDetailAniListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMangaDetailAniListUseCase: GetMangaDetailAniListUseCase,
    private val requestThrottler: RequestThrottler
) : ViewModel() {

    // State para el detalle del manga
    private val _mangaDetail = MutableStateFlow<MangaDetail?>(null)
    val mangaDetail: StateFlow<MangaDetail?> = _mangaDetail.asStateFlow()

    // State para loading
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // State para mensajes de error
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // ID del manga obtenido de los argumentos de navegación
    private val mangaId: Int = savedStateHandle["mangaId"] ?: 0

    init {
        Log.d(TAG, "MangaDetailAniListViewModel init - mangaId: $mangaId")
        if (mangaId != 0) {
            loadMangaDetail(mangaId)
        } else {
            Log.e(TAG, "MangaId is 0! Cannot load manga details")
            _errorMessage.value = "ID de manga inválido"
        }
    }

    /**
     * Carga los detalles del manga desde AniList
     *
     * @param mangaId ID del manga (MyAnimeList ID)
     */
    fun loadMangaDetail(mangaId: Int) {
        Log.d(TAG, "loadMangaDetail called with mangaId: $mangaId")
        _errorMessage.value = null
        _isLoading.value = true

        viewModelScope.launch {
            try {
                Log.d(TAG, "Fetching manga detail from AniList...")
                val detail = requestThrottler.throttle {
                    getMangaDetailAniListUseCase(malId = mangaId)
                }

                if (detail != null) {
                    Log.d(TAG, "Manga detail loaded successfully: ${detail.title}")
                    _mangaDetail.value = detail
                } else {
                    throw Exception("No se pudo obtener los detalles del manga")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error al cargar detalles del manga: ${e.localizedMessage ?: "Error desconocido"}", e)
                _errorMessage.value = "Algo salió mal al cargar los detalles del manga, por favor inténtalo de nuevo."
                _mangaDetail.value = null
            } finally {
                _isLoading.value = false
                Log.d(TAG, "loadMangaDetail finished. isLoading: false")
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
        private const val TAG = "MangaDetailVM"
    }
}

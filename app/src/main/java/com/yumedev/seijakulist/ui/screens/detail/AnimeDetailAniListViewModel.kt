package com.yumedev.seijakulist.ui.screens.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulist.common.RequestThrottler
import com.yumedev.seijakulist.data.local.entities.AnimeEntity
import com.yumedev.seijakulist.data.repository.AnimeLocalRepository
import com.yumedev.seijakulist.data.repository.FirestoreAnimeRepository
import com.yumedev.seijakulist.domain.models.AnimeDetail
import com.yumedev.seijakulist.domain.models.AnimeEntityDomain
import com.yumedev.seijakulist.domain.usecase.anilist.GetAnimeDetailAniListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de detalle de anime usando AniList API
 *
 * Proporciona funcionalidad básica para:
 * - Cargar detalles del anime desde AniList
 * - Verificar si el anime está en la lista local del usuario
 * - Agregar anime a la lista local del usuario
 * - Sincronización con Firestore
 *
 * @param savedStateHandle Manejo del estado guardado
 * @param getAnimeDetailAniListUseCase Caso de uso para obtener detalles del anime
 * @param animeLocalRepository Repositorio local para gestión de la lista del usuario
 * @param firestoreAnimeRepository Repositorio de Firestore para sincronización
 * @param requestThrottler Throttler para limitar requests a la API
 */
@HiltViewModel
class AnimeDetailAniListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getAnimeDetailAniListUseCase: GetAnimeDetailAniListUseCase,
    private val animeLocalRepository: AnimeLocalRepository,
    private val firestoreAnimeRepository: FirestoreAnimeRepository,
    private val requestThrottler: RequestThrottler
) : ViewModel() {

    // State para el detalle del anime
    private val _animeDetail = MutableStateFlow<AnimeDetail?>(null)
    val animeDetail: StateFlow<AnimeDetail?> = _animeDetail.asStateFlow()

    // State para loading
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // State para mensajes de error
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // State para verificar si el anime fue agregado
    private val _isAdded = MutableStateFlow(false)
    val isAdded: StateFlow<Boolean> = _isAdded.asStateFlow()

    // State para el anime existente en la lista local
    private val _existingAnime = MutableStateFlow<AnimeEntityDomain?>(null)
    val existingAnime: StateFlow<AnimeEntityDomain?> = _existingAnime.asStateFlow()

    // ID del anime obtenido de los argumentos de navegación
    private val animeId: Int = savedStateHandle["animeId"] ?: 0

    init {
        Log.d(TAG, "AnimeDetailAniListViewModel init - animeId: $animeId")
        if (animeId != 0) {
            loadAnimeDetail(animeId)
            loadExistingAnime(animeId)
        } else {
            Log.e(TAG, "AnimeId is 0! Cannot load anime details")
            _errorMessage.value = "ID de anime inválido"
        }
    }

    /**
     * Verifica si el anime existe en la lista local del usuario
     *
     * @param id ID del anime (MyAnimeList ID)
     */
    private fun loadExistingAnime(id: Int) {
        viewModelScope.launch {
            try {
                _existingAnime.value = animeLocalRepository.getAnimeById(id)
            } catch (e: Exception) {
                // El anime no está en la lista del usuario
                _existingAnime.value = null
            }
        }
    }

    /**
     * Carga los detalles del anime desde AniList
     *
     * @param animeId ID del anime (MyAnimeList ID)
     */
    fun loadAnimeDetail(animeId: Int) {
        Log.d(TAG, "loadAnimeDetail called with animeId: $animeId")
        _errorMessage.value = null
        _isLoading.value = true

        viewModelScope.launch {
            try {
                Log.d(TAG, "Fetching anime detail from AniList...")
                val detail = requestThrottler.throttle {
                    getAnimeDetailAniListUseCase(malId = animeId)
                }

                if (detail != null) {
                    Log.d(TAG, "Anime detail loaded successfully: ${detail.title}")
                    _animeDetail.value = detail
                } else {
                    throw Exception("No se pudo obtener los detalles del anime")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error al cargar detalles del anime: ${e.localizedMessage ?: "Error desconocido"}", e)
                _errorMessage.value = "Algo salió mal al cargar los detalles del anime, por favor inténtalo de nuevo."
                _animeDetail.value = null
            } finally {
                _isLoading.value = false
                Log.d(TAG, "loadAnimeDetail finished. isLoading: false")
            }
        }
    }

    /**
     * Agrega el anime a la lista local del usuario
     *
     * @param userScore Puntuación del usuario (0-10)
     * @param userStatus Estado del anime (Completado, Viendo, Pendiente, etc.)
     * @param userOpinion Opinión/nota del usuario
     * @param startDate Fecha de inicio (timestamp en milisegundos, opcional)
     * @param endDate Fecha de finalización (timestamp en milisegundos, opcional)
     * @param plannedPriority Prioridad del plan (solo si estado es "Planeado")
     * @param plannedNote Nota del plan (solo si estado es "Planeado")
     */
    fun addAnimeToList(
        userScore: Float,
        userStatus: String,
        userOpinion: String,
        startDate: Long? = null,
        endDate: Long? = null,
        plannedPriority: String? = null,
        plannedNote: String? = null
    ) {
        val current = _animeDetail.value ?: return

        viewModelScope.launch {
            try {
                // Convertir la lista de géneros a una cadena separada por comas
                val genresString = current.genres
                    .mapNotNull { it?.name }
                    .joinToString(", ")

                // Convertir la lista de studios a una cadena separada por comas
                val studiosString = current.studios
                    .mapNotNull { it?.nameStudio }
                    .joinToString(", ")

                val entity = AnimeEntity(
                    malId = current.malId,
                    title = current.title,
                    imageUrl = current.images,
                    userScore = userScore,
                    statusUser = userStatus,
                    userOpiniun = userOpinion,
                    totalEpisodes = current.episodes,
                    episodesWatched = if (userStatus == "Completado") current.episodes else 0,
                    rewatchCount = if (userStatus == "Completado") 1 else 0,
                    genres = genresString,
                    // Campos adicionales del detalle del anime
                    synopsis = current.synopsis,
                    titleEnglish = current.titleEnglish,
                    titleJapanese = current.titleJapanese,
                    studios = studiosString,
                    score = current.score,
                    scoreBy = current.scoreBy,
                    typeAnime = current.typeAnime,
                    duration = current.duration,
                    season = current.season,
                    year = current.year.toString(),
                    status = current.status,
                    aired = current.aired,
                    rank = current.rank,
                    rating = current.rating,
                    source = current.source,
                    // Fechas de seguimiento
                    startDate = startDate,
                    endDate = endDate,
                    // Prioridad del plan (solo si el estado es Planeado)
                    plannedPriority = if (userStatus == "Planeado") plannedPriority else null,
                    plannedNote = if (userStatus == "Planeado") plannedNote else null
                )

                // Insertar en la base de datos local
                animeLocalRepository.insertAnime(entity)
                _isAdded.value = true

                // Sincronizar con Firestore
                try {
                    firestoreAnimeRepository.syncAnimeToFirestore(entity)
                } catch (e: Exception) {
                    Log.e(TAG, "Error syncing to Firestore: ${e.message}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error al guardar anime: ${e.message}")
                _errorMessage.value = "Error al agregar el anime a tu lista"
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
     * Reinicia el estado de "agregado"
     */
    fun resetAddedState() {
        _isAdded.value = false
    }

    companion object {
        private const val TAG = "AnimeDetailAniListVM"
    }
}

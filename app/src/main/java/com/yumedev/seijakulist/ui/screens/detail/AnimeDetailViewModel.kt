package com.yumedev.seijakulist.ui.screens.detail

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableFloatState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulist.data.local.entities.AnimeEntity
import com.yumedev.seijakulist.data.repository.AnimeLocalRepository
import com.yumedev.seijakulist.data.repository.AnimeRepository
import com.yumedev.seijakulist.data.repository.FirestoreAnimeRepository
import com.yumedev.seijakulist.domain.models.AnimeDetail
import com.yumedev.seijakulist.domain.models.AnimeEpisode
import com.yumedev.seijakulist.domain.models.AnimeEpisodeDetail
import com.yumedev.seijakulist.domain.models.AnimePicture
import com.yumedev.seijakulist.domain.models.AnimeVideos
import com.yumedev.seijakulist.domain.usecase.GetAnimeDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AnimeDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getAnimeDetailUseCase: GetAnimeDetailUseCase,
    private val animeLocalRepository: AnimeLocalRepository,
    private val animeRepository: AnimeRepository,
    private val firestoreAnimeRepository: FirestoreAnimeRepository
) : ViewModel() {

    private val _animeDetail: MutableStateFlow<AnimeDetail?> = MutableStateFlow(null)
    val animeDetail: StateFlow<AnimeDetail?> = _animeDetail.asStateFlow()

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isAdded = MutableStateFlow(false)
    val isAdded: StateFlow<Boolean> = _isAdded.asStateFlow()

    private val _animePictures = MutableStateFlow<List<AnimePicture>>(emptyList())
    val animePictures: StateFlow<List<AnimePicture>> = _animePictures.asStateFlow()

    private val _isPicturesLoading = MutableStateFlow(false)
    val isPicturesLoading: StateFlow<Boolean> = _isPicturesLoading.asStateFlow()

    private val _animeVideos = MutableStateFlow<AnimeVideos?>(null)
    val animeVideos: StateFlow<AnimeVideos?> = _animeVideos.asStateFlow()

    private val _isVideosLoading = MutableStateFlow(false)
    val isVideosLoading: StateFlow<Boolean> = _isVideosLoading.asStateFlow()

    private val _animeEpisodes = MutableStateFlow<List<AnimeEpisode>>(emptyList())
    val animeEpisodes: StateFlow<List<AnimeEpisode>> = _animeEpisodes.asStateFlow()

    private val _isEpisodesLoading = MutableStateFlow(false)
    val isEpisodesLoading: StateFlow<Boolean> = _isEpisodesLoading.asStateFlow()

    private val _hasMoreEpisodes = MutableStateFlow(false)
    val hasMoreEpisodes: StateFlow<Boolean> = _hasMoreEpisodes.asStateFlow()

    private val _selectedEpisodeDetail = MutableStateFlow<AnimeEpisodeDetail?>(null)
    val selectedEpisodeDetail: StateFlow<AnimeEpisodeDetail?> = _selectedEpisodeDetail.asStateFlow()

    private val _isEpisodeDetailLoading = MutableStateFlow(false)
    val isEpisodeDetailLoading: StateFlow<Boolean> = _isEpisodeDetailLoading.asStateFlow()

    private var currentEpisodesPage = 1

    private var isDataLoaded = false
    private val animeId: Int = savedStateHandle["animeId"] ?: 0

    init {
        if (!isDataLoaded) {
            loadAnimeDetail(animeId)
            isDataLoaded = true
        }
    }

    fun loadAnimeDetail(animeId: Int) {
        _errorMessage.value = null
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val detail = getAnimeDetailUseCase(animeId)
                _animeDetail.value = detail
            } catch (e: Exception) {
                Log.e("AnimeDetailVM", "Error al cargar detalles del anime: ${e.localizedMessage ?: "Error desconocido"}")
                _errorMessage.value = "Ups! Algo salió mal al cargar los detalles del anime, por favor inténtalo de nuevo, gracias por tu paciencia."
                _animeDetail.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadAnimePictures(animeId: Int) {
        if (_animePictures.value.isNotEmpty()) return // Ya cargadas

        _isPicturesLoading.value = true
        viewModelScope.launch {
            try {
                val pictures = animeRepository.getAnimePicturesById(animeId)
                _animePictures.value = pictures
            } catch (e: Exception) {
                Log.e("AnimeDetailVM", "Error al cargar imágenes: ${e.localizedMessage}")
                _animePictures.value = emptyList()
            } finally {
                _isPicturesLoading.value = false
            }
        }
    }

    fun loadAnimeVideos(animeId: Int) {
        if (_animeVideos.value != null) return // Ya cargados

        _isVideosLoading.value = true
        viewModelScope.launch {
            try {
                val videos = animeRepository.getAnimeVideosById(animeId)
                _animeVideos.value = videos
            } catch (e: Exception) {
                Log.e("AnimeDetailVM", "Error al cargar videos: ${e.localizedMessage}")
                _animeVideos.value = null
            } finally {
                _isVideosLoading.value = false
            }
        }
    }

    fun loadAnimeEpisodes(animeId: Int, loadMore: Boolean = false) {
        if (!loadMore && _animeEpisodes.value.isNotEmpty()) return // Ya cargados

        if (loadMore) {
            currentEpisodesPage++
        } else {
            currentEpisodesPage = 1
        }

        _isEpisodesLoading.value = true
        viewModelScope.launch {
            try {
                val result = animeRepository.getAnimeEpisodesById(animeId, currentEpisodesPage)
                if (loadMore) {
                    _animeEpisodes.value = _animeEpisodes.value + result.episodes
                } else {
                    _animeEpisodes.value = result.episodes
                }
                _hasMoreEpisodes.value = result.pagination?.hasNextPage ?: false
            } catch (e: Exception) {
                Log.e("AnimeDetailVM", "Error al cargar episodios: ${e.localizedMessage}")
                if (!loadMore) {
                    _animeEpisodes.value = emptyList()
                }
                _hasMoreEpisodes.value = false
            } finally {
                _isEpisodesLoading.value = false
            }
        }
    }

    fun loadEpisodeDetail(animeId: Int, episodeId: Int) {
        _isEpisodeDetailLoading.value = true
        viewModelScope.launch {
            try {
                val detail = animeRepository.getAnimeEpisodeDetailById(animeId, episodeId)
                _selectedEpisodeDetail.value = detail
            } catch (e: Exception) {
                Log.e("AnimeDetailVM", "Error al cargar detalle del episodio: ${e.localizedMessage}")
                _selectedEpisodeDetail.value = null
            } finally {
                _isEpisodeDetailLoading.value = false
            }
        }
    }

    fun clearEpisodeDetail() {
        _selectedEpisodeDetail.value = null
    }

    fun addAnimeToList(
        userScore: Float,
        userStatus: String,
        userOpinion: String,
        startDate: Long? = null,
        endDate: Long? = null
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
                    endDate = endDate
                )
                animeLocalRepository.insertAnime(entity)
                _isAdded.value = true
                try {
                    firestoreAnimeRepository.syncAnimeToFirestore(entity)
                } catch (e: Exception) {
                    Log.e("AnimeDetailVM", "Error syncing to Firestore: ${e.message}")
                }
            } catch (e: Exception) {
                Log.e("AnimeDetailVM", "Error al guardar anime: ${e.message}")
            }
        }
    }
}
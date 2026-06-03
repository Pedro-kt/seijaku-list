package com.yumedev.seijakulist.ui.screens.my_animes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulist.data.local.entities.AnimeEntity
import com.yumedev.seijakulist.data.mapper.local.toAnimeEntity
import com.yumedev.seijakulist.data.repository.AnimeLocalRepository
import com.yumedev.seijakulist.data.repository.AnimeRepository
import com.yumedev.seijakulist.data.repository.FirestoreAnimeRepository
import com.yumedev.seijakulist.util.UserAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyAnimeListViewModel @Inject constructor(
    private val animeLocalRepository: AnimeLocalRepository,
    private val firestoreAnimeRepository: FirestoreAnimeRepository,
    private val animeRepository: AnimeRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var hasLoadedOnce = false

    // Evento para notificar cuando se completa un anime
    data class AnimeCompletedEvent(val animeId: Int, val totalCompleted: Int)
    private val _animeCompletedEvent = MutableStateFlow<AnimeCompletedEvent?>(null)
    val animeCompletedEvent: StateFlow<AnimeCompletedEvent?> = _animeCompletedEvent.asStateFlow()

    val savedAnimes: StateFlow<List<AnimeEntity>> =
        animeLocalRepository.getAllAnimes()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    val savedAnimeStatusComplete: StateFlow<List<AnimeEntity>> =
        animeLocalRepository.getAnimesStatusComplete()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    val savedAnimeStatusWatching: StateFlow<List<AnimeEntity>> =
        animeLocalRepository.getAnimesStatusWatching()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    val savedAnimeStatusPending: StateFlow<List<AnimeEntity>> =
        animeLocalRepository.getAnimesStatusPending()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    val savedAnimeStatusAbandoned: StateFlow<List<AnimeEntity>> =
        animeLocalRepository.getAnimesStatusAbandoned()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    val savedAnimeStatusPlanned: StateFlow<List<AnimeEntity>> =
        animeLocalRepository.getAnimesStatusPlanned()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    /**
     * Obtiene todos los géneros únicos de los animes guardados
     */
    fun getAvailableGenres(): List<String> {
        return savedAnimes.value
            .flatMap { anime ->
                anime.genres.split(",")
                    .map { it.trim() }
                    .filter { it.isNotBlank() }
            }
            .distinct()
            .sorted()
    }

    /**
     * Obtiene todos los años únicos de los animes guardados
     */
    fun getAvailableYears(): List<String> {
        return savedAnimes.value
            .mapNotNull { it.year }
            .filter { it.isNotBlank() && it != "No encontrado" }
            .distinct()
            .sortedDescending()
    }

    /**
     * Obtiene todos los tipos únicos de los animes guardados
     */
    fun getAvailableTypes(): List<String> {
        return savedAnimes.value
            .mapNotNull { it.typeAnime }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            // Solo mostrar skeleton la primera vez
            if (!hasLoadedOnce) {
                _isLoading.value = true
                val startTime = System.currentTimeMillis()
                savedAnimes.first()
                val elapsed = System.currentTimeMillis() - startTime
                if (elapsed < 600) {
                    delay(600 - elapsed)
                }
                _isLoading.value = false
                hasLoadedOnce = true
            }

            // Luego enriquecer detalles en background
            enrichMissingDetails()
        }
    }

    /**
     * Enriquece con datos completos de la API de Jikan los animes que vinieron de Firestore
     * con datos básicos (synopsis == null). Hace máximo 2 peticiones por segundo.
     */
    private suspend fun enrichMissingDetails() {
        try {
            val allAnimes = animeLocalRepository.getAllAnimes().first()
            val incomplete = allAnimes.filter { it.synopsis == null }
            if (incomplete.isEmpty()) return

            Log.d("MyAnimeListVM", "Enriqueciendo ${incomplete.size} anime(s) con detalles de la API...")

            incomplete.chunked(2).forEach { batch ->
                batch.forEach { anime ->
                    try {
                        val detail = animeRepository.getAnimeDetailsById(anime.malId)

                        val genresString = detail.genres
                            .mapNotNull { it?.name }
                            .joinToString(", ")
                            .ifBlank { anime.genres }

                        val studiosString = detail.studios
                            .mapNotNull { it?.nameStudio }
                            .joinToString(", ")

                        val enriched = anime.copy(
                            title = detail.title,
                            imageUrl = detail.images
                                .takeIf { it.isNotBlank() && it != "URL de imagen predeterminada" }
                                ?: anime.imageUrl,
                            totalEpisodes = if (detail.episodes > 0) detail.episodes else anime.totalEpisodes,
                            genres = genresString,
                            synopsis = detail.synopsis,
                            titleEnglish = detail.titleEnglish,
                            titleJapanese = detail.titleJapanese,
                            studios = studiosString,
                            score = detail.score,
                            scoreBy = detail.scoreBy,
                            typeAnime = detail.typeAnime,
                            duration = detail.duration,
                            season = detail.season,
                            year = detail.year?.toString()?.takeIf { it != "No encontrado" },
                            status = detail.status,
                            aired = detail.aired,
                            rank = detail.rank,
                            rating = detail.rating,
                            source = detail.source
                        )

                        animeLocalRepository.updateAnime(enriched)
                        Log.d("MyAnimeListVM", "Enriquecido: ${anime.title} (malId=${anime.malId})")
                    } catch (e: Exception) {
                        Log.e("MyAnimeListVM", "Error enriqueciendo malId=${anime.malId}: ${e.message}")
                    }
                }
                // 2 peticiones por segundo → esperamos 1s entre cada batch de 2
                delay(1000L)
            }

            Log.d("MyAnimeListVM", "Enriquecimiento completado")
        } catch (e: Exception) {
            Log.e("MyAnimeListVM", "Error en enrichMissingDetails: ${e.message}")
        }
    }

    fun handleUserAction(animeId: Int, action: UserAction) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentAnime = animeLocalRepository.getAnimeById(animeId)

            var wasJustCompleted = false

            val updatedAnime = when (action) {

                is UserAction.UpdateProgress -> {
                    val willBeCompleted = action.newProgress >= currentAnime.totalEpisodes
                    wasJustCompleted = willBeCompleted && currentAnime.userStatus != "Completado"
                    val newRewatchCount = if (willBeCompleted && currentAnime.userStatus != "Completado") {
                        if (currentAnime.rewatchCount == 0) 1 else currentAnime.rewatchCount + 1
                    } else {
                        currentAnime.rewatchCount
                    }

                    currentAnime.copy(
                        episodesWatched = action.newProgress,
                        userStatus = if (willBeCompleted) "Completado" else currentAnime.userStatus,
                        rewatchCount = newRewatchCount
                    )
                }

                is UserAction.MarkAsCompleted -> {
                    wasJustCompleted = currentAnime.userStatus != "Completado"
                    currentAnime.copy(
                        userStatus = "Completado",
                        episodesWatched = currentAnime.totalEpisodes,
                        rewatchCount = currentAnime.rewatchCount + 1
                    )
                }

                is UserAction.MarkAsPlanned -> {
                    currentAnime.copy(
                        userStatus = "Planeado",
                        episodesWatched = 0
                    )
                }

                is UserAction.MarkAsWatching -> {
                    currentAnime.copy(
                        userStatus = "Viendo",
                        episodesWatched = if (currentAnime.userStatus == "Completado") 0 else currentAnime.episodesWatched
                    )
                }

                is UserAction.MarkAsDropped -> {
                    currentAnime.copy(
                        userStatus = "Abandonado",
                        episodesWatched = if (currentAnime.userStatus == "Completado") 0 else currentAnime.episodesWatched
                    )
                }

                is UserAction.MarkAsPending -> {
                    currentAnime.copy(
                        userStatus = "Pendiente",
                        episodesWatched = if (currentAnime.userStatus == "Completado") 0 else currentAnime.episodesWatched
                    )
                }

                is UserAction.None -> currentAnime
            }

            val animeEntity = updatedAnime.toAnimeEntity()
            animeLocalRepository.updateAnime(animeEntity)

            // Si se acaba de completar, emitir el evento
            if (wasJustCompleted) {
                val totalCompleted = savedAnimeStatusComplete.value.size + 1
                _animeCompletedEvent.value = AnimeCompletedEvent(animeId, totalCompleted)
            }

            try {
                firestoreAnimeRepository.syncAnimeToFirestore(animeEntity)
            } catch (e: Exception) {
                Log.e("MyAnimeListVM", "Error syncing update to Firestore: ${e.message}")
            }
        }
    }

    fun updateEpisodesWatched(animeId: Int, newEpisodesWatched: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentAnime = animeLocalRepository.getAnimeById(animeId)

            val wasJustCompleted = newEpisodesWatched == currentAnime.totalEpisodes &&
                                   currentAnime.userStatus != "Completado"

            val newStatus = if (newEpisodesWatched == currentAnime.totalEpisodes) {
                "Completado"
            } else {
                currentAnime.userStatus
            }

            val newRewatchCount = if (newStatus == "Completado" && currentAnime.userStatus != "Completado") {
                currentAnime.rewatchCount + 1
            } else {
                currentAnime.rewatchCount
            }

            val updatedAnime = currentAnime.copy(
                episodesWatched = newEpisodesWatched,
                userStatus = newStatus,
                rewatchCount = newRewatchCount
            )

            val anime = updatedAnime.toAnimeEntity()
            animeLocalRepository.updateAnime(anime)

            // Si se acaba de completar, emitir el evento
            if (wasJustCompleted) {
                val totalCompleted = savedAnimeStatusComplete.value.size + 1
                _animeCompletedEvent.value = AnimeCompletedEvent(animeId, totalCompleted)
            }

            try {
                firestoreAnimeRepository.syncAnimeToFirestore(anime)
            } catch (e: Exception) {
                Log.e("MyAnimeListVM", "Error syncing episodes update to Firestore: ${e.message}")
            }
        }
    }

    fun clearAnimeCompletedEvent() {
        _animeCompletedEvent.value = null
    }

    fun deleteAnimeToList(animeId: Int) {
        viewModelScope.launch {
            try {
                animeLocalRepository.deleteAnimeById(animeId)
                try {
                    firestoreAnimeRepository.deleteAnimeFromFirestore(animeId)
                } catch (e: Exception) {
                    Log.e("MyAnimeListVM", "Error deleting from Firestore: ${e.message}")
                }
            } catch (e: Exception) {
                Log.e("MyAnimeListVM", "Error al eliminar el anime: ${e.message}")
            }
        }
    }
}
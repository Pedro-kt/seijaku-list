package com.yumedev.seijakulist.ui.screens.my_animes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulist.data.local.entities.AnimeEntity
import com.yumedev.seijakulist.data.mapper.local.toAnimeEntity
import com.yumedev.seijakulist.data.repository.AnimeLocalRepository
import com.yumedev.seijakulist.data.repository.AnimeAniListRepository
import com.yumedev.seijakulist.data.repository.FirestoreAnimeRepository
import com.yumedev.seijakulist.domain.usecase.anilist.GetAnimeDetailAniListUseCase
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
    private val getAnimeDetailAniListUseCase: GetAnimeDetailAniListUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var hasLoadedOnce = false

    // Evento para notificar cuando se completa un anime
    data class AnimeCompletedEvent(val animeId: Int, val totalCompleted: Int, val animeTitle: String)
    private val _animeCompletedEvent = MutableStateFlow<AnimeCompletedEvent?>(null)
    val animeCompletedEvent: StateFlow<AnimeCompletedEvent?> = _animeCompletedEvent.asStateFlow()

    // Estado para controlar el modal de review
    data class ReviewDialogState(
        val animeId: Int,
        val animeTitle: String,
        val currentScore: Float,
        val currentOpinion: String
    )
    private val _showReviewDialog = MutableStateFlow<ReviewDialogState?>(null)
    val showReviewDialog: StateFlow<ReviewDialogState?> = _showReviewDialog.asStateFlow()

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
     * Enriquece con datos completos de la API de AniList los animes que vinieron de Firestore
     * con datos básicos (synopsis == null). Hace máximo 2 peticiones por segundo.
     */
    private suspend fun enrichMissingDetails() {
        try {
            val allAnimes = animeLocalRepository.getAllAnimes().first()
            val incomplete = allAnimes.filter { it.synopsis == null }
            if (incomplete.isEmpty()) return

            Log.d("MyAnimeListVM", "Enriqueciendo ${incomplete.size} anime(s) con detalles de AniList API...")

            incomplete.chunked(2).forEach { batch ->
                batch.forEach { anime ->
                    try {
                        // Usar AniList para obtener detalles
                        val detail = getAnimeDetailAniListUseCase(anime.malId)

                        val genresString = detail.genres
                            .joinToString(", ")
                            .ifBlank { anime.genres }

                        val studiosString = detail.studios
                            .joinToString(", ")

                        val enriched = anime.copy(
                            title = detail.title,
                            imageUrl = detail.images
                                .takeIf { it.isNotBlank() && it != "URL de imagen predeterminada" }
                                ?: anime.imageUrl,
                            totalEpisodes = detail.episodes,
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
                            year = detail.year.toString().takeIf { it != "No encontrado" },
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

                    // Auto-setear fecha de fin solo si estaba "Viendo" y ahora se completa
                    val endDate = if (willBeCompleted && currentAnime.userStatus == "Viendo") {
                        System.currentTimeMillis()
                    } else {
                        currentAnime.endDate
                    }

                    currentAnime.copy(
                        episodesWatched = action.newProgress,
                        userStatus = if (willBeCompleted) "Completado" else currentAnime.userStatus,
                        rewatchCount = newRewatchCount,
                        endDate = endDate
                    )
                }

                is UserAction.MarkAsCompleted -> {
                    wasJustCompleted = currentAnime.userStatus != "Completado"
                    // Auto-setear fecha de fin solo si estaba "Viendo"
                    val endDate = if (currentAnime.userStatus == "Viendo") {
                        System.currentTimeMillis()
                    } else {
                        currentAnime.endDate
                    }
                    currentAnime.copy(
                        userStatus = "Completado",
                        episodesWatched = currentAnime.totalEpisodes,
                        rewatchCount = currentAnime.rewatchCount + 1,
                        endDate = endDate
                    )
                }

                is UserAction.MarkAsPlanned -> {
                    // Limpiar ambas fechas al marcar como planeado
                    currentAnime.copy(
                        userStatus = "Planeado",
                        episodesWatched = 0,
                        startDate = null,
                        endDate = null
                    )
                }

                is UserAction.MarkAsWatching -> {
                    // Auto-setear fecha de inicio solo si está vacía
                    val startDate = currentAnime.startDate ?: System.currentTimeMillis()
                    // Limpiar fecha de fin si venía de completado
                    val endDate = if (currentAnime.userStatus == "Completado") null else currentAnime.endDate
                    currentAnime.copy(
                        userStatus = "Viendo",
                        episodesWatched = if (currentAnime.userStatus == "Completado") 0 else currentAnime.episodesWatched,
                        startDate = startDate,
                        endDate = endDate
                    )
                }

                is UserAction.MarkAsDropped -> {
                    // Limpiar fecha de fin si venía de completado
                    val endDate = if (currentAnime.userStatus == "Completado") null else currentAnime.endDate
                    currentAnime.copy(
                        userStatus = "Abandonado",
                        episodesWatched = if (currentAnime.userStatus == "Completado") 0 else currentAnime.episodesWatched,
                        endDate = endDate
                    )
                }

                is UserAction.MarkAsPending -> {
                    // Limpiar fecha de fin si venía de completado
                    val endDate = if (currentAnime.userStatus == "Completado") null else currentAnime.endDate
                    currentAnime.copy(
                        userStatus = "Pendiente",
                        episodesWatched = if (currentAnime.userStatus == "Completado") 0 else currentAnime.episodesWatched,
                        endDate = endDate
                    )
                }

                is UserAction.None -> currentAnime
            }

            val animeEntity = updatedAnime.toAnimeEntity()
            animeLocalRepository.updateAnime(animeEntity)

            // Si se acaba de completar, emitir el evento y mostrar modal de review
            if (wasJustCompleted) {
                val totalCompleted = savedAnimeStatusComplete.value.size + 1
                _animeCompletedEvent.value = AnimeCompletedEvent(animeId, totalCompleted, animeEntity.title)
                _showReviewDialog.value = ReviewDialogState(
                    animeId = animeId,
                    animeTitle = animeEntity.title,
                    currentScore = animeEntity.userScore,
                    currentOpinion = animeEntity.userOpiniun
                )
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

            // Auto-setear fecha de fin solo si estaba "Viendo" y ahora se completa
            val endDate = if (newStatus == "Completado" && currentAnime.userStatus == "Viendo") {
                System.currentTimeMillis()
            } else {
                currentAnime.endDate
            }

            val updatedAnime = currentAnime.copy(
                episodesWatched = newEpisodesWatched,
                userStatus = newStatus,
                rewatchCount = newRewatchCount,
                endDate = endDate
            )

            val anime = updatedAnime.toAnimeEntity()
            animeLocalRepository.updateAnime(anime)

            // Si se acaba de completar, emitir el evento y mostrar modal de review
            if (wasJustCompleted) {
                val totalCompleted = savedAnimeStatusComplete.value.size + 1
                _animeCompletedEvent.value = AnimeCompletedEvent(animeId, totalCompleted, anime.title)
                _showReviewDialog.value = ReviewDialogState(
                    animeId = animeId,
                    animeTitle = anime.title,
                    currentScore = anime.userScore,
                    currentOpinion = anime.userOpiniun
                )
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

    /**
     * Actualiza la puntuación y opinión del usuario sobre un anime
     */
    fun updateAnimeReview(animeId: Int, score: Float, opinion: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentAnime = animeLocalRepository.getAnimeById(animeId)
                val updatedAnime = currentAnime.copy(
                    userScore = score,
                    userOpiniun = opinion
                )

                val animeEntity = updatedAnime.toAnimeEntity()
                animeLocalRepository.updateAnime(animeEntity)

                try {
                    firestoreAnimeRepository.syncAnimeToFirestore(animeEntity)
                } catch (e: Exception) {
                    Log.e("MyAnimeListVM", "Error syncing review to Firestore: ${e.message}")
                }

                Log.d("MyAnimeListVM", "Review actualizado: score=$score, opinion=${opinion.take(50)}")
            } catch (e: Exception) {
                Log.e("MyAnimeListVM", "Error updating anime review: ${e.message}")
            }
        }
    }

    /**
     * Cierra el modal de review
     */
    fun dismissReviewDialog() {
        _showReviewDialog.value = null
    }
}
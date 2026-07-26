package com.yumedev.seijakulist.ui.screens.my_mangas

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulist.data.local.entities.MangaEntity
import com.yumedev.seijakulist.data.mapper.toMangaEntity
import com.yumedev.seijakulist.data.repository.FirestoreMangaRepository
import com.yumedev.seijakulist.data.repository.MangaLocalRepository
import com.yumedev.seijakulist.domain.usecase.anilist.GetMangaDetailAniListUseCase
import com.yumedev.seijakulist.domain.usecase.manga.AddMangaToListUseCase
import com.yumedev.seijakulist.domain.usecase.manga.RemoveMangaFromListUseCase
import com.yumedev.seijakulist.domain.usecase.manga.SyncMangasFromFirestoreUseCase
import com.yumedev.seijakulist.domain.usecase.manga.UpdateMangaProgressUseCase
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
class MyMangasViewModel @Inject constructor(
    private val mangaLocalRepository: MangaLocalRepository,
    private val firestoreMangaRepository: FirestoreMangaRepository,
    private val getMangaDetailAniListUseCase: GetMangaDetailAniListUseCase,
    private val addMangaToListUseCase: AddMangaToListUseCase,
    private val updateMangaProgressUseCase: UpdateMangaProgressUseCase,
    private val removeMangaFromListUseCase: RemoveMangaFromListUseCase,
    private val syncMangasFromFirestoreUseCase: SyncMangasFromFirestoreUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var hasLoadedOnce = false

    // Evento para notificar cuando se completa un manga
    data class MangaCompletedEvent(val mangaId: Int, val totalCompleted: Int, val mangaTitle: String)
    private val _mangaCompletedEvent = MutableStateFlow<MangaCompletedEvent?>(null)
    val mangaCompletedEvent: StateFlow<MangaCompletedEvent?> = _mangaCompletedEvent.asStateFlow()

    // Estado para controlar el modal de review
    data class ReviewDialogState(
        val mangaId: Int,
        val mangaTitle: String,
        val currentScore: Float,
        val currentOpinion: String
    )
    private val _showReviewDialog = MutableStateFlow<ReviewDialogState?>(null)
    val showReviewDialog: StateFlow<ReviewDialogState?> = _showReviewDialog.asStateFlow()

    // StateFlows reactivos para todos los mangas
    val savedMangas: StateFlow<List<MangaEntity>> =
        mangaLocalRepository.getAllMangas()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    val readingMangas: StateFlow<List<MangaEntity>> =
        mangaLocalRepository.getMangasStatusReading()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    val completedMangas: StateFlow<List<MangaEntity>> =
        mangaLocalRepository.getMangasStatusCompleted()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    val plannedMangas: StateFlow<List<MangaEntity>> =
        mangaLocalRepository.getMangasStatusPlanned()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    val pausedMangas: StateFlow<List<MangaEntity>> =
        mangaLocalRepository.getMangasStatusPaused()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    val droppedMangas: StateFlow<List<MangaEntity>> =
        mangaLocalRepository.getMangasStatusDropped()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    /**
     * Obtiene todos los géneros únicos de los mangas guardados
     */
    fun getAvailableGenres(): List<String> {
        return savedMangas.value
            .flatMap { manga ->
                manga.genres.split(",")
                    .map { it.trim() }
                    .filter { it.isNotBlank() }
            }
            .distinct()
            .sorted()
    }

    /**
     * Obtiene todos los tipos únicos de los mangas guardados
     */
    fun getAvailableTypes(): List<String> {
        return savedMangas.value
            .map { it.typeManga }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            // Solo mostrar skeleton la primera vez
            if (!hasLoadedOnce) {
                _isLoading.value = true
                syncFromFirestore()
                val startTime = System.currentTimeMillis()
                savedMangas.first()
                val elapsed = System.currentTimeMillis() - startTime
                if (elapsed < 600) {
                    delay(600 - elapsed)
                }
                _isLoading.value = false
                hasLoadedOnce = true
            }

            // Enriquecer detalles en background
            enrichMissingDetails()
        }
    }

    /**
     * Sincroniza mangas desde Firestore
     */
    fun syncFromFirestore() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = syncMangasFromFirestoreUseCase()
                result.onSuccess { count ->
                    Log.d("MyMangasViewModel", "Sincronizados $count mangas desde Firestore")
                }.onFailure { e ->
                    Log.e("MyMangasViewModel", "Error syncing from Firestore: ${e.message}")
                }
            } catch (e: Exception) {
                Log.e("MyMangasViewModel", "Error in syncFromFirestore: ${e.message}")
            }
        }
    }

    /**
     * Enriquece con datos completos de la API los mangas que vinieron de Firestore
     * con datos básicos (synopsis == null). Hace máximo 2 peticiones por segundo.
     */
    private suspend fun enrichMissingDetails() {
        try {
            val allMangas = mangaLocalRepository.getAllMangas().first()
            val incomplete = allMangas.filter { it.synopsis == null }
            if (incomplete.isEmpty()) return

            Log.d("MyMangasViewModel", "Enriqueciendo ${incomplete.size} manga(s) con detalles de AniList API...")

            incomplete.chunked(2).forEach { batch ->
                batch.forEach { manga ->
                    try {
                        val detail = getMangaDetailAniListUseCase(malId = manga.malId)

                        val enriched = manga.copy(
                            title = detail.title,
                            titleEnglish = detail.titleEnglish,
                            titleJapanese = detail.titleJapanese,
                            imageUrl = detail.images
                                .takeIf { it.isNotBlank() }
                                ?: manga.imageUrl,
                            bannerImage = detail.bannerImage,
                            chapters = detail.chapters,
                            volumes = detail.volumes,
                            status = detail.status,
                            published = detail.published,
                            score = detail.score,
                            scoreBy = detail.scoreBy,
                            rank = detail.rank,
                            synopsis = detail.synopsis,
                            background = detail.background,
                            genres = detail.genres.mapNotNull { it?.name }.joinToString(","),
                            demographics = detail.demographics.mapNotNull { it?.name }.joinToString(","),
                            authors = com.google.gson.Gson().toJson(detail.authors),
                            serializations = com.google.gson.Gson().toJson(detail.serializations)
                        )

                        mangaLocalRepository.updateManga(enriched)
                        Log.d("MyMangasViewModel", "Enriquecido: ${manga.title} (malId=${manga.malId})")
                    } catch (e: Exception) {
                        Log.e("MyMangasViewModel", "Error enriqueciendo malId=${manga.malId}: ${e.message}")
                    }
                }
                delay(1000L) // 2 peticiones por segundo
            }

            Log.d("MyMangasViewModel", "Enriquecimiento completado")
        } catch (e: Exception) {
            Log.e("MyMangasViewModel", "Error en enrichMissingDetails: ${e.message}")
        }
    }

    /**
     * Agrega un manga a la lista
     */
    fun addMangaToList(
        mangaId: Int,
        status: String,
        chaptersRead: Int = 0,
        volumesRead: Int = 0,
        userScore: Float = 0f
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("MyMangasViewModel", "=== LLAMANDO addMangaToList ===")
            Log.d("MyMangasViewModel", "mangaId: $mangaId, status: $status, chapters: $chaptersRead, volumes: $volumesRead, score: $userScore")

            val result = addMangaToListUseCase(
                malId = mangaId,
                status = status,
                chaptersRead = chaptersRead,
                volumesRead = volumesRead,
                userScore = userScore
            )
            result.onSuccess {
                Log.d("MyMangasViewModel", "Manga agregado exitosamente")
            }
            result.onFailure { e ->
                Log.e("MyMangasViewModel", "Error adding manga to list: ${e.message}", e)
            }
        }
    }

    /**
     * Versión suspend de addMangaToList que espera a que termine la operación
     */
    suspend fun addMangaToListSuspend(
        mangaDetail: com.yumedev.seijakulist.domain.models.MangaDetail,
        status: String,
        chaptersRead: Int = 0,
        volumesRead: Int = 0,
        userScore: Float = 0f
    ) {
        Log.d("MyMangasViewModel", "=== LLAMANDO addMangaToListSuspend ===")
        Log.d("MyMangasViewModel", "manga: ${mangaDetail.title}, status: $status, chapters: $chaptersRead, volumes: $volumesRead, score: $userScore")

        val result = addMangaToListUseCase(
            mangaDetail = mangaDetail,
            status = status,
            chaptersRead = chaptersRead,
            volumesRead = volumesRead,
            userScore = userScore
        )
        result.onSuccess {
            Log.d("MyMangasViewModel", "Manga agregado exitosamente (suspend)")
        }
        result.onFailure { e ->
            Log.e("MyMangasViewModel", "Error adding manga to list (suspend): ${e.message}", e)
        }
    }

    /**
     * Actualiza el progreso de capítulos/volúmenes leídos
     */
    fun updateProgress(
        mangaId: Int,
        chaptersRead: Int? = null,
        volumesRead: Int? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentManga = mangaLocalRepository.getMangaById(mangaId) ?: return@launch

                val newChaptersRead = chaptersRead ?: currentManga.chaptersRead
                val totalChapters = currentManga.chapters

                // Auto-completar si se leyeron todos los capítulos
                val wasJustCompleted = totalChapters != null &&
                        newChaptersRead >= totalChapters &&
                        currentManga.userStatus != "Completado"

                val newStatus = if (totalChapters != null && newChaptersRead >= totalChapters) {
                    "Completado"
                } else {
                    currentManga.userStatus
                }

                val newRereadCount = if (wasJustCompleted) {
                    currentManga.rereadCount + 1
                } else {
                    currentManga.rereadCount
                }

                val endDate = if (wasJustCompleted && currentManga.userStatus == "Leyendo") {
                    System.currentTimeMillis()
                } else {
                    currentManga.endDate
                }

                val result = updateMangaProgressUseCase(
                    mangaId = mangaId,
                    chaptersRead = chaptersRead,
                    volumesRead = volumesRead,
                    status = if (newStatus != currentManga.userStatus) newStatus else null,
                    rereadCount = if (newRereadCount != currentManga.rereadCount) newRereadCount else null,
                    endDate = if (endDate != currentManga.endDate) endDate else null
                )

                result.onSuccess {
                    if (wasJustCompleted) {
                        val totalCompleted = completedMangas.value.size + 1
                        _mangaCompletedEvent.value = MangaCompletedEvent(
                            mangaId,
                            totalCompleted,
                            currentManga.title
                        )
                        _showReviewDialog.value = ReviewDialogState(
                            mangaId = mangaId,
                            mangaTitle = currentManga.title,
                            currentScore = currentManga.userScore,
                            currentOpinion = currentManga.userOpinion
                        )
                    }
                }

                result.onFailure { e ->
                    Log.e("MyMangasViewModel", "Error updating progress: ${e.message}")
                }
            } catch (e: Exception) {
                Log.e("MyMangasViewModel", "Error in updateProgress: ${e.message}")
            }
        }
    }

    /**
     * Cambia el estado de un manga
     */
    fun updateStatus(mangaId: Int, newStatus: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentManga = mangaLocalRepository.getMangaById(mangaId) ?: return@launch

                val startDate = when (newStatus) {
                    "Leyendo" -> currentManga.startDate ?: System.currentTimeMillis()
                    "Planeado" -> null
                    else -> currentManga.startDate
                }

                val endDate = when (newStatus) {
                    "Completado" -> if (currentManga.userStatus == "Leyendo") System.currentTimeMillis() else currentManga.endDate
                    "Planeado" -> null
                    "Leyendo" -> if (currentManga.userStatus == "Completado") null else currentManga.endDate
                    else -> if (currentManga.userStatus == "Completado") null else currentManga.endDate
                }

                val chaptersRead = when (newStatus) {
                    "Completado" -> currentManga.chapters ?: currentManga.chaptersRead
                    "Planeado", "Leyendo" -> if (currentManga.userStatus == "Completado") 0 else currentManga.chaptersRead
                    else -> currentManga.chaptersRead
                }

                val result = updateMangaProgressUseCase(
                    mangaId = mangaId,
                    status = newStatus,
                    startDate = startDate,
                    endDate = endDate,
                    chaptersRead = chaptersRead
                )

                result.onFailure { e ->
                    Log.e("MyMangasViewModel", "Error updating status: ${e.message}")
                }
            } catch (e: Exception) {
                Log.e("MyMangasViewModel", "Error in updateStatus: ${e.message}")
            }
        }
    }

    /**
     * Actualiza la puntuación y opinión del usuario
     */
    fun updateMangaReview(mangaId: Int, score: Float, opinion: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = updateMangaProgressUseCase(
                mangaId = mangaId,
                userScore = score,
                userOpinion = opinion
            )
            result.onFailure { e ->
                Log.e("MyMangasViewModel", "Error updating review: ${e.message}")
            }
        }
    }

    /**
     * Elimina un manga de la lista
     */
    fun deleteManga(mangaId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = removeMangaFromListUseCase(mangaId)
            result.onFailure { e ->
                Log.e("MyMangasViewModel", "Error deleting manga: ${e.message}")
            }
        }
    }

    fun clearMangaCompletedEvent() {
        _mangaCompletedEvent.value = null
    }

    fun dismissReviewDialog() {
        _showReviewDialog.value = null
    }
}

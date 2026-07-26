package com.yumedev.seijakulist.domain.usecase.manga

import com.yumedev.seijakulist.data.mapper.toMangaEntity
import com.yumedev.seijakulist.data.repository.FirestoreMangaRepository
import com.yumedev.seijakulist.data.repository.MangaLocalRepository
import javax.inject.Inject

/**
 * UseCase para actualizar el progreso y datos de un manga en la lista
 *
 * Este use case:
 * 1. Obtiene el manga actual de Room
 * 2. Actualiza los campos especificados
 * 3. Guarda los cambios en Room
 * 4. Sincroniza con Firestore
 */
class UpdateMangaProgressUseCase @Inject constructor(
    private val mangaLocalRepository: MangaLocalRepository,
    private val firestoreMangaRepository: FirestoreMangaRepository
) {
    /**
     * Actualiza el progreso y/o datos de un manga
     *
     * @param mangaId ID de MyAnimeList del manga
     * @param chaptersRead Capítulos leídos (null = no cambiar)
     * @param volumesRead Volúmenes leídos (null = no cambiar)
     * @param status Estado del manga (null = no cambiar)
     * @param userScore Puntuación del usuario (null = no cambiar)
     * @param userOpinion Opinión del usuario (null = no cambiar)
     * @param startDate Fecha de inicio (null = no cambiar)
     * @param endDate Fecha de fin (null = no cambiar)
     * @param rereadCount Contador de relecturas (null = no cambiar)
     * @param plannedPriority Prioridad (null = no cambiar)
     * @param plannedNote Nota (null = no cambiar)
     * @return Result<Unit> indicando éxito o error
     */
    suspend operator fun invoke(
        mangaId: Int,
        chaptersRead: Int? = null,
        volumesRead: Int? = null,
        status: String? = null,
        userScore: Float? = null,
        userOpinion: String? = null,
        startDate: Long? = null,
        endDate: Long? = null,
        rereadCount: Int? = null,
        plannedPriority: String? = null,
        plannedNote: String? = null
    ): Result<Unit> {
        return try {
            // 1. Obtener manga actual
            val currentManga = mangaLocalRepository.getMangaById(mangaId)
                ?: return Result.failure(Exception("Manga no encontrado en la lista"))

            // 2. Crear manga actualizado (solo actualizar campos no nulos)
            val updatedManga = currentManga.toMangaEntity().copy(
                chaptersRead = chaptersRead ?: currentManga.chaptersRead,
                volumesRead = volumesRead ?: currentManga.volumesRead,
                statusUser = status ?: currentManga.userStatus,
                userScore = userScore ?: currentManga.userScore,
                userOpinion = userOpinion ?: currentManga.userOpinion,
                startDate = startDate ?: currentManga.startDate,
                endDate = endDate ?: currentManga.endDate,
                rereadCount = rereadCount ?: currentManga.rereadCount,
                plannedPriority = plannedPriority ?: currentManga.plannedPriority,
                plannedNote = plannedNote ?: currentManga.plannedNote,
                lastModified = System.currentTimeMillis()
            )

            // 3. Actualizar en Room
            mangaLocalRepository.updateManga(updatedManga)

            // 4. Sincronizar con Firestore
            firestoreMangaRepository.syncMangaToFirestore(updatedManga)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

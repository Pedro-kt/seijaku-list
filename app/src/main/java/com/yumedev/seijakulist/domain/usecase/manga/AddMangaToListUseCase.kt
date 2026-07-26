package com.yumedev.seijakulist.domain.usecase.manga

import com.yumedev.seijakulist.data.mapper.toMangaEntity
import com.yumedev.seijakulist.data.repository.FirestoreMangaRepository
import com.yumedev.seijakulist.data.repository.MangaLocalRepository
import com.yumedev.seijakulist.domain.models.MangaDetail
import com.yumedev.seijakulist.domain.usecase.anilist.GetMangaDetailAniListUseCase
import javax.inject.Inject

/**
 * UseCase para agregar un manga a la lista del usuario
 *
 * Este use case:
 * 1. Usa los detalles del manga proporcionados o los obtiene de la API si no se proporcionan
 * 2. Crea una entidad con los datos del usuario
 * 3. Guarda localmente en Room
 * 4. Sincroniza con Firestore
 */
class AddMangaToListUseCase @Inject constructor(
    private val mangaLocalRepository: MangaLocalRepository,
    private val firestoreMangaRepository: FirestoreMangaRepository,
    private val getMangaDetailAniListUseCase: GetMangaDetailAniListUseCase
) {
    /**
     * Agrega un manga a la lista del usuario
     *
     * @param mangaDetail Detalles del manga (opcional, si no se proporciona se obtendrán de la API)
     * @param mangaId ID de AniList (opcional, solo si mangaDetail es null)
     * @param malId MyAnimeList ID (opcional, solo si mangaDetail es null)
     * @param status Estado del manga ("Leyendo", "Completado", "Planeado", "Pausado", "Abandonado")
     * @param chaptersRead Capítulos leídos (default: 0)
     * @param volumesRead Volúmenes leídos (default: 0)
     * @param userScore Puntuación del usuario 0-10 (default: 0f)
     * @param userOpinion Opinión del usuario (default: "")
     * @param startDate Fecha de inicio en milisegundos (default: null)
     * @param endDate Fecha de fin en milisegundos (default: null)
     * @param plannedPriority Prioridad si status es "Planeado" (default: null)
     * @param plannedNote Nota si status es "Planeado" (default: null)
     * @return Result<Unit> indicando éxito o error
     */
    suspend operator fun invoke(
        mangaDetail: MangaDetail? = null,
        mangaId: Int? = null,
        malId: Int? = null,
        status: String,
        chaptersRead: Int = 0,
        volumesRead: Int = 0,
        userScore: Float = 0f,
        userOpinion: String = "",
        startDate: Long? = null,
        endDate: Long? = null,
        plannedPriority: String? = null,
        plannedNote: String? = null
    ): Result<Unit> {
        return try {
            android.util.Log.d("AddMangaToListUseCase", "=== INICIANDO GUARDADO DE MANGA ===")
            android.util.Log.d("AddMangaToListUseCase", "mangaDetail: ${mangaDetail != null}, mangaId: $mangaId, malId: $malId, status: $status")

            // 1. Usar el mangaDetail proporcionado o obtenerlo de la API
            val detail = mangaDetail ?: run {
                android.util.Log.d("AddMangaToListUseCase", "Obteniendo manga desde API...")
                getMangaDetailAniListUseCase(
                    mangaId = mangaId,
                    malId = malId
                )
            }
            android.util.Log.d("AddMangaToListUseCase", "Manga: ${detail.title} (malId: ${detail.malId})")

            // 2. Crear entidad con datos del usuario
            val mangaEntity = detail.toMangaEntity(
                statusUser = status,
                chaptersRead = chaptersRead,
                volumesRead = volumesRead,
                userScore = userScore,
                userOpinion = userOpinion,
                startDate = startDate,
                endDate = endDate,
                plannedPriority = plannedPriority,
                plannedNote = plannedNote
            )
            android.util.Log.d("AddMangaToListUseCase", "Entidad creada con estado: ${mangaEntity.statusUser}")

            // 3. Guardar localmente
            mangaLocalRepository.insertManga(mangaEntity)
            android.util.Log.d("AddMangaToListUseCase", "Manga guardado en base de datos local")

            // 4. Sincronizar con Firestore
            firestoreMangaRepository.syncMangaToFirestore(mangaEntity)
            android.util.Log.d("AddMangaToListUseCase", "Manga sincronizado con Firestore")

            android.util.Log.d("AddMangaToListUseCase", "=== GUARDADO COMPLETADO CON ÉXITO ===")
            Result.success(Unit)
        } catch (e: Exception) {
            android.util.Log.e("AddMangaToListUseCase", "Error al guardar manga: ${e.message}", e)
            Result.failure(e)
        }
    }
}

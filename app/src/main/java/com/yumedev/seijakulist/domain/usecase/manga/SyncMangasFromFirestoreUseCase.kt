package com.yumedev.seijakulist.domain.usecase.manga

import com.yumedev.seijakulist.data.repository.FirestoreMangaRepository
import com.yumedev.seijakulist.data.repository.MangaLocalRepository
import com.yumedev.seijakulist.data.repository.toMangaEntity
import javax.inject.Inject

/**
 * UseCase para sincronizar mangas desde Firestore a Room
 *
 * Este use case:
 * 1. Obtiene todos los mangas desde Firestore
 * 2. Los guarda/actualiza en Room
 *
 * Se usa típicamente al:
 * - Iniciar sesión
 * - Abrir la app
 * - Pull-to-refresh
 */
class SyncMangasFromFirestoreUseCase @Inject constructor(
    private val mangaLocalRepository: MangaLocalRepository,
    private val firestoreMangaRepository: FirestoreMangaRepository
) {
    /**
     * Sincroniza todos los mangas desde Firestore
     *
     * @return Result<Int> con la cantidad de mangas sincronizados, o error
     */
    suspend operator fun invoke(): Result<Int> {
        return try {
            // 1. Obtener mangas desde Firestore
            val firestoreMangas = firestoreMangaRepository.fetchAllMangasFromFirestore()

            // 2. Convertir a entities y guardar en Room
            val mangaEntities = firestoreMangas.map { it.toMangaEntity() }
            mangaLocalRepository.insertAllMangas(mangaEntities)

            Result.success(mangaEntities.size)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

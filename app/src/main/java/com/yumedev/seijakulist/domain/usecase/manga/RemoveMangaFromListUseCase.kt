package com.yumedev.seijakulist.domain.usecase.manga

import com.yumedev.seijakulist.data.repository.FirestoreMangaRepository
import com.yumedev.seijakulist.data.repository.MangaLocalRepository
import javax.inject.Inject

/**
 * UseCase para eliminar un manga de la lista del usuario
 *
 * Este use case:
 * 1. Elimina el manga de Room
 * 2. Elimina el manga de Firestore
 */
class RemoveMangaFromListUseCase @Inject constructor(
    private val mangaLocalRepository: MangaLocalRepository,
    private val firestoreMangaRepository: FirestoreMangaRepository
) {
    /**
     * Elimina un manga de la lista del usuario
     *
     * @param mangaId ID de MyAnimeList del manga a eliminar
     * @return Result<Unit> indicando éxito o error
     */
    suspend operator fun invoke(mangaId: Int): Result<Unit> {
        return try {
            // 1. Eliminar de Room
            mangaLocalRepository.deleteMangaById(mangaId)

            // 2. Eliminar de Firestore
            firestoreMangaRepository.deleteMangaFromFirestore(mangaId)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

package com.yumedev.seijakulist.data.repository

import com.yumedev.seijakulist.data.local.dao.MangaDao
import com.yumedev.seijakulist.data.local.entities.MangaEntity
import com.yumedev.seijakulist.data.mapper.toMangaEntityDomain
import com.yumedev.seijakulist.domain.models.MangaEntityDomain
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MangaLocalRepository @Inject constructor(
    private val mangaDao: MangaDao
) {

    suspend fun insertManga(manga: MangaEntity) {
        mangaDao.insertManga(manga)
    }

    suspend fun insertAllMangas(mangas: List<MangaEntity>) {
        mangaDao.insertAllMangas(mangas)
    }

    suspend fun updateManga(manga: MangaEntity) {
        mangaDao.updateManga(manga)
    }

    suspend fun deleteMangaById(mangaId: Int) {
        mangaDao.deleteMangaById(mangaId)
    }

    suspend fun deleteAllMangas() {
        mangaDao.deleteAllMangas()
    }

    fun getAllMangas(): Flow<List<MangaEntity>> {
        return mangaDao.getAllMangas()
    }

    suspend fun getMangaById(mangaId: Int): MangaEntityDomain? {
        val mangaEntity = mangaDao.getMangaById(mangaId) ?: return null
        return mangaEntity.toMangaEntityDomain()
    }

    fun isMangaInList(mangaId: Int): Flow<Boolean> {
        return mangaDao.isMangaInList(mangaId)
    }

    // Queries por estado
    fun getMangasStatusReading(): Flow<List<MangaEntity>> {
        return mangaDao.getReadingMangas()
    }

    fun getMangasStatusCompleted(): Flow<List<MangaEntity>> {
        return mangaDao.getCompletedMangas()
    }

    fun getMangasStatusPlanned(): Flow<List<MangaEntity>> {
        return mangaDao.getPlannedMangas()
    }

    fun getMangasStatusPaused(): Flow<List<MangaEntity>> {
        return mangaDao.getPausedMangas()
    }

    fun getMangasStatusDropped(): Flow<List<MangaEntity>> {
        return mangaDao.getDroppedMangas()
    }

    // Estadísticas
    fun getTotalMangasCount(): Flow<Int> {
        return mangaDao.getTotalMangasCount()
    }

    fun getCompletedMangasCount(): Flow<Int> {
        return mangaDao.getCompletedMangasCount()
    }

    fun getTotalChaptersRead(): Flow<Int> {
        return mangaDao.getTotalChaptersRead()
    }

    fun getTotalVolumesRead(): Flow<Int> {
        return mangaDao.getTotalVolumesRead()
    }

    fun getAllGenres(): Flow<List<String>> {
        return mangaDao.getAllGenres()
    }

    // Sincronización
    suspend fun getMangasModifiedAfter(timestamp: Long): List<MangaEntity> {
        return mangaDao.getMangasModifiedAfter(timestamp)
    }
}

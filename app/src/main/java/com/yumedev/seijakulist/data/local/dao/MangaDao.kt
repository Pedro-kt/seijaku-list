package com.yumedev.seijakulist.data.local.dao

import androidx.room.*
import com.yumedev.seijakulist.data.local.entities.MangaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MangaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertManga(manga: MangaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMangas(mangas: List<MangaEntity>)

    @Update
    suspend fun updateManga(manga: MangaEntity)

    @Query("SELECT * FROM mangas WHERE malId = :mangaId")
    suspend fun getMangaById(mangaId: Int): MangaEntity?

    @Query("DELETE FROM mangas WHERE malId = :mangaId")
    suspend fun deleteMangaById(mangaId: Int)

    @Query("DELETE FROM mangas")
    suspend fun deleteAllMangas()

    // Queries reactivas
    @Query("SELECT * FROM mangas")
    fun getAllMangas(): Flow<List<MangaEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM mangas WHERE malId = :mangaId LIMIT 1)")
    fun isMangaInList(mangaId: Int): Flow<Boolean>

    // Queries por estado
    @Query("SELECT * FROM mangas WHERE statusUser = 'Leyendo'")
    fun getReadingMangas(): Flow<List<MangaEntity>>

    @Query("SELECT * FROM mangas WHERE statusUser = 'Completado'")
    fun getCompletedMangas(): Flow<List<MangaEntity>>

    @Query("SELECT * FROM mangas WHERE statusUser = 'Planeado'")
    fun getPlannedMangas(): Flow<List<MangaEntity>>

    @Query("SELECT * FROM mangas WHERE statusUser = 'Pausado'")
    fun getPausedMangas(): Flow<List<MangaEntity>>

    @Query("SELECT * FROM mangas WHERE statusUser = 'Abandonado'")
    fun getDroppedMangas(): Flow<List<MangaEntity>>

    // Estadísticas
    @Query("SELECT COUNT(*) FROM mangas")
    fun getTotalMangasCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM mangas WHERE statusUser = 'Completado'")
    fun getCompletedMangasCount(): Flow<Int>

    @Query("SELECT COALESCE(SUM(chaptersRead), 0) FROM mangas")
    fun getTotalChaptersRead(): Flow<Int>

    @Query("SELECT COALESCE(SUM(volumesRead), 0) FROM mangas")
    fun getTotalVolumesRead(): Flow<Int>

    @Query("SELECT genres FROM mangas WHERE genres != ''")
    fun getAllGenres(): Flow<List<String>>

    // Sincronización
    @Query("SELECT * FROM mangas WHERE lastModified > :timestamp")
    suspend fun getMangasModifiedAfter(timestamp: Long): List<MangaEntity>
}

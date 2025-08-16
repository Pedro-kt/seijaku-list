package com.example.seijakulist.data.local.dao

import androidx.room.*
import com.example.seijakulist.data.local.entities.AnimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnime(anime: AnimeEntity)

    @Query("SELECT * FROM animes WHERE malId = :animeId")
    suspend fun getAnimeById(animeId: Int): AnimeEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAnimes(animes: List<AnimeEntity>)

    @Query("SELECT * FROM animes")
    fun getAllAnimes(): Flow<List<AnimeEntity>>

    @Query("DELETE FROM animes WHERE malId = :animeId")
    suspend fun deleteAnimeById(animeId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM animes WHERE malId = :animeId LIMIT 1)")
    fun isAnimeInList(animeId: Int): Flow<Boolean>

    @Query("SELECT * FROM animes WHERE statusUser = 'Viendo'")
    fun getWatchingAnime(): Flow<List<AnimeEntity>>

    @Query("SELECT * FROM animes WHERE statusUser = 'Completado'")
    fun getCompletedAnime(): Flow<List<AnimeEntity>>

    @Query("SELECT * FROM animes WHERE statusUser = 'Pendiente'")
    fun getPendingAnime(): Flow<List<AnimeEntity>>

    @Query("SELECT * FROM animes WHERE statusUser = 'Abandonado'")
    fun getAbandonedAnime(): Flow<List<AnimeEntity>>

    @Query("SELECT * FROM animes WHERE statusUser = 'Planeado'")
    fun getPlannedAnime(): Flow<List<AnimeEntity>>

    @Query("UPDATE animes SET episodesWatched = :newEpisodesWatched WHERE malId = :animeId")
    suspend fun updateEpisodesWatched(animeId: Int, newEpisodesWatched: Int)

    @Query("UPDATE animes SET statusUser = :newStatus WHERE malId = :animeId")
    suspend fun updateAnimeStatus(animeId: Int, newStatus: String)
}
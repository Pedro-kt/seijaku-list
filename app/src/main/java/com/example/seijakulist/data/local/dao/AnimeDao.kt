package com.example.seijakulist.data.local.dao

import androidx.room.*
import com.example.seijakulist.data.local.entities.AnimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnime(anime: AnimeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAnimes(animes: List<AnimeEntity>)

    @Query("SELECT * FROM animes")
    fun getAllAnimes(): Flow<List<AnimeEntity>>

    @Query("DELETE FROM animes WHERE malId = :animeId")
    suspend fun deleteAnimeById(animeId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM animes WHERE malId = :animeId LIMIT 1)")
    fun isAnimeInList(animeId: Int): Flow<Boolean>
}
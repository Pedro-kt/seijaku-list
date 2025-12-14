package com.yumedev.seijakulist.data.repository

import com.yumedev.seijakulist.data.local.dao.AnimeDao
import com.yumedev.seijakulist.data.local.entities.AnimeEntity
import com.yumedev.seijakulist.data.mapper.local.toAnimeEntityDomain
import com.yumedev.seijakulist.domain.models.AnimeEntityDomain
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AnimeLocalRepository @Inject constructor(

    private val animeDao: AnimeDao

) {

    suspend fun insertAnime(anime: AnimeEntity) {
        animeDao.insertAnime(anime)
    }

    suspend fun insertAllAnimes(animes: List<AnimeEntity>) {
        animeDao.insertAllAnimes(animes)
    }

    suspend fun deleteAnimeById(animeId: Int) {
        animeDao.deleteAnimeById(animeId)
    }

    fun getAllAnimes(): Flow<List<AnimeEntity>> {
        return animeDao.getAllAnimes()
    }

    fun isAnimeInList(animeId: Int): Flow<Boolean> {
        return animeDao.isAnimeInList(animeId)
    }

    fun getAnimesStatusComplete(): Flow<List<AnimeEntity>> {
        return animeDao.getCompletedAnime()
    }

    fun getAnimesStatusWatching(): Flow<List<AnimeEntity>> {
        return animeDao.getWatchingAnime()
    }

    fun getAnimesStatusPending(): Flow<List<AnimeEntity>> {
        return animeDao.getPendingAnime()
    }

    fun getAnimesStatusAbandoned(): Flow<List<AnimeEntity>> {
        return animeDao.getAbandonedAnime()
    }

    fun getAnimesStatusPlanned(): Flow<List<AnimeEntity>> {
        return animeDao.getPlannedAnime()
    }

    suspend fun getAnimeById(animeId: Int): AnimeEntityDomain {

        val animeEntity = animeDao.getAnimeById(animeId)

        val anime = animeEntity.toAnimeEntityDomain()

        return anime
    }

    suspend fun updateAnime(anime: AnimeEntity) {
        animeDao.updateAnime(anime)
    }

    // Métodos para estadísticas
    fun getTotalAnimesCount(): Flow<Int> {
        return animeDao.getTotalAnimesCount()
    }

    fun getCompletedAnimesCount(): Flow<Int> {
        return animeDao.getCompletedAnimesCount()
    }

    fun getTotalEpisodesWatched(): Flow<Int> {
        return animeDao.getTotalEpisodesWatched()
    }

    fun getAllGenres(): Flow<List<String>> {
        return animeDao.getAllGenres()
    }
}
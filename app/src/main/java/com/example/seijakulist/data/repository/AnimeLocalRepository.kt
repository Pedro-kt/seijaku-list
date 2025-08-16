package com.example.seijakulist.data.repository

import com.example.seijakulist.data.local.dao.AnimeDao
import com.example.seijakulist.data.local.entities.AnimeEntity
import com.example.seijakulist.data.mapper.local.toAnimeEntityDomain
import com.example.seijakulist.domain.models.AnimeEntityDomain
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

    suspend fun updateEpisodesWatched(animeId: Int, newEpisodesWatched: Int) {
        animeDao.updateEpisodesWatched(animeId, newEpisodesWatched)
    }

    suspend fun updateAnimeStatus(animeId: Int, newStatus: String) {
        animeDao.updateAnimeStatus(animeId, newStatus)
    }
}
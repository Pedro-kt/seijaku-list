package com.yumedev.seijakulist.data.local.db


import androidx.room.Database
import androidx.room.RoomDatabase
import com.yumedev.seijakulist.data.local.dao.AnimeDao
import com.yumedev.seijakulist.data.local.dao.MangaDao
import com.yumedev.seijakulist.data.local.dao.SearchHistoryDao
import com.yumedev.seijakulist.data.local.dao.UserProfileDao
import com.yumedev.seijakulist.data.local.entities.AnimeEntity
import com.yumedev.seijakulist.data.local.entities.MangaEntity
import com.yumedev.seijakulist.data.local.entities.SearchHistoryEntity
import com.yumedev.seijakulist.data.local.entities.UserProfile

@Database(
    entities = [
        AnimeEntity::class,
        UserProfile::class,
        SearchHistoryEntity::class,
        MangaEntity::class
    ],
    version = 16,
    exportSchema = false
)
abstract class AnimeDatabase : RoomDatabase() {
    abstract fun animeDao(): AnimeDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun searchHistoryDao(): SearchHistoryDao
    abstract fun mangaDao(): MangaDao
}
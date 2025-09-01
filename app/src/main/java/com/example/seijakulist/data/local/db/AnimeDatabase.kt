package com.example.seijakulist.data.local.db


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.seijakulist.data.local.dao.AnimeDao
import com.example.seijakulist.data.local.dao.UserProfileDao
import com.example.seijakulist.data.local.entities.AnimeEntity
import com.example.seijakulist.data.local.entities.UserProfile

@Database(entities = [AnimeEntity::class, UserProfile::class], version = 7, exportSchema = false)
abstract class AnimeDatabase : RoomDatabase() {
    abstract fun animeDao(): AnimeDao
    abstract fun userProfileDao(): UserProfileDao
}
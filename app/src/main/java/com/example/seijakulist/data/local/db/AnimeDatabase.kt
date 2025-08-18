package com.example.seijakulist.data.local.db


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.seijakulist.data.local.dao.AnimeDao
import com.example.seijakulist.data.local.entities.AnimeEntity

@Database(entities = [AnimeEntity::class], version = 6, exportSchema = false)
abstract class AnimeDatabase : RoomDatabase() {
    abstract fun animeDao(): AnimeDao
}
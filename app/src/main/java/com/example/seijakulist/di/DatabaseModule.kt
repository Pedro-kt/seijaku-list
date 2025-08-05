package com.example.seijakulist.di

import android.content.Context
import androidx.room.Room
import com.example.seijakulist.data.local.db.AnimeDatabase
import com.example.seijakulist.data.local.dao.AnimeDao
import com.example.seijakulist.data.local.migration.MIGRATION_1_2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAnimeDatabase(@ApplicationContext appContext: Context): AnimeDatabase {
        return Room.databaseBuilder(
            appContext,
            AnimeDatabase::class.java,
            "anime_database"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Provides
    fun provideAnimeDao(db: AnimeDatabase): AnimeDao {
        return db.animeDao()
    }
}


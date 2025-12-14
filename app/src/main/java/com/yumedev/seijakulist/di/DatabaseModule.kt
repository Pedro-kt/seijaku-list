package com.yumedev.seijakulist.di

import android.content.Context
import androidx.room.Room
import com.yumedev.seijakulist.data.local.db.AnimeDatabase
import com.yumedev.seijakulist.data.local.dao.AnimeDao
import com.yumedev.seijakulist.data.local.dao.UserProfileDao
import com.yumedev.seijakulist.data.local.migration.MIGRATION_1_2
import com.yumedev.seijakulist.data.local.migration.MIGRATION_2_3
import com.yumedev.seijakulist.data.local.migration.MIGRATION_4_5
import com.yumedev.seijakulist.data.local.migration.MIGRATION_5_6
import com.yumedev.seijakulist.data.local.migration.MIGRATION_6_7
import com.yumedev.seijakulist.data.local.migration.MIGRATION_7_8
import com.yumedev.seijakulist.data.local.migration.MIGRATION_9_10
import com.yumedev.seijakulist.data.repository.UserProfileLocalRepository
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
            .addMigrations(MIGRATION_6_7, MIGRATION_7_8, MIGRATION_9_10)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideAnimeDao(db: AnimeDatabase): AnimeDao {
        return db.animeDao()
    }

    @Provides
    fun provideUserProfileDao(db: AnimeDatabase): UserProfileDao {
        return db.userProfileDao()
    }

}
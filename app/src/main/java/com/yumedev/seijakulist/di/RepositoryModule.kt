package com.yumedev.seijakulist.di

import com.yumedev.seijakulist.data.local.dao.MangaDao
import com.yumedev.seijakulist.data.local.dao.UserProfileDao
import com.yumedev.seijakulist.data.repository.FirestoreAnimeRepository
import com.yumedev.seijakulist.data.repository.FirestoreMangaRepository
import com.yumedev.seijakulist.data.repository.MangaLocalRepository
import com.yumedev.seijakulist.data.repository.UserProfileLocalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideUserProfileLocalRepository(
        userProfileDao: UserProfileDao
    ): UserProfileLocalRepository {
        return UserProfileLocalRepository(userProfileDao)
    }

    @Provides
    @Singleton
    fun provideFirestoreAnimeRepository(): FirestoreAnimeRepository {
        return FirestoreAnimeRepository()
    }

    @Provides
    @Singleton
    fun provideMangaLocalRepository(
        mangaDao: MangaDao
    ): MangaLocalRepository {
        return MangaLocalRepository(mangaDao)
    }

    @Provides
    @Singleton
    fun provideFirestoreMangaRepository(): FirestoreMangaRepository {
        return FirestoreMangaRepository()
    }
}
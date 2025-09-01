package com.example.seijakulist.di

import com.example.seijakulist.data.local.dao.UserProfileDao
import com.example.seijakulist.data.repository.UserProfileLocalRepository
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
}
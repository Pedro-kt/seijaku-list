package com.yumedev.seijakulist.di

import com.yumedev.seijakulist.ui.screens.configuration.SettingsViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule {

    @Provides
    @Singleton
    fun provideSettingsViewModel(): SettingsViewModel {
        return SettingsViewModel()
    }
}

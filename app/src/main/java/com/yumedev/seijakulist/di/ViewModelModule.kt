package com.yumedev.seijakulist.di

import android.content.Context
import com.yumedev.seijakulist.ui.screens.configuration.SettingsViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule {

    @Provides
    @Singleton
    fun provideSettingsViewModel(
        @ApplicationContext context: Context
    ): SettingsViewModel {
        return SettingsViewModel(context)
    }
}

package com.yumedev.seijakulist.di

import com.apollographql.apollo.ApolloClient
import com.yumedev.seijakulist.data.remote.api.AniListApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Apollo GraphQL Client para AniList API
    @Provides
    @Singleton
    fun provideAniListApiClient(): AniListApiClient {
        return AniListApiClient()
    }

    @Provides
    @Singleton
    fun provideApolloClient(apiClient: AniListApiClient): ApolloClient {
        return apiClient.apolloClient
    }

}
package com.example.seijakulist.di

import com.example.seijakulist.data.remote.api.JikanApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

//El app module dice a Hilt como construir el retrofit y la jikanApiService

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL: String = "https://api.jikan.moe/v4/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    @Provides
    @Singleton
    fun provideJikanApiService(retrofit: Retrofit): JikanApiService {

        return retrofit.create(JikanApiService::class.java)

    }

}
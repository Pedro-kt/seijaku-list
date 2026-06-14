package com.yumedev.seijakulist.data.remote.api

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.okHttpClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Cliente Apollo GraphQL para AniList API
 *
 * AniList API: https://graphql.anilist.co
 * Documentation: https://anilist.gitbook.io/anilist-apiv2-docs/
 * Rate Limit: 90 requests per minute
 */
@Singleton
class AniListApiClient @Inject constructor() {

    companion object {
        private const val BASE_URL = "https://graphql.anilist.co"
        private const val CONNECT_TIMEOUT = 30L
        private const val READ_TIMEOUT = 30L
    }

    /**
     * OkHttpClient configurado con:
     * - Logging interceptor para debug
     * - Timeouts configurados
     */
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        .build()

    /**
     * Apollo Client configurado para AniList
     *
     * Features:
     * - Normalized cache para mejor performance
     * - OkHttp client customizado
     * - Manejo automático de errores GraphQL
     */
    val apolloClient: ApolloClient = ApolloClient.Builder()
        .serverUrl(BASE_URL)
        .okHttpClient(okHttpClient)
        .build()
}

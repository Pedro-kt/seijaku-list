package com.example.seijakulist.data.repository

import com.example.seijakulist.data.remote.api.JikanApiService
import com.example.seijakulist.data.remote.models.SearchAnimeResponse
import javax.inject.Inject

class AnimeRepository @Inject constructor(

    private val ApiService: JikanApiService

) {

    suspend fun searchAnimes(query: String, page: Int?) : SearchAnimeResponse {

        return ApiService.searchAnimes(query, page)

    }
}
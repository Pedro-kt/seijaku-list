package com.example.seijakulist.data.repository

import android.util.Log
import com.example.seijakulist.data.mapper.toAnimeDetails
import com.example.seijakulist.data.remote.api.JikanApiService
import com.example.seijakulist.data.remote.models.AnimeDetailDto
import com.example.seijakulist.data.remote.models.AnimeDetailResponseDto
import com.example.seijakulist.data.remote.models.SearchAnimeResponse
import com.example.seijakulist.domain.models.AnimeDetail
import javax.inject.Inject

class AnimeRepository @Inject constructor(

    private val ApiService: JikanApiService

) {

    suspend fun searchAnimes(query: String, page: Int?) : SearchAnimeResponse {

        return ApiService.searchAnimes(query, page)

    }
    suspend fun getAnimeDetailsById(animeId: Int): AnimeDetail {
        try {
            val responseDto: AnimeDetailResponseDto = ApiService.getAnimeDetails(animeId)
            Log.d("AnimeRepo", "Respuesta DTO de la API (con envoltorio): $responseDto")

                // Accede al objeto "data" antes de mapear
            val animeDetailDto: AnimeDetailDto? = responseDto.data

                // Verifica si el DTO real del anime es nulo
            if (animeDetailDto == null) {
                throw Exception("API did not return anime data for ID $animeId")
            }

            val animeDetail: AnimeDetail = animeDetailDto.toAnimeDetails()
            Log.d("AnimeRepo", "AnimeDetail mapeado: $animeDetail")

            return animeDetail

        } catch (e: Exception) {
            Log.e("AnimeRepo", "Error al obtener detalles del anime: ${e.message}", e)
            throw Exception("Failed to fetch anime details from API for ID $animeId", e)
        }

    }

}
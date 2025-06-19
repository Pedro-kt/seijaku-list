package com.example.seijakulist.data.remote.api

import com.example.seijakulist.data.remote.models.AnimeDto
import com.example.seijakulist.data.remote.models.SearchAnimeResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

//Esto es la Interfaz de Retrofit

interface JikanApiService {

    //Esta peticion me devuelve una lista de animes
    @GET("anime")
    suspend fun searchAnimes(
        @Query("q") query: String,
        @Query("page") page: Int? = 1
    ): SearchAnimeResponse

    //Esta peticion es para darme los datos de un solo anime en especifico
    @GET("anime/{id}")
    suspend fun getAnimeDetails(
        @Path("id") malId: Int
    ): AnimeDto
}
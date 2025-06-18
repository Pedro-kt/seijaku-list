package com.example.seijakulist.data.remote.api

import com.example.seijakulist.data.remote.models.AnimeDto
import com.example.seijakulist.data.remote.models.SearchAnimeResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

//Anotaciones Retrofit, es para definir el tipo de peticion HTTP y la ruta deñ endpoint en este caso @GET
//Ruta del Endpoint,
//Parametros de consulta ( @Query ), para buscar un anime necesitamos un termino de busqueda ej: q=naruto, Retrofit permite definir estos parametros con @Query
//Funcion suspend, como es una operacion de red (asincronico) la funcion debe ser suspend
//Tipo de retorno, la funcion debe devolver la data class SearchAnimeResponse

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
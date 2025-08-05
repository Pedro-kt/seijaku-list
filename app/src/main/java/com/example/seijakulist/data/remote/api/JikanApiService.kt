package com.example.seijakulist.data.remote.api

import com.example.seijakulist.data.remote.models.AnimeCharactersResponseDto
import com.example.seijakulist.data.remote.models.AnimeDetailResponseDto
import com.example.seijakulist.data.remote.models.SearchAnimeResponse
import com.example.seijakulist.data.remote.models.anime_season_now.AnimeDetailSeasonNowResponseDto
import com.example.seijakulist.data.remote.models.anime_themes.AnimeThemesDto
import com.example.seijakulist.data.remote.models.character_detail.CharacterResponseDto
import com.example.seijakulist.data.remote.models.character_pictures.CharacterPicturesResponseDto
import com.example.seijakulist.domain.models.AnimeDetailSeasonNow
import com.example.seijakulist.domain.models.AnimeThemes
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
        @Path("id") malId: Int?
    ): AnimeDetailResponseDto

    @GET("anime/{id}/characters")
    suspend fun getAnimeCharacters(
        @Path("id") malId: Int?
    ): AnimeCharactersResponseDto

    @GET("characters/{id}/full")
    suspend fun getCharacterDetail(
        @Path("id") characterId: Int?
    ): CharacterResponseDto

    @GET("characters/{id}/pictures")
    suspend fun getCharacterPictures(
        @Path("id") characterId: Int?
    ): CharacterPicturesResponseDto

    @GET("seasons/now")
    suspend fun getAnimeSeasonNow(): SearchAnimeResponse

    @GET("top/anime")
    suspend fun getTopAnime(): SearchAnimeResponse

    @GET("seasons/upcoming")
    suspend fun getSeasonUpcoming(): SearchAnimeResponse

    @GET("anime/{id}/themes")
    suspend fun getAnimeThemes(
        @Path("id") malId: Int
    ): AnimeThemesDto
}
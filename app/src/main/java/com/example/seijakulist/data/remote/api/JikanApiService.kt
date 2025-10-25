package com.example.seijakulist.data.remote.api

import com.example.seijakulist.data.remote.models.AnimeCharactersResponseDto
import com.example.seijakulist.data.remote.models.AnimeDetailResponseDto
import com.example.seijakulist.data.remote.models.ProducerResponseDto
import com.example.seijakulist.data.remote.models.anime_random.AnimeCardResponseDto
import com.example.seijakulist.data.remote.models.SearchAnimeResponse
import com.example.seijakulist.data.remote.models.anime_themes.AnimeThemesDto
import com.example.seijakulist.data.remote.models.character_detail.CharacterResponseDto
import com.example.seijakulist.data.remote.models.character_pictures.CharacterPicturesResponseDto
import com.example.seijakulist.data.remote.models.genres.GenreResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

//Esto es la Interfaz de Retrofit

interface  JikanApiService {

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
    suspend fun getAnimeSeasonNow(
        @Query("sfw") sfw: Boolean = true
    ): SearchAnimeResponse

    @GET("top/anime")
    suspend fun getTopAnime(): SearchAnimeResponse

    @GET("seasons/upcoming")
    suspend fun getSeasonUpcoming(): SearchAnimeResponse

    @GET("anime/{id}/themes")
    suspend fun getAnimeThemes(
        @Path("id") malId: Int
    ): AnimeThemesDto

    @GET("random/anime")
    suspend fun getAnimeRandom(@Query("sfw") sfw: Boolean = true): AnimeCardResponseDto

    @GET("random/characters")
    suspend fun getCharacterRandom(): CharacterResponseDto

    @GET("schedules")
    suspend fun getAnimeSchedule(
        @Query("filter") filter: String,
        @Query("sfw") sfw: Boolean = true
    ): SearchAnimeResponse

    @GET("top/anime")
    suspend fun getTopAnimeFilter(
        @Query("type") filter: String,
        @Query("sfw") sfw: Boolean = true
    ): SearchAnimeResponse

    @GET("seasons/upcoming")
    suspend fun getSeasonUpcomingFilter(
        @Query("filter") filter: String,
        @Query("sfw") sfw: Boolean = true
    ) : SearchAnimeResponse

    @GET("producers/{id}/full")
    suspend fun getProducerDetail(
        @Path("id") producerId: Int
    ): ProducerResponseDto

    @GET("genres/anime")
    suspend fun getGenresAnime(): GenreResponse

    @GET("anime")
    suspend fun getAnimeByGenre(
        @Query("genres") genre: String
    ) : SearchAnimeResponse
}
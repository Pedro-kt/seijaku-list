package com.yumedev.seijakulist.data.remote.api

import com.yumedev.seijakulist.data.remote.models.AnimeCharactersResponseDto
import com.yumedev.seijakulist.data.remote.models.AnimeDetailResponseDto
import com.yumedev.seijakulist.data.remote.models.ProducerResponseDto
import com.yumedev.seijakulist.data.remote.models.anime_random.AnimeCardResponseDto
import com.yumedev.seijakulist.data.remote.models.SearchAnimeResponse
import com.yumedev.seijakulist.data.remote.models.anime_pictures.AnimePicturesResponseDto
import com.yumedev.seijakulist.data.remote.models.anime_themes.AnimeThemesDto
import com.yumedev.seijakulist.data.remote.models.anime_videos.AnimeVideosResponseDto
import com.yumedev.seijakulist.data.remote.models.anime_episodes.AnimeEpisodeDetailResponseDto
import com.yumedev.seijakulist.data.remote.models.anime_episodes.AnimeEpisodesResponseDto
import com.yumedev.seijakulist.data.remote.models.character_detail.CharacterResponseDto
import com.yumedev.seijakulist.data.remote.models.character_pictures.CharacterPicturesResponseDto
import com.yumedev.seijakulist.data.remote.models.genres.GenreResponse
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
        @Query("sfw") sfw: Boolean = true,
        @Query("page") page: Int? = 1
    ): SearchAnimeResponse

    @GET("top/anime")
    suspend fun getTopAnime(
        @Query("page") page: Int? = 1
    ): SearchAnimeResponse

    @GET("seasons/upcoming")
    suspend fun getSeasonUpcoming(
        @Query("page") page: Int? = 1
    ): SearchAnimeResponse

    @GET("anime/{id}/themes")
    suspend fun getAnimeThemes(
        @Path("id") malId: Int
    ): AnimeThemesDto

    @GET("anime/{id}/pictures")
    suspend fun getAnimePictures(
        @Path("id") malId: Int
    ): AnimePicturesResponseDto

    @GET("anime/{id}/videos")
    suspend fun getAnimeVideos(
        @Path("id") malId: Int
    ): AnimeVideosResponseDto

    @GET("anime/{id}/episodes")
    suspend fun getAnimeEpisodes(
        @Path("id") malId: Int,
        @Query("page") page: Int? = 1
    ): AnimeEpisodesResponseDto

    @GET("anime/{id}/episodes/{episode}")
    suspend fun getAnimeEpisodeDetail(
        @Path("id") animeId: Int,
        @Path("episode") episodeId: Int
    ): AnimeEpisodeDetailResponseDto

    @GET("random/anime")
    suspend fun getAnimeRandom(@Query("sfw") sfw: Boolean = true): AnimeCardResponseDto

    @GET("random/characters")
    suspend fun getCharacterRandom(): CharacterResponseDto

    @GET("schedules")
    suspend fun getAnimeSchedule(
        @Query("filter") filter: String,
        @Query("sfw") sfw: Boolean = true,
        @Query("page") page: Int? = 1
    ): SearchAnimeResponse

    @GET("top/anime")
    suspend fun getTopAnimeFilter(
        @Query("type") filter: String,
        @Query("sfw") sfw: Boolean = true,
        @Query("page") page: Int? = 1
    ): SearchAnimeResponse

    @GET("seasons/upcoming")
    suspend fun getSeasonUpcomingFilter(
        @Query("filter") filter: String,
        @Query("sfw") sfw: Boolean = true,
        @Query("page") page: Int? = 1
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
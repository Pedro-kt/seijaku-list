package com.example.seijakulist.data.repository

import android.util.Log
import com.example.seijakulist.data.local.dao.AnimeDao
import com.example.seijakulist.data.local.entities.AnimeEntity
import com.example.seijakulist.data.mapper.local.toAnimeDomain
import com.example.seijakulist.data.mapper.local.toAnimeEntity
import com.example.seijakulist.data.mapper.toAnimeCharactersDetail
import com.example.seijakulist.data.mapper.toAnimeDetails
import com.example.seijakulist.data.mapper.toCharacterDetail
import com.example.seijakulist.data.mapper.toCharacterPictures
import com.example.seijakulist.data.remote.api.JikanApiService
import com.example.seijakulist.data.remote.models.AnimeCharactersDto
import com.example.seijakulist.data.remote.models.AnimeCharactersResponseDto
import com.example.seijakulist.data.remote.models.AnimeDetailDto
import com.example.seijakulist.data.remote.models.AnimeDetailResponseDto
import com.example.seijakulist.data.remote.models.SearchAnimeResponse
import com.example.seijakulist.data.remote.models.StudiosDto
import com.example.seijakulist.data.remote.models.anime_season_now.AnimeDetailSeasonNowResponseDto
import com.example.seijakulist.domain.models.Anime
import com.example.seijakulist.domain.models.AnimeCharactersDetail
import com.example.seijakulist.domain.models.AnimeDetail
import com.example.seijakulist.domain.models.AnimeDetailSeasonNow
import com.example.seijakulist.domain.models.CharacterDetail
import com.example.seijakulist.domain.models.CharacterPictures
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AnimeRepository @Inject constructor(

    private val ApiService: JikanApiService,
    private val animeDao: AnimeDao

) {

    suspend fun searchAnimes(query: String, page: Int?): SearchAnimeResponse {

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

    suspend fun getAnimeCharactersById(animeId: Int): List<AnimeCharactersDetail> {
        val responseDto = ApiService.getAnimeCharacters(animeId)

        Log.d("REPO", "Personajes brutos desde la API: ${responseDto.data.size}")
        Log.d("REPO", "Contenido: ${responseDto.data.joinToString("\n")}")

        val animeCharacterDto = responseDto.data

        if (animeCharacterDto.isEmpty()) {
            throw Exception("API did not return characters for anime ID $animeId")
        }

        val characters = animeCharacterDto.toAnimeCharactersDetail()

        Log.d("REPO", "Personajes mapeados: ${characters.size}")
        return characters
    }

    suspend fun getCharacterDetailById(characterId: Int): CharacterDetail {
        val responseDto = ApiService.getCharacterDetail(characterId)

        Log.d("CHAR", "Personaje bruto desde la API: ${responseDto.data}")

        val characterFullDetailDto = responseDto.data

        if (characterFullDetailDto == null) {
            throw Exception("API did not return detail character data for ID $characterId")
        }

        val characterDetail = characterFullDetailDto.toCharacterDetail()

        Log.d("CHAR", "Personaje mapeado: $characterDetail")

        return characterDetail
    }

    suspend fun getCharacterPicturesById(characterId: Int): List<CharacterPictures> {
        val responseDto = ApiService.getCharacterPictures(characterId)

        Log.d("CharPicture", "Personaje bruto desde la API: ${responseDto.data}")

        val characterPicturesDto = responseDto.data

        val characterPictures = characterPicturesDto.toCharacterPictures()

        if (characterPictures.isEmpty()) {
            throw Exception("API did not return characters pictures for Id $characterId")
        }

        return characterPictures
    }

    suspend fun searchAnimeSeasonNow(): SearchAnimeResponse {

        return ApiService.getAnimeSeasonNow()

    }

    suspend fun searchTopAnimes(): SearchAnimeResponse {
        return ApiService.getTopAnime()
    }

    suspend fun searchAnimeSeasonUpcoming(): SearchAnimeResponse {
        return ApiService.getSeasonUpcoming()
    }

    //DB LOCAL

    suspend fun insertAnime(anime: AnimeEntity) {
        animeDao.insertAnime(anime)
    }

    suspend fun insertAllAnimes(animes: List<AnimeEntity>) {
        animeDao.insertAllAnimes(animes)
    }

    suspend fun deleteAnimeById(animeId: Int) {
        animeDao.deleteAnimeById(animeId)
    }

    fun getAllAnimes(): Flow<List<AnimeEntity>> {
        return animeDao.getAllAnimes()
    }

    fun isAnimeInList(animeId: Int): Flow<Boolean> {
        return animeDao.isAnimeInList(animeId)
    }


}

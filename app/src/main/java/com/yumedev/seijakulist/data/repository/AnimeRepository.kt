package com.yumedev.seijakulist.data.repository

import android.util.Log
import com.yumedev.seijakulist.data.mapper.toAnimeCharactersDetail
import com.yumedev.seijakulist.data.mapper.toAnimeDetails

import com.yumedev.seijakulist.data.mapper.toCharacterDetail
import com.yumedev.seijakulist.data.mapper.toCharacterPictures
import com.yumedev.seijakulist.data.mapper.toProducerDetail
import com.yumedev.seijakulist.data.remote.api.JikanApiService
import com.yumedev.seijakulist.data.remote.models.AnimeDetailDto
import com.yumedev.seijakulist.data.remote.models.AnimeDetailResponseDto
import com.yumedev.seijakulist.data.remote.models.anime_random.AnimeCardResponseDto
import com.yumedev.seijakulist.data.remote.models.SearchAnimeResponse
import com.yumedev.seijakulist.domain.models.AnimeCard
import com.yumedev.seijakulist.domain.models.AnimeCharactersDetail
import com.yumedev.seijakulist.domain.models.AnimeDetail
import com.yumedev.seijakulist.domain.models.AnimeThemes
import com.yumedev.seijakulist.domain.models.CharacterDetail
import com.yumedev.seijakulist.domain.models.CharacterPictures
import com.yumedev.seijakulist.domain.models.Genre
import com.yumedev.seijakulist.domain.models.ProducerDetail
import javax.inject.Inject

class AnimeRepository @Inject constructor(

    private val ApiService: JikanApiService,

) {

    suspend fun searchAnimes(query: String, page: Int?): SearchAnimeResponse {

        return ApiService.searchAnimes(query, page)

    }

    suspend fun getAnimeDetailsById(animeId: Int): AnimeDetail {
        try {
            val responseDto: AnimeDetailResponseDto = ApiService.getAnimeDetails(animeId)

            val animeDetailDto: AnimeDetailDto? = responseDto.data

            if (animeDetailDto == null) {
                throw Exception("API did not return anime data for ID $animeId")
            }

            val animeDetail: AnimeDetail = animeDetailDto.toAnimeDetails()

            return animeDetail

        } catch (e: Exception) {
            throw Exception("Failed to fetch anime details from API for ID $animeId", e)
        }

    }

    suspend fun getAnimeCharactersById(animeId: Int): List<AnimeCharactersDetail> {
        val responseDto = ApiService.getAnimeCharacters(animeId)

        val animeCharacterDto = responseDto.data

        if (animeCharacterDto.isEmpty()) {
            throw Exception("API did not return characters for anime ID $animeId")
        }

        val characters = animeCharacterDto.toAnimeCharactersDetail()

        return characters
    }

    suspend fun getCharacterDetailById(characterId: Int): CharacterDetail {
        val responseDto = ApiService.getCharacterDetail(characterId)

        val characterFullDetailDto = responseDto.data

        if (characterFullDetailDto == null) {
            throw Exception("API did not return detail character data for ID $characterId")
        }

        val characterDetail = characterFullDetailDto.toCharacterDetail()

        return characterDetail
    }

    suspend fun getCharacterPicturesById(characterId: Int): List<CharacterPictures> {
        val responseDto = ApiService.getCharacterPictures(characterId)

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

    suspend fun searchAnimeRandom(): AnimeCardResponseDto {
        return ApiService.getAnimeRandom()
    }

    suspend fun searchAnimeSchedule(day: String): SearchAnimeResponse {
        return ApiService.getAnimeSchedule(day)
    }

    suspend fun searchTopAnimeFilter(filter: String): SearchAnimeResponse {
        return ApiService.getTopAnimeFilter(filter)
    }

    suspend fun searchAnimeSeasonUpcomingFilter(filter: String): SearchAnimeResponse {
        return ApiService.getSeasonUpcomingFilter(filter)
    }

    suspend fun getAnimeThemesById(animeId: Int): AnimeThemes {

        val dto = ApiService.getAnimeThemes(animeId)

        return AnimeThemes(
            openings = dto.data.openings,
            endings = dto.data.endings
        )
    }

    suspend fun getCharacterRandom(): CharacterDetail {
        val responseDto = ApiService.getCharacterRandom()

        val characterFullDetailDto = responseDto.data

        if (characterFullDetailDto == null) {
            throw Exception("API did not return detail character data")
        }

        val characterDetail = characterFullDetailDto.toCharacterDetail()

        return characterDetail
    }

    suspend fun getProducerDetail(producerId: Int): ProducerDetail {
        val responseDto = ApiService.getProducerDetail(producerId)

        val producerDetailDto = responseDto.data

        val producerDetail = producerDetailDto.toProducerDetail()

        return producerDetail
    }

    suspend fun getGenresAnime(): List<Genre> {
        val response = ApiService.getGenresAnime()
        return response.data.map { genreDto ->
            Genre(
                malId = genreDto.malId,
                name = genreDto.name,
                url = genreDto.url,
                count = genreDto.count
            )
        }
    }

    suspend fun getAnimeByGenre(genre: String): List<AnimeCard> {

        val response = ApiService.getAnimeByGenre(genre)

        val animeDtoList = response.data

        val animeList: List<AnimeCard> = animeDtoList.map { animeDto ->
            AnimeCard(
                malId = animeDto?.malId ?: 0,
                title = animeDto?.title ?: "Título predeterminado",
                images = animeDto?.images?.webp?.largeImageUrl ?: "URL de imagen predeterminada",
                score = animeDto?.score ?: 0.0f,
                status = animeDto?.status ?: "Sin estado",
                genres = animeDto?.genres ?: emptyList(),
                year = (animeDto?.year ?: "N/A").toString(),
                episodes = (animeDto?.episodes ?: "N/A").toString()
            )
        }

        return animeList
    }
}
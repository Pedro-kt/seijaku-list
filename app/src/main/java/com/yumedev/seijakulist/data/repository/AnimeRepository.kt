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
import com.yumedev.seijakulist.domain.models.AnimeEpisode
import com.yumedev.seijakulist.domain.models.AnimeEpisodeDetail
import com.yumedev.seijakulist.domain.models.AnimeEpisodesPagination
import com.yumedev.seijakulist.domain.models.AnimeEpisodesResult
import com.yumedev.seijakulist.domain.models.AnimeEpisodeVideo
import com.yumedev.seijakulist.domain.models.AnimeMusicVideo
import com.yumedev.seijakulist.domain.models.AnimePicture
import com.yumedev.seijakulist.domain.models.AnimePromo
import com.yumedev.seijakulist.domain.models.AnimeThemes
import com.yumedev.seijakulist.domain.models.AnimeVideos
import com.yumedev.seijakulist.domain.models.CharacterDetail
import com.yumedev.seijakulist.domain.models.CharacterPictures
import com.yumedev.seijakulist.domain.models.AnimeRecommendation
import com.yumedev.seijakulist.domain.models.ForumTopic
import com.yumedev.seijakulist.domain.models.Genre
import com.yumedev.seijakulist.domain.models.HeroAnimeItem
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

    suspend fun searchAnimeSeasonNow(page: Int? = 1): SearchAnimeResponse {
        return ApiService.getAnimeSeasonNow(page = page)
    }

    suspend fun searchTopAnimes(page: Int? = 1): SearchAnimeResponse {
        return ApiService.getTopAnime(page = page)
    }

    suspend fun searchAnimeSeasonUpcoming(page: Int? = 1): SearchAnimeResponse {
        return ApiService.getSeasonUpcoming(page = page)
    }

    suspend fun searchAnimeRandom(): AnimeCardResponseDto {
        return ApiService.getAnimeRandom()
    }

    suspend fun searchAnimeSchedule(day: String, page: Int? = 1): SearchAnimeResponse {
        return ApiService.getAnimeSchedule(day, page = page)
    }

    suspend fun searchTopAnimeFilter(filter: String, page: Int? = 1): SearchAnimeResponse {
        return ApiService.getTopAnimeFilter(filter, page = page)
    }

    suspend fun searchAnimeSeasonUpcomingFilter(filter: String, page: Int? = 1): SearchAnimeResponse {
        return ApiService.getSeasonUpcomingFilter(filter, page = page)
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

    suspend fun getAnimePicturesById(animeId: Int): List<AnimePicture> {
        val response = ApiService.getAnimePictures(animeId)

        return response.data.map { dto ->
            AnimePicture(
                imageUrl = dto.jpg?.imageUrl ?: dto.webp?.imageUrl,
                smallImageUrl = dto.jpg?.smallImageUrl ?: dto.webp?.smallImageUrl,
                largeImageUrl = dto.jpg?.largeImageUrl ?: dto.webp?.largeImageUrl
            )
        }
    }

    suspend fun getAnimeVideosById(animeId: Int): AnimeVideos {
        val response = ApiService.getAnimeVideos(animeId)
        val data = response.data

        val promos = data.promo?.map { promo ->
            // Usar la mejor imagen disponible con fallback completo
            val thumbnailUrl = promo.trailer?.images?.let { images ->
                images.maximumImageUrl
                    ?: images.largeImageUrl
                    ?: images.mediumImageUrl
                    ?: images.smallImageUrl
                    ?: images.imageUrl
            }
            // Construir URL de YouTube si tenemos el ID
            val youtubeUrl = promo.trailer?.url
                ?: promo.trailer?.youtubeId?.let { "https://www.youtube.com/watch?v=$it" }

            AnimePromo(
                title = promo.title,
                youtubeId = promo.trailer?.youtubeId,
                youtubeUrl = youtubeUrl,
                thumbnailUrl = thumbnailUrl
            )
        } ?: emptyList()

        val episodes = data.episodes?.map { episode ->
            AnimeEpisodeVideo(
                malId = episode.malId,
                title = episode.title,
                episode = episode.episode,
                url = episode.url,
                imageUrl = episode.images?.jpg?.imageUrl
            )
        } ?: emptyList()

        val musicVideos = data.musicVideos?.map { mv ->
            // Usar la mejor imagen disponible con fallback completo
            val thumbnailUrl = mv.video?.images?.let { images ->
                images.maximumImageUrl
                    ?: images.largeImageUrl
                    ?: images.mediumImageUrl
                    ?: images.smallImageUrl
                    ?: images.imageUrl
            }
            // Construir URL de YouTube si tenemos el ID
            val youtubeUrl = mv.video?.url
                ?: mv.video?.youtubeId?.let { "https://www.youtube.com/watch?v=$it" }

            AnimeMusicVideo(
                title = mv.title,
                youtubeId = mv.video?.youtubeId,
                youtubeUrl = youtubeUrl,
                thumbnailUrl = thumbnailUrl,
                songTitle = mv.meta?.title,
                artist = mv.meta?.author
            )
        } ?: emptyList()

        return AnimeVideos(
            promos = promos,
            episodes = episodes,
            musicVideos = musicVideos
        )
    }

    suspend fun getAnimeEpisodesById(animeId: Int, page: Int = 1): AnimeEpisodesResult {
        val response = ApiService.getAnimeEpisodes(animeId, page)

        val episodes = response.data.map { dto ->
            AnimeEpisode(
                malId = dto.malId,
                url = dto.url,
                title = dto.title,
                titleJapanese = dto.titleJapanese,
                titleRomanji = dto.titleRomanji,
                aired = dto.aired,
                score = dto.score,
                filler = dto.filler,
                recap = dto.recap,
                forumUrl = dto.forumUrl
            )
        }

        val pagination = response.pagination?.let {
            AnimeEpisodesPagination(
                lastVisiblePage = it.lastVisiblePage,
                hasNextPage = it.hasNextPage
            )
        }

        return AnimeEpisodesResult(
            episodes = episodes,
            pagination = pagination
        )
    }

    suspend fun getAnimeEpisodeDetailById(animeId: Int, episodeId: Int): AnimeEpisodeDetail? {
        val response = ApiService.getAnimeEpisodeDetail(animeId, episodeId)

        return response.data?.let { dto ->
            AnimeEpisodeDetail(
                malId = dto.malId,
                url = dto.url,
                title = dto.title,
                titleJapanese = dto.titleJapanese,
                titleRomanji = dto.titleRomanji,
                duration = dto.duration,
                aired = dto.aired,
                filler = dto.filler,
                recap = dto.recap,
                synopsis = dto.synopsis
            )
        }
    }

    suspend fun getAnimeRecommendations(animeId: Int): List<AnimeRecommendation> {
        val response = ApiService.getAnimeRecommendations(animeId)
        return response.data.map { entry ->
            AnimeRecommendation(
                malId = entry.entry.malId,
                title = entry.entry.title ?: "Título desconocido",
                image = entry.entry.images?.webp?.largeImageUrl
                    ?: entry.entry.images?.jpg?.largeImageUrl ?: "",
                votes = entry.votes
            )
        }
    }

    suspend fun getAnimeForumTopics(animeId: Int): List<ForumTopic> {
        val response = ApiService.getAnimeForum(animeId)
        return response.data.map { dto ->
            ForumTopic(
                malId = dto.malId,
                url = dto.url ?: "",
                title = dto.title ?: "Sin título",
                date = dto.date ?: "",
                authorUsername = dto.authorUsername ?: "Anónimo",
                comments = dto.comments,
                lastCommentAuthor = dto.lastComment?.authorUsername,
                lastCommentDate = dto.lastComment?.date
            )
        }
    }

    suspend fun getWatchRecentEpisodes(): HeroAnimeItem? {
        val entry = ApiService.getWatchRecentEpisodes().data.randomOrNull()?.entry ?: return null
        return HeroAnimeItem(
            malId = entry.malId,
            title = entry.title ?: return null,
            imageUrl = entry.images?.webp?.largeImageUrl ?: entry.images?.jpg?.largeImageUrl ?: return null,
            label = "NUEVOS EPISODIOS"
        )
    }

    suspend fun getWatchRecentPromos(): HeroAnimeItem? {
        val promoEntry = ApiService.getWatchRecentPromos().data.randomOrNull() ?: return null
        val entry = promoEntry.entry
        val imageUrl = promoEntry.trailer?.images?.maximumImageUrl
            ?: promoEntry.trailer?.images?.largeImageUrl
            ?: entry.images?.webp?.largeImageUrl
            ?: entry.images?.jpg?.largeImageUrl
            ?: return null
        return HeroAnimeItem(
            malId = entry.malId,
            title = entry.title ?: return null,
            imageUrl = imageUrl,
            label = "PROMO",
            score = entry.score,
            year = entry.year?.toString(),
            status = entry.status,
            genres = entry.genres?.mapNotNull { it?.name }?.take(2) ?: emptyList(),
            episodes = entry.episodes
        )
    }

    suspend fun getTopClassicAnime(): HeroAnimeItem? {
        val randomPage = (1..4).random()
        val dto = ApiService.getTopAnime(page = randomPage).data?.randomOrNull() ?: return null
        return HeroAnimeItem(
            malId = dto.malId,
            title = dto.title ?: return null,
            imageUrl = dto.images?.webp?.largeImageUrl ?: dto.images?.jpg?.largeImageUrl ?: return null,
            label = "CLÁSICO",
            score = dto.score,
            year = dto.year?.toString(),
            status = dto.status,
            genres = dto.genres.mapNotNull { it?.name }.take(2),
            episodes = dto.episodes
        )
    }

    suspend fun getRecommendationForAnime(animeId: Int): HeroAnimeItem? {
        val entry = ApiService.getAnimeRecommendations(animeId).data.randomOrNull()?.entry ?: return null
        return HeroAnimeItem(
            malId = entry.malId,
            title = entry.title ?: return null,
            imageUrl = entry.images?.webp?.largeImageUrl ?: entry.images?.jpg?.largeImageUrl ?: return null,
            label = "PARA VOS",
            score = entry.score,
            year = entry.year?.toString(),
            status = entry.status,
            genres = entry.genres?.mapNotNull { it?.name }?.take(2) ?: emptyList(),
            episodes = entry.episodes
        )
    }

    suspend fun getUpcomingHeroItem(): HeroAnimeItem? {
        val dto = ApiService.getSeasonUpcoming().data?.randomOrNull() ?: return null
        return HeroAnimeItem(
            malId = dto.malId,
            title = dto.title ?: return null,
            imageUrl = dto.images?.webp?.largeImageUrl ?: dto.images?.jpg?.largeImageUrl ?: return null,
            label = "PRÓXIMAMENTE",
            score = dto.score,
            year = dto.year?.toString(),
            status = dto.status,
            genres = dto.genres.mapNotNull { it?.name }.take(2),
            episodes = dto.episodes
        )
    }
}
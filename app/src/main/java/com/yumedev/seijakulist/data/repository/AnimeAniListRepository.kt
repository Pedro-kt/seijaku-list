package com.yumedev.seijakulist.data.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.yumedev.seijakulist.data.mapper.toAnimeCard
import com.yumedev.seijakulist.data.mapper.toAnimeCharactersDetail
import com.yumedev.seijakulist.data.mapper.toAnimeDetail
import com.yumedev.seijakulist.data.remote.graphql.GetAnimeDetailsQuery
import com.yumedev.seijakulist.data.remote.graphql.GetCharacterDetailsQuery
import com.yumedev.seijakulist.data.remote.graphql.GetSeasonalAnimeQuery
import com.yumedev.seijakulist.data.remote.graphql.SearchAnimeQuery
import com.yumedev.seijakulist.data.remote.graphql.type.MediaFormat
import com.yumedev.seijakulist.data.remote.graphql.type.MediaSeason
import com.yumedev.seijakulist.data.remote.graphql.type.MediaSort
import com.yumedev.seijakulist.data.remote.graphql.type.MediaStatus
import com.yumedev.seijakulist.domain.models.AnimeCard
import com.yumedev.seijakulist.domain.models.AnimeCharactersDetail
import com.yumedev.seijakulist.domain.models.AnimeDetail
import com.yumedev.seijakulist.domain.models.AnimeRelationDomain
import com.yumedev.seijakulist.domain.models.CharacterDetail
import com.yumedev.seijakulist.domain.models.MangaRelationDomain
import com.yumedev.seijakulist.domain.models.VoiceActorDomain
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository para interactuar con AniList GraphQL API
 *
 * Este repository maneja todas las operaciones relacionadas con anime usando AniList GraphQL.
 * Utiliza Apollo Client para ejecutar queries y mutations.
 *
 * @param apolloClient Cliente Apollo configurado para AniList API
 */
@Singleton
class AnimeAniListRepository @Inject constructor(
    private val apolloClient: ApolloClient
) {

    companion object {
        private const val DEFAULT_PER_PAGE = 20
        private const val MAX_CHARACTERS = 50
    }

    /**
     * Busca anime por query de búsqueda
     *
     * @param query Texto de búsqueda
     * @param page Número de página (default: 1)
     * @param perPage Elementos por página (default: 20)
     * @return Lista de AnimeCard
     */
    suspend fun searchAnime(
        query: String,
        page: Int = 1,
        perPage: Int = DEFAULT_PER_PAGE
    ): List<AnimeCard> {
        val response = apolloClient.query(
            SearchAnimeQuery(
                page = Optional.present(page),
                perPage = Optional.present(perPage),
                search = Optional.present(query),
                sort = Optional.present(listOf(MediaSort.POPULARITY_DESC))
            )
        ).execute()

        if (response.hasErrors()) {
            throw Exception("GraphQL errors: ${response.errors?.joinToString { it.message }}")
        }

        val media = response.data?.Page?.media?.filterNotNull() ?: emptyList()
        return media.map { it.toAnimeCard() }
    }

    /**
     * Obtiene detalles de un anime por ID
     *
     * @param animeId ID de AniList (si está disponible)
     * @param malId MyAnimeList ID (fallback si no hay AniList ID)
     * @return AnimeDetail con información completa
     */
    suspend fun getAnimeDetailsById(animeId: Int? = null, malId: Int? = null): AnimeDetail {
        if (animeId == null && malId == null) {
            throw IllegalArgumentException("Se requiere animeId o malId")
        }

        val response = apolloClient.query(
            GetAnimeDetailsQuery(
                id = Optional.presentIfNotNull(animeId),
                idMal = Optional.presentIfNotNull(malId)
            )
        ).execute()

        if (response.hasErrors()) {
            throw Exception("GraphQL errors: ${response.errors?.joinToString { it.message }}")
        }

        val media = response.data?.Media
            ?: throw Exception("No se encontró anime con ID: $animeId / MAL ID: $malId")

        return media.toAnimeDetail()
    }

    /**
     * Obtiene personajes de un anime
     *
     * @param animeId ID de AniList
     * @param malId MyAnimeList ID (alternativo)
     * @return Lista de AnimeCharactersDetail
     */
    suspend fun getAnimeCharactersById(animeId: Int? = null, malId: Int? = null): List<AnimeCharactersDetail> {
        if (animeId == null && malId == null) {
            throw IllegalArgumentException("Se requiere animeId o malId")
        }

        val response = apolloClient.query(
            GetAnimeDetailsQuery(
                id = Optional.presentIfNotNull(animeId),
                idMal = Optional.presentIfNotNull(malId)
            )
        ).execute()

        if (response.hasErrors()) {
            throw Exception("GraphQL errors: ${response.errors?.joinToString { it.message }}")
        }

        val characters = response.data?.Media?.characters?.edges?.filterNotNull() ?: emptyList()
        return characters.map { it.toAnimeCharactersDetail() }
    }

    /**
     * Obtiene detalles de un personaje por ID
     *
     * @param characterId ID del personaje en AniList
     * @return CharacterDetail con información completa
     */
    suspend fun getCharacterDetailById(characterId: Int): CharacterDetail {
        val response = apolloClient.query(
            GetCharacterDetailsQuery(
                id = Optional.present(characterId)
            )
        ).execute()

        if (response.hasErrors()) {
            throw Exception("GraphQL errors: ${response.errors?.joinToString { it.message }}")
        }

        val character = response.data?.Character
            ?: throw Exception("No se encontró personaje con ID: $characterId")

        val charFields = character.characterFields

        // Mapear voice actors
        val voiceActors = character.media?.edges?.filterNotNull()
            ?.flatMap { edge ->
                edge.voiceActorRoles?.filterNotNull()?.mapNotNull { voiceActorRole ->
                    val actor = voiceActorRole.voiceActor ?: return@mapNotNull null
                    VoiceActorDomain(
                        personId = actor.id,
                        name = actor.name?.full ?: actor.name?.native ?: "",
                        imageUrl = actor.image?.large ?: actor.image?.medium ?: "",
                        language = actor.languageV2 ?: ""
                    )
                } ?: emptyList()
            }
            ?.distinctBy { it.personId } // Eliminar duplicados
            ?: emptyList()

        // Mapear relaciones de anime y manga
        val mediaEdges = character.media?.edges?.filterNotNull() ?: emptyList()
        val animeRelations = mediaEdges
            .filter { edge -> edge.node?.type?.name == "ANIME" }
            .mapNotNull { edge ->
                val node = edge.node ?: return@mapNotNull null
                AnimeRelationDomain(
                    malId = node.idMal ?: 0,
                    title = node.title?.romaji ?: node.title?.english ?: node.title?.native ?: "",
                    imageUrl = node.coverImage?.large ?: node.coverImage?.medium ?: "",
                    role = when (edge.characterRole?.name) {
                        "MAIN" -> "Principal"
                        "SUPPORTING" -> "Secundario"
                        "BACKGROUND" -> "Trasfondo"
                        else -> edge.characterRole?.name ?: ""
                    }
                )
            }

        val mangaRelations = mediaEdges
            .filter { edge -> edge.node?.type?.name == "MANGA" }
            .mapNotNull { edge ->
                val node = edge.node ?: return@mapNotNull null
                MangaRelationDomain(
                    malId = node.idMal ?: 0,
                    title = node.title?.romaji ?: node.title?.english ?: node.title?.native ?: "",
                    imageUrl = node.coverImage?.large ?: node.coverImage?.medium ?: ""
                )
            }

        val result = CharacterDetail(
            characterId = charFields.id,
            nameCharacter = charFields.name?.full ?: charFields.name?.native ?: "",
            nameKanjiCharacter = charFields.name?.native ?: "",
            descriptionCharacter = charFields.description ?: "",
            images = charFields.image?.large ?: "",
            favorites = charFields.favourites ?: 0,
            nicknames = charFields.name?.alternative?.filterNotNull() ?: emptyList(),
            voiceActors = voiceActors,
            animeRelations = animeRelations,
            mangaRelations = mangaRelations
        )

        android.util.Log.d("AniListRepo", "Character loaded: ${result.nameCharacter}")
        android.util.Log.d("AniListRepo", "Voice Actors: ${voiceActors.size}")
        android.util.Log.d("AniListRepo", "Anime Relations: ${animeRelations.size}")
        android.util.Log.d("AniListRepo", "Manga Relations: ${mangaRelations.size}")
        android.util.Log.d("AniListRepo", "Media edges total: ${mediaEdges.size}")

        return result
    }

    /**
     * Obtiene anime de una temporada específica
     *
     * @param season Temporada (WINTER, SPRING, SUMMER, FALL)
     * @param year Año de la temporada
     * @param page Número de página
     * @param perPage Elementos por página
     * @return Lista de AnimeCard
     */
    suspend fun getSeasonalAnime(
        season: MediaSeason,
        year: Int,
        page: Int = 1,
        perPage: Int = DEFAULT_PER_PAGE
    ): List<AnimeCard> {
        val response = apolloClient.query(
            GetSeasonalAnimeQuery(
                season = season,
                seasonYear = year,
                page = Optional.present(page),
                perPage = Optional.present(perPage)
            )
        ).execute()

        if (response.hasErrors()) {
            throw Exception("GraphQL errors: ${response.errors?.joinToString { it.message }}")
        }

        val media = response.data?.Page?.media?.filterNotNull() ?: emptyList()
        return media.map { it.toAnimeCard() }
    }

    /**
     * Obtiene los anime más populares
     *
     * @param page Número de página
     * @param perPage Elementos por página
     * @return Lista de AnimeCard
     */
    suspend fun getTopAnime(
        page: Int = 1,
        perPage: Int = DEFAULT_PER_PAGE
    ): List<AnimeCard> {
        val response = apolloClient.query(
            SearchAnimeQuery(
                page = Optional.present(page),
                perPage = Optional.present(perPage),
                sort = Optional.present(listOf(MediaSort.SCORE_DESC, MediaSort.POPULARITY_DESC))
            )
        ).execute()

        if (response.hasErrors()) {
            throw Exception("GraphQL errors: ${response.errors?.joinToString { it.message }}")
        }

        val media = response.data?.Page?.media?.filterNotNull() ?: emptyList()
        return media.map { it.toAnimeCard() }
    }

    /**
     * Obtiene anime que están actualmente en emisión
     *
     * @param page Número de página
     * @param perPage Elementos por página
     * @return Lista de AnimeCard
     */
    suspend fun getAiringAnime(
        page: Int = 1,
        perPage: Int = DEFAULT_PER_PAGE
    ): List<AnimeCard> {
        val response = apolloClient.query(
            SearchAnimeQuery(
                page = Optional.present(page),
                perPage = Optional.present(perPage),
                status = Optional.present(MediaStatus.RELEASING),
                sort = Optional.present(listOf(MediaSort.POPULARITY_DESC))
            )
        ).execute()

        if (response.hasErrors()) {
            throw Exception("GraphQL errors: ${response.errors?.joinToString { it.message }}")
        }

        val media = response.data?.Page?.media?.filterNotNull() ?: emptyList()
        return media.map { it.toAnimeCard() }
    }

    /**
     * Obtiene anime por formato (TV, MOVIE, OVA, etc.)
     *
     * @param format Formato del anime
     * @param page Número de página
     * @param perPage Elementos por página
     * @return Lista de AnimeCard
     */
    suspend fun getAnimeByFormat(
        format: MediaFormat,
        page: Int = 1,
        perPage: Int = DEFAULT_PER_PAGE
    ): List<AnimeCard> {
        val response = apolloClient.query(
            SearchAnimeQuery(
                page = Optional.present(page),
                perPage = Optional.present(perPage),
                format = Optional.present(format),
                sort = Optional.present(listOf(MediaSort.POPULARITY_DESC))
            )
        ).execute()

        if (response.hasErrors()) {
            throw Exception("GraphQL errors: ${response.errors?.joinToString { it.message }}")
        }

        val media = response.data?.Page?.media?.filterNotNull() ?: emptyList()
        return media.map { it.toAnimeCard() }
    }

    /**
     * Obtiene anime por género
     *
     * @param genre Nombre del género
     * @param page Número de página
     * @param perPage Elementos por página
     * @return Lista de AnimeCard
     */
    suspend fun getAnimeByGenre(
        genre: String,
        page: Int = 1,
        perPage: Int = DEFAULT_PER_PAGE
    ): List<AnimeCard> {
        val response = apolloClient.query(
            SearchAnimeQuery(
                page = Optional.present(page),
                perPage = Optional.present(perPage),
                genre = Optional.present(genre),
                sort = Optional.present(listOf(MediaSort.POPULARITY_DESC))
            )
        ).execute()

        if (response.hasErrors()) {
            throw Exception("GraphQL errors: ${response.errors?.joinToString { it.message }}")
        }

        val media = response.data?.Page?.media?.filterNotNull() ?: emptyList()
        return media.map { it.toAnimeCard() }
    }

    /**
     * Obtiene anime trending (en tendencia)
     *
     * @param page Número de página
     * @param perPage Elementos por página
     * @return Lista de AnimeCard
     */
    suspend fun getTrendingAnime(
        page: Int = 1,
        perPage: Int = DEFAULT_PER_PAGE
    ): List<AnimeCard> {
        val response = apolloClient.query(
            SearchAnimeQuery(
                page = Optional.present(page),
                perPage = Optional.present(perPage),
                sort = Optional.present(listOf(MediaSort.TRENDING_DESC, MediaSort.POPULARITY_DESC))
            )
        ).execute()

        if (response.hasErrors()) {
            throw Exception("GraphQL errors: ${response.errors?.joinToString { it.message }}")
        }

        val media = response.data?.Page?.media?.filterNotNull() ?: emptyList()
        return media.map { it.toAnimeCard() }
    }

    /**
     * Obtiene recomendaciones para un anime
     *
     * @param animeId ID de AniList
     * @param malId MyAnimeList ID (alternativo)
     * @return Lista de AnimeRecommendation
     */
    suspend fun getAnimeRecommendations(animeId: Int? = null, malId: Int? = null): List<com.yumedev.seijakulist.domain.models.AnimeRecommendation> {
        if (animeId == null && malId == null) {
            throw IllegalArgumentException("Se requiere animeId o malId")
        }

        val response = apolloClient.query(
            GetAnimeDetailsQuery(
                id = Optional.presentIfNotNull(animeId),
                idMal = Optional.presentIfNotNull(malId)
            )
        ).execute()

        if (response.hasErrors()) {
            throw Exception("GraphQL errors: ${response.errors?.joinToString { it.message }}")
        }

        val recommendations = response.data?.Media?.recommendations?.nodes?.filterNotNull() ?: emptyList()

        return recommendations.mapNotNull { rec ->
            val media = rec.mediaRecommendation ?: return@mapNotNull null
            com.yumedev.seijakulist.domain.models.AnimeRecommendation(
                malId = media.idMal ?: 0,
                title = media.title?.romaji ?: media.title?.english ?: media.title?.native ?: "",
                image = media.coverImage?.large ?: media.coverImage?.extraLarge ?: "",
                votes = rec.rating ?: 0
            )
        }
    }

    /**
     * Obtiene anime por año (requiere también especificar temporada)
     * Para búsqueda por año sin temporada específica, se recomienda usar searchAnimeAdvanced
     *
     * @param season Temporada
     * @param year Año
     * @param page Número de página
     * @param perPage Elementos por página
     * @return Lista de AnimeCard
     */
    suspend fun getAnimeBySeasonAndYear(
        season: MediaSeason,
        year: Int,
        page: Int = 1,
        perPage: Int = DEFAULT_PER_PAGE
    ): List<AnimeCard> {
        return getSeasonalAnime(season, year, page, perPage)
    }

    /**
     * Obtiene anime próximos a estrenarse (upcoming)
     *
     * @param page Número de página
     * @param perPage Elementos por página
     * @return Lista de AnimeCard
     */
    suspend fun getUpcomingAnime(
        page: Int = 1,
        perPage: Int = DEFAULT_PER_PAGE
    ): List<AnimeCard> {
        val response = apolloClient.query(
            SearchAnimeQuery(
                page = Optional.present(page),
                perPage = Optional.present(perPage),
                status = Optional.present(MediaStatus.NOT_YET_RELEASED),
                sort = Optional.present(listOf(MediaSort.POPULARITY_DESC))
            )
        ).execute()

        if (response.hasErrors()) {
            throw Exception("GraphQL errors: ${response.errors?.joinToString { it.message }}")
        }

        val media = response.data?.Page?.media?.filterNotNull() ?: emptyList()
        return media.map { it.toAnimeCard() }
    }

    /**
     * Obtiene episodios de streaming de un anime
     *
     * AniList proporciona streamingEpisodes que son enlaces a sitios de streaming.
     * No incluye información tan detallada como Jikan (score, filler, recap, etc).
     *
     * @param animeId ID de AniList (opcional)
     * @param malId MyAnimeList ID (opcional, usado como fallback)
     * @return Lista de AnimeEpisode mapeados desde streamingEpisodes
     */
    suspend fun getAnimeEpisodesById(animeId: Int? = null, malId: Int? = null): List<com.yumedev.seijakulist.domain.models.AnimeEpisode> {
        if (animeId == null && malId == null) {
            throw IllegalArgumentException("Se requiere animeId o malId")
        }

        val response = apolloClient.query(
            GetAnimeDetailsQuery(
                id = Optional.presentIfNotNull(animeId),
                idMal = Optional.presentIfNotNull(malId)
            )
        ).execute()

        if (response.hasErrors()) {
            throw Exception("GraphQL errors: ${response.errors?.joinToString { it.message }}")
        }

        val media = response.data?.Media
            ?: throw Exception("No se encontró anime con ID: $animeId / MAL ID: $malId")

        // Mapear streamingEpisodes a AnimeEpisode
        return media.streamingEpisodes?.filterNotNull()?.mapIndexed { index, ep ->
            com.yumedev.seijakulist.domain.models.AnimeEpisode(
                malId = index + 1, // Usar el índice como ID ya que AniList no proporciona IDs de episodios
                url = ep.url,
                title = ep.title,
                titleJapanese = null, // AniList no proporciona título japonés en streamingEpisodes
                titleRomanji = null, // AniList no proporciona título romanizado en streamingEpisodes
                aired = null, // AniList no proporciona fecha de emisión en streamingEpisodes
                score = null, // AniList no proporciona score de episodios
                filler = null, // AniList no proporciona información de filler
                recap = null, // AniList no proporciona información de recap
                forumUrl = null // AniList no proporciona forum URL
            )
        } ?: emptyList()
    }

    /**
     * Obtiene anime completados/finalizados
     *
     * @param page Número de página
     * @param perPage Elementos por página
     * @return Lista de AnimeCard
     */
    suspend fun getFinishedAnime(
        page: Int = 1,
        perPage: Int = DEFAULT_PER_PAGE
    ): List<AnimeCard> {
        val response = apolloClient.query(
            SearchAnimeQuery(
                page = Optional.present(page),
                perPage = Optional.present(perPage),
                status = Optional.present(MediaStatus.FINISHED),
                sort = Optional.present(listOf(MediaSort.POPULARITY_DESC))
            )
        ).execute()

        if (response.hasErrors()) {
            throw Exception("GraphQL errors: ${response.errors?.joinToString { it.message }}")
        }

        val media = response.data?.Page?.media?.filterNotNull() ?: emptyList()
        return media.map { it.toAnimeCard() }
    }

    /**
     * Búsqueda avanzada de anime con múltiples filtros
     * Nota: Para búsqueda por temporada/año usa getSeasonalAnime() o getAnimeBySeasonAndYear()
     *
     * @param query Texto de búsqueda (opcional)
     * @param format Formato (opcional)
     * @param status Estado (opcional)
     * @param genre Género (opcional)
     * @param sort Ordenamiento (opcional)
     * @param page Número de página
     * @param perPage Elementos por página
     * @return Lista de AnimeCard
     */
    suspend fun searchAnimeAdvanced(
        query: String? = null,
        format: MediaFormat? = null,
        status: MediaStatus? = null,
        genre: String? = null,
        sort: List<MediaSort> = listOf(MediaSort.POPULARITY_DESC),
        page: Int = 1,
        perPage: Int = DEFAULT_PER_PAGE
    ): List<AnimeCard> {
        val response = apolloClient.query(
            SearchAnimeQuery(
                page = Optional.present(page),
                perPage = Optional.present(perPage),
                search = Optional.presentIfNotNull(query),
                format = Optional.presentIfNotNull(format),
                status = Optional.presentIfNotNull(status),
                genre = Optional.presentIfNotNull(genre),
                sort = Optional.present(sort)
            )
        ).execute()

        if (response.hasErrors()) {
            throw Exception("GraphQL errors: ${response.errors?.joinToString { it.message }}")
        }

        val media = response.data?.Page?.media?.filterNotNull() ?: emptyList()
        return media.map { it.toAnimeCard() }
    }

    // ========== HERO SECTION METHODS ==========

    /**
     * Obtiene un anime aleatorio de los más populares para Hero Section
     *
     * @return HeroAnimeItem o null
     */
    suspend fun getTopClassicAnime(): com.yumedev.seijakulist.domain.models.HeroAnimeItem? {
        val randomPage = (1..4).random()
        val response = apolloClient.query(
            SearchAnimeQuery(
                page = Optional.present(randomPage),
                perPage = Optional.present(20),
                sort = Optional.present(listOf(MediaSort.SCORE_DESC, MediaSort.POPULARITY_DESC))
            )
        ).execute()

        if (response.hasErrors()) {
            return null
        }

        val media = response.data?.Page?.media?.filterNotNull()?.randomOrNull() ?: return null
        val fields = media.mediaFields

        return com.yumedev.seijakulist.domain.models.HeroAnimeItem(
            malId = fields.idMal ?: 0,
            title = fields.title?.romaji ?: fields.title?.english ?: return null,
            imageUrl = fields.bannerImage ?: fields.coverImage?.extraLarge ?: fields.coverImage?.large ?: return null,
            label = "CLÁSICO",
            score = (fields.averageScore ?: 0) / 10.0f,
            year = fields.seasonYear?.toString(),
            status = fields.status?.name,
            genres = fields.genres?.filterNotNull()?.take(2) ?: emptyList(),
            episodes = fields.episodes
        )
    }

    /**
     * Obtiene un anime próximo a estrenarse para Hero Section
     *
     * @return HeroAnimeItem o null
     */
    suspend fun getUpcomingHeroItem(): com.yumedev.seijakulist.domain.models.HeroAnimeItem? {
        val response = apolloClient.query(
            SearchAnimeQuery(
                page = Optional.present(1),
                perPage = Optional.present(20),
                status = Optional.present(MediaStatus.NOT_YET_RELEASED),
                sort = Optional.present(listOf(MediaSort.POPULARITY_DESC))
            )
        ).execute()

        if (response.hasErrors()) {
            return null
        }

        val media = response.data?.Page?.media?.filterNotNull()?.randomOrNull() ?: return null
        val fields = media.mediaFields

        return com.yumedev.seijakulist.domain.models.HeroAnimeItem(
            malId = fields.idMal ?: 0,
            title = fields.title?.romaji ?: fields.title?.english ?: return null,
            imageUrl = fields.bannerImage ?: fields.coverImage?.extraLarge ?: fields.coverImage?.large ?: return null,
            label = "PRÓXIMAMENTE",
            score = (fields.averageScore ?: 0) / 10.0f,
            year = fields.seasonYear?.toString(),
            status = fields.status?.name,
            genres = fields.genres?.filterNotNull()?.take(2) ?: emptyList(),
            episodes = fields.episodes
        )
    }

    /**
     * Obtiene un anime en tendencia para Hero Section
     *
     * @return HeroAnimeItem o null
     */
    suspend fun getTrendingHeroItem(): com.yumedev.seijakulist.domain.models.HeroAnimeItem? {
        val response = apolloClient.query(
            SearchAnimeQuery(
                page = Optional.present(1),
                perPage = Optional.present(20),
                sort = Optional.present(listOf(MediaSort.TRENDING_DESC))
            )
        ).execute()

        if (response.hasErrors()) {
            return null
        }

        val media = response.data?.Page?.media?.filterNotNull()?.randomOrNull() ?: return null
        val fields = media.mediaFields

        return com.yumedev.seijakulist.domain.models.HeroAnimeItem(
            malId = fields.idMal ?: 0,
            title = fields.title?.romaji ?: fields.title?.english ?: return null,
            imageUrl = fields.bannerImage ?: fields.coverImage?.extraLarge ?: fields.coverImage?.large ?: return null,
            label = "TENDENCIA",
            score = (fields.averageScore ?: 0) / 10.0f,
            year = fields.seasonYear?.toString(),
            status = fields.status?.name,
            genres = fields.genres?.filterNotNull()?.take(2) ?: emptyList(),
            episodes = fields.episodes
        )
    }

    /**
     * Obtiene una recomendación para un anime específico (Hero Section)
     *
     * @param animeId ID del anime
     * @return HeroAnimeItem o null
     */
    suspend fun getRecommendationForAnime(animeId: Int): com.yumedev.seijakulist.domain.models.HeroAnimeItem? {
        val response = apolloClient.query(
            GetAnimeDetailsQuery(
                id = Optional.present(animeId),
                idMal = Optional.absent()
            )
        ).execute()

        if (response.hasErrors()) {
            return null
        }

        val recommendation = response.data?.Media?.recommendations?.nodes
            ?.filterNotNull()
            ?.randomOrNull() ?: return null

        val media = recommendation.mediaRecommendation ?: return null

        return com.yumedev.seijakulist.domain.models.HeroAnimeItem(
            malId = media.idMal ?: 0,
            title = media.title?.romaji ?: media.title?.english ?: return null,
            imageUrl = media.bannerImage ?: media.coverImage?.extraLarge ?: media.coverImage?.large ?: return null,
            label = "PARA VOS",
            score = (media.averageScore ?: 0) / 10.0f,
            year = media.seasonYear?.toString(),
            status = media.status?.name,
            genres = media.genres?.filterNotNull()?.take(2) ?: emptyList(),
            episodes = media.episodes
        )
    }

    /**
     * Obtiene un anime que está actualmente en emisión (Hero Section)
     *
     * @return HeroAnimeItem o null
     */
    suspend fun getCurrentlyAiringHeroItem(): com.yumedev.seijakulist.domain.models.HeroAnimeItem? {
        val response = apolloClient.query(
            SearchAnimeQuery(
                page = Optional.present(1),
                perPage = Optional.present(20),
                status = Optional.present(MediaStatus.RELEASING),
                sort = Optional.present(listOf(MediaSort.POPULARITY_DESC))
            )
        ).execute()

        if (response.hasErrors()) {
            return null
        }

        val media = response.data?.Page?.media?.filterNotNull()?.randomOrNull() ?: return null
        val fields = media.mediaFields

        return com.yumedev.seijakulist.domain.models.HeroAnimeItem(
            malId = fields.idMal ?: 0,
            title = fields.title?.romaji ?: fields.title?.english ?: return null,
            imageUrl = fields.bannerImage ?: fields.coverImage?.extraLarge ?: fields.coverImage?.large ?: return null,
            label = "AL AIRE",
            score = (fields.averageScore ?: 0) / 10.0f,
            year = fields.seasonYear?.toString(),
            status = fields.status?.name,
            genres = fields.genres?.filterNotNull()?.take(2) ?: emptyList(),
            episodes = fields.episodes
        )
    }

    // ========== ADDITIONAL METHODS ==========

    /**
     * Obtiene videos del anime (trailer y episodios de streaming)
     *
     * Nota: AniList solo proporciona 1 trailer y episodios de streaming.
     * NO incluye music videos como Jikan.
     *
     * @param animeId ID de AniList
     * @param malId MyAnimeList ID (alternativo)
     * @return AnimeVideos con trailer y episodios
     */
    suspend fun getAnimeVideosById(animeId: Int? = null, malId: Int? = null): com.yumedev.seijakulist.domain.models.AnimeVideos {
        if (animeId == null && malId == null) {
            throw IllegalArgumentException("Se requiere animeId o malId")
        }

        val response = apolloClient.query(
            GetAnimeDetailsQuery(
                id = Optional.presentIfNotNull(animeId),
                idMal = Optional.presentIfNotNull(malId)
            )
        ).execute()

        if (response.hasErrors()) {
            throw Exception("GraphQL errors: ${response.errors?.joinToString { it.message }}")
        }

        val media = response.data?.Media
            ?: throw Exception("No se encontró anime con ID: $animeId / MAL ID: $malId")

        val fields = media.mediaFields

        // Mapear trailer a promo
        val promos = fields.trailer?.let { trailer ->
            listOf(
                com.yumedev.seijakulist.domain.models.AnimePromo(
                    title = "Trailer Oficial",
                    youtubeId = if (trailer.site?.lowercase() == "youtube") trailer.id else null,
                    youtubeUrl = when (trailer.site?.lowercase()) {
                        "youtube" -> "https://www.youtube.com/watch?v=${trailer.id}"
                        "dailymotion" -> "https://www.dailymotion.com/video/${trailer.id}"
                        else -> null
                    },
                    thumbnailUrl = when (trailer.site?.lowercase()) {
                        "youtube" -> "https://img.youtube.com/vi/${trailer.id}/maxresdefault.jpg"
                        else -> trailer.thumbnail
                    }
                )
            )
        } ?: emptyList()

        // Mapear streaming episodes a episode videos
        val episodes = media.streamingEpisodes?.filterNotNull()?.map { ep ->
            com.yumedev.seijakulist.domain.models.AnimeEpisodeVideo(
                malId = null,
                title = ep.title,
                episode = null,
                url = ep.url,
                imageUrl = ep.thumbnail
            )
        } ?: emptyList()

        return com.yumedev.seijakulist.domain.models.AnimeVideos(
            promos = promos,
            episodes = episodes,
            musicVideos = emptyList() // AniList no proporciona music videos
        )
    }

    /**
     * Obtiene imágenes del anime
     *
     * Nota: AniList proporciona coverImage en diferentes tamaños y bannerImage.
     * No tiene una galería de imágenes como Jikan.
     *
     * @param animeId ID de AniList
     * @param malId MyAnimeList ID (alternativo)
     * @return Lista de AnimePicture
     */
    suspend fun getAnimePicturesById(animeId: Int? = null, malId: Int? = null): List<com.yumedev.seijakulist.domain.models.AnimePicture> {
        if (animeId == null && malId == null) {
            throw IllegalArgumentException("Se requiere animeId o malId")
        }

        val response = apolloClient.query(
            GetAnimeDetailsQuery(
                id = Optional.presentIfNotNull(animeId),
                idMal = Optional.presentIfNotNull(malId)
            )
        ).execute()

        if (response.hasErrors()) {
            throw Exception("GraphQL errors: ${response.errors?.joinToString { it.message }}")
        }

        val media = response.data?.Media
            ?: throw Exception("No se encontró anime con ID: $animeId / MAL ID: $malId")

        val fields = media.mediaFields
        val pictures = mutableListOf<com.yumedev.seijakulist.domain.models.AnimePicture>()

        // Banner image (si existe)
        fields.bannerImage?.let {
            pictures.add(
                com.yumedev.seijakulist.domain.models.AnimePicture(
                    imageUrl = it,
                    smallImageUrl = it,
                    largeImageUrl = it
                )
            )
        }

        // Cover images en diferentes tamaños
        fields.coverImage?.let { cover ->
            pictures.add(
                com.yumedev.seijakulist.domain.models.AnimePicture(
                    imageUrl = cover.medium,
                    smallImageUrl = cover.medium,
                    largeImageUrl = cover.extraLarge ?: cover.large
                )
            )
        }

        return pictures
    }

    /**
     * Obtiene la lista de géneros disponibles
     *
     * Nota: AniList solo proporciona nombres de géneros, no incluye:
     * - malId
     * - url
     * - count
     *
     * @return Lista de Genre con datos limitados
     */
    suspend fun getGenresAnime(): List<com.yumedev.seijakulist.domain.models.Genre> {
        val response = apolloClient.query(
            com.yumedev.seijakulist.data.remote.graphql.GetGenreCollectionQuery()
        ).execute()

        if (response.hasErrors()) {
            throw Exception("GraphQL errors: ${response.errors?.joinToString { it.message }}")
        }

        val genres = response.data?.GenreCollection?.filterNotNull() ?: emptyList()

        return genres.mapIndexed { index, genreName ->
            com.yumedev.seijakulist.domain.models.Genre(
                malId = index + 1, // ID sintético ya que AniList no proporciona IDs
                name = genreName,
                url = "", // AniList no proporciona URLs de géneros
                count = 0 // AniList no proporciona conteos
            )
        }
    }

    /**
     * Obtiene anime nuevos/recientes (ordenados por fecha de inicio)
     *
     * @param page Número de página
     * @param perPage Elementos por página
     * @return Lista de AnimeCard
     */
    suspend fun getAnimeNew(
        page: Int = 1,
        perPage: Int = DEFAULT_PER_PAGE
    ): List<AnimeCard> {
        val response = apolloClient.query(
            SearchAnimeQuery(
                page = Optional.present(page),
                perPage = Optional.present(perPage),
                sort = Optional.present(listOf(MediaSort.START_DATE_DESC, MediaSort.POPULARITY_DESC))
            )
        ).execute()

        if (response.hasErrors()) {
            throw Exception("GraphQL errors: ${response.errors?.joinToString { it.message }}")
        }

        val media = response.data?.Page?.media?.filterNotNull() ?: emptyList()
        return media.map { it.toAnimeCard() }
    }

    /**
     * Obtiene anime populares
     *
     * @param page Número de página
     * @param perPage Elementos por página
     * @return Lista de AnimeCard
     */
    suspend fun getAnimePopular(
        page: Int = 1,
        perPage: Int = DEFAULT_PER_PAGE
    ): List<AnimeCard> {
        val response = apolloClient.query(
            SearchAnimeQuery(
                page = Optional.present(page),
                perPage = Optional.present(perPage),
                sort = Optional.present(listOf(MediaSort.POPULARITY_DESC))
            )
        ).execute()

        if (response.hasErrors()) {
            throw Exception("GraphQL errors: ${response.errors?.joinToString { it.message }}")
        }

        val media = response.data?.Page?.media?.filterNotNull() ?: emptyList()
        return media.map { it.toAnimeCard() }
    }

    /**
     * Obtiene anime de la temporada actual
     *
     * @param page Número de página
     * @param perPage Elementos por página
     * @return Lista de AnimeCard
     */
    suspend fun getCurrentSeasonAnime(
        page: Int = 1,
        perPage: Int = DEFAULT_PER_PAGE
    ): List<AnimeCard> {
        val calendar = java.util.Calendar.getInstance()
        val currentYear = calendar.get(java.util.Calendar.YEAR)
        val currentMonth = calendar.get(java.util.Calendar.MONTH) + 1 // Calendar.MONTH es 0-indexed

        val currentSeason = when (currentMonth) {
            12, 1, 2 -> MediaSeason.WINTER
            3, 4, 5 -> MediaSeason.SPRING
            6, 7, 8 -> MediaSeason.SUMMER
            9, 10, 11 -> MediaSeason.FALL
            else -> MediaSeason.WINTER
        }

        return getSeasonalAnime(
            season = currentSeason,
            year = currentYear,
            page = page,
            perPage = perPage
        )
    }

    /**
     * Obtiene un anime aleatorio
     *
     * Implementación: Selecciona un anime aleatorio de las primeras páginas de populares
     *
     * @return AnimeCard aleatorio
     */
    suspend fun getAnimeRandom(): AnimeCard? {
        val randomPage = (1..10).random()
        val response = apolloClient.query(
            SearchAnimeQuery(
                page = Optional.present(randomPage),
                perPage = Optional.present(50),
                sort = Optional.present(listOf(MediaSort.POPULARITY_DESC))
            )
        ).execute()

        if (response.hasErrors()) {
            return null
        }

        val media = response.data?.Page?.media?.filterNotNull()?.randomOrNull() ?: return null
        return media.toAnimeCard()
    }

    /**
     * Obtiene anime que se emiten en un rango de tiempo específico
     * Útil para obtener anime que se emiten en un día específico
     *
     * @param airingAtGreater Timestamp Unix del inicio del rango (en segundos)
     * @param airingAtLesser Timestamp Unix del fin del rango (en segundos)
     * @param page Número de página
     * @param perPage Elementos por página
     * @return Lista de AnimeCard con información de schedule
     */
    suspend fun getAiringScheduleByTimeRange(
        airingAtGreater: Int,
        airingAtLesser: Int,
        page: Int = 1,
        perPage: Int = 50
    ): List<AnimeCard> {
        val response = apolloClient.query(
            com.yumedev.seijakulist.data.remote.graphql.GetAiringScheduleByDayQuery(
                airingAt_greater = airingAtGreater,
                airingAt_lesser = airingAtLesser,
                page = Optional.present(page),
                perPage = Optional.present(perPage)
            )
        ).execute()

        if (response.hasErrors()) {
            throw Exception("GraphQL errors: ${response.errors?.joinToString { it.message }}")
        }

        val schedules = response.data?.Page?.airingSchedules?.filterNotNull() ?: emptyList()

        return schedules.mapNotNull { schedule ->
            val media = schedule.media ?: return@mapNotNull null

            // Crear AiringScheduleInfo
            val airingScheduleInfo = com.yumedev.seijakulist.domain.models.AiringScheduleInfo(
                airingAt = schedule.airingAt.toLong(),
                episode = schedule.episode,
                timeUntilAiring = schedule.timeUntilAiring.toLong(),
                dayOfWeek = getDayOfWeekFromTimestamp(schedule.airingAt.toLong()),
                formattedTime = getFormattedTimeFromTimestamp(schedule.airingAt.toLong())
            )

            AnimeCard(
                malId = media.idMal ?: 0,
                title = media.title?.romaji ?: media.title?.english ?: media.title?.native ?: "",
                images = media.coverImage?.large ?: media.coverImage?.medium ?: "",
                score = (media.averageScore ?: 0) / 10.0f,
                status = media.status?.name ?: "",
                type = media.format?.name ?: "",
                genres = media.genres?.filterNotNull()?.map { genre ->
                    com.yumedev.seijakulist.data.remote.models.GenreDto(
                        malId = 0,
                        name = genre
                    )
                } ?: emptyList(),
                year = media.seasonYear?.toString() ?: "",
                episodes = media.episodes?.toString() ?: "",
                airingSchedule = airingScheduleInfo
            )
        }
    }

    /**
     * Convierte un timestamp Unix a día de la semana
     */
    private fun getDayOfWeekFromTimestamp(timestamp: Long): String {
        val calendar = java.util.Calendar.getInstance()
        calendar.timeInMillis = timestamp * 1000L
        return when (calendar.get(java.util.Calendar.DAY_OF_WEEK)) {
            java.util.Calendar.SUNDAY -> "Sunday"
            java.util.Calendar.MONDAY -> "Monday"
            java.util.Calendar.TUESDAY -> "Tuesday"
            java.util.Calendar.WEDNESDAY -> "Wednesday"
            java.util.Calendar.THURSDAY -> "Thursday"
            java.util.Calendar.FRIDAY -> "Friday"
            java.util.Calendar.SATURDAY -> "Saturday"
            else -> ""
        }
    }

    /**
     * Convierte un timestamp Unix a hora formateada (HH:mm)
     */
    private fun getFormattedTimeFromTimestamp(timestamp: Long): String {
        val calendar = java.util.Calendar.getInstance()
        calendar.timeInMillis = timestamp * 1000L
        val hour = calendar.get(java.util.Calendar.HOUR_OF_DAY)
        val minute = calendar.get(java.util.Calendar.MINUTE)
        return String.format("%02d:%02d", hour, minute)
    }
}

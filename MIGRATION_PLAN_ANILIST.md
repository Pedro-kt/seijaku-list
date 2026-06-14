# Plan de Migración: Jikan API → AniList GraphQL API

**Fecha**: Junio 2026
**Proyecto**: SeijakuList
**Objetivo**: Migración completa de Jikan API (REST) a AniList GraphQL API

---

## 📋 Índice

1. [Resumen Ejecutivo](#resumen-ejecutivo)
2. [Fase 1: Investigación y Setup Inicial](#fase-1-investigación-y-setup-inicial)
3. [Fase 2: Mapeo de Campos Jikan → AniList](#fase-2-mapeo-de-campos-jikan--anilist)
4. [Fase 3: Implementación de Infraestructura GraphQL](#fase-3-implementación-de-infraestructura-graphql)
5. [Fase 4: Migración de DTOs y Modelos](#fase-4-migración-de-dtos-y-modelos)
6. [Fase 5: Actualización del Repository Layer](#fase-5-actualización-del-repository-layer)
7. [Fase 6: Actualización de Mappers](#fase-6-actualización-de-mappers)
8. [Fase 7: Testing y Validación](#fase-7-testing-y-validación)
9. [Fase 8: Limpieza y Optimización](#fase-8-limpieza-y-optimización)
10. [Consideraciones Importantes](#consideraciones-importantes)
11. [Cronograma Estimado](#cronograma-estimado)

---

## 🎯 Resumen Ejecutivo

### Estado Actual
- **API**: Jikan v4 (REST)
- **Endpoints**: 27 diferentes
- **DTOs**: 30+ modelos
- **Arquitectura**: Clean Architecture (ViewModel → UseCase → Repository → API)

### Motivación
- Jikan API dejará de funcionar en octubre 2026
- Necesidad de migrar a una API estable y mantenida

### Elección: AniList GraphQL API

**Ventajas de AniList:**
- ✅ GraphQL: Mayor eficiencia, un solo endpoint
- ✅ Datos actualizados y bien mantenidos
- ✅ Documentación excelente
- ✅ Comunidad activa
- ✅ Rate limit: 90 requests/min
- ✅ Soporte para anime y manga
- ✅ Sin costo

**Desventajas:**
- ❌ Requiere cambio de paradigma REST → GraphQL
- ❌ IDs diferentes (AniList ID vs MAL ID)
- ❌ Algunos campos pueden tener nombres/estructuras diferentes

---

## 📚 Fase 1: Investigación y Setup Inicial

### 1.1 Agregar Dependencias GraphQL

**build.gradle (app level):**
```kotlin
dependencies {
    // Apollo GraphQL Client
    implementation("com.apollographql.apollo3:apollo-runtime:3.8.2")
    implementation("com.apollographql.apollo3:apollo-normalized-cache:3.8.2")

    // Opcional: Cache persistente
    implementation("com.apollographql.apollo3:apollo-normalized-cache-sqlite:3.8.2")
}
```

**build.gradle (project level):**
```kotlin
plugins {
    id("com.apollographql.apollo3") version "3.8.2" apply false
}
```

**build.gradle (app level) - plugin:**
```kotlin
plugins {
    id("com.apollographql.apollo3")
}

apollo {
    service("anilist") {
        packageName.set("com.yumedev.seijakulist.data.remote.graphql")
        schemaFile.set(file("src/main/graphql/schema.graphqls"))
    }
}
```

### 1.2 Descargar Schema de AniList

Crear script para descargar el schema:
```bash
# scripts/download_schema.sh
curl https://graphql.anilist.co \
  -H "Content-Type: application/json" \
  -d '{"query":"{\n  __schema {\n    types {\n      name\n    }\n  }\n}"}' \
  | jq .data.__schema > app/src/main/graphql/schema.graphqls
```

O usar Apollo CLI:
```bash
./gradlew downloadAnilistApolloSchemaFromIntrospection \
  --endpoint="https://graphql.anilist.co" \
  --schema="app/src/main/graphql/schema.graphqls"
```

### 1.3 Investigar API de AniList

**Recursos:**
- Documentación oficial: https://anilist.gitbook.io/anilist-apiv2-docs/
- GraphiQL Explorer: https://anilist.co/graphiql
- GitHub: https://github.com/AniList/ApiV2-GraphQL-Docs

**Puntos clave a investigar:**
- ✅ Estructura de Media (anime/manga)
- ✅ Búsqueda y filtros
- ✅ Paginación (pageInfo, hasNextPage)
- ✅ Campos de personajes (Character)
- ✅ Relaciones (staff, studios, etc.)
- ✅ Rate limiting
- ✅ Autenticación OAuth2 (opcional para features avanzadas)

---

## 🗺️ Fase 2: Mapeo de Campos Jikan → AniList

### 2.1 Mapeo de Anime Details

| Jikan Field | AniList Field | Notas |
|-------------|---------------|-------|
| `mal_id` | `idMal` | AniList mantiene referencia a MAL |
| `title` | `title.romaji` | Múltiples títulos disponibles |
| `title_english` | `title.english` | |
| `title_japanese` | `title.native` | |
| `images.jpg.image_url` | `coverImage.large` | AniList usa URLs directas |
| `images.webp.image_url` | `coverImage.extraLarge` | |
| `trailer.youtube_id` | `trailer.id` | Formato similar |
| `trailer.url` | `trailer.site` + `.id` | Construir URL |
| `synopsis` | `description` | Puede incluir HTML |
| `type` | `format` | TV, MOVIE, OVA, etc. |
| `source` | `source` | MANGA, LIGHT_NOVEL, etc. |
| `episodes` | `episodes` | |
| `status` | `status` | FINISHED, RELEASING, etc. |
| `airing` | `status == RELEASING` | Derivado |
| `aired.from` | `startDate` | Objeto {year, month, day} |
| `aired.to` | `endDate` | Objeto {year, month, day} |
| `duration` | `duration` | En minutos |
| `rating` | N/A | AniList no tiene rating MPAA |
| `score` | `averageScore` | 0-100 en AniList |
| `scored_by` | `stats.scoreDistribution` | Más detallado |
| `rank` | `rankings` | Array de rankings |
| `popularity` | `popularity` | |
| `members` | `stats.statusDistribution` | Total users |
| `favorites` | `favourites` | |
| `broadcast.day` | `airingSchedule.airingAt` | Timestamp |
| `broadcast.time` | `airingSchedule.airingAt` | Timestamp |
| `producers` | N/A | No directamente |
| `studios` | `studios.nodes` | Array de studios |
| `genres` | `genres` | Array de strings |
| `demographics` | `tags` | Más granular en AniList |
| `season` | `season` | WINTER, SPRING, etc. |
| `year` | `seasonYear` | |

### 2.2 Mapeo de Character Details

| Jikan Field | AniList Field | Notas |
|-------------|---------------|-------|
| `mal_id` | `idMal` | |
| `name` | `name.full` | |
| `name_kanji` | `name.native` | |
| `images.jpg.image_url` | `image.large` | |
| `about` | `description` | |
| `voices` | `media.edges.voiceActors` | Más complejo |
| `anime` | `media.nodes` (anime) | Filtrar por type |
| `manga` | `media.nodes` (manga) | Filtrar por type |

### 2.3 Nuevos Campos Disponibles en AniList

**Ventajas de AniList:**
- `relations` - Relaciones (secuelas, precuelas, spin-offs)
- `recommendations` - Basadas en usuarios
- `tags` - Tags detallados con % de votos
- `externalLinks` - Enlaces a Crunchyroll, Funimation, etc.
- `streamingEpisodes` - Info de streaming
- `rankings` - Rankings por tipo, formato, año
- `stats` - Estadísticas detalladas de usuarios
- `nextAiringEpisode` - Próximo episodio (tiempo real)

### 2.4 Campos Perdidos (sin equivalente directo)

**Jikan tiene pero AniList NO:**
- `aired.prop` (día/mes separados) - Usar `startDate.day`, `startDate.month`
- `rating` (G, PG, PG-13, etc.) - Usar `isAdult` boolean
- `producers` (array separado) - Solo studios disponible
- `themes` (openings/endings) - NO disponible en AniList
- `broadcast.string` - Construir desde `airingSchedule`
- `title_synonyms` - Usar `synonyms` array

**Implicación:** Features como AnimeThemes pueden requerir API adicional o eliminarse.

---

## 🏗️ Fase 3: Implementación de Infraestructura GraphQL

### 3.1 Crear AniListApiClient

**Ubicación:** `data/remote/api/AniListApiClient.kt`

```kotlin
package com.yumedev.seijakulist.data.remote.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo3.cache.normalized.normalizedCache
import com.apollographql.apollo3.network.okHttpClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AniListApiClient @Inject constructor() {

    companion object {
        private const val BASE_URL = "https://graphql.anilist.co"
        private const val CACHE_SIZE_MB = 10L
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor { chain ->
            // Rate limiting: 90 requests/min
            // Agregar delay si es necesario
            chain.proceed(chain.request())
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    val apolloClient: ApolloClient = ApolloClient.Builder()
        .serverUrl(BASE_URL)
        .okHttpClient(okHttpClient)
        .normalizedCache(
            normalizedCacheFactory = MemoryCacheFactory(
                maxSizeBytes = CACHE_SIZE_MB * 1024 * 1024
            )
        )
        .build()
}
```

### 3.2 Definir GraphQL Queries

**Ubicación:** `app/src/main/graphql/com/yumedev/seijakulist/`

**Estructura de carpetas:**
```
app/src/main/graphql/
├── schema.graphqls
└── com/yumedev/seijakulist/
    ├── queries/
    │   ├── SearchAnime.graphql
    │   ├── GetAnimeDetails.graphql
    │   ├── GetSeasonalAnime.graphql
    │   ├── GetTrendingAnime.graphql
    │   ├── GetCharacterDetails.graphql
    │   └── ...
    └── fragments/
        ├── MediaFields.graphql
        ├── CharacterFields.graphql
        └── ...
```

**Ejemplo: MediaFields.graphql (Fragment reutilizable)**
```graphql
fragment MediaFields on Media {
  id
  idMal
  title {
    romaji
    english
    native
  }
  coverImage {
    large
    extraLarge
  }
  bannerImage
  format
  type
  status
  description
  startDate {
    year
    month
    day
  }
  endDate {
    year
    month
    day
  }
  season
  seasonYear
  episodes
  duration
  source
  averageScore
  meanScore
  popularity
  favourites
  genres
  studios(isMain: true) {
    nodes {
      id
      name
    }
  }
  tags {
    id
    name
    rank
    isMediaSpoiler
  }
  trailer {
    id
    site
  }
  nextAiringEpisode {
    airingAt
    timeUntilAiring
    episode
  }
}
```

**Ejemplo: SearchAnime.graphql**
```graphql
query SearchAnime(
  $page: Int = 1,
  $perPage: Int = 20,
  $search: String,
  $type: MediaType = ANIME,
  $format: MediaFormat,
  $status: MediaStatus,
  $genre: String,
  $sort: [MediaSort] = [POPULARITY_DESC]
) {
  Page(page: $page, perPage: $perPage) {
    pageInfo {
      total
      currentPage
      lastPage
      hasNextPage
      perPage
    }
    media(
      search: $search,
      type: $type,
      format: $format,
      status: $status,
      genre: $genre,
      sort: $sort
    ) {
      ...MediaFields
    }
  }
}
```

**Ejemplo: GetAnimeDetails.graphql**
```graphql
query GetAnimeDetails($id: Int, $idMal: Int) {
  Media(id: $id, idMal: $idMal, type: ANIME) {
    ...MediaFields

    # Campos adicionales para detalles
    synonyms
    isAdult
    countryOfOrigin

    relations {
      edges {
        relationType
        node {
          id
          idMal
          title {
            romaji
            english
          }
          coverImage {
            large
          }
          format
          status
        }
      }
    }

    characters(page: 1, perPage: 25, sort: [ROLE, FAVOURITES_DESC]) {
      edges {
        role
        voiceActors(language: JAPANESE) {
          id
          name {
            full
            native
          }
          image {
            large
          }
          language
        }
        node {
          id
          idMal
          name {
            full
            native
          }
          image {
            large
          }
        }
      }
    }

    recommendations(page: 1, perPage: 10, sort: RATING_DESC) {
      nodes {
        mediaRecommendation {
          id
          idMal
          title {
            romaji
            english
          }
          coverImage {
            large
          }
          format
          averageScore
        }
      }
    }

    externalLinks {
      site
      url
    }

    streamingEpisodes {
      title
      thumbnail
      url
      site
    }

    rankings {
      rank
      type
      format
      year
      season
      allTime
      context
    }

    stats {
      scoreDistribution {
        score
        amount
      }
      statusDistribution {
        status
        amount
      }
    }
  }
}
```

**Ejemplo: GetCharacterDetails.graphql**
```graphql
query GetCharacterDetails($id: Int, $idMal: Int) {
  Character(id: $id, idMal: $idMal) {
    id
    idMal
    name {
      full
      native
      alternative
    }
    image {
      large
    }
    description
    favourites

    media(page: 1, perPage: 25, sort: POPULARITY_DESC) {
      edges {
        characterRole
        voiceActors(language: JAPANESE) {
          id
          name {
            full
            native
          }
          image {
            large
          }
          language
        }
        node {
          id
          idMal
          type
          title {
            romaji
            english
          }
          coverImage {
            large
          }
          format
          status
        }
      }
    }
  }
}
```

**Ejemplo: GetSeasonalAnime.graphql**
```graphql
query GetSeasonalAnime(
  $season: MediaSeason!,
  $seasonYear: Int!,
  $page: Int = 1,
  $perPage: Int = 20
) {
  Page(page: $page, perPage: $perPage) {
    pageInfo {
      total
      currentPage
      lastPage
      hasNextPage
    }
    media(
      season: $season,
      seasonYear: $seasonYear,
      type: ANIME,
      sort: POPULARITY_DESC
    ) {
      ...MediaFields
    }
  }
}
```

**Ejemplo: GetTrendingAnime.graphql**
```graphql
query GetTrendingAnime($page: Int = 1, $perPage: Int = 20) {
  Page(page: $page, perPage: $perPage) {
    pageInfo {
      total
      currentPage
      lastPage
      hasNextPage
    }
    media(type: ANIME, sort: TRENDING_DESC) {
      ...MediaFields
      trending
    }
  }
}
```

### 3.3 Actualizar Dependency Injection (AppModule.kt)

**Ubicación:** `di/AppModule.kt`

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // MANTENER Jikan temporalmente para migración gradual
    @Provides
    @Singleton
    @Named("jikan")
    fun provideJikanRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.jikan.moe/v4/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideJikanApiService(@Named("jikan") retrofit: Retrofit): JikanApiService {
        return retrofit.create(JikanApiService::class.java)
    }

    // NUEVO: AniList Apollo Client
    @Provides
    @Singleton
    fun provideAniListApiClient(): AniListApiClient {
        return AniListApiClient()
    }

    @Provides
    @Singleton
    fun provideApolloClient(apiClient: AniListApiClient): ApolloClient {
        return apiClient.apolloClient
    }

    // Actualizar Repository para usar ambos (temporal)
    @Provides
    @Singleton
    fun provideAnimeRepository(
        jikanApiService: JikanApiService,  // Temporal
        apolloClient: ApolloClient,         // Nuevo
        localDataSource: LocalAnimeDataSource
    ): AnimeRepository {
        return AnimeRepositoryImpl(
            jikanApiService = jikanApiService,
            apolloClient = apolloClient,
            localDataSource = localDataSource
        )
    }
}
```

---

## 🔄 Fase 4: Migración de DTOs y Modelos

### 4.1 Enfoque

**Apollo GraphQL genera automáticamente los modelos** desde las queries `.graphql`.

Después de definir las queries y ejecutar:
```bash
./gradlew generateApolloSources
```

Apollo generará clases en:
```
build/generated/source/apollo/com/yumedev/seijakulist/data/remote/graphql/
├── SearchAnimeQuery.kt
├── GetAnimeDetailsQuery.kt
├── GetCharacterDetailsQuery.kt
└── fragment/
    ├── MediaFields.kt
    └── CharacterFields.kt
```

### 4.2 Estructura de Modelos Generados

**Ejemplo: SearchAnimeQuery.Data**
```kotlin
// Generado automáticamente por Apollo
data class SearchAnimeQuery(
    val page: Page
) {
    data class Page(
        val pageInfo: PageInfo,
        val media: List<Medium?>?
    )

    data class PageInfo(
        val total: Int,
        val currentPage: Int,
        val lastPage: Int,
        val hasNextPage: Boolean
    )

    data class Medium(
        val id: Int,
        val idMal: Int?,
        val title: Title?,
        val coverImage: CoverImage?,
        // ... otros campos según MediaFields fragment
    )
}
```

### 4.3 Crear DTOs Wrapper (Opcional)

Si quieres mantener una capa intermedia:

**Ubicación:** `data/remote/models/anilist/`

```kotlin
// Wrapper sobre modelos generados de Apollo
data class AniListAnimeDto(
    val id: Int,
    val malId: Int?,
    val title: AniListTitleDto,
    val coverImage: AniListImageDto,
    val format: String?,
    val status: String?,
    val description: String?,
    val episodes: Int?,
    val averageScore: Int?,
    val genres: List<String>?,
    // ... otros campos
)

// Mapper: Apollo Model → DTO
fun SearchAnimeQuery.Medium.toDto(): AniListAnimeDto {
    return AniListAnimeDto(
        id = id,
        malId = idMal,
        title = title.toDto(),
        coverImage = coverImage.toDto(),
        format = format?.name,
        status = status?.name,
        description = description,
        episodes = episodes,
        averageScore = averageScore,
        genres = genres?.filterNotNull()
    )
}
```

**Recomendación:** Usa los modelos generados de Apollo directamente en los mappers para reducir código duplicado.

---

## 🏪 Fase 5: Actualización del Repository Layer

### 5.1 Estrategia de Migración

**Opción A: Migración Total**
- Reemplazar completamente JikanApiService por ApolloClient
- Más rápido pero arriesgado

**Opción B: Migración Gradual (RECOMENDADO)**
- Mantener ambos APIs temporalmente
- Migrar endpoint por endpoint
- Permite rollback si hay problemas
- Testing incremental

### 5.2 Implementación Gradual

**AnimeRepository.kt - Versión Híbrida**

```kotlin
interface AnimeRepository {
    // Mantener firmas existentes
    suspend fun searchAnimes(query: String, page: Int): Result<List<AnimeCard>>
    suspend fun getAnimeDetailsById(id: Int): Result<AnimeDetail>
    // ... otros métodos
}

class AnimeRepositoryImpl @Inject constructor(
    private val jikanApiService: JikanApiService,  // Mantener temporalmente
    private val apolloClient: ApolloClient,        // Nuevo
    private val localDataSource: LocalAnimeDataSource
) : AnimeRepository {

    // Flag para controlar migración gradual
    private val useAniList = BuildConfig.USE_ANILIST  // o RemoteConfig

    override suspend fun searchAnimes(
        query: String,
        page: Int
    ): Result<List<AnimeCard>> = withContext(Dispatchers.IO) {
        try {
            if (useAniList) {
                // NUEVO: Usar AniList GraphQL
                val response = apolloClient.query(
                    SearchAnimeQuery(
                        search = Optional.present(query),
                        page = Optional.present(page),
                        perPage = Optional.present(20)
                    )
                ).execute()

                if (response.hasErrors()) {
                    return@withContext Result.failure(
                        Exception(response.errors?.firstOrNull()?.message)
                    )
                }

                val animes = response.data?.page?.media
                    ?.filterNotNull()
                    ?.map { it.toAnimeCard() }  // Mapper
                    ?: emptyList()

                Result.success(animes)
            } else {
                // VIEJO: Usar Jikan (fallback)
                val response = jikanApiService.searchAnimes(
                    query = query,
                    page = page
                )
                val animes = response.data.map { it.toAnimeCard() }
                Result.success(animes)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAnimeDetailsById(id: Int): Result<AnimeDetail> =
        withContext(Dispatchers.IO) {
            try {
                if (useAniList) {
                    // NUEVO: AniList GraphQL
                    val response = apolloClient.query(
                        GetAnimeDetailsQuery(
                            id = Optional.present(id)
                        )
                    ).execute()

                    if (response.hasErrors()) {
                        return@withContext Result.failure(
                            Exception(response.errors?.firstOrNull()?.message)
                        )
                    }

                    val anime = response.data?.media?.toAnimeDetail()
                        ?: return@withContext Result.failure(
                            Exception("Anime not found")
                        )

                    Result.success(anime)
                } else {
                    // VIEJO: Jikan
                    val response = jikanApiService.getAnimeDetailsById(id)
                    val anime = response.data.toAnimeDetail()
                    Result.success(anime)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    // Migrar método por método...

    override suspend fun getAnimeCharactersById(id: Int): Result<List<AnimeCharactersDetail>> =
        withContext(Dispatchers.IO) {
            try {
                if (useAniList) {
                    // En AniList, los personajes vienen en GetAnimeDetailsQuery
                    // O crear query separada si es necesario
                    val response = apolloClient.query(
                        GetAnimeDetailsQuery(id = Optional.present(id))
                    ).execute()

                    val characters = response.data?.media?.characters?.edges
                        ?.filterNotNull()
                        ?.map { it.toAnimeCharacterDetail() }
                        ?: emptyList()

                    Result.success(characters)
                } else {
                    val response = jikanApiService.getAnimeCharactersById(id)
                    val characters = response.data.map { it.toAnimeCharacterDetail() }
                    Result.success(characters)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun getAnimeThemesById(id: Int): Result<AnimeThemes> =
        withContext(Dispatchers.IO) {
            try {
                if (useAniList) {
                    // IMPORTANTE: AniList NO tiene temas OP/ED
                    // Opciones:
                    // 1. Usar API complementaria (AnimeThemes.moe)
                    // 2. Mantener Jikan solo para esto
                    // 3. Eliminar feature

                    // Opción 2: Fallback a Jikan
                    val malId = getMalIdFromAniListId(id)
                    if (malId != null) {
                        val response = jikanApiService.getAnimeThemesById(malId)
                        Result.success(response.data.toAnimeThemes())
                    } else {
                        Result.failure(Exception("MAL ID not found"))
                    }
                } else {
                    val response = jikanApiService.getAnimeThemesById(id)
                    Result.success(response.data.toAnimeThemes())
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    // Helper: Convertir AniList ID → MAL ID
    private suspend fun getMalIdFromAniListId(aniListId: Int): Int? {
        return try {
            val response = apolloClient.query(
                GetAnimeDetailsQuery(id = Optional.present(aniListId))
            ).execute()
            response.data?.media?.idMal
        } catch (e: Exception) {
            null
        }
    }

    // Seasonal animes
    override suspend fun searchAnimeSeasonNow(page: Int): Result<List<AnimeCard>> =
        withContext(Dispatchers.IO) {
            try {
                if (useAniList) {
                    val (season, year) = getCurrentSeasonAndYear()

                    val response = apolloClient.query(
                        GetSeasonalAnimeQuery(
                            season = season,
                            seasonYear = year,
                            page = Optional.present(page)
                        )
                    ).execute()

                    val animes = response.data?.page?.media
                        ?.filterNotNull()
                        ?.map { it.toAnimeCard() }
                        ?: emptyList()

                    Result.success(animes)
                } else {
                    val response = jikanApiService.searchAnimeSeasonNow(page)
                    val animes = response.data.map { it.toAnimeCard() }
                    Result.success(animes)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    // Helper: Calcular temporada actual
    private fun getCurrentSeasonAndYear(): Pair<MediaSeason, Int> {
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)

        val season = when (month) {
            in 1..3 -> MediaSeason.WINTER
            in 4..6 -> MediaSeason.SPRING
            in 7..9 -> MediaSeason.SUMMER
            else -> MediaSeason.FALL
        }

        return season to year
    }

    // Top animes
    override suspend fun searchTopAnimes(page: Int): Result<List<AnimeCard>> =
        withContext(Dispatchers.IO) {
            try {
                if (useAniList) {
                    val response = apolloClient.query(
                        SearchAnimeQuery(
                            page = Optional.present(page),
                            perPage = Optional.present(20),
                            sort = Optional.present(listOf(MediaSort.SCORE_DESC))
                        )
                    ).execute()

                    val animes = response.data?.page?.media
                        ?.filterNotNull()
                        ?.map { it.toAnimeCard() }
                        ?: emptyList()

                    Result.success(animes)
                } else {
                    val response = jikanApiService.searchTopAnimes(page)
                    val animes = response.data.map { it.toAnimeCard() }
                    Result.success(animes)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
```

### 5.3 Configuración de Build Flags

**build.gradle (app):**
```kotlin
android {
    buildTypes {
        debug {
            buildConfigField("boolean", "USE_ANILIST", "true")  // Test AniList
        }
        release {
            buildConfigField("boolean", "USE_ANILIST", "true")  // Production
        }
    }
}
```

O usar Remote Config de Firebase para control dinámico.

### 5.4 Orden de Migración Recomendado

**Prioridad 1 (Core Features):**
1. ✅ Search animes
2. ✅ Get anime details
3. ✅ Get characters
4. ✅ Seasonal animes (current/upcoming)
5. ✅ Top animes

**Prioridad 2 (Discovery):**
6. ✅ Trending/Popular
7. ✅ Random anime
8. ✅ Recommendations
9. ✅ Character details
10. ✅ Genre filtering

**Prioridad 3 (Advanced):**
11. ✅ Schedule (usar airingSchedule)
12. ✅ Pictures/Videos
13. ⚠️ Themes (considerar eliminar o API alternativa)
14. ✅ Producer/Studio details

---

## 🗺️ Fase 6: Actualización de Mappers

### 6.1 Crear Mappers para AniList Models

**Ubicación:** `data/mapper/anilist/`

**AniListAnimeMapper.kt:**
```kotlin
package com.yumedev.seijakulist.data.mapper.anilist

import com.yumedev.seijakulist.data.remote.graphql.SearchAnimeQuery
import com.yumedev.seijakulist.data.remote.graphql.GetAnimeDetailsQuery
import com.yumedev.seijakulist.data.remote.graphql.fragment.MediaFields
import com.yumedev.seijakulist.domain.models.AnimeCard
import com.yumedev.seijakulist.domain.models.AnimeDetail

// Mapper: SearchAnimeQuery.Medium → AnimeCard
fun SearchAnimeQuery.Medium.toAnimeCard(): AnimeCard {
    return AnimeCard(
        malId = idMal ?: 0,  // IMPORTANTE: Algunos animes pueden no tener MAL ID
        aniListId = id,       // Guardar AniList ID también
        imageUrl = coverImage?.large ?: "",
        title = title?.romaji ?: title?.english ?: "",
        titleEnglish = title?.english,
        titleJapanese = title?.native,
        type = format?.name ?: "UNKNOWN",
        episodes = episodes,
        score = (averageScore?.toDouble() ?: 0.0) / 10.0,  // Convertir 0-100 a 0-10
        status = status?.name ?: "UNKNOWN",
        year = seasonYear
    )
}

// Mapper: MediaFields fragment → AnimeCard
fun MediaFields.toAnimeCard(): AnimeCard {
    return AnimeCard(
        malId = idMal ?: 0,
        aniListId = id,
        imageUrl = coverImage?.large ?: "",
        title = title?.romaji ?: title?.english ?: "",
        titleEnglish = title?.english,
        titleJapanese = title?.native,
        type = format?.name ?: "UNKNOWN",
        episodes = episodes,
        score = (averageScore?.toDouble() ?: 0.0) / 10.0,
        status = status?.name ?: "UNKNOWN",
        year = seasonYear
    )
}

// Mapper: GetAnimeDetailsQuery.Media → AnimeDetail
fun GetAnimeDetailsQuery.Media.toAnimeDetail(): AnimeDetail {
    return AnimeDetail(
        malId = idMal ?: 0,
        aniListId = id,
        imageUrl = coverImage?.large ?: "",
        bannerImage = bannerImage,
        title = title?.romaji ?: title?.english ?: "",
        titleEnglish = title?.english,
        titleJapanese = title?.native,
        titleSynonyms = synonyms?.filterNotNull() ?: emptyList(),
        type = format?.name ?: "UNKNOWN",
        source = source?.name,
        episodes = episodes,
        status = status?.name ?: "UNKNOWN",
        airing = status?.name == "RELEASING",
        aired = mapAired(startDate, endDate),
        duration = duration,  // Ya en minutos
        rating = if (isAdult == true) "R+" else "PG-13",  // Aproximación
        score = (averageScore?.toDouble() ?: 0.0) / 10.0,
        scoredBy = stats?.scoreDistribution?.sumOf { it?.amount ?: 0 } ?: 0,
        rank = rankings?.firstOrNull { it?.allTime == true }?.rank,
        popularity = popularity,
        members = stats?.statusDistribution?.sumOf { it?.amount ?: 0 } ?: 0,
        favorites = favourites ?: 0,
        synopsis = description?.stripHtml(),  // Remover HTML tags
        background = null,  // AniList no tiene campo equivalente
        season = season?.name,
        year = seasonYear,
        broadcast = mapBroadcast(nextAiringEpisode),
        producers = emptyList(),  // No disponible en AniList
        studios = studios?.nodes?.filterNotNull()?.map {
            Studio(id = it.id, name = it.name)
        } ?: emptyList(),
        genres = genres?.filterNotNull() ?: emptyList(),
        demographics = tags?.filter { it?.rank ?: 0 > 80 }
            ?.map { it?.name ?: "" } ?: emptyList(),

        // Nuevos campos de AniList
        relations = relations?.edges?.filterNotNull()?.map {
            AnimeRelation(
                malId = it.node?.idMal,
                aniListId = it.node?.id,
                type = it.relationType?.name ?: "UNKNOWN",
                title = it.node?.title?.romaji ?: "",
                imageUrl = it.node?.coverImage?.large ?: ""
            )
        } ?: emptyList(),

        externalLinks = externalLinks?.filterNotNull()?.map {
            ExternalLink(site = it.site, url = it.url)
        } ?: emptyList(),

        streamingEpisodes = streamingEpisodes?.filterNotNull()?.map {
            StreamingEpisode(
                title = it.title ?: "",
                thumbnail = it.thumbnail,
                url = it.url ?: "",
                site = it.site ?: ""
            )
        } ?: emptyList(),

        trailer = trailer?.let {
            Trailer(
                youtubeId = it.id,
                url = when (it.site) {
                    "youtube" -> "https://www.youtube.com/watch?v=${it.id}"
                    "dailymotion" -> "https://www.dailymotion.com/video/${it.id}"
                    else -> null
                },
                embedUrl = when (it.site) {
                    "youtube" -> "https://www.youtube.com/embed/${it.id}"
                    else -> null
                }
            )
        }
    )
}

// Helper: Mapear fechas
private fun mapAired(
    startDate: GetAnimeDetailsQuery.StartDate?,
    endDate: GetAnimeDetailsQuery.EndDate?
): Aired {
    return Aired(
        from = startDate?.let {
            "${it.year}-${it.month?.toString()?.padStart(2, '0')}-${it.day?.toString()?.padStart(2, '0')}"
        },
        to = endDate?.let {
            "${it.year}-${it.month?.toString()?.padStart(2, '0')}-${it.day?.toString()?.padStart(2, '0')}"
        }
    )
}

// Helper: Mapear broadcast
private fun mapBroadcast(
    nextAiringEpisode: GetAnimeDetailsQuery.NextAiringEpisode?
): Broadcast? {
    if (nextAiringEpisode == null) return null

    val instant = Instant.ofEpochSecond(nextAiringEpisode.airingAt.toLong())
    val zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("Asia/Tokyo"))

    return Broadcast(
        day = zonedDateTime.dayOfWeek.name,
        time = zonedDateTime.toLocalTime().toString(),
        timezone = "Asia/Tokyo",
        string = "Airing ${zonedDateTime.dayOfWeek}s at ${zonedDateTime.toLocalTime()}"
    )
}

// Helper: Limpiar HTML
private fun String.stripHtml(): String {
    return this
        .replace("<br>", "\n")
        .replace("<br/>", "\n")
        .replace("<i>", "")
        .replace("</i>", "")
        .replace("<b>", "")
        .replace("</b>", "")
        .replace(Regex("<.*?>"), "")
}
```

**AniListCharacterMapper.kt:**
```kotlin
package com.yumedev.seijakulist.data.mapper.anilist

import com.yumedev.seijakulist.data.remote.graphql.GetAnimeDetailsQuery
import com.yumedev.seijakulist.data.remote.graphql.GetCharacterDetailsQuery
import com.yumedev.seijakulist.domain.models.AnimeCharactersDetail
import com.yumedev.seijakulist.domain.models.CharacterDetail
import com.yumedev.seijakulist.domain.models.VoiceActor

// Mapper: Character edge → AnimeCharactersDetail
fun GetAnimeDetailsQuery.Edge.toAnimeCharacterDetail(): AnimeCharactersDetail {
    val character = node
    val voiceActor = voiceActors?.firstOrNull()

    return AnimeCharactersDetail(
        malId = character?.idMal,
        aniListId = character?.id,
        name = character?.name?.full ?: "",
        nameKanji = character?.name?.native,
        imageUrl = character?.image?.large ?: "",
        role = role?.name ?: "SUPPORTING",
        voiceActor = voiceActor?.let {
            VoiceActor(
                malId = null,  // AniList no expone MAL ID para personas
                aniListId = it.id,
                name = it.name?.full ?: "",
                nameKanji = it.name?.native,
                imageUrl = it.image?.large ?: "",
                language = it.language?.name ?: "Japanese"
            )
        }
    )
}

// Mapper: GetCharacterDetailsQuery.Character → CharacterDetail
fun GetCharacterDetailsQuery.Character.toCharacterDetail(): CharacterDetail {
    return CharacterDetail(
        malId = idMal,
        aniListId = id,
        name = name?.full ?: "",
        nameKanji = name?.native,
        nameAlternatives = name?.alternative?.filterNotNull() ?: emptyList(),
        imageUrl = image?.large ?: "",
        about = description?.stripHtml(),
        favorites = favourites ?: 0,

        // Anime appearances
        anime = media?.edges?.filterNotNull()
            ?.filter { it.node?.type?.name == "ANIME" }
            ?.map { edge ->
                CharacterAnimeAppearance(
                    malId = edge.node?.idMal,
                    aniListId = edge.node?.id,
                    role = edge.characterRole?.name ?: "SUPPORTING",
                    title = edge.node?.title?.romaji ?: "",
                    imageUrl = edge.node?.coverImage?.large ?: "",
                    voiceActor = edge.voiceActors?.firstOrNull()?.let {
                        VoiceActor(
                            aniListId = it.id,
                            name = it.name?.full ?: "",
                            nameKanji = it.name?.native,
                            imageUrl = it.image?.large ?: "",
                            language = it.language?.name ?: "Japanese"
                        )
                    }
                )
            } ?: emptyList(),

        // Manga appearances (si tu app lo soporta)
        manga = media?.edges?.filterNotNull()
            ?.filter { it.node?.type?.name == "MANGA" }
            ?.map { edge ->
                CharacterMangaAppearance(
                    malId = edge.node?.idMal,
                    aniListId = edge.node?.id,
                    role = edge.characterRole?.name ?: "SUPPORTING",
                    title = edge.node?.title?.romaji ?: ""
                )
            } ?: emptyList()
    )
}
```

### 6.2 Actualizar Domain Models (si es necesario)

**Ubicación:** `domain/models/`

**Agregar campo `aniListId` a modelos existentes:**

```kotlin
// AnimeCard.kt
data class AnimeCard(
    val malId: Int,
    val aniListId: Int? = null,  // NUEVO
    val imageUrl: String,
    val title: String,
    // ... resto de campos
)

// AnimeDetail.kt
data class AnimeDetail(
    val malId: Int,
    val aniListId: Int? = null,  // NUEVO
    val imageUrl: String,
    // ... resto de campos

    // Nuevos campos opcionales de AniList
    val relations: List<AnimeRelation>? = null,
    val externalLinks: List<ExternalLink>? = null,
    val streamingEpisodes: List<StreamingEpisode>? = null,
)

// Nuevos modelos para features de AniList
data class AnimeRelation(
    val malId: Int?,
    val aniListId: Int?,
    val type: String,  // SEQUEL, PREQUEL, SIDE_STORY, etc.
    val title: String,
    val imageUrl: String
)

data class ExternalLink(
    val site: String,  // "Crunchyroll", "Funimation", etc.
    val url: String
)

data class StreamingEpisode(
    val title: String,
    val thumbnail: String?,
    val url: String,
    val site: String
)
```

---

## ✅ Fase 7: Testing y Validación

### 7.1 Unit Tests

**Ubicación:** `test/data/mapper/`

**AniListAnimeMapperTest.kt:**
```kotlin
class AniListAnimeMapperTest {

    @Test
    fun `test SearchAnimeQuery Medium to AnimeCard mapping`() {
        // Given
        val medium = SearchAnimeQuery.Medium(
            id = 12345,
            idMal = Optional.present(54321),
            title = SearchAnimeQuery.Title(
                romaji = "Kimetsu no Yaiba",
                english = "Demon Slayer",
                native = "鬼滅の刃"
            ),
            coverImage = SearchAnimeQuery.CoverImage(
                large = "https://example.com/image.jpg"
            ),
            format = MediaFormat.TV,
            status = MediaStatus.FINISHED,
            episodes = Optional.present(26),
            averageScore = Optional.present(85),
            seasonYear = Optional.present(2019)
        )

        // When
        val animeCard = medium.toAnimeCard()

        // Then
        assertEquals(54321, animeCard.malId)
        assertEquals(12345, animeCard.aniListId)
        assertEquals("Kimetsu no Yaiba", animeCard.title)
        assertEquals("Demon Slayer", animeCard.titleEnglish)
        assertEquals(26, animeCard.episodes)
        assertEquals(8.5, animeCard.score, 0.01)
        assertEquals(2019, animeCard.year)
    }

    @Test
    fun `test anime without MAL ID should use default value`() {
        // Given
        val medium = SearchAnimeQuery.Medium(
            id = 12345,
            idMal = Optional.absent(),
            // ... otros campos
        )

        // When
        val animeCard = medium.toAnimeCard()

        // Then
        assertEquals(0, animeCard.malId)
        assertEquals(12345, animeCard.aniListId)
    }

    @Test
    fun `test score conversion from 0-100 to 0-10`() {
        val testCases = listOf(
            100 to 10.0,
            85 to 8.5,
            0 to 0.0,
            null to 0.0
        )

        testCases.forEach { (input, expected) ->
            val medium = createMediumWithScore(input)
            val animeCard = medium.toAnimeCard()
            assertEquals(expected, animeCard.score, 0.01)
        }
    }
}
```

### 7.2 Integration Tests

**Ubicación:** `androidTest/data/repository/`

**AnimeRepositoryIntegrationTest.kt:**
```kotlin
@HiltAndroidTest
class AnimeRepositoryIntegrationTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: AnimeRepository

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun `search anime using AniList should return results`() = runBlocking {
        // Given
        val query = "Demon Slayer"

        // When
        val result = repository.searchAnimes(query, page = 1)

        // Then
        assertTrue(result.isSuccess)
        val animes = result.getOrNull()
        assertNotNull(animes)
        assertTrue(animes!!.isNotEmpty())

        // Verify structure
        val firstAnime = animes.first()
        assertNotNull(firstAnime.aniListId)
        assertTrue(firstAnime.title.isNotEmpty())
        assertTrue(firstAnime.imageUrl.isNotEmpty())
    }

    @Test
    fun `get anime details should include all expected fields`() = runBlocking {
        // Given - Kimetsu no Yaiba AniList ID
        val aniListId = 101922

        // When
        val result = repository.getAnimeDetailsById(aniListId)

        // Then
        assertTrue(result.isSuccess)
        val anime = result.getOrNull()
        assertNotNull(anime)

        assertEquals(aniListId, anime!!.aniListId)
        assertNotNull(anime.malId)
        assertTrue(anime.title.isNotEmpty())
        assertTrue(anime.synopsis?.isNotEmpty() ?: false)
        assertTrue(anime.genres.isNotEmpty())
        assertTrue(anime.score > 0.0)
    }

    @Test
    fun `seasonal anime should return current season`() = runBlocking {
        // When
        val result = repository.searchAnimeSeasonNow(page = 1)

        // Then
        assertTrue(result.isSuccess)
        val animes = result.getOrNull()
        assertNotNull(animes)
        assertTrue(animes!!.isNotEmpty())

        // Verify all are from current season
        val (currentSeason, currentYear) = getCurrentSeasonAndYear()
        animes.forEach { anime ->
            // El año debe ser actual o muy cercano
            assertTrue(
                anime.year == currentYear || anime.year == currentYear - 1,
                "Anime year ${anime.year} is not current season"
            )
        }
    }
}
```

### 7.3 Manual Testing Checklist

**Crear checklist en Issue o documento:**

#### Search & Discovery
- [ ] Text search returns relevant results
- [ ] Pagination works correctly
- [ ] Empty search handles gracefully
- [ ] Filters (format, status, genre) work
- [ ] Seasonal anime shows current season
- [ ] Top animes sorted by score
- [ ] Trending animes show popular content
- [ ] Random anime returns different results

#### Anime Details
- [ ] All metadata displays correctly
- [ ] Images load properly (cover, banner)
- [ ] Trailer plays if available
- [ ] Synopsis formats correctly (no HTML tags)
- [ ] Genres and tags display
- [ ] Studios show correctly
- [ ] Score and popularity accurate
- [ ] Relations display (sequels, prequels)
- [ ] External links work (Crunchyroll, etc.)
- [ ] Airing schedule accurate for ongoing

#### Characters
- [ ] Character list loads for anime
- [ ] Voice actors display correctly
- [ ] Character details page works
- [ ] Character images load
- [ ] Anime appearances list correctly
- [ ] Random character works

#### Edge Cases
- [ ] Anime without MAL ID handles correctly
- [ ] Anime without episodes (upcoming) works
- [ ] Adult content flagged appropriately
- [ ] Missing fields don't crash app
- [ ] Network errors handled gracefully
- [ ] Rate limiting doesn't cause crashes

#### Performance
- [ ] Response times acceptable (<2s)
- [ ] Images lazy load correctly
- [ ] Cache working (repeat queries faster)
- [ ] No memory leaks
- [ ] Pagination smooth

### 7.4 Regression Testing

**Comparar resultados Jikan vs AniList:**

```kotlin
// Test helper para comparar respuestas
@Test
fun `compare Jikan and AniList results for same anime`() = runBlocking {
    // Given - Usar MAL ID conocido
    val malId = 38000  // Kimetsu no Yaiba

    // When - Fetch from both APIs
    val jikanResult = fetchFromJikan(malId)
    val aniListResult = fetchFromAniList(malId)  // Usar idMal query

    // Then - Compare key fields
    assertEquals(jikanResult.title, aniListResult.title)
    assertEquals(jikanResult.episodes, aniListResult.episodes)
    assertEquals(jikanResult.type, aniListResult.type)

    // Scores pueden diferir ligeramente
    assertEquals(jikanResult.score, aniListResult.score, 0.5)

    // Géneros pueden diferir en formato pero contenido similar
    assertTrue(jikanResult.genres.size > 0)
    assertTrue(aniListResult.genres.size > 0)
}
```

---

## 🧹 Fase 8: Limpieza y Optimización

### 8.1 Eliminar Código de Jikan

**Una vez validada la migración:**

1. **Eliminar JikanApiService.kt**
```bash
rm app/src/main/java/com/yumedev/seijakulist/data/remote/api/JikanApiService.kt
```

2. **Eliminar DTOs de Jikan**
```bash
rm -rf app/src/main/java/com/yumedev/seijakulist/data/remote/models/*
# Mantener solo los de AniList si creaste carpeta separada
```

3. **Eliminar Mappers antiguos**
```bash
# Revisar y eliminar mappers obsoletos
rm app/src/main/java/com/yumedev/seijakulist/data/mapper/AnimeDetailsMapper.kt
# ... otros mappers de Jikan
```

4. **Limpiar Repository**
```kotlin
// Remover código condicional (if useAniList)
// Dejar solo implementación AniList
class AnimeRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient,  // Solo esto
    private val localDataSource: LocalAnimeDataSource
) : AnimeRepository {
    // Código limpio sin condicionales
}
```

5. **Actualizar AppModule.kt**
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // Remover provideJikanRetrofit, provideJikanApiService

    @Provides
    @Singleton
    fun provideApolloClient(): ApolloClient {
        return AniListApiClient().apolloClient
    }

    @Provides
    @Singleton
    fun provideAnimeRepository(
        apolloClient: ApolloClient,  // Solo AniList
        localDataSource: LocalAnimeDataSource
    ): AnimeRepository {
        return AnimeRepositoryImpl(apolloClient, localDataSource)
    }
}
```

6. **Eliminar dependencias de Retrofit (si ya no se usa)**
```kotlin
// build.gradle (app) - Remover si solo se usaba para Jikan
// implementation("com.squareup.retrofit2:retrofit:2.9.0")
// implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// Mantener OkHttp si Apollo lo usa
implementation("com.squareup.okhttp3:okhttp:4.12.0")
```

### 8.2 Optimizaciones de GraphQL

**8.2.1 Usar Fragments para Evitar Duplicación**

Las queries deben reutilizar fragments:
```graphql
# queries/GetAnimeDetails.graphql
query GetAnimeDetails($id: Int) {
  Media(id: $id, type: ANIME) {
    ...MediaFields  # Reutilizar fragment
    # Solo campos adicionales específicos aquí
    relations { ... }
    characters { ... }
  }
}
```

**8.2.2 Paginación Eficiente**

```kotlin
// Repository - Implementar paginación con cursor
suspend fun searchAnimesWithCursor(
    query: String,
    cursor: String? = null
): Result<PaginatedResult<AnimeCard>> {
    val response = apolloClient.query(
        SearchAnimeQuery(
            search = Optional.present(query),
            page = cursor?.toIntOrNull() ?: 1
        )
    ).execute()

    val pageInfo = response.data?.page?.pageInfo
    val animes = response.data?.page?.media
        ?.filterNotNull()
        ?.map { it.toAnimeCard() }
        ?: emptyList()

    return Result.success(
        PaginatedResult(
            items = animes,
            nextCursor = if (pageInfo?.hasNextPage == true) {
                (pageInfo.currentPage + 1).toString()
            } else null,
            hasMore = pageInfo?.hasNextPage ?: false
        )
    )
}
```

**8.2.3 Cache Normalizado de Apollo**

```kotlin
// AniListApiClient.kt - Mejorar cache
val apolloClient = ApolloClient.Builder()
    .serverUrl(BASE_URL)
    .normalizedCache(
        normalizedCacheFactory = MemoryCacheFactory(
            maxSizeBytes = 10 * 1024 * 1024
        ),
        cacheKeyGenerator = object : CacheKeyGenerator {
            override fun cacheKeyForObject(
                obj: Map<String, Any?>,
                context: CacheKeyGeneratorContext
            ): CacheKey? {
                // Usar id como cache key
                val id = obj["id"] as? Int ?: return null
                val typename = obj["__typename"] as? String ?: return null
                return CacheKey("$typename:$id")
            }
        }
    )
    .build()
```

**8.2.4 Batching de Queries (si es necesario)**

Para múltiples queries simultáneas:
```kotlin
suspend fun getMultipleAnimeDetails(ids: List<Int>): Result<List<AnimeDetail>> {
    // Apollo soporta batching automático
    val deferredResults = ids.map { id ->
        async(Dispatchers.IO) {
            apolloClient.query(GetAnimeDetailsQuery(id = Optional.present(id)))
                .execute()
        }
    }

    val results = deferredResults.awaitAll()
    val animes = results.mapNotNull { it.data?.media?.toAnimeDetail() }

    return Result.success(animes)
}
```

### 8.3 Manejo de Errores Mejorado

```kotlin
sealed class ApiError : Exception() {
    data class NetworkError(override val message: String) : ApiError()
    data class GraphQLError(val errors: List<String>) : ApiError()
    data class NotFoundError(val id: Int) : ApiError()
    object RateLimitError : ApiError()
    data class UnknownError(override val message: String) : ApiError()
}

// Repository
suspend fun searchAnimes(query: String, page: Int): Result<List<AnimeCard>> {
    return try {
        val response = apolloClient.query(
            SearchAnimeQuery(
                search = Optional.present(query),
                page = Optional.present(page)
            )
        ).execute()

        when {
            response.hasErrors() -> {
                val errorMessages = response.errors?.map { it.message } ?: listOf("Unknown error")
                Result.failure(ApiError.GraphQLError(errorMessages))
            }
            response.data == null -> {
                Result.failure(ApiError.NotFoundError(0))
            }
            else -> {
                val animes = response.data!!.page?.media
                    ?.filterNotNull()
                    ?.map { it.toAnimeCard() }
                    ?: emptyList()
                Result.success(animes)
            }
        }
    } catch (e: IOException) {
        Result.failure(ApiError.NetworkError("Network error: ${e.message}"))
    } catch (e: Exception) {
        Result.failure(ApiError.UnknownError(e.message ?: "Unknown error"))
    }
}
```

### 8.4 Rate Limiting

```kotlin
// RateLimiter.kt
class AniListRateLimiter {
    companion object {
        private const val MAX_REQUESTS_PER_MINUTE = 90
        private const val WINDOW_MS = 60_000L
    }

    private val requestTimestamps = mutableListOf<Long>()

    suspend fun <T> throttle(block: suspend () -> T): T {
        synchronized(requestTimestamps) {
            val now = System.currentTimeMillis()

            // Limpiar timestamps antiguos
            requestTimestamps.removeAll { it < now - WINDOW_MS }

            // Si estamos en el límite, esperar
            if (requestTimestamps.size >= MAX_REQUESTS_PER_MINUTE) {
                val oldestTimestamp = requestTimestamps.first()
                val waitTime = WINDOW_MS - (now - oldestTimestamp)
                if (waitTime > 0) {
                    delay(waitTime)
                }
            }

            requestTimestamps.add(now)
        }

        return block()
    }
}

// Usar en AniListApiClient
class AniListApiClient @Inject constructor() {
    private val rateLimiter = AniListRateLimiter()

    suspend fun <T> executeQuery(
        query: Query<T>
    ): ApolloResponse<T> {
        return rateLimiter.throttle {
            apolloClient.query(query).execute()
        }
    }
}
```

---

## ⚠️ Consideraciones Importantes

### IDs: AniList vs MAL

**Problema:** Tu app actualmente usa MAL IDs. AniList tiene su propio sistema de IDs.

**Soluciones:**

1. **Usar ambos IDs** (Recomendado):
```kotlin
data class AnimeCard(
    val malId: Int,        // Mantener para compatibilidad
    val aniListId: Int?,   // Nuevo
    // ... otros campos
)
```

2. **Búsqueda por MAL ID:**
AniList soporta buscar por `idMal`:
```graphql
query GetAnimeByMalId($malId: Int) {
  Media(idMal: $malId, type: ANIME) {
    id
    idMal
    # ... otros campos
  }
}
```

3. **Migración de Base de Datos Local:**
Si tienes animes guardados localmente con MAL ID:
```kotlin
// Migración de Room
@Migration(from = X, to = X+1)
val MIGRATION_ADD_ANILIST_ID = object : Migration(X, X+1) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE anime ADD COLUMN anilist_id INTEGER")
    }
}

// Fetch AniList IDs para animes existentes
suspend fun migrateLocalAnimeIds() {
    val localAnimes = localDataSource.getAllAnimes()

    localAnimes.forEach { anime ->
        val aniListId = fetchAniListIdFromMalId(anime.malId)
        if (aniListId != null) {
            localDataSource.updateAniListId(anime.malId, aniListId)
        }
    }
}
```

### Features No Disponibles en AniList

**1. Themes (Openings/Endings)**

AniList NO tiene esta información. Opciones:

- **Opción A:** Usar [AnimeThemes.moe API](https://animethemes.moe) como complemento
```kotlin
// Crear servicio adicional para themes
interface AnimeThemesApiService {
    @GET("anime/{mal_id}")
    suspend fun getThemes(@Path("mal_id") malId: Int): AnimeThemesResponse
}
```

- **Opción B:** Mantener llamada a Jikan solo para themes (híbrido)
- **Opción C:** Eliminar feature de themes

**2. Producers (separados de Studios)**

AniList solo tiene `studios`. Si necesitas productores:
- Usar campo `studios` para ambos
- O hacer query adicional a otra API

**3. Episode Details Individuales**

AniList tiene info de episodios pero limitada. Para metadata detallada de episodios, puede requerir API complementaria.

### Autenticación (Opcional)

AniList soporta autenticación OAuth2 para features avanzadas:
- Actualizar listas del usuario
- Marcar como favorito
- Escribir reviews
- Obtener recomendaciones personalizadas

**Si decides implementar auth:**

```kotlin
// AniListAuthManager.kt
class AniListAuthManager @Inject constructor(
    private val context: Context
) {
    companion object {
        private const val CLIENT_ID = "YOUR_CLIENT_ID"
        private const val REDIRECT_URI = "seijakulist://anilist-auth"
    }

    fun startOAuthFlow() {
        val authUrl = "https://anilist.co/api/v2/oauth/authorize" +
                "?client_id=$CLIENT_ID" +
                "&redirect_uri=$REDIRECT_URI" +
                "&response_type=token"

        // Abrir navegador
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(authUrl))
        context.startActivity(intent)
    }

    suspend fun exchangeToken(code: String): String {
        // Exchange authorization code for access token
        // Implementar según docs de AniList
    }
}

// Agregar token a Apollo Client
val apolloClient = ApolloClient.Builder()
    .serverUrl("https://graphql.anilist.co")
    .addHttpHeader("Authorization", "Bearer $accessToken")
    .build()
```

### Rate Limiting

- **Jikan:** 60 requests/min, 3 requests/sec
- **AniList:** 90 requests/min, sin límite por segundo

Más permisivo, pero implementar throttling es buena práctica.

### Migración de Deep Links

Si tu app usa deep links con MAL IDs:
```
seijakulist://anime/38000
```

Necesitarás manejar ambos:
```kotlin
// DeepLinkHandler.kt
suspend fun handleAnimeDeepLink(id: String, idType: String) {
    when (idType) {
        "mal" -> {
            // Convertir MAL ID → AniList ID
            val aniListId = repository.getAniListIdFromMalId(id.toInt())
            navigateToAnimeDetail(aniListId)
        }
        "anilist" -> {
            navigateToAnimeDetail(id.toInt())
        }
    }
}
```

---

## 📅 Cronograma Estimado

### Semana 1-2: Setup y Preparación
- [ ] Agregar dependencias Apollo GraphQL
- [ ] Descargar schema de AniList
- [ ] Crear estructura de carpetas para queries
- [ ] Investigar API de AniList (queries necesarias)
- [ ] Definir todos los fragments y queries GraphQL
- [ ] Configurar DI para Apollo Client

### Semana 3-4: Implementación Core
- [ ] Crear AniListApiClient
- [ ] Implementar mappers AniList → Domain Models
- [ ] Actualizar Repository con implementación híbrida
- [ ] Migrar endpoints prioritarios (search, details, characters)
- [ ] Testing básico de endpoints migrados

### Semana 5: Migración Features Secundarias
- [ ] Migrar seasonal animes
- [ ] Migrar top animes, trending
- [ ] Migrar random anime/character
- [ ] Implementar recommendations
- [ ] Manejar features sin equivalente (themes, producers)

### Semana 6: Testing y Validación
- [ ] Unit tests para mappers
- [ ] Integration tests para repository
- [ ] Manual testing exhaustivo
- [ ] Regression testing (comparar con Jikan)
- [ ] Performance testing
- [ ] Fix de bugs encontrados

### Semana 7: Optimización
- [ ] Implementar cache normalizado
- [ ] Optimizar queries GraphQL
- [ ] Implementar rate limiting
- [ ] Optimizar imágenes y lazy loading
- [ ] Code review y refactoring

### Semana 8: Limpieza y Lanzamiento
- [ ] Eliminar código de Jikan
- [ ] Limpiar dependencies no usadas
- [ ] Actualizar documentación
- [ ] Beta testing con usuarios
- [ ] Migración de DB local (si necesario)
- [ ] Release a producción

**Total estimado: 2 meses**

---

## 📚 Recursos Adicionales

### Documentación
- [AniList GraphQL Docs](https://anilist.gitbook.io/anilist-apiv2-docs/)
- [Apollo Android Docs](https://www.apollographql.com/docs/kotlin/)
- [AniList GraphiQL](https://anilist.co/graphiql)

### APIs Complementarias (si es necesario)
- [AnimeThemes.moe](https://animethemes.moe) - Para openings/endings
- [Jikan](https://jikan.moe) - Mantener para features específicas

### Herramientas
- [GraphQL Code Generator](https://www.graphql-code-generator.com/) - Opcional
- [Postman](https://www.postman.com/graphql/) - Para testing de queries

---

## ✅ Checklist Final

### Antes de Empezar
- [ ] Hacer backup del código actual
- [ ] Crear branch de migración: `git checkout -b migration/anilist`
- [ ] Documentar estado actual (screenshots, videos)
- [ ] Comunicar a usuarios sobre cambios futuros

### Durante Migración
- [ ] Commits frecuentes y descriptivos
- [ ] Mantener CI/CD funcionando
- [ ] Testing continuo
- [ ] Documentar decisiones importantes

### Antes de Lanzar
- [ ] Todos los tests pasan
- [ ] Performance acceptable
- [ ] Sin memory leaks
- [ ] Changelog actualizado
- [ ] Versión bumped (e.g., 2.0.0 para breaking change)
- [ ] Beta testing completado
- [ ] Backup plan si hay problemas

---

## 🎯 Conclusión

Esta migración es **compleja pero factible** gracias a tu arquitectura limpia actual. Los cambios principales están en el **Data Layer**, mientras que **Domain y UI** pueden mantenerse relativamente estables.

**Recomendaciones finales:**
1. ✅ Usa migración **gradual/híbrida** para reducir riesgos
2. ✅ Testea exhaustivamente antes de eliminar Jikan
3. ✅ Documenta todo el proceso
4. ✅ Considera features nuevas de AniList (relations, external links)
5. ✅ Mantén compatibilidad con MAL IDs si es posible
6. ⚠️ Planifica qué hacer con features sin equivalente (themes)

**Próximos pasos:**
1. Revisar este plan con el equipo
2. Decidir sobre features a mantener/eliminar
3. Empezar con Fase 1: Setup
4. Iterar y ajustar según avances

¡Buena suerte con la migración! 🚀

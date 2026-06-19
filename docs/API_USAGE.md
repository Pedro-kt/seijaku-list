# Guía de Uso de AniList GraphQL API

## Descripción General

SeijakuList utiliza exclusivamente **AniList GraphQL API** para obtener datos de anime, personajes y schedule.

**Documentación oficial**: https://anilist.gitbook.io/anilist-apiv2-docs/

**GraphQL Explorer**: https://anilist.co/graphiql

---

## Tabla de Contenidos

1. [Setup de Apollo Client](#setup-de-apollo-client)
2. [Queries Implementadas](#queries-implementadas)
3. [Tipos y Enums](#tipos-y-enums)
4. [Ejemplos de Uso](#ejemplos-de-uso)
5. [Rate Limiting](#rate-limiting)
6. [Mappers](#mappers)
7. [Errores Comunes](#errores-comunes)

---

## Setup de Apollo Client

### Dependencias (build.gradle.kts)

```kotlin
plugins {
    id("com.apollographql.apollo3") version "4.0.0"
}

dependencies {
    implementation("com.apollographql.apollo3:apollo-runtime:4.0.0")
    implementation("com.apollographql.apollo3:apollo-normalized-cache:4.0.0")
}

apollo {
    service("anilist") {
        packageName.set("com.yumedev.seijakulist")
        schemaFile.set(file("src/main/graphql/schema.graphqls"))
        introspection {
            endpointUrl.set("https://graphql.anilist.co")
            schemaFile.set(file("src/main/graphql/schema.graphqls"))
        }
    }
}
```

### ApolloClientProvider

```kotlin
object ApolloClientProvider {
    private const val BASE_URL = "https://graphql.anilist.co"

    fun provideApolloClient(): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(BASE_URL)
            .addHttpHeader("Content-Type", "application/json")
            .addHttpHeader("Accept", "application/json")
            .build()
    }
}
```

### Inyección con Hilt

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApolloClient(): ApolloClient {
        return ApolloClientProvider.provideApolloClient()
    }
}
```

---

## Queries Implementadas

### 1. SearchAnime.graphql

**Propósito**: Búsqueda de anime con filtros avanzados

```graphql
query SearchAnime(
  $query: String,
  $page: Int = 1,
  $perPage: Int = 25,
  $sort: [MediaSort],
  $format: MediaFormat,
  $status: MediaStatus,
  $genre: String,
  $genres: [String]
) {
  Page(page: $page, perPage: $perPage) {
    pageInfo {
      total
      currentPage
      lastPage
      hasNextPage
    }
    media(
      search: $query,
      type: ANIME,
      sort: $sort,
      format: $format,
      status: $status,
      genre: $genre,
      genre_in: $genres
    ) {
      id
      idMal
      title {
        romaji
        english
        native
      }
      coverImage {
        large
        medium
      }
      averageScore
      status
      format
      genres
      seasonYear
      episodes
    }
  }
}
```

**Uso**:
```kotlin
val response = apolloClient.query(
    SearchAnimeQuery(
        query = Optional.present("Naruto"),
        page = Optional.present(1),
        perPage = Optional.present(25),
        sort = Optional.present(listOf(MediaSort.POPULARITY_DESC))
    )
).execute()

val animes = response.data?.Page?.media?.mapNotNull { it?.toAnimeCard() } ?: emptyList()
```

### 2. GetAnimeDetail.graphql

**Propósito**: Obtener detalles completos de un anime

```graphql
query GetAnimeDetail($id: Int) {
  Media(idMal: $id, type: ANIME) {
    id
    idMal
    title {
      romaji
      english
      native
    }
    coverImage {
      extraLarge
      large
    }
    bannerImage
    description
    format
    episodes
    status
    startDate { year month day }
    endDate { year month day }
    season
    seasonYear
    duration
    averageScore
    meanScore
    popularity
    favourites
    rankings { rank type allTime }
    genres
    tags {
      name
      rank
    }
    studios {
      nodes {
        name
      }
    }
    characters(role: MAIN, perPage: 10) {
      edges {
        node {
          id
          name { full native }
          image { large }
        }
        role
        voiceActors(language: JAPANESE) {
          id
          name { full native }
          image { large }
        }
      }
    }
    relations {
      edges {
        node {
          id
          idMal
          title { romaji }
          coverImage { large }
          format
        }
        relationType
      }
    }
    recommendations(perPage: 10) {
      edges {
        node {
          mediaRecommendation {
            id
            idMal
            title { romaji }
            coverImage { large }
            averageScore
          }
        }
      }
    }
    trailer {
      id
      site
    }
    source
    countryOfOrigin
  }
}
```

**Uso**:
```kotlin
val response = apolloClient.query(
    GetAnimeDetailQuery(id = Optional.present(malId))
).execute()

val animeDetail = response.data?.Media?.toAnimeDetail()
```

### 3. GetAiringScheduleByDay.graphql

**Propósito**: Obtener anime que se emiten en un rango de tiempo específico

```graphql
query GetAiringScheduleByDay(
  $airingAt_greater: Int!,
  $airingAt_lesser: Int!,
  $page: Int = 1,
  $perPage: Int = 50
) {
  Page(page: $page, perPage: $perPage) {
    airingSchedules(
      airingAt_greater: $airingAt_greater,
      airingAt_lesser: $airingAt_lesser,
      sort: TIME_DESC
    ) {
      id
      airingAt
      timeUntilAiring
      episode
      media {
        id
        idMal
        title {
          romaji
          english
          native
        }
        coverImage {
          large
          medium
        }
        averageScore
        status
        genres
        episodes
        format
      }
    }
  }
}
```

**Uso**:
```kotlin
// Calcular timestamps para Monday
val (startTimestamp, endTimestamp) = getTimestampRangeForDay("monday")

val response = apolloClient.query(
    GetAiringScheduleByDayQuery(
        airingAt_greater = startTimestamp,
        airingAt_lesser = endTimestamp,
        page = Optional.present(1),
        perPage = Optional.present(50)
    )
).execute()

val schedules = response.data?.Page?.airingSchedules?.mapNotNull { it?.toAnimeCard() }
```

### 4. GetCharacterDetail.graphql

**Propósito**: Obtener detalles de un personaje

```graphql
query GetCharacterDetail($id: Int!) {
  Character(id: $id) {
    id
    name {
      full
      native
      alternative
    }
    image {
      large
    }
    description
    gender
    dateOfBirth { year month day }
    age
    bloodType
    favourites
    media(perPage: 20) {
      edges {
        node {
          id
          idMal
          title { romaji }
          coverImage { large }
          averageScore
        }
        characterRole
      }
    }
  }
}
```

**Uso**:
```kotlin
val response = apolloClient.query(
    GetCharacterDetailQuery(id = characterId)
).execute()

val character = response.data?.Character?.toCharacterDetail()
```

---

## Tipos y Enums

### MediaSort (Ordenamiento)

```kotlin
enum class MediaSort {
    TRENDING_DESC,
    POPULARITY_DESC,
    SCORE_DESC,
    UPDATED_AT_DESC,
    START_DATE_DESC,
    // ... más opciones
}
```

**Uso común**:
```kotlin
// Trending
sort = listOf(MediaSort.TRENDING_DESC)

// Top (por score)
sort = listOf(MediaSort.SCORE_DESC, MediaSort.POPULARITY_DESC)

// Recientes
sort = listOf(MediaSort.START_DATE_DESC)
```

### MediaFormat (Formato)

```kotlin
enum class MediaFormat {
    TV,           // Serie de TV
    TV_SHORT,     // Serie corta de TV
    MOVIE,        // Película
    SPECIAL,      // Especial
    OVA,          // OVA
    ONA,          // ONA
    MUSIC         // Video musical
}
```

**Mapeo desde UI**:
```kotlin
fun mapFilterToMediaFormat(filter: String): MediaFormat? {
    return when (filter.lowercase()) {
        "tv" -> MediaFormat.TV
        "movie" -> MediaFormat.MOVIE
        "ova" -> MediaFormat.OVA
        "ona" -> MediaFormat.ONA
        "special" -> MediaFormat.SPECIAL
        "music" -> MediaFormat.MUSIC
        else -> null
    }
}
```

### MediaStatus (Estado)

```kotlin
enum class MediaStatus {
    RELEASING,    // En emisión
    FINISHED,     // Finalizado
    NOT_YET_RELEASED,  // No estrenado
    CANCELLED,    // Cancelado
    HIATUS        // En pausa
}
```

**Uso**:
```kotlin
// Anime en emisión
status = MediaStatus.RELEASING

// Anime finalizados
status = MediaStatus.FINISHED
```

### MediaSeason (Temporada)

```kotlin
enum class MediaSeason {
    WINTER,   // Invierno (Jan, Feb, Mar)
    SPRING,   // Primavera (Apr, May, Jun)
    SUMMER,   // Verano (Jul, Aug, Sep)
    FALL      // Otoño (Oct, Nov, Dec)
}
```

**Cálculo de temporada actual**:
```kotlin
fun getCurrentSeason(): MediaSeason {
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
    return when (currentMonth) {
        12, 1, 2 -> MediaSeason.WINTER
        3, 4, 5 -> MediaSeason.SPRING
        6, 7, 8 -> MediaSeason.SUMMER
        9, 10, 11 -> MediaSeason.FALL
        else -> MediaSeason.WINTER
    }
}
```

---

## Ejemplos de Uso

### Trending Anime

```kotlin
suspend fun getTrendingAnime(page: Int = 1): List<AnimeCard> {
    val response = apolloClient.query(
        SearchAnimeQuery(
            page = Optional.present(page),
            perPage = Optional.present(25),
            sort = Optional.present(listOf(MediaSort.TRENDING_DESC))
        )
    ).execute()

    return response.data?.Page?.media?.mapNotNull { it?.toAnimeCard() } ?: emptyList()
}
```

### Top Anime por Formato

```kotlin
suspend fun getTopAnimeByFormat(format: MediaFormat, page: Int = 1): List<AnimeCard> {
    val response = apolloClient.query(
        SearchAnimeQuery(
            format = Optional.present(format),
            sort = Optional.present(listOf(MediaSort.SCORE_DESC, MediaSort.POPULARITY_DESC)),
            page = Optional.present(page),
            perPage = Optional.present(25)
        )
    ).execute()

    return response.data?.Page?.media?.mapNotNull { it?.toAnimeCard() } ?: emptyList()
}
```

### Anime de Temporada Actual

```kotlin
suspend fun getCurrentSeasonAnime(page: Int = 1): List<AnimeCard> {
    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val currentSeason = getCurrentSeason()

    return getSeasonalAnime(
        season = currentSeason,
        year = currentYear,
        page = page
    )
}
```

### Búsqueda por Género

```kotlin
suspend fun getAnimeByGenre(genre: String, page: Int = 1): List<AnimeCard> {
    val response = apolloClient.query(
        SearchAnimeQuery(
            genre = Optional.present(genre),
            sort = Optional.present(listOf(MediaSort.POPULARITY_DESC)),
            page = Optional.present(page),
            perPage = Optional.present(25)
        )
    ).execute()

    return response.data?.Page?.media?.mapNotNull { it?.toAnimeCard() } ?: emptyList()
}
```

### Próximos Estrenos

```kotlin
suspend fun getUpcomingAnime(page: Int = 1): List<AnimeCard> {
    val response = apolloClient.query(
        SearchAnimeQuery(
            status = Optional.present(MediaStatus.NOT_YET_RELEASED),
            sort = Optional.present(listOf(MediaSort.POPULARITY_DESC)),
            page = Optional.present(page),
            perPage = Optional.present(25)
        )
    ).execute()

    return response.data?.Page?.media?.mapNotNull { it?.toAnimeCard() } ?: emptyList()
}
```

---

## Rate Limiting

### Límites de AniList API

**Límites oficiales**:
- 90 requests por minuto
- ~1.5 requests por segundo

### RequestThrottler

**Implementación**:
```kotlin
class RequestThrottler @Inject constructor() {
    private val minDelay = 500L // 500ms entre requests
    private var lastRequestTime = 0L

    suspend fun <T> throttle(block: suspend () -> T): T? {
        val currentTime = System.currentTimeMillis()
        val timeSinceLastRequest = currentTime - lastRequestTime

        if (timeSinceLastRequest < minDelay) {
            delay(minDelay - timeSinceLastRequest)
        }

        lastRequestTime = System.currentTimeMillis()

        return try {
            block()
        } catch (e: ApolloException) {
            Log.e("RequestThrottler", "Apollo error: ${e.message}")
            null
        } catch (e: Exception) {
            Log.e("RequestThrottler", "Unexpected error: ${e.message}")
            null
        }
    }
}
```

**Uso en ViewModel**:
```kotlin
private fun loadData() {
    viewModelScope.launch {
        _isLoading.value = true

        val result = requestThrottler.throttle {
            repository.getTrendingAnime()
        }

        if (result != null) {
            _animeList.value = result
        } else {
            _errorMessage.value = "Error al cargar los datos"
        }

        _isLoading.value = false
    }
}
```

### Batch Processing

**Para enriquecimiento de datos**:
```kotlin
private suspend fun enrichMissingDetails() {
    val incomplete = animes.filter { it.synopsis == null }

    // Procesar en lotes de 2, esperar 1s entre cada lote
    incomplete.chunked(2).forEach { batch ->
        batch.forEach { anime ->
            try {
                val detail = getAnimeDetailUseCase(anime.malId)
                updateAnime(detail)
            } catch (e: Exception) {
                Log.e("Enrich", "Error: ${e.message}")
            }
        }
        delay(1000L) // 2 requests por segundo
    }
}
```

---

## Mappers

### SearchAnimeQuery.Medium → AnimeCard

```kotlin
fun SearchAnimeQuery.Medium.toAnimeCard(): AnimeCard {
    val schedule = airingSchedule?.edges?.firstOrNull()?.node?.let { airingNode ->
        AiringScheduleInfo(
            airingAt = airingNode.airingAt.toLong(),
            episode = airingNode.episode,
            timeUntilAiring = airingNode.timeUntilAiring.toLong(),
            dayOfWeek = getDayOfWeekFromTimestamp(airingNode.airingAt.toLong()),
            formattedTime = getFormattedTimeFromTimestamp(airingNode.airingAt.toLong())
        )
    }

    return AnimeCard(
        malId = idMal ?: 0,
        title = title?.romaji ?: title?.english ?: "",
        images = coverImage?.large ?: "",
        score = (averageScore ?: 0) / 10.0f,
        status = status?.rawValue ?: "",
        type = format?.rawValue ?: "",
        genres = genres?.mapNotNull { genre -> genre?.let { GenreDto(0, it) } } ?: emptyList(),
        year = seasonYear?.toString() ?: "",
        episodes = episodes?.toString() ?: "",
        airingSchedule = schedule
    )
}
```

### GetAnimeDetailQuery.Media → AnimeDetail

```kotlin
fun GetAnimeDetailQuery.Media.toAnimeDetail(): AnimeDetail {
    val genresList = genres?.filterNotNull() ?: emptyList()
    val studiosList = studios?.nodes?.mapNotNull { it?.name } ?: emptyList()

    val characters = characters?.edges?.mapNotNull { edge ->
        edge?.node?.let { char ->
            CharacterDto(
                malId = char.id,
                name = char.name?.full ?: "",
                images = char.image?.large ?: "",
                role = edge.role?.rawValue ?: ""
            )
        }
    } ?: emptyList()

    val relations = relations?.edges?.mapNotNull { edge ->
        edge?.node?.let { rel ->
            RelationDto(
                malId = rel.idMal ?: 0,
                title = rel.title?.romaji ?: "",
                type = edge.relationType?.rawValue ?: "",
                image = rel.coverImage?.large ?: ""
            )
        }
    } ?: emptyList()

    val recommendations = recommendations?.edges?.mapNotNull { edge ->
        edge?.node?.mediaRecommendation?.let { rec ->
            RecommendationDto(
                malId = rec.idMal ?: 0,
                title = rec.title?.romaji ?: "",
                image = rec.coverImage?.large ?: "",
                score = (rec.averageScore ?: 0) / 10.0f
            )
        }
    } ?: emptyList()

    return AnimeDetail(
        malId = idMal ?: 0,
        title = title?.romaji ?: "",
        titleEnglish = title?.english ?: "",
        titleJapanese = title?.native ?: "",
        images = coverImage?.extraLarge ?: coverImage?.large ?: "",
        trailer = trailer?.let { "https://www.youtube.com/watch?v=${it.id}" } ?: "",
        synopsis = description ?: "",
        typeAnime = format?.rawValue ?: "",
        episodes = episodes ?: 0,
        status = status?.rawValue ?: "",
        aired = "${startDate?.year}-${startDate?.month}-${startDate?.day}",
        duration = "${duration ?: 0} min",
        rating = "", // AniList no tiene este campo
        score = (averageScore ?: 0) / 10.0f,
        scoreBy = popularity ?: 0,
        rank = rankings?.firstOrNull()?.rank ?: 0,
        season = season?.rawValue ?: "",
        year = seasonYear ?: "No encontrado",
        studios = studiosList,
        genres = genresList,
        source = source?.rawValue ?: "",
        characters = characters,
        relations = relations,
        recommendations = recommendations
    )
}
```

### Helper Functions

```kotlin
fun getDayOfWeekFromTimestamp(timestamp: Long): String {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = timestamp * 1000
    }
    return when (calendar.get(Calendar.DAY_OF_WEEK)) {
        Calendar.MONDAY -> "Monday"
        Calendar.TUESDAY -> "Tuesday"
        Calendar.WEDNESDAY -> "Wednesday"
        Calendar.THURSDAY -> "Thursday"
        Calendar.FRIDAY -> "Friday"
        Calendar.SATURDAY -> "Saturday"
        Calendar.SUNDAY -> "Sunday"
        else -> ""
    }
}

fun getFormattedTimeFromTimestamp(timestamp: Long): String {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = timestamp * 1000
    }
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    return String.format("%02d:%02d", hour, minute)
}
```

---

## Errores Comunes

### Error 1: ApolloNetworkException

**Causa**: No hay conexión a internet

**Solución**:
```kotlin
try {
    val response = apolloClient.query(query).execute()
} catch (e: ApolloNetworkException) {
    Log.e("API", "No internet connection")
    // Mostrar mensaje al usuario
}
```

### Error 2: ApolloHttpException

**Causa**: Error del servidor (500, 404, etc.)

**Solución**:
```kotlin
try {
    val response = apolloClient.query(query).execute()
} catch (e: ApolloHttpException) {
    Log.e("API", "HTTP error: ${e.statusCode}")
    // Reintentar o mostrar error
}
```

### Error 3: Rate Limit Exceeded

**Causa**: Demasiadas requests

**Solución**: Usar RequestThrottler (ver sección Rate Limiting)

### Error 4: Optional.Absent vs null

**Problema**:
```kotlin
// ❌ INCORRECTO
SearchAnimeQuery(query = null)

// ✅ CORRECTO
SearchAnimeQuery(query = Optional.present("Naruto"))
// o
SearchAnimeQuery(query = Optional.absent())
```

**Explicación**: Apollo usa `Optional` para distinguir entre "no enviar este parámetro" y "enviar null"

### Error 5: Score Conversion

**Problema**: AniList usa 0-100, app usa 0-10

**Solución**:
```kotlin
score = (averageScore ?: 0) / 10.0f
```

### Error 6: Genre malId vs name

**Problema**: AniList usa nombres, app usa malId

**Solución**: Mapeo en PopularGenres.kt
```kotlin
val genreName = PopularGenres.getGenreById(malId)?.name ?: ""
repository.getAnimeByGenre(genreName)
```

---

## Testing GraphQL Queries

### GraphQL Playground

1. Ir a https://anilist.co/graphiql
2. Pegar query
3. Definir variables
4. Ejecutar y ver resultado

**Ejemplo**:
```graphql
query {
  Page(page: 1, perPage: 5) {
    media(type: ANIME, sort: TRENDING_DESC) {
      id
      idMal
      title {
        romaji
      }
      averageScore
    }
  }
}
```

### Test con Postman

```
POST https://graphql.anilist.co
Headers:
  Content-Type: application/json
  Accept: application/json

Body (raw JSON):
{
  "query": "query { Page(page: 1, perPage: 5) { media(type: ANIME, sort: TRENDING_DESC) { id title { romaji } } } }"
}
```

---

## Referencias

**Documentación Oficial**:
- API Docs: https://anilist.gitbook.io/anilist-apiv2-docs/
- GraphQL Reference: https://anilist.gitbook.io/anilist-apiv2-docs/overview/graphql
- Rate Limiting: https://anilist.gitbook.io/anilist-apiv2-docs/overview/rate-limiting

**Apollo Client**:
- Kotlin Docs: https://www.apollographql.com/docs/kotlin/
- Queries: https://www.apollographql.com/docs/kotlin/essentials/queries
- Error Handling: https://www.apollographql.com/docs/kotlin/essentials/errors

**Código en el proyecto**:
- Repositorio: `data/repository/AnimeAniListRepository.kt`
- Queries: `app/src/main/graphql/`
- Mappers: `data/mapper/anilist/`
- Use Cases: `domain/usecase/anilist/`

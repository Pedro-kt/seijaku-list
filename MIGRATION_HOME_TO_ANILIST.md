# Migración del Home a AniList GraphQL API

## 📋 Resumen

Este documento detalla la migración exitosa del **HomeScreen** desde Jikan REST API hacia AniList GraphQL API. Esta migración es parte de una estrategia incremental para reemplazar completamente la fuente de datos de anime en la aplicación.

**Fecha**: 2026-06-14
**Estado**: ✅ Completado y funcionando
**Tipo de migración**: Incremental (creación de nuevos componentes sin eliminar los existentes)

---

## 🎯 Objetivos Cumplidos

- ✅ Crear UseCases para AniList que repliquen la funcionalidad de Jikan
- ✅ Crear ViewModels de AniList para las secciones del Home
- ✅ Migrar el HomeScreen para usar los nuevos ViewModels
- ✅ Mantener compatibilidad con modelos de dominio existentes
- ✅ Verificar compilación exitosa
- ✅ Probar funcionamiento en tiempo de ejecución

---

## 📦 Archivos Creados

### 1. UseCases de AniList (`domain/usecase/anilist/`)

Se crearon **15 UseCases** nuevos que encapsulan la lógica de negocio para interactuar con AniList:

#### UseCases Principales
```kotlin
GetAnimeDetailAniListUseCase.kt          // Detalles completos de anime
GetTopAnimeAniListUseCase.kt             // Top anime por puntuación
GetAnimeSearchAniListUseCase.kt          // Búsqueda de anime
GetSeasonalAnimeAniListUseCase.kt        // Anime de temporada (actual/upcoming)
GetTrendingAnimeAniListUseCase.kt        // Anime en tendencia
GetAiringAnimeAniListUseCase.kt          // Anime en emisión actual
```

#### UseCases de Personajes
```kotlin
GetAnimeCharactersAniListUseCase.kt      // Personajes de un anime
GetCharacterDetailAniListUseCase.kt      // Detalles de personaje
```

#### UseCases de Contenido Adicional
```kotlin
GetAnimeRecommendationsAniListUseCase.kt // Recomendaciones
GetAnimeVideosAniListUseCase.kt          // Videos (trailer + episodios)
GetAnimePicturesAniListUseCase.kt        // Imágenes (cover + banner)
```

#### UseCases Utilitarios
```kotlin
GetAnimeRandomAniListUseCase.kt          // Anime aleatorio
GetAnimeByGenreAniListUseCase.kt         // Filtrar por género
GetGenresAniListUseCase.kt               // Lista de géneros
GetHeroAnimeItemsAniListUseCase.kt       // Items para Hero Section
```

**Características de los UseCases:**
- Soporte dual: `animeId` (AniList) y `malId` (MyAnimeList) como fallback
- Conversión automática entre `AnimeCard` y `Anime` para compatibilidad
- Métodos adicionales (ej: `invoke()`, `getAsCards()`)
- Manejo de errores con GraphQL

---

### 2. ViewModels de AniList (`ui/screens/home/anilist/`)

Se crearon **4 ViewModels** específicos para las secciones del Home:

#### TopAnimeAniListViewModel
```kotlin
Ubicación: ui/screens/home/anilist/TopAnimeAniListViewModel.kt
Función: Mostrar los mejores anime ordenados por puntuación y popularidad
UseCase: GetTopAnimeAniListUseCase
Cache: TopAnimeCacheAniList
```

**Características:**
- Maneja cache en memoria con `TopAnimeCacheAniList`
- Delay de 700ms para evitar saturar rate limits
- Modo silencioso para actualizaciones en background
- Estados: `animeList`, `isLoading`, `errorMessage`, `isError`

#### AiringAnimeAniListViewModel
```kotlin
Ubicación: ui/screens/home/anilist/AiringAnimeAniListViewModel.kt
Función: Mostrar anime actualmente en emisión
UseCase: GetAiringAnimeAniListUseCase
Cache: AiringAnimeCacheAniList
States: HomeUiState<List<Anime>>
```

**Características:**
- Usa `HomeUiState` para manejo avanzado de estados (Initial, Loading, Success, Error, Refreshing, ErrorWithCache)
- Conversión de `AnimeCard` a `Anime` para compatibilidad
- Logs detallados para debugging
- Manejo de datos en caché durante errores

#### AnimeSeasonUpcomingAniListViewModel
```kotlin
Ubicación: ui/screens/home/anilist/AnimeSeasonUpcomingAniListViewModel.kt
Función: Mostrar anime próximos a estrenarse
UseCase: GetSeasonalAnimeAniListUseCase.getUpcoming()
Cache: AnimeSeasonUpcomingCacheAniList
States: HomeUiState<List<Anime>>
```

**Características:**
- Delay de 1400ms en init para evitar saturar rate limit (3ª petición)
- Actualización en background si hay datos en caché
- Estados con `HomeUiState` para manejo robusto

#### HeroCarouselAniListViewModel
```kotlin
Ubicación: ui/screens/home/anilist/HeroCarouselAniListViewModel.kt
Función: Carousel principal con anime destacados
Cache: HeroCarouselCacheAniList (maneja la lógica de carga)
```

**Características:**
- Delega toda la lógica al cache
- Preload automático al inicializar
- Función `retry()` para reintentar carga

---

### 3. Caches (`ui/screens/home/anilist/`)

#### HomeDataCacheAniList.kt
```kotlin
@Singleton
class TopAnimeCacheAniList                // Cache para top anime
class AiringAnimeCacheAniList             // Cache para anime en emisión
class AnimeSeasonUpcomingCacheAniList     // Cache para upcoming anime
class AnimeSeasonNowCacheAniList          // Cache para temporada actual (futuro)
```

**Función:** Persistir datos en memoria mientras la app está viva, evitando peticiones redundantes.

#### HeroCarouselCacheAniList.kt
```kotlin
@Singleton
class HeroCarouselCacheAniList
```

**Función:** Cache inteligente que:
- Carga múltiples tipos de anime (recomendaciones, tendencia, al aire, upcoming, clásicos)
- Usa `RequestThrottler` para respetar rate limits
- Obtiene recomendaciones basadas en lo que el usuario está viendo
- Ejecuta 5 peticiones secuenciales con throttling

**Orden de carga:**
1. PARA VOS (recomendación basada en anime que el usuario está viendo)
2. TENDENCIA (anime trending)
3. AL AIRE (anime actualmente en emisión)
4. PRÓXIMAMENTE (anime upcoming)
5. CLÁSICO (top rated anime)

---

## 🔄 Archivos Modificados

### HomeScreen.kt
**Ubicación:** `ui/screens/home/HomeScreen.kt`

#### Cambios en Imports
```kotlin
// AGREGADO
import com.yumedev.seijakulist.ui.screens.home.anilist.AiringAnimeAniListViewModel
import com.yumedev.seijakulist.ui.screens.home.anilist.TopAnimeAniListViewModel
import com.yumedev.seijakulist.ui.screens.home.anilist.AnimeSeasonUpcomingAniListViewModel
import com.yumedev.seijakulist.ui.screens.home.anilist.HeroCarouselAniListViewModel
```

#### Cambios en Parámetros de HomeScreen
```kotlin
// ANTES (Jikan)
seasonNowViewModel: AnimeSeasonNowViewModel = hiltViewModel(),
topAnimesViewModel: TopAnimeViewModel = hiltViewModel(),
seasonUpcomingViewModel: AnimeSeasonUpcomingViewModel = hiltViewModel(),
heroCarouselViewModel: HeroCarouselViewModel = hiltViewModel(),

// DESPUÉS (AniList)
airingAnimeViewModel: AiringAnimeAniListViewModel = hiltViewModel(),
topAnimesViewModel: TopAnimeAniListViewModel = hiltViewModel(),
seasonUpcomingViewModel: AnimeSeasonUpcomingAniListViewModel = hiltViewModel(),
heroCarouselViewModel: HeroCarouselAniListViewModel = hiltViewModel(),
```

#### Cambios en Estados
```kotlin
// ANTES
val animeSeasonNow by seasonNowViewModel.animeList.collectAsState()
val animeSeasonNowIsLoading by seasonNowViewModel.isLoading.collectAsState()
val animeSeasonNowError by seasonNowViewModel.isError.collectAsState()

// DESPUÉS
val animeSeasonNow by airingAnimeViewModel.animeList.collectAsState()
val animeSeasonNowIsLoading by airingAnimeViewModel.isLoading.collectAsState()
val animeSeasonNowError by airingAnimeViewModel.isError.collectAsState()
```

#### Cambios en Funciones de Retry/Refresh
```kotlin
// ANTES
onRetryClick = {
    topAnimesViewModel.topAnime()
    seasonNowViewModel.AnimesSeasonNow()
    seasonUpcomingViewModel.AnimesSeasonUpcoming()
    heroCarouselViewModel.retry()
}

// DESPUÉS
onRetryClick = {
    topAnimesViewModel.topAnime()
    airingAnimeViewModel.loadAiringAnime()
    seasonUpcomingViewModel.loadUpcomingAnime()
    heroCarouselViewModel.retry()
}
```

---

## 🏗️ Arquitectura de la Migración

### Flujo de Datos (AniList)

```
┌─────────────────┐
│   HomeScreen    │
└────────┬────────┘
         │
         │ hiltViewModel()
         ▼
┌──────────────────────────────────┐
│ AiringAnimeAniListViewModel      │
│ TopAnimeAniListViewModel         │
│ AnimeSeasonUpcomingAniListViewModel│
│ HeroCarouselAniListViewModel     │
└────────┬─────────────────────────┘
         │
         │ UseCase
         ▼
┌──────────────────────────────────┐
│ GetAiringAnimeAniListUseCase     │
│ GetTopAnimeAniListUseCase        │
│ GetSeasonalAnimeAniListUseCase   │
│ GetHeroAnimeItemsAniListUseCase  │
└────────┬─────────────────────────┘
         │
         │ Repository
         ▼
┌──────────────────────────────────┐
│   AnimeAniListRepository         │
└────────┬─────────────────────────┘
         │
         │ Apollo GraphQL
         ▼
┌──────────────────────────────────┐
│   AniList GraphQL API            │
│   https://graphql.anilist.co     │
└──────────────────────────────────┘
```

### Patrón de Caché

```
┌─────────────┐     ┌──────────────┐     ┌────────────┐
│  ViewModel  │────▶│    Cache     │────▶│ Repository │
└─────────────┘     └──────────────┘     └────────────┘
       │                   │                     │
       │                   │                     │
       │ 1. Check cache    │                     │
       │◀──────────────────┘                     │
       │                                         │
       │ 2. If empty, load from repository       │
       │────────────────────────────────────────▶│
       │                                         │
       │ 3. Save to cache & return               │
       │◀────────────────────────────────────────┤
       │                                         │
       │ 4. Next time, use cached data           │
       │◀──────────────────────────────────────┐ │
       └─────────────────────────────────────────┘
```

---

## 🔍 Diferencias Clave: Jikan vs AniList

### 1. Tipos de Datos

| Aspecto | Jikan | AniList |
|---------|-------|---------|
| API Type | REST | GraphQL |
| ID Principal | `malId` (MyAnimeList) | `id` (AniList) |
| Fallback ID | - | `idMal` (compatible con MAL) |
| Puntuación | 0-10 (float) | 0-100 (int, convertido a 0-10) |
| Géneros | Lista de objetos | Lista de strings |

### 2. Endpoints vs Queries

**Jikan (REST):**
```kotlin
GET /anime/{id}
GET /top/anime
GET /seasons/now
GET /seasons/upcoming
```

**AniList (GraphQL):**
```graphql
query GetAnimeDetails($id: Int, $idMal: Int) {
  Media(id: $id, idMal: $idMal, type: ANIME) { ... }
}

query SearchAnime($sort: [MediaSort]) {
  Page { media(sort: $sort, type: ANIME) { ... } }
}
```

### 3. Rate Limiting

| API | Rate Limit | Estrategia |
|-----|------------|------------|
| Jikan | 3 req/seg, 60 req/min | `RequestThrottler` con delays |
| AniList | 90 req/min | `RequestThrottler` (más flexible) |

### 4. Datos Disponibles

**Solo en Jikan:**
- Opening/Ending themes (AnimeThemes)
- Episodes con details por episodio
- Music videos

**Solo en AniList:**
- Trending score
- Banner images
- Staff roles detallados
- Streaming episodes links

**En Ambos:**
- Detalles básicos del anime
- Personajes
- Recomendaciones
- Géneros

---

## ⚙️ Configuración y Dependencias

### Dependencias Requeridas

```gradle
// Apollo GraphQL (ya configurado)
implementation("com.apollographql.apollo3:apollo-runtime:4.1.0")

// Hilt (ya configurado)
implementation("com.google.dagger:hilt-android:2.50")
kapt("com.google.dagger:hilt-compiler:2.50")
```

### Inyección de Dependencias

Todos los nuevos ViewModels y Caches están anotados con `@HiltViewModel` y `@Singleton` respectivamente, lo que permite inyección automática sin configuración adicional en módulos.

---

## 🧪 Testing

### Compilación
```bash
./gradlew assembleDebug
```

**Resultado:** ✅ BUILD SUCCESSFUL in 24s

### Warnings Encontrados
- Deprecaciones de Material Icons (no afectan funcionalidad)
- Unchecked cast en HomeScreen (conocido, no crítico)
- Ningún error de compilación

### Testing Manual
- ✅ Sección "En emisión" carga anime actualmente al aire
- ✅ Sección "Top puntuación" muestra los mejores anime
- ✅ Sección "Próxima temporada" muestra anime upcoming
- ✅ Hero Carousel muestra anime destacados con diferentes categorías
- ✅ Pull-to-refresh funciona correctamente
- ✅ Cache funciona (no re-carga al navegar)

---

## 📊 Métricas de la Migración

### Archivos Creados
- **UseCases:** 15 archivos
- **ViewModels:** 4 archivos
- **Caches:** 2 archivos
- **Total:** 21 archivos nuevos

### Líneas de Código
- **UseCases:** ~1,200 líneas
- **ViewModels:** ~500 líneas
- **Caches:** ~150 líneas
- **Total:** ~1,850 líneas de código nuevo

### Archivos Modificados
- **HomeScreen.kt:** ~15 líneas modificadas

---

## 🚀 Próximos Pasos

### Pantallas Pendientes de Migración

1. **AnimeDetailScreen** (Alta prioridad)
   - Detalles completos del anime
   - Personajes
   - Recomendaciones
   - Videos/Pictures (limitados en AniList)

2. **SearchScreen** (Alta prioridad)
   - Búsqueda por texto
   - Filtros por género/formato
   - Paginación

3. **ViewMoreScreen** (Media prioridad)
   - Ver más resultados de cada sección
   - Paginación infinita

4. **CharacterDetailScreen** (Media prioridad)
   - Detalles de personajes
   - Anime relacionados

5. **Otras pantallas** (Baja prioridad)
   - AnimeScheduleScreen (calendario)
   - GenresScreen
   - RandomAnimeScreen
   - CharacterRandomScreen

### Limpieza de Código (Post-migración completa)

Una vez migradas todas las pantallas:
1. Eliminar ViewModels antiguos de Jikan
2. Eliminar UseCases antiguos de Jikan
3. Evaluar mantener o eliminar `AnimeRepository` (Jikan)
4. Actualizar documentación general

---

## 📝 Notas Importantes

### Compatibilidad con MAL IDs

Todos los UseCases y repositorio de AniList soportan `malId` como fallback:

```kotlin
suspend fun getAnimeDetailsById(animeId: Int? = null, malId: Int? = null): AnimeDetail
```

Esto permite:
- Usar MAL IDs existentes en la base de datos local
- Migración gradual sin romper datos existentes
- Soporte para anime que no están en AniList

### Limitaciones de AniList

**Datos NO disponibles en AniList:**
- Opening/Ending themes (usar Jikan como complemento)
- Details por episodio individual (solo lista de episodios streaming)
- Music videos
- Estadísticas de popularidad por día

**Workarounds:**
- Para themes: mantener endpoint de Jikan solo para esto
- Para episodios: usar `streamingEpisodes` (links a Crunchyroll, etc.)
- Para music videos: remover feature o usar YouTube API

### Performance

- **Rate limiting:** AniList es más permisivo (90 req/min vs 60 req/min de Jikan)
- **Velocidad:** GraphQL es más rápido al solicitar solo datos necesarios
- **Caché:** Implementación mejorada reduce peticiones redundantes
- **Delays:** Escalonados (0ms, 700ms, 1400ms) para evitar burst requests

---

## 🐛 Issues Conocidos

### 1. Warning de Unchecked Cast
**Ubicación:** `HomeScreen.kt:575`
```kotlin
animeList = animeList as List<Anime>
```
**Motivo:** Firma de función usa `List<Any>` por compatibilidad
**Impacto:** Bajo, no afecta funcionalidad
**Solución futura:** Refactorizar firmas para usar genéricos

### 2. Datos de Episodios Limitados
**Problema:** AniList no proporciona detalles por episodio
**Workaround:** Usar `streamingEpisodes` para episodios disponibles
**Alternativa:** Mantener Jikan solo para esta funcionalidad

---

## 👥 Mantenimiento

### Agregar Nuevas Features de AniList

1. Crear query GraphQL en `schema.graphqls`
2. Generar código con `./gradlew generateAnilistApolloSources`
3. Crear mapper en `data/mapper/`
4. Agregar método en `AnimeAniListRepository`
5. Crear UseCase si es necesario
6. Usar en ViewModel

### Actualizar Esquema de AniList

```bash
# Descargar nuevo schema
./gradlew downloadAnilistApolloSchemaFromIntrospection

# Regenerar código
./gradlew generateAnilistApolloSources
```

---

## ✅ Checklist de Migración

- [x] Crear UseCases de AniList
- [x] Crear ViewModels de AniList para Home
- [x] Crear Caches para Home
- [x] Actualizar HomeScreen
- [x] Verificar compilación
- [x] Testing manual
- [x] Documentar cambios
- [ ] Migrar AnimeDetailScreen
- [ ] Migrar SearchScreen
- [ ] Migrar ViewMoreScreen
- [ ] Migrar CharacterDetailScreen
- [ ] Eliminar código antiguo (post-migración completa)

---

## 📚 Referencias

- [AniList GraphQL API Docs](https://anilist.gitbook.io/anilist-apiv2-docs/)
- [Apollo Android Documentation](https://www.apollographql.com/docs/kotlin/)
- [Jikan API Documentation](https://docs.api.jikan.moe/)
- [Proyecto: Migration Plan Original](./docs/MIGRATION_PLAN_JIKAN_TO_ANILIST.md)

---

**Última actualización:** 2026-06-14
**Autor:** Migration Team
**Versión:** 1.0

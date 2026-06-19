# Seijaku List - Documentación Técnica

Esta documentación contiene información técnica detallada sobre la arquitectura, implementación y estructura del proyecto Seijaku List.

Para información general sobre el producto y sus características, consulta el [README principal](../README.md).

---

## Índice

- [Arquitectura](#arquitectura)
- [Stack tecnológico](#stack-tecnológico)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Base de datos local](#base-de-datos-local)
- [Integración con la API Jikan](#integración-con-la-api-jikan)
- [Patrones y convenciones](#patrones-y-convenciones)

---

## Arquitectura

```
Presentation (Jetpack Compose)
    └── Composables observan StateFlow via collectAsState()

 ViewModel (MVVM)
    └── Expone StateFlow, gestiona logica de UI y ciclo de vida

Domain Layer
    ├── Use Cases (GetAnimeDetailUseCase, etc.)
    └── Domain Models (Anime, AnimeDetail, AnimeEntityDomain, etc.)

Data Layer
    ├── AnimeRepository          → Jikan API (Retrofit)
    ├── AnimeLocalRepository     → Room SQLite
    └── FirestoreAnimeRepository → Firebase Firestore + Storage
```

### Patrones clave

- **StateFlow** para todo el estado reactivo. Sin LiveData.
- **Singleton cache** en los ViewModels del Home: las listas de anime persisten en memoria durante la sesion, evitando re-fetching al navegar hacia atras.
- **Mappers explicitos** entre capas: `AnimeEntity` ↔ `AnimeEntityDomain` ↔ `AnimeDetail`.
- **Room Migrations** para evolucionar el esquema sin perder datos de usuarios existentes (v6 → v13).
- **FileProvider** para compartir imagenes generadas con apps externas de forma segura.
- **Coroutines + Dispatchers** bien gestionados: IO para red/disco, Main para UI.

---

## Stack tecnológico

| Capa | Tecnologia |
|------|-----------|
| Lenguaje | Kotlin 2.0 |
| UI | Jetpack Compose + Material 3 |
| Arquitectura | MVVM + Clean Architecture |
| Inyeccion de dependencias | Hilt (Dagger) |
| Navegacion | Navigation Compose |
| Persistencia local | Room (SQLite) v13 |
| Red | Retrofit + Gson |
| Imagenes | Coil |
| Animaciones | Lottie + Compose Animations |
| Generacion de imagenes | Android Canvas API (Bitmap) |
| Compartir archivos | FileProvider + Intent.ACTION_SEND |
| Autenticacion | Firebase Auth |
| Base de datos en la nube | Firebase Firestore |
| Almacenamiento de archivos | Firebase Storage |
| Asincronía | Kotlin Coroutines + Flow |
| API externa | Jikan REST API v4 (MyAnimeList) |
| SDK minimo | API 26 (Android 8.0) |
| SDK objetivo | API 35 (Android 15) |

---

## Estructura del proyecto

```
com.yumedev.seijakulist/
├── data/
│   ├── local/
│   │   ├── dao/            AnimeDao, UserProfileDao
│   │   ├── entities/       AnimeEntity, UserProfile
│   │   ├── migration/      MIGRATION_6_7 ... MIGRATION_12_13
│   │   └── database/       AnimeDatabase (v13)
│   ├── mapper/local/       AnimeEntityMapper
│   ├── remote/
│   │   ├── api/            JikanApiService
│   │   └── models/         DTOs (anime, characters, episodes, videos, themes, forum, recommendations)
│   └── repository/
│       ├── AnimeRepository
│       ├── AnimeLocalRepository
│       └── FirestoreAnimeRepository
├── domain/
│   ├── models/             Anime, AnimeDetail, AnimeCard, AnimeEntityDomain,
│   │                       AnimeEpisode, AnimeVideos, AnimePicture, AnimeThemes,
│   │                       AnimeRecommendation, ForumTopic, CharacterDetail, Genre, ...
│   └── usecase/            GetAnimeDetailUseCase, ...
├── ui/
│   ├── components/         CardAnimesHome, CardAnimesHomeGrid, AnimeStatusChip,
│   │                       CustomDialog, AppScaffold, loaders, dialogs, ...
│   ├── navigation/         AppDestinations, NavGraph
│   ├── screens/
│   │   ├── auth_screen/    AuthScreen, LoginScreen, RegisterScreen, AuthViewModel
│   │   ├── home/           HomeScreen, AnimeSeasonNowViewModel, TopAnimeViewModel,
│   │   │                   AnimeSeasonUpcomingViewModel, AnimeRandomViewModel,
│   │   │                   AnimeScheduleViewModel, LocalAnimeIdsViewModel, ...
│   │   ├── detail/         AnimeDetailScreen, AnimeDetailViewModel
│   │   ├── local_anime_detail/ LocalAnimeDetailScreen, LocalAnimeDetailViewModel
│   │   ├── my_animes/      MyAnimesScreen, MyAnimesViewModel
│   │   ├── profile/        ProfileView, ProfileSetupView, SelectTop5Screen, ProfileViewModel
│   │   ├── search/         SearchScreen, SearchViewModel, GenresViewModel
│   │   ├── viewmore/       ViewMoreScreen, ViewMoreViewModel
│   │   ├── configuration/  ConfigurationScreen, SettingsViewModel
│   │   ├── novedades/      NovedadesScreen
│   │   ├── characters/     CharacterDetailScreen
│   │   ├── report/         ReportErrorScreen
│   │   └── policy_privacy/ PolicyPrivacyScreen
│   └── theme/              Color, Typography (Poppins), Shape, Theme
└── util/                   UserAction
```

---

## Base de datos local

**Room Database v13** — tabla `animes`:

```
-- Identificacion
malId           INT PRIMARY KEY    (ID de MyAnimeList)
title           TEXT
imageUrl        TEXT

-- Seguimiento del usuario
userScore       REAL               Puntuacion personal (0-10)
statusUser      TEXT               Viendo | Completado | Pendiente | Abandonado | Planeado
userOpiniun     TEXT               Resena personal
episodesWatched INT
totalEpisodes   INT
rewatchCount    INT
startDate       INTEGER            Timestamp en milisegundos
endDate         INTEGER            Timestamp en milisegundos

-- Datos del anime (guardados offline)
synopsis        TEXT
titleEnglish    TEXT
titleJapanese   TEXT
genres          TEXT               Separados por coma
studios         TEXT               Separados por coma
score           REAL               Puntaje MAL
scoreBy         INT                Cantidad de votos MAL
typeAnime       TEXT               TV | Movie | OVA | ONA | Special
duration        TEXT
season          TEXT
year            TEXT
status          TEXT
aired           TEXT
rank            INT
rating          TEXT               PG-13 | R | R+ | Rx
source          TEXT

-- Planificacion (solo relevante si statusUser = 'Planeado')
plannedPriority TEXT               Alta | Media | Baja
plannedNote     TEXT               Nota o motivo del plan
```

### Migraciones

El proyecto incluye migraciones de Room desde la versión 6 hasta la versión 13, permitiendo actualizaciones de esquema sin pérdida de datos:

- `MIGRATION_6_7` a `MIGRATION_12_13`
- Cada migración está documentada en el código con los cambios específicos aplicados

---

## Integración con la API Jikan

Consume la **Jikan REST API v4** — wrapper no oficial y gratuito de MyAnimeList. No requiere API key.

### Endpoints utilizados

| Categoria | Endpoint |
|-----------|----------|
| Temporada actual | `GET /seasons/now` |
| Top anime | `GET /top/anime` |
| Proxima temporada | `GET /seasons/upcoming` |
| Horario de emision | `GET /schedules` |
| Busqueda | `GET /anime?q={query}` |
| Filtro por tipo | `GET /top/anime?type={type}` |
| Detalle | `GET /anime/{id}` |
| Personajes | `GET /anime/{id}/characters` |
| Imagenes | `GET /anime/{id}/pictures` |
| Videos | `GET /anime/{id}/videos` |
| Temas musicales | `GET /anime/{id}/themes` |
| Episodios | `GET /anime/{id}/episodes?page={n}` |
| Detalle de episodio | `GET /anime/{id}/episodes/{ep}` |
| Recomendaciones | `GET /anime/{id}/recommendations` |
| Foro | `GET /anime/{id}/forum` |
| Detalle de personaje | `GET /characters/{id}` |
| Imagenes de personaje | `GET /characters/{id}/pictures` |
| Detalle de productor | `GET /producers/{id}` |
| Generos | `GET /genres/anime` |
| Anime aleatorio | `GET /random/anime` |
| Personaje aleatorio | `GET /random/characters` |

### Rate Limiting

Jikan API v4 tiene límites de tasa. El proyecto implementa:
- Caché en memoria para reducir llamadas duplicadas
- Manejo de errores HTTP 429 (Too Many Requests)
- Retry logic con backoff exponencial

---

## Patrones y convenciones

### State Management

- **StateFlow** como única fuente de verdad para el estado de UI
- Estados definidos con sealed classes o data classes
- Patrón UiState para cada pantalla (Loading, Success, Error, Empty)

### Inyección de Dependencias

- **Hilt** para DI en toda la aplicación
- Módulos separados por capa (NetworkModule, DatabaseModule, RepositoryModule)
- ViewModels inyectados con `@HiltViewModel`
- Scopes: `@Singleton`, `@ViewModelScoped`, `@ActivityRetainedScoped`

### Navegación

- Navigation Compose con type-safe arguments
- Rutas definidas en `AppDestinations`
- Animaciones de transición personalizadas
- Shared element transitions para imágenes de anime

### Testing

El proyecto está configurado para testing con:
- JUnit 4 para tests unitarios
- Mockk para mocking
- Coroutines Test para testing de código asíncrono
- Room In-Memory Database para testing de DAOs

### Recursos y temas

- Material 3 con paleta dinámica
- Soporte para modo claro, oscuro, sistema y tema japonés
- Tipografía personalizada: Poppins (Regular, Medium, Bold)
- Dimensiones y espaciados definidos en `Dimens.kt`

---

## Contribuciones

Para contribuir al proyecto:

1. Fork el repositorio
2. Crea una rama feature (`git checkout -b feature/NuevaCaracteristica`)
3. Sigue los patrones de arquitectura establecidos
4. Asegúrate de que tu código compile sin errores
5. Commit con mensajes descriptivos
6. Push a tu fork y crea un Pull Request

### Convenciones de código

- Kotlin coding conventions oficiales
- Nombres descriptivos en español para variables y funciones de UI
- Comentarios en español para lógica compleja
- KDoc para funciones públicas de repositorios y use cases

---

## Licencia

Este proyecto es de código abierto. Consulta el archivo LICENSE para más detalles.

---

**Desarrollado por Pedro Bustamante**

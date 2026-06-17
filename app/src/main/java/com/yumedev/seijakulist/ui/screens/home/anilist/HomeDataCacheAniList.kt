package com.yumedev.seijakulist.ui.screens.home.anilist

import com.yumedev.seijakulist.domain.models.Anime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Caches de scope de aplicación para las secciones principales del HomeScreen usando AniList.
 * Persisten mientras el proceso esté vivo, evitando peticiones redundantes
 * cuando los ViewModels se recrean por navegación.
 */

@Singleton
class AnimeSeasonNowCacheAniList @Inject constructor() {
    var animeList: List<Anime>? = null
}

@Singleton
class AnimeSeasonUpcomingCacheAniList @Inject constructor() {
    var animeList: List<Anime>? = null
}

@Singleton
class TopAnimeCacheAniList @Inject constructor() {
    var animeList: List<Anime>? = null
}

@Singleton
class AiringAnimeCacheAniList @Inject constructor() {
    var animeList: List<Anime>? = null
}

@Singleton
class TopMangaCacheAniList @Inject constructor() {
    var animeList: List<Anime>? = null
}

@Singleton
class PublishingMangaCacheAniList @Inject constructor() {
    var animeList: List<Anime>? = null
}

@Singleton
class TrendingMangaCacheAniList @Inject constructor() {
    var animeList: List<Anime>? = null
}

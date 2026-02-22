package com.yumedev.seijakulist.ui.screens.home

import com.yumedev.seijakulist.domain.models.Anime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Caches de scope de aplicación para las 3 secciones principales del HomeScreen.
 * Persisten mientras el proceso esté vivo, evitando peticiones redundantes
 * cuando los ViewModels se recrean por navegación.
 */

@Singleton
class AnimeSeasonNowCache @Inject constructor() {
    var animeList: List<Anime>? = null
}

@Singleton
class AnimeSeasonUpcomingCache @Inject constructor() {
    var animeList: List<Anime>? = null
}

@Singleton
class TopAnimeCache @Inject constructor() {
    var animeList: List<Anime>? = null
}
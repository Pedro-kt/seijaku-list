package com.yumedev.seijakulist.ui.screens.detail

/**
 * Cache temporal para mantener la imagen del anime visible durante la transición
 */
object AnimeTransitionCache {
    private var cachedImageUrl: String? = null
    private var cachedAnimeId: Int? = null

    fun setAnimeImage(animeId: Int, imageUrl: String) {
        cachedAnimeId = animeId
        cachedImageUrl = imageUrl
    }

    fun getAnimeImage(animeId: Int): String? {
        return if (cachedAnimeId == animeId) cachedImageUrl else null
    }

    fun clear() {
        cachedImageUrl = null
        cachedAnimeId = null
    }
}

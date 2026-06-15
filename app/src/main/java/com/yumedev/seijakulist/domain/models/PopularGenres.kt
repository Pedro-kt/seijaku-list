package com.yumedev.seijakulist.domain.models

/**
 * Local repository of all anime genres from AniList
 * Eliminates API calls for both discovery cards and genre selection
 * Data is based on AniList's GenreCollection
 * Note: AniList uses genre names (not IDs) for filtering
 */
object PopularGenres {

    /**
     * Complete list of all AniList anime genres
     * Sorted alphabetically for better UX in selection dialog
     * AniList uses simple genre names without IDs, so malId is synthetic for UI compatibility
     */
    private val allGenres = listOf(
        // AniList Core Genres (Alphabetically)
        Genre(malId = 1, name = "Action", url = "", count = 0),
        Genre(malId = 2, name = "Adventure", url = "", count = 0),
        Genre(malId = 3, name = "Comedy", url = "", count = 0),
        Genre(malId = 4, name = "Drama", url = "", count = 0),
        Genre(malId = 5, name = "Ecchi", url = "", count = 0),
        Genre(malId = 6, name = "Fantasy", url = "", count = 0),
        Genre(malId = 7, name = "Horror", url = "", count = 0),
        Genre(malId = 8, name = "Mahou Shoujo", url = "", count = 0),
        Genre(malId = 9, name = "Mecha", url = "", count = 0),
        Genre(malId = 10, name = "Music", url = "", count = 0),
        Genre(malId = 11, name = "Mystery", url = "", count = 0),
        Genre(malId = 12, name = "Psychological", url = "", count = 0),
        Genre(malId = 13, name = "Romance", url = "", count = 0),
        Genre(malId = 14, name = "Sci-Fi", url = "", count = 0),
        Genre(malId = 15, name = "Slice of Life", url = "", count = 0),
        Genre(malId = 16, name = "Sports", url = "", count = 0),
        Genre(malId = 17, name = "Supernatural", url = "", count = 0),
        Genre(malId = 18, name = "Thriller", url = "", count = 0)
    )

    /**
     * List of most popular genres for anime discovery cards
     * Displayed prominently in SearchScreen
     */
    val popularGenres = listOf(
        allGenres.first { it.malId == 1 },   // Action
        allGenres.first { it.malId == 2 },   // Adventure
        allGenres.first { it.malId == 3 },   // Comedy
        allGenres.first { it.malId == 4 },   // Drama
        allGenres.first { it.malId == 6 },   // Fantasy
        allGenres.first { it.malId == 7 },   // Horror
        allGenres.first { it.malId == 11 },  // Mystery
        allGenres.first { it.malId == 12 },  // Psychological
        allGenres.first { it.malId == 13 },  // Romance
        allGenres.first { it.malId == 14 },  // Sci-Fi
        allGenres.first { it.malId == 15 },  // Slice of Life
        allGenres.first { it.malId == 16 },  // Sports
        allGenres.first { it.malId == 17 },  // Supernatural
        allGenres.first { it.malId == 18 }   // Thriller
    )

    /**
     * List of most popular genres for manga discovery cards
     * Manga tends to have different popular genres than anime
     */
    val popularMangaGenres = listOf(
        allGenres.first { it.malId == 1 },   // Action
        allGenres.first { it.malId == 2 },   // Adventure
        allGenres.first { it.malId == 3 },   // Comedy
        allGenres.first { it.malId == 4 },   // Drama
        allGenres.first { it.malId == 6 },   // Fantasy
        allGenres.first { it.malId == 7 },   // Horror
        allGenres.first { it.malId == 11 },  // Mystery
        allGenres.first { it.malId == 12 },  // Psychological
        allGenres.first { it.malId == 13 },  // Romance
        allGenres.first { it.malId == 14 },  // Sci-Fi
        allGenres.first { it.malId == 15 },  // Slice of Life
        allGenres.first { it.malId == 17 },  // Supernatural
        allGenres.first { it.malId == 18 },  // Thriller
        allGenres.first { it.malId == 5 }    // Ecchi (más popular en manga)
    )

    /**
     * All genres sorted alphabetically
     * Used in GenresBottomSheet for full catalog selection
     */
    val genres: List<Genre> = allGenres.sortedBy { it.name }

    /**
     * Get a genre by its MAL ID
     */
    fun getGenreById(malId: Int): Genre? {
        return allGenres.find { it.malId == malId }
    }

    /**
     * Get a genre by name (case insensitive)
     */
    fun getGenreByName(name: String): Genre? {
        return allGenres.find { it.name.equals(name, ignoreCase = true) }
    }
}

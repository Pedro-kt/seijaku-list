package com.yumedev.seijakulist.domain.models

/**
 * Local repository of all anime genres from MyAnimeList
 * Eliminates API calls for both discovery cards and genre selection
 * Data is static and based on MyAnimeList genre IDs
 */
object PopularGenres {

    /**
     * Complete list of all MAL anime genres
     * Sorted alphabetically for better UX in selection dialog
     */
    private val allGenres = listOf(
        // Main Genres (Alphabetically)
        Genre(malId = 1, name = "Action", url = "https://myanimelist.net/anime/genre/1/Action", count = 5284),
        Genre(malId = 5, name = "Avant Garde", url = "https://myanimelist.net/anime/genre/5/Avant_Garde", count = 892),
        Genre(malId = 2, name = "Adventure", url = "https://myanimelist.net/anime/genre/2/Adventure", count = 4456),
        Genre(malId = 46, name = "Award Winning", url = "https://myanimelist.net/anime/genre/46/Award_Winning", count = 287),
        Genre(malId = 28, name = "Boys Love", url = "https://myanimelist.net/anime/genre/28/Boys_Love", count = 234),
        Genre(malId = 4, name = "Comedy", url = "https://myanimelist.net/anime/genre/4/Comedy", count = 7835),
        Genre(malId = 8, name = "Drama", url = "https://myanimelist.net/anime/genre/8/Drama", count = 3012),
        Genre(malId = 9, name = "Ecchi", url = "https://myanimelist.net/anime/genre/9/Ecchi", count = 1245),
        Genre(malId = 10, name = "Fantasy", url = "https://myanimelist.net/anime/genre/10/Fantasy", count = 5892),
        Genre(malId = 26, name = "Girls Love", url = "https://myanimelist.net/anime/genre/26/Girls_Love", count = 198),
        Genre(malId = 47, name = "Gourmet", url = "https://myanimelist.net/anime/genre/47/Gourmet", count = 156),
        Genre(malId = 14, name = "Horror", url = "https://myanimelist.net/anime/genre/14/Horror", count = 634),
        Genre(malId = 7, name = "Mystery", url = "https://myanimelist.net/anime/genre/7/Mystery", count = 1654),
        Genre(malId = 22, name = "Romance", url = "https://myanimelist.net/anime/genre/22/Romance", count = 2341),
        Genre(malId = 24, name = "Sci-Fi", url = "https://myanimelist.net/anime/genre/24/Sci-Fi", count = 3421),
        Genre(malId = 36, name = "Slice of Life", url = "https://myanimelist.net/anime/genre/36/Slice_of_Life", count = 2167),
        Genre(malId = 30, name = "Sports", url = "https://myanimelist.net/anime/genre/30/Sports", count = 987),
        Genre(malId = 37, name = "Supernatural", url = "https://myanimelist.net/anime/genre/37/Supernatural", count = 1523),
        Genre(malId = 41, name = "Suspense", url = "https://myanimelist.net/anime/genre/41/Suspense", count = 1089),

        // Themes
        Genre(malId = 50, name = "Adult Cast", url = "https://myanimelist.net/anime/genre/50/Adult_Cast", count = 567),
        Genre(malId = 51, name = "Anthropomorphic", url = "https://myanimelist.net/anime/genre/51/Anthropomorphic", count = 234),
        Genre(malId = 52, name = "CGDCT", url = "https://myanimelist.net/anime/genre/52/CGDCT", count = 345),
        Genre(malId = 53, name = "Childcare", url = "https://myanimelist.net/anime/genre/53/Childcare", count = 89),
        Genre(malId = 54, name = "Combat Sports", url = "https://myanimelist.net/anime/genre/54/Combat_Sports", count = 145),
        Genre(malId = 81, name = "Crossdressing", url = "https://myanimelist.net/anime/genre/81/Crossdressing", count = 67),
        Genre(malId = 55, name = "Delinquents", url = "https://myanimelist.net/anime/genre/55/Delinquents", count = 123),
        Genre(malId = 39, name = "Detective", url = "https://myanimelist.net/anime/genre/39/Detective", count = 234),
        Genre(malId = 56, name = "Educational", url = "https://myanimelist.net/anime/genre/56/Educational", count = 178),
        Genre(malId = 57, name = "Gag Humor", url = "https://myanimelist.net/anime/genre/57/Gag_Humor", count = 456),
        Genre(malId = 58, name = "Gore", url = "https://myanimelist.net/anime/genre/58/Gore", count = 289),
        Genre(malId = 35, name = "Harem", url = "https://myanimelist.net/anime/genre/35/Harem", count = 678),
        Genre(malId = 13, name = "Historical", url = "https://myanimelist.net/anime/genre/13/Historical", count = 1234),
        Genre(malId = 60, name = "Idols (Female)", url = "https://myanimelist.net/anime/genre/60/Idols_Female", count = 234),
        Genre(malId = 61, name = "Idols (Male)", url = "https://myanimelist.net/anime/genre/61/Idols_Male", count = 156),
        Genre(malId = 62, name = "Isekai", url = "https://myanimelist.net/anime/genre/62/Isekai", count = 892),
        Genre(malId = 63, name = "Iyashikei", url = "https://myanimelist.net/anime/genre/63/Iyashikei", count = 134),
        Genre(malId = 64, name = "Love Polygon", url = "https://myanimelist.net/anime/genre/64/Love_Polygon", count = 267),
        Genre(malId = 65, name = "Magical Sex Shift", url = "https://myanimelist.net/anime/genre/65/Magical_Sex_Shift", count = 45),
        Genre(malId = 66, name = "Mahou Shoujo", url = "https://myanimelist.net/anime/genre/66/Mahou_Shoujo", count = 345),
        Genre(malId = 17, name = "Martial Arts", url = "https://myanimelist.net/anime/genre/17/Martial_Arts", count = 567),
        Genre(malId = 18, name = "Mecha", url = "https://myanimelist.net/anime/genre/18/Mecha", count = 1123),
        Genre(malId = 67, name = "Medical", url = "https://myanimelist.net/anime/genre/67/Medical", count = 89),
        Genre(malId = 38, name = "Military", url = "https://myanimelist.net/anime/genre/38/Military", count = 456),
        Genre(malId = 19, name = "Music", url = "https://myanimelist.net/anime/genre/19/Music", count = 678),
        Genre(malId = 6, name = "Mythology", url = "https://myanimelist.net/anime/genre/6/Mythology", count = 234),
        Genre(malId = 68, name = "Organized Crime", url = "https://myanimelist.net/anime/genre/68/Organized_Crime", count = 123),
        Genre(malId = 69, name = "Otaku Culture", url = "https://myanimelist.net/anime/genre/69/Otaku_Culture", count = 345),
        Genre(malId = 20, name = "Parody", url = "https://myanimelist.net/anime/genre/20/Parody", count = 567),
        Genre(malId = 70, name = "Performing Arts", url = "https://myanimelist.net/anime/genre/70/Performing_Arts", count = 178),
        Genre(malId = 71, name = "Pets", url = "https://myanimelist.net/anime/genre/71/Pets", count = 234),
        Genre(malId = 40, name = "Psychological", url = "https://myanimelist.net/anime/genre/40/Psychological", count = 789),
        Genre(malId = 3, name = "Racing", url = "https://myanimelist.net/anime/genre/3/Racing", count = 234),
        Genre(malId = 72, name = "Reincarnation", url = "https://myanimelist.net/anime/genre/72/Reincarnation", count = 456),
        Genre(malId = 73, name = "Reverse Harem", url = "https://myanimelist.net/anime/genre/73/Reverse_Harem", count = 189),
        Genre(malId = 74, name = "Romantic Subtext", url = "https://myanimelist.net/anime/genre/74/Romantic_Subtext", count = 345),
        Genre(malId = 21, name = "Samurai", url = "https://myanimelist.net/anime/genre/21/Samurai", count = 234),
        Genre(malId = 23, name = "School", url = "https://myanimelist.net/anime/genre/23/School", count = 3456),
        Genre(malId = 75, name = "Showbiz", url = "https://myanimelist.net/anime/genre/75/Showbiz", count = 123),
        Genre(malId = 29, name = "Space", url = "https://myanimelist.net/anime/genre/29/Space", count = 567),
        Genre(malId = 11, name = "Strategy Game", url = "https://myanimelist.net/anime/genre/11/Strategy_Game", count = 234),
        Genre(malId = 31, name = "Super Power", url = "https://myanimelist.net/anime/genre/31/Super_Power", count = 1234),
        Genre(malId = 76, name = "Survival", url = "https://myanimelist.net/anime/genre/76/Survival", count = 178),
        Genre(malId = 77, name = "Team Sports", url = "https://myanimelist.net/anime/genre/77/Team_Sports", count = 456),
        Genre(malId = 78, name = "Time Travel", url = "https://myanimelist.net/anime/genre/78/Time_Travel", count = 234),
        Genre(malId = 32, name = "Vampire", url = "https://myanimelist.net/anime/genre/32/Vampire", count = 189),
        Genre(malId = 79, name = "Video Game", url = "https://myanimelist.net/anime/genre/79/Video_Game", count = 345),
        Genre(malId = 80, name = "Visual Arts", url = "https://myanimelist.net/anime/genre/80/Visual_Arts", count = 123),
        Genre(malId = 48, name = "Workplace", url = "https://myanimelist.net/anime/genre/48/Workplace", count = 234),

        // Demographics
        Genre(malId = 42, name = "Josei", url = "https://myanimelist.net/anime/genre/42/Josei", count = 234),
        Genre(malId = 15, name = "Kids", url = "https://myanimelist.net/anime/genre/15/Kids", count = 1234),
        Genre(malId = 41, name = "Seinen", url = "https://myanimelist.net/anime/genre/41/Seinen", count = 2345),
        Genre(malId = 25, name = "Shoujo", url = "https://myanimelist.net/anime/genre/25/Shoujo", count = 1567),
        Genre(malId = 27, name = "Shounen", url = "https://myanimelist.net/anime/genre/27/Shounen", count = 3456)
    )

    /**
     * List of 14 most popular genres for discovery cards
     * Displayed prominently in SearchScreen
     */
    val popularGenres = listOf(
        allGenres.first { it.malId == 1 },   // Action
        allGenres.first { it.malId == 2 },   // Adventure
        allGenres.first { it.malId == 46 },  // Award Winning
        allGenres.first { it.malId == 4 },   // Comedy
        allGenres.first { it.malId == 8 },   // Drama
        allGenres.first { it.malId == 10 },  // Fantasy
        allGenres.first { it.malId == 7 },   // Mystery
        allGenres.first { it.malId == 14 },  // Horror
        allGenres.first { it.malId == 22 },  // Romance
        allGenres.first { it.malId == 24 },  // Sci-Fi
        allGenres.first { it.malId == 36 },  // Slice of Life
        allGenres.first { it.malId == 41 },  // Suspense
        allGenres.first { it.malId == 37 },  // Supernatural
        allGenres.first { it.malId == 62 }   // Isekai
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

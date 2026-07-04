package com.yumedev.seijakulist.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Data class that holds the visual theme for a genre
 * @param startColor Start color of the gradient
 * @param endColor End color of the gradient
 * @param icon Material Icon that represents the genre
 */
data class GenreTheme(
    val startColor: Color,
    val endColor: Color,
    val icon: ImageVector
)

/**
 * Maps genre names to their visual themes (gradients + icons)
 * Uses Material Icons for copyright-free representation
 * UPDATED: Now uses Seijaku color palette (apagados, serenos)
 */
object GenreThemeMapper {

    private val genreThemes = mapOf(
        // CREAM (lo emocional, importante, lo que guardas)
        "Romance" to GenreTheme(
            startColor = SeijakuColors.Dark.cream,
            endColor = SeijakuColors.Dark.creamProfundo,
            icon = Icons.Default.Favorite
        ),
        "Drama" to GenreTheme(
            startColor = SeijakuColors.Dark.cream,
            endColor = SeijakuColors.Dark.creamClaro,
            icon = Icons.Default.Theaters
        ),
        "Slice of Life" to GenreTheme(
            startColor = SeijakuColors.Dark.creamClaro,
            endColor = SeijakuColors.Dark.cream,
            icon = Icons.Default.WbSunny
        ),

        // SALVIA (calma, naturaleza, paz)
        "Gourmet" to GenreTheme(
            startColor = SeijakuColors.Dark.salvia,
            endColor = SeijakuColors.Dark.salviaProfunda,
            icon = Icons.Default.Restaurant
        ),
        "Music" to GenreTheme(
            startColor = SeijakuColors.Dark.salviaClara,
            endColor = SeijakuColors.Dark.salvia,
            icon = Icons.Default.MusicNote
        ),
        "Idols" to GenreTheme(
            startColor = SeijakuColors.Dark.salvia,
            endColor = SeijakuColors.Dark.salviaClara,
            icon = Icons.Default.MusicNote
        ),
        "School" to GenreTheme(
            startColor = SeijakuColors.Dark.salviaProfunda,
            endColor = SeijakuColors.Dark.salvia,
            icon = Icons.Default.School
        ),
        "Kids" to GenreTheme(
            startColor = SeijakuColors.Dark.salviaClara,
            endColor = SeijakuColors.Dark.salviaProfunda,
            icon = Icons.Default.ChildCare
        ),

        // ESTADO VIENDO (celeste sereno - acción en progreso)
        "Action" to GenreTheme(
            startColor = SeijakuColors.Dark.estadoViendo,
            endColor = Color(0xFF5A8BA7),
            icon = Icons.Default.FlashOn
        ),
        "Adventure" to GenreTheme(
            startColor = Color(0xFF5A8BA7),
            endColor = SeijakuColors.Dark.estadoViendo,
            icon = Icons.Default.Explore
        ),
        "Sports" to GenreTheme(
            startColor = SeijakuColors.Dark.estadoViendo,
            endColor = Color(0xFF7FBDD4),
            icon = Icons.Default.SportsSoccer
        ),
        "Mecha" to GenreTheme(
            startColor = Color(0xFF5A8BA7),
            endColor = Color(0xFF7FBDD4),
            icon = Icons.Default.PrecisionManufacturing
        ),

        // ESTADO PLANEADO (lavanda - fantasía, magia, misterio)
        "Fantasy" to GenreTheme(
            startColor = SeijakuColors.Dark.estadoPlaneado,
            endColor = Color(0xFF8B76B4),
            icon = Icons.Default.AutoAwesome
        ),
        "Magic" to GenreTheme(
            startColor = Color(0xFF8B76B4),
            endColor = SeijakuColors.Dark.estadoPlaneado,
            icon = Icons.Default.Stars
        ),
        "Supernatural" to GenreTheme(
            startColor = SeijakuColors.Dark.estadoPlaneado,
            endColor = Color(0xFFAB9CD4),
            icon = Icons.Default.Nightlight
        ),
        "Isekai" to GenreTheme(
            startColor = Color(0xFF8B76B4),
            endColor = Color(0xFFAB9CD4),
            icon = Icons.Default.TravelExplore
        ),
        "Space" to GenreTheme(
            startColor = SeijakuColors.Dark.estadoPlaneado,
            endColor = Color(0xFF7A6AA4),
            icon = Icons.Default.Rocket
        ),
        "Sci-Fi" to GenreTheme(
            startColor = Color(0xFF7A6AA4),
            endColor = SeijakuColors.Dark.estadoViendo,
            icon = Icons.Default.Rocket
        ),

        // ESTADO COMPLETADO (verde natural - crecimiento, comedia, logro)
        "Comedy" to GenreTheme(
            startColor = SeijakuColors.Dark.estadoCompletado,
            endColor = Color(0xFF6FA55E),
            icon = Icons.Default.SentimentVerySatisfied
        ),
        "Award Winning" to GenreTheme(
            startColor = Color(0xFF8FC57E),
            endColor = SeijakuColors.Dark.estadoCompletado,
            icon = Icons.Default.EmojiEvents
        ),
        "Shounen" to GenreTheme(
            startColor = SeijakuColors.Dark.estadoCompletado,
            endColor = Color(0xFF8FC57E),
            icon = Icons.Default.Boy
        ),

        // ESTADO PENDIENTE (ámbar - suspenso, misterio)
        "Mystery" to GenreTheme(
            startColor = SeijakuColors.Dark.estadoPendiente,
            endColor = Color(0xFFC69940),
            icon = Icons.Default.Search
        ),
        "Psychological" to GenreTheme(
            startColor = Color(0xFFC69940),
            endColor = SeijakuColors.Dark.estadoPendiente,
            icon = Icons.Default.Psychology
        ),
        "Thriller" to GenreTheme(
            startColor = SeijakuColors.Dark.estadoPendiente,
            endColor = Color(0xFFE0BD5E),
            icon = Icons.Default.Warning
        ),
        "Suspense" to GenreTheme(
            startColor = Color(0xFFE0BD5E),
            endColor = SeijakuColors.Dark.estadoPendiente,
            icon = Icons.Default.RemoveRedEye
        ),

        // ESTADO ABANDONADO (terracota - intenso, oscuro)
        "Horror" to GenreTheme(
            startColor = SeijakuColors.Dark.estadoAbandonado,
            endColor = Color(0xFFBC6048),
            icon = Icons.Default.Bolt
        ),
        "Demons" to GenreTheme(
            startColor = Color(0xFFBC6048),
            endColor = SeijakuColors.Dark.estadoAbandonado,
            icon = Icons.Default.Whatshot
        ),
        "Vampire" to GenreTheme(
            startColor = SeijakuColors.Dark.estadoAbandonado,
            endColor = Color(0xFFE28070),
            icon = Icons.Default.Bloodtype
        ),
        "Samurai" to GenreTheme(
            startColor = Color(0xFFBC6048),
            endColor = Color(0xFFE28070),
            icon = Icons.Default.Sell
        ),
        "Martial Arts" to GenreTheme(
            startColor = SeijakuColors.Dark.estadoAbandonado,
            endColor = Color(0xFFBC6048),
            icon = Icons.Default.FitnessCenter
        ),

        // CREAM CLARO (demographics, variantes)
        "Shoujo" to GenreTheme(
            startColor = SeijakuColors.Dark.creamClaro,
            endColor = SeijakuColors.Dark.cream,
            icon = Icons.Default.Girl
        ),
        "Seinen" to GenreTheme(
            startColor = SeijakuColors.Dark.cream,
            endColor = SeijakuColors.Dark.creamProfundo,
            icon = Icons.Default.Man
        ),
        "Josei" to GenreTheme(
            startColor = SeijakuColors.Dark.creamProfundo,
            endColor = SeijakuColors.Dark.creamClaro,
            icon = Icons.Default.Woman
        ),

        // SALVIA PROFUNDA (otras categorías)
        "Game" to GenreTheme(
            startColor = SeijakuColors.Dark.salviaProfunda,
            endColor = SeijakuColors.Dark.salvia,
            icon = Icons.Default.SportsEsports
        ),
        "Parody" to GenreTheme(
            startColor = SeijakuColors.Dark.salviaClara,
            endColor = SeijakuColors.Dark.salviaProfunda,
            icon = Icons.Default.TagFaces
        ),
        "Harem" to GenreTheme(
            startColor = SeijakuColors.Dark.creamProfundo,
            endColor = SeijakuColors.Dark.cream,
            icon = Icons.Default.Favorite
        ),
        "Ecchi" to GenreTheme(
            startColor = Color(0xFFBC6048),
            endColor = Color(0xFFE28070),
            icon = Icons.Default.FavoriteBorder
        ),
        "Super Power" to GenreTheme(
            startColor = Color(0xFFC69940),
            endColor = SeijakuColors.Dark.estadoPendiente,
            icon = Icons.Default.FlashOn
        ),
        "Historical" to GenreTheme(
            startColor = SeijakuColors.Dark.creamProfundo,
            endColor = Color(0xFFA88560),
            icon = Icons.Default.HistoryEdu
        ),
        "Military" to GenreTheme(
            startColor = Color(0xFF5A8BA7),
            endColor = Color(0xFF7FBDD4),
            icon = Icons.Default.Shield
        )
    )

    /**
     * Default theme for genres without a specific mapping
     */
    private val defaultTheme = GenreTheme(
        startColor = SeijakuColors.Dark.creamProfundo,
        endColor = SeijakuColors.Dark.cream,
        icon = Icons.Default.Category
    )

    /**
     * Gets the theme for a genre by name
     * @param genreName The name of the genre
     * @return GenreTheme with gradient colors and icon
     */
    fun getThemeForGenre(genreName: String): GenreTheme {
        return genreThemes[genreName] ?: defaultTheme
    }

    /**
     * Checks if a genre has a specific theme mapping
     */
    fun hasTheme(genreName: String): Boolean {
        return genreThemes.containsKey(genreName)
    }
}

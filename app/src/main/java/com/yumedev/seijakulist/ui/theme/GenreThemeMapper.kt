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
 */
object GenreThemeMapper {

    private val genreThemes = mapOf(
        // Action & Adventure
        "Action" to GenreTheme(
            startColor = Color(0xFFFF6B6B),
            endColor = Color(0xFFFF4757),
            icon = Icons.Default.FlashOn
        ),
        "Adventure" to GenreTheme(
            startColor = Color(0xFF0CB2AF),
            endColor = Color(0xFF2EBFBC),
            icon = Icons.Default.Explore
        ),

        // Comedy & Slice of Life
        "Comedy" to GenreTheme(
            startColor = Color(0xFFFFBE0B),
            endColor = Color(0xFFFF9A00),
            icon = Icons.Default.SentimentVerySatisfied
        ),
        "Slice of Life" to GenreTheme(
            startColor = Color(0xFF06C88A),
            endColor = Color(0xFF2DC653),
            icon = Icons.Default.WbSunny
        ),

        // Romance & Drama
        "Romance" to GenreTheme(
            startColor = Color(0xFFFF758C),
            endColor = Color(0xFFFF4B6E),
            icon = Icons.Default.Favorite
        ),
        "Drama" to GenreTheme(
            startColor = Color(0xFF7B2FBE),
            endColor = Color(0xFFA855F7),
            icon = Icons.Default.Theaters
        ),

        // Fantasy & Magic
        "Fantasy" to GenreTheme(
            startColor = Color(0xFF6C63FF),
            endColor = Color(0xFF9C88FF),
            icon = Icons.Default.AutoAwesome
        ),
        "Magic" to GenreTheme(
            startColor = Color(0xFF7C3AED),
            endColor = Color(0xFFC084FC),
            icon = Icons.Default.Stars
        ),

        // Sci-Fi & Mecha
        "Sci-Fi" to GenreTheme(
            startColor = Color(0xFF0891B2),
            endColor = Color(0xFF22D3EE),
            icon = Icons.Default.Rocket
        ),
        "Mecha" to GenreTheme(
            startColor = Color(0xFF475569),
            endColor = Color(0xFF64748B),
            icon = Icons.Default.PrecisionManufacturing
        ),

        // Horror & Mystery
        "Horror" to GenreTheme(
            startColor = Color(0xFF4A0E0E),
            endColor = Color(0xFF7F1D1D),
            icon = Icons.Default.Bolt
        ),
        "Mystery" to GenreTheme(
            startColor = Color(0xFF1E293B),
            endColor = Color(0xFF334155),
            icon = Icons.Default.Search
        ),
        "Thriller" to GenreTheme(
            startColor = Color(0xFFDC2626),
            endColor = Color(0xFFF87171),
            icon = Icons.Default.Warning
        ),
        "Suspense" to GenreTheme(
            startColor = Color(0xFF831843),
            endColor = Color(0xFF9F1239),
            icon = Icons.Default.RemoveRedEye
        ),

        // Supernatural & Demons
        "Supernatural" to GenreTheme(
            startColor = Color(0xFF581C87),
            endColor = Color(0xFF7E22CE),
            icon = Icons.Default.Nightlight
        ),
        "Demons" to GenreTheme(
            startColor = Color(0xFF7F1D1D),
            endColor = Color(0xFFA16207),
            icon = Icons.Default.Whatshot
        ),
        "Vampire" to GenreTheme(
            startColor = Color(0xFF881337),
            endColor = Color(0xFFBE123C),
            icon = Icons.Default.Bloodtype
        ),

        // Sports & Games
        "Sports" to GenreTheme(
            startColor = Color(0xFF16A34A),
            endColor = Color(0xFF4ADE80),
            icon = Icons.Default.SportsSoccer
        ),
        "Game" to GenreTheme(
            startColor = Color(0xFF2563EB),
            endColor = Color(0xFF60A5FA),
            icon = Icons.Default.SportsEsports
        ),

        // School & Kids
        "School" to GenreTheme(
            startColor = Color(0xFF0D9488),
            endColor = Color(0xFF2DD4BF),
            icon = Icons.Default.School
        ),
        "Kids" to GenreTheme(
            startColor = Color(0xFFF59E0B),
            endColor = Color(0xFFFBBF24),
            icon = Icons.Default.ChildCare
        ),

        // Music & Performance
        "Music" to GenreTheme(
            startColor = Color(0xFFEC4899),
            endColor = Color(0xFFF472B6),
            icon = Icons.Default.MusicNote
        ),
        "Idols" to GenreTheme(
            startColor = Color(0xFFDB2777),
            endColor = Color(0xFFF472B6),
            icon = Icons.Default.MusicNote
        ),

        // Psychological
        "Psychological" to GenreTheme(
            startColor = Color(0xFF374151),
            endColor = Color(0xFF6B7280),
            icon = Icons.Default.Psychology
        ),

        // Historical & Military
        "Historical" to GenreTheme(
            startColor = Color(0xFF92400E),
            endColor = Color(0xFFB45309),
            icon = Icons.Default.HistoryEdu
        ),
        "Military" to GenreTheme(
            startColor = Color(0xFF4B5563),
            endColor = Color(0xFF6B7280),
            icon = Icons.Default.Shield
        ),
        "Samurai" to GenreTheme(
            startColor = Color(0xFF991B1B),
            endColor = Color(0xFFB91C1C),
            icon = Icons.Default.Sell
        ),

        // Martial Arts & Super Power
        "Martial Arts" to GenreTheme(
            startColor = Color(0xFFB91C1C),
            endColor = Color(0xFFDC2626),
            icon = Icons.Default.FitnessCenter
        ),
        "Super Power" to GenreTheme(
            startColor = Color(0xFFEA580C),
            endColor = Color(0xFFFB923C),
            icon = Icons.Default.FlashOn
        ),

        // Demographics
        "Shounen" to GenreTheme(
            startColor = Color(0xFFEA580C),
            endColor = Color(0xFFFB923C),
            icon = Icons.Default.Boy
        ),
        "Shoujo" to GenreTheme(
            startColor = Color(0xFFEC4899),
            endColor = Color(0xFFF9A8D4),
            icon = Icons.Default.Girl
        ),
        "Seinen" to GenreTheme(
            startColor = Color(0xFF1E40AF),
            endColor = Color(0xFF3B82F6),
            icon = Icons.Default.Man
        ),
        "Josei" to GenreTheme(
            startColor = Color(0xFF9333EA),
            endColor = Color(0xFFC084FC),
            icon = Icons.Default.Woman
        ),

        // Other
        "Parody" to GenreTheme(
            startColor = Color(0xFFF59E0B),
            endColor = Color(0xFFFBBF24),
            icon = Icons.Default.TagFaces
        ),
        "Harem" to GenreTheme(
            startColor = Color(0xFFE91E63),
            endColor = Color(0xFFF06292),
            icon = Icons.Default.Favorite
        ),
        "Ecchi" to GenreTheme(
            startColor = Color(0xFFFF1744),
            endColor = Color(0xFFFF5252),
            icon = Icons.Default.FavoriteBorder
        ),
        "Space" to GenreTheme(
            startColor = Color(0xFF1E3A8A),
            endColor = Color(0xFF3730A3),
            icon = Icons.Default.Rocket
        ),
        "Isekai" to GenreTheme(
            startColor = Color(0xFF7C3AED),
            endColor = Color(0xFF8B5CF6),
            icon = Icons.Default.TravelExplore
        ),
        "Award Winning" to GenreTheme(
            startColor = Color(0xFFD97706),
            endColor = Color(0xFFF59E0B),
            icon = Icons.Default.EmojiEvents
        ),
        "Gourmet" to GenreTheme(
            startColor = Color(0xFFDC2626),
            endColor = Color(0xFFF87171),
            icon = Icons.Default.Restaurant
        )
    )

    /**
     * Default theme for genres without a specific mapping
     */
    private val defaultTheme = GenreTheme(
        startColor = Color(0xFF6366F1),
        endColor = Color(0xFF8B5CF6),
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

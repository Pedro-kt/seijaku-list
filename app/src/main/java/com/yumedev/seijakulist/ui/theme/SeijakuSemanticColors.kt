package com.yumedev.seijakulist.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

/**
 * COLORES SEMÁNTICOS DE SEIJAKU
 * ------------------------------
 * Este archivo mapea usos específicos de color en la app a la paleta Seijaku.
 *
 * IMPORTANTE: Nunca usar colores hardcodeados. Siempre pasar por este sistema.
 *
 * Principios:
 * - CREAM = momentos emocionales, logros, lo importante
 * - SALVIA = información calma, detalles secundarios
 * - ESTADOS = solo para estados funcionales (viendo, completado, etc.)
 * - Si no es ninguno de los anteriores = usa MaterialTheme.colorScheme
 */
object SeijakuSemanticColors {

    /**
     * HERO CAROUSEL BADGES
     * Badges que aparecen en el carrusel hero de la home.
     * Siguen la filosofía: apagados, nunca saturados.
     */
    data class HeroBadgeColors(
        val containerColor: Color,
        val contentColor: Color,
        val borderColor: Color
    )

    fun heroBadgeColors(type: String, isDark: Boolean = true): HeroBadgeColors {
        return when (type) {
            "Para vos" -> {
                // Lo emocional: usa CREAM
                val base = if (isDark) SeijakuColors.Dark.cream else SeijakuColors.Light.cream
                HeroBadgeColors(
                    containerColor = base.copy(alpha = 0.15f),
                    contentColor = base,
                    borderColor = base.copy(alpha = 0.3f)
                )
            }
            "Promo", "Miralo ahora" -> {
                // Acción urgente pero calmada: ámbar apagado
                val base = if (isDark) SeijakuColors.Dark.estadoPendiente else SeijakuColors.Light.estadoPendiente
                HeroBadgeColors(
                    containerColor = base.copy(alpha = 0.15f),
                    contentColor = base,
                    borderColor = base.copy(alpha = 0.3f)
                )
            }
            "Sigue mirando", "Empieza a ver" -> {
                // En progreso: celeste/azul sereno
                val base = if (isDark) SeijakuColors.Dark.estadoViendo else SeijakuColors.Light.estadoViendo
                HeroBadgeColors(
                    containerColor = base.copy(alpha = 0.15f),
                    contentColor = base,
                    borderColor = base.copy(alpha = 0.3f)
                )
            }
            "Nuevos episodios" -> {
                // Novedad calma: salvia
                val base = if (isDark) SeijakuColors.Dark.salvia else SeijakuColors.Light.salvia
                HeroBadgeColors(
                    containerColor = base.copy(alpha = 0.15f),
                    contentColor = base,
                    borderColor = base.copy(alpha = 0.3f)
                )
            }
            "Próximamente" -> {
                // Futuro: lavanda apagada (planeado)
                val base = if (isDark) SeijakuColors.Dark.estadoPlaneado else SeijakuColors.Light.estadoPlaneado
                HeroBadgeColors(
                    containerColor = base.copy(alpha = 0.15f),
                    contentColor = base,
                    borderColor = base.copy(alpha = 0.3f)
                )
            }
            "Clásico" -> {
                // Destacado: cream claro
                val base = if (isDark) SeijakuColors.Dark.creamClaro else SeijakuColors.Light.creamClaro
                HeroBadgeColors(
                    containerColor = base.copy(alpha = 0.15f),
                    contentColor = base,
                    borderColor = base.copy(alpha = 0.3f)
                )
            }
            else -> {
                // Default: cream profundo
                val base = if (isDark) SeijakuColors.Dark.creamProfundo else SeijakuColors.Light.creamProfundo
                HeroBadgeColors(
                    containerColor = base.copy(alpha = 0.15f),
                    contentColor = base,
                    borderColor = base.copy(alpha = 0.3f)
                )
            }
        }
    }

    /**
     * RATING BADGE (Estrellas)
     * El rating es importante = CREAM (el acento que habla)
     */
    data class RatingBadgeColors(
        val containerColor: Color,
        val contentColor: Color,
        val borderColor: Color
    )

    fun ratingBadgeColors(isDark: Boolean = true): RatingBadgeColors {
        val base = if (isDark) SeijakuColors.Dark.cream else SeijakuColors.Light.cream
        return RatingBadgeColors(
            containerColor = base.copy(alpha = 0.18f),
            contentColor = base,
            borderColor = base.copy(alpha = 0.35f)
        )
    }

    /**
     * STATUS BADGES (Airing, Finished, etc.)
     * Estados del anime: usar estados semánticos de Seijaku
     */
    fun statusColor(status: String, isDark: Boolean = true): Color {
        return when {
            status.contains("Releasing", ignoreCase = true) ||
            status.contains("Airing", ignoreCase = true) -> {
                if (isDark) SeijakuColors.Dark.estadoViendo else SeijakuColors.Light.estadoViendo
            }
            status.contains("Finished", ignoreCase = true) -> {
                if (isDark) SeijakuColors.Dark.estadoCompletado else SeijakuColors.Light.estadoCompletado
            }
            else -> {
                if (isDark) SeijakuColors.Dark.estadoPlaneado else SeijakuColors.Light.estadoPlaneado
            }
        }
    }

    /**
     * USER INSIGHTS
     * Información del usuario: rotar entre cream, salvia y sus variantes.
     * Máximo 3 colores para mantener quietud.
     */
    fun insightColor(index: Int, isDark: Boolean = true): Color {
        val colors = if (isDark) {
            listOf(
                SeijakuColors.Dark.cream,           // Lo importante (total animes, logros)
                SeijakuColors.Dark.salvia,          // Info calma (horas, episodios)
                SeijakuColors.Dark.creamClaro,      // Variante cream para diversidad
                SeijakuColors.Dark.salviaProfunda,  // Variante salvia
                SeijakuColors.Dark.estadoCompletado // Verde para completados
            )
        } else {
            listOf(
                SeijakuColors.Light.cream,
                SeijakuColors.Light.salvia,
                SeijakuColors.Light.creamClaro,
                SeijakuColors.Light.salviaProfunda,
                SeijakuColors.Light.estadoCompletado
            )
        }
        return colors[index % colors.size]
    }

    /**
     * GENRE TAG
     * Tags de género: salvia (acompaña, no compite)
     */
    fun genreTagColor(isDark: Boolean = true): Color {
        return if (isDark) SeijakuColors.Dark.salvia else SeijakuColors.Light.salvia
    }

    /**
     * YEAR BADGE
     * Año de emisión: texto tenue, info secundaria
     */
    fun yearBadgeColor(isDark: Boolean = true): Color {
        return if (isDark) SeijakuColors.Dark.textoSecundario else SeijakuColors.Light.textoSecundario
    }
}

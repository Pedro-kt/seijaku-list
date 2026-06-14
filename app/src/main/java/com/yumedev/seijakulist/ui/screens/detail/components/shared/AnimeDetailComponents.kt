package com.yumedev.seijakulist.ui.screens.detail.components.shared

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsMedium
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import com.yumedev.seijakulist.ui.theme.adp
import com.yumedev.seijakulist.ui.theme.asp
import com.yumedev.seijakulist.util.translateStatus

/**
 * Chip pequeño para mostrar información básica como score, tipo, año
 */
@Composable
fun DetailChipSmall(icon: ImageVector, text: String) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceContainerHigh)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, modifier = Modifier.size(11.dp), tint = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text, fontSize = 11.asp(), fontFamily = PoppinsMedium, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

/**
 * Card para mostrar información tipo píldora con etiqueta y valor
 */
@Composable
fun InfoPillCard(
    label: String,
    value: String,
    infoType: String,
    modifier: Modifier = Modifier
) {
    val accentColor = getInfoColor(infoType, value)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.asp(),
            fontFamily = PoppinsRegular,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHighest,
            border = BorderStroke(1.dp, accentColor.copy(alpha = 0.3f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = value,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = PoppinsMedium,
                    fontSize = 13.asp(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Fila compacta para mostrar información clave-valor
 */
@Composable
fun CompactInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.asp(),
            fontFamily = PoppinsRegular,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.5f)
        )
        Text(
            text = value,
            fontSize = 13.asp(),
            fontFamily = PoppinsBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(0.5f),
            textAlign = TextAlign.End,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * Chip de género con color personalizado
 */
@Composable
fun GenreChip(
    genreName: String,
    onClick: () -> Unit = {}
) {
    val genreColor = getGenreColor(genreName)
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = genreColor.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, genreColor.copy(alpha = 0.4f)),
        modifier = Modifier.height(32.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = genreName,
                color = genreColor,
                fontFamily = PoppinsMedium,
                fontSize = 13.asp()
            )
        }
    }
}

/**
 * Badge de estado de emisión (Currently Airing, Finished Airing, etc.)
 */
@Composable
fun StatusChip(status: String) {
    val (statusBg, statusFg, dotColor) = when (status) {
        "Currently Airing" -> Triple(
            MaterialTheme.colorScheme.surfaceContainerHigh,
            Color(0xFF66BB6A),
            Color(0xFF66BB6A)
        )
        "Finished Airing" -> Triple(
            MaterialTheme.colorScheme.surfaceContainerHigh,
            Color(0xFF42A5F5),
            Color(0xFF42A5F5)
        )
        "Not yet aired" -> Triple(
            MaterialTheme.colorScheme.surfaceContainerHigh,
            Color(0xFFFFA726),
            Color(0xFFFFA726)
        )
        else -> Triple(
            MaterialTheme.colorScheme.surfaceContainerHigh,
            Color.White,
            Color.White
        )
    }
    val statusLabel = translateStatus(status)

    Surface(
        shape = RoundedCornerShape(50),
        color = statusBg,
        border = BorderStroke(1.dp, statusFg.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(dotColor)
            )
            Text(
                text = statusLabel,
                color = statusFg,
                fontSize = 11.asp(),
                fontFamily = PoppinsBold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

/**
 * Badge para información de broadcast (día y hora de emisión)
 */
@Composable
fun BroadcastBadge(
    day: String?,
    time: String?,
    fullString: String?
) {
    if (fullString != null || (day != null && time != null)) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = fullString ?: "$day a las $time",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.asp(),
                    fontFamily = PoppinsMedium
                )
            }
        }
    }
}

/**
 * Header de sección con título y subtítulo
 */
@Composable
fun SectionHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    trailingContent: @Composable () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(24.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    style = TextStyle(
                        fontFamily = PoppinsBold,
                        fontSize = 22.asp(),
                        letterSpacing = (-0.5).sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = subtitle,
                    style = TextStyle(
                        fontFamily = PoppinsRegular,
                        fontSize = 12.asp(),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                )
            }
        }
        trailingContent()
    }
}

/**
 * Mensaje de estado vacío con icono
 */
@Composable
fun StateMessage(
    icon: ImageVector,
    title: String,
    description: String,
    buttonText: String? = null,
    onButtonClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(64.adp()),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontFamily = PoppinsBold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = PoppinsRegular,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        if (buttonText != null && onButtonClick != null) {
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedButton(
                onClick = onButtonClick,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(buttonText, fontFamily = PoppinsBold)
            }
        }
    }
}

/**
 * Card para mostrar estadísticas destacadas (Score, Rank, Popularity)
 */
@Composable
fun StatsCard(
    icon: ImageVector,
    label: String,
    value: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = accentColor
            )
            Text(
                text = value,
                fontSize = 24.asp(),
                fontFamily = PoppinsBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = label,
                fontSize = 12.asp(),
                fontFamily = PoppinsRegular,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Barra de rating con estrellas
 */
@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Float,
    onRatingChange: (Float) -> Unit,
    stars: Int = 10,
    starColor: Color = Color(0xFFFFD700)
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            for (i in 1..stars) {
                val starValue = i.toFloat()
                val isFilled = starValue <= rating
                val isHalfFilled = (starValue - 0.5f) <= rating && !isFilled

                val imageVector = when {
                    isFilled -> Icons.Default.Star
                    isHalfFilled -> Icons.AutoMirrored.Filled.StarHalf
                    else -> Icons.Default.StarBorder
                }

                val starTint = if (isFilled || isHalfFilled) starColor else Color.Gray

                Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                    tint = starTint,
                    modifier = Modifier
                        .size(32.adp())
                        .clickable {
                            onRatingChange(starValue)
                        }
                )
            }
        }
    }
}

// Funciones de utilidad para colores

private fun getInfoColor(infoType: String, value: String): Color {
    return when (infoType.lowercase()) {
        "score", "puntuación" -> when {
            value.toFloatOrNull()?.let { it >= 8.0f } == true -> Color(0xFF4CAF50)
            value.toFloatOrNull()?.let { it >= 6.0f } == true -> Color(0xFFFFB300)
            else -> Color(0xFFFF5722)
        }
        "rank", "ranking" -> Color(0xFFFFD700)
        "scored_by", "puntuado por" -> Color(0xFF2196F3)
        "type", "tipo" -> when (value.lowercase()) {
            "tv" -> Color(0xFF9C27B0)
            "movie" -> Color(0xFFE91E63)
            "ova" -> Color(0xFF00BCD4)
            "ona" -> Color(0xFF3F51B5)
            "special" -> Color(0xFFFF9800)
            "music" -> Color(0xFFFF5722)
            else -> Color(0xFF78909C)
        }
        "episodes", "episodios" -> Color(0xFF673AB7)
        "duration", "duración" -> Color(0xFF00ACC1)
        "season", "temporada" -> when (value.lowercase()) {
            "winter", "invierno" -> Color(0xFF2196F3)
            "spring", "primavera" -> Color(0xFF4CAF50)
            "summer", "verano" -> Color(0xFFFF9800)
            "fall", "otoño", "autumn" -> Color(0xFFFF5722)
            else -> Color(0xFF78909C)
        }
        "year", "año" -> Color(0xFF607D8B)
        "status", "estado" -> when (value.lowercase()) {
            "currently airing", "en emisión" -> Color(0xFF4CAF50)
            "finished airing", "finalizado" -> Color(0xFF2196F3)
            "not yet aired", "no emitido" -> Color(0xFFFF9800)
            else -> Color(0xFF78909C)
        }
        "aired", "transmitido" -> Color(0xFF8E24AA)
        "rating" -> Color(0xFFE91E63)
        "source", "origen" -> when (value.lowercase()) {
            "manga" -> Color(0xFFFF6F00)
            "light novel" -> Color(0xFF5E35B1)
            "visual novel" -> Color(0xFFD81B60)
            "game", "video game" -> Color(0xFF00897B)
            "original" -> Color(0xFFC62828)
            "web manga" -> Color(0xFFEF6C00)
            else -> Color(0xFF78909C)
        }
        else -> Color(0xFF78909C)
    }
}

private fun getGenreColor(genreName: String): Color {
    return when (genreName.lowercase()) {
        "action", "adventure", "military", "super power" -> Color(0xFFE91E63)
        "romance", "drama", "josei", "shoujo" -> Color(0xFFEC407A)
        "comedy", "slice of life", "school" -> Color(0xFFFFB300)
        "fantasy", "magic", "supernatural" -> Color(0xFF9C27B0)
        "sci-fi", "mecha", "space", "cars" -> Color(0xFF2196F3)
        "horror", "mystery", "thriller", "psychological" -> Color(0xFF455A64)
        "sports", "game" -> Color(0xFF4CAF50)
        "music", "harem", "ecchi" -> Color(0xFFFF5722)
        "shounen" -> Color(0xFFFF6F00)
        "seinen" -> Color(0xFF37474F)
        "historical", "samurai", "demons" -> Color(0xFF8D6E63)
        "kids" -> Color(0xFF26C6DA)
        else -> Color(0xFF78909C)
    }
}

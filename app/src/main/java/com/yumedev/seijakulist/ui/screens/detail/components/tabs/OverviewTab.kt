package com.yumedev.seijakulist.ui.screens.detail.components.tabs

import android.net.Uri
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulist.domain.models.AnimeDetail
import com.yumedev.seijakulist.ui.screens.detail.components.shared.*
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsMedium
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import com.yumedev.seijakulist.ui.theme.adp
import com.yumedev.seijakulist.ui.theme.asp
import com.yumedev.seijakulist.util.translateAiredDates
import com.yumedev.seijakulist.util.translateDuration
import com.yumedev.seijakulist.util.translateSeason
import com.yumedev.seijakulist.util.translateSource
import java.net.URLEncoder

/**
 * Tab de descripción general del anime
 */
@Composable
fun OverviewTab(
    animeDetail: AnimeDetail?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var synopsisExpanded by remember { mutableStateOf(false) }
    var backgroundExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // ESTADÍSTICAS DESTACADAS
        animeDetail?.let { anime ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (anime.score > 0.0f) {
                    StatsCard(
                        icon = Icons.Default.Star,
                        label = "Puntuación",
                        value = String.format(java.util.Locale.US, "%.2f", anime.score),
                        accentColor = Color(0xFF4CAF50),
                        modifier = Modifier.weight(1f)
                    )
                }
                if (anime.rank > 0) {
                    StatsCard(
                        icon = Icons.Default.BarChart,
                        label = "Ranking",
                        value = "#${anime.rank}",
                        accentColor = Color(0xFFFFD700),
                        modifier = Modifier.weight(1f)
                    )
                }
                if (anime.scoreBy > 0) {
                    StatsCard(
                        icon = Icons.Default.People,
                        label = "Usuarios",
                        value = String.format(java.util.Locale.US, "%,d", anime.scoreBy),
                        accentColor = Color(0xFF2196F3),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // GÉNEROS Y DEMOGRAPHICS
        animeDetail?.let { anime ->
            if (!anime.genres.isNullOrEmpty() || !anime.demographics.isNullOrEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
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
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Géneros y Demografía",
                            fontSize = 16.asp(),
                            fontFamily = PoppinsBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Demographics primero (más destacados)
                            anime.demographics?.filterNotNull()?.forEach { demographic ->
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                                    border = BorderStroke(
                                        1.5.dp,
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                                    ),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.People,
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = demographic.name ?: "",
                                            color = MaterialTheme.colorScheme.primary,
                                            fontFamily = PoppinsBold,
                                            fontSize = 13.asp()
                                        )
                                    }
                                }
                            }

                            // Géneros
                            anime.genres?.filterNotNull()?.forEach { genre ->
                                GenreChip(genreName = genre.name ?: "")
                            }
                        }
                    }
                }
            }
        }

        // SINOPSIS
        animeDetail?.let { anime ->
            if (!anime.synopsis.isNullOrBlank()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
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
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Sinopsis",
                                fontSize = 16.asp(),
                                fontFamily = PoppinsBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Surface(
                                onClick = {
                                    val synopsis = anime.synopsis
                                    val textToTranslate = if (synopsis.length > 2000) {
                                        synopsis.substring(0, 2000) + "..."
                                    } else {
                                        synopsis
                                    }
                                    val encodedText = URLEncoder.encode(textToTranslate, "UTF-8")
                                    val url = "https://translate.google.com/m?sl=en&tl=es&q=$encodedText"

                                    val customTabsIntent = CustomTabsIntent.Builder()
                                        .setShowTitle(true)
                                        .build()
                                    customTabsIntent.launchUrl(context, Uri.parse(url))
                                },
                                shape = RoundedCornerShape(20.dp),
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                border = BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                                ),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.OpenInNew,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "Traducir",
                                        fontSize = 13.asp(),
                                        fontFamily = PoppinsMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

                        Text(
                            text = anime.synopsis,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.asp(),
                            fontFamily = PoppinsRegular,
                            textAlign = TextAlign.Justify,
                            lineHeight = 22.asp(),
                            maxLines = if (synopsisExpanded) Int.MAX_VALUE else 8,
                            onTextLayout = { textLayoutResult = it }
                        )

                        if ((textLayoutResult?.hasVisualOverflow ?: false) || synopsisExpanded) {
                            TextButton(
                                onClick = { synopsisExpanded = !synopsisExpanded },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text(
                                    text = if (synopsisExpanded) "Ver menos" else "Ver más",
                                    fontFamily = PoppinsBold,
                                    fontSize = 13.asp()
                                )
                            }
                        }
                    }
                }
            }
        }

        // BACKGROUND INFO
        animeDetail?.let { anime ->
            if (!anime.background.isNullOrBlank()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onTertiaryContainer,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Información Adicional",
                                fontSize = 16.asp(),
                                fontFamily = PoppinsBold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }

                        var backgroundLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

                        Text(
                            text = anime.background,
                            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.9f),
                            fontSize = 14.asp(),
                            fontFamily = PoppinsRegular,
                            textAlign = TextAlign.Justify,
                            lineHeight = 22.asp(),
                            maxLines = if (backgroundExpanded) Int.MAX_VALUE else 5,
                            onTextLayout = { backgroundLayoutResult = it }
                        )

                        if ((backgroundLayoutResult?.hasVisualOverflow ?: false) || backgroundExpanded) {
                            TextButton(
                                onClick = { backgroundExpanded = !backgroundExpanded },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text(
                                    text = if (backgroundExpanded) "Ver menos" else "Ver más",
                                    fontFamily = PoppinsBold,
                                    fontSize = 13.asp(),
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            }
                        }
                    }
                }
            }
        }

        // TÍTULOS ALTERNATIVOS
        animeDetail?.let { anime ->
            if (!anime.titleEnglish.isNullOrBlank() || !anime.titleJapanese.isNullOrBlank()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
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
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Títulos Alternativos",
                            fontSize = 16.asp(),
                            fontFamily = PoppinsBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        if (!anime.titleEnglish.isNullOrBlank()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Text(
                                        text = "Inglés",
                                        fontSize = 11.asp(),
                                        fontFamily = PoppinsRegular,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = anime.titleEnglish,
                                        fontSize = 13.asp(),
                                        fontFamily = PoppinsMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(anime.titleEnglish))
                                        Toast.makeText(
                                            context,
                                            "Título copiado",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copiar",
                                        modifier = Modifier.size(16.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            if (!anime.titleJapanese.isNullOrBlank()) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)
                                )
                            }
                        }

                        if (!anime.titleJapanese.isNullOrBlank()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Text(
                                        text = "Japonés",
                                        fontSize = 11.asp(),
                                        fontFamily = PoppinsRegular,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = anime.titleJapanese,
                                        fontSize = 13.asp(),
                                        fontFamily = PoppinsMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(anime.titleJapanese))
                                        Toast.makeText(
                                            context,
                                            "Título copiado",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copiar",
                                        modifier = Modifier.size(16.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // INFORMACIÓN DETALLADA
        animeDetail?.let { anime ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
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
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Información Detallada",
                        fontSize = 16.asp(),
                        fontFamily = PoppinsBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        maxItemsInEachRow = 2
                    ) {
                        if (anime.episodes > 0) {
                            InfoPillCard(
                                label = "Episodios",
                                value = anime.episodes.toString(),
                                infoType = "episodes",
                                modifier = Modifier.weight(1f).height(IntrinsicSize.Max)
                            )
                        }
                        if (!anime.duration.isNullOrBlank()) {
                            InfoPillCard(
                                label = "Duración",
                                value = translateDuration(anime.duration),
                                infoType = "duration",
                                modifier = Modifier.weight(1f).height(IntrinsicSize.Max)
                            )
                        }
                        if (!anime.aired.isNullOrBlank()) {
                            InfoPillCard(
                                label = "Emisión",
                                value = translateAiredDates(anime.aired),
                                infoType = "aired",
                                modifier = Modifier.weight(1f).height(IntrinsicSize.Max)
                            )
                        }
                        if (!anime.season.isNullOrBlank()) {
                            InfoPillCard(
                                label = "Temporada",
                                value = "${translateSeason(anime.season)} ${anime.year}",
                                infoType = "season",
                                modifier = Modifier.weight(1f).height(IntrinsicSize.Max)
                            )
                        }
                        if (!anime.source.isNullOrBlank()) {
                            InfoPillCard(
                                label = "Fuente",
                                value = translateSource(anime.source),
                                infoType = "source",
                                modifier = Modifier.weight(1f).height(IntrinsicSize.Max)
                            )
                        }
                        if (!anime.rating.isNullOrBlank()) {
                            InfoPillCard(
                                label = "Clasificación",
                                value = anime.rating,
                                infoType = "rating",
                                modifier = Modifier.weight(1f).height(IntrinsicSize.Max)
                            )
                        }
                    }
                }
            }
        }
    }
}

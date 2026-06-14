package com.yumedev.seijakulist.ui.screens.detail.components.header

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.yumedev.seijakulist.domain.models.AnimeDetail
import com.yumedev.seijakulist.ui.screens.detail.components.shared.BroadcastBadge
import com.yumedev.seijakulist.ui.screens.detail.components.shared.StatusChip
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsMedium
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import com.yumedev.seijakulist.ui.theme.adp
import com.yumedev.seijakulist.ui.theme.asp

/**
 * Header limpio del detalle del anime
 * Layout: Portada + Información organizada en columna
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AnimeDetailHeader(
    animeDetail: AnimeDetail?,
    displayImageUrl: String,
    isAdded: Boolean,
    onAddToListClick: () -> Unit,
    onTrailerClick: (() -> Unit)? = null,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    animeId: Int? = null
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // SECCIÓN PRINCIPAL: Portada + Info
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // PORTADA
            Card(
                modifier = Modifier
                    .width(120.adp())
                    .height(180.adp()),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                val imageModifier = if (sharedTransitionScope != null &&
                    animatedVisibilityScope != null && animeId != null
                ) {
                    with(sharedTransitionScope) {
                        Modifier
                            .sharedElement(
                                sharedContentState = rememberSharedContentState(
                                    key = "anime-image-$animeId"
                                ),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                    }
                } else {
                    Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                }

                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(displayImageUrl)
                        .size(Size.ORIGINAL)
                        .crossfade(false)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = imageModifier
                )
            }

            // INFORMACIÓN PRINCIPAL
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(180.adp()),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // TÍTULOS Y ESTADO
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Título principal
                    Text(
                        text = animeDetail?.title ?: "",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.asp(),
                        fontFamily = PoppinsBold,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 22.asp()
                    )

                    // Título japonés
                    if (!animeDetail?.titleJapanese.isNullOrBlank()) {
                        Text(
                            text = animeDetail.titleJapanese,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.asp(),
                            fontFamily = PoppinsRegular,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Status chip
                    StatusChip(animeDetail?.status ?: "")

                    // Broadcast badge (solo si está en emisión)
                    if (animeDetail?.status == "Currently Airing" && animeDetail.broadcast != null) {
                        BroadcastBadge(
                            day = animeDetail.broadcast.day,
                            time = animeDetail.broadcast.time,
                            fullString = animeDetail.broadcast.fullString
                        )
                    }
                }

                // BOTONES DE ACCIÓN
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Botón Añadir/Editar
                    Button(
                        onClick = onAddToListClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isAdded)
                                MaterialTheme.colorScheme.secondaryContainer
                            else
                                MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = if (isAdded) Icons.Default.Edit else Icons.Default.Favorite,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isAdded) "Editar" else "Añadir",
                            fontSize = 13.asp(),
                            fontFamily = PoppinsBold
                        )
                    }

                    // Botón de trailer
                    if (animeDetail?.trailer?.youtubeId != null) {
                        IconButton(
                            onClick = {
                                val youtubeId = animeDetail.trailer.youtubeId
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://www.youtube.com/watch?v=$youtubeId")
                                )
                                context.startActivity(intent)
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.tertiaryContainer)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Ver trailer",
                                tint = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }
                }
            }
        }

        // STATS CARDS: Score, Ranking, etc.
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Score
                if (animeDetail?.score != null && animeDetail.score > 0) {
                    StatItem(
                        icon = Icons.Default.Star,
                        label = "Score",
                        value = String.format(java.util.Locale.US, "%.1f", animeDetail.score),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Rank
                if (animeDetail?.rank != null && animeDetail.rank > 0) {
                    StatItem(
                        icon = Icons.Default.BarChart,
                        label = "Rank",
                        value = "#${animeDetail.rank}",
                        modifier = Modifier.weight(1f)
                    )
                }

                // Popularity/Users
                if (animeDetail?.scoreBy != null && animeDetail.scoreBy > 0) {
                    StatItem(
                        icon = Icons.Default.People,
                        label = "Usuarios",
                        value = formatNumber(animeDetail.scoreBy),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // QUICK INFO CHIPS
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Type
            if (!animeDetail?.typeAnime.isNullOrBlank()) {
                InfoChip(
                    icon = Icons.Default.Tv,
                    text = animeDetail!!.typeAnime
                )
            }

            // Episodes
            if (animeDetail?.episodes != null && animeDetail.episodes > 0) {
                InfoChip(
                    icon = Icons.Default.OndemandVideo,
                    text = "${animeDetail.episodes} eps"
                )
            }

            // Season + Year
            if (!animeDetail?.season.isNullOrBlank() && animeDetail?.year != null) {
                InfoChip(
                    icon = Icons.Default.CalendarMonth,
                    text = "${animeDetail.season} ${animeDetail.year}"
                )
            } else if (animeDetail?.year != null) {
                InfoChip(
                    icon = Icons.Default.CalendarMonth,
                    text = animeDetail.year.toString()
                )
            }

            // Duration
            if (!animeDetail?.duration.isNullOrBlank()) {
                InfoChip(
                    icon = Icons.Default.Timer,
                    text = animeDetail.duration
                )
            }

            // Demographics
            animeDetail?.demographics?.filterNotNull()?.take(2)?.forEach { demographic ->
                InfoChip(
                    icon = Icons.Default.People,
                    text = demographic.name ?: "",
                    highlighted = true
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            fontSize = 16.asp(),
            fontFamily = PoppinsBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            fontSize = 11.asp(),
            fontFamily = PoppinsRegular,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun InfoChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    highlighted: Boolean = false
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (highlighted)
            MaterialTheme.colorScheme.primaryContainer
        else
            MaterialTheme.colorScheme.surfaceContainerHigh,
        border = if (highlighted)
            androidx.compose.foundation.BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            )
        else null
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = if (highlighted)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = text,
                fontSize = 12.asp(),
                fontFamily = PoppinsMedium,
                color = if (highlighted)
                    MaterialTheme.colorScheme.onPrimaryContainer
                else
                    MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

private fun formatNumber(number: Int): String {
    return when {
        number >= 1_000_000 -> String.format(java.util.Locale.US, "%.1fM", number / 1_000_000.0)
        number >= 1_000 -> String.format(java.util.Locale.US, "%.1fK", number / 1_000.0)
        else -> number.toString()
    }
}

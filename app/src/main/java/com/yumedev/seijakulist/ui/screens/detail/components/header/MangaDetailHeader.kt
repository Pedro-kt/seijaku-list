package com.yumedev.seijakulist.ui.screens.detail.components.header

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.yumedev.seijakulist.domain.models.MangaDetail
import com.yumedev.seijakulist.ui.screens.detail.components.shared.StatusChip
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsMedium
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import com.yumedev.seijakulist.ui.theme.adp
import com.yumedev.seijakulist.ui.theme.asp

/**
 * Header limpio del detalle del manga
 * Layout: Portada + Información organizada en columna
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MangaDetailHeader(
    mangaDetail: MangaDetail?,
    displayImageUrl: String,
    isAdded: Boolean,
    onAddToListClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    mangaId: Int? = null
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
                    animatedVisibilityScope != null && mangaId != null
                ) {
                    with(sharedTransitionScope) {
                        Modifier
                            .sharedElement(
                                sharedContentState = rememberSharedContentState(
                                    key = "manga-image-$mangaId"
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
                        text = mangaDetail?.title ?: "",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.asp(),
                        fontFamily = PoppinsBold,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 22.asp()
                    )

                    // Título japonés
                    if (!mangaDetail?.titleJapanese.isNullOrBlank()) {
                        Text(
                            text = mangaDetail.titleJapanese,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.asp(),
                            fontFamily = PoppinsRegular,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Status chip
                    StatusChip(mangaDetail?.status ?: "")
                }

                // BOTÓN DE ACCIÓN
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Botón Añadir/Editar
                    Button(
                        onClick = onAddToListClick,
                        modifier = Modifier
                            .fillMaxWidth()
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
                if (mangaDetail?.score != null && mangaDetail.score > 0) {
                    StatItem(
                        icon = Icons.Default.Star,
                        label = "Score",
                        value = String.format(java.util.Locale.US, "%.1f", mangaDetail.score),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Rank
                if (mangaDetail?.rank != null && mangaDetail.rank > 0) {
                    StatItem(
                        icon = Icons.Default.BarChart,
                        label = "Rank",
                        value = "#${mangaDetail.rank}",
                        modifier = Modifier.weight(1f)
                    )
                }

                // Popularity/Users
                if (mangaDetail?.scoreBy != null && mangaDetail.scoreBy > 0) {
                    StatItem(
                        icon = Icons.Default.People,
                        label = "Usuarios",
                        value = formatNumber(mangaDetail.scoreBy),
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
            if (!mangaDetail?.typeManga.isNullOrBlank()) {
                InfoChip(
                    icon = Icons.Default.MenuBook,
                    text = mangaDetail!!.typeManga
                )
            }

            // Chapters
            if (mangaDetail?.chapters != null && mangaDetail.chapters > 0) {
                InfoChip(
                    icon = Icons.Default.Article,
                    text = "${mangaDetail.chapters} capítulos"
                )
            }

            // Volumes
            if (mangaDetail?.volumes != null && mangaDetail.volumes > 0) {
                InfoChip(
                    icon = Icons.Default.Book,
                    text = "${mangaDetail.volumes} volúmenes"
                )
            }

            // Published
            if (!mangaDetail?.published.isNullOrBlank()) {
                InfoChip(
                    icon = Icons.Default.CalendarMonth,
                    text = mangaDetail.published
                )
            }

            // Demographics
            mangaDetail?.demographics?.filterNotNull()?.take(2)?.forEach { demographic ->
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

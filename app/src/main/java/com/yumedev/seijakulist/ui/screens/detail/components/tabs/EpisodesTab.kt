package com.yumedev.seijakulist.ui.screens.detail.components.tabs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulist.domain.models.AnimeEpisode
import com.yumedev.seijakulist.ui.screens.detail.components.shared.SectionHeader
import com.yumedev.seijakulist.ui.screens.detail.components.shared.StateMessage
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsMedium
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import com.yumedev.seijakulist.ui.theme.asp

/**
 * Tab de episodios del anime (versión simplificada)
 * TODO: Implementar funcionalidad completa de episodios con paginación y detalles
 */
@Composable
fun EpisodesTab(
    episodes: List<AnimeEpisode>,
    isLoading: Boolean,
    hasMore: Boolean,
    onLoadMore: () -> Unit,
    onEpisodeClick: (AnimeEpisode) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        SectionHeader(
            title = "Episodios",
            subtitle = "${episodes.size} disponibles",
            modifier = Modifier.padding(vertical = 8.dp)
        )

        when {
            isLoading && episodes.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            episodes.isEmpty() -> {
                StateMessage(
                    icon = Icons.Default.VideocamOff,
                    title = "Sin episodios",
                    description = "No hay información de episodios disponible para este anime."
                )
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    episodes.forEach { episode ->
                        EpisodeItem(
                            episode = episode,
                            onClick = { onEpisodeClick(episode) },
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    if (hasMore) {
                        OutlinedButton(
                            onClick = onLoadMore,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            enabled = !isLoading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Text(
                                text = if (isLoading) "Cargando..." else "Cargar más episodios",
                                fontFamily = PoppinsBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EpisodeItem(
    episode: AnimeEpisode,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Episode number badge
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            ) {
                Text(
                    text = "${episode.malId}",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.asp(),
                    fontFamily = PoppinsBold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Episode info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = episode.title ?: "Episodio ${episode.malId}",
                    fontSize = 14.asp(),
                    fontFamily = PoppinsMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (!episode.titleJapanese.isNullOrBlank()) {
                    Text(
                        text = episode.titleJapanese,
                        fontSize = 11.asp(),
                        fontFamily = PoppinsRegular,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (!episode.aired.isNullOrBlank()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = episode.aired,
                            fontSize = 11.asp(),
                            fontFamily = PoppinsRegular,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

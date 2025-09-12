package com.example.seijakulist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.seijakulist.domain.models.AnimeCard
import com.example.seijakulist.ui.navigation.AppDestinations
import com.example.seijakulist.ui.theme.RobotoBold
import com.example.seijakulist.ui.theme.RobotoRegular

@Composable
fun AnimeRandomCard(
    anime: AnimeCard,
    navController: NavController,
    onRefresh: () -> Unit
) {

    ElevatedCard(
        onClick = { navController.navigate("${AppDestinations.ANIME_DETAIL_ROUTE}/${anime.malId}") },
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        var isLiked by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerHighest
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(anime.images)
                        .crossfade(true)
                        .build(),
                    contentDescription = anime.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(140.dp)
                        .height(210.dp)
                )
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(
                            start = 12.dp, end = 12.dp, top = 8.dp, bottom = 8.dp
                        ),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = anime.title,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(end = 40.dp)
                                .fillMaxWidth(),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 18.sp,
                            fontFamily = RobotoBold
                        )
                        Text(
                            text = "${anime.year ?: "N/A"} • ${anime.episodes ?: "?"} episodes",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 4.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp,
                            fontFamily = RobotoRegular,
                        )
                    }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        items(anime.genres.take(2)) { genre ->
                            genre?.name?.let {
                                GenreChip(text = it)
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(48.dp)
                    .align(Alignment.CenterEnd)
                    .background(
                        brush = Brush.horizontalGradient(listOf(
                            MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0f),
                            MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.8f)
                        ))
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = onRefresh,
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refrescar",
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}

@Composable
fun GenreChip(text: String) {
    Text(
        text = text,
        fontSize = 11.sp,
        color = MaterialTheme.colorScheme.onSecondaryContainer,
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

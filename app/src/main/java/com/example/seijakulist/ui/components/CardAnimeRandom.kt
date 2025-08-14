package com.example.seijakulist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.example.seijakulist.R
import com.example.seijakulist.domain.models.Anime
import com.example.seijakulist.domain.models.AnimeCard
import com.example.seijakulist.ui.navigation.AppDestinations

@Composable
fun AnimeRandomCard(
    anime: AnimeCard,
    navController: NavController,
    onRefresh: () -> Unit
) {

    var isLiked by remember { mutableStateOf(false) }

    val RobotoBold = FontFamily(
        Font(R.font.roboto_bold)
    )
    val RobotoRegular = FontFamily(
        Font(R.font.roboto_regular)
    )

    val gradientColorsTopBar = listOf(
        Color(0xFF160078),
        Color(0xff7226ff),
        Color(0xFF160078),
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFF202020)
                )
                .clip(RoundedCornerShape(16.dp))
                .clickable {
                    navController.navigate("${AppDestinations.ANIME_DETAIL_ROUTE}/${anime.malId}")
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(anime.images)
                        .size(Size.ORIGINAL)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Imagen de portada",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(140.dp)
                        .height(210.dp)
                        .clip(RoundedCornerShape(16.dp))

                )
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = anime.title,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .padding(start = 16.dp, top = 16.dp, end = 40.dp)
                            .fillMaxWidth(),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontFamily = RobotoBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .height(32.dp)
                            .wrapContentWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Puntuacion",
                            tint = Color(0xFFFDC700),
                            modifier = Modifier
                                .size(16.dp)
                        )
                        Text(
                            text = anime.score.toString(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .wrapContentWidth(),
                            color = Color(0xFFFDC700),
                            fontSize = 16.sp,
                            fontFamily = RobotoRegular
                        )
                        Text("•")
                        Icon(
                            imageVector = Icons.Default.Alarm,
                            contentDescription = "Icono de estrellas",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = anime.status,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(),
                            color = Color.White,
                            fontSize = 16.sp,
                            fontFamily = RobotoRegular
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(modifier = Modifier.padding(start = 16.dp)) {
                        item {
                            anime.genres.forEach { genreDto ->
                                ElevatedFilterChip(
                                    selected = false,
                                    onClick = { },
                                    label = {
                                        genreDto?.name?.let { Text(it, fontSize = 12.sp) }
                                    },
                                    modifier = Modifier.padding(end = 8.dp),
                                    colors = FilterChipDefaults.filterChipColors(
                                        containerColor = Color(0xFF404040),
                                        labelColor = Color.White.copy(alpha = 0.9f),
                                        selectedContainerColor = Color(0xff404040),
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(modifier = Modifier.padding(start = 16.dp)) {
                        Text(
                            text = "Año ${anime.year} • Episodios ${anime.episodes}",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 12.sp,
                            fontFamily = RobotoRegular,
                        )
                    }
                }
            }

            IconButton(
                onClick = onRefresh,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refrescar",
                    tint = Color.White,
                )
            }
            IconButton(
                onClick = { isLiked = !isLiked },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Añadir a favoritos",
                    tint = if (isLiked) Color.Red else Color.White
                )
            }
        }
    }
}
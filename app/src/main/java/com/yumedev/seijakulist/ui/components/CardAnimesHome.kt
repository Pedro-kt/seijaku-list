package com.yumedev.seijakulist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.yumedev.seijakulist.domain.models.Anime
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsMedium

@Composable
fun CardAnimesHome(
    animeList: List<Anime>,
    navController: NavController,
    localAnimeStatuses: Map<Int, String> = emptyMap()
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(animeList) { anime ->
            AnimeSectionCard(
                anime = anime,
                navController = navController,
                localAnimeStatuses = localAnimeStatuses
            )
        }
    }
}

@Composable
private fun AnimeSectionCard(
    anime: Anime,
    navController: NavController,
    localAnimeStatuses: Map<Int, String> = emptyMap()
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .width(135.dp) // Un poquito más ancha para que luzca el arte
            .clip(RoundedCornerShape(16.dp))
    ) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .clickable {
                    navController.navigate("${AppDestinations.ANIME_DETAIL_ROUTE}/${anime.malId}")
                }
        ) {
            // Imagen con transición suave
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(anime.image)
                    .crossfade(true)
                    .build(),
                contentDescription = anime.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            )

            // Gradiente superior para proteger la legibilidad del Score
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Black.copy(alpha = 0.5f), Color.Transparent)
                        )
                    )
            )

            // Score con diseño de "Badge"
            if (anime.score > 0) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .background(
                            color = Color.Black.copy(alpha = 0.75f),
                            shape = RoundedCornerShape(bottomStart = 6.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 3.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = String.format("%.1f", anime.score),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 11.sp,
                        fontFamily = PoppinsBold
                    )
                }
            }

            // Badge "en lista" con el estado del usuario
            val userStatus = localAnimeStatuses[anime.malId]
            if (userStatus != null) {
                val statusColor = when (userStatus) {
                    "Viendo" -> Color(0xFF66BB6A)
                    "Completado" -> Color(0xFF42A5F5)
                    "Pendiente" -> Color(0xFFFFCA28)
                    "Abandonado" -> Color(0xFFEF5350)
                    "Planeado" -> Color(0xFF78909C)
                    else -> Color.Gray
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .clip(RoundedCornerShape(bottomEnd = 6.dp))
                        .background(Color.Black.copy(alpha = 0.75f))
                        .padding(horizontal = 6.dp, vertical = 3.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .background(statusColor, CircleShape)
                    )
                    Text(
                        text = userStatus,
                        color = Color.White,
                        fontSize = 9.sp,
                        fontFamily = PoppinsBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Título con mejor interlineado
        Text(
            text = anime.title,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(
                fontFamily = PoppinsMedium, // Medium suele verse mejor que Regular en títulos
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
            ),
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}
@Composable
fun CardAnimesHomeGrid(
    anime: Anime,
    navController: NavController,
    modifier: Modifier = Modifier,
    localAnimeStatuses: Map<Int, String> = emptyMap()
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "card_scale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clip(RoundedCornerShape(12.dp))
            .clickable(interactionSource = interactionSource, indication = null) {
                navController.navigate("${AppDestinations.ANIME_DETAIL_ROUTE}/${anime.malId}")
            }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(anime.image)
                .size(Size.ORIGINAL)
                .crossfade(true)
                .build(),
            contentDescription = anime.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(195.dp)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
        )

        // Gradiente inferior para legibilidad del título
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.88f))
                    )
                )
        )

        // Título superpuesto sobre la imagen
        Text(
            text = anime.title,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = Color.White,
            fontFamily = PoppinsMedium,
            fontSize = 11.sp,
            lineHeight = 15.sp,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 6.dp, vertical = 6.dp)
        )

        // Badge de puntuación
        if (anime.score > 0) {
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(horizontal = 6.dp, vertical = 3.dp),
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(11.dp)
                )
                Text(
                    text = String.format("%.1f", anime.score),
                    color = Color.White,
                    fontSize = 11.sp,
                    fontFamily = PoppinsBold
                )
            }
        }

        // Badge "en lista" con el estado del usuario
        val userStatus = localAnimeStatuses[anime.malId]
        if (userStatus != null) {
            val statusColor = when (userStatus) {
                "Viendo" -> Color(0xFF66BB6A)
                "Completado" -> Color(0xFF42A5F5)
                "Pendiente" -> Color(0xFFFFCA28)
                "Abandonado" -> Color(0xFFEF5350)
                "Planeado" -> Color(0xFF78909C)
                else -> Color.Gray
            }
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black.copy(alpha = 0.75f))
                    .padding(horizontal = 6.dp, vertical = 3.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .background(statusColor, CircleShape)
                )
                Text(
                    text = userStatus,
                    color = Color.White,
                    fontSize = 9.sp,
                    fontFamily = PoppinsBold
                )
            }
        }
    }
}
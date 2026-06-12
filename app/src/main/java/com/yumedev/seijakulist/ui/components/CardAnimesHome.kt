package com.yumedev.seijakulist.ui.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
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
import com.yumedev.seijakulist.ui.theme.adp
import com.yumedev.seijakulist.ui.theme.asp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.yumedev.seijakulist.domain.models.Anime
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsMedium
import com.yumedev.seijakulist.ui.screens.detail.AnimeTransitionCache

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CardAnimesHome(
    animeList: List<Anime>,
    navController: NavController,
    localAnimeStatuses: Map<Int, String> = emptyMap(),
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(animeList, key = { it.malId }) { anime ->
            AnimeSectionCard(
                anime = anime,
                navController = navController,
                localAnimeStatuses = localAnimeStatuses,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun AnimeSectionCard(
    anime: Anime,
    navController: NavController,
    localAnimeStatuses: Map<Int, String> = emptyMap(),
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .width(135.adp())
            .height(200.adp())
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                // Guardar imagen en cache para la transición
                AnimeTransitionCache.setAnimeImage(anime.malId, anime.image)
                navController.navigate("${AppDestinations.ANIME_DETAIL_ROUTE}/${anime.malId}")
            }
    ) {
        // Imagen de fondo completa con shared element transition
        val imageModifier = if (sharedTransitionScope != null && animatedVisibilityScope != null) {
            with(sharedTransitionScope) {
                Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(key = "anime-image-${anime.malId}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            }
        } else {
            Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
        }

        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(anime.image)
                .crossfade(false) // Desactivar crossfade para que no interfiera con la transición
                .build(),
            contentDescription = anime.title,
            contentScale = ContentScale.Crop,
            modifier = imageModifier
        )

        // Gradiente superior para Score y Estado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.adp())
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color.Black.copy(alpha = 0.85f),
                            0.5f to Color.Black.copy(alpha = 0.5f),
                            1.0f to Color.Transparent
                        )
                    )
                )
        )

        // Gradiente inferior para Título
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.adp())
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color.Transparent,
                            0.4f to Color.Black.copy(alpha = 0.6f),
                            1.0f to Color.Black.copy(alpha = 0.92f)
                        )
                    )
                )
        )

        // Score en la parte superior derecha
        if (anime.score > 0) {
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = String.format("%.1f", anime.score),
                    color = Color.White,
                    fontSize = 12.asp(),
                    fontFamily = PoppinsBold
                )
            }
        }

        // Estado del anime en la parte superior izquierda
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
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(statusColor, CircleShape)
                )
                Text(
                    text = userStatus,
                    color = Color.White,
                    fontSize = 10.asp(),
                    fontFamily = PoppinsBold
                )
            }
        }

        // Título en la parte inferior
        Text(
            text = anime.title,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(
                fontFamily = PoppinsMedium,
                fontSize = 13.asp(),
                lineHeight = 17.asp(),
                color = Color.White
            ),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 10.dp, vertical = 10.dp)
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
                // Guardar imagen en cache para la transición
                AnimeTransitionCache.setAnimeImage(anime.malId, anime.image)
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
                .height(195.adp())
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
        )

        // Gradiente inferior para legibilidad del título
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.adp())
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
            fontSize = 11.asp(),
            lineHeight = 15.asp(),
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
                    fontSize = 11.asp(),
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
                    fontSize = 9.asp(),
                    fontFamily = PoppinsBold
                )
            }
        }
    }
}
package com.yumedev.seijakulist.ui.screens.local_anime_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.yumedev.seijakulist.ui.components.TitleWithPadding
import com.yumedev.seijakulist.ui.theme.RobotoBold
import com.yumedev.seijakulist.ui.theme.RobotoRegular

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeDetailScreenLocal(
    navController: NavController,
    viewModel: LocalAnimeDetailViewModel = hiltViewModel(),
    animeId: Int
) {

    val anime by viewModel.anime.collectAsState()
    val isSharing by viewModel.isSharing.collectAsState()
    val context = LocalContext.current
    val focusManager: FocusManager = LocalFocusManager.current

    when (val currentAnime = anime) {
        null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { focusManager.clearFocus() }
                        )
                    }
            ) {
                // Top App Bar
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Transparent,
                    tonalElevation = 3.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "Mi Anime",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 20.sp,
                            fontFamily = RobotoBold,
                            modifier = Modifier.align(Alignment.Center)
                        )
                        IconButton(
                            onClick = { viewModel.shareAnime(context) },
                            enabled = !isSharing,
                            modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
                            if (isSharing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Compartir",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Header con imagen
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(350.dp)
                        ) {
                            // Fondo borroso
                            Image(
                                painter = rememberAsyncImagePainter(
                                    ImageRequest.Builder(LocalContext.current)
                                        .data(data = currentAnime.image)
                                        .apply(block = fun ImageRequest.Builder.() {
                                            size(Size.ORIGINAL)
                                        }).build()
                                ),
                                contentDescription = "Fondo",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .blur(radius = 25.dp)
                                    .scale(1.2f),
                                contentScale = ContentScale.Crop,
                            )
                            // Overlay gradient
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
                                                Color.Transparent,
                                                MaterialTheme.colorScheme.background
                                            ),
                                            startY = 0f,
                                            endY = Float.POSITIVE_INFINITY
                                        )
                                    )
                            )
                            // Contenido
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Imagen del anime
                                Card(
                                    modifier = Modifier
                                        .width(160.dp)
                                        .height(240.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(currentAnime.image),
                                        contentDescription = currentAnime.title,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                // Información básica
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 4.dp)
                                ) {
                                    Text(
                                        text = currentAnime.title,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 22.sp,
                                        fontFamily = RobotoBold,
                                        maxLines = 4,
                                        overflow = TextOverflow.Ellipsis,
                                        lineHeight = 26.sp
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Puntuación
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = "Puntuación",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = if (currentAnime.userScore % 1.0 == 0.0) {
                                                    "${currentAnime.userScore.toInt()}/10"
                                                } else {
                                                    "${currentAnime.userScore}/10"
                                                },
                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                fontSize = 16.sp,
                                                fontFamily = RobotoBold
                                            )
                                        }
                                    }

                                    // Estado
                                    Text(
                                        text = currentAnime.userStatus,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 14.sp,
                                        fontFamily = RobotoRegular
                                    )
                                }
                            }
                        }
                    }

                    // Botones de acción
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            FilledTonalButton(
                                onClick = { /* TODO: Editar puntuación */ },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Star,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Puntuar")
                            }

                            OutlinedButton(
                                onClick = { /* TODO: Editar información */ },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Edit,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Editar")
                            }
                        }
                    }

                    // Estadísticas rápidas
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                // Episodios vistos
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Tv,
                                        contentDescription = "Episodios",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "${currentAnime.episodesWatched}/${currentAnime.totalEpisodes ?: "?"}",
                                        fontSize = 18.sp,
                                        fontFamily = RobotoBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Episodios",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                // Veces visto
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Autorenew,
                                        contentDescription = "Veces visto",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "${currentAnime.rewatchCount}",
                                        fontSize = 18.sp,
                                        fontFamily = RobotoBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Veces visto",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    // Reseña
                    item {
                        if (!currentAnime.userOpiniun.isNullOrEmpty()) {
                            Spacer(modifier = Modifier.height(24.dp))

                            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Mi Reseña",
                                        fontSize = 20.sp,
                                        fontFamily = RobotoBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    IconButton(
                                        onClick = { /* TODO: Editar reseña */ }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Editar reseña",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                                    )
                                ) {
                                    Text(
                                        text = currentAnime.userOpiniun,
                                        modifier = Modifier.padding(20.dp),
                                        textAlign = TextAlign.Justify,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 22.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }
        }
    }
}




package com.example.seijakulist.ui.screens.local_anime_detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.seijakulist.R
import com.example.seijakulist.ui.components.TitleScreen
import com.example.seijakulist.ui.components.TitleWithPadding
import com.example.seijakulist.ui.theme.RobotoBold
import com.example.seijakulist.ui.theme.RobotoRegular

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeDetailScreenLocal(
    navController: NavController,
    viewModel: LocalAnimeDetailViewModel = hiltViewModel(),
    animeId: Int
) {

    val anime by viewModel.anime.collectAsState()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager: FocusManager = LocalFocusManager.current


    when (val currentAnime = anime) {
        null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.inversePrimary)
            }
        }

        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(
                        key1 = Unit
                    ) {
                        detectTapGestures(
                            onTap = {
                                focusManager.clearFocus()
                            }
                        )
                    }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(MaterialTheme.colorScheme.background)
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
                        text = "Detalle del anime",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp,
                        fontFamily = com.example.seijakulist.ui.theme.RobotoBold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    IconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share"
                        )
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(350.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    ImageRequest.Builder(LocalContext.current)
                                        .data(data = currentAnime.image)
                                        .apply(block = fun ImageRequest.Builder.() {
                                            size(Size.ORIGINAL)
                                        }).build()
                                ),
                                contentDescription = "Imagen de fondo",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .blur(radius = 20.dp)
                                    .scale(1.1f),
                                contentScale = ContentScale.Crop,
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.background,
                                                Color.Transparent,
                                                MaterialTheme.colorScheme.background
                                            ),
                                            startY = 0f,
                                            endY = Float.POSITIVE_INFINITY
                                        )
                                    )
                            )
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.TopStart
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(
                                            start = 16.dp,
                                        )
                                        .fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(currentAnime.image),
                                        contentDescription = currentAnime.title,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .width(160.dp)
                                            .height(240.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(end = 16.dp, top = 16.dp)
                                    ) {
                                        Text(
                                            text = currentAnime.title,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontSize = 20.sp,
                                            fontFamily = RobotoBold,
                                            maxLines = 3,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            modifier = Modifier.padding(top = 8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = "Icono de estrellas",
                                                tint = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = currentAnime.userScore.toString(),
                                                color = MaterialTheme.colorScheme.onSurface,
                                                fontSize = 16.sp,
                                            )
                                        }
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            modifier = Modifier.padding(top = 8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircleOutline,
                                                contentDescription = "Icono de estrellas",
                                                tint = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = currentAnime.userStatus,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                fontSize = 16.sp,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    item {
                        if (!currentAnime.userOpiniun.isNullOrEmpty()) {
                            TitleWithPadding("Mi actividad")
                            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = CardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                        contentColor = MaterialTheme.colorScheme.onSurface,
                                        disabledContainerColor = Color.Transparent,
                                        disabledContentColor = Color.Transparent
                                    )
                                ) {
                                    Column(Modifier.padding(16.dp)) {
                                        Text(
                                            text = "Reseña",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontFamily = RobotoBold
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = currentAnime.userOpiniun,
                                            textAlign = TextAlign.Justify
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Autorenew,
                                        contentDescription = "Veces visto"
                                    )
                                    Text(
                                        text = "Visto ${currentAnime.rewatchCount} veces",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontFamily = RobotoRegular
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Tv,
                                        contentDescription = "Episodios vistos"
                                    )
                                    Text(
                                        text = "Episodios vistos: ${currentAnime.episodesWatched}/${currentAnime.totalEpisodes ?: "?"}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontFamily = RobotoRegular
                                    )
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}




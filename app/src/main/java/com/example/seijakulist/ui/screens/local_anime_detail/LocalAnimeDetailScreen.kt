package com.example.seijakulist.ui.screens.local_anime_detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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

@Composable
fun AnimeDetailScreenLocal(
    navController: NavController,
    viewModel: LocalAnimeDetailViewModel = hiltViewModel(),
    animeId: Int
) {

    val anime by viewModel.anime.collectAsState()

    val RobotoRegular = FontFamily(
        Font(R.font.roboto_regular)
    )
    val RobotoBold = FontFamily(
        Font(R.font.roboto_bold, FontWeight.Bold)
    )

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }


    when (val currentAnime = anime) {
        null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }

        else -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFF050505))
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                            .padding(top = 16.dp)
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
                                            Color(0xFF050505), Color.Transparent
                                        ),
                                        startY = 0f,
                                        endY = 400f,
                                    )
                                )
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent, Color(0xFF050505)
                                        ), startY = 400f, endY = Float.POSITIVE_INFINITY
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
                                    //dialogo
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 16.dp, top = 16.dp)
                                ) {
                                    Text(
                                        text = currentAnime.title,
                                        color = Color.White,
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
                                            tint = Color.White
                                        )
                                        Text(
                                            text = currentAnime.userScore.toString(),
                                            color = Color.White,
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
                                            tint = Color.White
                                        )
                                        Text(
                                            text = currentAnime.userStatus,
                                            color = Color.White,
                                            fontSize = 16.sp,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                item {
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        colors = CardColors(
                            contentColor = Color.White,
                            containerColor = Color(0xff050505),
                            disabledContainerColor = Color.Red,
                            disabledContentColor = Color.Cyan,
                        ),
                        border = BorderStroke(
                            width = 1.dp, color = Color.White.copy(alpha = 0.7f)
                        ),
                    ) {
                        TitleScreen("Opinion personal")

                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            Text(
                                text = currentAnime.userOpiniun,
                                color = Color.Gray,
                                fontSize = 14.sp,
                                fontFamily = RobotoRegular,
                                textAlign = TextAlign.Justify,
                            )
                        }
                    }
                }
            }
        }
    }
}




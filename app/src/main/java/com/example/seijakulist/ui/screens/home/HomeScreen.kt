package com.example.seijakulist.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MultilineChart
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.ConnectedTv
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Hail
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.OnlinePrediction
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Score
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.room.util.TableInfo
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.example.seijakulist.R
import com.example.seijakulist.domain.models.Anime
import com.example.seijakulist.ui.components.BottomNavItemScreen
import com.example.seijakulist.ui.components.CardAnimesHome
import com.example.seijakulist.ui.components.LoadingScreen
import com.example.seijakulist.ui.components.SubTitleIcon
import com.example.seijakulist.ui.components.SubTitleWithoutIcon
import com.example.seijakulist.ui.components.TitleScreen
import com.example.seijakulist.ui.components.TopHomeScreen
import com.example.seijakulist.ui.navigation.AppDestinations


data class BottomNavItem(
    val name: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String
)

@Composable
fun HomeScreen(
    navController: NavController,
    seasonNowViewModel: AnimeSeasonNowViewModel = hiltViewModel(),
    topAnimesViewModel: TopAnimeViewModel = hiltViewModel(),
    seasonUpcomingViewModel: AnimeSeasonUpcomingViewModel = hiltViewModel(),
    viewModel: AnimeRandomViewModel = hiltViewModel()
) {

    val animeSeasonNow by seasonNowViewModel.animeList.collectAsState()
    val animeSeasonNowIsLoading by seasonNowViewModel.isLoading.collectAsState()
    val animeSeasonNowErrorMessage by seasonNowViewModel.errorMessage.collectAsState()

    val topAnimes by topAnimesViewModel.animeList.collectAsState()
    val topAnimeIsLoading by topAnimesViewModel.isLoading.collectAsState()
    val topAnimeErrorMessage by topAnimesViewModel.errorMessage.collectAsState()

    val animeSeasonUpcoming by seasonUpcomingViewModel.animeList.collectAsState()
    val animeSeasonUpcomingIsLoading by seasonUpcomingViewModel.isLoading.collectAsState()
    val animeSeasonUpcomingErrorMessage by seasonUpcomingViewModel.errorMessage.collectAsState()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val RobotoRegular = FontFamily(
        Font(R.font.roboto_regular)
    )
    val RobotoBold = FontFamily(
        Font(R.font.roboto_bold, FontWeight.Bold)
    )

    val currentRoute = navController.currentBackStackEntry?.destination?.route

    val styledText = buildAnnotatedString {
        // Estilo para la primera parte del texto (blanco)
        withStyle(style = SpanStyle(color = Color(0xFF673AB7))) {
            append("S")
        }

        // Estilo para la segunda parte del texto (verde)
        withStyle(style = SpanStyle(color = Color.White)) {
            append("eija")
        }

        withStyle(style = SpanStyle(color = Color(0xFF673AB7))) {
            append("ku")
        }

        withStyle(style = SpanStyle(color = Color.Transparent)) {
            append(" ")
        }

        withStyle(style = SpanStyle(color = Color.White)) {
            append("L")
        }

        withStyle(style = SpanStyle(color = Color(0xFF673AB7))) {
            append("is")
        }

        withStyle(style = SpanStyle(color = Color.White)) {
            append("t")
        }

    }

    val gradientColors = listOf(
        Color(0xFF673AB7),
        Color(0xFF020202),
        Color(0xFF050505),
        Color(0xFF090909),
        Color(0xFF090909),
        Color.Black,
        Color.Black,
    )

    val gradientColorsTopBar = listOf(
        Color(0xFF020202),
        Color(0xFF100F0F),
        Color(0xFF151414),
        Color(0xFF090909),
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = gradientColors,
                )
            )
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            /*
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .height(48.dp)
                        .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                        .background(color = Color.Black)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = styledText,
                        fontSize = 32.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f),
                        fontFamily = RobotoBold
                    )
                }
            }
            ,
             */
            bottomBar = {
                BottomNavItemScreen(navController)
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {

                if (animeSeasonNowIsLoading) {
                    LoadingScreen()
                } else if (animeSeasonNowErrorMessage != null) {
                    Text(
                        text = animeSeasonNowErrorMessage!!,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else if (animeSeasonNow.isNotEmpty()) {

                    TopHomeScreen(navController)

                    LazyColumn() {
                        item {

                        }
                        item {
                            SubTitleWithoutIcon("En emision")
                        }
                        item {
                            CardAnimesHome(animeSeasonNow, navController)
                        }
                        item {
                            SubTitleWithoutIcon("Random")
                        }
                        item {
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .fillMaxSize()
                                    .height(230.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(color = Color(0xFF040404)),
                                    //.border(width = 1.dp, color = Color(0xFF673AB7), shape = RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center // Centra los elementos de carga y error
                            ) {

                                // 3. Maneja los diferentes estados
                                when {
                                    uiState.isLoading -> {
                                        // Muestra un indicador de carga si está cargando
                                            LoadingScreen()
                                    }
                                    uiState.errorMessage != null -> {
                                        // Muestra un mensaje de error si hay un problema
                                        Text(
                                            text = uiState.errorMessage!!,
                                            color = MaterialTheme.colorScheme.error,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                    uiState.anime != null -> {
                                        // Si hay un anime, muestra la tarjeta
                                        AnimeRandomCard(
                                            anime = uiState.anime!!,
                                            navController = navController,
                                            onRefresh = { viewModel.loadRandomAnime() }
                                        )
                                    }
                                    // Estado inicial: puedes mostrar un botón para cargar por primera vez
                                    else -> {
                                        Button(onClick = { viewModel.loadRandomAnime() }) {
                                            Text("Cargar Animes")
                                        }
                                    }
                                }
                            }
                        }
                        item {
                            SubTitleWithoutIcon("Top scores")
                        }
                        item {
                            CardAnimesHome(topAnimes, navController)
                        }
                        item {
                            SubTitleWithoutIcon("Proxima temporada")
                        }
                        item {
                            CardAnimesHome(animeSeasonUpcoming, navController)
                        }
                        item {
                            TitleScreen("Explora Mangas!")
                        }
                        item {
                            Text(
                                text = "Proximamente",
                                color = Color.White,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                fontFamily = RobotoBold
                            )
                        }
                    }
                } else {
                    Text(
                        text = "No se encontraron animes.",
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
fun AnimeRandomCard(
    anime: Anime,
    navController: NavController,
    onRefresh: () -> Unit
) {
    val RobotoBold = FontFamily(
        Font(R.font.roboto_bold)
    )

    val gradientsColorsAnimeRandom = listOf(
        Color(0xFF5D428D),
        Color(0xFF673AB7),
        Color(0xFF8459D2),
        Color(0xFF5D428D),
        Color(0xFF673AB7),
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp),
    ) {
        Box(modifier = Modifier.fillMaxWidth().background(brush = Brush.horizontalGradient(
            colors = gradientsColorsAnimeRandom
        ))) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                // La imagen del anime
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(anime.image)
                        .size(Size.ORIGINAL)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Imagen de portada",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(140.dp)
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            navController.navigate("${AppDestinations.ANIME_DETAIL_ROUTE}/${anime.malId}")
                        }
                        .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(8.dp))
                )
                // Columna para el título y otros datos (como la puntuación)
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom // Alinea el contenido de la columna arriba
                ) {
                    // El título, que estará en el top de la columna
                    SubTitleWithoutIcon(anime.title)

                    Row(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .height(32.dp)
                            .wrapContentWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Puntuacion",
                            tint = Color.Yellow,
                            modifier = Modifier
                                .size(24.dp)
                        )
                        Text(
                            text = anime.score.toString(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.wrapContentWidth().padding(end = 16.dp),
                            color = Color.White,
                            fontSize = 24.sp,
                            fontFamily = RobotoBold
                        )
                    }
                }
            }

            // El botón de refrescar, que se apila encima del contenido
            IconButton(
                onClick = onRefresh,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refrescar"
                )
            }
        }
    }
}
package com.example.seijakulist.ui.screens.home

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ConnectedTv
import androidx.compose.material.icons.filled.Hail
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.draw.clip
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.example.seijakulist.R
import com.example.seijakulist.ui.components.BottomNavItemScreen
import com.example.seijakulist.ui.components.LoadingScreen
import com.example.seijakulist.ui.components.SubTitleIcon
import com.example.seijakulist.ui.components.TitleScreen
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
    seasonUpcomingViewModel: AnimeSeasonUpcomingViewModel = hiltViewModel()
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



    val RobotoRegular = FontFamily(
        Font(R.font.roboto_regular)
    )
    val RobotoBold = FontFamily(
        Font(R.font.roboto_bold, FontWeight.Bold)
    )

    val navItems = listOf(
        BottomNavItem(name = "Mis Animes", icon = Icons.Default.Tv, route = AppDestinations.MY_ANIMES_ROUTE),
        BottomNavItem(name = "Mis Mangas", icon = Icons.AutoMirrored.Outlined.MenuBook, route = AppDestinations.MY_MANGAS_ROUTE),
        BottomNavItem(name = "Home", icon = Icons.Default.Home, route = AppDestinations.HOME),
        BottomNavItem(name = "Buscar", icon = Icons.Default.Search, route = AppDestinations.SEARCH_ANIME_ROUTE),
        BottomNavItem(name = "Perfil", icon = Icons.Default.AccountCircle, route = AppDestinations.MY_PROFILE_ROUTE)
    )

    val currentRoute = navController.currentBackStackEntry?.destination?.route

    Scaffold(
        containerColor = Color(0xFF050505),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(color = Color(0xFF121212))
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Home",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                    fontFamily = RobotoBold
                )
            }
        },
        bottomBar = {
            BottomNavItemScreen(navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF050505))
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

                LazyColumn() {

                    item {
                        TitleScreen("Animes")
                    }

                    item {
                        SubTitleIcon("En emision", Icons.Default.ConnectedTv)
                    }

                    item {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(animeSeasonNow) { anime ->
                                Column(
                                    modifier = Modifier
                                        .width(130.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box() {

                                        AsyncImage(
                                            model = ImageRequest.Builder(LocalContext.current)
                                                .data(anime.image)
                                                .size(Size.ORIGINAL)
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = "Imagen de portada",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .width(130.dp)
                                                .height(190.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .clickable {
                                                    navController.navigate("${AppDestinations.ANIME_DETAIL_ROUTE}/${anime.malId}")
                                                }
                                        )

                                        Row(
                                            modifier = Modifier
                                                .padding(start = 5.dp, top = 5.dp)
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(color = Color.Black.copy(alpha = 0.6f))
                                                .height(24.dp)
                                                .wrapContentWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = "Puntuacion",
                                                tint = Color.White,
                                                modifier = Modifier
                                                    .padding(start = 6.dp)
                                                    .size(12.dp)
                                            )
                                            Text(
                                                text = anime.score.toString(),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                textAlign = TextAlign.Start,
                                                modifier = Modifier.wrapContentWidth().padding(end = 6.dp),
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontFamily = RobotoBold
                                            )
                                        }
                                    }


                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = anime.title,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth(),
                                        color = Color.White,
                                        fontFamily = RobotoRegular
                                    )


                                }
                            }
                        }
                    }
                    item {
                        SubTitleIcon("Top scores", Icons.AutoMirrored.Filled.ShowChart)
                    }
                    item {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(topAnimes) { anime ->
                                Column(
                                    modifier = Modifier
                                        .width(130.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    Box() {

                                        AsyncImage(
                                            model = ImageRequest.Builder(LocalContext.current)
                                                .data(anime.image)
                                                .size(Size.ORIGINAL)
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = "Imagen de portada",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .width(130.dp)
                                                .height(190.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .clickable {
                                                    navController.navigate("${AppDestinations.ANIME_DETAIL_ROUTE}/${anime.malId}")
                                                }
                                        )

                                        Row(
                                            modifier = Modifier
                                                .padding(start = 5.dp, top = 5.dp)
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(color = Color.Black.copy(alpha = 0.6f))
                                                .height(24.dp)
                                                .wrapContentWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = "Puntuacion",
                                                tint = Color.White,
                                                modifier = Modifier
                                                    .padding(start = 6.dp)
                                                    .size(12.dp)

                                            )
                                            Text(
                                                text = anime.score.toString(),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                textAlign = TextAlign.Start,
                                                modifier = Modifier.wrapContentWidth().padding(end = 6.dp),
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontFamily = RobotoBold
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = anime.title,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth(),
                                        color = Color.White,
                                        fontFamily = RobotoRegular
                                    )

                                }
                            }
                        }
                    }
                    item {
                        SubTitleIcon("Proxima temporada", Icons.Default.AccessTime)
                    }

                    item {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(animeSeasonUpcoming) { anime ->
                                Column(
                                    modifier = Modifier
                                        .width(130.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box() {

                                        AsyncImage(
                                            model = ImageRequest.Builder(LocalContext.current)
                                                .data(anime.image)
                                                .size(Size.ORIGINAL)
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = "Imagen de portada",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .width(130.dp)
                                                .height(190.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .clickable {
                                                    navController.navigate("${AppDestinations.ANIME_DETAIL_ROUTE}/${anime.malId}")
                                                }
                                        )

                                        Row(
                                            modifier = Modifier
                                                .padding(start = 5.dp, top = 5.dp)
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(color = Color.Black.copy(alpha = 0.6f))
                                                .height(24.dp)
                                                .wrapContentWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = "Puntuacion",
                                                tint = Color.White,
                                                modifier = Modifier
                                                    .padding(start = 6.dp)
                                                    .size(12.dp)
                                            )
                                            Text(
                                                text = anime.score.toString(),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                textAlign = TextAlign.Start,
                                                modifier = Modifier.wrapContentWidth().padding(end = 6.dp),
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontFamily = RobotoBold
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = anime.title,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth(),
                                        color = Color.White,
                                        fontFamily = RobotoRegular
                                    )
                                }
                            }
                        }
                    }
                    item {
                        TitleScreen("Mangas")
                    }
                    item {
                        Text(
                            text = "No tenemos mangas disponibles... en futuras versiones lo implementare :)",
                            color = Color.White,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
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
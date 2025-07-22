package com.example.seijakulist.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Icon
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
import com.example.seijakulist.ui.navigation.AppDestinations


data class BottomNavItem(
    val name: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String
)


@Composable
fun MyAnimesScreen(
    navController: NavController,
    viewModel: AnimeSeasonNowViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = Unit) {
        viewModel.AnimesSeasonNow()
    }

    val animeSeasonNow by viewModel.animeList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()



    val RobotoRegular = FontFamily(
        Font(R.font.roboto_regular)
    )
    val RobotoBold = FontFamily(
        Font(R.font.roboto_bold, FontWeight.Bold)
    )

    val navItems = listOf(
        BottomNavItem(name = "Mis Animes", icon = Icons.Default.Tv, route = ""),
        BottomNavItem(name = "Mis Mangas", icon = Icons.AutoMirrored.Outlined.MenuBook, route = ""),
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
            NavigationBar(
                containerColor = Color(0xFF121212),

                modifier = Modifier.navigationBarsPadding().clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            ) {
                navItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.name,
                                tint = if (currentRoute == item.route) Color.White else Color.White.copy(alpha = 0.6f)
                            )
                        },
                        label = {
                            Text(
                                text = item.name,
                                color = if (currentRoute == item.route) Color.White else Color.White.copy(alpha = 0.6f),
                                fontFamily = RobotoRegular
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = Color.White,
                            unselectedIconColor = Color.White.copy(alpha = 0.6f),
                            unselectedTextColor = Color.White.copy(alpha = 0.6f),
                            indicatorColor = Color(0xFF050505)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF050505))
                .padding(innerPadding)
        ) {

            if (isLoading) {
                Text(
                    text = "Cargando animes...",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp),
                    fontFamily = RobotoRegular
                )
            } else if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else if (animeSeasonNow.isNotEmpty()) {

                LazyColumn() {
                    item {
                        Text(
                            text = "Animes en transmicion:",
                            color = Color.White,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                                .padding(16.dp),
                            fontFamily = RobotoBold
                        )
                    }

                    item {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            items(animeSeasonNow) { anime ->
                                Column(
                                    modifier = Modifier
                                        .width(140.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
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
                                            .width(135.dp)
                                            .height(190.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable() {
                                                navController.navigate("${AppDestinations.ANIME_DETAIL_ROUTE}/${anime.malId}")
                                            }
                                    )
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
                                    Row(modifier = Modifier.padding(start = 3.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = "Puntuacion",
                                            tint = Color.White,
                                            modifier = Modifier
                                                .size(14.dp)
                                                .align(alignment = Alignment.CenterVertically)
                                        )
                                        Text(
                                            text = anime.score.toString(),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            textAlign = TextAlign.Start,
                                            modifier = Modifier.fillMaxWidth(),
                                            color = Color.White,
                                            fontSize = 12.sp
                                        )
                                    }

                                }
                            }
                        }
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
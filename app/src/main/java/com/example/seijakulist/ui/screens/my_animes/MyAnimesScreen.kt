package com.example.seijakulist.ui.screens.my_animes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
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
import coil.compose.rememberAsyncImagePainter
import com.example.seijakulist.R
import com.example.seijakulist.data.local.entities.AnimeEntity
import com.example.seijakulist.ui.navigation.AppDestinations
import com.example.seijakulist.ui.screens.home.BottomNavItem

@Composable
fun MyAnimeListScreen(
    navController: NavController,
    viewModel: MyAnimeListViewModel = hiltViewModel()
) {
    val savedAnimes by viewModel.savedAnimes.collectAsState()

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
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver atras",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Mis animes",
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                    fontFamily = RobotoBold
                )

                IconButton(
                    onClick = {  },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Filtros",
                        tint = Color.White
                    )
                }
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
                            indicatorColor = Color(0xFF080808)
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
            LazyColumn(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)) {
                items(savedAnimes) { anime ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                navController.navigate("${AppDestinations.ANIME_DETAIL_ROUTE}/${anime.malId}")
                            },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))
                    ) {
                        Row(modifier = Modifier.padding(16.dp)) {
                            Image(
                                painter = rememberAsyncImagePainter(anime.imageUrl),
                                contentDescription = anime.title,
                                modifier = Modifier
                                    .size(90.dp)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(
                                    anime.title,
                                    fontFamily = RobotoBold,
                                    color = Color.White,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "Score: ${anime.score}",
                                    fontFamily = RobotoRegular,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

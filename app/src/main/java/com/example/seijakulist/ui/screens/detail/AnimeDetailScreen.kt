package com.example.seijakulist.ui.screens.detail

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.seijakulist.R
import com.example.seijakulist.domain.models.AnimeCharactersDetail
import com.example.seijakulist.ui.navigation.AppDestinations
import com.example.seijakulist.ui.screens.home.BottomNavItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeDetailScreen(
    navController: NavController,
    animeId: Int?,
    animeDetailViewModel: AnimeDetailViewModel = hiltViewModel(),
    animeCharacterDetailViewModel: AnimeCharacterDetailViewModel = hiltViewModel()
) {

    //viewModel de los detalles del anime
    val animeDetail by animeDetailViewModel.animeDetail.collectAsState()
    val isLoading by animeDetailViewModel.isLoading.collectAsState()
    val errorMessage by animeDetailViewModel.errorMessage.collectAsState()

    // viewModel de los personajes del anime
    val animeCharactersDetail by animeCharacterDetailViewModel.characters.collectAsState()
    val characterIsLoading by animeCharacterDetailViewModel.isLoading.collectAsState()
    val characterErrorMessage by animeCharacterDetailViewModel.errorMessage.collectAsState()

    val isAdded by animeDetailViewModel.isAdded.collectAsState()

    val RobotoRegular = FontFamily(
        Font(R.font.roboto_regular)
    )
    val RobotoBold = FontFamily(
        Font(R.font.roboto_bold, FontWeight.Bold)
    )

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.7f else 1f,
        label = "button_scale_animation"
    )

    val navItems = listOf(
        BottomNavItem(name = "Mis Animes", icon = Icons.Default.Tv, route = AppDestinations.MY_ANIMES_ROUTE),
        BottomNavItem(name = "Mis Mangas", icon = Icons.AutoMirrored.Outlined.MenuBook, route = AppDestinations.MY_MANGAS_ROUTE),
        BottomNavItem(name = "Home", icon = Icons.Default.Home, route = AppDestinations.HOME),
        BottomNavItem(name = "Buscar", icon = Icons.Default.Search, route = AppDestinations.SEARCH_ANIME_ROUTE),
        BottomNavItem(name = "Perfil", icon = Icons.Default.AccountCircle, route = AppDestinations.MY_PROFILE_ROUTE)
    )

    val currentRoute = navController.currentBackStackEntry?.destination?.route

    LaunchedEffect(key1 = animeId) {
        if (animeId != null) {
            animeDetailViewModel.loadAnimeDetail(animeId)
            animeCharacterDetailViewModel.loadAnimeCharacters(animeId)
        }
    }

    when {
        isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF050505)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }

        errorMessage != null -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF050505)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorMessage ?: "Error desconocido",
                    color = Color.Red,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        else -> {
            Scaffold(
                topBar = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .height(48.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(color = Color(0xFF121212))
                    ) {
                        Text(
                            text = "Detalle",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontFamily = RobotoBold,
                            modifier = Modifier.align(Alignment.Center)
                        )

                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver atras",
                                tint = Color.White
                            )
                        }
                    }
                },
                containerColor = Color(0xFF050505),
                floatingActionButton = {
                    FloatingActionButton(
                        modifier = Modifier
                            .padding(30.dp)
                            .size(60.dp)
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                            },
                        shape = FloatingActionButtonDefaults.shape,
                        interactionSource = interactionSource,
                        onClick = {
                            animeDetailViewModel.addAnimeToList()
                        },
                        containerColor = Color.White,
                        contentColor = contentColorFor(backgroundColor = Color.White)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Añadir anime",
                            tint = Color.Black
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(color = Color(0xFF050505))
                ) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(350.dp).padding(top = 16.dp)
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(animeDetail?.images)
                                    .size(Size.ORIGINAL)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Imagen de portada",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .blur(radius = 10.dp)
                                    .scale(1.1f),
                                contentScale = ContentScale.Crop,
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color(0xFF050505),
                                                Color.Transparent
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
                                                Color.Transparent,
                                                Color(0xFF050505)
                                            ),
                                            startY = 400f,
                                            endY = Float.POSITIVE_INFINITY
                                        )
                                    )
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.TopStart
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(
                                            start = 16.dp,
                                            top = 32.dp
                                        )
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(animeDetail?.images)
                                            .size(Size.ORIGINAL)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "Imagen de portada",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .width(150.dp)
                                            .height(220.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    )

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Column(
                                        modifier = Modifier.weight(1f).padding(end = 16.dp)
                                    ) {
                                        Text(
                                            text = animeDetail?.title ?: "",
                                            color = Color.White,
                                            fontSize = 24.sp,
                                            fontFamily = RobotoBold,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }

                        }

                    }

                    /*
                    item {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp)
                        ) {
                            items(animeCharactersDetail) { character ->
                                character?.let { it ->
                                    val imageUrl = it.imageCharacter?.jpg?.imageUrl ?: ""

                                    Column(
                                        modifier = Modifier
                                            .width(140.dp)
                                            .padding(8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        AsyncImage(
                                            model = ImageRequest.Builder(LocalContext.current)
                                                .data(imageUrl)
                                                .size(Size.ORIGINAL)
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = "Imagen de personaje",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(120.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .clickable() {
                                                    navController.navigate("${AppDestinations.CHARACTER_DETAIL_ROUTE}/${character.idCharacter}")
                                                }
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = it.nameCharacter?.takeIf { it.isNotBlank() }
                                                ?: "Nombre desconocido",
                                            style = MaterialTheme.typography.titleMedium,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.fillMaxWidth(),
                                            color = Color.White
                                        )
                                        Text(
                                            text = it.role ?: "No especificado",
                                            style = MaterialTheme.typography.bodySmall,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.fillMaxWidth(),
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }

                     */
                }
            }
        }
    }
}

package com.example.seijakulist.ui.screens.search

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.seijakulist.R
import com.example.seijakulist.ui.navigation.AppDestinations

@Composable
fun SearchScreen(
    viewModel: AnimeSearchViewModel = hiltViewModel(),
    navController: NavController
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val animeList by viewModel.animeList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val RobotoRegular = FontFamily(
        Font(R.font.roboto_regular)
    )
    val RobotoBold = FontFamily(
        Font(R.font.roboto_bold, FontWeight.Bold)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF050505))
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
            ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 10.dp),
                value = searchQuery,
                onValueChange = { newText -> viewModel.onSearchQueryChanged(newText) },
                label = { Text("Buscar anime...", color = Color.White) },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF050505),
                    unfocusedContainerColor = Color(0xFF050505),
                    focusedIndicatorColor = Color.White.copy(alpha = 0.5f),
                    unfocusedIndicatorColor = Color.White.copy(alpha = 0.3f),
                    cursorColor = Color.White
                ),
                trailingIcon = {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .clickable { viewModel.searchAnimes() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Icono de búsqueda",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .clickable {  },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Filtros",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.White)
            }
        } else if (errorMessage != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = errorMessage!!, color = Color.Red)
            }
        } else if (animeList.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp)
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = "Resultados para: $searchQuery",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                        color = Color.White,
                        fontFamily = RobotoBold,
                    )
                }
                items(animeList, key = { it.malId }) { anime ->
                    ElevatedCard(
                        elevation = CardDefaults.cardElevation(defaultElevation = 20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(340.dp)
                            .clickable {
                                navController.navigate("${AppDestinations.ANIME_DETAIL_ROUTE}/${anime.malId}")
                            }
                            .clip(RoundedCornerShape(16.dp))
                            .border(
                                width = 1.dp,
                                color = Color(0xFF202020).copy(alpha = 0.5f),
                                shape = RoundedCornerShape(16.dp)
                            ),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            AsyncImage(
                                model = anime.image,
                                contentDescription = "Imagen de portada de ${anime.title}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                                    .clip(
                                        RoundedCornerShape(
                                            bottomStart = 13.dp,
                                            bottomEnd = 13.dp
                                        )
                                    ),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(3.dp))

                            Text(
                                text = anime.title,
                                modifier = Modifier.padding(start = 10.dp, end = 6.dp),
                                fontSize = 16.sp,
                                color = Color.White,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                fontFamily = RobotoBold
                            )
                            Spacer(modifier = Modifier.weight(1f))

                            Row(
                                modifier = Modifier
                                    .padding(start = 10.dp, bottom = 10.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .border(
                                        width = 1.dp,
                                        color = Color.Yellow.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .background(Color.Transparent)
                                    .padding(horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "Puntuación",
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${anime.score}",
                                    fontSize = 11.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        } else if (searchQuery.isNotBlank()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No se encontraron animes para \"$searchQuery\"",
                    color = Color.White
                )
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Ingresa un término de búsqueda para encontrar animes.",
                    color = Color.White,
                    fontSize = 13.sp
                )
            }
        }
    }
}
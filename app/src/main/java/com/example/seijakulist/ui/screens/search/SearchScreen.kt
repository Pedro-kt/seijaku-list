package com.example.seijakulist.ui.screens.search

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF06141B))
            .padding(16.dp)
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            value = searchQuery,
            onValueChange = { newText -> viewModel.onSearchQueryChanged(newText) },
            label = { Text("Buscar anime...", color = Color.White) },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color(0xFF11212D),
                unfocusedContainerColor = Color(0xFF11212D),
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.White
        )
        )

        Spacer(Modifier.height(10.dp))

        Button(
            onClick = { viewModel.searchAnimes() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Text("Buscar", color = Color.Black)
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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp)
            ) {
                items(animeList.chunked(2)) { twoAnimes ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        twoAnimes.forEachIndexed { index, anime ->
                            ElevatedCard(
                                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                                modifier = Modifier
                                    .size(width = 195.dp, height = 300.dp)
                                    .clickable {
                                        navController.navigate(AppDestinations.ANIME_DETAIL_ROUTE)
                                    },
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF))
                            ) {
                                Column(modifier = Modifier.fillMaxSize()) {
                                    AsyncImage(
                                        model = anime.image,
                                        contentDescription = "Imagen de portada de ${anime.title}",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(225.dp)
                                            .clip(RoundedCornerShape(bottomStart = 13.dp, bottomEnd = 13.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                    Text(
                                        text = anime.title,
                                        modifier = Modifier.padding(start = 6.dp),
                                        fontSize = 15.sp,
                                        color = Color.Black,
                                        maxLines = 2
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = "Puntuacion: ${anime.score}",
                                        modifier = Modifier.padding(start = 6.dp, bottom = 4.dp),
                                        fontSize = 10.sp,
                                        color = Color.DarkGray
                                    )
                                }
                            }
                            if (index < twoAnimes.lastIndex) {
                                Spacer(modifier = Modifier.width(10.dp))
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

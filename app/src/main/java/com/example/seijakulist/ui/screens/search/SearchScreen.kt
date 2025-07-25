package com.example.seijakulist.ui.screens.search

import android.app.appsearch.SearchResults
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.seijakulist.R
import com.example.seijakulist.ui.components.ArrowBackTopAppBar
import com.example.seijakulist.ui.components.FilterTopAppBar
import com.example.seijakulist.ui.components.LoadingScreen
import com.example.seijakulist.ui.navigation.AppDestinations
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
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

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val listFilterChip = listOf("Anime", "Manga", "Generos", "Personajes", "Staff", "Estudios")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF050505))
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f),
                value = searchQuery,
                onValueChange = { newText -> viewModel.onSearchQueryChanged(newText) },
                label = {
                    Text("Buscar...", color = Color.White)
                },
                placeholder = { Text(text = "Ingrese término de búsqueda...", color = Color.White.copy(alpha = 0.5f), modifier = Modifier.padding(start = 4.dp)) },
                singleLine = true,
                shape = RoundedCornerShape(50.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF050505),
                    unfocusedContainerColor = Color(0xFF050505),
                    focusedIndicatorColor = Color.White.copy(alpha = 0.5f),
                    unfocusedIndicatorColor = Color.White.copy(alpha = 0.3f),
                    cursorColor = Color.White,
                    focusedLabelColor = Color.White.copy(alpha = 0.7f),
                    unfocusedLabelColor = Color.White.copy(alpha = 0.5f)
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                },
                trailingIcon = {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else if (searchQuery.isNotBlank()) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Eliminar texto de búsqueda",
                            tint = Color.White,
                            modifier = Modifier
                                .padding(end = 6.dp)
                                .size(24.dp)
                                .clickable {
                                    viewModel.onSearchQueryChanged("")
                                    focusManager.clearFocus()
                                }
                        )
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = androidx.compose.ui.text.input.ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.searchAnimes()
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
                ),
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Icono de filtrar por",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp)) // Espacio entre el icono y el texto
            Text(
                text = "Filtrar por:",
                color = Color.White,
                fontSize = 16.sp
            )
        }

        LazyRow(
        ) {
            items(listFilterChip) { genre ->
                FilterChip(
                    selected = true,
                    onClick = {
                    },
                    label = { Text(genre, color = Color.White) },
                    modifier = Modifier.padding(end = 8.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color(0xFF050505),
                        labelColor = Color.White,
                        selectedContainerColor = Color(0xFF121212),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoadingScreen()
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
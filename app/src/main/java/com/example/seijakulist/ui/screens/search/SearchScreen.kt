package com.example.seijakulist.ui.screens.search

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.seijakulist.R
import com.example.seijakulist.ui.components.LoadingScreen
import com.example.seijakulist.ui.components.NoInternetScreen
import com.example.seijakulist.ui.navigation.AppDestinations

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: AnimeSearchViewModel = hiltViewModel()
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

    var selectedFilter by remember { mutableStateOf<String?>(null) }

    var colapsedFilter by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(50.dp)),
                value = searchQuery,
                onValueChange = { newText -> viewModel.onSearchQueryChanged(newText) },
                placeholder = {
                    Text(
                        text = "Explorar...",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(50.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.inversePrimary,
                    focusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                },
                trailingIcon = {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 2.dp
                        )
                    } else if (searchQuery.isNotBlank()) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Eliminar texto de búsqueda",
                            tint = MaterialTheme.colorScheme.onSurface,
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
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        if (searchQuery.isNotBlank()) {
                            viewModel.searchAnimes()
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                    }
                ),
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
        ) {
            items(listFilterChip) { filter ->
                val isSelected = selectedFilter == filter

                ElevatedFilterChip(
                    selected = isSelected,
                    onClick = {
                        selectedFilter = if (isSelected) null else filter
                    },
                    label = {
                        Text(
                            text = filter,
                            color = if (isSelected) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.surfaceContainerHighest
                        }
                    ),
                    shape = RoundedCornerShape(50.dp)
                )
            }
        }


        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoadingScreen()
            }
        } else if (errorMessage != null) {
            NoInternetScreen {}
        } else if (animeList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(
                    top = 8.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
            ) {
                items(animeList, key = { it.malId }) { anime ->
                    var visible by rememberSaveable { mutableStateOf(false) }

                    LaunchedEffect(Unit) {
                        visible = true
                    }

                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(animationSpec = tween(500)) + slideInHorizontally(
                            animationSpec = tween(500)
                        )
                    ) {
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate("${AppDestinations.ANIME_DETAIL_ROUTE}/${anime.malId}")
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                            ),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 16.dp
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box() {
                                        AsyncImage(
                                            model = anime.images,
                                            contentDescription = "Imagen de portada de ${anime.title}",
                                            modifier = Modifier
                                                .height(180.dp)
                                                .width(125.dp)
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(color = MaterialTheme.colorScheme.surfaceContainer),
                                            contentScale = ContentScale.Crop
                                        )
                                        Row(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(topEnd = 16.dp))
                                                .background(color = Color.Black.copy(alpha = 0.8f))
                                                .height(24.dp)
                                                .wrapContentWidth()
                                                .align(Alignment.BottomStart),
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
                                                text = String.format("%.1f", anime.score),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                textAlign = TextAlign.Start,
                                                modifier = Modifier
                                                    .wrapContentWidth()
                                                    .padding(end = 10.dp),
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontFamily = RobotoBold
                                            )
                                        }
                                    }
                                    Column(
                                        modifier = Modifier
                                            .weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Text(
                                            text = anime.title,
                                            modifier = Modifier.padding(horizontal = 16.dp),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontFamily = RobotoRegular,
                                            fontSize = 18.sp
                                        )
                                        Row(
                                            modifier = Modifier.padding(start = 16.dp),
                                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.AccessTime,
                                                contentDescription = "Puntuación",
                                                tint = MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Text(
                                                text = anime.status,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                fontSize = 14.sp,
                                                fontFamily = RobotoRegular
                                            )
                                        }
                                        LazyRow(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            contentPadding = PaddingValues(horizontal = 16.dp),
                                        ) {
                                            items(anime.genres) { genre ->
                                                ElevatedFilterChip(
                                                    selected = false,
                                                    onClick = { },
                                                    label = {
                                                        genre?.name?.let {
                                                            Text(
                                                                it,
                                                                fontSize = 12.sp,
                                                            )
                                                        }
                                                    },
                                                    colors = FilterChipDefaults.filterChipColors(
                                                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                                                        labelColor = MaterialTheme.colorScheme.onSurface,
                                                    )
                                                )
                                            }
                                        }
                                        Text(
                                            text = "Año ${anime.year} • Episodios ${anime.episodes}",
                                            modifier = Modifier.padding(start = 16.dp),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontSize = 12.sp,
                                            fontFamily = RobotoRegular
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = { },
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FavoriteBorder,
                                        contentDescription = "Favorito",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Ingresa un término de búsqueda para encontrar animes.",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 13.sp
                )
            }
        }
    }
}
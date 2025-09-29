package com.example.seijakulist.ui.screens.search

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Indication
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
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
import com.example.seijakulist.ui.components.HorizontalDividerComponent
import com.example.seijakulist.ui.components.LoadingScreen
import com.example.seijakulist.ui.components.NoInternetScreen
import com.example.seijakulist.ui.navigation.AppDestinations
import com.example.seijakulist.ui.theme.RobotoBold
import com.example.seijakulist.ui.theme.RobotoRegular

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: AnimeSearchViewModel = hiltViewModel(),
    listGenres: GenresViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val animeList by viewModel.animeList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val genres by listGenres.genres.collectAsState()
    val isLoadingGenres by listGenres.isLoading.collectAsState()
    val errorMessageGenres by listGenres.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        listGenres.fetchGenres()
    }

    val focusManager = LocalFocusManager.current

    val listFilterChip = listOf("Anime", "Manga", "Generos", "Personajes", "Staff", "Estudios")
    var selectedFilter by remember { mutableStateOf<String?>(null) }

    var colapsedFilter by remember { mutableStateOf(false) }
    var openBottomSheet by remember { mutableStateOf(false) }

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
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(50.dp)),
                value = searchQuery,
                onValueChange = viewModel::onSearchQueryChanged,
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
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
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
                        if (filter == "Generos") {
                            openBottomSheet = true
                        } else {
                            selectedFilter = if (isSelected) null else filter
                        }
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
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        selectedContainerColor = MaterialTheme.colorScheme.primary
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            Color.Transparent
                        }
                    ),
                    shape = RoundedCornerShape(50.dp),
                    trailingIcon = {
                        if (filter == "Generos") {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Ver generos",
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
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
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(animationSpec = tween(300)) + slideInHorizontally(animationSpec = tween(500), initialOffsetX = { -it / 2 })
                    ) {


                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate("${AppDestinations.ANIME_DETAIL_ROUTE}/${anime.malId}")
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box {
                                        AsyncImage(
                                            model = anime.images,
                                            contentDescription = "Imagen de portada de ${anime.title}",
                                            modifier = Modifier
                                                .height(180.dp)
                                                .width(125.dp)
                                                .clip(RoundedCornerShape(16.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                        Row(
                                            modifier = Modifier
                                                .align(Alignment.BottomStart)
                                                .padding(4.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(
                                                    MaterialTheme.colorScheme.scrim.copy(alpha = 0.8f)
                                                )
                                                .padding(horizontal = 6.dp, vertical = 2.dp),
                                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = "Puntuacion",
                                                tint = Color.White,
                                                modifier = Modifier.size(14.dp)

                                            )
                                            Text(
                                                text = String.format("%.1f", anime.score),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                textAlign = TextAlign.Start,
                                                modifier = Modifier
                                                    .wrapContentWidth(align = Alignment.Start),
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontFamily = RobotoBold
                                            )
                                        }
                                    }
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(horizontal = 16.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = anime.title,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            val (statusColor, onStatusColor) = when (anime.status) {
                                                "Currently Airing" -> Color(0xFF4CAF50).copy(alpha = 0.2f) to Color(0xFF4CAF50) // Verde
                                                "Finished Airing" -> Color(0xFF2196F3).copy(alpha = 0.2f) to Color(0xFF2196F3) // Azul
                                                "Not yet aired" -> Color(0xFFFF9800).copy(alpha = 0.2f) to Color(0xFFFF9800) // Naranja
                                                else -> MaterialTheme.colorScheme.surfaceContainer to MaterialTheme.colorScheme.onSurfaceVariant
                                            }

                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = statusColor,
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.AccessTime,
                                                        contentDescription = "Estado",
                                                        tint = onStatusColor,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                    Text(
                                                        text = anime.status,
                                                        style = MaterialTheme.typography.labelMedium,
                                                        color = onStatusColor,
                                                    )
                                                }
                                            }
                                        }

                                        if (anime.genres.isNotEmpty()) {
                                            LazyRow(
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            ) {
                                                items(anime.genres) { genre ->
                                                    genre?.name?.let {
                                                        SuggestionChip(
                                                            onClick = { /* No action */ },
                                                            label = {
                                                                Text(
                                                                    it,
                                                                    style = MaterialTheme.typography.labelSmall
                                                                )
                                                            },
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                        Text(
                                            text = "Año ${anime.year ?: "N/A"} • ${anime.episodes ?: "N/A"} episodios",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                            )
                                    }
                                }
                                IconButton(
                                    onClick = { },
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(4.dp)
                                ) {
                                    Icon( // Cambiado para indicar "guardar" o "añadir a lista"
                                        imageVector = Icons.Default.BookmarkBorder,
                                        contentDescription = "Añadir a la lista",
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
                    text = "Ingresa un término de búsqueda.",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 13.sp
                )
            }
        }
        if (openBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { openBottomSheet = false },
            ) {
                if (isLoadingGenres) {
                    LoadingScreen()
                } else if (errorMessageGenres != null) {
                    Text(text = errorMessageGenres ?: "Error desconocido")
                } else {
                    LazyColumn() {
                        item {
                            Text(
                                text = "Generos",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val firstHalf = genres.take(genres.size / 2)
                                val secondHalf = genres.takeLast(genres.size / 2)

                                Column(modifier = Modifier.weight(1f).padding(end = 4.dp)) {
                                    firstHalf.forEach { genre ->
                                        GenreButton(genre = genre.name, onClick = { /* TODO: Implementar acción de filtro */ }, count = genre.count)
                                    }
                                }
                                Column(modifier = Modifier.weight(1f).padding(start = 4.dp)) {
                                    secondHalf.forEach { genre ->
                                        GenreButton(genre = genre.name, onClick = { /* TODO: Implementar acción de filtro */ }, count = genre.count)
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextButton(
                                    onClick = { openBottomSheet = false },
                                    modifier = Modifier.padding(end = 16.dp, bottom = 16.dp)
                                ) {
                                    Text(
                                        text = "CERRAR",
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Button(
                                    onClick = { openBottomSheet = false },
                                    modifier = Modifier.padding(end = 16.dp, bottom = 16.dp)
                                ) {
                                    Text(
                                        text = "BUSCAR",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun GenreButton(genre: String, onClick: () -> Unit, count: Int) {
    var isSelectedItem by remember { mutableStateOf(false) }

    Button(
        onClick = {
            isSelectedItem = !isSelectedItem
            onClick() // La lambda original se sigue llamando
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelectedItem) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
            contentColor = if (isSelectedItem) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        ),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = genre,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}
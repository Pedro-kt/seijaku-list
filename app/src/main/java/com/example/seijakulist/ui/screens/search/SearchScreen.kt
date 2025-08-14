package com.example.seijakulist.ui.screens.search

import android.app.appsearch.SearchResults
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import coil.compose.AsyncImage
import com.example.seijakulist.R
import com.example.seijakulist.ui.components.ArrowBackTopAppBar
import com.example.seijakulist.ui.components.BottomNavItemScreen
import com.example.seijakulist.ui.components.FilterTopAppBar
import com.example.seijakulist.ui.components.LoadingScreen
import com.example.seijakulist.ui.components.NoInternetScreen
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

    var selectedFilter by remember { mutableStateOf<String?>(null) }

    var colapsedFilter by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomNavItemScreen(navController)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xff121211))
                .padding(padding)
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f),
                    value = searchQuery,
                    onValueChange = { newText -> viewModel.onSearchQueryChanged(newText) },
                    placeholder = {
                        Text(
                            text = "Explorar...",
                            color = Color.White.copy(alpha = 0.5f),
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(50.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color(0xFF202020),
                        unfocusedContainerColor = Color(0xFF202020),
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
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            if (searchQuery.isNotBlank()) {
                                viewModel.searchAnimes()
                                focusManager.clearFocus()
                                keyboardController?.hide()
                                colapsedFilter = false
                            }
                        }
                    ),
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Filtrar por:",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 14.sp
                )
                IconButton(onClick = { colapsedFilter = !colapsedFilter }) {
                    Icon(
                        imageVector = if (colapsedFilter) {
                            Icons.Default.KeyboardArrowDown
                        } else {
                            Icons.AutoMirrored.Filled.KeyboardArrowRight
                        },
                        contentDescription = "Icono de filtrar por",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            if (colapsedFilter) {
                LazyRow(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    items(listFilterChip) { filter ->
                        val isSelected = selectedFilter == filter

                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedFilter = if (isSelected) null else filter
                            },
                            label = {
                                Text(filter)
                            },
                            modifier = Modifier.padding(end = 8.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color(0xFF202020),
                                labelColor = Color.White,
                                selectedContainerColor = Color(0xff7226ff),
                                selectedLabelColor = Color.White
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = Color.White.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(50.dp)
                        )
                    }
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
                        .padding(horizontal = 16.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    contentPadding = PaddingValues(top = 0.dp, bottom = 20.dp)
                ) {
                    item() {
                        if (searchQuery.isNotBlank()) {
                            Text(
                                text = "Resultados de la búsqueda:",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(),
                                color = Color.White.copy(alpha = 0.9f),
                                fontFamily = RobotoRegular,
                            )
                        }
                    }
                    items(animeList, key = { it.malId }) { anime ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { navController.navigate("${AppDestinations.ANIME_DETAIL_ROUTE}/${anime.malId}") },
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF202020)
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
                                    AsyncImage(
                                        model = anime.images,
                                        contentDescription = "Imagen de portada de ${anime.title}",
                                        modifier = Modifier
                                            .height(170.dp)
                                            .width(120.dp)
                                            .clip(RoundedCornerShape(16.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(top = 8.dp, bottom = 8.dp, end = 8.dp)
                                    ) {
                                        Text(
                                            text = anime.title,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            color = Color.White.copy(alpha = 0.9f),
                                            fontFamily = RobotoRegular,
                                            fontSize = 18.sp
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = "Puntuación",
                                                tint = Color(0xFFFDC700),
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Text(
                                                text = anime.score.toString(),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                color = Color(0xFFFDC700),
                                                fontSize = 14.sp,
                                                fontFamily = RobotoRegular
                                            )
                                            Text(
                                                text = "•",
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                color = Color.White,
                                                fontSize = 14.sp,
                                                fontFamily = RobotoRegular
                                            )
                                            Icon(
                                                imageVector = Icons.Default.AccessTime,
                                                contentDescription = "Puntuación",
                                                tint = Color.White,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Text(
                                                text = anime.status,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                color = Color.White,
                                                fontSize = 14.sp,
                                                fontFamily = RobotoRegular
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        LazyRow() {
                                            item {
                                                anime.genres.forEach { genre ->
                                                    ElevatedFilterChip(
                                                        selected = false,
                                                        onClick = { },
                                                        label = {
                                                            genre?.name?.let { Text(it, fontSize = 12.sp) }
                                                        },
                                                        modifier = Modifier.padding(end = 8.dp),
                                                        colors = FilterChipDefaults.filterChipColors(
                                                            containerColor = Color(0xFF404040),
                                                            labelColor = Color.White.copy(alpha = 0.9f),
                                                            selectedContainerColor = Color(0xff404040),
                                                            selectedLabelColor = Color.White
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Año ${anime.year} • Episodios ${anime.episodes}",
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            color = Color.White.copy(alpha = 0.5f),
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
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }
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
}
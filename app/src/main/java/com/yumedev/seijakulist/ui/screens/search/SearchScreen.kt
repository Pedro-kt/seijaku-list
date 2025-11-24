package com.yumedev.seijakulist.ui.screens.search

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Tv
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
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.yumedev.seijakulist.R
import com.yumedev.seijakulist.domain.models.Anime
import com.yumedev.seijakulist.domain.models.AnimeCard
import com.yumedev.seijakulist.domain.models.Genre
import com.yumedev.seijakulist.ui.components.HorizontalDividerComponent
import com.yumedev.seijakulist.ui.components.LoadingScreen
import com.yumedev.seijakulist.ui.components.NoInternetScreen
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.theme.RobotoBold
import com.yumedev.seijakulist.ui.theme.RobotoRegular

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
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val selectedGenreId by viewModel.selectedGenreId.collectAsState()

    val genres by listGenres.genres.collectAsState()
    val isLoadingGenres by listGenres.isLoading.collectAsState()
    val errorMessageGenres by listGenres.errorMessage.collectAsState()

    var openBottomSheet by remember { mutableStateOf(false) }
    var openAddAnimeDialog by remember { mutableStateOf(false) }
    var selectedAnimeToAdd by remember { mutableStateOf<AnimeCard?>(null) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }
    ) {
        SearchHeader(
            searchQuery = searchQuery,
            isLoading = isLoading,
            onSearchQueryChanged = viewModel::onSearchQueryChanged,
            onClearSearch = {
                viewModel.onSearchQueryChanged("")
                focusManager.clearFocus()
                viewModel.performSearchOrFilter()
            },
            onSearch = {
                if (searchQuery.isNotBlank()) {
                    viewModel.performSearchOrFilter()
                    focusManager.clearFocus()
                }
            },
            focusManager = focusManager
        )

        SearchFilters(
            selectedFilter = selectedFilter,
            onFilterSelected = { filter ->
                if (filter == "Generos") {
                    openBottomSheet = true
                } else {
                    viewModel.onFilterSelected(
                        if (selectedFilter == filter) null else filter
                    )
                }
            }
        )

        SearchContent(
            isLoading = isLoading,
            errorMessage = errorMessage,
            animeList = animeList,
            searchQuery = searchQuery,
            navController = navController,
            onAddToListClick = { anime ->
                selectedAnimeToAdd = anime
                openAddAnimeDialog = true
            }
        )

        if (openBottomSheet) {
            GenresBottomSheet(
                isLoadingGenres = isLoadingGenres,
                errorMessageGenres = errorMessageGenres,
                genres = genres,
                selectedGenreId = selectedGenreId,
                onGenreSelected = viewModel::onGenreSelected,
                onDismiss = { openBottomSheet = false },
                onSearch = {
                    if (selectedGenreId != null) {
                        viewModel.onFilterSelected("Generos")
                        viewModel.performSearchOrFilter()
                    } else {
                        viewModel.onFilterSelected(null)
                    }
                    openBottomSheet = false
                }
            )
        }

        if (openAddAnimeDialog && selectedAnimeToAdd != null) {
            AddAnimeDialog(
                anime = selectedAnimeToAdd!!,
                onDismiss = {
                    openAddAnimeDialog = false
                    selectedAnimeToAdd = null
                },
                onConfirm = { userScore, userStatus, userOpinion ->
                    viewModel.addAnimeToList(
                        anime = selectedAnimeToAdd!!,
                        userScore = userScore,
                        userStatus = userStatus,
                        userOpinion = userOpinion,
                        onSuccess = {
                            openAddAnimeDialog = false
                            selectedAnimeToAdd = null
                        }
                    )
                }
            )
        }
    }
}

@Composable
private fun SearchHeader(
    searchQuery: String,
    isLoading: Boolean,
    onSearchQueryChanged: (String) -> Unit,
    onClearSearch: () -> Unit,
    onSearch: () -> Unit,
    focusManager: FocusManager
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            value = searchQuery,
            onValueChange = onSearchQueryChanged,
            placeholder = {
                Text(
                    text = "Anime, manga, personajes...",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    fontFamily = RobotoRegular
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp)
                )
            },
            trailingIcon = {
                when {
                    isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 2.dp
                        )
                    }
                    searchQuery.isNotBlank() -> {
                        IconButton(
                            onClick = onClearSearch,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Limpiar búsqueda",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { onSearch() }
            )
        )
    }
}

@Composable
private fun SearchFilters(
    selectedFilter: String?,
    onFilterSelected: (String) -> Unit
) {
    val listFilterChip = listOf(
        "Anime" to Icons.Default.Tv,
        "Manga" to Icons.AutoMirrored.Filled.MenuBook,
        "Generos" to Icons.Default.Category,
        "Personajes" to Icons.Default.Person,
        "Staff" to Icons.Default.Groups,
        "Estudios" to Icons.Default.Business
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(listFilterChip) { (filter, icon) ->
            val isSelected = selectedFilter == filter

            FilterChip(
                selected = isSelected,
                onClick = { onFilterSelected(filter) },
                label = {
                    Text(
                        text = filter,
                        fontFamily = if (isSelected) RobotoBold else RobotoRegular,
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = if (filter == "Generos") {
                    {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Ver géneros",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                } else null,
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    labelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    iconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = Color.Transparent,
                    selectedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    borderWidth = 0.dp,
                    selectedBorderWidth = 1.dp
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

@Composable
private fun SearchContent(
    isLoading: Boolean,
    errorMessage: String?,
    animeList: List<AnimeCard>,
    searchQuery: String,
    navController: NavController,
    onAddToListClick: (AnimeCard) -> Unit = {}
) {
    when {
        isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingScreen()
            }
        }
        errorMessage != null -> {
            NoInternetScreen(onRetryClick = {})
        }
        animeList.isNotEmpty() -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
            ) {
                item {
                    Text(
                        text = "${animeList.size} resultados encontrados",
                        fontFamily = RobotoRegular,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                items(animeList, key = { it.malId }) { anime ->
                    AnimeCardItem(
                        navController = navController,
                        anime = anime,
                        onAddToListClick = onAddToListClick
                    )
                }
            }
        }
        else -> {
            EmptySearchState(searchQuery = searchQuery)
        }
    }
}

@Composable
private fun EmptySearchState(searchQuery: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (searchQuery.isBlank()) {
                "Comienza tu búsqueda"
            } else {
                "No se encontraron resultados"
            },
            fontFamily = RobotoBold,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (searchQuery.isBlank()) {
                "Explora anime, manga, personajes y más"
            } else {
                "Intenta con otros términos de búsqueda"
            },
            fontFamily = RobotoRegular,
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            lineHeight = 20.sp
        )
    }
}

@Composable
fun AnimeCardItem(
    navController: NavController,
    anime: AnimeCard,
    onAddToListClick: (AnimeCard) -> Unit = {}
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "card_scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = {
                        navController.navigate("${AppDestinations.ANIME_DETAIL_ROUTE}/${anime.malId}")
                    }
                )
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            // Imagen con efectos mejorados
            Box(
                modifier = Modifier
                    .width(130.dp)
                    .fillMaxHeight()
            ) {
                // Imagen principal
                AsyncImage(
                    model = anime.images,
                    contentDescription = "Portada de ${anime.title}",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop
                )

                // Score badge flotante mejorado
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF1E1E1E).copy(alpha = 0.88f),
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = String.format("%.1f", anime.score),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontFamily = RobotoBold,
                            letterSpacing = 0.2.sp
                        )
                    }
                }
            }

            // Contenido con mejor espaciado
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(start = 14.dp, end = 14.dp, top = 14.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Sección superior: Título, Status y Géneros
                Column(
                    verticalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    Text(
                        text = anime.title,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp,
                        fontFamily = RobotoBold,
                        lineHeight = 20.sp,
                        letterSpacing = 0.1.sp
                    )

                    // Status badge moderno
                    StatusBadgeModern(status = anime.status)

                    // Géneros con scroll horizontal
                    if (anime.genres.isNotEmpty()) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(anime.genres.take(3)) { genre ->
                                genre?.name?.let {
                                    GenreChip(genreName = it)
                                }
                            }
                        }
                    }
                }

                // Sección inferior: Info y acciones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Info compacta con colores del tema
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        InfoRow(
                            icon = Icons.Default.CalendarToday,
                            text = anime.year,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        InfoRow(
                            icon = Icons.Default.PlayCircleOutline,
                            text = "${anime.episodes} eps",
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    // Botón de bookmark mejorado
                    Surface(
                        onClick = { onAddToListClick(anime) },
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.BookmarkBorder,
                                contentDescription = "Añadir a lista",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusBadgeModern(status: String) {
    val (statusColor, statusText, dotColor) = when (status) {
        "Currently Airing" -> Triple(
            Color(0xFF10B981),
            "En emisión",
            Color(0xFF34D399)
        )
        "Finished Airing" -> Triple(
            Color(0xFF3B82F6),
            "Finalizado",
            Color(0xFF60A5FA)
        )
        "Not yet aired" -> Triple(
            Color(0xFFF59E0B),
            "Próximamente",
            Color(0xFFFBBF24)
        )
        else -> Triple(
            Color.Gray,
            status,
            Color.LightGray
        )
    }

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = statusColor.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, statusColor.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Punto animado
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .background(dotColor, CircleShape)
            )

            Text(
                text = statusText,
                style = MaterialTheme.typography.labelSmall,
                color = statusColor,
                fontFamily = RobotoBold,
                fontSize = 12.sp,
                letterSpacing = 0.3.sp
            )
        }
    }
}

@Composable
private fun GenreChip(genreName: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
    ) {
        Text(
            text = genreName,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            fontFamily = RobotoRegular,
            fontSize = 11.sp,
            letterSpacing = 0.2.sp
        )
    }
}

@Composable
private fun InfoRow(
    icon: ImageVector,
    text: String,
    color: Color
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(15.dp),
            tint = color.copy(alpha = 0.85f)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
            fontSize = 13.sp,
            fontFamily = RobotoRegular,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.1.sp
        )
    }
}

// VERSIÓN ALTERNATIVA: Aún más minimalista y elegante
@Composable
fun AnimeCardItemMinimal(navController: NavController, anime: AnimeCard) {
    var isPressed by remember { mutableStateOf(false) }

    val elevation by animateDpAsState(
        targetValue = if (isPressed) 2.dp else 6.dp,
        label = "elevation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = {
                        navController.navigate("${AppDestinations.ANIME_DETAIL_ROUTE}/${anime.malId}")
                    }
                )
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
        ) {
            // Imagen limpia
            AsyncImage(
                model = anime.images,
                contentDescription = "Portada de ${anime.title}",
                modifier = Modifier
                    .width(130.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 18.dp, bottomStart = 18.dp)),
                contentScale = ContentScale.Crop
            )

            // Contenido espaciado
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(18.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Título con score inline
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = anime.title,
                            modifier = Modifier.weight(1f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 17.sp,
                            fontFamily = RobotoBold,
                            lineHeight = 20.sp
                        )

                        // Score compacto
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFBBF24),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = String.format("%.1f", anime.score),
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 13.sp,
                                fontFamily = RobotoBold
                            )
                        }
                    }

                    StatusBadgeMinimal(status = anime.status)

                    // Géneros en una línea
                    if (anime.genres.isNotEmpty()) {
                        Text(
                            text = anime.genres.take(3).mapNotNull { it?.name }.joinToString(" • "),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            fontSize = 11.sp
                        )
                    }
                }

                // Footer compacto
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${anime.year} • ${anime.episodes} eps",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        fontSize = 11.sp
                    )

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusBadgeMinimal(status: String) {
    val (statusColor, statusText) = when (status) {
        "Currently Airing" -> Color(0xFF10B981) to "En emisión"
        "Finished Airing" -> Color(0xFF3B82F6) to "Finalizado"
        "Not yet aired" -> Color(0xFFF59E0B) to "Próximamente"
        else -> Color.Gray to status
    }

    Text(
        text = "● $statusText",
        style = MaterialTheme.typography.labelSmall,
        color = statusColor,
        fontFamily = RobotoBold,
        fontSize = 12.sp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenresBottomSheet(
    isLoadingGenres: Boolean,
    errorMessageGenres: String?,
    genres: List<Genre>,
    selectedGenreId: String?,
    onGenreSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    onSearch: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Seleccionar género",
                    fontFamily = RobotoBold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar"
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            when {
                isLoadingGenres -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingScreen()
                    }
                }
                errorMessageGenres != null -> {
                    Text(
                        text = errorMessageGenres,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.height(400.dp)
                    ) {
                        items(genres) { genre ->
                            GenreChip(
                                genre = genre,
                                isSelected = selectedGenreId == genre.malId.toString(),
                                onClick = { onGenreSelected(genre.malId.toString()) }
                            )
                        }
                    }
                }
            }

            // Action buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar", fontFamily = RobotoRegular)
                }
                Button(
                    onClick = onSearch,
                    modifier = Modifier.weight(1f),
                    enabled = selectedGenreId != null
                ) {
                    Text("Aplicar filtro", fontFamily = RobotoBold)
                }
            }
        }
    }
}

@Composable
private fun GenreChip(
    genre: Genre,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainerHigh
        },
        border = if (isSelected) {
            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        } else null,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = genre.name,
                fontFamily = if (isSelected) RobotoBold else RobotoRegular,
                fontSize = 14.sp,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(
                    text = genre.count.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddAnimeDialog(
    anime: AnimeCard,
    onDismiss: () -> Unit,
    onConfirm: (userScore: Float, userStatus: String, userOpinion: String) -> Unit
) {
    var selectedStatus by remember { mutableStateOf<String?>(null) }
    var userRating by remember { mutableStateOf(0.0f) }
    var userOpinion by remember { mutableStateOf("") }
    var expandedStatus by remember { mutableStateOf(false) }

    val statusAnime = listOf("Viendo", "Completado", "Pendiente", "Abandonado", "Planeado")
    val statusColors = mapOf(
        "Viendo" to Color(0xFF4CAF50),
        "Completado" to Color(0xFF2196F3),
        "Pendiente" to Color(0xFFFF9800),
        "Abandonado" to Color(0xFFF44336),
        "Planeado" to Color(0xFF78909C)
    )

    val isFormValid = selectedStatus != null

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header mejorado
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Añadir a mi lista",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 26.sp,
                        fontFamily = RobotoBold
                    )
                }
            }

            // Card: Estado
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .animateContentSize(),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 2.dp
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Estado",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 20.sp,
                                fontFamily = RobotoBold
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            if (selectedStatus != null) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            statusColors[selectedStatus]!!,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "✓",
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = { expandedStatus = !expandedStatus },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedStatus != null) statusColors[selectedStatus]!! else MaterialTheme.colorScheme.surface,
                                contentColor = if (selectedStatus != null) Color.Black else MaterialTheme.colorScheme.onSurface
                            ),
                            shape = RoundedCornerShape(12.dp),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 2.dp,
                                pressedElevation = 6.dp
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = selectedStatus ?: "Seleccionar estado",
                                    modifier = Modifier.weight(1f),
                                    textAlign = if (selectedStatus != null) TextAlign.Center else TextAlign.Start,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                val rotation: Float by animateFloatAsState(
                                    targetValue = if (expandedStatus) 180f else 0f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    ),
                                    label = "Arrow Rotation"
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(28.dp)
                                        .graphicsLayer { rotationZ = rotation }
                                )
                            }
                        }

                        AnimatedVisibility(
                            visible = expandedStatus,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier
                                    .padding(top = 12.dp)
                                    .height(200.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(
                                    items = statusAnime,
                                    key = { it }
                                ) { status ->
                                    val isSelected = selectedStatus == status
                                    val scale by animateFloatAsState(
                                        targetValue = if (isSelected) 1.05f else 1f,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy
                                        ),
                                        label = "Card Scale"
                                    )

                                    Card(
                                        onClick = {
                                            if (selectedStatus == status) {
                                                selectedStatus = null
                                            } else {
                                                selectedStatus = status
                                            }
                                            expandedStatus = false
                                        },
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .graphicsLayer {
                                                scaleX = scale
                                                scaleY = scale
                                            },
                                        colors = CardDefaults.cardColors(
                                            containerColor = statusColors[status] ?: Color.Gray
                                        ),
                                        elevation = CardDefaults.cardElevation(
                                            defaultElevation = if (isSelected) 8.dp else 2.dp
                                        ),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(12.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = status,
                                                color = Color.Black,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Card: Calificación
            item {
                AnimatedVisibility(
                    visible = selectedStatus != null,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                        ),
                        elevation = CardDefaults.elevatedCardElevation(
                            defaultElevation = 4.dp
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Calificación",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 20.sp,
                                    fontFamily = RobotoBold
                                )
                            }

                            if (selectedStatus != "Planeado") {
                                RatingBar(
                                    rating = userRating,
                                    onRatingChange = { userRating = it }
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            MaterialTheme.colorScheme.surface,
                                            RoundedCornerShape(12.dp)
                                        )
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "No puedes puntuar un anime planeado",
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                        textAlign = TextAlign.Center,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Card: Opinión
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = 4.dp
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Opinión",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 20.sp,
                                fontFamily = RobotoBold
                            )
                        }

                        OutlinedTextField(
                            value = userOpinion,
                            onValueChange = { userOpinion = it },
                            placeholder = {
                                Text(text = "Comparte tu opinión sobre este anime...")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                cursorColor = MaterialTheme.colorScheme.primary,
                                focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }

            // Botón Guardar mejorado
            item {
                Button(
                    onClick = {
                        if (isFormValid) {
                            val scoreToPass = if (selectedStatus == "Planeado") {
                                0.0f
                            } else {
                                userRating
                            }
                            onConfirm(scoreToPass, selectedStatus!!, userOpinion)
                        }
                    },
                    enabled = isFormValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    ),
                    shape = RoundedCornerShape(14.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 6.dp,
                        pressedElevation = 12.dp,
                        disabledElevation = 0.dp
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Guardar en mi lista",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Float,
    onRatingChange: (Float) -> Unit,
    stars: Int = 10,
    starColor: Color = Color(0xFFFFD700)
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            for (i in 1..stars) {
                val starValue = i.toFloat()
                val isFilled = starValue <= rating
                val isHalfFilled = (starValue - 0.5f) <= rating && !isFilled

                val imageVector = when {
                    isFilled -> Icons.Default.Star
                    isHalfFilled -> Icons.AutoMirrored.Filled.StarHalf
                    else -> Icons.Default.StarBorder
                }

                val tint =
                    if (isFilled || isHalfFilled) starColor else MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.5f
                    )
                val size by animateFloatAsState(
                    targetValue = if (isFilled || isHalfFilled) 1.2f else 1.0f,
                    label = "starSizeAnimation"
                )

                Icon(
                    imageVector = imageVector,
                    contentDescription = "Puntuación de $starValue estrellas",
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = size
                            scaleY = size
                        }
                        .size(32.dp)
                        .clickable {
                            val newRating = if (rating == starValue) {
                                starValue - 0.5f
                            } else if (rating == starValue - 0.5f) {
                                0f
                            } else {
                                starValue
                            }
                            onRatingChange(newRating)
                        },
                    tint = tint
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = if (rating % 1 == 0f) {
                "Tu calificación: %d / %d".format(rating.toInt(), stars)
            } else {
                "Tu calificación: %.1f / %d".format(rating, stars)
            },
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = RobotoBold,
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.surfaceContainerHigh,
                    RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}
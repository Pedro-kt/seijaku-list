package com.yumedev.seijakulist.ui.screens.search

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Upcoming
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yumedev.seijakulist.domain.models.AnimeCard
import com.yumedev.seijakulist.domain.models.Genre
import com.yumedev.seijakulist.ui.components.LoadingScreen
import com.yumedev.seijakulist.ui.components.NoInternetScreen
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import com.yumedev.seijakulist.ui.theme.adp
import com.yumedev.seijakulist.ui.theme.asp
import kotlin.collections.listOf

// Altura aproximada de la barra colapsada
private val SearchBarCollapsedHeight = 72.dp

// ─── Paleta de gradientes para las tarjetas de género ───────────────────────
private val genreCardColors = listOf(
    Color(0xFF6C63FF) to Color(0xFF9C88FF),
    Color(0xFFFF6B6B) to Color(0xFFFF8E53),
    Color(0xFF0CB2AF) to Color(0xFF2EBFBC),
    Color(0xFFFF9A00) to Color(0xFFFFBE0B),
    Color(0xFF06C88A) to Color(0xFF2DC653),
    Color(0xFFE91E8C) to Color(0xFFFF4B6E),
    Color(0xFF7B2FBE) to Color(0xFFA855F7),
    Color(0xFF2563EB) to Color(0xFF60A5FA),
    Color(0xFFEA580C) to Color(0xFFFB923C),
    Color(0xFF16A34A) to Color(0xFF4ADE80),
    Color(0xFF0891B2) to Color(0xFF22D3EE),
    Color(0xFFDC2626) to Color(0xFFF87171),
    Color(0xFF0D9488) to Color(0xFF2DD4BF),
    Color(0xFF7C3AED) to Color(0xFFC084FC),
)

// ─── Modelo para quick-filter chips ─────────────────────────────────────────
data class QuickFilter(
    val label: String,
    val icon: ImageVector,
    val filterKey: String            // valor que se pasa al ViewModel
)

// ─── Quick filters: atajos de un solo toque ─────────────────────────────────
private val quickFilters = listOf(
    QuickFilter("En emisión", Icons.Default.Tv, "airing"),
    QuickFilter("Tendencias", Icons.Default.TrendingUp, "trending"),
    QuickFilter("Top valorados", Icons.Default.Star, "top"),
    QuickFilter("Próximos", Icons.Default.Upcoming, "upcoming"),
    QuickFilter("Temporada", Icons.Default.WbSunny, "season"),
    QuickFilter("Novedades", Icons.Default.NewReleases, "new"),
    QuickFilter("Populares", Icons.Default.LocalFireDepartment, "popular"),
)

// ─── Tipos de contenido (filtros de categoría) ──────────────────────────────
private val contentTypeFilters = listOf(
    "Anime" to Icons.Default.Tv,
    "Manga" to Icons.AutoMirrored.Filled.MenuBook,
    "Géneros" to Icons.Default.Category,
    "Personajes" to Icons.Default.Person,
    "Staff" to Icons.Default.Groups,
    "Estudios" to Icons.Default.Business,
)

// ─── Formatos de anime ───────────────────────────────────────────────────────
private val formatFilters = listOf("TV", "Película", "OVA", "ONA", "Especial", "Música")

// ─────────────────────────────────────────────────────────────────────────────
// PANTALLA PRINCIPAL
// ─────────────────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: AnimeSearchViewModel = hiltViewModel(),
    listGenres: GenresViewModel = hiltViewModel(),
    onSearchExpandedChange: (Boolean) -> Unit = {}
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val animeList by viewModel.animeList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val selectedGenreId by viewModel.selectedGenreId.collectAsState()
    val selectedQuick by viewModel.selectedQuickFilter.collectAsState() // nuevo estado en VM
    val selectedFormat by viewModel.selectedFormat.collectAsState() // formato seleccionado
    val recentSearches by viewModel.recentSearches.collectAsState() // búsquedas recientes

    val genres by listGenres.genres.collectAsState()
    val isLoadingGenres by listGenres.isLoading.collectAsState()
    val errorMessageGenres by listGenres.errorMessage.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    var openBottomSheet by remember { mutableStateOf(false) }

    // Notificar cambio de estado de expansión
    LaunchedEffect(expanded) {
        onSearchExpandedChange(expanded)
    }

    BackHandler(enabled = expanded) { expanded = false }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // ── Vista de descubrimiento (barra colapsada) ─────────────────────
        if (!expanded) {
            SearchDiscoveryView(
                selectedFilter = selectedFilter,
                selectedQuick = selectedQuick,
                selectedFormat = selectedFormat,
                genres = genres,
                isLoadingGenres = isLoadingGenres,
                onFilterSelected = { filter ->
                    if (filter == "Géneros") openBottomSheet = true
                    else {
                        viewModel.onFilterSelected(if (selectedFilter == filter) null else filter)
                    }
                },
                onQuickFilterTap = { qf ->
                    // Selecciona/deselecciona el quick filter y lanza búsqueda
                    viewModel.onQuickFilterSelected(if (selectedQuick == qf.filterKey) null else qf.filterKey)
                    viewModel.performSearchOrFilter()
                    expanded = true
                },
                onFormatSelected = { format ->
                    viewModel.onFormatSelected(if (selectedFormat == format) null else format)
                },
                onGenreDirectTap = { genreId ->
                    viewModel.onGenreSelected(genreId)
                    viewModel.onFilterSelected("Géneros")
                    viewModel.performSearchOrFilter()
                    expanded = true
                }
            )
        }

        // ── SearchBar Material 3 ──────────────────────────────────────────
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .then(
                    if (expanded) Modifier
                    else Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ),
            colors = SearchBarDefaults.colors(
                containerColor = if (expanded) MaterialTheme.colorScheme.background
                else MaterialTheme.colorScheme.surfaceContainerHigh,
                dividerColor = if (expanded) Color.Transparent
                else MaterialTheme.colorScheme.outlineVariant
            ),
            inputField = {
                SearchBarDefaults.InputField(
                    query = searchQuery,
                    onQueryChange = viewModel::onSearchQueryChanged,
                    onSearch = {
                        if (searchQuery.isNotBlank()) viewModel.performSearchOrFilter()
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    colors = SearchBarDefaults.inputFieldColors(
                        focusedContainerColor = if (expanded) MaterialTheme.colorScheme.background
                        else MaterialTheme.colorScheme.surfaceContainerHigh,
                        unfocusedContainerColor = if (expanded) MaterialTheme.colorScheme.background
                        else MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    placeholder = {
                        Text(
                            text = "Anime, manga, personajes...",
                            fontFamily = PoppinsRegular,
                            fontSize = 14.asp(),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    },
                    leadingIcon = {
                        if (expanded) {
                            IconButton(onClick = { expanded = false }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Volver",
                                    modifier = Modifier.size(22.adp()),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        } else {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                modifier = Modifier.size(22.adp()),
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    },
                    trailingIcon = {
                        when {
                            searchQuery.isNotBlank() -> IconButton(
                                onClick = { viewModel.clearSearch() }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Limpiar búsqueda",
                                    modifier = Modifier.size(20.adp()),
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
            windowInsets = WindowInsets(0.dp)
        ) {
            SearchContent(
                isLoading = isLoading,
                isLoadingMore = isLoadingMore,
                errorMessage = errorMessage,
                animeList = animeList,
                searchQuery = searchQuery,
                navController = navController,
                selectedFilter = selectedFilter,
                onFilterSelected = { filter ->
                    if (filter == "Géneros") openBottomSheet = true
                    else viewModel.onFilterSelected(if (selectedFilter == filter) null else filter)
                },
                onLoadMore = viewModel::loadMoreAnimes,
                recentSearches = recentSearches,
                onRecentSearchClick = viewModel::onRecentSearchClicked
            )
        }
    }

    // ── Bottom sheet de géneros ───────────────────────────────────────────
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
                    viewModel.onFilterSelected("Géneros")
                    viewModel.performSearchOrFilter()
                    expanded = true
                } else {
                    viewModel.onFilterSelected(null)
                }
                openBottomSheet = false
            }
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// VISTA DE DESCUBRIMIENTO (barra colapsada)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun SearchDiscoveryView(
    selectedFilter: String?,
    selectedQuick: String?,
    selectedFormat: String?,
    genres: List<Genre>,
    isLoadingGenres: Boolean,
    onFilterSelected: (String) -> Unit,
    onQuickFilterTap: (QuickFilter) -> Unit,
    onFormatSelected: (String) -> Unit,
    onGenreDirectTap: (genreId: String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = SearchBarCollapsedHeight)
            .verticalScroll(rememberScrollState())
    ) {
        // ── Título ────────────────────────────────────────────────────────
        SectionHeader(title = "Explorar")

        // ── Tipos de contenido ────────────────────────────────────────────
        ContentTypeFilters(
            selectedFilter = selectedFilter,
            onFilterSelected = onFilterSelected
        )

        // ═══════════════════════════════════════════════════════════════════
        // SECCIÓN 1 — Quick Filters (atajos de un toque, estilo Play Store)
        // ═══════════════════════════════════════════════════════════════════
        AnimatedVisibility(
            visible = selectedFilter == "Anime",
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                SectionHeader(title = "Acceso rápido")

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    items(quickFilters) { qf ->
                        val isActive = selectedQuick == qf.filterKey
                        QuickFilterChip(
                            label = qf.label,
                            icon = qf.icon,
                            isActive = isActive,
                            onClick = { onQuickFilterTap(qf) }
                        )
                    }
                }
            }
        }

        // ═══════════════════════════════════════════════════════════════════
        // SECCIÓN 2 — Formatos de anime
        // ═══════════════════════════════════════════════════════════════════
        AnimatedVisibility(
            visible = selectedFilter == "Anime",
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                SectionHeader(title = "Formato")

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(formatFilters) { format ->
                        FormatChip(
                            label = format,
                            isActive = selectedFormat == format,
                            onClick = { onFormatSelected(format) }
                        )
                    }
                }
            }
        }

        // ═══════════════════════════════════════════════════════════════════
        // SECCIÓN 3 — Géneros populares (grid 2 cols con gradiente)
        // ═══════════════════════════════════════════════════════════════════
        Spacer(modifier = Modifier.height(12.dp))

        if (isLoadingGenres) {
            GenreSkeletonGrid()
        } else if (genres.isNotEmpty()) {
            SectionHeader(title = "Géneros populares")

            val displayGenres = genres.take(14)
            val chunked = displayGenres.chunked(2)

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                chunked.forEachIndexed { rowIndex, rowGenres ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        rowGenres.forEachIndexed { colIndex, genre ->
                            val colorIndex = (rowIndex * 2 + colIndex) % genreCardColors.size
                            val (startColor, endColor) = genreCardColors[colorIndex]
                            DiscoveryGenreCard(
                                genre = genre,
                                startColor = startColor,
                                endColor = endColor,
                                onClick = { onGenreDirectTap(genre.malId.toString()) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (rowGenres.size == 1) Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(120.adp()))
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// COMPONENTES DE CHIP
// ─────────────────────────────────────────────────────────────────────────────

/** Chip de acceso rápido — toque único lanza búsqueda preconfigurada */
@Composable
private fun QuickFilterChip(
    label: String,
    icon: ImageVector,
    isActive: Boolean,
    onClick: () -> Unit
) {

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (isActive) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceContainerHigh,
        border = if (isActive)
            BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))
        else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.adp()),
                tint = if (isActive) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = label,
                fontFamily = if (isActive) PoppinsBold else PoppinsRegular,
                fontSize = 13.asp(),
                color = if (isActive) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        }
    }
}

/** Chip de formato — TV, Movie, OVA, ONA, Especial */
@Composable
private fun FormatChip(label: String, isActive: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (isActive) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceContainerHigh,
        border = if (isActive)
            BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))
        else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Text(
            text = label,
            fontFamily = if (isActive) PoppinsBold else PoppinsRegular,
            fontSize = 13.asp(),
            color = if (isActive) MaterialTheme.colorScheme.onPrimaryContainer
            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)
        )
    }
}

/** Header reutilizable estilo Google Play */
@Composable
private fun SectionHeader(
    title: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontFamily = PoppinsBold,
            fontSize = 15.asp(),
            color = MaterialTheme.colorScheme.onBackground
        )
        if (actionLabel != null && onAction != null) {
            Text(
                text = actionLabel,
                fontFamily = PoppinsRegular,
                fontSize = 13.asp(),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onAction() }
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// FILTROS DE TIPO DE CONTENIDO (chips horizontales)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun ContentTypeFilters(
    selectedFilter: String?,
    onFilterSelected: (String) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(contentTypeFilters) { (filter, icon) ->
            QuickFilterChip(
                label = filter,
                icon = icon,
                isActive = selectedFilter == filter,
                onClick = { onFilterSelected(filter) }
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// TARJETA DE GÉNERO (grid en descubrimiento)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun DiscoveryGenreCard(
    genre: Genre,
    startColor: Color,
    endColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        modifier = modifier.height(80.adp())
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(startColor, endColor),
                        start = Offset.Zero,
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
                .padding(12.dp)
        ) {
            Text(
                text = genre.name,
                color = Color.White,
                fontFamily = PoppinsBold,
                fontSize = 14.asp(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.align(Alignment.TopStart)
            )
            Text(
                text = "${genre.count}",
                color = Color.White.copy(alpha = 0.65f),
                fontFamily = PoppinsRegular,
                fontSize = 11.asp(),
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}

/** Skeleton 2x4 mientras cargan los géneros */
@Composable
private fun GenreSkeletonGrid() {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(4) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(2) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(80.adp())
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// CONTENIDO DE BÚSQUEDA (panel expandido)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun SearchContent(
    isLoading: Boolean,
    isLoadingMore: Boolean,
    errorMessage: String?,
    animeList: List<AnimeCard>,
    searchQuery: String,
    navController: NavController,
    selectedFilter: String?,
    onFilterSelected: (String) -> Unit,
    onLoadMore: () -> Unit = {},
    recentSearches: List<String> = emptyList(),
    onRecentSearchClick: (String) -> Unit = {}
) {
    when {
        isLoading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { LoadingScreen() }

        errorMessage != null -> NoInternetScreen(onRetryClick = {})

        animeList.isNotEmpty() -> LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            item {
                ContentTypeFilters(
                    selectedFilter = selectedFilter,
                    onFilterSelected = onFilterSelected
                )
            }
            item {
                Text(
                    text = "${animeList.size}+ resultados encontrados",
                    fontFamily = PoppinsRegular,
                    fontSize = 14.asp(),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            items(animeList, key = { it.malId }) { anime ->
                AnimeCardItem(navController = navController, anime = anime)
                // Carga anticipada: 2 elementos antes del final
                if (anime == animeList.getOrNull(animeList.size - 2) && !isLoadingMore) {
                    LaunchedEffect(key1 = anime.malId) { onLoadMore() }
                }
            }
            if (isLoadingMore) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.adp()),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 3.dp
                        )
                    }
                }
            }
            item { Spacer(Modifier.height(100.adp())) }
        }

        else -> {
            EmptySearchState(
                selectedFilter = selectedFilter,
                onFilterSelected = onFilterSelected,
                recentSearches = recentSearches,
                onSearchClick = onRecentSearchClick
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// ESTADO VACÍO
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun EmptySearchState(
    recentSearches: List<String> = listOf("TV", "Movie", "OVA", "ONA", "Especial"),
    trendingSearches: List<String> = listOf("Naruto", "Jujutsu Kaisen", "One Piece"),
    onSearchClick: (String) -> Unit = {},
    selectedFilter: String? = null,
    onFilterSelected: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
            ContentTypeFilters(
                selectedFilter = selectedFilter,
                onFilterSelected = onFilterSelected
            )

        // -------- RECENT SEARCHES --------
        if (recentSearches.isNotEmpty()) {
            SectionHeader(title = "Búsquedas recientes")

            recentSearches.forEach { query ->
                SearchItemRow(
                    text = query,
                    icon = Icons.Default.History,
                    onClick = { onSearchClick(query) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // -------- TRENDING --------
        if (trendingSearches.isNotEmpty()) {
            SectionHeader(title = "Tendencias")

            trendingSearches.forEachIndexed { index, query ->
                SearchItemRow(
                    text = "${index + 1}. $query",
                    icon = Icons.Default.LocalFireDepartment,
                    highlight = true,
                    onClick = { onSearchClick(query) }
                )
            }
        }
    }
}

@Composable
private fun SearchItemRow(
    text: String,
    icon: ImageVector,
    highlight: Boolean = false,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null, // más limpio estilo Google Play
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.adp()),
            tint = if (highlight)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Text(
            text = text,
            fontFamily = PoppinsRegular,
            fontSize = 14.asp(),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// TARJETA DE ANIME — VERSIÓN COMPLETA (resultados de búsqueda)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun AnimeCardItem(
    navController: NavController,
    anime: AnimeCard
) {
    var isPressed by remember { mutableStateOf(false) }
    val isDarkTheme = MaterialTheme.colorScheme.surface.luminance() < 0.5f

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "card_scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { isPressed = true; tryAwaitRelease(); isPressed = false },
                    onTap = { navController.navigate("${AppDestinations.ANIME_DETAIL_ROUTE}/${anime.malId}") }
                )
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp, pressedElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.adp())
        ) {
            // ── Cover con badge de score ──────────────────────────────────
            Box(
                modifier = Modifier
                    .width(120.adp())
                    .fillMaxHeight()
            ) {
                AsyncImage(
                    model = anime.images,
                    contentDescription = "Portada de ${anime.title}",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 18.dp, bottomStart = 18.dp)),
                    contentScale = ContentScale.Crop
                )
                // Gradiente sobre la imagen
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Black.copy(alpha = 0.3f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.5f)
                                )
                            )
                        )
                )
                // Badge de score
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                    shadowElevation = 6.dp,
                    tonalElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFB300),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = String.format("%.1f", anime.score),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 13.asp(),
                            fontFamily = PoppinsBold,
                            letterSpacing = 0.2.sp
                        )
                    }
                }
            }

            // ── Info ──────────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = anime.title,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 15.asp(),
                        fontFamily = PoppinsBold,
                        lineHeight = 19.asp(),
                        letterSpacing = 0.sp
                    )
                    StatusBadge(status = anime.status)
                    if (anime.genres.isNotEmpty()) {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(anime.genres.take(2)) { genre ->
                                genre?.name?.let { GenreChipCompact(genreName = it) }
                            }
                            if (anime.genres.size > 2) {
                                item { GenreChipCompact(genreName = "+${anime.genres.size - 2}") }
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        InfoChip(icon = Icons.Default.CalendarToday, text = anime.year)
                        InfoChip(icon = Icons.Default.PlayCircleOutline, text = "${anime.episodes}")
                    }
                    Surface(
                        onClick = {
                            navController.navigate("${AppDestinations.ANIME_DETAIL_ROUTE}/${anime.malId}?openSheet=true")
                        },
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        modifier = Modifier.size(38.adp())
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.BookmarkBorder,
                                contentDescription = "Añadir a lista",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.adp())
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// TARJETA MINIMAL (variante compacta para otras pantallas)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun AnimeCardItemMinimal(navController: NavController, anime: AnimeCard) {
    var isPressed by remember { mutableStateOf(false) }
    val isDarkTheme = MaterialTheme.colorScheme.surface.luminance() < 0.5f

    val elevation by animateDpAsState(
        targetValue = if (isPressed) 2.dp else 6.dp,
        label = "elevation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { isPressed = true; tryAwaitRelease(); isPressed = false },
                    onTap = { navController.navigate("${AppDestinations.ANIME_DETAIL_ROUTE}/${anime.malId}") }
                )
            },
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkTheme) MaterialTheme.colorScheme.surface else Color.White
        ),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.adp())
        ) {
            AsyncImage(
                model = anime.images,
                contentDescription = "Portada de ${anime.title}",
                modifier = Modifier
                    .width(130.adp())
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 18.dp, bottomStart = 18.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(18.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
                            fontSize = 17.asp(),
                            fontFamily = PoppinsBold,
                            lineHeight = 20.asp()
                        )
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
                                fontSize = 13.asp(),
                                fontFamily = PoppinsBold
                            )
                        }
                    }
                    StatusBadgeMinimal(status = anime.status)
                    if (anime.genres.isNotEmpty()) {
                        Text(
                            text = anime.genres.take(3).mapNotNull { it?.name }.joinToString(" • "),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            fontSize = 11.asp()
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${anime.year} • ${anime.episodes} eps",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        fontSize = 11.asp()
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.adp())
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// SUBCOMPONENTES VISUALES
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun StatusBadge(status: String) {
    val (color, text, dot) = when (status) {
        "Currently Airing" -> Triple(Color(0xFF10B981), "En emisión", "●")
        "Finished Airing" -> Triple(MaterialTheme.colorScheme.primary, "Finalizado", "✓")
        "Not yet aired" -> Triple(Color(0xFFFF9800), "Próximamente", "◐")
        else -> Triple(MaterialTheme.colorScheme.outline, status, "○")
    }
    Surface(shape = RoundedCornerShape(8.dp), color = color.copy(alpha = 0.15f)) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = dot, color = color, fontSize = 10.asp(), fontFamily = PoppinsBold)
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                color = color,
                fontFamily = PoppinsBold,
                fontSize = 11.asp(),
                letterSpacing = 0.2.sp
            )
        }
    }
}

@Composable
private fun StatusBadgeMinimal(status: String) {
    val (color, text) = when (status) {
        "Currently Airing" -> Color(0xFF10B981) to "En emisión"
        "Finished Airing" -> Color(0xFF3B82F6) to "Finalizado"
        "Not yet aired" -> Color(0xFFF59E0B) to "Próximamente"
        else -> Color.Gray to status
    }
    Text(
        text = "● $text",
        style = MaterialTheme.typography.labelSmall,
        color = color,
        fontFamily = PoppinsBold,
        fontSize = 12.asp()
    )
}

@Composable
private fun GenreChipCompact(genreName: String) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
    ) {
        Text(
            text = genreName,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            fontFamily = PoppinsRegular,
            fontSize = 10.asp(),
            letterSpacing = 0.1.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun InfoChip(icon: ImageVector, text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(12.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            fontSize = 11.asp(),
            fontFamily = PoppinsRegular,
            letterSpacing = 0.sp
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// BOTTOM SHEET DE GÉNEROS
// ─────────────────────────────────────────────────────────────────────────────
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Seleccionar género",
                    fontFamily = PoppinsBold,
                    fontSize = 20.asp(),
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar")
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            when {
                isLoadingGenres -> Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.adp()),
                    contentAlignment = Alignment.Center
                ) { LoadingScreen() }

                errorMessageGenres != null -> Text(
                    text = errorMessageGenres,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.error
                )

                else -> LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxHeight(0.7f)
                ) {
                    items(genres) { genre ->
                        GenreGridChip(
                            genre = genre,
                            isSelected = selectedGenreId == genre.malId.toString(),
                            onClick = { onGenreSelected(genre.malId.toString()) }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                    Text("Cancelar", fontFamily = PoppinsRegular)
                }
                Button(
                    onClick = onSearch,
                    modifier = Modifier.weight(1f),
                    enabled = selectedGenreId != null
                ) {
                    Text("Aplicar filtro", fontFamily = PoppinsBold)
                }
            }
        }
    }
}

@Composable
private fun GenreGridChip(
    genre: Genre,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceContainerHigh,
        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
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
                fontFamily = if (isSelected) PoppinsBold else PoppinsRegular,
                fontSize = 14.asp(),
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.adp())
                )
            } else {
                Text(
                    text = genre.count.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    fontSize = 12.asp()
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// DIALOG — AÑADIR ANIME A LISTA
// ─────────────────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAnimeDialog(
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
            // Header
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
                        modifier = Modifier.size(28.adp())
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Añadir a mi lista",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 26.asp(),
                        fontFamily = PoppinsBold
                    )
                }
            }

            // Estado
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .animateContentSize(),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
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
                                modifier = Modifier.size(24.adp())
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Estado",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 20.asp(),
                                fontFamily = PoppinsBold
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
                                        fontSize = 14.asp()
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = { expandedStatus = !expandedStatus },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.adp()),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedStatus != null) statusColors[selectedStatus]!!
                                else MaterialTheme.colorScheme.surface,
                                contentColor = if (selectedStatus != null) Color.Black
                                else MaterialTheme.colorScheme.onSurface
                            ),
                            shape = RoundedCornerShape(12.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = selectedStatus ?: "Seleccionar estado",
                                    modifier = Modifier.weight(1f),
                                    textAlign = if (selectedStatus != null) TextAlign.Center else TextAlign.Start,
                                    fontSize = 16.asp(),
                                    fontWeight = FontWeight.SemiBold
                                )
                                val rotation by animateFloatAsState(
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
                                        .size(28.adp())
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
                                    .height(200.adp()),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(items = statusAnime, key = { it }) { status ->
                                    val isSelected = selectedStatus == status
                                    val scale by animateFloatAsState(
                                        targetValue = if (isSelected) 1.05f else 1f,
                                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                                        label = "Card Scale"
                                    )
                                    Card(
                                        onClick = {
                                            selectedStatus =
                                                if (selectedStatus == status) null else status
                                            expandedStatus = false
                                        },
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .graphicsLayer { scaleX = scale; scaleY = scale },
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
                                                fontSize = 15.asp(),
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

            // Calificación
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
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
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
                                    modifier = Modifier.size(24.adp())
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Calificación",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 20.asp(),
                                    fontFamily = PoppinsBold
                                )
                            }
                            if (selectedStatus != "Planeado") {
                                RatingBar(rating = userRating, onRatingChange = { userRating = it })
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
                                        text = "No podés puntuar un anime planeado",
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                        textAlign = TextAlign.Center,
                                        fontSize = 14.asp()
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Opinión
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
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
                                modifier = Modifier.size(24.adp())
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Opinión",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 20.asp(),
                                fontFamily = PoppinsBold
                            )
                        }
                        OutlinedTextField(
                            value = userOpinion,
                            onValueChange = { userOpinion = it },
                            placeholder = {
                                Text(text = "Compartí tu opinión sobre este anime...")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.adp()),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = 0.2f
                                ),
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                cursorColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }

            // Botón guardar
            item {
                Button(
                    onClick = {
                        if (selectedStatus != null) {
                            val score = if (selectedStatus == "Planeado") 0.0f else userRating
                            onConfirm(score, selectedStatus!!, userOpinion)
                        }
                    },
                    enabled = selectedStatus != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .height(56.adp()),
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
                            modifier = Modifier.size(22.adp())
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Guardar en mi lista",
                            fontSize = 16.asp(),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// RATING BAR
// ─────────────────────────────────────────────────────────────────────────────
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
                val tint = if (isFilled || isHalfFilled) starColor
                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                val scale by animateFloatAsState(
                    targetValue = if (isFilled || isHalfFilled) 1.2f else 1.0f,
                    label = "starSizeAnimation"
                )
                Icon(
                    imageVector = imageVector,
                    contentDescription = "Puntuación de $starValue estrellas",
                    modifier = Modifier
                        .graphicsLayer { scaleX = scale; scaleY = scale }
                        .size(32.adp())
                        .clickable {
                            onRatingChange(
                                when (rating) {
                                    starValue -> starValue - 0.5f
                                    starValue - 0.5f -> 0f
                                    else -> starValue
                                }
                            )
                        },
                    tint = tint
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = if (rating % 1 == 0f)
                "Tu calificación: %d / %d".format(rating.toInt(), stars)
            else
                "Tu calificación: %.1f / %d".format(rating, stars),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.asp(),
            fontWeight = FontWeight.Bold,
            fontFamily = PoppinsBold,
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.surfaceContainerHigh,
                    RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}
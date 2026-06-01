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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.material.icons.filled.FilterList
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
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
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
import androidx.compose.runtime.snapshotFlow
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
import com.yumedev.seijakulist.domain.models.PopularGenres
import com.yumedev.seijakulist.ui.components.LoadingScreen
import com.yumedev.seijakulist.ui.components.NoInternetScreen
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.theme.GenreThemeMapper
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import com.yumedev.seijakulist.ui.theme.adp
import com.yumedev.seijakulist.ui.theme.asp
import kotlin.collections.listOf

// Altura aproximada de la barra colapsada
private val SearchBarCollapsedHeight = 72.dp

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
    val trendingAnimes by viewModel.trendingAnimes.collectAsState() // tendencias dinámicas
    val previewResults by viewModel.previewResults.collectAsState() // vista previa de resultados

    var expanded by remember { mutableStateOf(false) }
    var openBottomSheet by remember { mutableStateOf(false) }
    var openFiltersSheet by remember { mutableStateOf(false) }

    // Lazy load trending animes when screen opens
    LaunchedEffect(Unit) {
        viewModel.loadTrendingAnimes()
    }

    // Rastrear el estado anterior de expanded para detectar transiciones
    var previousExpanded by remember { mutableStateOf(expanded) }

    // Notificar cambio de estado de expansión
    LaunchedEffect(expanded) {
        onSearchExpandedChange(expanded)

        // Limpiar filtros y resultados cuando se navega de pantalla expandida a base
        if (previousExpanded && !expanded) {
            viewModel.clearAllFilters()
        }

        previousExpanded = expanded
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
                                contentDescription = "Buscar",
                                modifier = Modifier.size(22.adp()),
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    },
                    trailingIcon = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Botón de filtros (solo cuando está expandido)
                            if (expanded) {
                                val activeFiltersCount = listOfNotNull(
                                    selectedQuick,
                                    selectedFormat,
                                    selectedGenreId
                                ).size

                                BadgedBox(
                                    badge = {
                                        if (activeFiltersCount > 0) {
                                            Badge(
                                                containerColor = MaterialTheme.colorScheme.primary,
                                                contentColor = MaterialTheme.colorScheme.onPrimary
                                            ) {
                                                Text(
                                                    text = activeFiltersCount.toString(),
                                                    fontSize = 10.asp(),
                                                    fontFamily = PoppinsBold
                                                )
                                            }
                                        }
                                    }
                                ) {
                                    IconButton(onClick = { openFiltersSheet = true }) {
                                        Icon(
                                            imageVector = Icons.Default.FilterList,
                                            contentDescription = "Filtros",
                                            modifier = Modifier.size(22.adp()),
                                            tint = if (activeFiltersCount > 0)
                                                MaterialTheme.colorScheme.primary
                                            else
                                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                        )
                                    }
                                }
                            }

                            // Botón de limpiar búsqueda
                            if (searchQuery.isNotBlank()) {
                                IconButton(
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
                selectedQuick = selectedQuick,
                selectedFormat = selectedFormat,
                selectedGenreId = selectedGenreId,
                onFilterSelected = { filter ->
                    if (filter == "Géneros") openBottomSheet = true
                    else viewModel.onFilterSelected(if (selectedFilter == filter) null else filter)
                },
                onClearAllFilters = viewModel::clearAllFilters,
                onLoadMore = viewModel::loadMoreAnimes,
                recentSearches = recentSearches,
                trendingSearches = trendingAnimes,
                previewResults = previewResults,
                onRecentSearchClick = viewModel::onRecentSearchClicked,
                onDeleteRecentSearch = viewModel::deleteRecentSearch,
                onPerformSearch = viewModel::performSearchOrFilter,
                onRemoveQuickFilter = {
                    viewModel.onQuickFilterSelected(null)
                    viewModel.performSearchOrFilter()
                },
                onRemoveFormat = {
                    viewModel.onFormatSelected(null)
                    viewModel.performSearchOrFilter()
                },
                onRemoveGenre = {
                    viewModel.clearGenreFilter()
                    viewModel.performSearchOrFilter()
                }
            )
        }
    }

    // ── Bottom sheet de géneros ───────────────────────────────────────────
    if (openBottomSheet) {
        GenresBottomSheet(
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

    // ── Bottom sheet de filtros completo ──────────────────────────────────
    if (openFiltersSheet) {
        FiltersBottomSheet(
            selectedQuick = selectedQuick,
            selectedFormat = selectedFormat,
            selectedGenreId = selectedGenreId,
            onQuickFilterSelected = { filter ->
                viewModel.onQuickFilterSelected(if (selectedQuick == filter) null else filter)
            },
            onFormatSelected = { format ->
                viewModel.onFormatSelected(if (selectedFormat == format) null else format)
            },
            onGenreSelected = viewModel::onGenreSelected,
            onDismiss = { openFiltersSheet = false },
            onApplyFilters = {
                viewModel.performSearchOrFilter()
                openFiltersSheet = false
            },
            onClearAll = {
                viewModel.clearAllFilters()
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

        SectionHeader(title = "Géneros populares")

        val displayGenres = PopularGenres.popularGenres
        val chunked = displayGenres.chunked(2)

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            chunked.forEachIndexed { _, rowGenres ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rowGenres.forEach { genre ->
                        val genreTheme = GenreThemeMapper.getThemeForGenre(genre.name)
                        DiscoveryGenreCard(
                            genre = genre,
                            startColor = genreTheme.startColor,
                            endColor = genreTheme.endColor,
                            icon = genreTheme.icon,
                            onClick = { onGenreDirectTap(genre.malId.toString()) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowGenres.size == 1) Spacer(modifier = Modifier.weight(1f))
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
        color = if (isActive) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
        else MaterialTheme.colorScheme.surfaceContainerHigh,
        border = if (isActive)
            BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
        else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Icono de $label",
                modifier = Modifier.size(16.adp()),
                tint = if (isActive) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = label,
                fontFamily = if (isActive) PoppinsBold else PoppinsRegular,
                fontSize = 13.asp(),
                color = if (isActive) MaterialTheme.colorScheme.primary
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
        color = if (isActive) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
        else MaterialTheme.colorScheme.surfaceContainerHigh,
        border = if (isActive)
            BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
        else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Text(
            text = label,
            fontFamily = if (isActive) PoppinsBold else PoppinsRegular,
            fontSize = 13.asp(),
            color = if (isActive) MaterialTheme.colorScheme.primary
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
    icon: ImageVector,
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
        ) {
            // Background icon as watermark
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.15f),
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 12.dp, y = 12.dp)
            )

            // Content
            Box(
                modifier = Modifier
                    .fillMaxSize()
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
    selectedQuick: String?,
    selectedFormat: String?,
    selectedGenreId: String?,
    onFilterSelected: (String) -> Unit,
    onClearAllFilters: () -> Unit,
    onLoadMore: () -> Unit = {},
    recentSearches: List<String> = emptyList(),
    trendingSearches: List<String> = emptyList(),
    previewResults: List<AnimeCard> = emptyList(),
    onRecentSearchClick: (String) -> Unit = {},
    onDeleteRecentSearch: (String) -> Unit = {},
    onPerformSearch: () -> Unit = {},
    onRemoveQuickFilter: () -> Unit = {},
    onRemoveFormat: () -> Unit = {},
    onRemoveGenre: () -> Unit = {}
) {
    val hasActiveFilters = selectedQuick != null || selectedFormat != null || selectedGenreId != null
    val showPreview = previewResults.isNotEmpty() && searchQuery.isNotBlank() && animeList.isEmpty()
    when {
        isLoading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { LoadingScreen() }

        errorMessage != null -> NoInternetScreen(onRetryClick = {})

        animeList.isNotEmpty() -> {
            val listState = rememberLazyListState()

            // Improved pagination trigger - prevents duplicate calls
            LaunchedEffect(listState) {
                snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                    .collect { lastVisibleIndex ->
                        if (lastVisibleIndex != null && lastVisibleIndex >= animeList.size - 3 && !isLoadingMore) {
                            onLoadMore()
                        }
                    }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
            item {
                ContentTypeFilters(
                    selectedFilter = selectedFilter,
                    onFilterSelected = onFilterSelected
                )
            }

            // ── Chips de filtros activos removibles ─────────────────
            if (hasActiveFilters) {
                item {
                    ActiveFiltersChips(
                        selectedQuick = selectedQuick,
                        selectedFormat = selectedFormat,
                        selectedGenreId = selectedGenreId,
                        onRemoveQuickFilter = onRemoveQuickFilter,
                        onRemoveFormat = onRemoveFormat,
                        onRemoveGenre = onRemoveGenre
                    )
                }
            }
            if (hasActiveFilters) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${animeList.size}+ resultados encontrados",
                            fontFamily = PoppinsRegular,
                            fontSize = 14.asp(),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        OutlinedButton(
                            onClick = onClearAllFilters,
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Limpiar filtros",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Limpiar filtros",
                                fontFamily = PoppinsRegular,
                                fontSize = 12.asp()
                            )
                        }
                    }
                }
            } else {
                item {
                    Text(
                        text = "${animeList.size}+ resultados encontrados",
                        fontFamily = PoppinsRegular,
                        fontSize = 14.asp(),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 4.dp)
                    )
                }
            }
            items(animeList, key = { it.malId }) { anime ->
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    AnimeCardItem(navController = navController, anime = anime)
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
        }

        showPreview -> {
            // Vista previa de resultados mientras escribe
            Column(modifier = Modifier.fillMaxSize()) {
                ContentTypeFilters(
                    selectedFilter = selectedFilter,
                    onFilterSelected = onFilterSelected
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Resultados sugeridos",
                    fontFamily = PoppinsBold,
                    fontSize = 14.asp(),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                previewResults.forEach { anime ->
                    PreviewAnimeCard(
                        anime = anime,
                        navController = navController,
                        onClick = {
                            onPerformSearch()
                        }
                    )
                }

                // Botón para ver todos los resultados
                if (previewResults.size >= 4) {
                    OutlinedButton(
                        onClick = onPerformSearch,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Ver todos los resultados",
                            fontFamily = PoppinsBold,
                            fontSize = 14.asp()
                        )
                    }
                }
            }
        }

        else -> {
            EmptySearchState(
                selectedFilter = selectedFilter,
                onFilterSelected = onFilterSelected,
                recentSearches = recentSearches,
                trendingSearches = trendingSearches,
                onSearchClick = onRecentSearchClick,
                onDeleteSearch = onDeleteRecentSearch
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// ESTADO VACÍO
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun EmptySearchState(
    recentSearches: List<String> = emptyList(),
    trendingSearches: List<String> = emptyList(),
    onSearchClick: (String) -> Unit = {},
    onDeleteSearch: (String) -> Unit = {},
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
                    showDelete = true,
                    onClick = { onSearchClick(query) },
                    onDelete = { onDeleteSearch(query) }
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
    showDelete: Boolean = false,
    onClick: () -> Unit,
    onDelete: () -> Unit = {}
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
            contentDescription = if (highlight) "Tendencia" else "Búsqueda reciente",
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

        if (showDelete) {
            IconButton(
                onClick = {
                    onDelete()
                },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Eliminar búsqueda",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// TARJETA DE VISTA PREVIA — VERSIÓN COMPACTA
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun PreviewAnimeCard(
    anime: AnimeCard,
    navController: NavController,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("${AppDestinations.ANIME_DETAIL_ROUTE}/${anime.malId}") }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagen pequeña
        AsyncImage(
            model = anime.images,
            contentDescription = "Portada de ${anime.title}",
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        // Info
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = anime.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.asp(),
                fontFamily = PoppinsBold
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Score
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Calificación",
                        tint = Color(0xFFFFB300),
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = String.format("%.1f", anime.score),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        fontSize = 12.asp(),
                        fontFamily = PoppinsRegular
                    )
                }

                Text(
                    text = "•",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    fontSize = 12.asp()
                )

                Text(
                    text = anime.year,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 12.asp(),
                    fontFamily = PoppinsRegular
                )
            }
        }

        // Flecha
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Ver detalles",
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
            modifier = Modifier.size(16.dp)
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
            // ── Cover limpia (sin badges) ──────────────────────────────────
            AsyncImage(
                model = anime.images,
                contentDescription = "Portada de ${anime.title}",
                modifier = Modifier
                    .width(120.adp())
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 18.dp, bottomStart = 18.dp)),
                contentScale = ContentScale.Crop
            )

            // ── Info rediseñada ──────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(start = 14.dp, end = 14.dp, top = 14.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    // 1. Título (1 línea, prominente)
                    Text(
                        text = anime.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.asp(),
                        fontFamily = PoppinsBold,
                        lineHeight = 20.asp(),
                        letterSpacing = 0.sp
                    )

                    // 2. Type + Score + Status en formato Pill (misma Row)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TypePill(type = anime.type)
                        ScorePill(score = anime.score)
                        StatusPill(status = anime.status)
                    }

                    // 3. Géneros (hasta 3)
                    if (anime.genres.isNotEmpty()) {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(anime.genres.take(3)) { genre ->
                                genre?.name?.let { GenreChipCompact(genreName = it) }
                            }
                            if (anime.genres.size > 3) {
                                item { GenreChipCompact(genreName = "+${anime.genres.size - 3}") }
                            }
                        }
                    }
                }

                // 4. Bottom: Metadata Pills + Botón
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        YearPill(year = anime.year)
                        EpisodesPill(episodes = anime.episodes)
                    }
                    Surface(
                        onClick = {
                            navController.navigate("${AppDestinations.ANIME_DETAIL_ROUTE}/${anime.malId}?openSheet=true")
                        },
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        modifier = Modifier.size(48.adp())
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.BookmarkBorder,
                                contentDescription = "Añadir a lista",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.adp())
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
                                contentDescription = "Calificación",
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
                        contentDescription = "Ver detalles",
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

// ═══════════════════════════════════════════════════════════════════════════
// PILLS COMPACTOS (Type, Score, Status) - Formato unificado
// ═══════════════════════════════════════════════════════════════════════════

@Composable
private fun TypePill(type: String) {
    val (color, displayText) = when (type) {
        "TV" -> Color(0xFF6366F1) to "TV"
        "Movie" -> Color(0xFFEC4899) to "Movie"
        "OVA" -> Color(0xFF8B5CF6) to "OVA"
        "ONA" -> Color(0xFF14B8A6) to "ONA"
        "Special" -> Color(0xFFF59E0B) to "Special"
        "Music" -> Color(0xFF06B6D4) to "Music"
        else -> MaterialTheme.colorScheme.tertiary to type
    }
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.18f)
    ) {
        Text(
            text = displayText,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            color = color,
            fontFamily = PoppinsBold,
            fontSize = 10.asp(),
            letterSpacing = 0.4.sp
        )
    }
}

@Composable
private fun ScorePill(score: Float) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFFFB300).copy(alpha = 0.18f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Calificación",
                tint = Color(0xFFFFB300),
                modifier = Modifier.size(11.dp)
            )
            Text(
                text = String.format("%.1f", score),
                color = Color(0xFFFFB300),
                fontSize = 10.asp(),
                fontFamily = PoppinsBold,
                letterSpacing = 0.3.sp
            )
        }
    }
}

@Composable
private fun StatusPill(status: String) {
    val (color, text, dot) = when (status) {
        "Currently Airing" -> Triple(Color(0xFF10B981), "Emisión", "●")
        "Finished Airing" -> Triple(MaterialTheme.colorScheme.primary, "Finalizado", "✓")
        "Not yet aired" -> Triple(Color(0xFFFF9800), "Próximo", "◐")
        else -> Triple(MaterialTheme.colorScheme.outline, status, "○")
    }
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.18f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = dot, color = color, fontSize = 9.asp(), fontFamily = PoppinsBold)
            Text(
                text = text,
                color = color,
                fontFamily = PoppinsBold,
                fontSize = 10.asp(),
                letterSpacing = 0.3.sp
            )
        }
    }
}

@Composable
private fun YearPill(year: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.6f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = "Año",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.size(10.dp)
            )
            Text(
                text = year,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                fontSize = 10.asp(),
                fontFamily = PoppinsBold,
                letterSpacing = 0.3.sp
            )
        }
    }
}

@Composable
private fun EpisodesPill(episodes: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.6f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.PlayCircleOutline,
                contentDescription = "Episodios",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.size(10.dp)
            )
            Text(
                text = episodes,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                fontSize = 10.asp(),
                fontFamily = PoppinsBold,
                letterSpacing = 0.3.sp
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// BADGES (usado en AnimeCardItemMinimal - mantener compatibilidad)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun TypeBadge(type: String) {
    val (color, displayText) = when (type) {
        "TV" -> Color(0xFF6366F1) to "TV"
        "Movie" -> Color(0xFFEC4899) to "Movie"
        "OVA" -> Color(0xFF8B5CF6) to "OVA"
        "ONA" -> Color(0xFF14B8A6) to "ONA"
        "Special" -> Color(0xFFF59E0B) to "Special"
        "Music" -> Color(0xFF06B6D4) to "Music"
        else -> MaterialTheme.colorScheme.tertiary to type
    }
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.15f)
    ) {
        Text(
            text = displayText,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = color,
            fontFamily = PoppinsBold,
            fontSize = 10.asp(),
            letterSpacing = 0.3.sp
        )
    }
}

@Composable
private fun ScoreChip(score: Float) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = Color(0xFFFFB300).copy(alpha = 0.15f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Calificación",
                tint = Color(0xFFFFB300),
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = String.format("%.1f", score),
                color = Color(0xFFFFB300),
                fontSize = 11.asp(),
                fontFamily = PoppinsBold,
                letterSpacing = 0.2.sp
            )
        }
    }
}

@Composable
private fun StatusBadge(status: String) {
    val (color, text, dot) = when (status) {
        "Currently Airing" -> Triple(Color(0xFF10B981), "En emisión", "●")
        "Finished Airing" -> Triple(MaterialTheme.colorScheme.primary, "Finalizado", "✓")
        "Not yet aired" -> Triple(Color(0xFFFF9800), "Próximamente", "◐")
        else -> Triple(MaterialTheme.colorScheme.outline, status, "○")
    }
    Surface(shape = RoundedCornerShape(7.dp), color = color.copy(alpha = 0.15f)) {
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
            contentDescription = "Información: $text",
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
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar selector de géneros")
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxHeight(0.7f)
            ) {
                items(PopularGenres.genres) { genre ->
                    GenreGridChip(
                        genre = genre,
                        isSelected = selectedGenreId == genre.malId.toString(),
                        onClick = { onGenreSelected(genre.malId.toString()) }
                    )
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
        color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
        else MaterialTheme.colorScheme.surfaceContainerHigh,
        border = if (isSelected) BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)) else null,
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
                color = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Género seleccionado",
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
// CHIPS DE FILTROS ACTIVOS REMOVIBLES
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun ActiveFiltersChips(
    selectedQuick: String?,
    selectedFormat: String?,
    selectedGenreId: String?,
    onRemoveQuickFilter: () -> Unit,
    onRemoveFormat: () -> Unit,
    onRemoveGenre: () -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Quick filter chip
        if (selectedQuick != null) {
            item {
                val quickFilterLabel = quickFilters.find { it.filterKey == selectedQuick }?.label ?: selectedQuick
                ActiveFilterChip(
                    label = quickFilterLabel,
                    onRemove = onRemoveQuickFilter
                )
            }
        }

        // Format chip
        if (selectedFormat != null) {
            item {
                ActiveFilterChip(
                    label = selectedFormat,
                    onRemove = onRemoveFormat
                )
            }
        }

        // Genre chip
        if (selectedGenreId != null) {
            item {
                val genreName = PopularGenres.genres.find { it.malId.toString() == selectedGenreId }?.name ?: "Género"
                ActiveFilterChip(
                    label = genreName,
                    onRemove = onRemoveGenre
                )
            }
        }
    }
}

@Composable
private fun ActiveFilterChip(
    label: String,
    onRemove: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, end = 8.dp, top = 6.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label,
                fontFamily = PoppinsBold,
                fontSize = 13.asp(),
                color = MaterialTheme.colorScheme.primary
            )
            Surface(
                onClick = onRemove,
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                modifier = Modifier.size(20.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remover filtro",
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// BOTTOM SHEET DE FILTROS COMPLETO
// ─────────────────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FiltersBottomSheet(
    selectedQuick: String?,
    selectedFormat: String?,
    selectedGenreId: String?,
    onQuickFilterSelected: (String) -> Unit,
    onFormatSelected: (String) -> Unit,
    onGenreSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    onApplyFilters: () -> Unit,
    onClearAll: () -> Unit
) {
    val hasActiveFilters = selectedQuick != null || selectedFormat != null || selectedGenreId != null

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // ── Header ────────────────────────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filtros",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.adp())
                        )
                        Text(
                            text = "Filtros",
                            fontFamily = PoppinsBold,
                            fontSize = 24.asp(),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar filtros",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            item { HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant) }

            // ── SECCIÓN 1: Quick Filters ──────────────────────────────
            item {
                Text(
                    text = "Acceso rápido",
                    fontFamily = PoppinsBold,
                    fontSize = 16.asp(),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp)
                ) {
                    items(quickFilters) { qf ->
                        QuickFilterChip(
                            label = qf.label,
                            icon = qf.icon,
                            isActive = selectedQuick == qf.filterKey,
                            onClick = { onQuickFilterSelected(qf.filterKey) }
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )
            }

            // ── SECCIÓN 2: Formatos ────────────────────────────────────
            item {
                Text(
                    text = "Formatos",
                    fontFamily = PoppinsBold,
                    fontSize = 16.asp(),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp)
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

            item {
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )
            }

            // ── SECCIÓN 3: Géneros ─────────────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Géneros",
                        fontFamily = PoppinsBold,
                        fontSize = 16.asp(),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (selectedGenreId != null) {
                        val selectedGenreName = PopularGenres.genres.find { it.malId.toString() == selectedGenreId }?.name
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = selectedGenreName ?: "Seleccionado",
                                fontFamily = PoppinsRegular,
                                fontSize = 12.asp(),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            items(PopularGenres.genres.chunked(2)) { rowGenres ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowGenres.forEach { genre ->
                        GenreFilterChip(
                            genre = genre,
                            isSelected = selectedGenreId == genre.malId.toString(),
                            onClick = { onGenreSelected(genre.malId.toString()) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowGenres.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }

        // ── Botones de acción (fijos en la parte inferior) ────────────
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onApplyFilters,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.adp()),
                    enabled = hasActiveFilters,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Aplicar filtros",
                        fontFamily = PoppinsBold,
                        fontSize = 16.asp()
                    )
                }

                if (hasActiveFilters) {
                    OutlinedButton(
                        onClick = {
                            onClearAll()
                            onDismiss()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.adp()),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Limpiar todo",
                            fontFamily = PoppinsRegular,
                            fontSize = 14.asp()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GenreFilterChip(
    genre: Genre,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
        else MaterialTheme.colorScheme.surfaceContainerHigh,
        border = if (isSelected)
            BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
        else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = genre.name,
                fontFamily = if (isSelected) PoppinsBold else PoppinsRegular,
                fontSize = 13.asp(),
                color = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Seleccionado",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Text(
                    text = genre.count.toString(),
                    fontFamily = PoppinsRegular,
                    fontSize = 11.asp(),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
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
                        contentDescription = "Agregar a favoritos",
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
                                contentDescription = "Estado del anime",
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
                                    contentDescription = "Expandir opciones de estado",
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
                                    contentDescription = "Calificación",
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
                                contentDescription = "Escribir opinión",
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
                            contentDescription = "Confirmar",
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
package com.yumedev.seijakulist.ui.screens.my_animes

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.yumedev.seijakulist.R
import com.yumedev.seijakulist.ui.components.AdvancedFiltersBottomSheet
import com.yumedev.seijakulist.ui.components.AnimeFilters
import com.yumedev.seijakulist.ui.components.AnimeReviewDialog
import com.yumedev.seijakulist.ui.components.AnimeStatusChip
import com.yumedev.seijakulist.ui.components.CustomDialog
import com.yumedev.seijakulist.ui.components.DeleteMyAnime
import com.yumedev.seijakulist.ui.components.DialogType
import com.yumedev.seijakulist.ui.components.ViewMode
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import kotlinx.coroutines.launch
import com.yumedev.seijakulist.ui.theme.adp
import com.yumedev.seijakulist.ui.theme.asp
import com.yumedev.seijakulist.ui.theme.getAnimeStatusColor
import kotlinx.coroutines.delay

@Composable
fun MyAnimeListScreen(
    navController: NavController,
    viewModel: MyAnimeListViewModel = hiltViewModel(),
    isSearching: Boolean,
    onDismissSearch: () -> Unit,
    viewMode: ViewMode = ViewMode.LIST,
    sortOrder: com.yumedev.seijakulist.ui.components.SortOrder = com.yumedev.seijakulist.ui.components.SortOrder.NONE
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val savedAnimes by viewModel.savedAnimes.collectAsState()
    val savedAnimeStatusComplete by viewModel.savedAnimeStatusComplete.collectAsState()
    val savedAnimeStatusWatching by viewModel.savedAnimeStatusWatching.collectAsState()
    val savedAnimeStatusPending by viewModel.savedAnimeStatusPending.collectAsState()
    val savedAnimeStatusAbandoned by viewModel.savedAnimeStatusAbandoned.collectAsState()
    val savedAnimeStatusPlanned by viewModel.savedAnimeStatusPlanned.collectAsState()
    val animeCompletedEvent by viewModel.animeCompletedEvent.collectAsState()
    val showReviewDialog by viewModel.showReviewDialog.collectAsState()

    // Estado para el glow de cada anime
    var glowingAnimeId by remember { mutableStateOf<Int?>(null) }

    val RobotoRegular = FontFamily(
        Font(R.font.roboto_regular)
    )
    val RobotoBold = FontFamily(
        Font(R.font.roboto_bold, FontWeight.Bold)
    )

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val statusAnime = listOf("Viendo", "Completado", "Pendiente", "Abandonado", "Planeado")

    var collapsedFilter by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    var searchQuery by remember { mutableStateOf("") }
    var collapsedSearch by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    var isSortedAscending by remember { mutableStateOf(false) }

    LaunchedEffect(collapsedSearch) {
        if (collapsedSearch) {
            focusRequester.requestFocus()
        }
    }

    // Escuchar evento de anime completado
    LaunchedEffect(animeCompletedEvent) {
        animeCompletedEvent?.let { event ->
            // Activar el glow
            glowingAnimeId = event.animeId

            // Mostrar snackbar
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "¡Completado! Tu anime numero ${event.totalCompleted}",
                    duration = SnackbarDuration.Short
                )
            }

            // Desactivar el glow después de 1.5 segundos
            delay(1500)
            glowingAnimeId = null

            // Limpiar el evento
            viewModel.clearAnimeCompletedEvent()
        }
    }

    var showDialogStatus by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var animeIdToDelete by remember { mutableStateOf(0) }
    var selectedFilter by remember { mutableStateOf<String?>(null) }

    // Estados para filtros avanzados
    var showAdvancedFilters by remember { mutableStateOf(false) }
    var activeFilters by remember { mutableStateOf(AnimeFilters()) }

    // Estados de scroll para cada modo de visualización
    val gridState = rememberLazyGridState()
    val cardState = rememberLazyGridState()
    val listState = rememberLazyListState()

    // Determinar si mostrar el botón de "volver arriba"
    val showScrollToTopButton by remember {
        derivedStateOf {
            when (viewMode) {
                ViewMode.GRID -> gridState.firstVisibleItemIndex > 3
                ViewMode.CARD -> cardState.firstVisibleItemIndex > 2
                ViewMode.LIST -> listState.firstVisibleItemIndex > 2
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            focusManager.clearFocus()
                        }
                    )
                }
        ) {
        when {
            // Mostrar skeleton loading
            isLoading -> {
                when (viewMode) {
                    ViewMode.LIST -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(5) {
                                SkeletonLoadingCard(
                                    viewMode = ViewMode.LIST
                                )
                            }
                        }
                    }

                    ViewMode.GRID -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(9) {
                                SkeletonLoadingCard(viewMode = ViewMode.GRID)
                            }
                        }
                    }

                    ViewMode.CARD -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(6) {
                                SkeletonLoadingCard(viewMode = ViewMode.CARD)
                            }
                        }
                    }
                }
            }
            // Mostrar empty state
            savedAnimes.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Título principal
                        Text(
                            text = "¡Tu lista está vacía!",
                            fontFamily = PoppinsBold,
                            fontSize = 28.asp(),
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )

                        // Descripción
                        Text(
                            text = "Comienza a construir tu colección de animes favoritos. Busca y agrega tus primeros títulos para empezar a llevar el control de lo que ves.",
                            fontFamily = PoppinsRegular,
                            fontSize = 16.asp(),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center,
                            lineHeight = 24.asp()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Botón mejorado
                        Button(
                            onClick = {
                                navController.navigate(AppDestinations.SEARCH_ANIME_ROUTE)
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .height(56.adp()),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp,
                                pressedElevation = 8.dp
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                modifier = Modifier.size(24.adp())
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Buscar anime",
                                fontFamily = PoppinsBold,
                                fontSize = 18.asp()
                            )
                        }

                        // Texto secundario
                        Text(
                            text = "Explora miles de títulos disponibles",
                            fontFamily = PoppinsRegular,
                            fontSize = 14.asp(),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            // Mostrar contenido
            else -> {

                AnimatedContent(
                    targetState = isSearching,
                    transitionSpec = {
                        // Anima la entrada y salida del contenido.
                        (slideInVertically(animationSpec = tween(500)) + fadeIn())
                            .togetherWith(slideOutVertically(animationSpec = tween(500)) + fadeOut())
                            .using(
                                // Anima el tamaño del contenedor principal para un efecto más suave.
                                SizeTransform(clip = false)
                            )
                    }
                ) { targetIsSearching ->
                    if (targetIsSearching) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Buscar anime...") },
                            trailingIcon = {
                                IconButton(onClick = {
                                    onDismissSearch()
                                    searchQuery = ""
                                }) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Cerrar búsqueda"
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = Color.Transparent,
                                cursorColor = MaterialTheme.colorScheme.primary,
                                focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = 0.5f
                                ),
                                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = 0.7f
                                )
                            ),
                        )
                    } else {
                        Spacer(modifier = Modifier.height(0.dp))
                    }
                }

                // Calcular la cantidad de animes por estado
                val animeCounts = remember(savedAnimes) {
                    statusAnime.associateWith { status ->
                        savedAnimes.count { it.statusUser == status }
                    }
                }

                // Contador de animes y filtros
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Filtros redondeados
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        // Botón de filtros avanzados
                        item {
                            Surface(
                                onClick = { showAdvancedFilters = true },
                                shape = RoundedCornerShape(20.dp),
                                color = if (activeFilters.isActive())
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                else MaterialTheme.colorScheme.surfaceContainerHigh,
                                border = if (activeFilters.isActive())
                                    BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                                else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AddCircleOutline,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.adp()),
                                        tint = if (activeFilters.isActive())
                                            MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                    Text(
                                        text = if (activeFilters.isActive()) "Filtros activos" else "Más filtros",
                                        fontFamily = if (activeFilters.isActive()) PoppinsBold else PoppinsRegular,
                                        fontSize = 13.asp(),
                                        color = if (activeFilters.isActive())
                                            MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                    )
                                    if (activeFilters.isActive()) {
                                        val totalActiveFilters = activeFilters.statuses.size +
                                                activeFilters.genres.size +
                                                activeFilters.types.size +
                                                activeFilters.years.size +
                                                if (activeFilters.scoreRange != 0f..10f) 1 else 0
                                        Text(
                                            text = totalActiveFilters.toString(),
                                            fontFamily = PoppinsBold,
                                            fontSize = 12.asp(),
                                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                                        )
                                    }
                                }
                            }
                        }

                        items(statusAnime.size) { index ->
                            val filter = statusAnime[index]
                            val isSelected = selectedFilter == filter
                            val count = animeCounts[filter] ?: 0

                            val filterIcon = when (filter) {
                                "Viendo" -> Icons.Default.RemoveRedEye
                                "Completado" -> Icons.Default.CheckCircle
                                "Pendiente" -> Icons.Default.Schedule
                                "Abandonado" -> Icons.Default.Stop
                                "Planeado" -> Icons.Default.EventAvailable
                                else -> Icons.Default.Star
                            }

                            Surface(
                                onClick = {
                                    selectedFilter = if (isSelected) null else filter
                                },
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                else MaterialTheme.colorScheme.surfaceContainerHigh,
                                border = if (isSelected)
                                    BorderStroke(
                                        1.5.dp,
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                    )
                                else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                            ) {
                                Row(
                                    modifier = Modifier.padding(
                                        horizontal = 14.dp,
                                        vertical = 5.dp
                                    ),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = filterIcon,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.adp()),
                                        tint = if (isSelected) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                    Text(
                                        text = filter,
                                        fontFamily = if (isSelected) PoppinsBold else PoppinsRegular,
                                        fontSize = 13.asp(),
                                        color = if (isSelected) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                    )
                                    Text(
                                        text = count.toString(),
                                        fontFamily = if (isSelected) PoppinsBold else PoppinsRegular,
                                        fontSize = 12.asp(),
                                        color = if (isSelected) MaterialTheme.colorScheme.primary.copy(
                                            alpha = 0.7f
                                        )
                                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                    )
                                }
                            }
                        }
                    }
                }

                val displayedAnimes =
                    remember(savedAnimes, searchQuery, selectedFilter, sortOrder, activeFilters) {
                        val filtered = savedAnimes.filter { anime ->
                            // Filtro por búsqueda de texto
                            val matchesSearch = searchQuery.isBlank() || anime.title.contains(
                                searchQuery,
                                ignoreCase = true
                            )

                            // Filtro por estado simple (barra de filtros)
                            val matchesSimpleStatus = selectedFilter == null || anime.statusUser == selectedFilter

                            // Filtros avanzados - Estados
                            val matchesAdvancedStatus = activeFilters.statuses.isEmpty() ||
                                    activeFilters.statuses.contains(anime.statusUser)

                            // Filtros avanzados - Géneros
                            val matchesGenres = if (activeFilters.genres.isEmpty()) {
                                true
                            } else {
                                val animeGenres = anime.genres.split(",")
                                    .map { it.trim() }
                                    .filter { it.isNotBlank() }
                                activeFilters.genres.any { filterGenre ->
                                    animeGenres.any { it.equals(filterGenre, ignoreCase = true) }
                                }
                            }

                            // Filtros avanzados - Tipos
                            val matchesTypes = activeFilters.types.isEmpty() ||
                                    activeFilters.types.contains(anime.typeAnime)

                            // Filtros avanzados - Años
                            val matchesYears = activeFilters.years.isEmpty() ||
                                    (anime.year != null && activeFilters.years.contains(anime.year))

                            // Filtros avanzados - Puntuación
                            val matchesScore = anime.userScore >= activeFilters.scoreRange.start &&
                                    anime.userScore <= activeFilters.scoreRange.endInclusive

                            matchesSearch && matchesSimpleStatus && matchesAdvancedStatus &&
                                    matchesGenres && matchesTypes && matchesYears && matchesScore
                        }

                        when (sortOrder) {
                            com.yumedev.seijakulist.ui.components.SortOrder.A_TO_Z -> filtered.sortedBy { it.title.lowercase() }
                            com.yumedev.seijakulist.ui.components.SortOrder.Z_TO_A -> filtered.sortedByDescending { it.title.lowercase() }
                            com.yumedev.seijakulist.ui.components.SortOrder.NONE -> filtered.sortedWith(
                                compareByDescending { it.statusUser == "Viendo" }
                            )
                        }
                    }

                // Mostrar empty state personalizado si no hay resultados con filtro activo
                if (displayedAnimes.isEmpty() && (selectedFilter != null || searchQuery.isNotBlank())) {
                    EmptyStateByFilter(
                        selectedFilter = selectedFilter,
                        onNavigateToSearch = {
                            navController.navigate(AppDestinations.SEARCH_ANIME_ROUTE)
                        }
                    )
                } else {
                    when (viewMode) {
                        ViewMode.GRID -> {
                            LazyVerticalGrid(
                                state = gridState,
                                columns = GridCells.Fixed(3),
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(
                                    start = 16.dp,
                                    end = 16.dp,
                                    top = 8.dp,
                                    bottom = 116.dp
                                )
                            ) {
                                items(displayedAnimes) { anime ->
                                    CompactAnimeCard(
                                        anime = anime,
                                        statusColor = getAnimeStatusColor(anime.statusUser),
                                        onAnimeClick = {
                                            navController.navigate("${AppDestinations.ANIME_DETAIL_LOCAL_ROUTE}/${anime.malId}")
                                        },
                                        onDeleteConfirmed = {
                                            showDialog = true
                                            animeIdToDelete = anime.malId
                                        },
                                        viewModel = viewModel,
                                        isGlowing = glowingAnimeId == anime.malId
                                    )
                                }
                            }
                        }

                        ViewMode.CARD -> {
                            LazyVerticalGrid(
                                state = cardState,
                                columns = GridCells.Fixed(2),
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(
                                    start = 16.dp,
                                    end = 16.dp,
                                    top = 8.dp,
                                    bottom = 116.dp
                                )
                            ) {
                                items(displayedAnimes) { anime ->
                                    OnboardingStyleAnimeCard(
                                        anime = anime,
                                        statusColor = getAnimeStatusColor(anime.statusUser),
                                        onAnimeClick = {
                                            navController.navigate("${AppDestinations.ANIME_DETAIL_LOCAL_ROUTE}/${anime.malId}")
                                        },
                                        viewModel = viewModel,
                                        isGlowing = glowingAnimeId == anime.malId
                                    )
                                }
                            }
                        }

                        ViewMode.LIST -> {
                            LazyColumn(
                                state = listState,
                                modifier = Modifier
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(
                                    start = 16.dp,
                                    end = 16.dp,
                                    top = 8.dp,
                                    bottom = 16.dp
                                )
                            ) {
                                items(displayedAnimes) { anime ->
                                    var isPressed by remember { mutableStateOf(false) }
                                    val isGlowing = glowingAnimeId == anime.malId

                                    val scale by animateFloatAsState(
                                        targetValue = if (isPressed) 0.97f else 1f,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessMedium
                                        ),
                                        label = "card_scale"
                                    )

                                    // Animación del glow
                                    val glowAlpha by animateFloatAsState(
                                        targetValue = if (isGlowing) 1f else 0f,
                                        animationSpec = tween(300),
                                        label = "glow_alpha"
                                    )

                                    ElevatedCard(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(160.adp())
                                            .graphicsLayer {
                                                scaleX = scale
                                                scaleY = scale
                                            }
                                            .then(
                                                if (isGlowing) Modifier
                                                    .shadow(
                                                        elevation = 12.dp,
                                                        shape = RoundedCornerShape(20.dp),
                                                        ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = glowAlpha * 0.5f),
                                                        spotColor = MaterialTheme.colorScheme.primary.copy(alpha = glowAlpha)
                                                    )
                                                    .border(
                                                        width = 3.dp,
                                                        color = MaterialTheme.colorScheme.primary.copy(alpha = glowAlpha),
                                                        shape = RoundedCornerShape(20.dp)
                                                    )
                                                else Modifier
                                            )
                                            .pointerInput(Unit) {
                                                detectTapGestures(
                                                    onPress = {
                                                        isPressed = true
                                                        tryAwaitRelease()
                                                        isPressed = false
                                                    },
                                                    onTap = {
                                                        navController.navigate("${AppDestinations.ANIME_DETAIL_LOCAL_ROUTE}/${anime.malId}")
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
                                        Box(modifier = Modifier.fillMaxSize()) {
                                            Row(
                                                modifier = Modifier.fillMaxSize()
                                            ) {
                                                // Imagen
                                                Box(
                                                    modifier = Modifier
                                                        .width(120.adp())
                                                        .fillMaxHeight()
                                                ) {
                                                    Image(
                                                        painter = rememberAsyncImagePainter(anime.imageUrl),
                                                        contentDescription = anime.title,
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .clip(
                                                                RoundedCornerShape(
                                                                    topStart = 20.dp,
                                                                    bottomStart = 20.dp
                                                                )
                                                            ),
                                                        contentScale = ContentScale.Crop
                                                    )
                                                }

                                                // Contenido
                                                Column(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .fillMaxHeight()
                                                        .padding(
                                                            start = 14.dp,
                                                            top = 10.dp,
                                                            bottom = 10.dp,
                                                            end = 10.dp
                                                        ),
                                                    verticalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    // Título e información
                                                    Column(
                                                        verticalArrangement = Arrangement.spacedBy(5.dp)
                                                    ) {
                                                        Text(
                                                            text = anime.title,
                                                            fontFamily = PoppinsBold,
                                                            fontSize = 15.asp(),
                                                            color = MaterialTheme.colorScheme.onSurface,
                                                            maxLines = 2,
                                                            overflow = TextOverflow.Ellipsis,
                                                            lineHeight = 17.asp()
                                                        )

                                                        // Información adicional (tipo, año y score si existen)
                                                        if (!anime.typeAnime.isNullOrBlank() || !anime.year.isNullOrBlank() || anime.userScore > 0f) {
                                                            Row(
                                                                horizontalArrangement = Arrangement.spacedBy(
                                                                    6.dp
                                                                ),
                                                                verticalAlignment = Alignment.CenterVertically
                                                            ) {
                                                                if (!anime.typeAnime.isNullOrBlank()) {
                                                                    Text(
                                                                        text = anime.typeAnime
                                                                            ?: "",
                                                                        fontFamily = PoppinsRegular,
                                                                        fontSize = 11.asp(),
                                                                        color = MaterialTheme.colorScheme.onSurface.copy(
                                                                            alpha = 0.6f
                                                                        )
                                                                    )
                                                                }
                                                                if (!anime.typeAnime.isNullOrBlank() && !anime.year.isNullOrBlank()) {
                                                                    Text(
                                                                        text = "•",
                                                                        fontFamily = PoppinsRegular,
                                                                        fontSize = 11.asp(),
                                                                        color = MaterialTheme.colorScheme.onSurface.copy(
                                                                            alpha = 0.4f
                                                                        )
                                                                    )
                                                                }
                                                                if (!anime.year.isNullOrBlank()) {
                                                                    Text(
                                                                        text = anime.year ?: "",
                                                                        fontFamily = PoppinsRegular,
                                                                        fontSize = 11.asp(),
                                                                        color = MaterialTheme.colorScheme.onSurface.copy(
                                                                            alpha = 0.6f
                                                                        )
                                                                    )
                                                                }
                                                                if (anime.userScore > 0f) {
                                                                    if (!anime.typeAnime.isNullOrBlank() || !anime.year.isNullOrBlank()) {
                                                                        Text(
                                                                            text = "•",
                                                                            fontFamily = PoppinsRegular,
                                                                            fontSize = 11.asp(),
                                                                            color = MaterialTheme.colorScheme.onSurface.copy(
                                                                                alpha = 0.4f
                                                                            )
                                                                        )
                                                                    }
                                                                    Row(
                                                                        horizontalArrangement = Arrangement.spacedBy(
                                                                            3.dp
                                                                        ),
                                                                        verticalAlignment = Alignment.CenterVertically
                                                                    ) {
                                                                        Icon(
                                                                            imageVector = Icons.Default.Star,
                                                                            contentDescription = null,
                                                                            tint = Color(0xFFFFD700),
                                                                            modifier = Modifier.size(
                                                                                11.dp
                                                                            )
                                                                        )
                                                                        Text(
                                                                            text = String.format(
                                                                                "%.1f",
                                                                                anime.userScore
                                                                            ),
                                                                            fontFamily = PoppinsBold,
                                                                            fontSize = 11.asp(),
                                                                            color = MaterialTheme.colorScheme.onSurface.copy(
                                                                                alpha = 0.7f
                                                                            )
                                                                        )
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        // Progreso de episodios
                                                        Row(
                                                            horizontalArrangement = Arrangement.spacedBy(
                                                                6.dp
                                                            ),
                                                            verticalAlignment = Alignment.CenterVertically
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Default.PlayArrow,
                                                                contentDescription = null,
                                                                modifier = Modifier.size(14.dp),
                                                                tint = MaterialTheme.colorScheme.primary
                                                            )
                                                            Text(
                                                                text = "${anime.episodesWatched}/${anime.totalEpisodes}",
                                                                fontFamily = PoppinsBold,
                                                                fontSize = 12.asp(),
                                                                color = MaterialTheme.colorScheme.onSurface
                                                            )
                                                            Text(
                                                                text = "eps",
                                                                fontFamily = PoppinsRegular,
                                                                fontSize = 11.asp(),
                                                                color = MaterialTheme.colorScheme.onSurface.copy(
                                                                    alpha = 0.6f
                                                                )
                                                            )
                                                        }

                                                        // Progress bar
                                                        Box(
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .height(6.dp)
                                                                .clip(RoundedCornerShape(3.dp))
                                                                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                                                        ) {
                                                            Box(
                                                                modifier = Modifier
                                                                    .fillMaxHeight()
                                                                    .fillMaxWidth(
                                                                        if (anime.totalEpisodes > 0) {
                                                                            (anime.episodesWatched.toFloat() / anime.totalEpisodes.toFloat()).coerceIn(
                                                                                0f,
                                                                                1f
                                                                            )
                                                                        } else 0f
                                                                    )
                                                                    .clip(RoundedCornerShape(3.dp))
                                                                    .background(
                                                                        Brush.horizontalGradient(
                                                                            colors = listOf(
                                                                                MaterialTheme.colorScheme.primary,
                                                                                MaterialTheme.colorScheme.tertiary
                                                                            )
                                                                        )
                                                                    )
                                                            )
                                                        }
                                                    }

                                                    // Status chip y botón +1
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        // Status chip + priority badge
                                                        Row(
                                                            horizontalArrangement = Arrangement.spacedBy(
                                                                6.dp
                                                            ),
                                                            verticalAlignment = Alignment.CenterVertically
                                                        ) {
                                                            AnimeStatusChip(
                                                                status = anime.statusUser,
                                                                statusColor = getAnimeStatusColor(anime.statusUser),
                                                                onStatusSelected = { action ->
                                                                    viewModel.handleUserAction(
                                                                        anime.malId,
                                                                        action
                                                                    )
                                                                },
                                                                episodesWatched = anime.episodesWatched,
                                                                totalEpisodes = anime.totalEpisodes,
                                                                animeTitle = anime.title
                                                            )
                                                            if (anime.statusUser == "Planeado" && anime.plannedPriority != null) {
                                                                val priorityColor =
                                                                    when (anime.plannedPriority) {
                                                                        "Alta" -> Color(0xFFEF5350)
                                                                        "Media" -> Color(0xFFFFCA28)
                                                                        else -> Color(0xFF66BB6A)
                                                                    }
                                                                Surface(
                                                                    shape = RoundedCornerShape(20.dp),
                                                                    color = Color.Black.copy(alpha = 0.55f)
                                                                ) {
                                                                    Row(
                                                                        modifier = Modifier.padding(
                                                                            horizontal = 8.dp,
                                                                            vertical = 4.dp
                                                                        ),
                                                                        verticalAlignment = Alignment.CenterVertically,
                                                                        horizontalArrangement = Arrangement.spacedBy(
                                                                            4.dp
                                                                        )
                                                                    ) {
                                                                        Box(
                                                                            modifier = Modifier
                                                                                .size(6.dp)
                                                                                .background(
                                                                                    priorityColor,
                                                                                    CircleShape
                                                                                )
                                                                        )
                                                                        Text(
                                                                            text = anime.plannedPriority
                                                                                ?: "",
                                                                            fontFamily = PoppinsBold,
                                                                            fontSize = 10.asp(),
                                                                            color = Color.White
                                                                        )
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        // Botón +1
                                                        if (anime.statusUser == "Viendo" && anime.totalEpisodes > 0 && anime.episodesWatched < anime.totalEpisodes) {
                                                            Surface(
                                                                onClick = {
                                                                    val newEpisodesWatched =
                                                                        anime.episodesWatched + 1
                                                                    viewModel.updateEpisodesWatched(
                                                                        anime.malId,
                                                                        newEpisodesWatched
                                                                    )
                                                                },
                                                                shape = RoundedCornerShape(10.dp),
                                                                color = MaterialTheme.colorScheme.primaryContainer,
                                                                modifier = Modifier.size(32.adp())
                                                            ) {
                                                                Box(
                                                                    modifier = Modifier.fillMaxSize(),
                                                                    contentAlignment = Alignment.Center
                                                                ) {
                                                                    Icon(
                                                                        imageVector = Icons.Default.PlusOne,
                                                                        contentDescription = "Marcar episodio",
                                                                        modifier = Modifier.size(18.adp()),
                                                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            DeleteMyAnime(
                                                modifier = Modifier
                                                    .align(Alignment.TopEnd)
                                                    .padding(10.dp),
                                                onDeleteConfirmed = {
                                                    showDialog = true
                                                    animeIdToDelete = anime.malId
                                                }
                                            )
                                        }
                                    }
                                }

                                item {
                                    Spacer(Modifier.height(100.adp()))
                                }
                            }
                        }
                    }
                }
            }
        }
        }

        // SnackbarHost para mostrar el mensaje de completado
        androidx.compose.material3.SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 90.dp)
        ) { snackbarData ->
            androidx.compose.material3.Snackbar(
                snackbarData = snackbarData,
                containerColor = MaterialTheme.colorScheme.inverseSurface,
                contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                actionColor = MaterialTheme.colorScheme.inversePrimary,
                shape = RoundedCornerShape(12.dp)
            )
        }

        // Botón flotante de "volver arriba" - solo visible cuando hay contenido para mostrar
        if (!isLoading && savedAnimes.isNotEmpty()) {
            AnimatedVisibility(
                visible = showScrollToTopButton,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 }),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 90.dp)
            ) {
                Surface(
                    onClick = {
                        scope.launch {
                            when (viewMode) {
                                ViewMode.GRID -> gridState.animateScrollToItem(0)
                                ViewMode.CARD -> cardState.animateScrollToItem(0)
                                ViewMode.LIST -> listState.animateScrollToItem(0)
                            }
                        }
                    },
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shadowElevation = 6.dp,
                    tonalElevation = 3.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Volver arriba",
                            fontFamily = PoppinsBold,
                            fontSize = 13.asp(),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }

    // Bottom sheet de filtros avanzados
    AdvancedFiltersBottomSheet(
        isVisible = showAdvancedFilters,
        onDismiss = { showAdvancedFilters = false },
        currentFilters = activeFilters,
        onApplyFilters = { newFilters ->
            activeFilters = newFilters
            // Si hay filtros avanzados de estado, limpiar el filtro simple
            if (newFilters.statuses.isNotEmpty()) {
                selectedFilter = null
            }
        },
        availableGenres = viewModel.getAvailableGenres(),
        availableYears = viewModel.getAvailableYears()
    )

    // Modal de review al completar un anime
    showReviewDialog?.let { dialogState ->
        AnimeReviewDialog(
            onDismissRequest = { viewModel.dismissReviewDialog() },
            onSave = { score, opinion ->
                viewModel.updateAnimeReview(dialogState.animeId, score, opinion)
            },
            onSkip = {
                // No hacer nada, solo cerrar el modal
            },
            animeTitle = dialogState.animeTitle,
            currentScore = dialogState.currentScore,
            currentOpinion = dialogState.currentOpinion
        )
    }

    // Modal de confirmación de eliminación (disponible para todas las vistas)
    if (showDialog) {
        CustomDialog(
            onDismissRequest = {
                showDialog = false
            },
            onConfirm = {
                viewModel.deleteAnimeToList(animeIdToDelete)
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Anime eliminado de tu lista",
                        actionLabel = "Deshacer",
                        duration = SnackbarDuration.Long
                    )
                }
            },
            onDismiss = {
                // Solo cierra el diálogo
            },
            title = "Confirmar eliminación",
            message = "¿Estás seguro de que quieres eliminar este anime de tu lista?\n\nUna vez eliminado tendrás que volver a agregarlo de nuevo a tu lista.",
            confirmButtonText = "Eliminar",
            dismissButtonText = "Cancelar",
            type = DialogType.DELETE
        )
    }
}

@Composable
fun EmptyStateByFilter(
    selectedFilter: String?,
    onNavigateToSearch: () -> Unit
) {
    val (title, description) = when (selectedFilter) {
        "Viendo" -> Pair(
            "No estás viendo ningún anime",
            "Comienza a ver un anime marcándolo como 'Viendo' o busca nuevos títulos para agregar a tu lista."
        )
        "Completado" -> Pair(
            "Aún no has completado ningún anime",
            "Cuando termines de ver un anime aparecerá aquí. ¡Sigue disfrutando de tus series!"
        )
        "Pendiente" -> Pair(
            "No tienes animes pendientes",
            "Marca animes como 'Pendiente' cuando quieras verlos pero aún no sea el momento indicado."
        )
        "Abandonado" -> Pair(
            "No has abandonado ningún anime",
            "Los animes que decidas dejar de ver aparecerán aquí. ¡Esperemos que no sean muchos!"
        )
        "Planeado" -> Pair(
            "No tienes animes planeados",
            "Planea los animes que quieres ver próximamente y mantenlos organizados con prioridades."
        )
        else -> Pair(
            "No se encontraron resultados",
            "Intenta ajustar tu búsqueda o filtros para encontrar lo que buscas."
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título
            Text(
                text = title,
                fontFamily = PoppinsBold,
                fontSize = 22.asp(),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            // Descripción
            Text(
                text = description,
                fontFamily = PoppinsRegular,
                fontSize = 15.asp(),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                lineHeight = 22.asp()
            )
        }
    }
}

@Composable
fun SkeletonLoadingCard(viewMode: ViewMode) {
    val infiniteTransition = rememberInfiniteTransition(label = "skeleton")
    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer"
    )

    when (viewMode) {
        ViewMode.LIST -> {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.adp()),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .width(120.adp())
                            .fillMaxHeight()
                            .background(
                                MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = shimmerAlpha)
                            )
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(16.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = shimmerAlpha)
                                )
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .height(12.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = shimmerAlpha)
                                )
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(28.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(
                                        MaterialTheme.colorScheme.surfaceContainerHighest.copy(
                                            alpha = shimmerAlpha
                                        )
                                    )
                            )
                            Box(
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(28.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        MaterialTheme.colorScheme.surfaceContainerHighest.copy(
                                            alpha = shimmerAlpha
                                        )
                                    )
                            )
                        }
                    }
                }
            }
        }

        ViewMode.GRID -> {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.adp()),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.adp())
                            .background(
                                MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = shimmerAlpha)
                            )
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = shimmerAlpha)
                                )
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .height(10.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = shimmerAlpha)
                                )
                        )
                    }
                }
            }
        }

        ViewMode.CARD -> {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.adp()),
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 12.dp,
                tonalElevation = 2.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = shimmerAlpha)
                        )
                )
            }
        }
    }
}

@Composable
fun CompactAnimeCard(
    anime: com.yumedev.seijakulist.data.local.entities.AnimeEntity,
    statusColor: Color,
    onAnimeClick: () -> Unit,
    onDeleteConfirmed: () -> Unit,
    viewModel: MyAnimeListViewModel,
    isGlowing: Boolean = false
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "compact_card_scale"
    )

    val glowAlpha by animateFloatAsState(
        targetValue = if (isGlowing) 1f else 0f,
        animationSpec = tween(300),
        label = "glow_alpha"
    )

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.adp())
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .then(
                if (isGlowing) Modifier
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(12.dp),
                        ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = glowAlpha * 0.5f),
                        spotColor = MaterialTheme.colorScheme.primary.copy(alpha = glowAlpha)
                    )
                    .border(
                        width = 3.dp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = glowAlpha),
                        shape = RoundedCornerShape(12.dp)
                    )
                else Modifier
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onAnimeClick() }
                )
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Imagen
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.adp())
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(anime.imageUrl),
                        contentDescription = anime.title,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                // Contenido inferior
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(8.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = anime.title,
                        fontFamily = PoppinsBold,
                        fontSize = 12.asp(),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 14.asp()
                    )

                    // Progreso y score
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${anime.episodesWatched}/${anime.totalEpisodes}",
                                fontFamily = PoppinsRegular,
                                fontSize = 10.asp(),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            if (anime.userScore > 0f) {
                                Text(
                                    text = "•",
                                    fontFamily = PoppinsRegular,
                                    fontSize = 10.asp(),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                )
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(10.dp)
                                )
                                Text(
                                    text = String.format("%.1f", anime.userScore),
                                    fontFamily = PoppinsBold,
                                    fontSize = 10.asp(),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }

                        Surface(
                            shape = CircleShape,
                            color = statusColor.copy(alpha = 0.2f),
                            modifier = Modifier.size(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(1.dp)
                                    .background(statusColor, CircleShape)
                            )
                        }
                    }
                }
            }

            // Botón de eliminar
            DeleteMyAnime(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(6.dp),
                onDeleteConfirmed = onDeleteConfirmed
            )
        }
    }
}

@Composable
fun OnboardingStyleAnimeCard(
    anime: com.yumedev.seijakulist.data.local.entities.AnimeEntity,
    statusColor: Color,
    onAnimeClick: () -> Unit,
    viewModel: MyAnimeListViewModel,
    isGlowing: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "card")

    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer"
    )

    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "onboarding_card_scale"
    )

    val glowAlpha by animateFloatAsState(
        targetValue = if (isGlowing) 1f else 0f,
        animationSpec = tween(300),
        label = "glow_alpha"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.adp())
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .then(
                if (isGlowing) Modifier
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(20.dp),
                        ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = glowAlpha * 0.5f),
                        spotColor = MaterialTheme.colorScheme.primary.copy(alpha = glowAlpha)
                    )
                    .border(
                        width = 3.dp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = glowAlpha),
                        shape = RoundedCornerShape(20.dp)
                    )
                else Modifier
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onAnimeClick() }
                )
            },
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 12.dp,
        tonalElevation = 2.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Imagen de fondo
            Image(
                painter = rememberAsyncImagePainter(anime.imageUrl),
                contentDescription = anime.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Degradado superior
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.5f),
                                Color.Transparent,
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.4f),
                                Color.Black.copy(alpha = 0.7f),
                                Color.Black.copy(alpha = 0.85f)
                            )
                        )
                    )
            )

            // Efecto de brillo diagonal
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.adp())
                    .align(Alignment.TopStart)
                    .graphicsLayer {
                        rotationZ = -45f
                        alpha = shimmerAlpha * 0.2f
                    }
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.6f),
                                Color.Transparent
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tipo de anime en texto plano
                    if (!anime.typeAnime.isNullOrBlank()) {
                        Text(
                            text = anime.typeAnime ?: "",
                            fontFamily = PoppinsBold,
                            fontSize = 10.asp(),
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }

                    // Score en texto plano
                    if (anime.userScore > 0f) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = String.format("%.1f", anime.userScore),
                                fontFamily = PoppinsBold,
                                fontSize = 12.asp(),
                                color = Color.White
                            )
                        }
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Título
                    Text(
                        text = anime.title,
                        fontFamily = PoppinsBold,
                        fontSize = 15.asp(),
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 17.asp()
                    )

                    // Año si existe
                    if (!anime.year.isNullOrBlank()) {
                        Text(
                            text = anime.year ?: "",
                            fontFamily = PoppinsRegular,
                            fontSize = 11.asp(),
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }

                    // Estado con AnimeStatusChip y episodios
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AnimeStatusChip(
                            status = anime.statusUser,
                            statusColor = statusColor,
                            onStatusSelected = { action ->
                                viewModel.handleUserAction(anime.malId, action)
                            },
                            episodesWatched = anime.episodesWatched,
                            totalEpisodes = anime.totalEpisodes,
                            animeTitle = anime.title
                        )

                        // Episodios
                        Text(
                            text = "${anime.episodesWatched}/${anime.totalEpisodes} eps",
                            fontFamily = PoppinsBold,
                            fontSize = 11.asp(),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
package com.yumedev.seijakulist.ui.screens.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yumedev.seijakulist.domain.models.Anime
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.components.CardAnimesHome
import com.yumedev.seijakulist.ui.components.CardAnimesHomeLoading
import com.yumedev.seijakulist.ui.screens.home.FilterAnimesHome
import com.yumedev.seijakulist.ui.components.LoadingScreen
import com.yumedev.seijakulist.ui.components.MangaPlaceholder
import com.yumedev.seijakulist.ui.components.NoInternetScreen
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsMedium
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import kotlinx.coroutines.delay


// HomeScreen con UI mejorada
@Composable
fun HomeScreen(
    navController: NavController,
    seasonNowViewModel: AnimeSeasonNowViewModel = hiltViewModel(),
    topAnimesViewModel: TopAnimeViewModel = hiltViewModel(),
    seasonUpcomingViewModel: AnimeSeasonUpcomingViewModel = hiltViewModel(),
    animeRandomViewModel: AnimeRandomViewModel = hiltViewModel(),
    characterRandomViewModel: CharacterRandomViewModel = hiltViewModel(),
    animeScheduleViewModel: AnimeScheduleViewModel = hiltViewModel(),
    animeFilterViewModel: TopAnimeFilterViewModel = hiltViewModel(),
    seasonUpcomingFilterViewModel: AnimeSeasonUpcomingFilterViewModel = hiltViewModel(),
    profileViewModel: com.yumedev.seijakulist.ui.screens.profile.ProfileViewModel = hiltViewModel()
) {
    // Estados
    val animeSeasonNow by seasonNowViewModel.animeList.collectAsState()
    val animeSeasonNowIsLoading by seasonNowViewModel.isLoading.collectAsState()
    val animeSeasonNowError by seasonNowViewModel.isError.collectAsState()

    val topAnimes by topAnimesViewModel.animeList.collectAsState()
    val topAnimeError by topAnimesViewModel.isError.collectAsState()

    val animeSeasonUpcoming by seasonUpcomingViewModel.animeList.collectAsState()
    val animeSeasonUpcomingIsLoading by seasonUpcomingViewModel.isLoading.collectAsState()
    val animeSeasonUpcomingError by seasonUpcomingViewModel.isError.collectAsState()

    val animeSchedule by animeScheduleViewModel.animeList.collectAsState()
    val animeScheduleIsLoading by animeScheduleViewModel.isLoading.collectAsState()

    val topAnimeFilter by animeFilterViewModel.animeList.collectAsState()
    val topAnimeFilterIsLoading by animeFilterViewModel.isLoading.collectAsState()

    val animeSeasonUpcomingFilter by seasonUpcomingFilterViewModel.animeList.collectAsState()
    val animeSeasonUpcomingFilterIsLoading by seasonUpcomingFilterViewModel.isLoading.collectAsState()

    // Estados de anime random y perfil
    val animeRandomState by animeRandomViewModel.uiState.collectAsState()
    val profileUiState by profileViewModel.uiState.collectAsState()

    val hasError = animeSeasonNowError || topAnimeError || animeSeasonUpcomingError

    val listTab = listOf("Anime", "Manga")
    val selectedTab = remember { mutableStateOf(listTab[0]) }

    val listDays = listOf("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday")
    val listDaysFilter = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")

    val listTypeAnime = listOf("tv", "movie", "ova", "special", "ona", "music", "cm", "pv", "tv_special")
    val listTypeAnimeFilter = listOf("TV", "Película", "OVA", "Especial", "ONA", "Música", "CM", "PV", "TV Especial")

    val listTypeSeasonUpcoming = listOf("tv", "movie", "ova", "special", "ona", "music")
    val listTypeSeasonUpcomingFilter = listOf("TV", "Película", "OVA", "Especial", "ONA", "Música")

    var selectedDayFilter by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedTypeFilter by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedUpcomingFilter by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(selectedDayFilter) {
        selectedDayFilter?.let { animeScheduleViewModel.AnimeSchedule(it) }
    }

    LaunchedEffect(selectedTypeFilter) {
        selectedTypeFilter?.let { animeFilterViewModel.TopAnimeFilter(it) }
    }

    LaunchedEffect(selectedUpcomingFilter) {
        selectedUpcomingFilter?.let { seasonUpcomingFilterViewModel.AnimeSeasonUpcomingFilter(it) }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (hasError) {
            NoInternetScreen(
                onRetryClick = {
                    topAnimesViewModel.topAnime()
                    seasonNowViewModel.AnimesSeasonNow()
                    seasonUpcomingViewModel.AnimesSeasonUpcoming()
                    animeRandomViewModel.loadRandomAnime()
                    characterRandomViewModel.loadCharacterRandom()
                }
            )
        } else {
            if (animeSeasonUpcomingIsLoading) {
                LoadingScreen()
            } else if (animeSeasonNow.isNotEmpty()) {
                HomeTabSection(
                    selectedTab = selectedTab.value,
                    tabs = listTab,
                    onTabSelected = { selectedTab.value = it }
                )

                when (selectedTab.value) {
                    "Anime" -> {
                        AnimeContent(
                            animeSeasonNow = animeSeasonNow,
                            topAnimes = topAnimes,
                            animeSeasonUpcoming = animeSeasonUpcoming,
                            animeSchedule = animeSchedule,
                            topAnimeFilter = topAnimeFilter,
                            animeSeasonUpcomingFilter = animeSeasonUpcomingFilter,
                            animeScheduleIsLoading = animeScheduleIsLoading,
                            topAnimeFilterIsLoading = topAnimeFilterIsLoading,
                            animeSeasonUpcomingFilterIsLoading = animeSeasonUpcomingFilterIsLoading,
                            listDays = listDays,
                            listDaysFilter = listDaysFilter,
                            listTypeAnime = listTypeAnime,
                            listTypeAnimeFilter = listTypeAnimeFilter,
                            listTypeSeasonUpcoming = listTypeSeasonUpcoming,
                            listTypeSeasonUpcomingFilter = listTypeSeasonUpcomingFilter,
                            selectedDayFilter = selectedDayFilter,
                            selectedTypeFilter = selectedTypeFilter,
                            selectedUpcomingFilter = selectedUpcomingFilter,
                            onDayFilterSelected = { selectedDayFilter = it },
                            onTypeFilterSelected = { selectedTypeFilter = it },
                            onUpcomingFilterSelected = { selectedUpcomingFilter = it },
                            animeRandomState = animeRandomState,
                            profileUiState = profileUiState,
                            navController = navController
                        )
                    }
                    "Manga" -> {
                        MangaPlaceholder()
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeTabSection(
    selectedTab: String,
    tabs: List<String>,
    onTabSelected: (String) -> Unit
) {
    val hapticFeedback = androidx.compose.ui.platform.LocalHapticFeedback.current

    // Detectar dark mode comparando la luminancia del color de surface
    val surfaceColor = MaterialTheme.colorScheme.surface
    val isDarkTheme = surfaceColor.luminance() < 0.5f

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        tabs.forEach { tab ->
            val isSelected = tab == selectedTab

            // InteractionSource para detectar presiones
            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()

            // Animación de escala al presionar
            val scale by animateFloatAsState(
                targetValue = if (isPressed) 0.95f else 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                ),
                label = "tab_scale_animation"
            )

            // Animación de opacidad al cambiar tabs
            val alpha by animateFloatAsState(
                targetValue = if (isSelected) 1f else 0.85f,
                animationSpec = tween(durationMillis = 300),
                label = "tab_alpha_animation"
            )

            // Animación de color de fondo
            val backgroundColor by androidx.compose.animation.animateColorAsState(
                targetValue = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    if (isDarkTheme) {
                        MaterialTheme.colorScheme.surfaceContainerHigh
                    } else {
                        Color.White
                    }
                },
                animationSpec = tween(durationMillis = 300),
                label = "tab_background_animation"
            )

            // Animación de color de texto
            val textColor by androidx.compose.animation.animateColorAsState(
                targetValue = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                },
                animationSpec = tween(durationMillis = 300),
                label = "tab_text_animation"
            )

            androidx.compose.material3.Surface(
                modifier = Modifier
                    .weight(1f)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                    },
                onClick = {
                    if (!isSelected) {
                        // Feedback háptico al cambiar de tab
                        hapticFeedback.performHapticFeedback(
                            androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress
                        )
                        onTabSelected(tab)
                    }
                },
                shape = RoundedCornerShape(16.dp),
                color = backgroundColor,
                border = if (isSelected) {
                    androidx.compose.foundation.BorderStroke(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    if (!isDarkTheme) {
                        androidx.compose.foundation.BorderStroke(
                            width = 1.dp,
                            color = Color.Black.copy(alpha = 0.2f)
                        )
                    } else {
                        null
                    }
                },
                interactionSource = interactionSource
            ) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tab,
                        fontFamily = PoppinsBold,
                        fontSize = 16.sp,
                        color = textColor
                    )
                }
            }
        }
    }
}

@Composable
private fun AnimeContent(
    animeSeasonNow: List<Any>,
    topAnimes: List<Any>,
    animeSeasonUpcoming: List<Any>,
    animeSchedule: List<Any>,
    topAnimeFilter: List<Any>,
    animeSeasonUpcomingFilter: List<Any>,
    animeScheduleIsLoading: Boolean,
    topAnimeFilterIsLoading: Boolean,
    animeSeasonUpcomingFilterIsLoading: Boolean,
    listDays: List<String>,
    listDaysFilter: List<String>,
    listTypeAnime: List<String>,
    listTypeAnimeFilter: List<String>,
    listTypeSeasonUpcoming: List<String>,
    listTypeSeasonUpcomingFilter: List<String>,
    selectedDayFilter: String?,
    selectedTypeFilter: String?,
    selectedUpcomingFilter: String?,
    onDayFilterSelected: (String?) -> Unit,
    onTypeFilterSelected: (String?) -> Unit,
    onUpcomingFilterSelected: (String?) -> Unit,
    animeRandomState: AnimeRandomUiState,
    profileUiState: com.yumedev.seijakulist.ui.screens.profile.ProfileUiState,
    navController: NavController
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Quick Stats PRIMERO (solo si tiene animes guardados)
        profileUiState.allSavedAnimes.takeIf { it.isNotEmpty() }?.let { savedAnimes ->
            item {
                QuickStats(
                    stats = profileUiState.stats,
                    recentAnimes = savedAnimes.filter { it.statusUser == "Viendo" }.take(3),
                    navController = navController
                )
            }
        }

        // Banner Hero con anime aleatorio DESPUÉS
        item {
            if (animeRandomState.isLoading) {
                HeroBannerSkeleton()
            } else {
                animeRandomState.anime?.let { randomAnime ->
                    HeroBanner(
                        anime = randomAnime,
                        navController = navController
                    )
                }
            }
        }

        // Sección En Emisión
        item {
            AnimeSectionWithFilter(
                title = "En emisión",
                icon = Icons.Default.Tv,
                filters = listDays,
                filterLabels = listDaysFilter,
                selectedFilter = selectedDayFilter,
                onFilterSelected = onDayFilterSelected,
                isLoading = animeScheduleIsLoading,
                animeList = if (selectedDayFilter != null) animeSchedule else animeSeasonNow,
                emptyMessage = "No hay animes programados para este día.",
                navController = navController,
                onViewMoreClick = {
                    navController.navigate("${AppDestinations.VIEW_MORE_ROUTE}/season_now")
                }
            )
        }

        // Sección Top Puntuación
        item {
            AnimeSectionWithFilter(
                title = "Top puntuación",
                icon = Icons.Default.Star,
                filters = listTypeAnime,
                filterLabels = listTypeAnimeFilter,
                selectedFilter = selectedTypeFilter,
                onFilterSelected = onTypeFilterSelected,
                isLoading = topAnimeFilterIsLoading,
                animeList = if (selectedTypeFilter != null) topAnimeFilter else topAnimes,
                emptyMessage = "No se encontraron animes para el filtro seleccionado.",
                navController = navController,
                onViewMoreClick = {
                    navController.navigate("${AppDestinations.VIEW_MORE_ROUTE}/top_anime")
                }
            )
        }

        // Sección Próxima Temporada
        item {
            AnimeSectionWithFilter(
                title = "Próxima temporada",
                icon = Icons.Default.CalendarMonth,
                filters = listTypeSeasonUpcoming,
                filterLabels = listTypeSeasonUpcomingFilter,
                selectedFilter = selectedUpcomingFilter,
                onFilterSelected = onUpcomingFilterSelected,
                isLoading = animeSeasonUpcomingFilterIsLoading,
                animeList = if (selectedUpcomingFilter != null) animeSeasonUpcomingFilter else animeSeasonUpcoming,
                emptyMessage = "No se encontraron animes para el filtro seleccionado.",
                navController = navController,
                onViewMoreClick = {
                    navController.navigate("${AppDestinations.VIEW_MORE_ROUTE}/season_upcoming")
                }
            )
        }
        item {
            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
private fun AnimeSectionWithFilter(
    title: String,
    icon: ImageVector,
    filters: List<String>,
    filterLabels: List<String>,
    selectedFilter: String?,
    onFilterSelected: (String?) -> Unit,
    isLoading: Boolean,
    animeList: List<Any>,
    emptyMessage: String,
    navController: NavController,
    onViewMoreClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header con diseño mejorado
        AnimeSectionHeader(
            title = title,
            icon = icon,
            onViewMoreClick = onViewMoreClick
        )

        // Filtros con animación
        androidx.compose.animation.AnimatedVisibility(
            visible = filters.isNotEmpty(),
            enter = androidx.compose.animation.fadeIn() + androidx.compose.animation.expandVertically(),
            exit = androidx.compose.animation.fadeOut() + androidx.compose.animation.shrinkVertically()
        ) {
            FilterAnimesHome(
                list = filters,
                listLabel = filterLabels,
                selectedFilter = selectedFilter,
                onFilterSelected = onFilterSelected
            )
        }

        // Contenido con transiciones animadas
        androidx.compose.animation.AnimatedContent(
            targetState = when {
                isLoading -> ContentState.Loading
                animeList.isNotEmpty() -> ContentState.Content
                else -> ContentState.Empty
            },
            transitionSpec = {
                androidx.compose.animation.fadeIn(
                    animationSpec = androidx.compose.animation.core.tween(300)
                ) togetherWith androidx.compose.animation.fadeOut(
                    animationSpec = androidx.compose.animation.core.tween(300)
                )
            },
            label = "anime_content_transition"
        ) { state ->
            when (state) {
                ContentState.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        CardAnimesHomeLoading()
                    }
                }
                ContentState.Content -> {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        CardAnimesHome(animeList as List<Anime>, navController)
                    }
                }
                ContentState.Empty -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        EnhancedEmptyState(emptyMessage)
                    }
                }
            }
        }
    }
}

// Estado del contenido para mejor type-safety
private enum class ContentState {
    Loading, Content, Empty
}

// Header mejorado con diseño profesional
@Composable
private fun AnimeSectionHeader(
    title: String,
    icon: ImageVector,
    onViewMoreClick: () -> Unit
) {
    androidx.compose.material3.Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Título con icono y efecto visual
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Contenedor del icono con fondo
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Título con estilo mejorado
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 21.sp,
                    fontFamily = PoppinsBold,
                    letterSpacing = 0.3.sp
                )
            }

            // Botón "Ver más" mejorado
            androidx.compose.material3.FilledTonalButton(
                onClick = onViewMoreClick,
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Ver más",
                    fontSize = 13.sp,
                    fontFamily = PoppinsMedium
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
}

// Header legacy (mantener para compatibilidad)
@Composable
private fun SectionHeader(
    title: String,
    icon: ImageVector,
    onViewMoreClick: () -> Unit
) = AnimeSectionHeader(title, icon, onViewMoreClick)

@Composable
fun FilterAnimesHome(
    list: List<String>,
    listLabel: List<String>,
    selectedFilter: String?,
    onFilterSelected: (String?) -> Unit
) {
    val scrollState = rememberScrollState()
    // Detectar dark mode comparando la luminancia del color de surface
    val surfaceColor = MaterialTheme.colorScheme.surface
    val isDarkTheme = surfaceColor.luminance() < 0.5f

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        list.forEachIndexed { index, filter ->
            val isSelected = selectedFilter == filter

            FilterChip(
                selected = isSelected,
                onClick = {
                    onFilterSelected(if (isSelected) null else filter)
                },
                label = {
                    Text(
                        text = listLabel[index],
                        fontFamily = if (isSelected) PoppinsBold else PoppinsRegular,
                        fontSize = 14.sp
                    )
                },
                leadingIcon = if (isSelected) {
                    {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                } else null,
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = if (isDarkTheme) {
                        MaterialTheme.colorScheme.surfaceContainerHigh
                    } else {
                        Color.White
                    },
                    labelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    iconColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = if (isSelected) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    } else {
                        if (isDarkTheme) {
                            Color.Transparent
                        } else {
                            Color.Black.copy(alpha = 0.2f)
                        }
                    },
                    selectedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    borderWidth = if (!isSelected && !isDarkTheme) 1.dp else 1.dp,
                    selectedBorderWidth = 1.dp
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

// Estado vacío mejorado con diseño profesional
@Composable
private fun EnhancedEmptyState(message: String) {
    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Icono con diseño circular
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                    modifier = Modifier.size(28.dp)
                )
            }

            // Mensaje
            Text(
                text = message,
                fontFamily = PoppinsRegular,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                lineHeight = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

// Mensaje vacío legacy (mantener para compatibilidad)
@Composable
private fun EmptyStateMessage(message: String) = EnhancedEmptyState(message)

// Objeto para controlar si ya se animó la primera vez
private object HomeAnimationState {
    var hasAnimated = false
}

// Banner Hero Mejorado - Anime del día (diseño compacto)
@Composable
private fun HeroBanner(
    anime: com.yumedev.seijakulist.domain.models.AnimeCard,
    navController: NavController
) {
    val shouldAnimate = !HomeAnimationState.hasAnimated
    var isVisible by remember { mutableStateOf(!shouldAnimate) }
    val interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = androidx.compose.animation.core.spring(
            dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
            stiffness = androidx.compose.animation.core.Spring.StiffnessLow
        ),
        label = "hero_banner_scale"
    )

    LaunchedEffect(shouldAnimate) {
        if (shouldAnimate) {
            HomeAnimationState.hasAnimated = true
            kotlinx.coroutines.delay(100)
            isVisible = true
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(500)) +
                scaleIn(
                    initialScale = 0.95f,
                    animationSpec = tween(500, easing = FastOutSlowInEasing)
                )
    ) {
        androidx.compose.material3.Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(horizontal = 16.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
            shape = RoundedCornerShape(20.dp),
            elevation = androidx.compose.material3.CardDefaults.cardElevation(
                defaultElevation = 4.dp,
                pressedElevation = 2.dp
            ),
            onClick = { navController.navigate("${AppDestinations.ANIME_DETAIL_ROUTE}/${anime.malId}") },
            interactionSource = interactionSource
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                // Imagen del lado izquierdo
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(130.dp)
                ) {
                    coil.compose.AsyncImage(
                        model = anime.images,
                        contentDescription = anime.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                // Contenido del lado derecho
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Título y metadata
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Anime random",
                                fontFamily = PoppinsMedium,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.primary,
                                letterSpacing = 1.sp
                            )

                            Text(
                                text = anime.title,
                                fontFamily = PoppinsBold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 2,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                                lineHeight = 20.sp
                            )

                            // Año y status
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                anime.year.takeIf { it.isNotEmpty() && it != "null" }?.let { year ->
                                    Text(
                                        text = year,
                                        fontFamily = PoppinsRegular,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                }

                                anime.status.takeIf { it.isNotEmpty() }?.let { status ->
                                    Text(
                                        text = "•  $status",
                                        fontFamily = PoppinsRegular,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                        maxLines = 1
                                    )
                                }
                            }
                        }

                        // Score destacado
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "${anime.score}",
                                fontFamily = PoppinsBold,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "/ 10",
                                fontFamily = PoppinsRegular,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }
        }
    }
}

// Skeleton del Banner Hero (diseño horizontal)
@Composable
private fun HeroBannerSkeleton() {
    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Skeleton de la imagen (lado izquierdo)
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(130.dp)
                    .background(
                        shimmerBrush(
                            targetValue = 1000f,
                            showShimmer = true
                        )
                    )
            )

            // Skeleton del contenido (lado derecho)
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Skeleton superior (título y metadata)
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Label skeleton
                        Box(
                            modifier = Modifier
                                .width(90.dp)
                                .height(14.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    shimmerBrush(
                                        targetValue = 1000f,
                                        showShimmer = true
                                    )
                                )
                        )

                        // Título skeleton
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .height(20.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    shimmerBrush(
                                        targetValue = 1000f,
                                        showShimmer = true
                                    )
                                )
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .height(18.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    shimmerBrush(
                                        targetValue = 1000f,
                                        showShimmer = true
                                    )
                                )
                        )

                        // Metadata skeleton
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier
                                    .width(50.dp)
                                    .height(14.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        shimmerBrush(
                                            targetValue = 1000f,
                                            showShimmer = true
                                        )
                                    )
                            )
                            Box(
                                modifier = Modifier
                                    .width(60.dp)
                                    .height(14.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        shimmerBrush(
                                            targetValue = 1000f,
                                            showShimmer = true
                                        )
                                    )
                            )
                        }
                    }

                    // Score skeleton (inferior)
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(24.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                shimmerBrush(
                                    targetValue = 1000f,
                                    showShimmer = true
                                )
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun shimmerBrush(targetValue: Float = 1000f, showShimmer: Boolean = true): Brush {
    return if (showShimmer) {
        val shimmerColors = listOf(
            MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.9f),
            MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.3f),
            MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.9f)
        )

        val transition = androidx.compose.animation.core.rememberInfiniteTransition(label = "shimmer")
        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = targetValue,
            animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                animation = androidx.compose.animation.core.tween(
                    durationMillis = 1200,
                    easing = androidx.compose.animation.core.FastOutSlowInEasing
                ),
                repeatMode = androidx.compose.animation.core.RepeatMode.Restart
            ),
            label = "shimmerTranslate"
        )

        Brush.linearGradient(
            colors = shimmerColors,
            start = androidx.compose.ui.geometry.Offset.Zero,
            end = androidx.compose.ui.geometry.Offset(x = translateAnimation.value, y = translateAnimation.value)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color.Transparent, Color.Transparent),
            start = androidx.compose.ui.geometry.Offset.Zero,
            end = androidx.compose.ui.geometry.Offset.Zero
        )
    }
}

// Quick Stats Mejorado - Tu progreso
@Composable
private fun QuickStats(
    stats: com.yumedev.seijakulist.ui.screens.profile.AnimeStats,
    recentAnimes: List<com.yumedev.seijakulist.data.local.entities.AnimeEntity>,
    navController: NavController
) {
    val shouldAnimate = !HomeAnimationState.hasAnimated
    var isVisible by remember { mutableStateOf(!shouldAnimate) }
    var isExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(shouldAnimate) {
        if (shouldAnimate) {
            kotlinx.coroutines.delay(200)
            isVisible = true
        }
    }

    val rotationAngle by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(300),
        label = "Arrow rotation"
    )

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(600)) +
                androidx.compose.animation.slideInVertically(
                    initialOffsetY = { 30 },
                    animationSpec = tween(600, easing = androidx.compose.animation.core.FastOutSlowInEasing)
                )
    ) {
        androidx.compose.material3.Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = androidx.compose.material3.CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            ),
            elevation = androidx.compose.material3.CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header mejorado
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Icono con fondo
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "Tu progreso",
                                fontFamily = PoppinsBold,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                letterSpacing = 0.3.sp
                            )
                            Text(
                                text = "Resumen de actividad",
                                fontFamily = PoppinsRegular,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }

                    // Botón de expandir/contraer
                    androidx.compose.material3.IconButton(
                        onClick = { isExpanded = !isExpanded },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = if (isExpanded) "Contraer" else "Expandir",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(24.dp)
                                .rotate(rotationAngle)
                        )
                    }
                }

                // Contenido desplegable
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically(animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)),
                    exit = shrinkVertically(animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        // Stats Cards con animación escalonada
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            EnhancedStatCard(
                                modifier = Modifier.weight(1f),
                                value = "${stats.totalAnimes}",
                                label = "Total",
                                delay = 100
                            )
                            EnhancedStatCard(
                                modifier = Modifier.weight(1f),
                                value = "${stats.completedAnimes}",
                                label = "Completos",
                                delay = 200
                            )
                            EnhancedStatCard(
                                modifier = Modifier.weight(1f),
                                value = "${stats.totalEpisodesWatched}",
                                label = "Episodios",
                                delay = 300
                            )
                        }

                        // Continúa viendo con diseño mejorado
                        if (recentAnimes.isNotEmpty()) {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Continúa viendo",
                                        fontFamily = PoppinsBold,
                                        fontSize = 15.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${recentAnimes.size} animes",
                                        fontFamily = PoppinsRegular,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    recentAnimes.take(3).forEach { anime ->
                                        EnhancedContinueWatchingCard(
                                            anime = anime,
                                            navController = navController,
                                            modifier = Modifier.weight(1f)
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
}

// Card de estadística mejorada con animación
@Composable
private fun EnhancedStatCard(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    delay: Int = 0
) {
    val shouldAnimate = !HomeAnimationState.hasAnimated
    var isVisible by remember { mutableStateOf(!shouldAnimate) }

    LaunchedEffect(shouldAnimate) {
        if (shouldAnimate) {
            kotlinx.coroutines.delay(delay.toLong())
            isVisible = true
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(400)) +
                scaleIn(
                    initialScale = 0.9f,
                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                ),
        modifier = modifier
    ) {
        androidx.compose.material3.Card(
            modifier = Modifier.height(70.dp),
            shape = RoundedCornerShape(14.dp),
            colors = androidx.compose.material3.CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ),
            elevation = androidx.compose.material3.CardDefaults.cardElevation(
                defaultElevation = 1.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = label,
                    fontFamily = PoppinsRegular,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    letterSpacing = 0.3.sp
                )

                Text(
                    text = value,
                    fontFamily = PoppinsBold,
                    fontSize = 26.sp,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 0.sp,
                    lineHeight = 26.sp
                )
            }
        }
    }
}

// Card legacy (mantener compatibilidad)
@Composable
private fun QuickStatCard(
    modifier: Modifier = Modifier,
    value: String,
    label: String
) = EnhancedStatCard(modifier, value, label, 0)

// Card mejorada de "Continúa viendo"
@Composable
private fun EnhancedContinueWatchingCard(
    anime: com.yumedev.seijakulist.data.local.entities.AnimeEntity,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = androidx.compose.animation.core.spring(
            dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
            stiffness = androidx.compose.animation.core.Spring.StiffnessMedium
        ),
        label = "continue_watching_scale"
    )

    androidx.compose.material3.Card(
        modifier = modifier
            .height(140.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        shape = RoundedCornerShape(16.dp),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        onClick = { navController.navigate("${AppDestinations.ANIME_DETAIL_LOCAL_ROUTE}/${anime.malId}") },
        interactionSource = interactionSource
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Imagen con overlay
            coil.compose.AsyncImage(
                model = anime.imageUrl,
                contentDescription = anime.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradiente mejorado
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.4f),
                                Color.Black.copy(alpha = 0.9f)
                            ),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )

            // Indicador de progreso circular en la esquina
            val progress = if (anime.totalEpisodes > 0) {
                anime.episodesWatched.toFloat() / anime.totalEpisodes.toFloat()
            } else 0f

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                androidx.compose.material3.CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.size(28.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 3.dp,
                    trackColor = Color.White.copy(alpha = 0.3f)
                )
            }

            // Información
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = anime.title,
                    fontFamily = PoppinsBold,
                    fontSize = 12.sp,
                    color = Color.White,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                    lineHeight = 16.sp,
                    style = androidx.compose.ui.text.TextStyle(
                        shadow = androidx.compose.ui.graphics.Shadow(
                            color = Color.Black.copy(alpha = 0.5f),
                            offset = Offset(0f, 1f),
                            blurRadius = 3f
                        )
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Barra de progreso lineal
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(progress)
                                .clip(RoundedCornerShape(3.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                                        )
                                    )
                                )
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Ep. ${anime.episodesWatched}/${anime.totalEpisodes}",
                            fontFamily = PoppinsMedium,
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.95f)
                        )
                        Text(
                            text = "${(progress * 100).toInt()}%",
                            fontFamily = PoppinsBold,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

// Card legacy (mantener compatibilidad)
@Composable
private fun ContinueWatchingCard(
    anime: com.yumedev.seijakulist.data.local.entities.AnimeEntity,
    navController: NavController,
    modifier: Modifier = Modifier
) = EnhancedContinueWatchingCard(anime, navController, modifier)
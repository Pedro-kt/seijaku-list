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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import android.content.Context
import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.draw.shadow
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yumedev.seijakulist.domain.models.Anime
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.components.CardAnimesHome
import com.yumedev.seijakulist.ui.components.CardAnimesHomeLoading
import com.yumedev.seijakulist.ui.components.CURRENT_WHATS_NEW
import com.yumedev.seijakulist.ui.components.WhatsNewBanner
import com.yumedev.seijakulist.ui.screens.home.FilterAnimesHome
import com.yumedev.seijakulist.ui.components.LoadingScreen
import com.yumedev.seijakulist.ui.components.MangaPlaceholder
import com.yumedev.seijakulist.ui.components.NoInternetScreen
import com.yumedev.seijakulist.ui.screens.profile.CustomSeijakuTabSelector
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
    //animeRandomViewModel: AnimeRandomViewModel = hiltViewModel(),
    //characterRandomViewModel: CharacterRandomViewModel = hiltViewModel(),
    animeScheduleViewModel: AnimeScheduleViewModel = hiltViewModel(),
    animeFilterViewModel: TopAnimeFilterViewModel = hiltViewModel(),
    seasonUpcomingFilterViewModel: AnimeSeasonUpcomingFilterViewModel = hiltViewModel(),
    profileViewModel: com.yumedev.seijakulist.ui.screens.profile.ProfileViewModel = hiltViewModel(),
    localAnimeIdsViewModel: LocalAnimeIdsViewModel = hiltViewModel(),
    heroCarouselViewModel: HeroCarouselViewModel = hiltViewModel()
) {
    // ── Banner "¿Qué hay de nuevo?" — una sola vez por versionCode ──────────
    val context = LocalContext.current
    var showWhatsNewBanner by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val prefs = context.getSharedPreferences("seijaku_prefs", Context.MODE_PRIVATE)
        val currentVersionCode = currentVersionCode(context)
        val lastShownVersion = prefs.getInt("whats_new_last_version", 0)
        if (currentVersionCode > lastShownVersion) {
            prefs.edit().putInt("whats_new_last_version", currentVersionCode).apply()
            showWhatsNewBanner = true
        }
    }

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
    //val animeRandomState by animeRandomViewModel.uiState.collectAsState()
    val profileUiState by profileViewModel.uiState.collectAsState()
    val localAnimeStatuses by localAnimeIdsViewModel.localAnimeStatuses.collectAsState()
    val heroCards by heroCarouselViewModel.cards.collectAsState()
    val heroIsLoading by heroCarouselViewModel.isLoading.collectAsState()

    val hasError = animeSeasonNowError || topAnimeError || animeSeasonUpcomingError

    val listTab = listOf("Anime", "Manga")
    val selectedTab = remember { mutableStateOf(listTab[0]) }

    val listDays =
        listOf("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday")
    val listDaysFilter =
        listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")

    val listTypeAnime =
        listOf("tv", "movie", "ova", "special", "ona", "music", "cm", "pv", "tv_special")
    val listTypeAnimeFilter =
        listOf("TV", "Película", "OVA", "Especial", "ONA", "Música", "CM", "PV", "TV Especial")

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

    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }
    val tabs = listOf("Anime", "Manga")

    Column(modifier = Modifier.fillMaxSize()) {
        if (hasError) {
            NoInternetScreen(
                onRetryClick = {
                    topAnimesViewModel.topAnime()
                    seasonNowViewModel.AnimesSeasonNow()
                    seasonUpcomingViewModel.AnimesSeasonUpcoming()
                    //animeRandomViewModel.loadRandomAnime()
                    //characterRandomViewModel.loadCharacterRandom()
                })
        } else {
            if (animeSeasonUpcomingIsLoading) {
                LoadingScreen()
            } else if (animeSeasonNow.isNotEmpty()) {
                CustomSeijakuTabSelector(
                    tabs = listOf("Anime", "Manga"),
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = { selectedTabIndex = it })

                WhatsNewBanner(
                    versionName = CURRENT_WHATS_NEW.versionName,
                    visible = showWhatsNewBanner,
                    onTap = {
                        showWhatsNewBanner = false
                        navController.navigate(AppDestinations.NOVEDADES_ROUTE)
                    },
                    onDismiss = { showWhatsNewBanner = false })

                when (selectedTabIndex) {
                    0 -> {
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
                            //animeRandomState = animeRandomState,
                            profileUiState = profileUiState,
                            navController = navController,
                            localAnimeStatuses = localAnimeStatuses,
                            heroCards = heroCards,
                            heroIsLoading = heroIsLoading
                        )
                    }

                    1 -> {
                        MangaPlaceholder()
                    }
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
    //animeRandomState: AnimeRandomUiState,
    profileUiState: com.yumedev.seijakulist.ui.screens.profile.ProfileUiState,
    navController: NavController,
    localAnimeStatuses: Map<Int, String> = emptyMap(),
    heroCards: List<com.yumedev.seijakulist.domain.models.HeroAnimeItem>? = null,
    heroIsLoading: Boolean = true
) {
    LazyColumn(
        contentPadding = PaddingValues(top = 2.dp, bottom = 16.dp),
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

        // Hero Carousel — 5 cards deslizables
        item {
            HeroCarousel(
                navController = navController,
                cards = heroCards,
                isLoading = heroIsLoading
            )
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
                localAnimeStatuses = localAnimeStatuses,
                onViewMoreClick = {
                    navController.navigate("${AppDestinations.VIEW_MORE_ROUTE}/season_now")
                })
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
                localAnimeStatuses = localAnimeStatuses,
                onViewMoreClick = {
                    navController.navigate("${AppDestinations.VIEW_MORE_ROUTE}/top_anime")
                })
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
                localAnimeStatuses = localAnimeStatuses,
                onViewMoreClick = {
                    navController.navigate("${AppDestinations.VIEW_MORE_ROUTE}/season_upcoming")
                })
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
    onViewMoreClick: () -> Unit,
    localAnimeStatuses: Map<Int, String> = emptyMap()
) {
    Column(
        modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Header con diseño mejorado
        AnimeSectionHeader(
            title = title, icon = icon, onViewMoreClick = onViewMoreClick
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
            }, transitionSpec = {
                androidx.compose.animation.fadeIn(
                    animationSpec = androidx.compose.animation.core.tween(300)
                ) togetherWith androidx.compose.animation.fadeOut(
                    animationSpec = androidx.compose.animation.core.tween(300)
                )
            }, label = "anime_content_transition"
        ) { state ->
            when (state) {
                ContentState.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        CardAnimesHomeLoading()
                    }
                }

                ContentState.Content -> {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        CardAnimesHome(
                            animeList = animeList as List<Anime>,
                            navController = navController,
                            localAnimeStatuses = localAnimeStatuses
                        )
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
    title: String, icon: ImageVector, onViewMoreClick: () -> Unit
) {
    androidx.compose.material3.Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp), color = Color.Transparent
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
                        ), contentAlignment = Alignment.Center
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
            val btnInteraction = remember { MutableInteractionSource() }
            val btnPressed by btnInteraction.collectIsPressedAsState()
            val btnScale by animateFloatAsState(
                targetValue = if (btnPressed) 0.92f else 1f,
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                label = "btn_scale"
            )
            val iconOffset by animateDpAsState(
                targetValue = if (btnPressed) 4.dp else 0.dp,
                animationSpec = tween(durationMillis = 150),
                label = "icon_offset"
            )
            androidx.compose.material3.FilledTonalButton(
                onClick = onViewMoreClick,
                interactionSource = btnInteraction,
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                        )
                    )
                ),
                colors = androidx.compose.material3.ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.graphicsLayer {
                    scaleX = btnScale
                    scaleY = btnScale
                }
            ) {
                Text(
                    text = "Ver más", fontSize = 13.sp, fontFamily = PoppinsMedium
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier
                        .size(15.dp)
                        .offset(x = iconOffset)
                )
            }
        }
    }
}

// Header legacy (mantener para compatibilidad)
@Composable
private fun SectionHeader(
    title: String, icon: ImageVector, onViewMoreClick: () -> Unit
) = AnimeSectionHeader(title, icon, onViewMoreClick)

@Composable
fun FilterAnimesHome(
    list: List<String>,
    listLabel: List<String>,
    selectedFilter: String?,
    onFilterSelected: (String?) -> Unit
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        list.forEachIndexed { index, filter ->
            val isSelected = selectedFilter == filter

            // Animación suave para los colores del contenedor y el contenido
            val containerColor by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surfaceContainer,
                animationSpec = tween(durationMillis = 300),
                label = "color_anim"
            )

            val contentColor by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSurfaceVariant,
                animationSpec = tween(durationMillis = 300),
                label = "text_anim"
            )

            FilterChip(
                selected = isSelected,
                onClick = { onFilterSelected(if (isSelected) null else filter) },
                label = {
                    Text(
                        text = listLabel[index], style = TextStyle(
                            fontFamily = if (isSelected) PoppinsBold else PoppinsRegular,
                            fontSize = 14.sp,
                            color = contentColor // Aplicamos la animación al texto
                        )
                    )
                },
                leadingIcon = if (isSelected) {
                    {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = contentColor // Aplicamos la animación al icono
                        )
                    }
                } else null,
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = containerColor,
                    labelColor = contentColor,
                    selectedContainerColor = containerColor, // Forzamos el uso de nuestra animación
                    selectedLabelColor = contentColor,
                    selectedLeadingIconColor = contentColor
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = if (isSelected) Color.Transparent
                    else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                    borderWidth = 1.dp,
                    selectedBorderWidth = 0.dp // Al estar seleccionado, el color sólido manda
                ),
                // Mantenemos el redondeado sutil de 12.dp como pediste
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
                    ), contentAlignment = Alignment.Center
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

private val heroPlaceholderColors = listOf(
    Color(0xFF6200EA),
    Color(0xFF00897B),
    Color(0xFFE64A19),
    Color(0xFF1565C0),
    Color(0xFF558B2F),
)

// Hero Carousel — 5 cards deslizables
@Composable
private fun HeroCarousel(
    navController: NavController,
    cards: List<com.yumedev.seijakulist.domain.models.HeroAnimeItem>?,
    @Suppress("UNUSED_PARAMETER") isLoading: Boolean
) {
    val pageCount = if (cards.isNullOrEmpty()) heroPlaceholderColors.size else cards.size
    val pagerState = rememberPagerState(pageCount = { pageCount })

    LaunchedEffect(pagerState.settledPage) {
        delay(5000)
        val next = (pagerState.settledPage + 1) % pageCount
        pagerState.animateScrollToPage(next)
    }

    val shouldAnimate = !HomeAnimationState.hasAnimated
    var isVisible by remember { mutableStateOf(!shouldAnimate) }

    LaunchedEffect(shouldAnimate) {
        if (shouldAnimate) {
            HomeAnimationState.hasAnimated = true
            delay(100)
            isVisible = true
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(500)) + slideInVertically(
            initialOffsetY = { it / 4 },
            animationSpec = tween(500, easing = FastOutSlowInEasing)
        )
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 16.dp),
                pageSpacing = 12.dp,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                if (cards.isNullOrEmpty()) {
                    HeroPlaceholderCard(color = heroPlaceholderColors[page])
                } else {
                    HeroAnimeCard(
                        item = cards[page],
                        onClick = { navController.navigate("${AppDestinations.ANIME_DETAIL_ROUTE}/${cards[page].malId}") }
                    )
                }
            }

            // Indicador de puntos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(pageCount) { index ->
                    val isSelected = pagerState.currentPage == index
                    val dotWidth by animateDpAsState(
                        targetValue = if (isSelected) 20.dp else 6.dp,
                        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                        label = "dot_width_$index"
                    )
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .height(6.dp)
                            .width(dotWidth)
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun HeroPlaceholderCard(color: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            val transition = rememberInfiniteTransition(label = "shimmer")
            val alpha by transition.animateFloat(
                initialValue = 0.3f,
                targetValue = 0.6f,
                animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
                label = "shimmer_alpha"
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = alpha))
            )
        }
    }
}

@Composable
private fun HeroAnimeCard(
    item: com.yumedev.seijakulist.domain.models.HeroAnimeItem,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "card_scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black),
        onClick = onClick,
        interactionSource = interactionSource
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Imagen de fondo
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Gradiente oscuro de abajo a arriba
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.3f),
                                Color.Black.copy(alpha = 0.85f)
                            )
                        )
                    )
            )

            // Label badge — pill con color e ícono por categoría
            val badgeColor = when (item.label) {
                "PARA VOS"        -> Color(0xFF7C3AED)
                "PROMO"           -> Color(0xFFEA580C)
                "NUEVOS EPISODIOS"-> Color(0xFF059669)
                "PRÓXIMAMENTE"    -> Color(0xFF2563EB)
                "CLÁSICO"         -> Color(0xFFD97706)
                else              -> Color(0xFF6200EA)
            }
            val badgeIcon = when (item.label) {
                "PARA VOS"        -> Icons.Default.Favorite
                "PROMO"           -> Icons.Default.PlayArrow
                "NUEVOS EPISODIOS"-> Icons.Default.Tv
                "PRÓXIMAMENTE"    -> Icons.Default.CalendarToday
                "CLÁSICO"         -> Icons.Default.Star
                else              -> Icons.Default.Star
            }
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(50), ambientColor = badgeColor, spotColor = badgeColor)
                    .background(
                        Brush.horizontalGradient(listOf(badgeColor, badgeColor.copy(alpha = 0.75f))),
                        RoundedCornerShape(50)
                    )
                    .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(50))
                    .clip(RoundedCornerShape(50))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = badgeIcon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(11.dp)
                    )
                    Text(
                        text = item.label,
                        fontFamily = PoppinsBold,
                        fontSize = 9.sp,
                        letterSpacing = 0.8.sp,
                        color = Color.White
                    )
                }
            }

            // Info abajo
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = item.title,
                    fontFamily = PoppinsBold,
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(0f, 2f),
                            blurRadius = 6f
                        )
                    )
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    item.score?.let { score ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFC107),
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = String.format(java.util.Locale.US, "%.1f", score),
                                fontFamily = PoppinsMedium,
                                fontSize = 12.sp,
                                color = Color.White
                            )
                        }
                    }
                    item.year?.let {
                        Text(
                            text = it,
                            fontFamily = PoppinsRegular,
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                    item.status?.let {
                        Text(
                            text = it,
                            fontFamily = PoppinsRegular,
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ScoreBadge(
    score: Float?, modifier: Modifier = Modifier
) {
    if (score == null) return

    val formattedScore = remember(score) {
        String.format(java.util.Locale.US, "%.1f", score)
    }

    val animatedAlpha by animateFloatAsState(
        targetValue = 1f, animationSpec = tween(400)
    )

    Surface(
        modifier = modifier.graphicsLayer { alpha = animatedAlpha },
        shape = RoundedCornerShape(50),
        color = Color.Black.copy(alpha = 0.75f),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color(0xFFFFC107), // Dorado más elegante
                modifier = Modifier.size(13.dp)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = formattedScore,
                fontFamily = PoppinsMedium,
                fontSize = 12.sp,
                color = Color.White
            )
        }
    }
}


@Composable
fun SectionLabel() {
    Text(
        text = "Anime Random",
        fontFamily = PoppinsBold,
        fontSize = 10.sp,
        letterSpacing = 1.4.sp,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun DetailsButton() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Ver detalles",
                fontFamily = PoppinsMedium,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.primary
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
fun MetadataItemModern(text: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            fontFamily = PoppinsRegular,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
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
                            targetValue = 1000f, showShimmer = true
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
                                        targetValue = 1000f, showShimmer = true
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
                                        targetValue = 1000f, showShimmer = true
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
                                        targetValue = 1000f, showShimmer = true
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
                                            targetValue = 1000f, showShimmer = true
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
                                            targetValue = 1000f, showShimmer = true
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
                                    targetValue = 1000f, showShimmer = true
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

        val transition =
            rememberInfiniteTransition(label = "shimmer")
        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = targetValue,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1200,
                    easing = FastOutSlowInEasing
                ), repeatMode = RepeatMode.Restart
            ),
            label = "shimmerTranslate"
        )

        Brush.linearGradient(
            colors = shimmerColors,
            start = androidx.compose.ui.geometry.Offset.Zero,
            end = androidx.compose.ui.geometry.Offset(
                x = translateAnimation.value, y = translateAnimation.value
            )
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

    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "Arrow rotation"
    )

    AnimatedVisibility(
        visible = isVisible, enter = fadeIn(tween(600)) + slideInVertically(initialOffsetY = { 40 })
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceContainer,
            shadowElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // --- CABECERA (Siempre visible) ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Tu Progreso",
                                fontFamily = PoppinsBold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 18.sp
                            )
                            Text(
                                text = "${stats.totalAnimes} animes en tu lista",
                                fontFamily = PoppinsRegular,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 15.sp
                            )
                        }
                    }

                    // Botón expandir — estilo outlined
                    Surface(
                        modifier = Modifier.size(32.dp),
                        shape = CircleShape,
                        color = Color.Transparent,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        onClick = { isExpanded = !isExpanded }
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(18.dp)
                                    .rotate(rotationAngle),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // --- CONTENIDO DESPLEGABLE ---
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically(animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)) + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        val primaryColor   = MaterialTheme.colorScheme.primary
                        val secondaryColor = MaterialTheme.colorScheme.secondary
                        val tertiaryColor  = MaterialTheme.colorScheme.tertiary
                        val greenColor     = Color(0xFF4CAF50)
                        val hours          = stats.totalEpisodesWatched * 24 / 60

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            NarrativeStatCard(
                                icon     = Icons.AutoMirrored.Filled.List,
                                iconTint = primaryColor,
                                text     = buildAnnotatedString {
                                    append("Tu lista tiene ")
                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = primaryColor)) {
                                        append("${stats.totalAnimes}")
                                    }
                                    append(" animes guardados")
                                }
                            )
                            NarrativeStatCard(
                                icon     = Icons.Default.Check,
                                iconTint = greenColor,
                                text     = buildAnnotatedString {
                                    append("¡Ya terminaste ")
                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = greenColor)) {
                                        append("${stats.completedAnimes}")
                                    }
                                    append(" series completas!")
                                }
                            )
                            NarrativeStatCard(
                                icon     = Icons.Default.PlayArrow,
                                iconTint = secondaryColor,
                                text     = buildAnnotatedString {
                                    append("Acumulaste ")
                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = secondaryColor)) {
                                        append("${stats.totalEpisodesWatched} eps,")
                                    }
                                    append(" son ")
                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = secondaryColor)) {
                                        append("$hours hs")
                                    }
                                    append(" de anime!")
                                }
                            )
                            NarrativeStatCard(
                                icon     = Icons.Default.Tv,
                                iconTint = tertiaryColor,
                                text     = buildAnnotatedString {
                                    append("Ahora mismo estás viendo ")
                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = tertiaryColor)) {
                                        append("${stats.watchingAnimes}")
                                    }
                                    append(" animes")
                                }
                            )
                        }

                        // Sección de Animes Recientes (dentro del mismo despliegue)
                        if (recentAnimes.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = "Continuar viendo",
                                fontFamily = PoppinsBold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

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

@Composable
private fun NarrativeStatCard(
    icon: ImageVector,
    iconTint: Color,
    text: AnnotatedString
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(iconTint.copy(alpha = 0.08f))
            .padding(horizontal = 12.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(iconTint.copy(alpha = 0.20f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(18.dp)
            )
        }
        Text(
            text       = text,
            fontFamily = PoppinsRegular,
            fontSize   = 13.sp,
            color      = MaterialTheme.colorScheme.onSurface,
            lineHeight = 19.sp
        )
    }
}

// Card de estadística mejorada con animación
@Composable
private fun EnhancedStatCard(
    modifier: Modifier = Modifier, value: String, label: String, delay: Int = 0
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
        visible = isVisible, enter = fadeIn(animationSpec = tween(400)) + scaleIn(
            initialScale = 0.9f, animationSpec = tween(400, easing = FastOutSlowInEasing)
        ), modifier = modifier
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
    modifier: Modifier = Modifier, value: String, label: String
) = EnhancedStatCard(modifier, value, label, 0)

// Card mejorada de "Continúa viendo"
@Composable
private fun EnhancedContinueWatchingCard(
    anime: com.yumedev.seijakulist.data.local.entities.AnimeEntity,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f, animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow
        ), label = "scale"
    )

    val progress = remember(anime.episodesWatched, anime.totalEpisodes) {
        if (anime.totalEpisodes > 0) anime.episodesWatched.toFloat() / anime.totalEpisodes.toFloat() else 0f
    }

    Card(
        modifier = modifier
            .height(150.dp) // Un poco más de altura para que respire
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp), // Flat design con borde es más moderno
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
        onClick = { navController.navigate("${AppDestinations.ANIME_DETAIL_LOCAL_ROUTE}/${anime.malId}") },
        interactionSource = interactionSource
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // IMAGEN DE FONDO
            AsyncImage(
                model = anime.imageUrl,
                contentDescription = anime.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // OVERLAY GRADIENTE CINEMÁTICO
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.2f),
                                Color.Black.copy(alpha = 0.85f)
                            )
                        )
                    )
            )

            // CONTENIDO
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                // TÍTULO CON SOMBRA
                Text(
                    text = anime.title, style = TextStyle(
                        fontFamily = PoppinsBold,
                        fontSize = 12.sp,
                        color = Color.White,
                        shadow = Shadow(Color.Black, Offset(0f, 2f), 4f)
                    ), maxLines = 1, overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // SECCIÓN DE PROGRESO
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    // BARRA DE PROGRESO ESTILIZADA
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(progress)
                                .clip(CircleShape)
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
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
                        // Badge pequeño de episodios
                        Surface(
                            color = Color.Black.copy(alpha = 0.4f), shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "EP. ${anime.episodesWatched}",
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                                style = TextStyle(
                                    fontFamily = PoppinsMedium,
                                    fontSize = 9.sp,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            )
                        }

                        Text(
                            text = "${(progress * 100).toInt()}%", style = TextStyle(
                                fontFamily = PoppinsBold,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
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

// ─────────────────────────────────────────────────────────────────────────────
// Utilidad: obtiene el versionCode actual de la app
// ─────────────────────────────────────────────────────────────────────────────
private fun currentVersionCode(context: android.content.Context): Int {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            context.packageManager.getPackageInfo(context.packageName, 0).longVersionCode.toInt()
        } else {
            @Suppress("DEPRECATION") context.packageManager.getPackageInfo(
                context.packageName,
                0
            ).versionCode
        }
    } catch (e: Exception) {
        0
    }
}
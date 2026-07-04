package com.yumedev.seijakulist.ui.screens.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.draw.shadow
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FiberNew
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
import com.yumedev.seijakulist.data.local.entities.AnimeEntity
import com.yumedev.seijakulist.domain.models.Anime
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.components.CardAnimesHome
import com.yumedev.seijakulist.ui.components.CardAnimesHomeLoading
import com.yumedev.seijakulist.ui.components.CURRENT_WHATS_NEW
import com.yumedev.seijakulist.ui.components.WhatsNewBanner
import com.yumedev.seijakulist.ui.screens.home.FilterAnimesHome
import com.yumedev.seijakulist.ui.components.FullHomeScreenSkeleton
import com.yumedev.seijakulist.ui.components.MangaPlaceholder
import com.yumedev.seijakulist.ui.components.NoInternetScreen
import com.yumedev.seijakulist.ui.components.shimmerBrush
import com.yumedev.seijakulist.ui.screens.profile.AnimeStats
import com.yumedev.seijakulist.ui.screens.profile.CustomSeijakuTabSelector
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsMedium
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import kotlinx.coroutines.delay
import com.yumedev.seijakulist.ui.theme.adp
import com.yumedev.seijakulist.ui.theme.asp
import com.yumedev.seijakulist.ui.theme.getAnimeStatusColor
import com.yumedev.seijakulist.ui.theme.SeijakuSemanticColors
import com.yumedev.seijakulist.ui.theme.SeijakuColors
import androidx.compose.foundation.isSystemInDarkTheme
import kotlinx.coroutines.launch
import com.yumedev.seijakulist.ui.screens.home.anilist.AiringAnimeAniListViewModel
import com.yumedev.seijakulist.ui.screens.home.anilist.TopAnimeAniListViewModel
import com.yumedev.seijakulist.ui.screens.home.anilist.AnimeSeasonUpcomingAniListViewModel
import com.yumedev.seijakulist.ui.screens.home.anilist.HeroCarouselAniListViewModel
import com.yumedev.seijakulist.ui.screens.home.anilist.HeroCarouselMangaAniListViewModel
import com.yumedev.seijakulist.ui.screens.home.anilist.TopMangaAniListViewModel
import com.yumedev.seijakulist.ui.screens.home.anilist.PublishingMangaAniListViewModel
import com.yumedev.seijakulist.ui.screens.home.anilist.TrendingMangaAniListViewModel


// HomeScreen con UI mejorada
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    airingAnimeViewModel: AiringAnimeAniListViewModel = hiltViewModel(),
    topAnimesViewModel: TopAnimeAniListViewModel = hiltViewModel(),
    seasonUpcomingViewModel: AnimeSeasonUpcomingAniListViewModel = hiltViewModel(),
    //animeRandomViewModel: AnimeRandomViewModel = hiltViewModel(),
    //characterRandomViewModel: CharacterRandomViewModel = hiltViewModel(),
    animeScheduleViewModel: AnimeScheduleViewModel = hiltViewModel(),
    animeFilterViewModel: TopAnimeFilterViewModel = hiltViewModel(),
    seasonUpcomingFilterViewModel: AnimeSeasonUpcomingFilterViewModel = hiltViewModel(),
    profileViewModel: com.yumedev.seijakulist.ui.screens.profile.ProfileViewModel = hiltViewModel(),
    localAnimeIdsViewModel: LocalAnimeIdsViewModel = hiltViewModel(),
    heroCarouselViewModel: HeroCarouselAniListViewModel = hiltViewModel(),
    heroCarouselMangaViewModel: HeroCarouselMangaAniListViewModel = hiltViewModel(),
    topMangaViewModel: TopMangaAniListViewModel = hiltViewModel(),
    publishingMangaViewModel: PublishingMangaAniListViewModel = hiltViewModel(),
    trendingMangaViewModel: TrendingMangaAniListViewModel = hiltViewModel(),
    topMangaFilterViewModel: TopMangaFilterViewModel = hiltViewModel(),
    publishingMangaFilterViewModel: PublishingMangaFilterViewModel = hiltViewModel(),
    onScrollChanged: (Boolean) -> Unit = {}
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("seijaku_prefs", Context.MODE_PRIVATE) }

    LaunchedEffect(Unit) {
        val currentVersionCode = currentVersionCode(context)
        val lastShownVersion = prefs.getInt("whats_new_last_version", 0)
        if (currentVersionCode > lastShownVersion) {
            prefs.edit().putInt("whats_new_last_version", currentVersionCode).apply()
        }
    }

    // Estados - Usando AniList ViewModels
    val animeSeasonNow by airingAnimeViewModel.animeList.collectAsState()
    val animeSeasonNowIsLoading by airingAnimeViewModel.isLoading.collectAsState()
    val animeSeasonNowError by airingAnimeViewModel.isError.collectAsState()

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
    val heroMangaCards by heroCarouselMangaViewModel.cards.collectAsState()
    val heroMangaIsLoading by heroCarouselMangaViewModel.isLoading.collectAsState()

    // Estados de Manga
    val topManga by topMangaViewModel.animeList.collectAsState()
    val topMangaIsLoading by topMangaViewModel.isLoading.collectAsState()
    val topMangaError by topMangaViewModel.isError.collectAsState()

    val publishingManga by publishingMangaViewModel.animeList.collectAsState()
    val publishingMangaIsLoading by publishingMangaViewModel.isLoading.collectAsState()
    val publishingMangaError by publishingMangaViewModel.isError.collectAsState()

    val trendingManga by trendingMangaViewModel.animeList.collectAsState()
    val trendingMangaIsLoading by trendingMangaViewModel.isLoading.collectAsState()
    val trendingMangaError by trendingMangaViewModel.isError.collectAsState()

    val topMangaFilter by topMangaFilterViewModel.animeList.collectAsState()
    val topMangaFilterIsLoading by topMangaFilterViewModel.isLoading.collectAsState()

    val publishingMangaFilter by publishingMangaFilterViewModel.animeList.collectAsState()
    val publishingMangaFilterIsLoading by publishingMangaFilterViewModel.isLoading.collectAsState()

    // Solo mostrar error si TODOS los ViewModels críticos tienen error Y no hay datos
    val hasError = animeSeasonNowError && topAnimeError && animeSeasonUpcomingError &&
            animeSeasonNow.isEmpty() && topAnimes.isEmpty() && animeSeasonUpcoming.isEmpty()

    val listTab = listOf("Anime", "Manga")

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

    // Filtros para Manga
    val listTypeManga = listOf("manga", "one_shot", "novel")
    val listTypeMangaFilter = listOf("Manga", "One-shot", "Novela Ligera")

    var selectedDayFilter by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedTypeFilter by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedUpcomingFilter by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedTopMangaFilter by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedPublishingMangaFilter by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(selectedDayFilter) {
        selectedDayFilter?.let { animeScheduleViewModel.AnimeSchedule(it) }
    }

    LaunchedEffect(selectedTypeFilter) {
        selectedTypeFilter?.let { animeFilterViewModel.TopAnimeFilter(it) }
    }

    LaunchedEffect(selectedUpcomingFilter) {
        selectedUpcomingFilter?.let { seasonUpcomingFilterViewModel.AnimeSeasonUpcomingFilter(it) }
    }

    LaunchedEffect(selectedTopMangaFilter) {
        selectedTopMangaFilter?.let { topMangaFilterViewModel.TopMangaFilter(it) }
    }

    LaunchedEffect(selectedPublishingMangaFilter) {
        selectedPublishingMangaFilter?.let { publishingMangaFilterViewModel.PublishingMangaFilter(it) }
    }

    // Recuperar el tab guardado o usar 0 (Anime) por defecto
    val savedTabIndex = remember { prefs.getInt("selected_home_tab", 0) }
    var selectedTabIndex by rememberSaveable { mutableStateOf(savedTabIndex) }
    val tabs = listOf("Anime", "Manga")

    // Guardar el tab seleccionado cuando cambia
    LaunchedEffect(selectedTabIndex) {
        prefs.edit().putInt("selected_home_tab", selectedTabIndex).apply()
    }

    // Estado de scroll para el LazyColumn - NO guardar estado entre navegaciones
    val listState = rememberLazyListState()

    // Detectar si se ha hecho scroll
    val isScrolled = androidx.compose.runtime.derivedStateOf {
        listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0
    }

    // Notificar cambios de scroll
    LaunchedEffect(isScrolled.value) {
        onScrollChanged(isScrolled.value)
    }

    // Determinar si estamos en carga inicial
    // SOLO mostrar skeleton si:
    // 1. Estamos cargando Y
    // 2. NO hay datos que mostrar
    val isInitialLoading = (animeSeasonNowIsLoading || animeSeasonUpcomingIsLoading) &&
                           animeSeasonNow.isEmpty() &&
                           topAnimes.isEmpty() &&
                           animeSeasonUpcoming.isEmpty()

    // Log de estados para debugging
    LaunchedEffect(animeSeasonNowIsLoading, animeSeasonUpcomingIsLoading,
                   animeSeasonNow.size, topAnimes.size, animeSeasonUpcoming.size) {
        Log.d("HomeScreen", """
            |--- HOME SCREEN STATE ---
            |SeasonNow Loading: $animeSeasonNowIsLoading, Size: ${animeSeasonNow.size}
            |SeasonUpcoming Loading: $animeSeasonUpcomingIsLoading, Size: ${animeSeasonUpcoming.size}
            |TopAnimes Size: ${topAnimes.size}
            |isInitialLoading: $isInitialLoading
            |-------------------------
        """.trimMargin())
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (hasError) {
            NoInternetScreen(
                onRetryClick = {
                    topAnimesViewModel.topAnime()
                    airingAnimeViewModel.loadAiringAnime()
                    seasonUpcomingViewModel.loadUpcomingAnime()
                    heroCarouselViewModel.retry()
                    heroCarouselMangaViewModel.retry()
                    //animeRandomViewModel.loadRandomAnime()
                    //characterRandomViewModel.loadCharacterRandom()
                })
        } else {
            if (isInitialLoading) {
                FullHomeScreenSkeleton()
            } else if (animeSeasonNow.isNotEmpty()) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // TabRow fijo sin padding bottom
                    CustomSeijakuTabSelector(
                        tabs = listOf("Anime", "Manga"),
                        selectedTabIndex = selectedTabIndex,
                        onTabSelected = { selectedTabIndex = it }
                    )

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
                            heroIsLoading = heroIsLoading,
                            listState = listState,
                            isRefreshing = animeSeasonNowIsLoading,
                            onRefresh = {
                                airingAnimeViewModel.loadAiringAnime()
                                topAnimesViewModel.topAnime()
                                seasonUpcomingViewModel.loadUpcomingAnime()
                                heroCarouselViewModel.retry()
                            },
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    }

                    1 -> {
                        MangaContent(
                            topManga = topManga,
                            publishingManga = publishingManga,
                            trendingManga = trendingManga,
                            topMangaFilter = topMangaFilter,
                            publishingMangaFilter = publishingMangaFilter,
                            topMangaFilterIsLoading = topMangaFilterIsLoading,
                            publishingMangaFilterIsLoading = publishingMangaFilterIsLoading,
                            trendingMangaIsLoading = trendingMangaIsLoading,
                            listTypeManga = listTypeManga,
                            listTypeMangaFilter = listTypeMangaFilter,
                            selectedTopMangaFilter = selectedTopMangaFilter,
                            selectedPublishingMangaFilter = selectedPublishingMangaFilter,
                            onTopMangaFilterSelected = { selectedTopMangaFilter = it },
                            onPublishingMangaFilterSelected = { selectedPublishingMangaFilter = it },
                            navController = navController,
                            localAnimeStatuses = localAnimeStatuses,
                            heroMangaCards = heroMangaCards,
                            heroMangaIsLoading = heroMangaIsLoading,
                            listState = listState,
                            isRefreshing = topMangaIsLoading,
                            onRefresh = {
                                topMangaViewModel.topManga()
                                publishingMangaViewModel.loadPublishingManga()
                                trendingMangaViewModel.loadTrendingManga()
                                heroCarouselMangaViewModel.retry()
                            },
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    }
                }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
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
    heroIsLoading: Boolean = true,
    listState: androidx.compose.foundation.lazy.LazyListState = rememberLazyListState(),
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh
    ) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(bottom = 16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
        // Quick Stats + Hero Carousel en un solo item
        item(key = "top_section_${profileUiState.allSavedAnimes.size}_${heroCards?.size ?: 0}") {
            Column {
                // Quick Stats (solo si tiene animes guardados)
                profileUiState.allSavedAnimes.takeIf { it.isNotEmpty() }?.let { savedAnimes ->
                    val recentAnimes = savedAnimes.filter { it.statusUser == "Viendo" }.take(3)
                    QuickStats(
                        stats = profileUiState.stats,
                        recentAnimes = recentAnimes,
                        navController = navController
                    )
                }

                // Hero Carousel
                HeroCarousel(
                    navController = navController,
                    cards = heroCards,
                    isLoading = heroIsLoading,
                    localAnimeStatuses = localAnimeStatuses
                )
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
                localAnimeStatuses = localAnimeStatuses,
                onViewMoreClick = {
                    navController.navigate("${AppDestinations.VIEW_MORE_ROUTE}/season_now")
                },
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope
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
                localAnimeStatuses = localAnimeStatuses,
                onViewMoreClick = {
                    navController.navigate("${AppDestinations.VIEW_MORE_ROUTE}/top_anime")
                },
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope
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
                localAnimeStatuses = localAnimeStatuses,
                onViewMoreClick = {
                    navController.navigate("${AppDestinations.VIEW_MORE_ROUTE}/season_upcoming")
                },
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope
            )
        }

        // Espacio final
        item {
            Spacer(Modifier.height(80.adp()))
        }
    }
  }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun MangaContent(
    topManga: List<Any>,
    publishingManga: List<Any>,
    trendingManga: List<Any>,
    topMangaFilter: List<Any>,
    publishingMangaFilter: List<Any>,
    topMangaFilterIsLoading: Boolean,
    publishingMangaFilterIsLoading: Boolean,
    trendingMangaIsLoading: Boolean,
    listTypeManga: List<String>,
    listTypeMangaFilter: List<String>,
    selectedTopMangaFilter: String?,
    selectedPublishingMangaFilter: String?,
    onTopMangaFilterSelected: (String?) -> Unit,
    onPublishingMangaFilterSelected: (String?) -> Unit,
    navController: NavController,
    localAnimeStatuses: Map<Int, String> = emptyMap(),
    heroMangaCards: List<com.yumedev.seijakulist.domain.models.HeroAnimeItem>? = null,
    heroMangaIsLoading: Boolean = true,
    listState: androidx.compose.foundation.lazy.LazyListState = rememberLazyListState(),
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh
    ) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(bottom = 16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Hero Carousel para Manga
            item(key = "hero_manga_${heroMangaCards?.size ?: 0}") {
                Column {
                    Spacer(Modifier.height(16.dp))
                    HeroCarousel(
                        navController = navController,
                        cards = heroMangaCards,
                        isLoading = heroMangaIsLoading,
                        localAnimeStatuses = localAnimeStatuses,
                        isManga = true
                    )
                }
            }

            // Sección Top Puntuación
            item {
                AnimeSectionWithFilter(
                    title = "Top puntuación",
                    icon = Icons.Default.Star,
                    filters = listTypeManga,
                    filterLabels = listTypeMangaFilter,
                    selectedFilter = selectedTopMangaFilter,
                    onFilterSelected = onTopMangaFilterSelected,
                    isLoading = topMangaFilterIsLoading,
                    animeList = if (selectedTopMangaFilter != null) topMangaFilter else topManga,
                    emptyMessage = "No se encontraron manga para el filtro seleccionado.",
                    navController = navController,
                    localAnimeStatuses = localAnimeStatuses,
                    onViewMoreClick = {
                        // TODO: Implementar ruta de ver más para manga
                    },
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    isManga = true
                )
            }

            // Sección En Publicación
            item {
                AnimeSectionWithFilter(
                    title = "En publicación",
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    filters = listTypeManga,
                    filterLabels = listTypeMangaFilter,
                    selectedFilter = selectedPublishingMangaFilter,
                    onFilterSelected = onPublishingMangaFilterSelected,
                    isLoading = publishingMangaFilterIsLoading,
                    animeList = if (selectedPublishingMangaFilter != null) publishingMangaFilter else publishingManga,
                    emptyMessage = "No se encontraron manga para el filtro seleccionado.",
                    navController = navController,
                    localAnimeStatuses = localAnimeStatuses,
                    onViewMoreClick = {
                        // TODO: Implementar ruta de ver más para manga
                    },
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    isManga = true
                )
            }

            // Sección Tendencias
            item {
                AnimeSectionWithFilter(
                    title = "Tendencias",
                    icon = Icons.AutoMirrored.Filled.TrendingUp,
                    filters = emptyList(),
                    filterLabels = emptyList(),
                    selectedFilter = null,
                    onFilterSelected = {},
                    isLoading = trendingMangaIsLoading,
                    animeList = trendingManga,
                    emptyMessage = "No se encontraron manga en tendencia.",
                    navController = navController,
                    localAnimeStatuses = localAnimeStatuses,
                    onViewMoreClick = {
                        // TODO: Implementar ruta de ver más para manga
                    },
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    isManga = true
                )
            }

            // Espacio final
            item {
                Spacer(Modifier.height(80.adp()))
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
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
    localAnimeStatuses: Map<Int, String> = emptyMap(),
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    isManga: Boolean = false
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Header con diseño mejorado
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            AnimeSectionHeader(
                title = title, icon = icon, onViewMoreClick = onViewMoreClick
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

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
                            localAnimeStatuses = localAnimeStatuses,
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope,
                            isManga = isManga
                        )
                    }
                }

                ContentState.Empty -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
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
        modifier = Modifier.fillMaxWidth(),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Título con estilo itálica
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 21.asp(),
                fontFamily = PoppinsBold,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                letterSpacing = 0.3.sp
            )

            // Botón "Ver más" circular
            androidx.compose.material3.Surface(
                modifier = Modifier.size(36.adp()),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.onBackground,
                onClick = onViewMoreClick
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Ver más",
                        tint = MaterialTheme.colorScheme.background,
                        modifier = Modifier.size(18.adp())
                    )
                }
            }
        }
    }
}

@Composable
fun FilterAnimesHome(
    list: List<String>,
    listLabel: List<String>,
    selectedFilter: String?,
    onFilterSelected: (String?) -> Unit
) {
    val scrollState = rememberScrollState()

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        list.forEachIndexed { index, filter ->
            val isSelected = selectedFilter == filter

            item {
                androidx.compose.material3.Surface(
                    onClick = { onFilterSelected(if (isSelected) null else filter) },
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    else MaterialTheme.colorScheme.surfaceContainerHigh,
                    border = if (isSelected)
                        BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                    else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(16.adp()),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text(
                            text = listLabel[index],
                            fontFamily = if (isSelected) PoppinsBold else PoppinsRegular,
                            fontSize = 13.asp(),
                            color = if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

// Estado vacío mejorado con diseño profesional
@Composable
private fun EnhancedEmptyState(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.adp())
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
                    modifier = Modifier.size(28.adp())
                )
            }
            Text(
                text = message,
                fontFamily = PoppinsRegular,
                fontSize = 14.asp(),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                lineHeight = 20.asp(),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

// Hero Carousel — 5 cards deslizables con diseño moderno inspirado en "My Favorites"
@Composable
private fun HeroCarousel(
    navController: NavController,
    cards: List<com.yumedev.seijakulist.domain.models.HeroAnimeItem>?,
    @Suppress("UNUSED_PARAMETER") isLoading: Boolean,
    localAnimeStatuses: Map<Int, String> = emptyMap(),
    isManga: Boolean = false
) {
    val hasCards = !cards.isNullOrEmpty()
    val pageCount = if (hasCards) cards!!.size else 5
    // Usar remember con key para recrear el pagerState cuando cambie el estado de carga de las cards
    val pagerState = androidx.compose.runtime.key(hasCards) {
        rememberPagerState(pageCount = { pageCount })
    }

    // Control de auto-scroll con detección de interacción del usuario
    var lastUserInteractionTime by remember { mutableStateOf(0L) }

    // Detectar cuando el usuario arrastra manualmente
    LaunchedEffect(pagerState.currentPageOffsetFraction) {
        // Si hay un offset significativo y el pager está siendo arrastrado (scroll in progress),
        // es muy probable que sea interacción del usuario
        if (kotlin.math.abs(pagerState.currentPageOffsetFraction) > 0.01f && pagerState.isScrollInProgress) {
            lastUserInteractionTime = System.currentTimeMillis()
        }
    }

    // Auto-scroll que respeta la interacción del usuario
    LaunchedEffect(pagerState.settledPage) {
        delay(5000)
        // Solo hacer auto-scroll si han pasado más de 3 segundos desde la última interacción
        val timeSinceInteraction = System.currentTimeMillis() - lastUserInteractionTime
        if (timeSinceInteraction > 3000) {
            val next = (pagerState.settledPage + 1) % pageCount
            pagerState.animateScrollToPage(next)
        }
    }

    // Obtener el título dinámico basado en la página actual
    val currentLabel = remember(pagerState.currentPage, hasCards, cards) {
        if (hasCards && pagerState.currentPage < cards!!.size) {
            heroBadgeConfig(cards[pagerState.currentPage].label).displayLabel
        } else {
            "Destacados"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
            // Header: Título dinámico + Botón Ver más
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Título animado que cambia con la página
                AnimatedContent(
                    targetState = currentLabel,
                    transitionSpec = {
                        fadeIn(tween(300)) togetherWith fadeOut(tween(300))
                    },
                    label = "title_animation"
                ) { label ->
                    Text(
                        text = label,
                        fontFamily = PoppinsBold,
                        fontSize = 20.asp(),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        letterSpacing = 0.3.sp
                    )
                }

                // Botón "→" circular
                Surface(
                    modifier = Modifier.size(36.adp()),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    onClick = {
                        // Navegar a ver más (puedes ajustar la ruta)
                        navController.navigate("${AppDestinations.VIEW_MORE_ROUTE}/hero")
                    }
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Ver más",
                            tint = MaterialTheme.colorScheme.background,
                            modifier = Modifier.size(18.adp())
                        )
                    }
                }
            }

            // HorizontalPager con las cards y efecto 3D carousel
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 24.dp),
                pageSpacing = 16.dp,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                // Calcular offset para efecto 3D
                val pageOffset =
                    (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                val absOffset = kotlin.math.abs(pageOffset)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            // Efecto de rotación 3D
                            rotationY = pageOffset * 15f

                            // Escala - las cards de los lados se ven más pequeñas
                            val scale = 1f - (absOffset * 0.15f).coerceIn(0f, 0.15f)
                            scaleX = scale
                            scaleY = scale

                            // Opacidad - las cards alejadas son más transparentes
                            alpha = 1f - (absOffset * 0.3f).coerceIn(0f, 0.5f)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (!hasCards) {
                        HeroPlaceholderCard()
                    } else {
                        HeroAnimeCard(
                            item = cards!![page],
                            localAnimeStatus = localAnimeStatuses[cards[page].malId],
                            onClick = {
                                val route = if (isManga) {
                                    "${AppDestinations.MANGA_DETAIL_ROUTE}/${cards[page].malId}"
                                } else {
                                    "${AppDestinations.ANIME_DETAIL_ROUTE}/${cards[page].malId}"
                                }
                                navController.navigate(route)
                            },
                            isManga = isManga
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Dots indicadores — debajo de las cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.5f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
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
                                        else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                                    )
                            )
                        }
                    }
                }
            }
        }
    }

@Composable
private fun HeroPlaceholderCard() {
    val brush = shimmerBrush()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.adp()),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush)
        ) {
            // Contenido skeleton del hero card
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Géneros skeleton
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    repeat(2) {
                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .height(18.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.White.copy(alpha = 0.2f))
                        )
                    }
                }

                // Título skeleton
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.White.copy(alpha = 0.3f))
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(18.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.White.copy(alpha = 0.3f))
                )

                // Meta badges skeleton
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .width(50.dp)
                                .height(22.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color.White.copy(alpha = 0.2f))
                        )
                    }
                }
            }
        }
    }
}

private data class HeroBadgeConfig(
    val displayLabel: String,
    val icon: ImageVector,
    val badgeType: String // Tipo para buscar en SeijakuSemanticColors
)

private fun heroBadgeConfig(label: String): HeroBadgeConfig = when (label) {
    "PARA VOS" -> HeroBadgeConfig("Pensado para vos", Icons.Default.Favorite, "PARA VOS")
    "PROMO" -> HeroBadgeConfig("Miralo ahora", Icons.Default.PlayArrow, "PROMO")
    "SIGUE MIRANDO" -> HeroBadgeConfig("Sigue mirando", Icons.Default.Tv, "SIGUE MIRANDO")
    "EMPIEZA A VER" -> HeroBadgeConfig("Empieza a ver", Icons.Default.PlayArrow, "EMPIEZA A VER")
    "NUEVOS EPISODIOS" -> HeroBadgeConfig(
        "Nuevos episodios",
        Icons.Default.FiberNew,
        "NUEVOS EPISODIOS"
    )
    "PRÓXIMAMENTE" -> HeroBadgeConfig(
        "Próximamente",
        Icons.Default.CalendarToday,
        "PRÓXIMAMENTE"
    )
    "CLÁSICO" -> HeroBadgeConfig("Un clásico", Icons.Default.Star, "CLÁSICO")
    else -> HeroBadgeConfig(label, Icons.Default.Star, "DEFAULT")
}

private fun translateAnimeStatus(status: String): String = when {
    status.contains("Airing", ignoreCase = true) -> "En emisión"
    status.contains("Finished", ignoreCase = true) -> "Finalizado"
    else -> "Próximamente"
}

private val genreTranslations = mapOf(
    "Action" to "Acción", "Adventure" to "Aventura", "Comedy" to "Comedia",
    "Drama" to "Drama", "Fantasy" to "Fantasía", "Horror" to "Horror",
    "Mystery" to "Misterio", "Romance" to "Romance", "Sci-Fi" to "Ciencia Ficción",
    "Slice of Life" to "Vida Cotidiana", "Sports" to "Deportes",
    "Supernatural" to "Sobrenatural", "Thriller" to "Thriller",
    "Psychological" to "Psicológico", "Mecha" to "Mecha", "Music" to "Música",
    "Historical" to "Histórico", "Military" to "Militar", "School" to "Escolar",
    "Magic" to "Magia", "Space" to "Espacio", "Vampire" to "Vampiros",
    "Isekai" to "Isekai", "Demons" to "Demonios", "Harem" to "Harem",
)

private fun translateGenre(genre: String): String = genreTranslations[genre] ?: genre

@Composable
private fun HeroAnimeCard(
    item: com.yumedev.seijakulist.domain.models.HeroAnimeItem,
    localAnimeStatus: String?,
    onClick: () -> Unit,
    isManga: Boolean = false
) {
    val isDark = isSystemInDarkTheme()
    val ratingColors = SeijakuSemanticColors.ratingBadgeColors(isDark)

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
            .height(220.adp())
            .graphicsLayer { scaleX = scale; scaleY = scale },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = SeijakuColors.Dark.fondoCard),
        border = BorderStroke(
            width = 1.dp,
            color = SeijakuColors.Dark.borde.copy(alpha = 0.3f)
        ),
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

            // Gradiente más alto desde abajo (empieza en 0.2 en vez de 0.35)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0f to Color.Transparent,
                                0.2f to Color.Black.copy(alpha = 0.15f),
                                0.5f to Color.Black.copy(alpha = 0.65f),
                                1.0f to Color.Black.copy(alpha = 0.95f)
                            )
                        )
                    )
            )

            // Vignette lateral izquierda
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.28f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // ── Indicador de estado mejorado (top end) ──────────
            localAnimeStatus?.let { userStatus ->
                val statusColor = getAnimeStatusColor(userStatus, isDark)

                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = statusColor.copy(alpha = 0.20f),
                    border = BorderStroke(1.dp, statusColor.copy(alpha = 0.4f)),
                    tonalElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(statusColor)
                        )
                        Text(
                            text = userStatus,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }
                }
            }

            // ── Info inferior ─────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Título con sombra reforzada
                Text(
                    text = item.title,
                    fontSize = 20.sp,
                    lineHeight = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.85f),
                            offset = Offset(0f, 2f),
                            blurRadius = 14f
                        )
                    )
                )

                // Meta-badges: Pills redondeados
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Rating pill
                    item.score?.let { score ->
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = ratingColors.containerColor,
                            border = BorderStroke(1.dp, ratingColors.borderColor)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = ratingColors.contentColor
                                )
                                Text(
                                    text = String.format(java.util.Locale.US, "%.1f", score),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ratingColors.contentColor
                                )
                            }
                        }
                    }

                    // Year pill
                    item.year?.let { year ->
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color.White.copy(alpha = 0.12f),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp),
                                    tint = Color.White.copy(alpha = 0.85f)
                                )
                                Text(
                                    text = year,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White.copy(alpha = 0.92f)
                                )
                            }
                        }
                    }

                    // Episodes pill
                    item.episodes?.let { eps ->
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color.White.copy(alpha = 0.12f),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isManga) Icons.AutoMirrored.Filled.MenuBook else Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp),
                                    tint = Color.White.copy(alpha = 0.85f)
                                )
                                Text(
                                    text = if (isManga) "$eps caps" else "$eps eps",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White.copy(alpha = 0.92f)
                                )
                            }
                        }
                    }

                    // Status pill
                    item.status?.let { status ->
                        val statusColor = SeijakuSemanticColors.statusColor(status, isDark)
                        val statusText = when {
                            status.contains("Releasing", ignoreCase = true) && isManga -> "En publicación"
                            status.contains("Airing", ignoreCase = true) -> "En emisión"
                            status.contains("Finished", ignoreCase = true) -> "Finalizado"
                            else -> "Próximamente"
                        }

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = statusColor.copy(alpha = 0.18f),
                            border = BorderStroke(1.dp, statusColor.copy(alpha = 0.38f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isManga) Icons.AutoMirrored.Filled.MenuBook else Icons.Default.Tv,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp),
                                    tint = statusColor
                                )
                                Text(
                                    text = statusText,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = statusColor
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Quick Stats Mejorado - Tu progreso
@Composable
private fun QuickStats(
    stats: AnimeStats,
    recentAnimes: List<AnimeEntity>,
    navController: NavController
) {
    val isDark = isSystemInDarkTheme()
    var isExpanded by remember { mutableStateOf(false) }

    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "Arrow rotation"
    )

    // Animación de pulso sutil para el botón cuando está colapsado
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val buttonScale = if (!isExpanded) pulseScale else 1f

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // --- CABECERA (Siempre visible) ---
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tu Progreso",
                            fontFamily = PoppinsBold,
                            fontSize = 16.asp(),
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 18.asp()
                        )

                        // Botón expandir — estilo outlined con animación de pulso
                        Surface(
                            modifier = Modifier
                                .size(32.adp())
                                .graphicsLayer {
                                    scaleX = buttonScale
                                    scaleY = buttonScale
                                },
                            shape = CircleShape,
                            color = Color.Transparent,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            onClick = { isExpanded = !isExpanded }
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(18.adp())
                                        .rotate(rotationAngle),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    // Texto rotativo con insights - cada uno con su color
                    data class InsightData(val text: String, val color: Color)

                    val insights = remember(stats, isDark) {
                        buildList {
                            var insightIndex = 0

                            // 1. Siempre: Total de animes - Usa cream (lo importante)
                            val animeText = if (stats.totalAnimes == 1) "anime" else "animes"
                            add(InsightData("Llevas ${stats.totalAnimes} $animeText en tu refugio", SeijakuSemanticColors.insightColor(insightIndex++, isDark)))

                            // 2. Siempre: Horas vistas - Usa salvia (info calma)
                            val totalMinutes = stats.totalEpisodesWatched * 24
                            val hours = totalMinutes / 60
                            val days = hours / 24
                            if (days > 0) {
                                val diasText = if (days == 1) "día" else "días"
                                add(InsightData("$days $diasText viendo anime, ¿no es genial?", SeijakuSemanticColors.insightColor(insightIndex++, isDark)))
                            } else if (hours > 0) {
                                val horasText = if (hours == 1) "hora" else "horas"
                                add(InsightData("$hours $horasText viendo anime, vas bien", SeijakuSemanticColors.insightColor(insightIndex++, isDark)))
                            } else {
                                val minutosText = if (totalMinutes == 1) "minuto" else "minutos"
                                add(InsightData("$totalMinutes $minutosText de anime, ¡bienvenido!", SeijakuSemanticColors.insightColor(insightIndex++, isDark)))
                            }

                            // 3. Si completedAnimes > 0 - Cream claro
                            if (stats.completedAnimes in 1..<10) {
                                val animeCompletadoText = if (stats.completedAnimes == 1) "anime" else "animes"
                                add(InsightData("Ya cerraste ${stats.completedAnimes} $animeCompletadoText, ¿cuál te gustó más?", SeijakuSemanticColors.insightColor(insightIndex++, isDark)))
                            }

                            // 4. Si averageScore > 0 - Salvia profunda
                            if (stats.averageScore > 0) {
                                add(InsightData("Les das un ${String.format("%.1f", stats.averageScore)}/10 en promedio, buen ojo", SeijakuSemanticColors.insightColor(insightIndex++, isDark)))
                            }

                            // 5. Si genreStats no está vacío - Estado completado (logro)
                            val topGenre = stats.genreStats.maxByOrNull { it.value }?.key
                            if (topGenre != null) {
                                add(InsightData("$topGenre es tu zona de confort", SeijakuSemanticColors.insightColor(insightIndex++, isDark)))
                            }

                            // 6. Si watchingAnimes > 0 - Rota colores
                            if (stats.watchingAnimes > 0) {
                                val viendoText = if (stats.watchingAnimes == 1) "anime" else "animes"
                                add(InsightData("Tienes ${stats.watchingAnimes} $viendoText viendo ahora, ¿qué tal?", SeijakuSemanticColors.insightColor(insightIndex++, isDark)))
                            }

                            // 7. Si completedAnimes >= 10 - Rota colores
                            if (stats.completedAnimes >= 10) {
                                val animeMilestoneText = if (stats.completedAnimes == 1) "anime" else "animes"
                                add(InsightData("¡Ya completaste ${stats.completedAnimes} $animeMilestoneText! Impresionante", SeijakuSemanticColors.insightColor(insightIndex++, isDark)))
                            }

                            // 8. Si totalEpisodesWatched >= 100 - Rota colores
                            if (stats.totalEpisodesWatched >= 100) {
                                val episodiosText = if (stats.totalEpisodesWatched == 1) "episodio visto" else "episodios vistos"
                                add(InsightData("${stats.totalEpisodesWatched} $episodiosText, ¡qué locura!", SeijakuSemanticColors.insightColor(insightIndex++, isDark)))
                            }
                        }
                    }

                    var currentInsightIndex by remember { mutableStateOf(0) }

                    LaunchedEffect(insights) {
                        if (insights.isNotEmpty()) {
                            while (true) {
                                kotlinx.coroutines.delay(4000)
                                currentInsightIndex = (currentInsightIndex + 1) % insights.size
                            }
                        }
                    }

                    if (insights.isNotEmpty()) {
                        val currentInsight = insights[currentInsightIndex]

                        // Animar el color del stripe
                        val stripeColor by animateColorAsState(
                            targetValue = currentInsight.color,
                            animationSpec = tween(600, easing = FastOutSlowInEasing),
                            label = "stripe_color"
                        )

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceContainer,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Stripe vertical de color
                                Box(
                                    modifier = Modifier
                                        .width(3.dp)
                                        .fillMaxHeight()
                                        .background(stripeColor)
                                )

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 12.dp, vertical = 10.dp)
                                ) {
                                    AnimatedContent(
                                        targetState = currentInsight,
                                        transitionSpec = {
                                            // Slide desde abajo con fade
                                            (slideInVertically(
                                                animationSpec = tween(500, easing = EaseOutBack),
                                                initialOffsetY = { it / 2 }
                                            ) + fadeIn(
                                                animationSpec = tween(400)
                                            )) togetherWith (slideOutVertically(
                                                animationSpec = tween(400, easing = FastOutSlowInEasing),
                                                targetOffsetY = { -it / 2 }
                                            ) + fadeOut(
                                                animationSpec = tween(300)
                                            ))
                                        },
                                        label = "insight_animation"
                                    ) { insight ->
                                        // Función para resaltar números y palabras clave en el texto
                                        val annotatedText = buildAnnotatedString {
                                            val text = insight.text

                                            // Detectar si es el insight de género favorito (nuevo patrón)
                                            if (text.contains(" es tu zona de confort")) {
                                                val parts = text.split(" es tu zona de confort")

                                                // Resaltar el género (está al principio)
                                                withStyle(
                                                    SpanStyle(
                                                        color = insight.color,
                                                        fontFamily = PoppinsBold
                                                    )
                                                ) {
                                                    append(parts[0])
                                                }

                                                append(" es tu zona de confort")
                                                append(parts.getOrNull(1) ?: "")
                                            } else {
                                                // Para otros insights, resaltar números
                                                val numberRegex = "\\d+(?:[.,]\\d+)?".toRegex()
                                                var lastIndex = 0

                                                numberRegex.findAll(text).forEach { matchResult ->
                                                    // Añadir texto antes del número
                                                    append(text.substring(lastIndex, matchResult.range.first))

                                                    // Añadir número con color
                                                    withStyle(
                                                        SpanStyle(
                                                            color = insight.color,
                                                            fontFamily = PoppinsBold
                                                        )
                                                    ) {
                                                        append(matchResult.value)
                                                    }

                                                    lastIndex = matchResult.range.last + 1
                                                }

                                                // Añadir texto restante
                                                append(text.substring(lastIndex))
                                            }
                                        }

                                        Text(
                                            text = annotatedText,
                                            fontFamily = PoppinsMedium,
                                            fontSize = 12.asp(),
                                            color = MaterialTheme.colorScheme.onSurface,
                                            lineHeight = 16.asp()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // --- CONTENIDO DESPLEGABLE ---
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically(animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)) + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    // Sección "Continuar viendo"
                    if (recentAnimes.isNotEmpty()) {
                        Column {
                            Spacer(modifier = Modifier.height(16.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Continuar viendo",
                                fontFamily = PoppinsBold,
                                fontSize = 16.asp(),
                                color = MaterialTheme.colorScheme.onSurface,
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

// Card mejorada de "Continúa viendo"
@Composable
private fun EnhancedContinueWatchingCard(
    anime: AnimeEntity,
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

    val targetProgress = remember(anime.episodesWatched, anime.totalEpisodes) {
        if (anime.totalEpisodes > 0) anime.episodesWatched.toFloat() / anime.totalEpisodes.toFloat() else 0f
    }

    // Animación suave de la barra de progreso
    val progress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "progress_animation"
    )

    Card(
        modifier = modifier
            .height(150.adp()) // Un poco más de altura para que respire
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
                        fontSize = 12.asp(),
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
                                    fontSize = 9.asp(),
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            )
                        }

                        Text(
                            text = "${(progress * 100).toInt()}%", style = TextStyle(
                                fontFamily = PoppinsBold,
                                fontSize = 10.asp(),
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            }
        }
    }
}

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
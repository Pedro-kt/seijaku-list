package com.yumedev.seijakulist.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yumedev.seijakulist.domain.models.Anime
import com.yumedev.seijakulist.ui.components.CardAnimesHome
import com.yumedev.seijakulist.ui.components.CardAnimesHomeLoading
import com.yumedev.seijakulist.ui.screens.home.FilterAnimesHome
import com.yumedev.seijakulist.ui.components.LoadingScreen
import com.yumedev.seijakulist.ui.components.MangaPlaceholder
import com.yumedev.seijakulist.ui.components.NoInternetScreen
import com.yumedev.seijakulist.ui.theme.RobotoBold
import com.yumedev.seijakulist.ui.theme.RobotoRegular


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
    seasonUpcomingFilterViewModel: AnimeSeasonUpcomingFilterViewModel = hiltViewModel()
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
    TabRow(
        selectedTabIndex = tabs.indexOf(selectedTab),
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.background,
        indicator = { tabPositions ->
            SecondaryIndicator(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[tabs.indexOf(selectedTab)])
                    .height(3.dp)
                    .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp)),
                color = MaterialTheme.colorScheme.primary
            )
        },
        divider = {}
    ) {
        tabs.forEach { tab ->
            Tab(
                selected = tab == selectedTab,
                onClick = { onTabSelected(tab) },
                text = {
                    Text(
                        text = tab,
                        fontFamily = RobotoBold,
                        fontSize = 15.sp
                    )
                }
            )
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
    navController: NavController
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
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
                onViewMoreClick = { /* TODO */ }
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
                onViewMoreClick = { /* TODO */ }
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
                onViewMoreClick = { /* TODO */ }
            )
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
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionHeader(
            title = title,
            icon = icon,
            onViewMoreClick = onViewMoreClick
        )

        FilterAnimesHome(
            list = filters,
            listLabel = filterLabels,
            selectedFilter = selectedFilter,
            onFilterSelected = onFilterSelected
        )

        when {
            isLoading -> CardAnimesHomeLoading()
            animeList.isNotEmpty() -> CardAnimesHome(animeList as List<Anime>, navController)
            else -> EmptyStateMessage(emptyMessage)
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    icon: ImageVector,
    onViewMoreClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp,
                fontFamily = RobotoBold,
                letterSpacing = 0.5.sp
            )
        }

        TextButton(
            onClick = onViewMoreClick,
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(
                text = "Ver más",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                fontFamily = RobotoRegular
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.primary
            )
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
                        fontFamily = if (isSelected) RobotoBold else RobotoRegular,
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
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    labelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
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
                        Color.Transparent
                    },
                    selectedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    borderWidth = 1.dp,
                    selectedBorderWidth = 1.dp
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

@Composable
private fun EmptyStateMessage(message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = message,
            fontFamily = RobotoRegular,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            lineHeight = 20.sp
        )
    }
}
package com.example.seijakulist.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.seijakulist.ui.components.CardAnimesHome
import com.example.seijakulist.ui.components.CardAnimesHomeLoading
import com.example.seijakulist.ui.components.CompleteAnimeCard
import com.example.seijakulist.ui.components.CompleteCharacterCard
import com.example.seijakulist.ui.components.FilterAnimesHome
import com.example.seijakulist.ui.components.HorizontalDividerComponent
import com.example.seijakulist.ui.components.LoadingScreen
import com.example.seijakulist.ui.components.NoInternetScreen
import com.example.seijakulist.ui.components.SubTitleWithoutIcon
import com.example.seijakulist.ui.theme.RobotoBold
import com.example.seijakulist.ui.theme.RobotoRegular


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

    val animeSeasonNow by seasonNowViewModel.animeList.collectAsState()
    val animeSeasonNowIsLoading by seasonNowViewModel.isLoading.collectAsState()
    val animeSeasonNowErrorMessage by seasonNowViewModel.errorMessage.collectAsState()
    val animeSeasonNowError by seasonNowViewModel.isError.collectAsState()

    val topAnimes by topAnimesViewModel.animeList.collectAsState()
    val topAnimeIsLoading by topAnimesViewModel.isLoading.collectAsState()
    val topAnimeErrorMessage by topAnimesViewModel.errorMessage.collectAsState()
    val topAnimeError by topAnimesViewModel.isError.collectAsState()

    val animeSeasonUpcoming by seasonUpcomingViewModel.animeList.collectAsState()
    val animeSeasonUpcomingIsLoading by seasonUpcomingViewModel.isLoading.collectAsState()
    val animeSeasonUpcomingErrorMessage by seasonUpcomingViewModel.errorMessage.collectAsState()
    val animeSeasonUpcomingError by seasonUpcomingViewModel.isError.collectAsState()

    val characterRandom by characterRandomViewModel.uiCharacterState.collectAsState()

    val animeRandom by animeRandomViewModel.uiState.collectAsStateWithLifecycle()

    val animeSchedule by animeScheduleViewModel.animeList.collectAsState()
    val animeScheduleIsLoading by animeScheduleViewModel.isLoading.collectAsState()
    val animeScheduleErrorMessage by animeScheduleViewModel.errorMessage.collectAsState()
    val animeScheduleError by animeScheduleViewModel.isError.collectAsState()

    val topAnimeFilter by animeFilterViewModel.animeList.collectAsState()
    val topAnimeFilterIsLoading by animeFilterViewModel.isLoading.collectAsState()
    val topAnimeFilterErrorMessage by animeFilterViewModel.errorMessage.collectAsState()
    val topAnimeFilterError by animeFilterViewModel.isError.collectAsState()

    val animeSeasonUpcomingFilter by seasonUpcomingFilterViewModel.animeList.collectAsState()
    val animeSeasonUpcomingFilterIsLoading by seasonUpcomingFilterViewModel.isLoading.collectAsState()
    val animeSeasonUpcomingFilterErrorMessage by seasonUpcomingFilterViewModel.errorMessage.collectAsState()
    val animeSeasonUpcomingFilterError by seasonUpcomingFilterViewModel.isError.collectAsState()

    val hasError = animeSeasonNowError || topAnimeError || animeSeasonUpcomingError

    val listTab = listOf("Anime", "Manga")
    val selectedTab = remember { mutableStateOf(listTab[0]) }

    var visible by rememberSaveable { mutableStateOf(false) }

    val listDays = listOf("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday")
    val listDaysFilter = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")

    val listTypeAnime = listOf("tv", "movie", "ova", "special", "ona", "music", "cm", "pv", "tv_special")
    val listTypeAnimeFilter = listOf("TV", "Película", "OVA", "Especial", "ONA", "Música", "CM", "PV", "TV Especial")

    val listTypeSeasonUpcoming = listOf("tv", "movie", "ova", "special", "ona", "music")
    val listTypeSeasonUpcomingFilter = listOf("TV", "Película", "OVA", "Especial", "ONA", "Música")

    var selectedDayFilter by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedTypeFilter by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedUpcomingFilter by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = selectedDayFilter) {
        selectedDayFilter?.let { day ->
            animeScheduleViewModel.AnimeSchedule(day)
        }
    }
    LaunchedEffect(key1 = selectedTypeFilter) {
        selectedTypeFilter?.let { filter ->
            animeFilterViewModel.TopAnimeFilter(filter)
        }
    }
    LaunchedEffect(selectedUpcomingFilter) {
        selectedUpcomingFilter?.let { filter ->
            seasonUpcomingFilterViewModel.AnimeSeasonUpcomingFilter(filter)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

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

                TabRow(
                    selectedTabIndex = listTab.indexOf(selectedTab.value),
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    indicator = { tabPositions ->
                        HorizontalDivider(
                            modifier = Modifier.tabIndicatorOffset(
                                tabPositions[listTab.indexOf(
                                    selectedTab.value
                                )]
                            ),
                            color = MaterialTheme.colorScheme.primary,
                            thickness = 3.dp
                        )
                    }
                ) {
                    listTab.forEach { tab ->
                        Tab(
                            selected = tab == selectedTab.value,
                            onClick = { selectedTab.value = tab },
                            text = {
                                Text(
                                    text = tab,
                                    fontFamily = RobotoBold
                                )
                            }
                        )
                    }
                }

                when (selectedTab.value) {

                    "Anime" -> {
                        LazyColumn() {
                            item {
                                SubTitleWithoutIcon("En emision")
                                FilterAnimesHome(
                                    list = listDays,
                                    listLabel = listDaysFilter,
                                    selectedFilter = selectedDayFilter,
                                    onFilterSelected = { filter ->
                                        selectedDayFilter = filter
                                    }
                                )
                            }
                            item {
                                selectedDayFilter?.let { day ->
                                    if (animeScheduleIsLoading) {
                                        CardAnimesHomeLoading()
                                    } else if (animeSchedule.isNotEmpty()) {
                                        CardAnimesHome(animeSchedule, navController)
                                    } else {
                                        Text(
                                            text = "No hay animes programados para este dia.",
                                            modifier = Modifier
                                                .padding(16.dp)
                                        )
                                    }
                                } ?: run {
                                    CardAnimesHome(animeSeasonNow, navController)
                                }
                            }
                            item {
                                SubTitleWithoutIcon("Top puntuacion")
                                FilterAnimesHome(
                                    list = listTypeAnime,
                                    listLabel = listTypeAnimeFilter,
                                    selectedFilter = selectedTypeFilter,
                                    onFilterSelected = { filter ->
                                        selectedTypeFilter = filter
                                    }
                                )
                            }
                            item {
                                selectedTypeFilter?.let { filter ->
                                    if (topAnimeFilterIsLoading) {
                                        CardAnimesHomeLoading()
                                    } else if (topAnimeFilter.isNotEmpty()) {
                                        CardAnimesHome(topAnimeFilter, navController)
                                    } else {
                                        Text(
                                            text = "No se encontraron animes para el filtro seleccionado.",
                                            modifier = Modifier
                                                .padding(16.dp)
                                        )
                                    }
                                } ?: run {
                                    CardAnimesHome(topAnimes, navController)
                                }
                            }
                            item {
                                SubTitleWithoutIcon("Próxima temporada")
                                FilterAnimesHome(
                                    list = listTypeSeasonUpcoming,
                                    listLabel = listTypeSeasonUpcomingFilter,
                                    selectedFilter = selectedUpcomingFilter,
                                    onFilterSelected = { filter ->
                                        selectedUpcomingFilter = filter
                                    }
                                )
                            }
                            item {
                                selectedUpcomingFilter?.let { filter ->
                                    if (animeSeasonUpcomingFilterIsLoading) {
                                        CardAnimesHomeLoading()
                                    } else if (animeSeasonUpcomingFilter.isNotEmpty()) {
                                        CardAnimesHome(animeSeasonUpcomingFilter, navController)
                                    } else {
                                        Text(
                                            text = "No se encontraron animes para el filtro seleccionado.",
                                            modifier = Modifier
                                                .padding(16.dp)
                                        )
                                    }
                                } ?: run {
                                    CardAnimesHome(animeSeasonUpcoming, navController)
                                }
                            }
                            /*
                            item {
                                SubTitleWithoutIcon("Anime aleatorio")
                            }
                            item {
                                CompleteAnimeCard(animeRandom, navController, animeRandomViewModel)
                            }
                            item {
                                Spacer(modifier = Modifier.padding(4.dp))
                            }
                            item {
                                SubTitleWithoutIcon("Personaje aleatorio")
                            }
                            item {
                                CompleteCharacterCard(
                                    characterRandom,
                                    navController,
                                    characterRandomViewModel
                                )
                            }
                        }

                             */
                        }
                    }

                    "Manga" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "En este momento no hay mangas disponibles, pero pronto lo estará!",
                                fontFamily = RobotoRegular,
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                }


            }
        }
    }
}
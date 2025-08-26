package com.example.seijakulist.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.seijakulist.R
import com.example.seijakulist.ui.components.CardAnimesHome
import com.example.seijakulist.ui.components.CompleteAnimeCard
import com.example.seijakulist.ui.components.CompleteCharacterCard
import com.example.seijakulist.ui.components.HorizontalDividerComponent
import com.example.seijakulist.ui.components.LoadingScreen
import com.example.seijakulist.ui.components.NoInternetScreen
import com.example.seijakulist.ui.components.SubTitleWithoutIcon
import com.example.seijakulist.ui.components.TitleScreen
import com.example.seijakulist.ui.theme.RobotoBold
import com.example.seijakulist.ui.theme.RobotoRegular


@Composable
fun HomeScreen(
    navController: NavController,
    seasonNowViewModel: AnimeSeasonNowViewModel = hiltViewModel(),
    topAnimesViewModel: TopAnimeViewModel = hiltViewModel(),
    seasonUpcomingViewModel: AnimeSeasonUpcomingViewModel = hiltViewModel(),
    animeRandomViewModel: AnimeRandomViewModel = hiltViewModel(),
    characterRandomViewModel: CharacterRandomViewModel = hiltViewModel()
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

    val hasError = animeSeasonNowError || topAnimeError || animeSeasonUpcomingError

    val listTab = listOf("Anime", "Manga")
    val selectedTab = remember { mutableStateOf(listTab[0]) }

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
                        HorizontalDivider(modifier = Modifier.tabIndicatorOffset(tabPositions[listTab.indexOf(selectedTab.value)]),
                            color = MaterialTheme.colorScheme.inversePrimary,
                            thickness = 2.dp
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
                            }
                            item {
                                CardAnimesHome(animeSeasonNow, navController)
                            }
                            item {
                                HorizontalDividerComponent()
                            }
                            item {
                                SubTitleWithoutIcon("Anime random")
                            }
                            item {
                                CompleteAnimeCard(animeRandom, navController, animeRandomViewModel)
                            }
                            item {
                                HorizontalDividerComponent()
                            }
                            item {
                                SubTitleWithoutIcon("Top scores")
                            }
                            item {
                                CardAnimesHome(topAnimes, navController)
                            }
                            item {
                                HorizontalDividerComponent()
                            }
                            item {
                                SubTitleWithoutIcon("Personaje random")
                            }
                            item {
                                CompleteCharacterCard(
                                    characterRandom,
                                    navController,
                                    characterRandomViewModel
                                )
                            }
                            item {
                                HorizontalDividerComponent()
                            }
                            item {
                                SubTitleWithoutIcon("Proxima temporada")
                            }
                            item {
                                CardAnimesHome(animeSeasonUpcoming, navController)
                            }
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
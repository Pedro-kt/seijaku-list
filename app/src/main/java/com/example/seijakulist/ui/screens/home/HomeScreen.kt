package com.example.seijakulist.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.seijakulist.ui.components.LoadingScreen
import com.example.seijakulist.ui.components.NoInternetScreen
import com.example.seijakulist.ui.components.SubTitleWithoutIcon
import com.example.seijakulist.ui.components.TitleScreen


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

    val RobotoRegular = FontFamily(
        Font(R.font.roboto_regular)
    )
    val RobotoBold = FontFamily(
        Font(R.font.roboto_bold, FontWeight.Bold)
    )

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

                LazyColumn() {
                    item {
                        SubTitleWithoutIcon("En emision")
                    }
                    item {
                        CardAnimesHome(animeSeasonNow, navController)
                    }
                    item {
                        SubTitleWithoutIcon("Anime random")
                    }
                    item {
                        CompleteAnimeCard(animeRandom, navController, animeRandomViewModel)
                    }
                    item {
                        SubTitleWithoutIcon("Top scores")
                    }
                    item {
                        CardAnimesHome(topAnimes, navController)
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
                        SubTitleWithoutIcon("Proxima temporada")
                    }
                    item {
                        CardAnimesHome(animeSeasonUpcoming, navController)
                    }
                    item {
                        TitleScreen("Explora Mangas!")
                    }
                    item {
                        Text(
                            text = "Proximamente",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(16.dp),
                            fontFamily = RobotoBold
                        )
                    }
                }
            }
        }
    }
}
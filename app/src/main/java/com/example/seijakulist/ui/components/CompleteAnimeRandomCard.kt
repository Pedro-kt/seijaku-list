package com.example.seijakulist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.seijakulist.ui.screens.home.AnimeRandomUiState
import com.example.seijakulist.ui.screens.home.AnimeRandomViewModel

@Composable
fun CompleteAnimeCard(uiState: AnimeRandomUiState, navController: NavController, viewModel: AnimeRandomViewModel) {

    val gradientColorsTopBar = listOf(
        Color(0xFF160078),
        Color(0xff7226ff),
        Color(0xFF160078),
    )

    Box(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxSize()
            .height(230.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(brush = Brush.linearGradient(colors = gradientColorsTopBar)),
        contentAlignment = Alignment.Center
    ) {

        when {
            uiState.isLoading -> {
                LoadingScreen()
                IconButton(
                    onClick = { viewModel.loadRandomAnime() },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refrescar",
                        tint = Color.White,
                    )
                }
            }

            uiState.errorMessage != null -> {
                Text(
                    text = uiState.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }

            uiState.anime != null -> {
                AnimeRandomCard(
                    anime = uiState.anime!!,
                    navController = navController,
                    onRefresh = { viewModel.loadRandomAnime() }
                )
            }
            else -> {
                Button(onClick = { viewModel.loadRandomAnime() }) {
                    Text("Cargar Animes")
                }
            }
        }
    }
}
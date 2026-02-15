package com.yumedev.seijakulist.ui.screens.profile

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.yumedev.seijakulist.ui.navigation.AppDestinations

@Composable
fun ProfileLoaderScreen(navController: NavController, profileViewModel: ProfileViewModel = hiltViewModel()) {
    val uiState by profileViewModel.uiState.collectAsState()

    LaunchedEffect(uiState.userProfile) {
        val userProfile = uiState.userProfile

        // 1. Si no hay datos, no hagas nada.
        if (userProfile == null) {
            return@LaunchedEffect
        }

        // 2. Si los datos están incompletos, navega a la pantalla de configuración.
        if (userProfile.username.isNullOrEmpty() || userProfile.profilePictureUrl.isNullOrEmpty()) {
            navController.navigate(AppDestinations.PROFILE_SETUP_ROUTE) {
                popUpTo(AppDestinations.PROFILE_LOADER_ROUTE) { inclusive = true }
            }
        } else {
            // 3. Si los datos están completos, navega a la pantalla de perfil.
            navController.navigate(AppDestinations.PROFILE_VIEW_ROUTE) {
                popUpTo(AppDestinations.PROFILE_LOADER_ROUTE) { inclusive = true }
            }
        }
    }

    // Muestra un círculo mientras los datos se cargan.
    if (uiState.userProfile == null) {
        val infiniteTransition = rememberInfiniteTransition(label = "loading")
        val scale by infiniteTransition.animateFloat(
            initialValue = 0.8f,
            targetValue = 1.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "loading_pulse"
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .scale(scale)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
            )
        }
    }
}
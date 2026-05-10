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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.theme.adp

@Composable
fun ProfileLoaderScreen(navController: NavController, profileViewModel: ProfileViewModel = hiltViewModel()) {
    val uiState by profileViewModel.uiState.collectAsState()
    var hasNavigated by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.userProfile) {
        val userProfile = uiState.userProfile

        android.util.Log.d("ProfileLoaderScreen", "Profile state changed: $userProfile")

        // 1. Si no hay datos, esperar
        if (userProfile == null) {
            android.util.Log.d("ProfileLoaderScreen", "Profile is null, waiting...")
            return@LaunchedEffect
        }

        // Evitar navegación múltiple
        if (hasNavigated) {
            android.util.Log.d("ProfileLoaderScreen", "Already navigated, skipping")
            return@LaunchedEffect
        }

        hasNavigated = true

        // 2. Si los datos están incompletos, navega a la pantalla de configuración.
        if (userProfile.username.isNullOrEmpty() || userProfile.profilePictureUrl.isNullOrEmpty()) {
            android.util.Log.d("ProfileLoaderScreen", "Profile incomplete, navigating to setup")
            navController.navigate(AppDestinations.PROFILE_SETUP_ROUTE) {
                popUpTo(AppDestinations.PROFILE_LOADER_ROUTE) { inclusive = true }
            }
        } else {
            // 3. Si los datos están completos, navega a HOME
            android.util.Log.d("ProfileLoaderScreen", "Profile complete, navigating to home")
            navController.navigate(AppDestinations.HOME) {
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
                    .size(48.adp())
                    .scale(scale)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
            )
        }
    }
}
package com.yumedev.seijakulist.ui.screens.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    // Muestra una barra de progreso mientras los datos se cargan.
    if (uiState.userProfile == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
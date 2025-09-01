package com.example.seijakulist.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.seijakulist.ui.components.TitleScreen

@Composable
fun ProfileView(navController: NavController, profileViewModel: ProfileViewModel = hiltViewModel()) {
    val uiState by profileViewModel.uiState.collectAsState()

    // 1. Muestra una carga si el perfil aún es nulo.
    if (uiState.userProfile == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    // 2. Accede a los datos del perfil directamente del uiState.
    val userProfile = uiState.userProfile
    val username = userProfile?.username ?: "Nombre de usuario"
    val profilePictureUrl = userProfile?.profilePictureUrl

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(shape = CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = profilePictureUrl),
                    contentDescription = "Imagen de perfil",
                    modifier = Modifier.fillMaxSize(),
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            TitleScreen(username)
        }
    }
}
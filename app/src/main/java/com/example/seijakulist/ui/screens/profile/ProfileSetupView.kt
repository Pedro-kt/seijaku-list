package com.example.seijakulist.ui.screens.profile

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.seijakulist.ui.navigation.AppDestinations
import com.example.seijakulist.ui.screens.auth_screen.AuthResult

@Composable
fun ProfileSetupView(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    var username by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val uiState by profileViewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isLoading, uiState.userProfile) {
        if (!uiState.isLoading && uiState.userProfile?.username != null && uiState.error == null) {
            navController.navigate(AppDestinations.HOME) {
                popUpTo(AppDestinations.PROFILE_LOADER_ROUTE) { inclusive = true }
            }
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Crea tu perfil", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Nombre de usuario") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { launcher.launch("image/*") },
            ) {
                Text("Subir foto de perfil")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    profileViewModel.updateUserProfile(username, selectedImageUri)
                },
            ) {
                Text("Guardar y continuar")
            }

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(visible = uiState.isLoading) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    if (uiState.isUploadingImage) {
                        Text("Subiendo imagen...", style = MaterialTheme.typography.bodySmall)
                    } else {
                        Text("Guardando perfil...", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            if (uiState.error != null) {
                Text(
                    text = "Error: ${uiState.error}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
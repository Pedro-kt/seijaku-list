package com.example.seijakulist.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.seijakulist.ui.components.TitleScreen
import com.example.seijakulist.ui.navigation.AppDestinations

@Composable
fun ProfileView(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .clip(shape = CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                        contentAlignment = Alignment.Center,

                        ) {
                        Image(
                            painter = rememberAsyncImagePainter(model = profilePictureUrl),
                            contentDescription = "Imagen de perfil",
                            modifier = Modifier.fillMaxSize(),
                            alignment = Alignment.Center,
                            contentScale = ContentScale.Crop
                        )
                    }
                    FilterChip(
                        onClick = {},
                        modifier = Modifier
                            .align(Alignment.BottomEnd),
                        selected = false,
                        label = {
                            Text("Editar")
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar perfil"
                            )
                        }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                TitleScreen(username)
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(shape = RoundedCornerShape(24.dp))
                    .background(color = MaterialTheme.colorScheme.surfaceContainerHighest)
            ) {
                val probateCards = listOf(1,2,3,4,5)
                LazyRow(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    contentPadding = PaddingValues(16.dp)
                ) {
                    probateCards.forEach { it ->
                        item{
                            ElevatedCard(
                                modifier = Modifier
                                    .width(200.dp)
                                    .fillMaxHeight(),
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 16.dp
                                )
                            ) {
                                Text(
                                    text = "Prueba",
                                    modifier = Modifier.fillMaxSize(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
        item {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

            }
        }

    }
}
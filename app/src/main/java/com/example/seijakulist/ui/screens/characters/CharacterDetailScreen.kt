package com.example.seijakulist.ui.screens.characters

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import java.nio.file.WatchEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    navController: NavController,
    characterId: Int,
    characterDetailViewModel: CharacterDetailViewModel = hiltViewModel()
) {
    val characterDetail by characterDetailViewModel.characterDetail.collectAsState()
    val characterIsLoading by characterDetailViewModel.isLoading.collectAsState()
    val characterErrorMessage by characterDetailViewModel.errorMessage.collectAsState()

    LaunchedEffect(key1 = characterId) {
        characterDetailViewModel.loadCharacterDetail(characterId)
    }

    Column(modifier = Modifier.fillMaxSize().background(color = Color(0xFF06141B))) {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color(
                    0xFF11212D
                )
            ),
            title = {
                Text(
                    text = "Personaje",
                    color = Color.White
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver atrás",
                        tint = Color.White
                    )
                }
            }
        )
        LazyColumn() {
            item() {
                Text(
                    text = "Detalle del personaje",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 35.sp,
                    color = Color.White
                )
            }
            item {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth()
                        .background(Color(
                            0xFF11212D
                        )),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        alignment = Alignment.Center,
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 16.dp)
                            .width(190.dp)
                            .height(295.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        contentScale = ContentScale.Crop,
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(characterDetail.images)
                            .size(Size.ORIGINAL)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Imagen de personaje",
                    )
                }
            }

            item() {
                Column {
                    Text(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 20.dp)
                            .fillMaxWidth(),
                        text = characterDetail.nameCharacter,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = characterDetail.nameKanjiCharacter,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth(),
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = characterDetail.descriptionCharacter,
                        modifier = Modifier.padding(16.dp),
                        fontSize = 15.sp,
                        color = Color.White,
                        textAlign = TextAlign.Justify
                    )
                }
            }
        }
    }
}


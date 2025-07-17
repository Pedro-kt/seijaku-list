package com.example.seijakulist.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.example.seijakulist.domain.models.AnimeCharactersDetail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeDetailScreen(
    navController: NavController,
    animeId: Int?,
    animeDetailViewModel: AnimeDetailViewModel = hiltViewModel(),
    animeCharacterDetailViewModel: AnimeCharacterDetailViewModel = hiltViewModel()
) {

    //viewModel de los detalles del anime
    val animeDetail by animeDetailViewModel.animeDetail.collectAsState()
    val isLoading by animeDetailViewModel.isLoading.collectAsState()
    val errorMessage by animeDetailViewModel.errorMessage.collectAsState()

    // viewModel de los personajes del anime
    val animeCharactersDetail by animeCharacterDetailViewModel.characters.collectAsState()
    val characterIsLoading by animeCharacterDetailViewModel.isLoading.collectAsState()
    val characterErrorMessage by animeCharacterDetailViewModel.errorMessage.collectAsState()

    LaunchedEffect(key1 = animeId) {
        if (animeId != null) {
            animeDetailViewModel.loadAnimeDetail(animeId)
            animeCharacterDetailViewModel.loadAnimeCharacters(animeId)
        }
    }

    when {
        isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF06141B)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }

        errorMessage != null -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF06141B)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorMessage ?: "Error desconocido",
                    color = Color.Red,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .background(color = Color(0xFF06141B))
            ) {
                item {

                    CenterAlignedTopAppBar(
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Color(
                                0xFF11212D
                            )
                        ),
                        title = {
                            Text(
                                text = "Detalle",
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


                }

                item {

                    Box {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(animeDetail?.imageUrl)
                                .size(Size.ORIGINAL)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Imagen de ${animeDetail?.title}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .background(Color.Black.copy(alpha = 0.7f))
                        )

                        Box {
                            Row(
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    top = 220.dp,
                                    end = 16.dp,
                                    bottom = 16.dp
                                )
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(animeDetail?.imageUrl)
                                        .size(Size.ORIGINAL)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "Imagen de ${animeDetail?.title}",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(3.dp))
                                )
                                Text(
                                    "${animeDetail?.title}",
                                    modifier = Modifier.padding(
                                        start = 16.dp,
                                        top = 96.dp,
                                        end = 16.dp,
                                        bottom = 16.dp
                                    ),
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 35.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }

                }

                item {
                    Column(
                        modifier = Modifier.height(60.dp).fillMaxWidth()
                            .background(color = Color(0xFF11212D))
                    ) {
                        Text(
                            "Sinopsis",
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                item {
                    Column(
                        modifier = Modifier.padding(
                            start = 16.dp,
                            top = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        ).fillMaxWidth()
                    ) {
                        Text(
                            "${animeDetail?.synopsis}",
                            color = Color.White
                        )
                    }
                }

                item {
                    Column(
                        modifier = Modifier.height(60.dp).fillMaxWidth()
                            .background(color = Color(0xFF11212D))
                    ) {
                        Text(
                            "Informacion",
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                item {

                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                "Genero:",
                                textAlign = TextAlign.Start,
                                color = Color.White
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                            ) {
                                animeDetail?.genres?.forEach { genre ->
                                    genre?.name?.let {
                                        Text(
                                            text = it,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.White,
                                            modifier = Modifier
                                                .background(
                                                    Color.DarkGray,
                                                    RoundedCornerShape(8.dp)
                                                )
                                                .padding(horizontal = 8.dp, vertical = 4.dp),
                                            textAlign = TextAlign.End
                                        )
                                    }
                                }
                            }
                        }
                        HorizontalDivider(
                            Modifier.padding(vertical = 15.dp),
                            DividerDefaults.Thickness,
                            color = Color.White
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                "Tipo de anime:",
                                textAlign = TextAlign.Start,
                                color = Color.White
                            )
                            Text(
                                "${animeDetail?.animeType}",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End,
                                color = Color.White
                            )
                        }
                        HorizontalDivider(
                            Modifier.padding(vertical = 15.dp),
                            DividerDefaults.Thickness,
                            color = Color.White
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                "Estado:",
                                textAlign = TextAlign.Start,
                                color = Color.White
                            )
                            Text(
                                "${animeDetail?.status}",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End,
                                color = Color.White
                            )
                        }
                        HorizontalDivider(
                            Modifier.padding(vertical = 15.dp),
                            DividerDefaults.Thickness,
                            color = Color.White
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                "Puntuacion:",
                                textAlign = TextAlign.Start,
                                color = Color.White
                            )
                            Text(
                                "${animeDetail?.score}",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End,
                                color = Color.White
                            )
                        }
                        HorizontalDivider(
                            Modifier.padding(vertical = 15.dp),
                            DividerDefaults.Thickness,
                            color = Color.White
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                "Episodios:",
                                textAlign = TextAlign.Start,
                                color = Color.White
                            )
                            Text(
                                "${animeDetail?.episodes}",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End,
                                color = Color.White
                            )
                        }
                        HorizontalDivider(
                            Modifier.padding(vertical = 15.dp),
                            DividerDefaults.Thickness,
                            color = Color.White
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                "Duracion:",
                                textAlign = TextAlign.Start,
                                color = Color.White
                            )
                            Text(
                                "${animeDetail?.duration}",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End,
                                color = Color.White
                            )
                        }
                        HorizontalDivider(
                            Modifier.padding(vertical = 15.dp),
                            DividerDefaults.Thickness,
                            color = Color.White
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                "Transmitido:",
                                textAlign = TextAlign.Start,
                                color = Color.White
                            )
                            Text(
                                "${animeDetail?.aired?.airedString}",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End,
                                color = Color.White
                            )
                        }
                        HorizontalDivider(
                            Modifier.padding(vertical = 15.dp),
                            DividerDefaults.Thickness,
                            color = Color.White
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                "Estudio:",
                                textAlign = TextAlign.Start,
                                color = Color.White
                            )
                            animeDetail?.studios?.forEach { studios ->
                                studios?.nameStudio?.let {
                                    Text(
                                        text = it,
                                        color = Color.White,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.End
                                    )
                                }
                            }
                        }
                        HorizontalDivider(
                            Modifier.padding(vertical = 15.dp),
                            DividerDefaults.Thickness,
                            color = Color.White
                        )
                    }
                }

                item {
                    Column(
                        modifier = Modifier.height(60.dp).fillMaxWidth()
                            .background(color = Color(0xFF11212D))
                    ) {
                        Text(
                            "Personajes: ${animeCharactersDetail.size}",
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                item {
                    // Aquí va la LazyRow con los personajes
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp)
                    ) {
                        items(animeCharactersDetail) { character ->
                            character?.let {
                                val imageUrl = it.imageCharacter?.jpg?.imageUrl ?: ""

                                Column(
                                    modifier = Modifier
                                        .width(140.dp)
                                        .padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(imageUrl)
                                            .size(Size.ORIGINAL)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "Imagen de personaje",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(120.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = it.nameCharacter?.takeIf { it.isNotBlank() }
                                            ?: "Nombre desconocido",
                                        style = MaterialTheme.typography.titleMedium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth(),
                                        color = Color.White
                                    )
                                    Text(
                                        text = it.role ?: "No especificado",
                                        style = MaterialTheme.typography.bodySmall,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth(),
                                        color = Color.White
                                    )
                                }
                            }
                        }

                    }

                }
            }
        }
    }
}
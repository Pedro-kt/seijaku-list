package com.example.seijakulist.ui.screens.characters

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    navController: NavController,
    characterId: Int,
    characterDetailViewModel: CharacterDetailViewModel = hiltViewModel(),
    characterPictureViewModel: CharacterPictureViewModel = hiltViewModel()
) {
    val characterDetail by characterDetailViewModel.characterDetail.collectAsState()
    val characterIsLoading by characterDetailViewModel.isLoading.collectAsState()
    val characterErrorMessage by characterDetailViewModel.errorMessage.collectAsState()

    val characterPictures by characterPictureViewModel.characterPictures.collectAsState()
    val characterPicturesIsLoading by characterPictureViewModel.isLoadingPicture.collectAsState()
    val characterPicturesErrorMessage by characterPictureViewModel.errorMessagePicture.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var selectedImageUrl by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = characterId) {
        characterDetailViewModel.loadCharacterDetail(characterId)
        characterPictureViewModel.loadCharacterPictures(characterId)
    }

    val overallIsLoading = characterIsLoading || characterPicturesIsLoading
    val overallErrorMessage = remember(characterErrorMessage, characterPicturesErrorMessage) {
        when {
            characterErrorMessage != null && characterPicturesErrorMessage != null ->
                "Error en detalle: ${characterErrorMessage}\nError en imágenes: ${characterPicturesErrorMessage}"
            characterErrorMessage != null -> characterErrorMessage
            characterPicturesErrorMessage != null -> characterPicturesErrorMessage
            else -> null
        }
    }

    if (overallIsLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF06141B)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color.White
            )
        }
    } else if (overallErrorMessage != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF06141B)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = overallErrorMessage,
                color = Color.Red,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    } else {
        Column(modifier = Modifier.fillMaxSize().background(color = Color(0xFF06141B))) {
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
                            .background(
                                Color(
                                    0xFF11212D
                                )
                            ),
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

                item {
                    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
                        Text(
                            modifier = Modifier
                                .padding(top = 20.dp, bottom = 20.dp)
                                .fillMaxWidth(),
                            text = characterDetail.nameCharacter,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = characterDetail.nameKanjiCharacter,
                            modifier = Modifier.padding(bottom = 16.dp)
                                .fillMaxWidth(),
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                item {
                    Column(
                        modifier = Modifier.height(60.dp).fillMaxWidth()
                            .background(color = Color(0xFF11212D))
                    ) {
                        Text(
                            "Descripcion: ",
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                item {
                    Column(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
                        Text(
                            text = characterDetail.descriptionCharacter,
                            modifier = Modifier.padding(bottom = 16.dp),
                            fontSize = 15.sp,
                            color = Color.White,
                            textAlign = TextAlign.Justify
                        )
                    }
                }

                item {
                    Column(
                        modifier = Modifier.height(60.dp).fillMaxWidth()
                            .background(color = Color(0xFF11212D))
                    ) {
                        Text(
                            "Imagenes de ${characterDetail.nameCharacter}: ",
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                item {
                    Column {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(top = 16.dp, bottom = 16.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(characterPictures) { characterPicture ->
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(characterPicture.characterPictures)
                                        .size(Size.ORIGINAL)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .width(190.dp)
                                        .height(295.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable(
                                            onClick = {
                                                selectedImageUrl = characterPicture.characterPictures
                                                showDialog = true
                                            },
                                        ),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                    if (showDialog) {
                        Dialog(
                            onDismissRequest = {
                                showDialog = false
                            },
                            properties = DialogProperties(usePlatformDefaultWidth = false)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        showDialog = false
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(selectedImageUrl)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "Imagen de personaje ampliada",
                                    modifier = Modifier
                                        .fillMaxWidth(0.9f)
                                        .clickable(enabled = false) {  }
                                        .clip(RoundedCornerShape(16.dp)),
                                )
                                IconButton(
                                    onClick = { showDialog = false },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(16.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Cerrar",
                                        tint = Color.White
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
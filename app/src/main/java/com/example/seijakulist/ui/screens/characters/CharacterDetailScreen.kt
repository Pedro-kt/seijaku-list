package com.example.seijakulist.ui.screens.characters

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.example.seijakulist.ui.components.LoadingScreen
import com.example.seijakulist.ui.components.TitleScreen
import com.example.seijakulist.ui.theme.RobotoBold
import com.example.seijakulist.ui.theme.RobotoRegular

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

    var expanded by remember { mutableStateOf(false) }

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
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingScreen()
        }
    } else if (overallErrorMessage != null) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
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
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = "Detalle del personaje",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontFamily = RobotoBold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                        ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 16.dp
                        )
                    ) {
                        Row() {
                            AsyncImage(
                                alignment = Alignment.Center,
                                modifier = Modifier
                                    .width(160.dp)
                                    .height(240.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop,
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(characterDetail.images)
                                    .size(Size.ORIGINAL)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Imagen de personaje",
                            )
                            Column() {
                                Text(
                                    modifier = Modifier
                                        .padding(top = 20.dp, bottom = 20.dp)
                                        .fillMaxWidth(),
                                    text = characterDetail.nameCharacter,
                                    fontSize = 28.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center,
                                    fontFamily = RobotoBold,
                                    lineHeight = 30.sp
                                )
                                Row(
                                    modifier = Modifier
                                        .padding(start = 16.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .height(32.dp)
                                        .wrapContentWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Kanji:",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        textAlign = TextAlign.Start,
                                        modifier = Modifier
                                            .wrapContentWidth()
                                            .padding(end = 8.dp),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 16.sp,
                                        fontFamily = RobotoRegular
                                    )
                                    Text(
                                        text = characterDetail.nameKanjiCharacter,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        textAlign = TextAlign.Start,
                                        modifier = Modifier
                                            .wrapContentWidth()
                                            .padding(end = 16.dp),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 12.sp,
                                        fontFamily = RobotoRegular
                                    )
                                }

                            }
                        }
                    }
                }

                item {
                    TitleScreen("Descripcion")
                }

                item {
                    Column() {
                        Text(
                            text = characterDetail.descriptionCharacter,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp,
                            fontFamily = RobotoRegular,
                            textAlign = TextAlign.Justify,
                            maxLines = if (expanded) Int.MAX_VALUE else 15,
                        )
                        Text(
                            text = if (expanded) "ver menos" else "ver más",
                            modifier = Modifier
                                .padding(top = 16.dp, bottom = 16.dp)
                                .clickable { expanded = !expanded },
                            color = MaterialTheme.colorScheme.inversePrimary,
                        )
                    }
                }

                item {
                    TitleScreen("Imagenes")
                }

                item {

                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
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
                                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
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
                                        .clickable(enabled = false) { }
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
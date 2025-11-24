package com.yumedev.seijakulist.ui.screens.characters

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.yumedev.seijakulist.ui.components.LoadingScreen
import com.yumedev.seijakulist.ui.components.TitleScreen
import com.yumedev.seijakulist.ui.theme.RobotoBold
import com.yumedev.seijakulist.ui.theme.RobotoRegular

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
        // Loading state with a top bar
        Column(Modifier.fillMaxSize()) {
            CharacterDetailTopBar(navController = navController, title = "Cargando...")
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    } else if (overallErrorMessage != null) {
        // Error state with a top bar
        Column(Modifier.fillMaxSize()) {
            CharacterDetailTopBar(navController = navController, title = "Error")
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Oops, algo salió mal:\n\n$overallErrorMessage",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = RobotoRegular,
                    lineHeight = 24.sp
                )
            }
        }

    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(380.dp)
                    ) {
                        // Background image with blur
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(characterDetail.images)
                                .size(Size.ORIGINAL)
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .blur(20.dp),
                            contentScale = ContentScale.Crop
                        )

                        // Gradient overlay
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Black.copy(alpha = 0.3f),
                                            Color.Black.copy(alpha = 0.6f),
                                            MaterialTheme.colorScheme.background
                                        ),
                                        startY = 0f,
                                        endY = 1200f
                                    )
                                )
                        )

                        // Back button
                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(8.dp)
                                .zIndex(2f)
                                .background(
                                    color = Color.Black.copy(alpha = 0.3f),
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = Color.White
                            )
                        }

                        // Character content
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Character image
                            Surface(
                                modifier = Modifier
                                    .size(180.dp),
                                shape = CircleShape,
                                shadowElevation = 12.dp,
                                color = MaterialTheme.colorScheme.surface
                            ) {
                                AsyncImage(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .border(
                                            width = 4.dp,
                                            color = MaterialTheme.colorScheme.surface,
                                            shape = CircleShape
                                        ),
                                    contentScale = ContentScale.Crop,
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(characterDetail.images)
                                        .size(Size.ORIGINAL)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "Imagen de ${characterDetail.nameCharacter}",
                                )
                            }

                            // Character name
                            Text(
                                text = characterDetail.nameCharacter,
                                fontSize = 32.sp,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                fontFamily = RobotoBold,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )

                            // Kanji name
                            if (characterDetail.nameKanjiCharacter.isNotEmpty()) {
                                Text(
                                    text = characterDetail.nameKanjiCharacter,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.Center,
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 20.sp,
                                    fontFamily = RobotoRegular,
                                    modifier = Modifier.padding(horizontal = 24.dp)
                                )
                            }
                        }
                    }
                }

                item {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Descripción",
                            fontSize = 24.sp,
                            fontFamily = RobotoBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                            ),
                            shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(Modifier.padding(20.dp)) {
                                Text(
                                    text = characterDetail.descriptionCharacter,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 16.sp,
                                    lineHeight = 24.sp,
                                    fontFamily = RobotoRegular,
                                    textAlign = TextAlign.Justify,
                                    maxLines = if (expanded) Int.MAX_VALUE else 8,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Surface(
                                    modifier = Modifier
                                        .padding(top = 12.dp)
                                        .align(Alignment.CenterHorizontally)
                                        .clickable { expanded = !expanded },
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = if (expanded) "Ver menos" else "Ver más",
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        fontFamily = RobotoBold,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }

                if (characterPictures.isNotEmpty()) {
                    item {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Galería",
                                fontSize = 24.sp,
                                fontFamily = RobotoBold,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )

                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                contentPadding = PaddingValues(horizontal = 20.dp)
                            ) {
                                items(characterPictures) { characterPicture ->
                                    ElevatedCard(
                                        modifier = Modifier
                                            .width(220.dp)
                                            .height(320.dp)
                                            .clickable {
                                                selectedImageUrl = characterPicture.characterPictures
                                                showDialog = true
                                            },
                                        shape = RoundedCornerShape(20.dp),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                                    ) {
                                        AsyncImage(
                                            model = ImageRequest.Builder(LocalContext.current)
                                                .data(characterPicture.characterPictures)
                                                .size(Size.ORIGINAL)
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop,
                                        )
                                    }
                                }
                            }
                        }
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
                            .background(Color.Black.copy(alpha = 0.95f))
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
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth(0.95f)
                                .clickable(enabled = false) { }
                                .clip(RoundedCornerShape(20.dp)),
                            contentScale = ContentScale.Fit
                        )

                        Surface(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(16.dp),
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            IconButton(
                                onClick = { showDialog = false }
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

@Composable
fun CharacterDetailTopBar(navController: NavController, title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 4.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 20.sp,
            fontFamily = RobotoBold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


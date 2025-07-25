package com.example.seijakulist.ui.screens.detail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
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
import com.example.seijakulist.R
import com.example.seijakulist.ui.components.BottomNavItemScreen
import com.example.seijakulist.ui.components.SliderSelectScore
import com.example.seijakulist.ui.components.TitleScreen
import com.example.seijakulist.ui.navigation.AppDestinations
import kotlinx.coroutines.launch

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

    val isAdded by animeDetailViewModel.isAdded.collectAsState()

    val RobotoRegular = FontFamily(
        Font(R.font.roboto_regular)
    )
    val RobotoBold = FontFamily(
        Font(R.font.roboto_bold, FontWeight.Bold)
    )

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.7f else 1f,
        label = "button_scale_animation"
    )

    //snakbar de notificacion
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = animeId) {
        if (animeId != null) {
            animeDetailViewModel.loadAnimeDetail(animeId)
            animeCharacterDetailViewModel.loadAnimeCharacters(animeId)
        }
    }

    var showDialog by remember { mutableStateOf(false) }
    var selectedImageUrl by remember { mutableStateOf<String?>(null) }

    val score = remember { mutableFloatStateOf(1f) }

    when {
        isLoading -> {
            Box(
                modifier = Modifier
                    .background(Color(0xFF050505))
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LinearProgressIndicator(color = Color.White, trackColor = Color.DarkGray)
            }
        }

        errorMessage != null -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF050505)),
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
            Scaffold(
                topBar = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .height(48.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(color = Color(0xFF121212))
                    ) {
                        Text(
                            text = "Detalle",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontFamily = RobotoBold,
                            modifier = Modifier.align(Alignment.Center)
                        )

                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver atras",
                                tint = Color.White
                            )
                        }
                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Compartir",
                                tint = Color.White
                            )
                        }
                    }
                },
                containerColor = Color(0xFF050505),
                floatingActionButton = {
                    FloatingActionButton(
                        modifier = Modifier
                            .padding(30.dp)
                            .size(60.dp)
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                            },
                        shape = FloatingActionButtonDefaults.shape,
                        interactionSource = interactionSource,
                        onClick = {
                            animeDetailViewModel.addAnimeToList()
                            scope.launch {
                                snackbarHostState.showSnackbar(message = "Anime agregado a tu lista", actionLabel = "Deshacer", duration = SnackbarDuration.Long)
                            }
                        },
                        containerColor = Color.White,
                        contentColor = contentColorFor(backgroundColor = Color.White)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Añadir anime",
                            tint = Color.Black
                        )
                    }
                },
                bottomBar = {
                    NavigationBar(
                        containerColor = Color(0xFF121212),

                        modifier = Modifier.navigationBarsPadding().clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    ) {
                        BottomNavItemScreen(navController)
                    }
                },
                snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState) { data ->
                        Snackbar(
                            snackbarData = data,
                            containerColor = Color.White,
                            contentColor = Color.Black,
                            actionContentColor = Color.Black,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            ) { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(color = Color(0xFF050505))
                ) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(350.dp).padding(top = 16.dp)
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(animeDetail?.images)
                                    .size(Size.ORIGINAL)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Imagen de fondo",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .blur(radius = 20.dp)
                                    .scale(1.1f),
                                contentScale = ContentScale.Crop,
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color(0xFF050505),
                                                Color.Transparent
                                            ),
                                            startY = 0f,
                                            endY = 400f,
                                        )
                                    )
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color(0xFF050505)
                                            ),
                                            startY = 400f,
                                            endY = Float.POSITIVE_INFINITY
                                        )
                                    )
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.TopStart
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(
                                            start = 16.dp,
                                        )
                                        .fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(animeDetail?.images)
                                            .size(Size.ORIGINAL)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "Imagen de portada",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .width(160.dp)
                                            .height(230.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable(
                                                onClick = {
                                                    selectedImageUrl = animeDetail?.images
                                                    showDialog = true
                                                },
                                            ),
                                    )

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Column(
                                        modifier = Modifier.weight(1f).padding(end = 16.dp, top = 16.dp)
                                    ) {
                                        Text(
                                            text = animeDetail?.title ?: "",
                                            color = Color.White,
                                            fontSize = 20.sp,
                                            fontFamily = RobotoBold,
                                            maxLines = 3,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 8.dp)) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = "Icono de estrellas",
                                                tint = Color.White
                                            )
                                            Text(
                                                text = animeDetail?.score.toString(),
                                                color = Color.White,
                                                fontSize = 16.sp,
                                            )
                                        }
                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 8.dp)) {
                                            Icon(
                                                imageVector = Icons.Default.Tv,
                                                contentDescription = "Icono de estrellas",
                                                tint = Color.White
                                            )
                                            Text(
                                                text = animeDetail?.typeAnime ?: "",
                                                color = Color.White,
                                                fontSize = 16.sp,
                                            )
                                        }
                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 8.dp)) {
                                            Icon(
                                                imageVector = Icons.Default.Alarm,
                                                contentDescription = "Icono de estrellas",
                                                tint = Color.White
                                            )
                                            Text(
                                                text = animeDetail?.status ?: "",
                                                color = Color.White,
                                                fontSize = 16.sp,
                                            )
                                        }
                                    }
                                }
                            }

                        }

                    }

                    item {
                        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
                            item {
                                animeDetail?.genres?.forEach { genre ->
                                    ElevatedFilterChip(
                                        selected = false,
                                        onClick = { /*TODO*/ },
                                        label = { Text(genre?.name ?: "No encontrado", color = Color.White) },
                                        modifier = Modifier.padding(end = 8.dp),
                                        colors = FilterChipDefaults.elevatedFilterChipColors(
                                            containerColor = Color(0xFF121212),
                                            labelColor = Color.White,
                                            selectedContainerColor = Color(0xFF121212),
                                            selectedLabelColor = Color.White
                                        )
                                    )
                                }
                            }
                        }
                    }

                    item {
                        TitleScreen("Descripcion")
                    }

                    item {
                        Text(
                            text = animeDetail?.synopsis ?: "Synopsis no encontrada",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontFamily = RobotoBold,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                        )
                    }

                    item {
                        TitleScreen("Personajes")
                    }

                    item {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp)
                        ) {
                            items(animeCharactersDetail) { character ->
                                character?.let { it ->
                                    val imageUrl = it.imageCharacter?.jpg?.imageUrl ?: ""

                                    if (it.role == "Main") {
                                        it.role = "Principal"
                                    } else if (it.role == "Supporting") {
                                        it.role = "Secundario"
                                    }

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
                                                .width(140.dp)
                                                .height(200.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .clickable() {
                                                    navController.navigate("${AppDestinations.CHARACTER_DETAIL_ROUTE}/${character.idCharacter}")
                                                }
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
                                            text = it.role,
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
                   /*
                    item {
                        SliderSelectScore(score = score)

                    }
                    */
                    item {
                        Button(
                            onClick = {
                                animeDetailViewModel.deleteAnimeToList(animeId!!)
                                scope.launch {
                                    snackbarHostState.showSnackbar(message = "Anime eliminado de tu lista", actionLabel = "Deshacer", duration = SnackbarDuration.Long)
                                }
                            },
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text(
                                text = "Eliminar anime",
                                color = Color.White
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
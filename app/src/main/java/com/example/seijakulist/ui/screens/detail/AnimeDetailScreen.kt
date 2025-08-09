package com.example.seijakulist.ui.screens.detail

import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Filter9Plus
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAbsoluteAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.example.seijakulist.R
import com.example.seijakulist.ui.components.BottomNavItemScreen
import com.example.seijakulist.ui.components.DescriptionAnime
import com.example.seijakulist.ui.components.SliderSelectScore
import com.example.seijakulist.ui.components.SubTitleIcon
import com.example.seijakulist.ui.components.TitleScreen
import com.example.seijakulist.ui.navigation.AppDestinations
import com.example.seijakulist.util.CleanName
import com.example.seijakulist.util.YoutubeUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeDetailScreen(
    navController: NavController,
    animeId: Int?,
    animeDetailViewModel: AnimeDetailViewModel = hiltViewModel(),
    animeCharacterDetailViewModel: AnimeCharacterDetailViewModel = hiltViewModel(),
    animeThemesViewModel: AnimeThemesViewModel = hiltViewModel()
) {

    val animeDetail by animeDetailViewModel.animeDetail.collectAsState()
    val isLoading by animeDetailViewModel.isLoading.collectAsState()
    val errorMessage by animeDetailViewModel.errorMessage.collectAsState()

    val animeCharactersDetail by animeCharacterDetailViewModel.characters.collectAsState()
    val characterIsLoading by animeCharacterDetailViewModel.isLoading.collectAsState()
    val characterErrorMessage by animeCharacterDetailViewModel.errorMessage.collectAsState()

    val animeThemes by animeThemesViewModel.themes.collectAsState()

    val isAdded by animeDetailViewModel.isAdded.collectAsState()

    val RobotoRegular = FontFamily(
        Font(R.font.roboto_regular)
    )
    val RobotoBold = FontFamily(
        Font(R.font.roboto_bold, FontWeight.Bold)
    )

    //snakbar de notificacion
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = animeId) {
        if (animeId != null) {
            animeThemesViewModel.animeThemes(animeId)
        }
    }

    var showDialog by remember { mutableStateOf(false) }
    var selectedImageUrl by remember { mutableStateOf<String?>(null) }

    val score = remember { mutableFloatStateOf(1f) }

    var expanded by remember { mutableStateOf(false) }

    val tabIcons = listOf(
        Icons.Default.Description,
        Icons.Default.People,
        Icons.AutoMirrored.Filled.List,
        Icons.Default.Business,
        Icons.Default.AddCircleOutline
    )

    val statusAnime = listOf("Viendo", "Completado", "Pendiente", "Abandonado", "Planeado")

    val statusColors = mapOf(
        "Viendo" to Color(0xFF66BB6A),
        "Completado" to Color(0xFF42A5F5),
        "Pendiente" to Color(0xFFFFCA28),
        "Abandonado" to Color(0xFFEF5350),
        "Planeado" to Color(0xFF78909C)
    )

    val selectedTabIndex = remember { mutableStateOf(0) }

    //webview
    val context = LocalContext.current

    var userOpinion by remember { mutableStateOf("") }
    val focusManager: FocusManager = LocalFocusManager.current

    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    var isSearchingStaff by remember { mutableStateOf(false) }

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
                containerColor = Color(0xFF050505), snackbarHost = {
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(350.dp)
                                .padding(top = 16.dp)
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(animeDetail?.images).size(Size.ORIGINAL).crossfade(true)
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
                                                Color(0xFF050505), Color.Transparent
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
                                                Color.Transparent, Color(0xFF050505)
                                            ), startY = 400f, endY = Float.POSITIVE_INFINITY
                                        )
                                    )
                            )
                            Box(
                                modifier = Modifier.fillMaxSize(),
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
                                            .data(animeDetail?.images).size(Size.ORIGINAL)
                                            .crossfade(true).build(),
                                        contentDescription = "Imagen de portada",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .width(160.dp)
                                            .height(240.dp)
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
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(end = 16.dp, top = 16.dp)
                                    ) {
                                        Text(
                                            text = animeDetail?.title ?: "",
                                            color = Color.White,
                                            fontSize = 20.sp,
                                            fontFamily = RobotoBold,
                                            maxLines = 3,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            modifier = Modifier.padding(top = 8.dp)
                                        ) {
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
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            modifier = Modifier.padding(top = 8.dp)
                                        ) {
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
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            modifier = Modifier.padding(top = 8.dp)
                                        ) {
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
                        TabRow(
                            selectedTabIndex = selectedTabIndex.value,
                            containerColor = Color.Transparent,
                            contentColor = Color.White,
                            modifier = Modifier
                                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .border(
                                    width = 1.dp,
                                    shape = RoundedCornerShape(24.dp),
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                        ) {
                            tabIcons.forEachIndexed { index, icon ->
                                Tab(
                                    selected = selectedTabIndex.value == index,
                                    onClick = { selectedTabIndex.value = index },
                                    selectedContentColor = Color.White,
                                    unselectedContentColor = Color.Gray
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(12.dp)
                                            .size(24.dp)
                                    )
                                }
                            }
                        }
                    }

                    if (selectedTabIndex.value == 0) {

                        item {
                            Card(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                colors = CardColors(
                                    contentColor = Color.White,
                                    containerColor = Color.Transparent,
                                    disabledContainerColor = Color.Red,
                                    disabledContentColor = Color.Cyan,
                                ),
                                border = BorderStroke(
                                    width = 1.dp, color = Color.White.copy(alpha = 0.7f)
                                )
                            ) {

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    TitleScreen("Generos:")

                                    LazyRow(
                                        contentPadding = PaddingValues(horizontal = 16.dp),
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        items(animeDetail?.genres.orEmpty()) { genre ->
                                            ElevatedFilterChip(
                                                selected = false,
                                                onClick = { /*TODO*/ },
                                                label = {
                                                    Text(
                                                        genre?.name ?: "No encontrado",
                                                        color = Color.White
                                                    )
                                                },
                                                modifier = Modifier.padding(end = 8.dp),
                                                colors = FilterChipDefaults.elevatedFilterChipColors(
                                                    containerColor = Color.Transparent,
                                                    labelColor = Color.White,
                                                    selectedContainerColor = Color(0xFF121212),
                                                    selectedLabelColor = Color.White
                                                ),
                                                border = FilterChipDefaults.filterChipBorder(
                                                    borderColor = Color.White,
                                                    borderWidth = 1.dp,
                                                    enabled = true,
                                                    selected = false
                                                )
                                            )
                                        }
                                    }
                                }

                            }
                        }

                        item {
                            Card(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth(),
                                colors = CardColors(
                                    contentColor = Color.White,
                                    containerColor = Color(0xff050505),
                                    disabledContainerColor = Color.Red,
                                    disabledContentColor = Color.Cyan,
                                ),
                                border = BorderStroke(
                                    width = 1.dp, color = Color.White.copy(alpha = 0.7f)
                                ),
                            ) {
                                TitleScreen("Synopsis")

                                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                                    Text(
                                        text = animeDetail?.synopsis ?: "Sinopsis no encontrada",
                                        color = Color.Gray,
                                        fontSize = 14.sp,
                                        fontFamily = RobotoRegular,
                                        textAlign = TextAlign.Justify,
                                        maxLines = if (expanded) Int.MAX_VALUE else 10,
                                    )
                                    Text(
                                        text = if (expanded) "ver menos" else "ver más",
                                        modifier = Modifier
                                            .padding(top = 16.dp, bottom = 16.dp)
                                            .clickable { expanded = !expanded },
                                        color = Color.White.copy(alpha = 0.5f)
                                    )
                                }
                            }
                        }

                        item {
                            Card(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                colors = CardColors(
                                    contentColor = Color.White,
                                    containerColor = Color.Transparent,
                                    disabledContainerColor = Color.Red,
                                    disabledContentColor = Color.Cyan,
                                ),
                                border = BorderStroke(
                                    width = 1.dp, color = Color.White.copy(alpha = 0.7f)
                                )
                            ) {
                                TitleScreen("Otros titulos")

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Icon(
                                        Icons.Default.Circle,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(top = 4.dp)
                                            .size(8.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "Título en Ingles:",
                                            color = Color.White,
                                            fontSize = 16.sp,
                                            fontFamily = RobotoBold
                                        )
                                        Text(
                                            text = animeDetail?.titleEnglish ?: "",
                                            color = Color.Gray,
                                            fontSize = 16.sp,
                                            fontFamily = RobotoRegular,
                                            textAlign = TextAlign.Start,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Icon(
                                        Icons.Default.Circle,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(top = 4.dp)
                                            .size(8.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "Título en Japonés:",
                                            color = Color.White,
                                            fontSize = 16.sp,
                                            fontFamily = RobotoBold
                                        )
                                        Text(
                                            text = animeDetail?.titleJapanese ?: "",
                                            color = Color.Gray,
                                            fontSize = 16.sp,
                                            fontFamily = RobotoRegular,
                                            textAlign = TextAlign.Start,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Card(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth(),
                                colors = CardColors(
                                    contentColor = Color.White,
                                    containerColor = Color.Transparent,
                                    disabledContainerColor = Color.Red,
                                    disabledContentColor = Color.Cyan,
                                ),
                                border = BorderStroke(
                                    width = 1.dp, color = Color.White.copy(alpha = 0.7f)
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    TitleScreen("Studio:")

                                    LazyRow(
                                        contentPadding = PaddingValues(horizontal = 16.dp),
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        items(animeDetail?.studios.orEmpty()) { studio ->
                                            ElevatedFilterChip(
                                                selected = false,
                                                onClick = { /*TODO*/ },
                                                label = {
                                                    Text(
                                                        studio?.nameStudio ?: "No encontrado",
                                                        color = Color.White
                                                    )
                                                },
                                                modifier = Modifier.padding(end = 8.dp),
                                                colors = FilterChipDefaults.elevatedFilterChipColors(
                                                    containerColor = Color.Transparent,
                                                    labelColor = Color.White,
                                                    selectedContainerColor = Color(0xFF121212),
                                                    selectedLabelColor = Color.White
                                                ),
                                                border = FilterChipDefaults.filterChipBorder(
                                                    borderColor = Color(0xFFFF00FF),
                                                    borderWidth = 1.dp,
                                                    enabled = true,
                                                    selected = false
                                                )
                                            )
                                        }
                                    }
                                }

                            }
                        }

                        item {
                            Card(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                colors = CardColors(
                                    contentColor = Color.White,
                                    containerColor = Color.Transparent,
                                    disabledContainerColor = Color.Red,
                                    disabledContentColor = Color.Cyan,
                                ),
                                border = BorderStroke(
                                    width = 1.dp, color = Color.White.copy(alpha = 0.7f)
                                )
                            ) {

                                TitleScreen("Informacion")

                                DescriptionAnime(
                                    Icons.Default.Star,
                                    "Puntuacion de la comunidad:",
                                    "${animeDetail?.score}"
                                )

                                DescriptionAnime(
                                    Icons.Default.People,
                                    "Puntuado por:",
                                    if (animeDetail?.scoreBy == 0) {
                                        "No encontrado"
                                    } else {
                                        "${animeDetail?.scoreBy} Personas"
                                    },
                                )

                                DescriptionAnime(
                                    Icons.Default.Tv, "Tipo de anime:", "${animeDetail?.typeAnime}"
                                )

                                DescriptionAnime(
                                    Icons.Default.FormatListNumbered,
                                    "Episodios:",
                                    "${animeDetail?.episodes}"
                                )

                                DescriptionAnime(
                                    Icons.Default.Timer, "Duracion:", "${animeDetail?.duration}"
                                )

                                DescriptionAnime(
                                    Icons.Default.WbSunny, "Temporada:", "${animeDetail?.season}"
                                )

                                DescriptionAnime(
                                    Icons.Default.AvTimer,
                                    "Año de lanzamiento:",
                                    "${animeDetail?.year}"
                                )

                                DescriptionAnime(
                                    Icons.Default.LiveTv, "Estado:", "${animeDetail?.status}"
                                )

                                DescriptionAnime(
                                    Icons.Default.CalendarMonth,
                                    "Transmitido:",
                                    "${animeDetail?.aired}"
                                )

                                DescriptionAnime(
                                    Icons.Default.BarChart,
                                    "Posicion global:",
                                    "${animeDetail?.rank}"
                                )

                                DescriptionAnime(
                                    Icons.Default.Filter9Plus, "Rating:", "${animeDetail?.rating}"
                                )

                                DescriptionAnime(
                                    Icons.AutoMirrored.Filled.LibraryBooks,
                                    "Proveniente de:",
                                    "${animeDetail?.source}"
                                )
                            }
                        }

                        item {
                            Card(
                                modifier = Modifier
                                    .padding(
                                        start = 16.dp, end = 16.dp, bottom = 16.dp
                                    )
                                    .fillMaxWidth(), colors = CardColors(
                                    contentColor = Color.White,
                                    containerColor = Color.Transparent,
                                    disabledContainerColor = Color.Red,
                                    disabledContentColor = Color.Cyan,
                                ), border = BorderStroke(
                                    width = 1.dp, color = Color.White.copy(alpha = 0.7f)
                                )
                            ) {
                                TitleScreen("Musica")

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Icon(
                                        Icons.Default.MusicNote,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(top = 4.dp)
                                            .size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "Opening:",
                                            color = Color.White,
                                            fontSize = 16.sp,
                                            fontFamily = RobotoBold
                                        )
                                        if (animeThemes.endings.isEmpty()) {
                                            Text(
                                                text = "No encontrado",
                                                color = Color.Gray,
                                                fontSize = 16.sp,
                                                fontFamily = RobotoRegular,
                                                textAlign = TextAlign.Start,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        } else {
                                            animeThemes.openings.forEach { opening ->
                                                val stringClean = CleanName(opening)
                                                val searchUrl =
                                                    YoutubeUtils.buildSearchUrl(stringClean)
                                                Text(
                                                    text = opening,
                                                    color = Color(0xFF00BCD4),
                                                    fontSize = 16.sp,
                                                    fontFamily = RobotoRegular,
                                                    textAlign = TextAlign.Start,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 6.dp)
                                                        .clickable() {
                                                            val encodedUrl = Uri.encode(searchUrl)
                                                            navController.navigate("${AppDestinations.WEB_VIEW}/$encodedUrl")
                                                        },
                                                    textDecoration = TextDecoration.Underline
                                                )
                                            }
                                        }
                                    }
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Icon(
                                        Icons.Default.MusicOff,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(top = 4.dp)
                                            .size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(modifier = Modifier.padding(bottom = 4.dp)) {
                                        Text(
                                            text = "Ending:",
                                            color = Color.White,
                                            fontSize = 16.sp,
                                            fontFamily = RobotoBold
                                        )
                                        if (animeThemes.endings.isEmpty()) {
                                            Text(
                                                text = "No encontrado",
                                                color = Color.Gray,
                                                fontSize = 16.sp,
                                                fontFamily = RobotoRegular,
                                                textAlign = TextAlign.Start,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        } else {
                                            animeThemes.endings.forEach { ending ->
                                                val stringClean = CleanName(ending)
                                                val searchUrl =
                                                    YoutubeUtils.buildSearchUrl(stringClean)
                                                Text(
                                                    text = ending,
                                                    color = Color(0xFF00BCD4),
                                                    fontSize = 16.sp,
                                                    fontFamily = RobotoRegular,
                                                    textAlign = TextAlign.Start,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 6.dp)
                                                        .clickable() {
                                                            val encodedUrl = Uri.encode(searchUrl)
                                                            navController.navigate("${AppDestinations.WEB_VIEW}/$encodedUrl")
                                                        },
                                                    textDecoration = TextDecoration.Underline
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (selectedTabIndex.value == 1) {

                        item {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TitleScreen("Personajes")

                                Spacer(modifier = Modifier.weight(1f))

                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Buscar personaje",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .padding(end = 32.dp)
                                        .size(24.dp)
                                        .clickable {
                                            isSearching = true
                                        })
                            }
                        }
                        item {
                            Card(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth()
                                    .pointerInput(Unit) {
                                        detectTapGestures(onTap = {
                                            focusManager.clearFocus()
                                        })
                                    }
                                    .imePadding(), colors = CardColors(
                                    contentColor = Color.White,
                                    containerColor = Color.Transparent,
                                    disabledContainerColor = Color.Red,
                                    disabledContentColor = Color.Cyan,
                                ), border = BorderStroke(
                                    width = 1.dp, color = Color.White.copy(alpha = 0.7f)
                                )) {

                                when {
                                    characterIsLoading -> {
                                        Box(
                                            modifier = Modifier
                                                .background(Color(0xFF050505))
                                                .height(250.dp)
                                                .fillMaxWidth(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            LinearProgressIndicator(
                                                color = Color.White, trackColor = Color.DarkGray
                                            )
                                        }
                                    }

                                    characterErrorMessage != null -> {
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

                                        if (isSearching) {
                                            Row(
                                                modifier = Modifier
                                                    .padding(
                                                        start = 16.dp, end = 16.dp, top = 16.dp
                                                    )
                                                    .fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                OutlinedTextField(
                                                    modifier = Modifier.weight(1f),
                                                    value = searchQuery,
                                                    onValueChange = { searchQuery = it },
                                                    label = {
                                                        Text(
                                                            "Buscar personaje...",
                                                            color = Color.White
                                                        )
                                                    },
                                                    trailingIcon = {
                                                        IconButton(onClick = {
                                                            isSearching = false
                                                            searchQuery = ""
                                                        }) {
                                                            Icon(
                                                                Icons.Default.Close,
                                                                contentDescription = "Cerrar búsqueda"
                                                            )
                                                        }
                                                    },
                                                    placeholder = {
                                                        Text(
                                                            text = "Ingrese término de búsqueda...",
                                                            color = Color.White.copy(alpha = 0.5f),
                                                            modifier = Modifier.padding(start = 4.dp)
                                                        )
                                                    },
                                                    singleLine = true,
                                                    shape = RoundedCornerShape(50.dp),
                                                    colors = TextFieldDefaults.colors(
                                                        focusedTextColor = Color.White,
                                                        unfocusedTextColor = Color.White,
                                                        focusedContainerColor = Color(0xFF050505),
                                                        unfocusedContainerColor = Color(
                                                            0xFF050505
                                                        ),
                                                        focusedIndicatorColor = Color.White.copy(
                                                            alpha = 0.5f
                                                        ),
                                                        unfocusedIndicatorColor = Color.White.copy(
                                                            alpha = 0.3f
                                                        ),
                                                        cursorColor = Color.White,
                                                        focusedLabelColor = Color.White.copy(
                                                            alpha = 0.7f
                                                        ),
                                                        unfocusedLabelColor = Color.White.copy(
                                                            alpha = 0.5f
                                                        )
                                                    ),
                                                    leadingIcon = {
                                                        Icon(
                                                            imageVector = Icons.Default.Search,
                                                            contentDescription = "Buscar",
                                                            tint = Color.White,
                                                            modifier = Modifier.size(24.dp)
                                                        )
                                                    },
                                                )
                                            }
                                        }

                                        LazyRow(
                                            modifier = Modifier.fillMaxWidth(),
                                            contentPadding = PaddingValues(
                                                horizontal = 8.dp, vertical = 16.dp
                                            )
                                        ) {
                                            // Filtramos la lista de personajes basada en el texto de búsqueda
                                            val filteredCharacters = if (searchQuery.isBlank()) {
                                                animeCharactersDetail
                                            } else {
                                                animeCharactersDetail.filter { character ->
                                                    character?.nameCharacter?.contains(
                                                        searchQuery, ignoreCase = true
                                                    ) == true
                                                }
                                            }

                                            items(filteredCharacters) { character ->
                                                character?.let { characterItem ->
                                                    val imageUrl =
                                                        characterItem.imageCharacter?.jpg?.imageUrl.orEmpty()
                                                    val translatedRole = when (characterItem.role) {
                                                        "Main" -> "Principal"
                                                        "Supporting" -> "Secundario"
                                                        else -> characterItem.role
                                                    }

                                                    Column(
                                                        modifier = Modifier
                                                            .width(140.dp)
                                                            .padding(8.dp),
                                                        horizontalAlignment = Alignment.CenterHorizontally
                                                    ) {
                                                        AsyncImage(
                                                            model = ImageRequest.Builder(
                                                                LocalContext.current
                                                            ).data(imageUrl).size(Size.ORIGINAL)
                                                                .crossfade(true).build(),
                                                            contentDescription = "Imagen de personaje",
                                                            contentScale = ContentScale.Crop,
                                                            modifier = Modifier
                                                                .width(140.dp)
                                                                .height(200.dp)
                                                                .clip(RoundedCornerShape(8.dp))
                                                                .clickable {
                                                                    navController.navigate("${AppDestinations.CHARACTER_DETAIL_ROUTE}/${characterItem.idCharacter}")
                                                                })
                                                        Spacer(modifier = Modifier.height(8.dp))
                                                        Text(text = characterItem.nameCharacter?.takeIf { it.isNotBlank() }
                                                            ?: "Nombre desconocido",
                                                            style = MaterialTheme.typography.titleMedium,
                                                            maxLines = 1,
                                                            overflow = TextOverflow.Ellipsis,
                                                            textAlign = TextAlign.Center,
                                                            modifier = Modifier.fillMaxWidth(),
                                                            color = Color.White)
                                                        Text(text = translatedRole.takeIf { it.isNotBlank() }
                                                            ?: "Rol desconocido",
                                                            style = MaterialTheme.typography.bodySmall,
                                                            maxLines = 1,
                                                            overflow = TextOverflow.Ellipsis,
                                                            textAlign = TextAlign.Center,
                                                            modifier = Modifier.fillMaxWidth(),
                                                            color = Color.White)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TitleScreen("Staff")

                                Spacer(modifier = Modifier.weight(1f))

                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Buscar Staff",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .padding(end = 32.dp)
                                        .size(24.dp)
                                        .clickable {
                                            isSearchingStaff = true
                                        })
                            }

                            Card(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth()
                                    .pointerInput(Unit) {
                                        detectTapGestures(onTap = {
                                            focusManager.clearFocus()
                                        })
                                    }
                                    .imePadding(), colors = CardColors(
                                    contentColor = Color.White,
                                    containerColor = Color.Transparent,
                                    disabledContainerColor = Color.Red,
                                    disabledContentColor = Color.Cyan,
                                ), border = BorderStroke(
                                    width = 1.dp, color = Color.White.copy(alpha = 0.7f)
                                )) {
                                if (isSearchingStaff) {
                                    Row(
                                        modifier = Modifier
                                            .padding(
                                                start = 16.dp, end = 16.dp, top = 16.dp
                                            )
                                            .fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        OutlinedTextField(
                                            modifier = Modifier.weight(1f),
                                            value = searchQuery,
                                            onValueChange = { searchQuery = it },
                                            label = {
                                                Text("Buscar staff...", color = Color.White)
                                            },
                                            trailingIcon = {
                                                IconButton(onClick = {
                                                    isSearchingStaff = false
                                                    searchQuery = ""
                                                }) {
                                                    Icon(
                                                        Icons.Default.Close,
                                                        contentDescription = "Cerrar búsqueda"
                                                    )
                                                }
                                            },
                                            placeholder = {
                                                Text(
                                                    text = "Ingrese término de búsqueda...",
                                                    color = Color.White.copy(alpha = 0.5f),
                                                    modifier = Modifier.padding(start = 4.dp)
                                                )
                                            },
                                            singleLine = true,
                                            shape = RoundedCornerShape(50.dp),
                                            colors = TextFieldDefaults.colors(
                                                focusedTextColor = Color.White,
                                                unfocusedTextColor = Color.White,
                                                focusedContainerColor = Color(0xFF050505),
                                                unfocusedContainerColor = Color(0xFF050505),
                                                focusedIndicatorColor = Color.White.copy(
                                                    alpha = 0.5f
                                                ),
                                                unfocusedIndicatorColor = Color.White.copy(
                                                    alpha = 0.3f
                                                ),
                                                cursorColor = Color.White,
                                                focusedLabelColor = Color.White.copy(alpha = 0.7f),
                                                unfocusedLabelColor = Color.White.copy(alpha = 0.5f)
                                            ),
                                            leadingIcon = {
                                                Icon(
                                                    imageVector = Icons.Default.Search,
                                                    contentDescription = "Buscar",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                            },
                                        )
                                    }
                                }
                            }
                        }
                    }
                    if (selectedTabIndex.value == 2) {

                    }
                    if (selectedTabIndex.value == 3) {

                    }
                    if (selectedTabIndex.value == 4) {
                        item {
                            var selectedStatus by remember { mutableStateOf<String?>(null) }
                            var userRating by remember { mutableStateOf(0.0f) }

                            val isFormValid = selectedStatus != null
                            Card(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                                    .pointerInput(Unit) {
                                        detectTapGestures(onTap = {
                                            focusManager.clearFocus()
                                        })
                                    }, colors = CardColors(
                                    contentColor = Color.White,
                                    containerColor = Color.Transparent,
                                    disabledContainerColor = Color.Red,
                                    disabledContentColor = Color.Cyan,
                                ), border = BorderStroke(
                                    width = 1.dp, color = Color.White.copy(alpha = 0.7f)
                                )
                            ) {
                                Column() {
                                    Text(
                                        text = "Añadir a mi lista",
                                        color = Color.White,
                                        fontSize = 24.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth(),
                                        fontFamily = RobotoBold
                                    )

                                    SubTitleIcon("Estado del anime:", Icons.Default.AddTask)

                                    Box(
                                        modifier = Modifier
                                            .padding(horizontal = 10.dp)
                                            .fillMaxWidth(), contentAlignment = Alignment.Center
                                    ) {
                                        LazyRow(
                                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            items(statusAnime) { status ->
                                                var status = status

                                                val isSelected = selectedStatus == status

                                                val chipColor =
                                                    statusColors[status] ?: Color.LightGray

                                                FilterChip(
                                                    onClick = {
                                                        selectedStatus =
                                                            if (isSelected) null else status

                                                        if (status == "Planeado") {
                                                            userRating = 0.0f
                                                        }
                                                    },
                                                    label = {
                                                        Text(
                                                            text = status,
                                                            fontSize = 16.sp,
                                                            modifier = Modifier.padding(6.dp)
                                                        )
                                                    },
                                                    selected = isSelected,
                                                    colors = FilterChipDefaults.elevatedFilterChipColors(
                                                        containerColor = if (isSelected) chipColor else Color.Black,
                                                        selectedContainerColor = chipColor,
                                                        labelColor = if (isSelected) Color.Black else chipColor,
                                                        selectedLabelColor = Color.Black
                                                    ),
                                                    border = BorderStroke(
                                                        width = 1.dp,
                                                        color = if (isSelected) chipColor else chipColor
                                                    ),
                                                    trailingIcon = {
                                                        if (isSelected) {
                                                            Icon(
                                                                imageVector = Icons.Default.Check,
                                                                contentDescription = "Seleccionado",
                                                                tint = Color.Black
                                                            )
                                                        }
                                                    })
                                            }
                                        }
                                    }
                                    SubTitleIcon("Puntuacion del usuario:", Icons.Default.Star)

                                    if (selectedStatus != "Planeado") {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp, vertical = 8.dp),
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "%.1f".format(userRating),
                                                color = Color.White,
                                                fontSize = 14.sp,
                                                modifier = Modifier.padding(end = 8.dp)
                                            )

                                            for (i in 1..10) {
                                                val starValue = i.toFloat()
                                                val isFilled = starValue <= userRating
                                                val isHalfFilled =
                                                    (starValue - 0.5f) <= userRating && !isFilled

                                                Icon(
                                                    imageVector = when {
                                                        isFilled -> Icons.Default.Star
                                                        isHalfFilled -> Icons.AutoMirrored.Filled.StarHalf
                                                        else -> Icons.Default.StarBorder
                                                    },
                                                    contentDescription = "Puntuación de $starValue estrellas",
                                                    modifier = Modifier
                                                        .size(32.dp)
                                                        .clickable {
                                                            userRating =
                                                                if (userRating == starValue) {
                                                                    starValue - 0.5f
                                                                } else {
                                                                    starValue
                                                                }
                                                        },
                                                    tint = if (isFilled || isHalfFilled) Color(
                                                        0xFFFFD700
                                                    ) else Color.Gray
                                                )
                                            }
                                        }
                                    } else {
                                        Text(
                                            "No puedes puntuar el anime si el estado es 'Planeado'",
                                            color = Color.White,
                                            modifier = Modifier
                                                .padding(
                                                    start = 16.dp, end = 16.dp
                                                )
                                                .fillMaxWidth(),
                                            textAlign = TextAlign.Center
                                        )
                                    }

                                    SubTitleIcon("Tu opinión:", Icons.Default.ChatBubble)

                                    OutlinedTextField(
                                        value = userOpinion,
                                        onValueChange = { userOpinion = it },
                                        label = { Text("Escribe tu reseña aquí...") },
                                        placeholder = { Text("Ej: 'Una gran historia con personajes inolvidables.'") },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp)
                                            .height(200.dp)
                                    )

                                    SubTitleIcon(
                                        "Fecha que empezaste a mirar",
                                        Icons.Default.CalendarMonth
                                    )
                                    Text(
                                        "En desarrollo",
                                        color = Color.White,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                    SubTitleIcon(
                                        "Fecha que terminaste de mirar",
                                        Icons.Default.CalendarToday
                                    )
                                    Text(
                                        "En desarrollo",
                                        color = Color.White,
                                        modifier = Modifier.padding(16.dp)
                                    )

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Button(
                                            onClick = {
                                                if (isFormValid) {
                                                    val scoreToPass =
                                                        if (selectedStatus == "Planeado") {
                                                            0.0f
                                                        } else {
                                                            userRating
                                                        }

                                                    animeDetailViewModel.addAnimeToList(
                                                        userScore = scoreToPass,
                                                        userStatus = selectedStatus!!,
                                                        userOpinion = userOpinion
                                                    )
                                                    scope.launch {
                                                        snackbarHostState.showSnackbar(
                                                            message = "Anime agregado a tu lista",
                                                            actionLabel = "Deshacer",
                                                            duration = SnackbarDuration.Long
                                                        )
                                                    }
                                                } else {
                                                    scope.launch {
                                                        snackbarHostState.showSnackbar(
                                                            message = "Debes seleccionar un estado para guardar.",
                                                            actionLabel = "Deshacer",
                                                            duration = SnackbarDuration.Long
                                                        )
                                                    }
                                                }
                                            },
                                            enabled = isFormValid,
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .fillMaxWidth()
                                        ) {
                                            Text(text = "Guardar")
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
                        }, properties = DialogProperties(usePlatformDefaultWidth = false)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    showDialog = false
                                }, contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(selectedImageUrl).crossfade(true).build(),
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
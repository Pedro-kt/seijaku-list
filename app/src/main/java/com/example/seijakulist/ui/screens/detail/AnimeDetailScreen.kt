package com.example.seijakulist.ui.screens.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Filter9Plus
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.DatePicker
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.example.seijakulist.R
import com.example.seijakulist.ui.components.DescriptionAnime
import com.example.seijakulist.ui.components.LoadingScreen
import com.example.seijakulist.ui.components.TitleWithPadding
import com.example.seijakulist.ui.navigation.AppDestinations
import com.example.seijakulist.ui.theme.RobotoBold
import com.example.seijakulist.ui.theme.RobotoRegular
import java.text.SimpleDateFormat
import androidx.compose.ui.graphics.vector.ImageVector
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

    var expanded by rememberSaveable { mutableStateOf(false) }

    val tabIcons = listOf(
        Icons.Default.Description,
        Icons.Default.People,
        Icons.AutoMirrored.Filled.List,
        Icons.Default.Business,
        Icons.Default.FavoriteBorder
    )

    val statusAnime = listOf("Viendo", "Completado", "Pendiente", "Abandonado", "Planeado")

    val statusColors = mapOf(
        "Viendo" to Color(0xFF66BB6A),
        "Completado" to Color(0xFF42A5F5),
        "Pendiente" to Color(0xFFFFCA28),
        "Abandonado" to Color(0xFFEF5350),
        "Planeado" to Color(0xFF78909C)
    )


    var selectedTabIndex by remember { mutableStateOf(0) }

    //webview
    val context = LocalContext.current

    var userOpinion by remember { mutableStateOf("") }
    val focusManager: FocusManager = LocalFocusManager.current

    var isSearching by rememberSaveable { mutableStateOf(false) }
    var searchQuery by rememberSaveable { mutableStateOf("") }

    var expandedStatus by remember { mutableStateOf(false) }

    when {
        isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingScreen()
            }
        }

        errorMessage != null -> {
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
                        text = "Detalle del anime",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp,
                        fontFamily = com.example.seijakulist.ui.theme.RobotoBold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = RobotoBold
                    )
                }
            }
        }

        else -> {
            Scaffold(
                snackbarHost = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        SnackbarHost(hostState = snackbarHostState) { data ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                shape = RoundedCornerShape(12.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = data.visuals.message,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    if (data.visuals.actionLabel != null) {
                                        Text(
                                            text = data.visuals.actionLabel!!,
                                            color = MaterialTheme.colorScheme.primary,
                                            style = MaterialTheme.typography.labelLarge,
                                            modifier = Modifier
                                                .padding(start = 16.dp)
                                                .clickable { data.performAction() }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            ) { paddingValues ->
                Column(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(
                        key1 = Unit
                    ) {
                        detectTapGestures(
                            onTap = {
                                focusManager.clearFocus()
                            }
                        )
                    }
            ) {
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
                        text = "Detalle del anime",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp,
                        fontFamily = com.example.seijakulist.ui.theme.RobotoBold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(350.dp)
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
                                                MaterialTheme.colorScheme.background,
                                                Color.Transparent,
                                                MaterialTheme.colorScheme.background
                                            ),
                                            startY = 0f,
                                            endY = Float.POSITIVE_INFINITY
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
                                            .weight(1f),
                                        verticalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = animeDetail?.title ?: "",
                                            color = MaterialTheme.colorScheme.onSurface,
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
                                                tint = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = animeDetail?.score.toString(),
                                                color = MaterialTheme.colorScheme.onSurface,
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
                                                tint = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = animeDetail?.typeAnime ?: "",
                                                color = MaterialTheme.colorScheme.onSurface,
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
                                                tint = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = animeDetail?.status ?: "",
                                                color = MaterialTheme.colorScheme.onSurface,
                                                fontSize = 16.sp,
                                            )
                                        }
                                    }
                                }
                            }

                        }

                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .border(
                                    width = 1.dp,
                                    shape = RoundedCornerShape(24.dp),
                                    color = MaterialTheme.colorScheme.onSurface
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            tabIcons.forEachIndexed { index, icon ->
                                val isSelected = selectedTabIndex == index

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                                        )
                                        .clickable { selectedTabIndex = index }
                                        .padding(12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp),
                                        tint = if (isSelected) Color.White else Color.Gray
                                    )
                                }

                                if (index < tabIcons.size - 1) {
                                    VerticalDivider(
                                        modifier = Modifier
                                            .height(48.dp)
                                            .width(1.dp),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    if (selectedTabIndex == 0) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            ) {
                                TitleWithPadding("Generos:")

                                LazyRow(
                                    contentPadding = PaddingValues(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) { //TODO: Se ha modificado la distribución para evitar overflows.
                                    items(animeDetail?.genres.orEmpty()) { genre ->
                                        genre?.let {
                                            ElevatedFilterChip(
                                                selected = false,
                                                onClick = {
                                                    // Navegar a la pantalla de búsqueda con el género seleccionado
                                                },
                                                label = {
                                                    Text(
                                                        it.name ?: "No encontrado",
                                                        color = MaterialTheme.colorScheme.onSurface,
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                },
                                                leadingIcon = {
                                                    Icon(Icons.Default.FilterList, contentDescription = "Genre Icon", tint = MaterialTheme.colorScheme.secondary)
                                                },
                                                colors = FilterChipDefaults.elevatedFilterChipColors(
                                                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                                ),
                                                elevation = FilterChipDefaults.filterChipElevation(elevation = 4.dp),
                                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                                            )
                                        }
                                    }
                                }
                            }


                        }
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        item {
                            TitleWithPadding("Synopsis")

                            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                                var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
                                val isExpandable = textLayoutResult?.hasVisualOverflow ?: false

                                Text(
                                    text = animeDetail?.synopsis ?: "Sinopsis no encontrada",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 14.sp,
                                    fontFamily = RobotoRegular,
                                    textAlign = TextAlign.Justify,
                                    maxLines = if (expanded) Int.MAX_VALUE else 8,
                                    onTextLayout = { textLayoutResult = it }
                                )
                                if (isExpandable || expanded) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .align(Alignment.End)
                                            .padding(top = 8.dp, bottom = 16.dp)
                                            .clickable { expanded = !expanded },
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Text(
                                            text = if (expanded) "Ver menos" else "Ver más",
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                        val rotation: Float by animateFloatAsState(targetValue = if (expanded) 180f else 0f, label = "rotation")
                                        Icon(
                                            imageVector = Icons.Default.ArrowDropDown,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.rotate(rotation)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        item {

                            TitleWithPadding("Otros titulos")

                            OtherTitleItem(
                                label = "Título en Inglés:",
                                value = animeDetail?.titleEnglish
                            )
                            OtherTitleItem(
                                label = "Título en Japonés:",
                                value = animeDetail?.titleJapanese
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Studio",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    items(animeDetail?.studios.orEmpty()) { studio ->
                                        Card(
                                            onClick = { /* TODO: Navegar a una pantalla de búsqueda o del estudio */ },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                                            ),
                                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                        ) {
                                            Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.Business, contentDescription = "Studio Icon", modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    studio?.nameStudio ?: "No encontrado",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        item {

                            TitleWithPadding("Informacion")

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

                            Spacer(modifier = Modifier.height(16.dp))

                        }
                        item {
                            TitleWithPadding("Temas")

                            ThemeItem(
                                title = "Openings",
                                icon = Icons.Default.MusicNote,
                                themes = animeThemes.openings
                            )
                            ThemeItem(
                                title = "Endings",
                                icon = Icons.Default.MusicOff,
                                themes = animeThemes.endings
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    if (selectedTabIndex == 1) {
                        item {
                            SectionHeaderWithSearch(
                                title = "Personajes",
                                isSearching = isSearching,
                                onSearchClick = { isSearching = true }
                            )
                        }
                        item {

                            when {
                                characterIsLoading -> {
                                    Box(
                                        modifier = Modifier
                                            .height(250.dp)
                                            .fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        LinearProgressIndicator(
                                            color = MaterialTheme.colorScheme.inversePrimary,
                                            trackColor = MaterialTheme.colorScheme.outline
                                        )
                                    }
                                }

                                characterErrorMessage != null -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .height(250.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = characterErrorMessage!!,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontSize = 16.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }

                                else -> {

                                    AnimatedVisibility(
                                        visible = isSearching,
                                        enter = slideInHorizontally(initialOffsetX = { it }),
                                        exit = slideOutHorizontally(targetOffsetX = { it })
                                    ) {
                                        OutlinedTextField(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            value = searchQuery,
                                            onValueChange = { searchQuery = it },
                                            trailingIcon = {
                                                IconButton(onClick = {
                                                    isSearching = false
                                                    searchQuery = ""
                                                    focusManager.clearFocus()
                                                }) {
                                                    Icon(
                                                        Icons.Default.Close,
                                                        contentDescription = "Cerrar búsqueda",
                                                        tint = MaterialTheme.colorScheme.onSurface,
                                                    )
                                                }
                                            },
                                            placeholder = {
                                                Text(
                                                    text = "Buscar personaje",
                                                    color = MaterialTheme.colorScheme.onSurface.copy(
                                                        alpha = 0.5f
                                                    ),
                                                    modifier = Modifier.padding(start = 4.dp)
                                                )
                                            },
                                            singleLine = true,
                                            shape = RoundedCornerShape(50.dp),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                                                focusedBorderColor = MaterialTheme.colorScheme.inversePrimary,
                                                unfocusedBorderColor = Color.Transparent,
                                                cursorColor = MaterialTheme.colorScheme.inversePrimary,
                                                focusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(
                                                    alpha = 0.7f
                                                ),
                                                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(
                                                    alpha = 0.5f
                                                )
                                            ),
                                        )
                                    }

                                    if (searchQuery.isBlank()) {
                                        TitleWithPadding("Personajes")
                                    }


                                    LazyRow(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentPadding = PaddingValues(
                                            horizontal = 8.dp
                                        )
                                    ) {
                                        val filteredCharacters = if (searchQuery.isBlank()) {
                                            animeCharactersDetail
                                        } else {
                                            animeCharactersDetail.filter { character ->
                                                character.nameCharacter?.contains(
                                                    searchQuery, ignoreCase = true
                                                ) == true
                                            }
                                        }

                                        items(filteredCharacters) { character ->
                                            character.let { characterItem ->
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
                                                            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
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
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                    Text(text = translatedRole.takeIf { it.isNotBlank() }
                                                        ?: "Rol desconocido",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis,
                                                        textAlign = TextAlign.Center,
                                                        modifier = Modifier.fillMaxWidth(),
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }
                    if (selectedTabIndex == 2) {

                    }
                    if (selectedTabIndex == 3) {

                    }
                    if (selectedTabIndex == 4) {
                        item {
                            var selectedStatus by remember { mutableStateOf<String?>(null) }
                            var userRating by remember { mutableStateOf(0.0f) }

                            val isFormValid = selectedStatus != null

                            Column() {
                                Text(
                                    text = "Añadir a mi lista",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 24.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    fontFamily = RobotoBold
                                )

                                Text(
                                    text = "Estado",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 22.sp,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 16.dp)
                                        .fillMaxWidth(),
                                    fontFamily = RobotoBold
                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Column {
                                        Button(
                                            onClick = { expandedStatus = !expandedStatus },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(50.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (selectedStatus != null) statusColors[selectedStatus]!! else MaterialTheme.colorScheme.surfaceContainerHigh,
                                                contentColor = if (selectedStatus != null) Color.Black else MaterialTheme.colorScheme.onSurface
                                            ),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = selectedStatus ?: "Seleccionar estado",
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .padding(start = 8.dp),
                                                    textAlign = if (selectedStatus != null) TextAlign.Center else TextAlign.Start,
                                                )
                                                val rotation: Float by animateFloatAsState(
                                                    targetValue = if (expandedStatus) 90f else 0f,
                                                    label = "Arrow Rotation"
                                                )
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                                                    contentDescription = null,
                                                    modifier = Modifier
                                                        .size(24.dp)
                                                        .rotate(rotation),
                                                    tint = if (selectedStatus != null) Color.Black else MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }

                                        AnimatedVisibility(visible = expandedStatus) {
                                            LazyVerticalGrid(
                                                columns = GridCells.Fixed(2),
                                                modifier = Modifier
                                                    .padding(top = 8.dp)
                                                    .height(180.dp),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                verticalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                items(statusAnime.size) { index ->
                                                    val status = statusAnime[index]
                                                    Card(
                                                        onClick = {
                                                            if (selectedStatus == status) {
                                                                selectedStatus = null
                                                            } else {
                                                                selectedStatus = status
                                                            }
                                                            expandedStatus = false
                                                        },
                                                        modifier = Modifier.fillMaxHeight(),
                                                        colors = CardDefaults.cardColors(
                                                            containerColor = statusColors[status]
                                                                ?: Color.Gray
                                                        )
                                                    ) {
                                                        Box(
                                                            modifier = Modifier
                                                                .fillMaxSize()
                                                                .padding(8.dp),
                                                            contentAlignment = Alignment.Center
                                                        ) {
                                                            Text(
                                                                text = status,
                                                                color = Color.Black,
                                                                fontWeight = FontWeight.Bold
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                Text(
                                    text = "Calificacion",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 22.sp,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 16.dp)
                                        .fillMaxWidth(),
                                    fontFamily = RobotoBold
                                )

                                if (selectedStatus != "Planeado") {
                                    RatingBar(
                                        rating = userRating,
                                        onRatingChange = { userRating = it })
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

                                Text(
                                    text = "Opinion",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 22.sp,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 16.dp)
                                        .fillMaxWidth(),
                                    fontFamily = RobotoBold
                                )

                                OutlinedTextField(
                                    value = userOpinion,
                                    onValueChange = { userOpinion = it },
                                    placeholder = {
                                        Text(text = "Escribe tu opinion")
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                        .height(200.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                        focusedBorderColor = Color.Transparent,
                                        unfocusedBorderColor = Color.Transparent,
                                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                        cursorColor = MaterialTheme.colorScheme.inversePrimary,
                                        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(
                                            alpha = 0.6f
                                        ),
                                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(
                                            alpha = 0.6f
                                        )
                                    )
                                )

                                Text(
                                    text = "Fechas",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 22.sp,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 16.dp)
                                        .fillMaxWidth(),
                                    fontFamily = RobotoBold
                                )

                                var startDate by remember { mutableStateOf<Long?>(null) }
                                var endDate by remember { mutableStateOf<Long?>(null) }
                                val dateFormat = SimpleDateFormat("dd/MM/yyyy")

                                var showStartDatePicker by remember { mutableStateOf(false) }
                                var showEndDatePicker by remember { mutableStateOf(false) }

                                if (showStartDatePicker) {
                                    DatePickerDialog(
                                        onDismissRequest = { showStartDatePicker = false },
                                        confirmButton = {
                                            Button(onClick = { showStartDatePicker = false }) {
                                                Text("OK")
                                            }
                                        },
                                        dismissButton = {
                                            Button(onClick = { showStartDatePicker = false }) {
                                                Text("Cancelar")
                                            }
                                        }
                                    ) {
                                        DatePicker(
                                            state = rememberDatePickerState(
                                                initialSelectedDateMillis = startDate
                                            )
                                        )
                                    }
                                }

                                if (showEndDatePicker) {
                                    DatePickerDialog(
                                        onDismissRequest = { showEndDatePicker = false },
                                        confirmButton = {
                                            Button(onClick = { showEndDatePicker = false }) {
                                                Text("OK")
                                            }
                                        },
                                        dismissButton = {
                                            Button(onClick = { showEndDatePicker = false }) {
                                                Text("Cancelar")
                                            }
                                        }
                                    ) {
                                        DatePicker(
                                            state = rememberDatePickerState(
                                                initialSelectedDateMillis = endDate
                                            )
                                        )
                                    }
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Button(
                                        onClick = { showStartDatePicker = true },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(50.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                            contentColor = MaterialTheme.colorScheme.onSurface
                                        ),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CalendarToday,
                                            contentDescription = "Fecha de inicio"
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = startDate?.let { dateFormat.format(it) }
                                            ?: "Inicio")
                                    }

                                    Button(
                                        onClick = { showEndDatePicker = true },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(50.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                            contentColor = MaterialTheme.colorScheme.onSurface
                                        ),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CalendarToday,
                                            contentDescription = "Fecha de finalización"
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = endDate?.let { dateFormat.format(it) }
                                            ?: "Final")
                                    }
                                }

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
                                                val result = snackbarHostState.showSnackbar(
                                                    message = "Anime agregado a tu lista",
                                                    actionLabel = "Ver lista",
                                                    duration = SnackbarDuration.Short
                                                )
                                                if (result == SnackbarResult.ActionPerformed) {
                                                    navController.navigate(AppDestinations.MY_ANIMES_ROUTE)
                                                }

                                            }
                                        }
                                    },
                                    enabled = isFormValid,
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .height(50.dp)
                                        .fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(text = "Guardar")
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
}

@Composable
fun ThemeItem(title: String, icon: ImageVector, themes: List<String>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = "$title Icon",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = RobotoBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                thickness = 1.dp
            )
            if (themes.isEmpty()) {
                Text(
                    text = "No encontrado",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = RobotoRegular,
                    modifier = Modifier.padding(start = 12.dp)
                )
            } else {
                themes.forEach { theme ->
                    Text(
                        text = theme,
                        color = MaterialTheme.colorScheme.tertiary,
                        style = MaterialTheme.typography.bodyLarge,
                        fontFamily = RobotoRegular,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp, horizontal = 12.dp)
                            .clickable {  }
                    )
                }
            }
        }
    }
}

@Composable
fun OtherTitleItem(label: String, value: String?) {
    if (!value.isNullOrBlank()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(0.4f)
                )
                Text(
                    text = value,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(0.6f)
                )
            }
        }
    }
}

@Composable
fun SectionHeaderWithSearch(
    title: String,
    isSearching: Boolean,
    onSearchClick: () -> Unit
) {
    if (!isSearching) {
        Box(modifier = Modifier.fillMaxWidth().padding(top = 16.dp, end = 32.dp)) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar $title",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(24.dp)
                    .clickable(onClick = onSearchClick)
            )
        }
    }
}

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Float,
    onRatingChange: (Float) -> Unit,
    stars: Int = 10,
    starColor: Color = Color(0xFFFFD700)
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            for (i in 1..stars) {
                val starValue = i.toFloat()
                val isFilled = starValue <= rating
                val isHalfFilled = (starValue - 0.5f) <= rating && !isFilled

                val imageVector = when {
                    isFilled -> Icons.Default.Star
                    isHalfFilled -> Icons.AutoMirrored.Filled.StarHalf
                    else -> Icons.Default.StarBorder
                }

                val tint =
                    if (isFilled || isHalfFilled) starColor else MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.5f
                    )
                val size by animateFloatAsState(
                    targetValue = if (isFilled || isHalfFilled) 1.2f else 1.0f,
                    label = "starSizeAnimation"
                )

                Icon(
                    imageVector = imageVector,
                    contentDescription = "Puntuación de $starValue estrellas",
                    modifier = Modifier
                        .scale(size)
                        .size(32.dp)
                        .clickable {
                            val newRating = if (rating == starValue) {
                                starValue - 0.5f
                            } else if (rating == starValue - 0.5f) {
                                0f
                            } else {
                                starValue
                            }
                            onRatingChange(newRating)
                        },
                    tint = tint
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = if (rating % 1 == 0f) {
                "Tu calificación: %d / %d".format(rating.toInt(), stars)
            } else {
                "Tu calificación: %.1f / %d".format(rating, stars)
            },
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = RobotoBold,
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.surfaceContainerHigh,
                    RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}


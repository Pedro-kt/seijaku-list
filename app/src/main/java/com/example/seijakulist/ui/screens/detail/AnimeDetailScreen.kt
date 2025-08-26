package com.example.seijakulist.ui.screens.detail

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.StarHalf
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
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
import com.example.seijakulist.ui.components.DescriptionAnime
import com.example.seijakulist.ui.components.LoadingScreen
import com.example.seijakulist.ui.components.SubTitleIcon
import com.example.seijakulist.ui.components.TitleScreen
import com.example.seijakulist.ui.components.TitleWithPadding
import com.example.seijakulist.ui.navigation.AppDestinations
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

    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    var isSearchingStaff by remember { mutableStateOf(false) }

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
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorMessage ?: "Error desconocido",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        else -> {
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
                                        color = if (isSelected) MaterialTheme.colorScheme.inversePrimary else Color.Transparent
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

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TitleWithPadding("Generos:")

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
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        },
                                        modifier = Modifier.padding(end = 8.dp),
                                        colors = FilterChipDefaults.elevatedFilterChipColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                        ),
                                        elevation = FilterChipDefaults.filterChipElevation(
                                            elevation = 16.dp
                                        )
                                    )
                                }
                            }
                        }


                    }

                    item {
                        TitleWithPadding("Synopsis")

                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            Text(
                                text = animeDetail?.synopsis ?: "Sinopsis no encontrada",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                                color = MaterialTheme.colorScheme.inversePrimary,
                            )
                        }

                    }

                    item {

                        TitleWithPadding("Otros titulos")

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
                                    .size(8.dp),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Título en Ingles:",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 16.sp,
                                    fontFamily = RobotoBold
                                )
                                Text(
                                    text = animeDetail?.titleEnglish ?: "",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                                    .size(8.dp),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Título en Japonés:",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 16.sp,
                                    fontFamily = RobotoBold
                                )
                                Text(
                                    text = animeDetail?.titleJapanese ?: "",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 16.sp,
                                    fontFamily = RobotoRegular,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                    }

                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TitleWithPadding("Studio:")

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
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        },
                                        modifier = Modifier.padding(end = 8.dp),
                                        colors = FilterChipDefaults.elevatedFilterChipColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                        ),
                                        elevation = FilterChipDefaults.filterChipElevation(
                                            elevation = 16.dp
                                        )
                                    )
                                }
                            }
                        }

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

                    }

                    item {
                        TitleWithPadding("Temas")

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
                                    .size(20.dp),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Opening:",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 16.sp,
                                    fontFamily = RobotoBold
                                )
                                if (animeThemes.endings.isEmpty()) {
                                    Text(
                                        text = "No encontrado",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 16.sp,
                                        fontFamily = RobotoRegular,
                                        textAlign = TextAlign.Start,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                } else {
                                    animeThemes.openings.forEach { opening ->
                                        Text(
                                            text = opening,
                                            color = Color(0xFF00BCD4),
                                            fontSize = 16.sp,
                                            fontFamily = RobotoRegular,
                                            textAlign = TextAlign.Start,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 6.dp),
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
                                    .size(20.dp),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.padding(bottom = 4.dp)) {
                                Text(
                                    text = "Ending:",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 16.sp,
                                    fontFamily = RobotoBold
                                )
                                if (animeThemes.endings.isEmpty()) {
                                    Text(
                                        text = "No encontrado",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 16.sp,
                                        fontFamily = RobotoRegular,
                                        textAlign = TextAlign.Start,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                } else {
                                    animeThemes.endings.forEach { ending ->
                                        Text(
                                            text = ending,
                                            color = Color(0xFF00BCD4),
                                            fontSize = 16.sp,
                                            fontFamily = RobotoRegular,
                                            textAlign = TextAlign.Start,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 6.dp),
                                            textDecoration = TextDecoration.Underline
                                        )
                                    }
                                }
                            }
                        }

                    }
                }

                if (selectedTabIndex == 1) {

                    item {

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
                                    trailingIcon = {
                                        IconButton(onClick = {
                                            isSearching = false
                                            searchQuery = ""
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
                                            text = "Buscar personaje...",
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
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TitleWithPadding("Personajes")

                                Spacer(modifier = Modifier.weight(1f))

                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Buscar personaje",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier
                                        .padding(end = 32.dp)
                                        .size(24.dp)
                                        .clickable {
                                            isSearching = true
                                        })
                            }
                        }
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
                                        color = MaterialTheme.colorScheme.inversePrimary, trackColor = MaterialTheme.colorScheme.outline
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

                    item {
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
                                    trailingIcon = {
                                        IconButton(onClick = {
                                            isSearchingStaff = false
                                            searchQuery = ""
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
                                            text = "Buscar staff...",
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
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TitleWithPadding("Staff")

                                Spacer(modifier = Modifier.weight(1f))

                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Buscar staff",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier
                                        .padding(end = 32.dp)
                                        .size(24.dp)
                                        .clickable {
                                            isSearchingStaff = true
                                        })
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

                            SubTitleIcon("Estado del anime:", Icons.Default.AddTask)

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(), contentAlignment = Alignment.Center
                            ) {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    contentPadding = PaddingValues(horizontal = 16.dp)
                                ) {
                                    items(statusAnime) { status ->
                                        var status = status

                                        val isSelected = selectedStatus == status

                                        val chipColor =
                                            statusColors[status] ?: Color.LightGray

                                        ElevatedFilterChip(
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
                                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                                selectedContainerColor = chipColor,
                                                labelColor = chipColor,
                                                selectedLabelColor = Color.Black
                                            ),
                                            border = BorderStroke(
                                                width = 1.dp,
                                                color = chipColor
                                            ),
                                            trailingIcon = {
                                                if (isSelected) {
                                                    Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = "Seleccionado",
                                                        tint = Color.Black
                                                    )
                                                }
                                            },
                                            elevation = FilterChipDefaults.elevatedFilterChipElevation(
                                                elevation = 16.dp
                                            )
                                        )
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
                                        color = MaterialTheme.colorScheme.onSurface,
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
                                placeholder = { Text("Ej: 'Una gran historia con personajes inolvidables' o tal vez, 'me encanta tomar el te despues de clases...'") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                                    .height(200.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.inversePrimary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                                    focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    cursorColor = MaterialTheme.colorScheme.inversePrimary,
                                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
                                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface
                                )
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
                                        .fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text(text = "Guardar")
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

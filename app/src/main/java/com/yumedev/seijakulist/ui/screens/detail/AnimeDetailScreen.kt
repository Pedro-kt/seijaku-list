package com.yumedev.seijakulist.ui.screens.detail

import android.content.Context
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.padding
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.*
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DatePickerDialog
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
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.animation.animateContentSize
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.yumedev.seijakulist.ui.components.DescriptionAnime
import com.yumedev.seijakulist.ui.components.LoadingScreen
import com.yumedev.seijakulist.ui.components.TitleWithPadding
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.theme.RobotoBold
import com.yumedev.seijakulist.ui.theme.RobotoRegular
import java.text.SimpleDateFormat
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeDetailScreen(
    navController: NavController,
    animeId: Int?,
    animeDetailViewModel: AnimeDetailViewModel = hiltViewModel(),
    animeCharacterDetailViewModel: AnimeCharacterDetailViewModel = hiltViewModel(),
    animeThemesViewModel: AnimeThemesViewModel = hiltViewModel(),
    producerDetailViewModel: ProducerDetailViewModel = hiltViewModel()
) {

    val animeDetail by animeDetailViewModel.animeDetail.collectAsState()
    val isLoading by animeDetailViewModel.isLoading.collectAsState()
    val errorMessage by animeDetailViewModel.errorMessage.collectAsState()

    val animeCharactersDetail by animeCharacterDetailViewModel.characters.collectAsState()
    val characterIsLoading by animeCharacterDetailViewModel.isLoading.collectAsState()
    val characterErrorMessage by animeCharacterDetailViewModel.errorMessage.collectAsState()

    val producerDetail by producerDetailViewModel.producer.collectAsState()
    val producerIsLoading by producerDetailViewModel.isLoading.collectAsState()
    val producerErrorMessage by producerDetailViewModel.errorMessage.collectAsState()

    val animeThemes by animeThemesViewModel.themes.collectAsState()

    val isAdded by animeDetailViewModel.isAdded.collectAsState()

    //snakbar de notificacion
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var showProducerLoading by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = animeId) {
        if (animeId != null) {
            animeThemesViewModel.animeThemes(animeId)
        }
    }

    LaunchedEffect(key1 = animeId, key2 = animeDetail?.studios) {
        if (animeId != null && animeDetail?.studios?.isNotEmpty() == true) {
            showProducerLoading = true
            delay(2000L) // delay to show loading, as requested
            showProducerLoading = false
            producerDetailViewModel.getProducerDetail(animeDetail?.studios[0]?.idStudio ?: 56)
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

    var userOpinion by remember { mutableStateOf("") }
    val focusManager: FocusManager = LocalFocusManager.current

    var isSearching by rememberSaveable { mutableStateOf(false) }
    var searchQuery by rememberSaveable { mutableStateOf("") }

    var expandedStatus by remember { mutableStateOf(false) }

    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

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
                        fontFamily = RobotoBold,
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
                            fontFamily = RobotoBold,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        item {

                        }

                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(350.dp)
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(animeDetail?.images).size(Size.ORIGINAL)
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
                                            modifier = Modifier
                                                .width(160.dp)
                                                .height(240.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .clickable(
                                                    onClick = {
                                                        selectedImageUrl =
                                                            animeDetail?.images
                                                        showDialog = true
                                                    },
                                                ),
                                            model = ImageRequest.Builder(LocalContext.current)
                                                .data(animeDetail?.images).size(Size.ORIGINAL)
                                                .crossfade(true).build(),
                                            contentDescription = "Imagen de portada",
                                            contentScale = ContentScale.Crop,
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

                                            Spacer(modifier = Modifier.height(16.dp))

                                            Card(
                                                shape = RoundedCornerShape(12.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = MaterialTheme.colorScheme.surfaceContainer.copy(
                                                        alpha = 0.8f
                                                    )
                                                ),
                                                elevation = CardDefaults.cardElevation(
                                                    defaultElevation = 0.dp
                                                ),
                                                modifier = Modifier
                                                    .padding(end = 16.dp)
                                            ) {
                                                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                                                    InfoRow(
                                                        icon = Icons.Default.Star,
                                                        text = animeDetail?.score.toString(),
                                                        iconTint =
                                                            MaterialTheme.colorScheme.secondary,
                                                        isBold = true
                                                    )
                                                    HorizontalDivider(
                                                        modifier = Modifier.padding(
                                                            vertical = 2.dp
                                                        )
                                                    )
                                                    InfoRow(
                                                        icon = Icons.Default.Tv,
                                                        text = animeDetail?.typeAnime ?: "",
                                                        iconTint = MaterialTheme.colorScheme.secondary
                                                    )
                                                    HorizontalDivider(
                                                        modifier = Modifier.padding(
                                                            vertical = 2.dp
                                                        )
                                                    )
                                                    Row(
                                                        modifier = Modifier.padding(
                                                            horizontal = 12.dp,
                                                            vertical = 4.dp
                                                        ),
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(
                                                            10.dp
                                                        )
                                                    ) {
                                                        val status = animeDetail?.status ?: ""
                                                        val (statusColor, onStatusColor) = when (status) {
                                                            "Currently Airing" -> Color(0xFF4CAF50).copy(
                                                                alpha = 0.2f
                                                            ) to Color(0xFF4CAF50) // Verde
                                                            "Finished Airing" -> Color(0xFF2196F3).copy(
                                                                alpha = 0.2f
                                                            ) to Color(0xFF2196F3) // Azul
                                                            "Not yet aired" -> Color(0xFFFF9800).copy(
                                                                alpha = 0.2f
                                                            ) to Color(0xFFFF9800) // Naranja
                                                            else -> Color.Transparent to MaterialTheme.colorScheme.tertiary
                                                        }
                                                        Box(
                                                            modifier = Modifier
                                                                .clip(RoundedCornerShape(50))
                                                                .background(statusColor)
                                                                .padding(
                                                                    horizontal = 8.dp,
                                                                    vertical = 4.dp
                                                                ),
                                                            contentAlignment = Alignment.Center
                                                        ) {
                                                            Row(
                                                                verticalAlignment = Alignment.CenterVertically,
                                                                horizontalArrangement = Arrangement.spacedBy(
                                                                    4.dp
                                                                )
                                                            ) {
                                                                Icon(
                                                                    Icons.Default.Alarm,
                                                                    "Status",
                                                                    tint = onStatusColor,
                                                                    modifier = Modifier.size(14.dp)
                                                                )
                                                                Text(
                                                                    status,
                                                                    color = onStatusColor,
                                                                    fontSize = 12.sp,
                                                                    fontWeight = FontWeight.Bold
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

                        }

                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(24.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Row(modifier = Modifier.padding(4.dp)) {
                                        tabIcons.forEachIndexed { index, icon ->
                                            val isSelected = selectedTabIndex == index
                                            val backgroundColor by animateColorAsState(
                                                targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                                label = "Tab Background Color"
                                            )
                                            val iconColor by animateColorAsState(
                                                targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                                label = "Tab Icon Color"
                                            )

                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .clip(RoundedCornerShape(20.dp))
                                                    .background(backgroundColor)
                                                    .clickable { selectedTabIndex = index }
                                                    .padding(vertical = 10.dp, horizontal = 12.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = icon,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(24.dp),
                                                    tint = iconColor
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (selectedTabIndex == 0) {
                            item {
                                Spacer(modifier = Modifier.height(20.dp))

                                // Sección de Géneros mejorada
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(bottom = 12.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.FilterList,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Géneros",
                                            style = MaterialTheme.typography.titleLarge,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontFamily = RobotoBold
                                        )
                                    }

                                    LazyRow(
                                        contentPadding = PaddingValues(vertical = 8.dp),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        items(animeDetail?.genres.orEmpty()) { genre ->
                                            genre?.let {
                                                Card(
                                                    shape = RoundedCornerShape(20.dp),
                                                    colors = CardDefaults.cardColors(
                                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                                    ),
                                                    elevation = CardDefaults.cardElevation(
                                                        defaultElevation = 2.dp
                                                    )
                                                ) {
                                                    Text(
                                                        text = it.name ?: "No encontrado",
                                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                        fontWeight = FontWeight.SemiBold,
                                                        fontSize = 14.sp,
                                                        modifier = Modifier.padding(
                                                            horizontal = 16.dp,
                                                            vertical = 8.dp
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }


                            }
                            item {
                                Spacer(modifier = Modifier.height(20.dp))

                                // Sección de Synopsis mejorada
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                                    ),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Column(modifier = Modifier.padding(20.dp)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(bottom = 12.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Description,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(24.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Sinopsis",
                                                style = MaterialTheme.typography.titleLarge,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                fontFamily = RobotoBold
                                            )
                                        }

                                        var textLayoutResult by remember {
                                            mutableStateOf<TextLayoutResult?>(null)
                                        }

                                        Text(
                                            text = animeDetail?.synopsis ?: "Sinopsis no encontrada",
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 15.sp,
                                            fontFamily = RobotoRegular,
                                            textAlign = TextAlign.Justify,
                                            lineHeight = 22.sp,
                                            maxLines = if (expanded) Int.MAX_VALUE else 8,
                                            onTextLayout = { textLayoutResult = it }
                                        )

                                        if ((textLayoutResult?.hasVisualOverflow ?: false) || expanded) {
                                            TextButton(
                                                onClick = { expanded = !expanded },
                                                modifier = Modifier.align(Alignment.End)
                                            ) {
                                                Text(
                                                    text = if (expanded) "Ver menos" else "Ver más",
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Icon(
                                                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                                    contentDescription = if (expanded) "Contraer sinopsis" else "Expandir sinopsis",
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))
                            }
                            item {
                                // Sección de Otros títulos mejorada
                                if (!animeDetail?.titleEnglish.isNullOrBlank() || !animeDetail?.titleJapanese.isNullOrBlank()) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(bottom = 12.dp)
                                        ) {
                                            Icon(
                                                Icons.AutoMirrored.Filled.List,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(24.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Otros títulos",
                                                style = MaterialTheme.typography.titleLarge,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                fontFamily = RobotoBold
                                            )
                                        }

                                        OtherTitleItem(
                                            label = "Título en Inglés:",
                                            value = animeDetail?.titleEnglish
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        OtherTitleItem(
                                            label = "Título en Japonés:",
                                            value = animeDetail?.titleJapanese
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(20.dp))
                                }
                            }
                            item {
                                // Sección de Estudio mejorada
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(bottom = 12.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Business,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Estudio",
                                            style = MaterialTheme.typography.titleLarge,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontFamily = RobotoBold
                                        )
                                    }

                                    LazyRow(
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    ) {
                                        items(animeDetail?.studios.orEmpty()) { studio ->
                                            Card(
                                                onClick = {
                                                    selectedTabIndex = 3
                                                },
                                                shape = RoundedCornerShape(12.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                                                ),
                                                elevation = CardDefaults.cardElevation(
                                                    defaultElevation = 2.dp
                                                )
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(
                                                        horizontal = 16.dp,
                                                        vertical = 10.dp
                                                    ),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        Icons.Default.Business,
                                                        contentDescription = "Studio Icon",
                                                        modifier = Modifier.size(18.dp),
                                                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(
                                                        studio?.nameStudio ?: "No encontrado",
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Icon(
                                                        Icons.AutoMirrored.Filled.ArrowRight,
                                                        contentDescription = null,
                                                        modifier = Modifier.size(16.dp),
                                                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))
                            }
                            item {
                                // Sección de Información mejorada
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                                    ),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Column(modifier = Modifier.padding(20.dp)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(bottom = 16.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.BarChart,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(24.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Información",
                                                style = MaterialTheme.typography.titleLarge,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                fontFamily = RobotoBold
                                            )
                                        }

                                        InfoGridItem(
                                            icon = Icons.Default.Star,
                                            label = "Puntuación",
                                            value = "${animeDetail?.score}"
                                        )
                                        InfoGridItem(
                                            icon = Icons.Default.People,
                                            label = "Puntuado por",
                                            value = if (animeDetail?.scoreBy == 0) "No encontrado" else "${animeDetail?.scoreBy} personas"
                                        )
                                        InfoGridItem(
                                            icon = Icons.Default.Tv,
                                            label = "Tipo",
                                            value = "${animeDetail?.typeAnime}"
                                        )
                                        InfoGridItem(
                                            icon = Icons.Default.FormatListNumbered,
                                            label = "Episodios",
                                            value = "${animeDetail?.episodes}"
                                        )
                                        InfoGridItem(
                                            icon = Icons.Default.Timer,
                                            label = "Duración",
                                            value = "${animeDetail?.duration}"
                                        )
                                        InfoGridItem(
                                            icon = Icons.Default.WbSunny,
                                            label = "Temporada",
                                            value = "${animeDetail?.season}"
                                        )
                                        InfoGridItem(
                                            icon = Icons.Default.AvTimer,
                                            label = "Año",
                                            value = "${animeDetail?.year}"
                                        )
                                        InfoGridItem(
                                            icon = Icons.Default.LiveTv,
                                            label = "Estado",
                                            value = "${animeDetail?.status}"
                                        )
                                        InfoGridItem(
                                            icon = Icons.Default.CalendarMonth,
                                            label = "Transmitido",
                                            value = "${animeDetail?.aired}"
                                        )
                                        InfoGridItem(
                                            icon = Icons.Default.BarChart,
                                            label = "Ranking",
                                            value = "#${animeDetail?.rank}"
                                        )
                                        InfoGridItem(
                                            icon = Icons.Default.Filter9Plus,
                                            label = "Rating",
                                            value = "${animeDetail?.rating}"
                                        )
                                        InfoGridItem(
                                            icon = Icons.AutoMirrored.Filled.LibraryBooks,
                                            label = "Origen",
                                            value = "${animeDetail?.source}",
                                            isLast = true
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))
                            }
                            item {
                                // Sección de Temas mejorada
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(bottom = 12.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.MusicNote,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Temas Musicales",
                                            style = MaterialTheme.typography.titleLarge,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontFamily = RobotoBold
                                        )
                                    }

                                    ThemeItem(
                                        title = "Openings",
                                        icon = Icons.Default.MusicNote,
                                        themes = animeThemes.openings,
                                        context,
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    ThemeItem(
                                        title = "Endings",
                                        icon = Icons.Default.MusicOff,
                                        themes = animeThemes.endings,
                                        context
                                    )
                                }
                                Spacer(modifier = Modifier.height(24.dp))
                            }
                        }

                        if (selectedTabIndex == 1) {
                            item {
                                Spacer(modifier = Modifier.height(20.dp))

                                // Header de Personajes mejorado
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Default.People,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Personajes",
                                            style = MaterialTheme.typography.titleLarge,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontFamily = RobotoBold
                                        )
                                    }
                                    IconButton(
                                        onClick = { isSearching = true },
                                        modifier = Modifier
                                            .background(
                                                MaterialTheme.colorScheme.primaryContainer,
                                                RoundedCornerShape(50)
                                            )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = "Buscar Personajes",
                                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }
                                }

                            }

                            item {
                                AnimatedContent(
                                    targetState = characterIsLoading,
                                    label = "CharacterContent"
                                ) { isLoading ->
                                    when {
                                        isLoading -> {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 16.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                LinearProgressIndicator(
                                                    modifier = Modifier.fillMaxWidth(0.8f)
                                                )
                                            }
                                        }

                                        characterErrorMessage != null -> {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(16.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "Oops! No pudimos cargar los personajes.\n$characterErrorMessage",
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    textAlign = TextAlign.Center
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
                                                    text = "Oops! No pudimos cargar los personajes.\n$characterErrorMessage",
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    textAlign = TextAlign.Center,
                                                    modifier = Modifier.padding(16.dp)
                                                )
                                            }
                                        }

                                        else -> {
                                            Column {
                                                // Barra de búsqueda animada
                                                AnimatedVisibility(visible = isSearching) {
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(
                                                                horizontal = 16.dp,
                                                                vertical = 8.dp
                                                            )
                                                    ) {
                                                        OutlinedTextField(
                                                            modifier = Modifier
                                                                .weight(1f),
                                                            value = searchQuery,
                                                            onValueChange = { searchQuery = it },
                                                            placeholder = {
                                                                Text(
                                                                    text = "Buscar personaje",
                                                                    color = MaterialTheme.colorScheme.onSurface.copy(
                                                                        alpha = 0.5f
                                                                    )
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
                                                            ),
                                                            trailingIcon = {
                                                                IconButton(
                                                                    onClick = {
                                                                        isSearching = false
                                                                        searchQuery = ""
                                                                        focusManager.clearFocus()
                                                                    },
                                                                ) {
                                                                    Icon(
                                                                        Icons.Default.Close,
                                                                        contentDescription = "Cerrar búsqueda",
                                                                        tint = MaterialTheme.colorScheme.onSurface
                                                                    )
                                                                }
                                                            }
                                                        )
                                                    }
                                                }


                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(
                                                            horizontal = 16.dp,
                                                            vertical = 16.dp
                                                        ),
                                                ) {
                                                    val filteredCharacters =
                                                        if (searchQuery.isBlank()) {
                                                            animeCharactersDetail
                                                        } else {
                                                            animeCharactersDetail.filter { character ->
                                                                character.nameCharacter?.contains(
                                                                    searchQuery, ignoreCase = true
                                                                ) == true
                                                            }
                                                        }

                                                    if (filteredCharacters.isEmpty()) {
                                                        Box(
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .padding(top = 50.dp),
                                                            contentAlignment = Alignment.Center
                                                        ) {
                                                            Text(
                                                                "No se encontraron personajes",
                                                                style = MaterialTheme.typography.bodyLarge
                                                            )
                                                        }
                                                    }

                                                    filteredCharacters.chunked(3)
                                                        .forEach { rowItems ->
                                                            Row(
                                                                horizontalArrangement = Arrangement.spacedBy(
                                                                    12.dp
                                                                ),
                                                                modifier = Modifier.padding(bottom = 12.dp)
                                                            ) {
                                                                rowItems.forEach { character ->
                                                                    Card(
                                                                        modifier = Modifier
                                                                            .weight(1f)
                                                                            .clickable {
                                                                                navController.navigate(
                                                                                    "${AppDestinations.CHARACTER_DETAIL_ROUTE}/${character.idCharacter}"
                                                                                )
                                                                            },
                                                                        shape = RoundedCornerShape(
                                                                            16.dp
                                                                        ),
                                                                        elevation = CardDefaults.cardElevation(
                                                                            defaultElevation = 3.dp,
                                                                            pressedElevation = 6.dp
                                                                        ),
                                                                        colors = CardDefaults.cardColors(
                                                                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                                                                        )
                                                                    ) {
                                                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                                            val imageUrl =
                                                                                character.imageCharacter?.jpg?.imageUrl.orEmpty()
                                                                            val translatedRole =
                                                                                when (character.role) {
                                                                                    "Main" -> "Principal"
                                                                                    "Supporting" -> "Secundario"
                                                                                    else -> character.role
                                                                                }

                                                                            Box(
                                                                                modifier = Modifier
                                                                                    .fillMaxWidth()
                                                                                    .height(160.dp)
                                                                            ) {
                                                                                AsyncImage(
                                                                                    model = ImageRequest.Builder(
                                                                                        LocalContext.current
                                                                                    )
                                                                                        .data(imageUrl)
                                                                                        .size(Size.ORIGINAL)
                                                                                        .crossfade(true)
                                                                                        .build(),
                                                                                    contentDescription = "Imagen de ${character.nameCharacter}",
                                                                                    contentScale = ContentScale.Crop,
                                                                                    modifier = Modifier.fillMaxSize()
                                                                                )

                                                                                // Badge de rol en la esquina superior
                                                                                Box(
                                                                                    modifier = Modifier
                                                                                        .align(Alignment.TopEnd)
                                                                                        .padding(8.dp)
                                                                                        .clip(RoundedCornerShape(8.dp))
                                                                                        .background(
                                                                                            when (character.role) {
                                                                                                "Main" -> MaterialTheme.colorScheme.primaryContainer
                                                                                                else -> MaterialTheme.colorScheme.secondaryContainer
                                                                                            }
                                                                                        )
                                                                                        .padding(
                                                                                            horizontal = 8.dp,
                                                                                            vertical = 4.dp
                                                                                        )
                                                                                ) {
                                                                                    Text(
                                                                                        text = translatedRole
                                                                                            ?: "Rol desconocido",
                                                                                        style = MaterialTheme.typography.labelSmall,
                                                                                        fontWeight = FontWeight.Bold,
                                                                                        color = when (character.role) {
                                                                                            "Main" -> MaterialTheme.colorScheme.onPrimaryContainer
                                                                                            else -> MaterialTheme.colorScheme.onSecondaryContainer
                                                                                        },
                                                                                        fontSize = 10.sp
                                                                                    )
                                                                                }
                                                                            }

                                                                            Text(
                                                                                text = character.nameCharacter
                                                                                    ?: "Desconocido",
                                                                                style = MaterialTheme.typography.titleSmall,
                                                                                fontWeight = FontWeight.Bold,
                                                                                maxLines = 2,
                                                                                overflow = TextOverflow.Ellipsis,
                                                                                textAlign = TextAlign.Center,
                                                                                modifier = Modifier.padding(
                                                                                    horizontal = 8.dp,
                                                                                    vertical = 12.dp
                                                                                ),
                                                                                color = MaterialTheme.colorScheme.onSurface,
                                                                                fontSize = 13.sp,
                                                                                lineHeight = 16.sp
                                                                            )
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                            if (rowItems.size < 3) {
                                                                for (i in 0 until (3 - rowItems.size)) {
                                                                    Spacer(
                                                                        modifier = Modifier.weight(
                                                                            1f
                                                                        )
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
                        }
                        if (selectedTabIndex == 2) {

                        }
                        if (selectedTabIndex == 3) {

                            item {
                                Spacer(modifier = Modifier.height(20.dp))

                                // Header de Estudio mejorado
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = 16.dp, bottom = 16.dp, end = 16.dp
                                        )
                                ) {
                                    Icon(
                                        Icons.Default.Business,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Estudio de Producción",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontFamily = RobotoBold
                                    )
                                }
                            }

                            item {
                                when {
                                    producerIsLoading || showProducerLoading -> {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(200.dp)
                                                .padding(horizontal = 16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            LoadingScreen()
                                        }
                                    }

                                    producerErrorMessage != null -> {
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp),
                                            shape = RoundedCornerShape(16.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.errorContainer
                                            )
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(20.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Icon(
                                                    Icons.Default.Close,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.onErrorContainer,
                                                    modifier = Modifier.size(48.dp)
                                                )
                                                Spacer(modifier = Modifier.height(12.dp))
                                                Text(
                                                    text = "Error al cargar información del estudio",
                                                    style = MaterialTheme.typography.titleMedium,
                                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(
                                                    text = producerErrorMessage!!,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f),
                                                    textAlign = TextAlign.Center
                                                )
                                            }
                                        }
                                    }

                                    producerDetail != null -> {
                                        showProducerLoading = false

                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp),
                                            shape = RoundedCornerShape(20.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                                            ),
                                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(24.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.spacedBy(16.dp)
                                            ) {
                                                // Logo del estudio
                                                Card(
                                                    shape = RoundedCornerShape(16.dp),
                                                    colors = CardDefaults.cardColors(
                                                        containerColor = MaterialTheme.colorScheme.surface
                                                    ),
                                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                                ) {
                                                    AsyncImage(
                                                        model = producerDetail?.images,
                                                        contentDescription = "Logo del estudio",
                                                        modifier = Modifier
                                                            .height(120.dp)
                                                            .padding(16.dp),
                                                        contentScale = ContentScale.Fit
                                                    )
                                                }

                                                // Nombre del estudio
                                                producerDetail?.titles?.firstOrNull()?.title?.let { title ->
                                                    Text(
                                                        text = title,
                                                        style = MaterialTheme.typography.headlineMedium,
                                                        fontWeight = FontWeight.Bold,
                                                        color = MaterialTheme.colorScheme.onSurface,
                                                        textAlign = TextAlign.Center
                                                    )
                                                }

                                                HorizontalDivider()

                                                // Fecha de fundación
                                                producerDetail?.established?.let { established ->
                                                    val date = established.split("T").firstOrNull()
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .background(
                                                                MaterialTheme.colorScheme.primaryContainer,
                                                                RoundedCornerShape(12.dp)
                                                            )
                                                            .padding(16.dp),
                                                        horizontalArrangement = Arrangement.Center,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Icon(
                                                            Icons.Default.CalendarMonth,
                                                            contentDescription = null,
                                                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                                            modifier = Modifier.size(20.dp)
                                                        )
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                        Text(
                                                            text = "Fundado: ",
                                                            style = MaterialTheme.typography.bodyLarge,
                                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                                        )
                                                        Text(
                                                            text = date ?: "N/A",
                                                            style = MaterialTheme.typography.bodyLarge,
                                                            fontWeight = FontWeight.Bold,
                                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                                        )
                                                    }
                                                }

                                                // Descripción del estudio
                                                producerDetail?.about?.let { about ->
                                                    var expandedProducerAbout by remember {
                                                        mutableStateOf(false)
                                                    }
                                                    Column {
                                                        Text(
                                                            text = "Acerca del estudio",
                                                            style = MaterialTheme.typography.titleMedium,
                                                            fontWeight = FontWeight.Bold,
                                                            color = MaterialTheme.colorScheme.primary,
                                                            modifier = Modifier.padding(bottom = 8.dp)
                                                        )
                                                        Text(
                                                            text = about,
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                            textAlign = TextAlign.Justify,
                                                            lineHeight = 20.sp,
                                                            overflow = TextOverflow.Ellipsis,
                                                            maxLines = if (expandedProducerAbout) Int.MAX_VALUE else 5
                                                        )
                                                        if (about.length > 200) {
                                                            TextButton(
                                                                onClick = {
                                                                    expandedProducerAbout = !expandedProducerAbout
                                                                },
                                                                modifier = Modifier.align(Alignment.End)
                                                            ) {
                                                                Text(
                                                                    if (expandedProducerAbout) "Ver menos" else "Ver más",
                                                                    fontWeight = FontWeight.SemiBold
                                                                )
                                                                Spacer(modifier = Modifier.width(4.dp))
                                                                Icon(
                                                                    imageVector = if (expandedProducerAbout) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                                                    contentDescription = null,
                                                                    modifier = Modifier.size(20.dp)
                                                                )
                                                            }
                                                        }
                                                    }
                                                }

                                                // Enlaces externos
                                                val externalLinks = producerDetail?.external
                                                if (!externalLinks.isNullOrEmpty()) {
                                                    HorizontalDivider()

                                                    Text(
                                                        text = "Enlaces Externos",
                                                        style = MaterialTheme.typography.titleMedium,
                                                        fontWeight = FontWeight.Bold,
                                                        color = MaterialTheme.colorScheme.primary,
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(bottom = 8.dp)
                                                    )

                                                    LazyRow(
                                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                                    ) {
                                                        items(externalLinks) { external ->
                                                            Card(
                                                                onClick = {
                                                                    external.url?.let {
                                                                        uriHandler.openUri(it)
                                                                    }
                                                                },
                                                                shape = RoundedCornerShape(16.dp),
                                                                colors = CardDefaults.cardColors(
                                                                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                                                                ),
                                                                elevation = CardDefaults.cardElevation(
                                                                    defaultElevation = 2.dp,
                                                                    pressedElevation = 6.dp
                                                                )
                                                            ) {
                                                                Column(
                                                                    modifier = Modifier
                                                                        .width(100.dp)
                                                                        .padding(16.dp),
                                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                                    verticalArrangement = Arrangement.Center
                                                                ) {
                                                                    AsyncImage(
                                                                        model = producerDetail?.images,
                                                                        contentDescription = "Imagen del estudio",
                                                                        modifier = Modifier
                                                                            .size(60.dp)
                                                                            .clip(RoundedCornerShape(50))
                                                                    )
                                                                    Spacer(modifier = Modifier.height(8.dp))
                                                                    Text(
                                                                        text = external.name ?: "Enlace",
                                                                        style = MaterialTheme.typography.labelMedium,
                                                                        textAlign = TextAlign.Center,
                                                                        maxLines = 2,
                                                                        overflow = TextOverflow.Ellipsis,
                                                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                                                        fontWeight = FontWeight.SemiBold
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
                                Spacer(modifier = Modifier.height(24.dp))
                            }
                        }
                        if (selectedTabIndex == 4) {
                            item {
                                var selectedStatus by remember { mutableStateOf<String?>(null) }
                                var userRating by remember { mutableStateOf(0.0f) }

                                val isFormValid = selectedStatus != null

                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    // Header mejorado
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 20.dp, vertical = 16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Favorite,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(28.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = "Añadir a mi lista",
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontSize = 26.sp,
                                            fontFamily = RobotoBold
                                        )
                                    }

                                    // Card: Estado
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp)
                                            .animateContentSize(),
                                        colors = CardDefaults.elevatedCardColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                                        ),
                                        elevation = CardDefaults.elevatedCardElevation(
                                            defaultElevation = 4.dp,
                                            pressedElevation = 2.dp
                                        ),
                                        shape = RoundedCornerShape(16.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(20.dp)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.padding(bottom = 12.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Text(
                                                    text = "Estado",
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                    fontSize = 20.sp,
                                                    fontFamily = RobotoBold
                                                )
                                                Spacer(modifier = Modifier.weight(1f))
                                                if (selectedStatus != null) {
                                                    Box(
                                                        modifier = Modifier
                                                            .background(
                                                                statusColors[selectedStatus]!!,
                                                                RoundedCornerShape(8.dp)
                                                            )
                                                            .padding(horizontal = 12.dp, vertical = 4.dp)
                                                    ) {
                                                        Text(
                                                            text = "✓",
                                                            color = Color.Black,
                                                            fontWeight = FontWeight.Bold,
                                                            fontSize = 14.sp
                                                        )
                                                    }
                                                }
                                            }

                                            Button(
                                                onClick = { expandedStatus = !expandedStatus },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(56.dp),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = if (selectedStatus != null) statusColors[selectedStatus]!! else MaterialTheme.colorScheme.surface,
                                                    contentColor = if (selectedStatus != null) Color.Black else MaterialTheme.colorScheme.onSurface
                                                ),
                                                shape = RoundedCornerShape(12.dp),
                                                elevation = ButtonDefaults.buttonElevation(
                                                    defaultElevation = 2.dp,
                                                    pressedElevation = 6.dp
                                                )
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Text(
                                                        text = selectedStatus ?: "Seleccionar estado",
                                                        modifier = Modifier.weight(1f),
                                                        textAlign = if (selectedStatus != null) TextAlign.Center else TextAlign.Start,
                                                        fontSize = 16.sp,
                                                        fontWeight = FontWeight.SemiBold
                                                    )
                                                    val rotation: Float by animateFloatAsState(
                                                        targetValue = if (expandedStatus) 180f else 0f,
                                                        animationSpec = spring(
                                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                                            stiffness = Spring.StiffnessLow
                                                        ),
                                                        label = "Arrow Rotation"
                                                    )
                                                    Icon(
                                                        imageVector = Icons.Default.ArrowDropDown,
                                                        contentDescription = null,
                                                        modifier = Modifier
                                                            .size(28.dp)
                                                            .rotate(rotation)
                                                    )
                                                }
                                            }

                                            AnimatedVisibility(
                                                visible = expandedStatus,
                                                enter = fadeIn() + expandVertically(),
                                                exit = fadeOut() + shrinkVertically()
                                            ) {
                                                LazyVerticalGrid(
                                                    columns = GridCells.Fixed(2),
                                                    modifier = Modifier
                                                        .padding(top = 12.dp)
                                                        .height(200.dp),
                                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                                ) {
                                                    items(
                                                        items = statusAnime,
                                                        key = { it }
                                                    ) { status ->
                                                        val isSelected = selectedStatus == status
                                                        val scale by animateFloatAsState(
                                                            targetValue = if (isSelected) 1.05f else 1f,
                                                            animationSpec = spring(
                                                                dampingRatio = Spring.DampingRatioMediumBouncy
                                                            ),
                                                            label = "Card Scale"
                                                        )

                                                        Card(
                                                            onClick = {
                                                                if (selectedStatus == status) {
                                                                    selectedStatus = null
                                                                } else {
                                                                    selectedStatus = status
                                                                }
                                                                expandedStatus = false
                                                            },
                                                            modifier = Modifier
                                                                .fillMaxHeight()
                                                                .scale(scale),
                                                            colors = CardDefaults.cardColors(
                                                                containerColor = statusColors[status] ?: Color.Gray
                                                            ),
                                                            elevation = CardDefaults.cardElevation(
                                                                defaultElevation = if (isSelected) 8.dp else 2.dp
                                                            ),
                                                            shape = RoundedCornerShape(12.dp)
                                                        ) {
                                                            Box(
                                                                modifier = Modifier
                                                                    .fillMaxSize()
                                                                    .padding(12.dp),
                                                                contentAlignment = Alignment.Center
                                                            ) {
                                                                Text(
                                                                    text = status,
                                                                    color = Color.Black,
                                                                    fontWeight = FontWeight.Bold,
                                                                    fontSize = 15.sp,
                                                                    textAlign = TextAlign.Center
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    // Card: Calificación
                                    AnimatedVisibility(
                                        visible = selectedStatus != null,
                                        enter = fadeIn() + expandVertically(),
                                        exit = fadeOut() + shrinkVertically()
                                    ) {
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp),
                                            colors = CardDefaults.elevatedCardColors(
                                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                                            ),
                                            elevation = CardDefaults.elevatedCardElevation(
                                                defaultElevation = 4.dp
                                            ),
                                            shape = RoundedCornerShape(16.dp)
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(20.dp)
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.padding(bottom = 12.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Star,
                                                        contentDescription = null,
                                                        tint = Color(0xFFFFD700),
                                                        modifier = Modifier.size(24.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(12.dp))
                                                    Text(
                                                        text = "Calificación",
                                                        color = MaterialTheme.colorScheme.onSurface,
                                                        fontSize = 20.sp,
                                                        fontFamily = RobotoBold
                                                    )
                                                }

                                                if (selectedStatus != "Planeado") {
                                                    RatingBar(
                                                        rating = userRating,
                                                        onRatingChange = { userRating = it }
                                                    )
                                                } else {
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .background(
                                                                MaterialTheme.colorScheme.surface,
                                                                RoundedCornerShape(12.dp)
                                                            )
                                                            .padding(16.dp),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Text(
                                                            "No puedes puntuar un anime planeado",
                                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                                            textAlign = TextAlign.Center,
                                                            fontSize = 14.sp
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    // Card: Opinión
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp),
                                        colors = CardDefaults.elevatedCardColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                                        ),
                                        elevation = CardDefaults.elevatedCardElevation(
                                            defaultElevation = 4.dp
                                        ),
                                        shape = RoundedCornerShape(16.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(20.dp)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.padding(bottom = 12.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Edit,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Text(
                                                    text = "Opinión",
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                    fontSize = 20.sp,
                                                    fontFamily = RobotoBold
                                                )
                                            }

                                            OutlinedTextField(
                                                value = userOpinion,
                                                onValueChange = { userOpinion = it },
                                                placeholder = {
                                                    Text(text = "Comparte tu opinión sobre este anime...")
                                                },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(180.dp),
                                                colors = OutlinedTextFieldDefaults.colors(
                                                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                                                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                                    focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                                                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                                    cursorColor = MaterialTheme.colorScheme.primary,
                                                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                                ),
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                        }
                                    }

                                    // Card: Fechas
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp),
                                        colors = CardDefaults.elevatedCardColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                                        ),
                                        elevation = CardDefaults.elevatedCardElevation(
                                            defaultElevation = 4.dp
                                        ),
                                        shape = RoundedCornerShape(16.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(20.dp)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.padding(bottom = 12.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.CalendarToday,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Text(
                                                    text = "Fechas",
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                    fontSize = 20.sp,
                                                    fontFamily = RobotoBold
                                                )
                                            }

                                            var startDate by remember { mutableStateOf<Long?>(null) }
                                            var endDate by remember { mutableStateOf<Long?>(null) }
                                            val dateFormat = SimpleDateFormat("dd/MM/yyyy")

                                            var showStartDatePicker by remember { mutableStateOf(false) }
                                            var showEndDatePicker by remember { mutableStateOf(false) }

                                            if (showStartDatePicker) {
                                                DatePickerDialog(
                                                    onDismissRequest = { showStartDatePicker = false },
                                                    confirmButton = {
                                                        Button(onClick = {
                                                            showStartDatePicker = false
                                                        }) {
                                                            Text("OK")
                                                        }
                                                    },
                                                    dismissButton = {
                                                        Button(onClick = { showStartDatePicker = false }) {
                                                            Text("Cancelar")
                                                        }
                                                    }
                                                ) {
                                                    val datePickerState = rememberDatePickerState(
                                                        initialSelectedDateMillis = startDate
                                                    )
                                                    DatePicker(state = datePickerState)
                                                    startDate = datePickerState.selectedDateMillis
                                                }
                                            }

                                            if (showEndDatePicker) {
                                                DatePickerDialog(
                                                    onDismissRequest = { showEndDatePicker = false },
                                                    confirmButton = {
                                                        Button(onClick = {
                                                            showEndDatePicker = false
                                                        }) {
                                                            Text("OK")
                                                        }
                                                    },
                                                    dismissButton = {
                                                        Button(onClick = { showEndDatePicker = false }) {
                                                            Text("Cancelar")
                                                        }
                                                    }
                                                ) {
                                                    val datePickerState = rememberDatePickerState(
                                                        initialSelectedDateMillis = endDate
                                                    )
                                                    DatePicker(state = datePickerState)
                                                    endDate = datePickerState.selectedDateMillis
                                                }
                                            }

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                                            ) {
                                                Card(
                                                    onClick = { showStartDatePicker = true },
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .height(70.dp),
                                                    colors = CardDefaults.cardColors(
                                                        containerColor = MaterialTheme.colorScheme.surface
                                                    ),
                                                    elevation = CardDefaults.cardElevation(
                                                        defaultElevation = 2.dp,
                                                        pressedElevation = 6.dp
                                                    ),
                                                    shape = RoundedCornerShape(12.dp)
                                                ) {
                                                    Column(
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .padding(12.dp),
                                                        verticalArrangement = Arrangement.Center,
                                                        horizontalAlignment = Alignment.CenterHorizontally
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.CalendarToday,
                                                            contentDescription = "Fecha de inicio",
                                                            tint = MaterialTheme.colorScheme.primary,
                                                            modifier = Modifier.size(20.dp)
                                                        )
                                                        Spacer(modifier = Modifier.height(4.dp))
                                                        Text(
                                                            text = startDate?.let { dateFormat.format(it) } ?: "Inicio",
                                                            fontSize = 12.sp,
                                                            fontWeight = FontWeight.Medium,
                                                            color = MaterialTheme.colorScheme.onSurface
                                                        )
                                                    }
                                                }

                                                Card(
                                                    onClick = { showEndDatePicker = true },
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .height(70.dp),
                                                    colors = CardDefaults.cardColors(
                                                        containerColor = MaterialTheme.colorScheme.surface
                                                    ),
                                                    elevation = CardDefaults.cardElevation(
                                                        defaultElevation = 2.dp,
                                                        pressedElevation = 6.dp
                                                    ),
                                                    shape = RoundedCornerShape(12.dp)
                                                ) {
                                                    Column(
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .padding(12.dp),
                                                        verticalArrangement = Arrangement.Center,
                                                        horizontalAlignment = Alignment.CenterHorizontally
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.CalendarToday,
                                                            contentDescription = "Fecha de finalización",
                                                            tint = MaterialTheme.colorScheme.primary,
                                                            modifier = Modifier.size(20.dp)
                                                        )
                                                        Spacer(modifier = Modifier.height(4.dp))
                                                        Text(
                                                            text = endDate?.let { dateFormat.format(it) } ?: "Final",
                                                            fontSize = 12.sp,
                                                            fontWeight = FontWeight.Medium,
                                                            color = MaterialTheme.colorScheme.onSurface
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    // Botón Guardar mejorado
                                    Button(
                                        onClick = {
                                            if (isFormValid) {
                                                val scoreToPass = if (selectedStatus == "Planeado") {
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
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 8.dp)
                                            .height(56.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = Color.White,
                                            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                        ),
                                        shape = RoundedCornerShape(14.dp),
                                        elevation = ButtonDefaults.buttonElevation(
                                            defaultElevation = 6.dp,
                                            pressedElevation = 12.dp,
                                            disabledElevation = 0.dp
                                        )
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                modifier = Modifier.size(22.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Guardar en mi lista",
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))
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
private fun InfoRow(
    icon: ImageVector,
    text: String,
    iconTint: Color,
    isBold: Boolean = false
) {
    Row(
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun ThemeItem(
    title: String,
    icon: ImageVector,
    themes: List<String>,
    context: Context,
    animeThemesViewModel: AnimeThemesViewModel = hiltViewModel()
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "$title Icon",
                    tint = when (title) {
                        "Openings" -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.secondary
                    },
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = RobotoBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.weight(1f))
                if (themes.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .background(
                                when (title) {
                                    "Openings" -> MaterialTheme.colorScheme.primaryContainer
                                    else -> MaterialTheme.colorScheme.secondaryContainer
                                },
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${themes.size}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = when (title) {
                                "Openings" -> MaterialTheme.colorScheme.onPrimaryContainer
                                else -> MaterialTheme.colorScheme.onSecondaryContainer
                            }
                        )
                    }
                }
            }

            if (themes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surface,
                            RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay $title disponibles",
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = RobotoRegular
                    )
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    themes.forEachIndexed { index, theme ->
                        Card(
                            onClick = {
                                animeThemesViewModel.openYoutubeSearch(context, theme)
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 1.dp,
                                pressedElevation = 4.dp
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(
                                            when (title) {
                                                "Openings" -> MaterialTheme.colorScheme.primaryContainer
                                                else -> MaterialTheme.colorScheme.secondaryContainer
                                            },
                                            RoundedCornerShape(8.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${index + 1}",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = when (title) {
                                            "Openings" -> MaterialTheme.colorScheme.onPrimaryContainer
                                            else -> MaterialTheme.colorScheme.onSecondaryContainer
                                        }
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = theme,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontFamily = RobotoRegular,
                                    modifier = Modifier.weight(1f),
                                    fontWeight = FontWeight.Medium
                                )
                                Icon(
                                    imageVector = Icons.Default.MusicNote,
                                    contentDescription = "Reproducir",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
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
fun OtherTitleItem(label: String, value: String?) {
    if (!value.isNullOrBlank()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = label,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = value,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun InfoGridItem(
    icon: ImageVector,
    label: String,
    value: String,
    isLast: Boolean = false
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = label,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
            Text(
                text = value,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            )
        }
        if (!isLast) {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
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

@Composable
private fun InfoColumn(title: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

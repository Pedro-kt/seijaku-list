package com.yumedev.seijakulist.ui.screens.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.padding
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.painterResource
import com.yumedev.seijakulist.R
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
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Forum
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
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DatePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.material3.DatePicker
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.draw.alpha
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
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.yumedev.seijakulist.domain.models.Anime
import com.yumedev.seijakulist.domain.models.AnimeEpisode
import com.yumedev.seijakulist.domain.models.AnimeEpisodeDetail
import com.yumedev.seijakulist.ui.components.CardAnimesHome
import com.yumedev.seijakulist.ui.components.DescriptionAnime
import com.yumedev.seijakulist.ui.components.LoadingScreen
import com.yumedev.seijakulist.ui.components.TitleWithPadding
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsMedium
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import java.text.SimpleDateFormat
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import com.yumedev.seijakulist.ui.components.CustomDialog
import com.yumedev.seijakulist.ui.components.DialogType
import com.yumedev.seijakulist.ui.components.confirm_dialog.ConfirmChangePlannedDialog
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeDetailScreen(
    navController: NavController,
    animeId: Int?,
    initialTab: Int = 0,
    openSheet: Boolean = false,
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

    val animePictures by animeDetailViewModel.animePictures.collectAsState()
    val isPicturesLoading by animeDetailViewModel.isPicturesLoading.collectAsState()

    val animeVideos by animeDetailViewModel.animeVideos.collectAsState()
    val isVideosLoading by animeDetailViewModel.isVideosLoading.collectAsState()

    val recommendations by animeDetailViewModel.recommendations.collectAsState()
    val isRecommendationsLoading by animeDetailViewModel.isRecommendationsLoading.collectAsState()
    val forumTopics by animeDetailViewModel.forumTopics.collectAsState()
    val isForumLoading by animeDetailViewModel.isForumLoading.collectAsState()

    val animeEpisodes by animeDetailViewModel.animeEpisodes.collectAsState()
    val isEpisodesLoading by animeDetailViewModel.isEpisodesLoading.collectAsState()
    val hasMoreEpisodes by animeDetailViewModel.hasMoreEpisodes.collectAsState()

    val selectedEpisodeDetail by animeDetailViewModel.selectedEpisodeDetail.collectAsState()
    val isEpisodeDetailLoading by animeDetailViewModel.isEpisodeDetailLoading.collectAsState()

    // Estado para el modal de imagen
    var selectedPictureUrl by remember { mutableStateOf<String?>(null) }
    var pendingExternalUrl by remember { mutableStateOf("") }
    var showExternalLinkDialog by remember { mutableStateOf(false) }
    var showAllPictures by remember { mutableStateOf(false) }

    val isAdded by animeDetailViewModel.isAdded.collectAsState()
    val existingAnime by animeDetailViewModel.existingAnime.collectAsState()

    //snakbar de notificacion
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var showProducerLoading by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = animeId) {
        if (animeId != null) {
            animeThemesViewModel.animeThemes(animeId)
        }
    }

    var showDialog by remember { mutableStateOf(false) }
    var selectedImageUrl by remember { mutableStateOf<String?>(null) }

    val score = remember { mutableFloatStateOf(1f) }

    val lazyListState = rememberLazyListState()
    val showFab by remember { derivedStateOf { lazyListState.firstVisibleItemIndex >= 2 } }
    val isScrolledPastHeader by remember { derivedStateOf { lazyListState.firstVisibleItemIndex >= 1 } }
    val headerBgAlpha by animateFloatAsState(
        targetValue = if (isScrolledPastHeader) 1f else 0f,
        animationSpec = tween(250),
        label = "header_bg_alpha"
    )

    val headerVisible = animeDetail != null
    val headerAlpha by animateFloatAsState(
        targetValue = if (headerVisible) 1f else 0f,
        animationSpec = tween(500),
        label = "header_alpha"
    )
    val headerOffsetY by animateDpAsState(
        targetValue = if (headerVisible) 0.dp else 32.dp,
        animationSpec = tween(500, easing = androidx.compose.animation.core.EaseOutCubic),
        label = "header_offset"
    )

    var showAddToListSheet by remember { mutableStateOf(false) }
    val addToListSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(openSheet, isLoading) {
        if (openSheet && !isLoading && animeDetail != null) {
            showAddToListSheet = true
        }
    }
    var selectedStatus by remember { mutableStateOf<String?>(null) }
    var userRating by remember { mutableStateOf(0.0f) }
    var startDate by remember { mutableStateOf<Long?>(null) }
    var endDate by remember { mutableStateOf<Long?>(null) }
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()) }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var plannedPriority by remember { mutableStateOf<String?>(null) }
    var plannedNote by remember { mutableStateOf("") }
    var showChangePlannedDialog by remember { mutableStateOf(false) }
    var pendingNewStatus by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(existingAnime) {
        existingAnime?.let { existing ->
            if (selectedStatus == null) {
                selectedStatus = existing.userStatus
                plannedPriority = existing.plannedPriority
                plannedNote = existing.plannedNote ?: ""
            }
        }
    }

    var expanded by rememberSaveable { mutableStateOf(false) }

    // Estado para manejar el estudio expandido
    var expandedStudioId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(key1 = expandedStudioId) {
        if (expandedStudioId != null) {
            showProducerLoading = true
            delay(500L) // delay to show loading
            showProducerLoading = false
            producerDetailViewModel.getProducerDetail(expandedStudioId!!)
        }
    }

    val tabIcons = listOf(
        Icons.Default.Description,
        Icons.Default.People,
        Icons.AutoMirrored.Filled.List,
        Icons.Default.Business
    )

    val statusAnime = listOf("Viendo", "Completado", "Pendiente", "Abandonado", "Planeado")

    val statusColors = mapOf(
        "Viendo" to Color(0xFF66BB6A),
        "Completado" to Color(0xFF42A5F5),
        "Pendiente" to Color(0xFFFFCA28),
        "Abandonado" to Color(0xFFEF5350),
        "Planeado" to Color(0xFF78909C)
    )

    val animeVideosRemember = remember(animeVideos) {
        animeVideos?.episodes?.associateBy(
            keySelector = { it.malId },
            valueTransform = { it }
        ) ?: emptyMap()
    }

    var selectedTabIndex by remember { mutableStateOf(initialTab) }

    // Cargar imágenes, videos y episodios cuando se selecciona el tab 2
    // Con delay de 1 segundo entre cada petición para respetar el rate limit de la API (3/seg)
    LaunchedEffect(selectedTabIndex) {
        if (selectedTabIndex == 2 && animeDetail != null) {
            animeDetailViewModel.loadAnimePictures(animeDetail!!.malId)
            delay(1000L)
            animeDetailViewModel.loadAnimeVideos(animeDetail!!.malId)
            delay(1000L)
            animeDetailViewModel.loadAnimeEpisodes(animeDetail!!.malId)
            delay(1000L)
            animeDetailViewModel.loadForumTopics(animeDetail!!.malId)
        }
    }

    LaunchedEffect(animeDetail) {
        if (animeDetail != null) {
            delay(500L)
            animeDetailViewModel.loadRecommendations(animeDetail!!.malId)
        }
    }

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
                            painter = painterResource(id = R.drawable.ic_arrow_left_line),
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "Detalle del anime",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp,
                        fontFamily = PoppinsBold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    StateMessage(
                        icon = Icons.Default.CloudOff,
                        title = "No se pudo cargar",
                        description = errorMessage!!,
                        buttonText = "Reintentar",
                        onButtonClick = {
                            if (animeId != null) animeDetailViewModel.loadAnimeDetail(animeId)
                        }
                    )
                }
            }
        }

        else -> {
            Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                floatingActionButton = {
                    AnimatedVisibility(
                        visible = showFab,
                        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                    ) {
                        ExtendedFloatingActionButton(
                            onClick = { showAddToListSheet = true },
                            icon = {
                                Icon(
                                    imageVector = if (isAdded) Icons.Default.Edit else Icons.Default.Favorite,
                                    contentDescription = null
                                )
                            },
                            text = {
                                Text(
                                    text = if (isAdded) "Editar en lista" else "Añadir a lista",
                                    fontFamily = PoppinsBold
                                )
                            },
                            containerColor = if (isAdded)
                                MaterialTheme.colorScheme.secondaryContainer
                            else
                                MaterialTheme.colorScheme.primary,
                            contentColor = if (isAdded)
                                MaterialTheme.colorScheme.onSecondaryContainer
                            else
                                MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                snackbarHost = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        SnackbarHost(hostState = snackbarHostState) { data ->
                            var visible by remember { mutableStateOf(false) }

                            LaunchedEffect(data) {
                                visible = true
                            }

                            val offsetY by animateDpAsState(
                                targetValue = if (visible) 0.dp else (-100).dp,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMedium
                                ),
                                label = "snackbar_slide"
                            )

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .offset(y = offsetY),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    // Ícono de éxito verde
                                    Surface(
                                        shape = CircleShape,
                                        color = Color(0xFF4CAF50),
                                        modifier = Modifier.size(40.dp)
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }

                                    // Mensaje
                                    Text(
                                        text = data.visuals.message,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.weight(1f)
                                    )

                                    // Botón de acción
                                    if (data.visuals.actionLabel != null) {
                                        Surface(
                                            onClick = { data.performAction() },
                                            shape = RoundedCornerShape(12.dp),
                                            color = MaterialTheme.colorScheme.surfaceContainerHighest
                                        ) {
                                            Text(
                                                text = data.visuals.actionLabel!!,
                                                color = MaterialTheme.colorScheme.primary,
                                                style = MaterialTheme.typography.labelLarge,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(key1 = Unit) {
                            detectTapGestures(onTap = { focusManager.clearFocus() })
                        }
                ) {
                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = paddingValues.calculateBottomPadding())
                    ) {

                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(340.dp)
                            ) {
                                // 1. FONDO DIFUMINADO (Mantenemos el efecto premium)
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(animeDetail?.images).size(Size.ORIGINAL)
                                        .crossfade(true).build(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .blur(radius = 28.dp)
                                        .scale(1.2f),
                                    contentScale = ContentScale.Crop,
                                )

                                // OVERLAY GRADIENTE
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            brush = Brush.verticalGradient(
                                                0f to Color.Transparent,
                                                0.6f to MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                                                1f to MaterialTheme.colorScheme.background
                                            )
                                        )
                                )

                                // 2. CONTENIDO HORIZONTAL (Alineado a la altura de la portada)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.TopCenter)
                                        .padding(horizontal = 16.dp)
                                        .padding(bottom = 24.dp, top = 70.dp), // Espacio arriba y abajo
                                    verticalAlignment = Alignment.Bottom // Todo se apoya en la base
                                ) {
                                    // PORTADA
                                    Card(
                                        modifier = Modifier
                                            .width(140.dp)
                                            .height(210.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                                    ) {
                                        AsyncImage(
                                            model = ImageRequest.Builder(LocalContext.current)
                                                .data(animeDetail?.images).size(Size.ORIGINAL)
                                                .crossfade(true).build(),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    // COLUMNA DE DATOS + BOTÓN
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(210.dp), // Misma altura que la portada
                                        verticalArrangement = Arrangement.SpaceBetween // Empuja los datos arriba y el botón abajo
                                    ) {
                                        // SECCIÓN SUPERIOR: Títulos y Status
                                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Text(
                                                text = animeDetail?.title ?: "",
                                                color = MaterialTheme.colorScheme.onSurface,
                                                fontSize = 18.sp,
                                                fontFamily = PoppinsBold,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis,
                                                lineHeight = 22.sp
                                            )

                                            if (!animeDetail?.titleJapanese.isNullOrBlank()) {
                                                Text(
                                                    text = animeDetail?.titleJapanese ?: "",
                                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                                    fontSize = 11.sp,
                                                    fontFamily = PoppinsRegular,
                                                    maxLines = 1
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(4.dp))

                                            // Status e Info rápida en una sola línea
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                StatusChip(animeDetail?.status ?: "")
                                                QuickInfoItem(Icons.Default.Star, animeDetail?.score?.toString() ?: "N/A", Color(0xFFFFD700))
                                            }
                                        }

                                        // SECCIÓN INFERIOR: Chips de detalle y Botón
                                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                            // Chips pequeños (Tipo y Año)
                                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                                DetailChipSmall(Icons.Default.Tv, animeDetail?.typeAnime ?: "TV")
                                                DetailChipSmall(Icons.Default.CalendarMonth, animeDetail?.year?.toString() ?: "-")
                                            }

                                            // BOTÓN COMPACTO
                                            Button(
                                                onClick = { showAddToListSheet = true },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(42.dp), // Altura reducida
                                                shape = RoundedCornerShape(10.dp),
                                                contentPadding = PaddingValues(horizontal = 12.dp),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = if (isAdded)
                                                        MaterialTheme.colorScheme.secondaryContainer
                                                    else
                                                        MaterialTheme.colorScheme.primary
                                                )
                                            ) {
                                                Icon(
                                                    imageVector = if (isAdded) Icons.Default.Edit else Icons.Default.Favorite,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = if (isAdded) "Editar lista" else "Añadir",
                                                    fontSize = 13.sp,
                                                    fontFamily = PoppinsBold
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
                                    .padding(start = 16.dp, end = 16.dp, bottom = 20.dp),
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
                            // Géneros
                            if (!animeDetail?.genres.isNullOrEmpty()) {
                                item {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 20.dp, end = 20.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceContainer
                                        ),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            verticalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            Text(
                                                text = "Géneros",
                                                fontSize = 16.sp,
                                                fontFamily = PoppinsBold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )

                                            FlowRow(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                verticalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                animeDetail?.genres?.filterNotNull()?.forEach { genre ->
                                                    Surface(
                                                        shape = RoundedCornerShape(8.dp),
                                                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                                        border = BorderStroke(
                                                            1.dp,
                                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                                        )
                                                    ) {
                                                        Text(
                                                            text = genre.name ?: "N/A",
                                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                            fontFamily = PoppinsRegular,
                                                            fontSize = 13.sp,
                                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(20.dp))
                                }
                            }

                            // Sinopsis
                            item {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                                    ),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Text(
                                            text = "Sinopsis",
                                            fontSize = 16.sp,
                                            fontFamily = PoppinsBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                        var textLayoutResult by remember {
                                            mutableStateOf<TextLayoutResult?>(null)
                                        }

                                        Text(
                                            text = animeDetail?.synopsis ?: "Sinopsis no encontrada",
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 14.sp,
                                            fontFamily = PoppinsRegular,
                                            textAlign = TextAlign.Justify,
                                            lineHeight = 20.sp,
                                            maxLines = if (expanded) Int.MAX_VALUE else 8,
                                            onTextLayout = { textLayoutResult = it }
                                        )

                                        if ((textLayoutResult?.hasVisualOverflow ?: false) || expanded) {
                                            Text(
                                                text = if (expanded) "Ver menos" else "Ver más",
                                                fontFamily = PoppinsBold,
                                                fontSize = 13.sp,
                                                color = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.clickable { expanded = !expanded }
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                            }

                            // Otros títulos
                            if (!animeDetail?.titleEnglish.isNullOrBlank() || !animeDetail?.titleJapanese.isNullOrBlank()) {
                                item {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 20.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceContainer
                                        ),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            verticalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            Text(
                                                text = "Otros títulos",
                                                fontSize = 16.sp,
                                                fontFamily = PoppinsBold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )

                                            if (!animeDetail?.titleEnglish.isNullOrBlank()) {
                                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                                    Text(
                                                        text = "Inglés",
                                                        fontSize = 12.sp,
                                                        fontFamily = PoppinsRegular,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                    Text(
                                                        text = animeDetail?.titleEnglish ?: "",
                                                        fontSize = 14.sp,
                                                        fontFamily = PoppinsBold,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                }
                                                if (!animeDetail?.titleJapanese.isNullOrBlank()) {
                                                    HorizontalDivider(
                                                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                                                    )
                                                }
                                            }

                                            if (!animeDetail?.titleJapanese.isNullOrBlank()) {
                                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                                    Text(
                                                        text = "Japonés",
                                                        fontSize = 12.sp,
                                                        fontFamily = PoppinsRegular,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                    Text(
                                                        text = animeDetail?.titleJapanese ?: "",
                                                        fontSize = 14.sp,
                                                        fontFamily = PoppinsBold,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(20.dp))
                                }
                            }

                            // Estudio
                            if (!animeDetail?.studios.isNullOrEmpty()) {
                                item {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 20.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceContainer
                                        ),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            verticalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            Text(
                                                text = "Estudio",
                                                fontSize = 16.sp,
                                                fontFamily = PoppinsBold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )

                                            FlowRow(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                verticalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                animeDetail?.studios?.filterNotNull()?.forEach { studio ->
                                                    Surface(
                                                        onClick = { selectedTabIndex = 3 },
                                                        shape = RoundedCornerShape(8.dp),
                                                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                                                        border = BorderStroke(
                                                            1.dp,
                                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                                        )
                                                    ) {
                                                        Text(
                                                            text = studio.nameStudio ?: "N/A",
                                                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                                                            fontFamily = PoppinsRegular,
                                                            fontSize = 13.sp,
                                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(20.dp))
                                }
                            }

                            // Información
                            item {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                                    ),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = "Información",
                                            fontSize = 16.sp,
                                            fontFamily = PoppinsBold,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.padding(bottom = 4.dp)
                                        )

                                        animeDetail?.score?.let {
                                            CompactInfoRow(label = "Puntuación", value = "$it")
                                        }
                                        animeDetail?.scoreBy?.let {
                                            if (it > 0) CompactInfoRow(label = "Puntuado por", value = "$it personas")
                                        }
                                        animeDetail?.typeAnime?.let {
                                            CompactInfoRow(label = "Tipo", value = it)
                                        }
                                        animeDetail?.episodes?.let {
                                            CompactInfoRow(label = "Episodios", value = "$it")
                                        }
                                        animeDetail?.duration?.let {
                                            CompactInfoRow(label = "Duración", value = it)
                                        }
                                        animeDetail?.season?.let {
                                            CompactInfoRow(label = "Temporada", value = it)
                                        }
                                        animeDetail?.year?.let {
                                            CompactInfoRow(label = "Año", value = "$it")
                                        }
                                        animeDetail?.status?.let {
                                            CompactInfoRow(label = "Estado", value = it)
                                        }
                                        animeDetail?.aired?.let {
                                            CompactInfoRow(label = "Transmitido", value = it)
                                        }
                                        animeDetail?.rank?.let {
                                            CompactInfoRow(label = "Ranking", value = "#$it")
                                        }
                                        animeDetail?.rating?.let {
                                            CompactInfoRow(label = "Rating", value = it)
                                        }
                                        animeDetail?.source?.let {
                                            CompactInfoRow(label = "Origen", value = it)
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                            }

                            // Temas Musicales
                            item {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                                    ),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Text(
                                            text = "Temas Musicales",
                                            fontSize = 16.sp,
                                            fontFamily = PoppinsBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                        ThemeItem(
                                            title = "Openings",
                                            icon = Icons.Default.MusicNote,
                                            themes = animeThemes.openings,
                                            context,
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        ThemeItem(
                                            title = "Endings",
                                            icon = Icons.Default.MusicOff,
                                            themes = animeThemes.endings,
                                            context
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(24.dp))
                            }

                            // Sección "Si te gustó este anime..."
                            item {
                                if (isRecommendationsLoading) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(220.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(modifier = Modifier.size(32.dp))
                                    }
                                } else if (recommendations.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    SectionHeader(
                                        title = "Te puede gustar",
                                        subtitle = "${recommendations.size} recomendaciones"
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                        ) {
                                            Text(
                                                text = "${recommendations.size}",
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                                color = MaterialTheme.colorScheme.primary,
                                                fontFamily = PoppinsBold,
                                                fontSize = 13.sp
                                            )
                                        }
                                    }
                                    CardAnimesHome(
                                        animeList = recommendations.map { rec ->
                                            Anime(rec.malId, rec.title, rec.image, score = 0f)
                                        },
                                        navController = navController
                                    )
                                } else {
                                    return@item
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }

                        if (selectedTabIndex == 1) {
                            item {
                                AnimatedContent(
                                    targetState = isSearching,
                                    transitionSpec = {
                                        (fadeIn(animationSpec = tween(250)) + expandVertically()) togetherWith
                                                (fadeOut(animationSpec = tween(200)) + shrinkVertically())
                                    },
                                    label = "CharacterHeader"
                                ) { searching ->
                                    if (searching) {
                                        // MODO BÚSQUEDA: Ultra-limpio
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp)
                                        ) {
                                            OutlinedTextField(
                                                modifier = Modifier.fillMaxWidth(),
                                                value = searchQuery,
                                                onValueChange = { searchQuery = it },
                                                placeholder = {
                                                    Text(
                                                        "Buscar por nombre...",
                                                        style = TextStyle(
                                                            fontFamily = PoppinsRegular,
                                                            fontSize = 15.sp,
                                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                                        )
                                                    )
                                                },
                                                leadingIcon = {
                                                    Icon(
                                                        Icons.Rounded.Search,
                                                        null,
                                                        tint = MaterialTheme.colorScheme.primary,
                                                        modifier = Modifier.size(22.dp)
                                                    )
                                                },
                                                trailingIcon = {
                                                    IconButton(onClick = {
                                                        if (searchQuery.isEmpty()) isSearching = false
                                                        else searchQuery = ""
                                                    }) {
                                                        Icon(
                                                            Icons.Rounded.Close,
                                                            null,
                                                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                                            modifier = Modifier.size(20.dp)
                                                        )
                                                    }
                                                },
                                                singleLine = true,
                                                shape = RoundedCornerShape(24.dp), // Un redondeado elegante, no exagerado
                                                colors = OutlinedTextFieldDefaults.colors(
                                                    focusedContainerColor = Color.Transparent, // Sin fondo para máxima limpieza
                                                    unfocusedContainerColor = Color.Transparent,
                                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                                                ),
                                                textStyle = TextStyle(
                                                    fontFamily = PoppinsMedium,
                                                    fontSize = 15.sp,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            )
                                        }
                                    } else {
                                        SectionHeader(
                                            title = "Personajes",
                                            subtitle = "Reparto de la obra"
                                        ) {
                                            IconButton(
                                                onClick = { isSearching = true },
                                                modifier = Modifier.size(44.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Rounded.Search,
                                                    contentDescription = "Buscar",
                                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                                    modifier = Modifier.size(26.dp)
                                                )
                                            }
                                        }
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
                                            StateMessage(
                                                icon = Icons.Default.CloudOff, // O Icons.Default.Error
                                                title = "¡Ups! Algo salió mal",
                                                description = characterErrorMessage ?: "No pudimos conectar con el servidor.",
                                                buttonText = "Reintentar",
                                                onButtonClick = { /* Aquí llamas a tu función de carga de nuevo */ }
                                            )
                                        }

                                        animeCharactersDetail.isEmpty() -> {
                                            StateMessage(
                                                icon = Icons.Default.PersonOff,
                                                title = "Sin personajes",
                                                description = "Este anime aún no tiene personajes registrados en la base de datos."
                                            )
                                        }

                                        else -> {
                                            Column {
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
                            // ========== GALERÍA DE IMÁGENES ==========
                            item {
                                SectionHeader(
                                    title = "Galería",
                                    subtitle = if (isPicturesLoading) "Cargando..." else "${animePictures.size} imagen${if (animePictures.size != 1) "es" else ""}"
                                ) {
                                    if (!isPicturesLoading) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                        ) {
                                            Text(
                                                text = "${animePictures.size}",
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                                color = MaterialTheme.colorScheme.primary,
                                                fontFamily = PoppinsBold,
                                                fontSize = 13.sp
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                            }

                            if (isPicturesLoading) {
                                item {
                                    Box(Modifier.fillMaxWidth().padding(32.dp), Alignment.Center) {
                                        CircularProgressIndicator()
                                    }
                                }
                            } else if (animePictures.isEmpty()) {
                                item {
                                    Column(
                                        Modifier.fillMaxWidth().padding(32.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.PhotoLibrary, null,
                                            Modifier.size(40.dp),
                                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                                        )
                                        Text("Sin imágenes disponibles", fontFamily = PoppinsMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            } else {
                                item {
                                    val photos = if (showAllPictures) animePictures else animePictures.take(9)
                                    BoxWithConstraints(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                                        val gap = 6.dp
                                        val itemSize = (maxWidth - gap * 2) / 3
                                        Column(verticalArrangement = Arrangement.spacedBy(gap)) {
                                            photos.chunked(3).forEach { row ->
                                                Row(horizontalArrangement = Arrangement.spacedBy(gap)) {
                                                    row.forEach { picture ->
                                                        Box(
                                                            modifier = Modifier
                                                                .size(itemSize)
                                                                .clip(RoundedCornerShape(12.dp))
                                                                .clickable {
                                                                    selectedPictureUrl = picture.largeImageUrl
                                                                        ?: picture.imageUrl
                                                                }
                                                        ) {
                                                            AsyncImage(
                                                                model = picture.largeImageUrl ?: picture.imageUrl,
                                                                contentDescription = null,
                                                                contentScale = ContentScale.Crop,
                                                                modifier = Modifier.fillMaxSize()
                                                            )
                                                            Box(
                                                                modifier = Modifier
                                                                    .fillMaxWidth()
                                                                    .height(28.dp)
                                                                    .align(Alignment.BottomCenter)
                                                                    .background(
                                                                        Brush.verticalGradient(
                                                                            listOf(Color.Transparent,
                                                                                Color.Black.copy(alpha = 0.35f))
                                                                        )
                                                                    )
                                                            )
                                                        }
                                                    }
                                                    repeat(3 - row.size) { Spacer(Modifier.size(itemSize)) }
                                                }
                                            }
                                        }
                                    }
                                }
                                if (animePictures.size > 9 && !showAllPictures) {
                                    item {
                                        Box(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                                            OutlinedButton(
                                                onClick = { showAllPictures = true },
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(12.dp)
                                            ) {
                                                Icon(Icons.Default.ExpandMore, null, Modifier.size(18.dp))
                                                Spacer(Modifier.width(6.dp))
                                                Text("Ver ${animePictures.size - 9} fotos más", fontFamily = PoppinsMedium)
                                            }
                                        }
                                    }
                                }
                            }

                            item {
                                Spacer(modifier = Modifier.height(24.dp))
                            }

                            // ========== SECCIÓN DE VIDEOS ==========
                            if (isVideosLoading) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(100.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            } else if (animeVideos != null) {
                                // Filtrar promos con URL válida
                                val validPromos = animeVideos!!.promos.filter { !it.youtubeUrl.isNullOrBlank() }

                                // ========== TRAILERS / PROMOS ==========
                                if (validPromos.isNotEmpty()) {
                                    item {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.PlayCircle,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(24.dp)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text(
                                                text = "Trailers",
                                                color = MaterialTheme.colorScheme.onSurface,
                                                fontFamily = PoppinsBold,
                                                fontSize = 20.sp
                                            )
                                            Spacer(modifier = Modifier.weight(1f))
                                            Text(
                                                text = "${validPromos.size} video${if (validPromos.size != 1) "s" else ""}",
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                                fontFamily = PoppinsRegular,
                                                fontSize = 13.sp
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text(
                                            text = "Trailers y promos oficiales",
                                            fontFamily = PoppinsRegular,
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                                            modifier = Modifier.padding(start = 52.dp)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }

                                    item {
                                        LazyRow(
                                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                                            contentPadding = PaddingValues(horizontal = 16.dp)
                                        ) {
                                            items(validPromos) { promo ->
                                                VideoCard(
                                                    title = promo.title ?: "Trailer",
                                                    thumbnailUrl = promo.thumbnailUrl,
                                                    youtubeUrl = promo.youtubeUrl,
                                                    context = context
                                                )
                                            }
                                        }
                                    }
                                }

                                // Filtrar music videos con URL válida
                                val validMusicVideos = animeVideos!!.musicVideos.filter { !it.youtubeUrl.isNullOrBlank() }

                                // ========== MUSIC VIDEOS ==========
                                if (validMusicVideos.isNotEmpty()) {
                                    item {
                                        Spacer(modifier = Modifier.height(24.dp))
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.MusicNote,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(24.dp)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text(
                                                text = "Music Videos",
                                                color = MaterialTheme.colorScheme.onSurface,
                                                fontFamily = PoppinsBold,
                                                fontSize = 20.sp
                                            )
                                            Spacer(modifier = Modifier.weight(1f))
                                            Text(
                                                text = "${validMusicVideos.size} video${if (validMusicVideos.size != 1) "s" else ""}",
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                                fontFamily = PoppinsRegular,
                                                fontSize = 13.sp
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text(
                                            text = "Abre en YouTube",
                                            fontFamily = PoppinsRegular,
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                                            modifier = Modifier.padding(start = 52.dp)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }

                                    item {
                                        LazyRow(
                                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                                            contentPadding = PaddingValues(horizontal = 16.dp)
                                        ) {
                                            items(validMusicVideos) { musicVideo ->
                                                VideoCard(
                                                    title = musicVideo.title ?: musicVideo.songTitle ?: "Music Video",
                                                    subtitle = musicVideo.artist,
                                                    thumbnailUrl = musicVideo.thumbnailUrl,
                                                    youtubeUrl = musicVideo.youtubeUrl,
                                                    context = context
                                                )
                                            }
                                        }
                                    }
                                }

                            }

                            // ========== SECCIÓN DE EPISODIOS UNIFICADA ==========
                            item {
                                Spacer(modifier = Modifier.height(24.dp))
                                SectionHeader(
                                    title = "Episodios",
                                    subtitle = if (isEpisodesLoading && animeEpisodes.isEmpty()) "Cargando..." else "${animeEpisodes.size} emitidos"
                                ) {
                                    if (!isEpisodesLoading || animeEpisodes.isNotEmpty()) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                        ) {
                                            Text(
                                                text = "${animeEpisodes.size}",
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                                color = MaterialTheme.colorScheme.primary,
                                                fontFamily = PoppinsBold,
                                                fontSize = 13.sp
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            if (isEpisodesLoading && animeEpisodes.isEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(100.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            color = MaterialTheme.colorScheme.primary,
                                            strokeWidth = 2.dp,
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }
                                }
                            } else if (animeEpisodes.isEmpty()) {
                                item {
                                    Column(
                                        Modifier.fillMaxWidth().padding(32.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.OndemandVideo, null,
                                            Modifier.size(40.dp),
                                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                                        )
                                        Text("Sin episodios disponibles", fontFamily = PoppinsMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            } else {
                                // Mapa de imágenes de episodios desde el endpoint de videos
                                val episodeVideoImages = animeVideosRemember

                                item {
                                    var selectedEpisodeId by remember { mutableStateOf<Int?>(null) }
                                    val selectedEp = animeEpisodes.find { it.malId == selectedEpisodeId }
                                    val selectedEpVideo = selectedEpisodeId?.let { episodeVideoImages[it] }

                                    Column {
                                        // LazyRow de episodios
                                        LazyRow(
                                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                                            contentPadding = PaddingValues(horizontal = 16.dp)
                                        ) {
                                            items(animeEpisodes) { episode ->
                                                val videoInfo = episodeVideoImages[episode.malId]
                                                val isSelected = selectedEpisodeId == episode.malId

                                                Card(
                                                    modifier = Modifier
                                                        .width(170.dp)
                                                        .height(130.dp)
                                                        .clickable {
                                                            animeDetail?.let { anime ->
                                                                animeDetailViewModel.loadEpisodeDetail(anime.malId, episode.malId!!)
                                                            }
                                                        },
                                                    shape = RoundedCornerShape(14.dp),
                                                    elevation = CardDefaults.cardElevation(
                                                        defaultElevation = if (isSelected) 8.dp else 2.dp
                                                    )
                                                ) {
                                                    Box(Modifier.fillMaxSize()) {
                                                        // Imagen de fondo o placeholder
                                                        if (!videoInfo?.imageUrl.isNullOrBlank()) {
                                                            AsyncImage(
                                                                model = videoInfo?.imageUrl,
                                                                contentDescription = episode.title,
                                                                contentScale = ContentScale.Crop,
                                                                modifier = Modifier.fillMaxSize()
                                                            )
                                                        } else {
                                                            Box(
                                                                Modifier
                                                                    .fillMaxSize()
                                                                    .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                                                                contentAlignment = Alignment.Center
                                                            ) {
                                                                Icon(
                                                                    Icons.Default.OndemandVideo, null,
                                                                    Modifier.size(32.dp),
                                                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
                                                                )
                                                            }
                                                        }
                                                        // Gradiente inferior
                                                        Box(
                                                            Modifier
                                                                .fillMaxWidth()
                                                                .height(90.dp)
                                                                .align(Alignment.BottomCenter)
                                                                .background(
                                                                    Brush.verticalGradient(
                                                                        listOf(
                                                                            Color.Transparent,
                                                                            Color.Black.copy(alpha = 0.9f)
                                                                        )
                                                                    )
                                                                )
                                                        )
                                                        // Badges superiores
                                                        Row(
                                                            modifier = Modifier
                                                                .align(Alignment.TopStart)
                                                                .padding(6.dp),
                                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                        ) {
                                                            Box(
                                                                Modifier
                                                                    .background(
                                                                        MaterialTheme.colorScheme.primary,
                                                                        RoundedCornerShape(20.dp)
                                                                    )
                                                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                                                            ) {
                                                                Text(
                                                                    text = "Ep ${episode.malId ?: "?"}",
                                                                    color = MaterialTheme.colorScheme.onPrimary,
                                                                    fontSize = 9.sp,
                                                                    fontFamily = PoppinsBold
                                                                )
                                                            }
                                                            if (episode.filler == true) {
                                                                Box(
                                                                    Modifier
                                                                        .background(
                                                                            Color(0xFFFF9800),
                                                                            RoundedCornerShape(20.dp)
                                                                        )
                                                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                                                ) {
                                                                    Text(
                                                                        "Filler",
                                                                        color = Color.White,
                                                                        fontSize = 9.sp,
                                                                        fontFamily = PoppinsBold
                                                                    )
                                                                }
                                                            } else if (episode.recap == true) {
                                                                Box(
                                                                    Modifier
                                                                        .background(
                                                                            Color(0xFF9C27B0),
                                                                            RoundedCornerShape(20.dp)
                                                                        )
                                                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                                                ) {
                                                                    Text(
                                                                        "Recap",
                                                                        color = Color.White,
                                                                        fontSize = 9.sp,
                                                                        fontFamily = PoppinsBold
                                                                    )
                                                                }
                                                            }
                                                        }
                                                        // Info inferior superpuesta
                                                        Column(
                                                            modifier = Modifier
                                                                .align(Alignment.BottomStart)
                                                                .padding(horizontal = 8.dp, vertical = 7.dp)
                                                                .fillMaxWidth(),
                                                            verticalArrangement = Arrangement.spacedBy(3.dp)
                                                        ) {
                                                            Text(
                                                                text = episode.title ?: "Episodio ${episode.malId}",
                                                                color = Color.White,
                                                                fontSize = 11.sp,
                                                                fontFamily = PoppinsBold,
                                                                maxLines = 2,
                                                                overflow = TextOverflow.Ellipsis,
                                                                lineHeight = 15.sp
                                                            )
                                                            if (episode.score != null && episode.score > 0) {
                                                                Row(
                                                                    verticalAlignment = Alignment.CenterVertically,
                                                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                                                ) {
                                                                    Icon(
                                                                        Icons.Default.Star, null,
                                                                        Modifier.size(10.dp),
                                                                        tint = Color(0xFFFFD700)
                                                                    )
                                                                    Text(
                                                                        text = String.format("%.1f", episode.score),
                                                                        color = Color.White.copy(alpha = 0.8f),
                                                                        fontSize = 10.sp,
                                                                        fontFamily = PoppinsMedium
                                                                    )
                                                                }
                                                            }
                                                        }
                                                        // Anillo de selección
                                                        if (isSelected) {
                                                            Box(
                                                                Modifier
                                                                    .fillMaxSize()
                                                                    .border(
                                                                        2.dp,
                                                                        MaterialTheme.colorScheme.primary,
                                                                        RoundedCornerShape(14.dp)
                                                                    )
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                // Botón para cargar más
                                if (hasMoreEpisodes) {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (isEpisodesLoading) {
                                                CircularProgressIndicator(
                                                    color = MaterialTheme.colorScheme.primary,
                                                    strokeWidth = 2.dp,
                                                    modifier = Modifier.size(28.dp)
                                                )
                                            } else {
                                                OutlinedButton(
                                                    onClick = {
                                                        animeDetail?.let {
                                                            animeDetailViewModel.loadAnimeEpisodes(it.malId, loadMore = true)
                                                        }
                                                    }
                                                ) {
                                                    Icon(
                                                        Icons.Default.ExpandMore,
                                                        contentDescription = null,
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(
                                                        text = "Cargar más episodios",
                                                        fontFamily = PoppinsMedium
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // ========== SECCIÓN DE FORO ==========
                            item {
                                Spacer(modifier = Modifier.height(24.dp))
                                SectionHeader(
                                    title = "Foro",
                                    subtitle = if (isForumLoading) "Cargando..." else "${forumTopics.size} tema${if (forumTopics.size != 1) "s en MyAnimeList" else " en MyAnimeList"}"
                                ) {
                                    if (!isForumLoading) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                        ) {
                                            Text(
                                                text = "${forumTopics.size}",
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                                color = MaterialTheme.colorScheme.primary,
                                                fontFamily = PoppinsBold,
                                                fontSize = 13.sp
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                            if (isForumLoading) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(32.dp),
                                        contentAlignment = Alignment.Center
                                    ) { CircularProgressIndicator() }
                                }
                            } else if (forumTopics.isEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(32.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "No hay temas de discusión",
                                            fontFamily = PoppinsMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            } else {
                                items(forumTopics) { topic ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 6.dp),
                                        shape = RoundedCornerShape(20.dp), // Más redondeado para un look moderno
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                                                2.dp
                                            )
                                        ),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // Flat design con sutil color de elevación
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    pendingExternalUrl = topic.url
                                                    showExternalLinkDialog = true
                                                }
                                                .padding(16.dp), // Padding interno uniforme
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            // 1. INDICADOR DE ACTIVIDAD (En lugar de una barra lateral, un punto con "glow")
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                modifier = Modifier.padding(end = 12.dp)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(8.dp)
                                                        .clip(CircleShape)
                                                        .background(MaterialTheme.colorScheme.primary)
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                // Línea conectora sutil opcional
                                                Box(
                                                    modifier = Modifier
                                                        .width(1.dp)
                                                        .height(20.dp)
                                                        .background(
                                                            MaterialTheme.colorScheme.primary.copy(
                                                                alpha = 0.2f
                                                            )
                                                        )
                                                )
                                            }

                                            Column(
                                                modifier = Modifier.weight(1f),
                                                verticalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                // TÍTULO (Más prominente)
                                                Text(
                                                    text = topic.title,
                                                    maxLines = 2,
                                                    overflow = TextOverflow.Ellipsis,
                                                    fontFamily = PoppinsBold,
                                                    fontSize = 15.sp,
                                                    lineHeight = 20.sp,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )

                                                // INFO DE AUTOR Y METADATOS
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(
                                                            8.dp
                                                        )
                                                    ) {
                                                        // AVATAR MEJORADO
                                                        Surface(
                                                            shape = CircleShape,
                                                            color = MaterialTheme.colorScheme.primaryContainer.copy(
                                                                alpha = 0.4f
                                                            ),
                                                            modifier = Modifier.size(24.dp)
                                                        ) {
                                                            Box(contentAlignment = Alignment.Center) {
                                                                Text(
                                                                    text = topic.authorUsername.firstOrNull()
                                                                        ?.uppercase() ?: "?",
                                                                    fontSize = 11.sp,
                                                                    fontFamily = PoppinsBold,
                                                                    color = MaterialTheme.colorScheme.primary
                                                                )
                                                            }
                                                        }

                                                        Text(
                                                            text = topic.authorUsername,
                                                            fontFamily = PoppinsMedium,
                                                            fontSize = 12.sp,
                                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                                        )
                                                    }

                                                    // BADGE DE COMENTARIOS (Estilo burbuja de chat)
                                                    Surface(
                                                        shape = RoundedCornerShape(8.dp),
                                                        color = MaterialTheme.colorScheme.surfaceVariant.copy(
                                                            alpha = 0.5f
                                                        ),
                                                    ) {
                                                        Row(
                                                            modifier = Modifier.padding(
                                                                horizontal = 8.dp,
                                                                vertical = 4.dp
                                                            ),
                                                            verticalAlignment = Alignment.CenterVertically,
                                                            horizontalArrangement = Arrangement.spacedBy(
                                                                4.dp
                                                            )
                                                        ) {
                                                            Icon(
                                                                Icons.Rounded.ChatBubbleOutline,
                                                                null,
                                                                Modifier.size(12.dp),
                                                                tint = MaterialTheme.colorScheme.primary
                                                            )
                                                            Text(
                                                                text = "${topic.comments}",
                                                                fontFamily = PoppinsBold,
                                                                fontSize = 11.sp,
                                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                                            )
                                                        }
                                                    }
                                                }
                                            }

                                            // ICONO DE SALIDA (Más sutil y elegante)
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .padding(start = 12.dp)
                                                    .size(14.dp),
                                                tint = MaterialTheme.colorScheme.onSurface.copy(
                                                    alpha = 0.2f
                                                )
                                            )
                                        }
                                    }
                                }
                            }

                            item {
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                        if (selectedTabIndex == 3) {

                            item {
                                val studiosCount = animeDetail?.studios?.size ?: 0
                                SectionHeader(
                                    title = "Estudios",
                                    subtitle = "$studiosCount estudio${if (studiosCount != 1) "s" else ""}"
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                    ) {
                                        Text(
                                            text = "$studiosCount",
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                            color = MaterialTheme.colorScheme.primary,
                                            fontFamily = PoppinsBold,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                            }

                            items(animeDetail?.studios?.filterNotNull() ?: emptyList()) { studio ->
                                    val studioId = studio.idStudio
                                    val isExpanded = expandedStudioId == studioId

                                    Column(
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)
                                    ) {
                                        StudioCompactCard(
                                            studioName = studio.nameStudio ?: "Sin nombre",
                                            isExpanded = isExpanded,
                                            onClick = {
                                                expandedStudioId = if (isExpanded) null else studioId
                                            }
                                        )

                                        // AnimatedContent para mostrar información detallada
                                        AnimatedVisibility(
                                            visible = isExpanded,
                                            enter = expandVertically(
                                                animationSpec = tween(300),
                                                expandFrom = Alignment.Top
                                            ) + fadeIn(animationSpec = tween(300)),
                                            exit = shrinkVertically(
                                                animationSpec = tween(300),
                                                shrinkTowards = Alignment.Top
                                            ) + fadeOut(animationSpec = tween(300))
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(top = 12.dp)
                                            ) {
                                                when {
                                                    producerIsLoading || showProducerLoading -> {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(120.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.spacedBy(10.dp)
                                            ) {
                                                CircularProgressIndicator(
                                                    color = MaterialTheme.colorScheme.primary,
                                                    strokeWidth = 2.dp,
                                                    modifier = Modifier.size(28.dp)
                                                )
                                                Text(
                                                    text = "Cargando estudio...",
                                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                                    fontFamily = PoppinsRegular,
                                                    fontSize = 13.sp
                                                )
                                            }
                                        }
                                    }

                                    producerErrorMessage != null -> {
                                        Surface(
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(12.dp),
                                            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.15f),
                                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.3f))
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(14.dp),
                                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    Icons.Default.Close,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                                                    modifier = Modifier.size(20.dp)
                                                )
                                                Text(
                                                    text = "No se pudo cargar la información del estudio",
                                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                                    fontFamily = PoppinsRegular,
                                                    fontSize = 13.sp
                                                )
                                            }
                                        }
                                    }

                                    producerDetail != null -> {
                                        showProducerLoading = false

                                        Column(
                                            verticalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            // Logo y nombre del estudio
                                            Surface(
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(16.dp),
                                                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                                border = BorderStroke(
                                                    1.dp,
                                                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                                                )
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(16.dp),
                                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Surface(
                                                        shape = RoundedCornerShape(14.dp),
                                                        color = MaterialTheme.colorScheme.surface,
                                                        border = BorderStroke(
                                                            1.dp,
                                                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                                                        )
                                                    ) {
                                                        Box(
                                                            modifier = Modifier
                                                                .size(80.dp),
                                                            contentAlignment = Alignment.Center
                                                        ) {
                                                            AsyncImage(
                                                                model = producerDetail?.images,
                                                                contentDescription = "Logo del estudio",
                                                                modifier = Modifier.fillMaxSize(),
                                                                contentScale = ContentScale.Crop
                                                            )
                                                        }
                                                    }

                                                    Column(
                                                        modifier = Modifier.weight(1f),
                                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                                    ) {
                                                        producerDetail?.titles?.firstOrNull()?.title?.let { title ->
                                                            Text(
                                                                text = title,
                                                                fontFamily = PoppinsBold,
                                                                color = MaterialTheme.colorScheme.onSurface,
                                                                fontSize = 18.sp,
                                                                lineHeight = 22.sp,
                                                                maxLines = 2,
                                                                overflow = TextOverflow.Ellipsis
                                                            )
                                                        }

                                                        Row(
                                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                            verticalAlignment = Alignment.CenterVertically
                                                        ) {
                                                            producerDetail?.established?.let { established ->
                                                                val date = established.split("T").firstOrNull()
                                                                Text(
                                                                    text = date ?: "",
                                                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                                                    fontFamily = PoppinsRegular,
                                                                    fontSize = 12.sp
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            // Descripción del estudio
                                            producerDetail?.about?.let { about ->
                                                var expandedProducerAbout by remember {
                                                    mutableStateOf(false)
                                                }
                                                Column(
                                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                                ) {
                                                    Text(
                                                        text = about,
                                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                                                        fontFamily = PoppinsRegular,
                                                        textAlign = TextAlign.Justify,
                                                        lineHeight = 22.sp,
                                                        fontSize = 13.sp,
                                                        overflow = TextOverflow.Ellipsis,
                                                        maxLines = if (expandedProducerAbout) Int.MAX_VALUE else 4
                                                    )

                                                    if (about.length > 150) {
                                                        Text(
                                                            text = if (expandedProducerAbout) "Ver menos" else "Ver más",
                                                            fontFamily = PoppinsBold,
                                                            fontSize = 13.sp,
                                                            color = MaterialTheme.colorScheme.primary,
                                                            modifier = Modifier.clickable {
                                                                expandedProducerAbout = !expandedProducerAbout
                                                            }
                                                        )
                                                    }
                                                }
                                            }

                                            // Enlaces externos
                                            val externalLinks = producerDetail?.external
                                            if (!externalLinks.isNullOrEmpty()) {
                                                LazyRow(
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                ) {
                                                    items(externalLinks) { external ->
                                                        Surface(
                                                            onClick = {
                                                                external.url?.let {
                                                                    uriHandler.openUri(it)
                                                                }
                                                            },
                                                            shape = RoundedCornerShape(20.dp),
                                                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                                            border = BorderStroke(
                                                                1.dp,
                                                                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                                            )
                                                        ) {
                                                            Row(
                                                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                                                verticalAlignment = Alignment.CenterVertically,
                                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                            ) {
                                                                Icon(
                                                                    imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                                                                    contentDescription = null,
                                                                    tint = MaterialTheme.colorScheme.primary,
                                                                    modifier = Modifier.size(16.dp)
                                                                )
                                                                Text(
                                                                    text = external.name ?: "Enlace",
                                                                    maxLines = 1,
                                                                    overflow = TextOverflow.Ellipsis,
                                                                    color = MaterialTheme.colorScheme.primary,
                                                                    fontFamily = PoppinsMedium,
                                                                    fontSize = 13.sp
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
                                    }
                                }
                        }

                    }
                    if (showExternalLinkDialog) {
                        CustomDialog(
                            onDismissRequest = { showExternalLinkDialog = false },
                            onConfirm = {
                                if (pendingExternalUrl.isNotEmpty()) uriHandler.openUri(pendingExternalUrl)
                                showExternalLinkDialog = false
                            },
                            onDismiss = { showExternalLinkDialog = false },
                            title = "Saliendo de SeijakuList",
                            message = "Estás a punto de abrir un enlace externo en MyAnimeList. Se abrirá en tu navegador.",
                            confirmButtonText = "Abrir",
                            dismissButtonText = "Cancelar",
                            type = DialogType.INFO
                        )
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
            // TopAppBar transparente superpuesto — flota sobre el contenido scrolleable
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                // Degradado siempre visible para legibilidad del botón volver
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
                                    Color.Transparent
                                )
                            )
                        )
                )
                // Fondo sólido que aparece al scrollear
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            MaterialTheme.colorScheme.background.copy(alpha = headerBgAlpha)
                        )
                )
                // Botón volver (siempre visible)
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_left_line),
                        contentDescription = "Volver",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                // Título (aparece al scrollear)
                Text(
                    text = "Detalle del anime",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontFamily = PoppinsBold,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .alpha(headerBgAlpha)
                )
            }
        }

        if (showAddToListSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showAddToListSheet = false
                    showChangePlannedDialog = false
                    pendingNewStatus = null
                },
                sheetState = addToListSheetState,
            ) {
                val isFormValid = selectedStatus != null

                if (showStartDatePicker) {
                    DatePickerDialog(
                        onDismissRequest = { showStartDatePicker = false },
                        confirmButton = {
                            Button(onClick = { showStartDatePicker = false }) { Text("OK") }
                        },
                        dismissButton = {
                            Button(onClick = { showStartDatePicker = false }) { Text("Cancelar") }
                        }
                    ) {
                        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = startDate)
                        DatePicker(state = datePickerState)
                        startDate = datePickerState.selectedDateMillis
                    }
                }

                if (showEndDatePicker) {
                    DatePickerDialog(
                        onDismissRequest = { showEndDatePicker = false },
                        confirmButton = {
                            Button(onClick = { showEndDatePicker = false }) { Text("OK") }
                        },
                        dismissButton = {
                            Button(onClick = { showEndDatePicker = false }) { Text("Cancelar") }
                        }
                    ) {
                        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = endDate)
                        DatePicker(state = datePickerState)
                        endDate = datePickerState.selectedDateMillis
                    }
                }

                if (showChangePlannedDialog) {
                    ConfirmChangePlannedDialog(
                        newStatus = pendingNewStatus ?: "",
                        onConfirm = {
                            showChangePlannedDialog = false
                            val newSt = pendingNewStatus
                            pendingNewStatus = null
                            if (newSt != null) {
                                selectedStatus = newSt
                                val scoreToPass = if (newSt == "Planeado") 0.0f else userRating
                                animeDetailViewModel.addAnimeToList(
                                    userScore = scoreToPass,
                                    userStatus = newSt,
                                    userOpinion = userOpinion,
                                    startDate = startDate,
                                    endDate = endDate,
                                    plannedPriority = null,
                                    plannedNote = null
                                )
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "Estado actualizado",
                                        actionLabel = "Ver lista",
                                        duration = SnackbarDuration.Short
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        navController.navigate(AppDestinations.MY_ANIMES_ROUTE)
                                    }
                                }
                                showAddToListSheet = false
                            }
                        },
                        onDismiss = {
                            showChangePlannedDialog = false
                            pendingNewStatus = null
                        }
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .padding(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = if (isAdded) "Editar en mi lista" else "Añadir a mi lista",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 20.sp,
                            fontFamily = PoppinsBold
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Estado",
                            fontSize = 16.sp,
                            fontFamily = PoppinsBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        val animeAiringStatus = animeDetail?.status ?: ""
                        val isNotYetAired = animeAiringStatus == "Not yet aired"
                        val isCurrentlyAiring = animeAiringStatus == "Currently Airing"

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.height(180.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(statusAnime) { status ->
                                val isSelected = selectedStatus == status
                                val isDisabled = when {
                                    isNotYetAired -> status != "Planeado"
                                    isCurrentlyAiring -> status == "Completado"
                                    else -> false
                                }
                                Surface(
                                    onClick = {
                                        if (!isDisabled) {
                                            selectedStatus = if (isSelected) null else status
                                        }
                                    },
                                    modifier = Modifier
                                        .height(50.dp)
                                        .then(if (isDisabled) Modifier.alpha(0.4f) else Modifier),
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected)
                                        statusColors[status] ?: MaterialTheme.colorScheme.primaryContainer
                                    else
                                        MaterialTheme.colorScheme.surfaceContainerHighest,
                                    shadowElevation = if (isSelected) 4.dp else 1.dp
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(16.dp),
                                                    tint = Color.Black
                                                )
                                            }
                                            Text(
                                                text = status,
                                                fontSize = 14.sp,
                                                fontFamily = PoppinsRegular,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    AnimatedVisibility(
                        visible = selectedStatus == "Planeado",
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Prioridad",
                                fontSize = 16.sp,
                                fontFamily = PoppinsBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            val priorities = listOf("Alta", "Media", "Baja")
                            val priorityColors = mapOf(
                                "Alta" to Color(0xFFEF5350),
                                "Media" to Color(0xFFFFCA28),
                                "Baja" to Color(0xFF66BB6A)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                priorities.forEach { priority ->
                                    val isSelected = plannedPriority == priority
                                    Surface(
                                        onClick = { plannedPriority = if (isSelected) null else priority },
                                        modifier = Modifier.weight(1f).height(44.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isSelected)
                                            priorityColors[priority] ?: MaterialTheme.colorScheme.primaryContainer
                                        else
                                            MaterialTheme.colorScheme.surfaceContainerHighest,
                                        shadowElevation = if (isSelected) 4.dp else 1.dp
                                    ) {
                                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                            Text(
                                                text = priority,
                                                fontSize = 14.sp,
                                                fontFamily = PoppinsRegular,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                            Text(
                                text = "Nota del plan (opcional)",
                                fontSize = 16.sp,
                                fontFamily = PoppinsBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            OutlinedTextField(
                                value = plannedNote,
                                onValueChange = { plannedNote = it },
                                placeholder = { Text("¿Por qué lo tenés planeado?") },
                                modifier = Modifier.fillMaxWidth().height(100.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = Color.Transparent,
                                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    cursorColor = MaterialTheme.colorScheme.primary
                                ),
                                shape = RoundedCornerShape(12.dp),
                                maxLines = 4
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible = selectedStatus != null && selectedStatus != "Planeado",
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Calificación",
                                    fontSize = 16.sp,
                                    fontFamily = PoppinsBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                if (userRating > 0) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = null,
                                                tint = Color(0xFFFFD700),
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Text(
                                                text = String.format("%.1f", userRating),
                                                fontSize = 14.sp,
                                                fontFamily = PoppinsBold,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                }
                            }
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceContainerHighest
                            ) {
                                Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
                                    RatingBar(rating = userRating, onRatingChange = { userRating = it })
                                }
                            }
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Opinión (opcional)",
                            fontSize = 16.sp,
                            fontFamily = PoppinsBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        OutlinedTextField(
                            value = userOpinion,
                            onValueChange = { userOpinion = it },
                            placeholder = { Text("Comparte tu opinión...") },
                            modifier = Modifier.fillMaxWidth().height(120.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = Color.Transparent,
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                cursorColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            maxLines = 5
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Fechas (opcional)",
                            fontSize = 16.sp,
                            fontFamily = PoppinsBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        val canSelectStartDate = selectedStatus != "Planeado"
                        val canSelectEndDate = selectedStatus == "Completado"
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                onClick = { if (canSelectStartDate) showStartDatePicker = true },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                color = if (canSelectStartDate)
                                    MaterialTheme.colorScheme.surfaceContainerHighest
                                else
                                    MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarToday,
                                        contentDescription = null,
                                        tint = if (canSelectStartDate)
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                        else
                                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = startDate?.let { dateFormat.format(it) } ?: "Inicio",
                                        fontSize = 12.sp,
                                        fontFamily = PoppinsRegular,
                                        color = if (canSelectStartDate)
                                            MaterialTheme.colorScheme.onSurface
                                        else
                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                    )
                                }
                            }
                            Surface(
                                onClick = { if (canSelectEndDate) showEndDatePicker = true },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                color = if (canSelectEndDate)
                                    MaterialTheme.colorScheme.surfaceContainerHighest
                                else
                                    MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarToday,
                                        contentDescription = null,
                                        tint = if (canSelectEndDate)
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                        else
                                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = endDate?.let { dateFormat.format(it) } ?: "Final",
                                        fontSize = 12.sp,
                                        fontFamily = PoppinsRegular,
                                        color = if (canSelectEndDate)
                                            MaterialTheme.colorScheme.onSurface
                                        else
                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                    )
                                }
                            }
                        }
                    }

                    Button(
                        onClick = {
                            if (isFormValid) {
                                val newStatus = selectedStatus!!
                                val wasPlanned = existingAnime?.userStatus == "Planeado"
                                val changingFromPlanned = wasPlanned && newStatus != "Planeado"
                                val hadPriorityData = existingAnime?.plannedPriority != null || existingAnime?.plannedNote?.isNotBlank() == true

                                if (changingFromPlanned && hadPriorityData) {
                                    pendingNewStatus = newStatus
                                    showChangePlannedDialog = true
                                } else {
                                    val scoreToPass = if (newStatus == "Planeado") 0.0f else userRating
                                    animeDetailViewModel.addAnimeToList(
                                        userScore = scoreToPass,
                                        userStatus = newStatus,
                                        userOpinion = userOpinion,
                                        startDate = startDate,
                                        endDate = endDate,
                                        plannedPriority = if (newStatus == "Planeado") plannedPriority else null,
                                        plannedNote = if (newStatus == "Planeado" && plannedNote.isNotBlank()) plannedNote else null
                                    )
                                    scope.launch {
                                        val result = snackbarHostState.showSnackbar(
                                            message = "Anime guardado en tu lista",
                                            actionLabel = "Ver lista",
                                            duration = SnackbarDuration.Short
                                        )
                                        if (result == SnackbarResult.ActionPerformed) {
                                            navController.navigate(AppDestinations.MY_ANIMES_ROUTE)
                                        }
                                    }
                                    showAddToListSheet = false
                                }
                            }
                        },
                        enabled = isFormValid,
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 8.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(20.dp))
                            Text(text = "Guardar en mi lista", fontSize = 16.sp, fontFamily = PoppinsBold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
        }
    }

    // Modal para ver imagen en grande
    if (selectedPictureUrl != null) {
        Dialog(
            onDismissRequest = { selectedPictureUrl = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.9f))
                    .clickable { selectedPictureUrl = null },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = selectedPictureUrl,
                    contentDescription = "Imagen ampliada",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                IconButton(
                    onClick = { selectedPictureUrl = null },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(50))
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

    // Modal para ver detalle del episodio
    if (selectedEpisodeDetail != null || isEpisodeDetailLoading) {
        Dialog(
            onDismissRequest = { animeDetailViewModel.clearEpisodeDetail() },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f))
                    .clickable { animeDetailViewModel.clearEpisodeDetail() },
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .clickable(enabled = false) { } // Evitar que el click cierre el modal
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    if (isEpisodeDetailLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else {
                        selectedEpisodeDetail?.let { episode ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp)
                            ) {
                                // Header con número de episodio y botón cerrar
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(48.dp)
                                                .background(
                                                    MaterialTheme.colorScheme.primary,
                                                    RoundedCornerShape(8.dp)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "${episode.malId ?: "?"}",
                                                color = MaterialTheme.colorScheme.onPrimary,
                                                fontSize = 18.sp,
                                                fontFamily = PoppinsBold
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = "Episodio ${episode.malId}",
                                            style = MaterialTheme.typography.titleLarge,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontFamily = PoppinsBold
                                        )
                                    }
                                    IconButton(
                                        onClick = { animeDetailViewModel.clearEpisodeDetail() }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Cerrar",
                                            tint = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Título
                                Text(
                                    text = episode.title ?: "Sin título",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontFamily = PoppinsBold
                                )

                                // Título japonés
                                if (!episode.titleJapanese.isNullOrBlank()) {
                                    Text(
                                        text = episode.titleJapanese,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                        fontFamily = PoppinsRegular
                                    )
                                }

                                // Título romanji
                                if (!episode.titleRomanji.isNullOrBlank()) {
                                    Text(
                                        text = episode.titleRomanji,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                        fontFamily = PoppinsRegular,
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Info row: Duración, Fecha de emisión
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    // Duración
                                    if (episode.duration != null && episode.duration > 0) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                Icons.Default.Timer,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "${episode.duration} seg",
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                                fontSize = 13.sp,
                                                fontFamily = PoppinsMedium
                                            )
                                        }
                                    }

                                    // Fecha de emisión
                                    if (!episode.aired.isNullOrBlank()) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                Icons.Default.CalendarToday,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = episode.aired.substringBefore("T"),
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                                fontSize = 13.sp,
                                                fontFamily = PoppinsMedium
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Badges: Filler y Recap
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    if (episode.filler == true) {
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    Color(0xFFFF9800),
                                                    RoundedCornerShape(6.dp)
                                                )
                                                .padding(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = "Filler",
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontFamily = PoppinsBold
                                            )
                                        }
                                    }

                                    if (episode.recap == true) {
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    Color(0xFF9C27B0),
                                                    RoundedCornerShape(6.dp)
                                                )
                                                .padding(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = "Recap",
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontFamily = PoppinsBold
                                            )
                                        }
                                    }
                                }

                                // Sinopsis
                                if (!episode.synopsis.isNullOrBlank()) {
                                    Spacer(modifier = Modifier.height(16.dp))

                                    Text(
                                        text = "Sinopsis",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontFamily = PoppinsBold
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = episode.synopsis,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                        fontFamily = PoppinsRegular,
                                        lineHeight = 22.sp
                                    )
                                }

                                // Botón para ver en MAL
                                if (!episode.url.isNullOrBlank()) {
                                    Spacer(modifier = Modifier.height(20.dp))

                                    Button(
                                        onClick = {
                                            try {
                                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(episode.url))
                                                context.startActivity(intent)
                                            } catch (e: Exception) {
                                                // Handle error
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary
                                        )
                                    ) {
                                        Icon(
                                            Icons.Default.OpenInNew,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Ver en MyAnimeList",
                                            fontFamily = PoppinsMedium
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
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontFamily = PoppinsBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (themes.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (title == "Openings")
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    else
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "${themes.size}",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        color = if (title == "Openings")
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondary,
                        fontFamily = PoppinsBold,
                        fontSize = 11.sp
                    )
                }
            }
        }

        if (themes.isEmpty()) {
            Text(
                text = "No hay $title disponibles",
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                fontFamily = PoppinsRegular,
                fontSize = 13.sp
            )
        } else {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                themes.forEach { theme ->
                    val accentColor = if (title == "Openings")
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.secondary

                    Surface(
                        onClick = {
                            animeThemesViewModel.openYoutubeSearch(context, theme)
                        },
                        shape = RoundedCornerShape(20.dp),
                        color = accentColor.copy(alpha = 0.1f),
                        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.25f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MusicNote,
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = theme,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = PoppinsRegular,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- COMPONENTES DE APOYO (Para limpiar el código principal) ---

@Composable
fun QuickInfoItem(icon: ImageVector, text: String, iconColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = iconColor, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface, fontFamily = PoppinsBold)
    }
}

@Composable
fun DetailChip(icon: ImageVector, text: String) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(6.dp))
            Text(text, fontSize = 12.sp, fontFamily = PoppinsMedium)
        }
    }
}

@Composable
fun DetailChipSmall(icon: ImageVector, text: String) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, modifier = Modifier.size(10.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text, fontSize = 10.sp, fontFamily = PoppinsMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    trailingContent: @Composable () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(24.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    style = TextStyle(
                        fontFamily = PoppinsBold,
                        fontSize = 22.sp,
                        letterSpacing = (-0.5).sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = subtitle,
                    style = TextStyle(
                        fontFamily = PoppinsRegular,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                )
            }
        }
        trailingContent()
    }
}

@Composable
fun StatusChip(status: String) {
    // Definimos los colores basados en el estado
    val (statusBg, statusFg, statusLabel) = when (status) {
        "Currently Airing" -> Triple(Color(0xFF4CAF50).copy(alpha = 0.15f), Color(0xFF4CAF50), "En emisión")
        "Finished Airing" -> Triple(Color(0xFF2196F3).copy(alpha = 0.15f), Color(0xFF2196F3), "Finalizado")
        "Not yet aired" -> Triple(Color(0xFFFF9800).copy(alpha = 0.15f), Color(0xFFFF9800), "Próximamente")
        else -> Triple(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.onSurfaceVariant, status)
    }

    Surface(
        shape = RoundedCornerShape(50), // Estilo píldora
        color = statusBg,
        border = BorderStroke(0.5.dp, statusFg.copy(alpha = 0.3f)) // Un borde sutil le da más calidad
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // El puntito indicador con un pequeño brillo (opcional)
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(statusFg)
            )
            Text(
                text = statusLabel,
                color = statusFg,
                fontSize = 11.sp,
                fontFamily = PoppinsBold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun StateMessage(
    icon: ImageVector,
    title: String,
    description: String,
    buttonText: String? = null,
    onButtonClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontFamily = PoppinsBold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = PoppinsRegular,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        if (buttonText != null && onButtonClick != null) {
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedButton(
                onClick = onButtonClick,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(buttonText, fontFamily = PoppinsBold)
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
private fun CompactInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontFamily = PoppinsRegular,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.5f)
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontFamily = PoppinsBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(0.5f),
            textAlign = TextAlign.End,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
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
            fontFamily = PoppinsBold,
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

@Composable
private fun StudioCompactCard(
    studioName: String,
    isExpanded: Boolean = false,
    onClick: () -> Unit = {}
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "Arrow rotation"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isExpanded)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
        animationSpec = tween(300),
        label = "border_color"
    )

    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        color = if (isExpanded)
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
        else
            MaterialTheme.colorScheme.surfaceContainerHigh,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Business,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = studioName,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = PoppinsBold,
                        fontSize = 15.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 20.sp
                    )
                    Text(
                        text = if (isExpanded) "Toca para cerrar" else "Toca para ver detalles",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                        fontFamily = PoppinsRegular,
                        fontSize = 11.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Contraer" else "Expandir",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(22.dp)
                        .rotate(rotationAngle)
                )
            }
        }
    }
}

@Composable
private fun VideoCard(
    title: String,
    subtitle: String? = null,
    thumbnailUrl: String?,
    youtubeUrl: String?,
    context: Context
) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .clickable {
                youtubeUrl?.let { url ->
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        intent.setPackage("com.google.android.youtube")
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(webIntent)
                        }
                    } catch (e: Exception) {
                        // Handle error silently
                    }
                }
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(112.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            ) {
                if (!thumbnailUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = thumbnailUrl,
                        contentDescription = title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                // Play icon overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayCircle,
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .padding(12.dp)
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 13.sp,
                    fontFamily = PoppinsMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (subtitle != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        fontSize = 11.sp,
                        fontFamily = PoppinsRegular,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun EpisodeCard(
    title: String,
    episode: String?,
    imageUrl: String?,
    url: String?,
    context: Context
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable {
                url?.let { link ->
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        // Handle error silently
                    }
                }
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            ) {
                if (!imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.OndemandVideo,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                // Episode badge
                if (episode != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = episode,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 10.sp,
                            fontFamily = PoppinsBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EpisodeListItem(
    episode: AnimeEpisode,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Episode number badge
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${episode.malId ?: "?"}",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp,
                    fontFamily = PoppinsBold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Episode info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = episode.title ?: "Episodio ${episode.malId}",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp,
                    fontFamily = PoppinsMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (!episode.titleJapanese.isNullOrBlank()) {
                    Text(
                        text = episode.titleJapanese,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        fontSize = 12.sp,
                        fontFamily = PoppinsRegular,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Aired date
                    if (!episode.aired.isNullOrBlank()) {
                        Text(
                            text = episode.aired.substringBefore("T"),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            fontSize = 11.sp,
                            fontFamily = PoppinsRegular
                        )
                    }

                    // Score
                    if (episode.score != null && episode.score > 0) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = String.format("%.1f", episode.score),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                fontSize = 11.sp,
                                fontFamily = PoppinsMedium
                            )
                        }
                    }

                    // Filler/Recap badges
                    if (episode.filler == true) {
                        Box(
                            modifier = Modifier
                                .background(
                                    Color(0xFFFF9800).copy(alpha = 0.2f),
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Filler",
                                color = Color(0xFFFF9800),
                                fontSize = 10.sp,
                                fontFamily = PoppinsMedium
                            )
                        }
                    }

                    if (episode.recap == true) {
                        Box(
                            modifier = Modifier
                                .background(
                                    Color(0xFF9C27B0).copy(alpha = 0.2f),
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Recap",
                                color = Color(0xFF9C27B0),
                                fontSize = 10.sp,
                                fontFamily = PoppinsMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

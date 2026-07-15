package com.yumedev.seijakulist.ui.screens.detail

import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.ui.draw.blur
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import android.widget.Toast
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.yumedev.seijakulist.domain.models.AnimeCharactersDetail
import com.yumedev.seijakulist.domain.models.AnimeDetail
import com.yumedev.seijakulist.domain.models.AnimeEntityDomain
import com.yumedev.seijakulist.ui.components.LoadingScreen
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.screens.detail.components.shared.CompactDemographicCard
import com.yumedev.seijakulist.ui.screens.detail.components.shared.CompactGenreCard
import com.yumedev.seijakulist.ui.screens.detail.components.shared.RatingBar
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsMedium
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import com.yumedev.seijakulist.ui.theme.adp
import com.yumedev.seijakulist.ui.theme.asp
import com.yumedev.seijakulist.ui.theme.getAnimeStatusColor
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Pantalla de detalle del anime
 * Diseño consistente con Material Design 3 y el resto de la app
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun AnimeDetailScreen(
    navController: NavController,
    animeId: Int?,
    initialTab: Int = 0,
    openSheet: Boolean = false,
    animeDetailViewModel: AnimeDetailAniListViewModel = hiltViewModel(),
    animeCharacterViewModel: AnimeCharacterDetailViewModel = hiltViewModel(),
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null
) {
    // ViewModels state
    val animeDetail by animeDetailViewModel.animeDetail.collectAsState()
    val isLoading by animeDetailViewModel.isLoading.collectAsState()
    val errorMessage by animeDetailViewModel.errorMessage.collectAsState()

    val isAdded by animeDetailViewModel.isAdded.collectAsState()
    val existingAnime by animeDetailViewModel.existingAnime.collectAsState()

    // Characters state
    val characters by animeCharacterViewModel.characters.collectAsState()
    val charactersLoading by animeCharacterViewModel.isLoading.collectAsState()

    // UI State
    var selectedTab by remember { mutableStateOf(AnimeDetailTab.OVERVIEW) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val listState = rememberLazyListState()
    val context = LocalContext.current
    val view = androidx.compose.ui.platform.LocalView.current

    // Modal Bottom Sheet state
    var showAddToListSheet by remember { mutableStateOf(openSheet) }

    // Configurar status bar y navigation bar para esta pantalla
    DisposableEffect(Unit) {
        val window = (context as? android.app.Activity)?.window
        val originalStatusBarColor = window?.statusBarColor

        window?.let {
            WindowCompat.setDecorFitsSystemWindows(it, false)
            it.statusBarColor = android.graphics.Color.TRANSPARENT
        }

        onDispose {
            // Restaurar el estado original al salir
            window?.let {
                WindowCompat.setDecorFitsSystemWindows(it, true)
                originalStatusBarColor?.let { color ->
                    it.statusBarColor = color
                }
            }
        }
    }

    // FAB visibility based on scroll
    val showFab by remember {
        derivedStateOf { listState.firstVisibleItemIndex >= 1 }
    }

    // Handle open sheet parameter
    LaunchedEffect(openSheet) {
        showAddToListSheet = openSheet
    }

    // Set initial tab if provided
    LaunchedEffect(initialTab) {
        selectedTab = when (initialTab) {
            1 -> AnimeDetailTab.INFORMATION
            2 -> AnimeDetailTab.CHARACTERS
            else -> AnimeDetailTab.OVERVIEW
        }
    }

    // Load characters when CHARACTERS tab is selected
    LaunchedEffect(selectedTab, animeId) {
        if (selectedTab == AnimeDetailTab.CHARACTERS && animeId != null) {
            animeCharacterViewModel.loadAnimeCharacters(animeId)
        }
    }

    // Main content
    when {
        isLoading && animeDetail == null -> {
            LoadingScreen()
        }

        errorMessage != null && animeDetail == null -> {
            AnimeErrorState(
                message = errorMessage ?: "Error desconocido",
                onRetry = { animeId?.let { animeDetailViewModel.loadAnimeDetail(it) } },
                onBack = { navController.popBackStack() }
            )
        }

        animeDetail != null -> {
            Scaffold(
                containerColor = Color.Transparent,
                floatingActionButton = {
                    AnimatedVisibility(
                        visible = showFab,
                        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                    ) {
                        if (isAdded) {
                            // Estado B: FAB circular "Editar" (56dp)
                            FloatingActionButton(
                                onClick = {
                                    showAddToListSheet = true
                                },
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                shape = CircleShape,
                                elevation = FloatingActionButtonDefaults.elevation(
                                    defaultElevation = 8.dp,
                                    pressedElevation = 12.dp,
                                    hoveredElevation = 10.dp
                                ),
                                modifier = Modifier
                                    .size(56.dp)
                                    .padding(bottom = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Editar",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        } else {
                            // Estado A: FAB extendido "Añadir a mi lista"
                            ExtendedFloatingActionButton(
                                onClick = {
                                    showAddToListSheet = true
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp)
                                    )
                                },
                                text = {
                                    Text(
                                        text = "Añadir a mi lista",
                                        fontFamily = PoppinsBold,
                                        fontSize = 16.sp,
                                        letterSpacing = 0.2.sp
                                    )
                                },
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                shape = RoundedCornerShape(20.dp),
                                elevation = FloatingActionButtonDefaults.elevation(
                                    defaultElevation = 8.dp,
                                    pressedElevation = 12.dp,
                                    hoveredElevation = 10.dp
                                ),
                                modifier = Modifier
                                    .height(60.dp)
                                    .padding(bottom = 8.dp)
                            )
                        }
                    }
                },
                snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState)
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = { focusManager.clearFocus() })
                        }
                ) {
                    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

                    // CONTENIDO CON SCROLL
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            bottom = paddingValues.calculateBottomPadding()
                        )
                    ) {
                        // BANNER CON BLUR + HEADER ENCIMA
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                // Banner image con blur (si existe)
                                animeDetail!!.bannerImage?.let { banner ->
                                    if (!banner.contains("no-image", ignoreCase = true) && banner.isNotBlank()) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(statusBarHeight + 320.dp)
                                        ) {
                                            // Banner image con blur
                                            AsyncImage(
                                                model = banner,
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .blur(25.dp),
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
                                                                Color.Black.copy(alpha = 0.4f),
                                                                Color.Black.copy(alpha = 0.5f),
                                                                MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                                                                MaterialTheme.colorScheme.background
                                                            )
                                                        )
                                                    )
                                            )
                                        }
                                    }
                                }

                                // Header con portada e información (ENCIMA del banner)
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = statusBarHeight + 56.dp)
                                ) {
                                    AnimeDetailHeader(
                                        animeDetail = animeDetail,
                                        isAdded = isAdded,
                                        onAddToListClick = {
                                            showAddToListSheet = true
                                        },
                                        sharedTransitionScope = sharedTransitionScope,
                                        animatedVisibilityScope = animatedVisibilityScope,
                                        animeId = animeId
                                    )
                                }
                            }
                        }

                        // Stats Cards
                        item {
                            AnimeStatsRow(animeDetail = animeDetail)
                        }

                        // Tab selector
                        item {
                            AnimeDetailTabSelector(
                                selectedTab = selectedTab,
                                onTabSelected = { selectedTab = it },
                                isAdded = isAdded
                            )
                        }

                        // Tab content
                        item {
                            AnimatedContent(
                                targetState = selectedTab,
                                transitionSpec = {
                                    (fadeIn(animationSpec = tween(300)) +
                                            slideInHorizontally(initialOffsetX = { it / 2 }))
                                        .togetherWith(
                                            fadeOut(animationSpec = tween(300)) +
                                                    slideOutHorizontally(targetOffsetX = { -it / 2 })
                                        )
                                },
                                label = "AnimeTabContent"
                            ) { tab ->
                                when (tab) {
                                    AnimeDetailTab.OVERVIEW -> {
                                        AnimeOverviewTab(animeDetail = animeDetail)
                                    }
                                    AnimeDetailTab.INFORMATION -> {
                                        AnimeInformationTab(animeDetail = animeDetail)
                                    }
                                    AnimeDetailTab.CHARACTERS -> {
                                        AnimeCharactersTab(
                                            characters = characters,
                                            isLoading = charactersLoading,
                                            navController = navController
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // TOP BAR CON BOTONES (transparente, sobre el banner)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(statusBarHeight + 56.dp)
                            .align(Alignment.TopCenter)
                    ) {
                        // Botón de atrás (izquierda)
                        Surface(
                            shape = CircleShape,
                            color = Color.Black.copy(alpha = 0.5f),
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(start = 8.dp, bottom = 8.dp)
                        ) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Volver",
                                    tint = Color.White
                                )
                            }
                        }

                        // Botón de compartir (derecha)
                        Surface(
                            shape = CircleShape,
                            color = Color.Black.copy(alpha = 0.5f),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 8.dp, bottom = 8.dp)
                        ) {
                            IconButton(
                                onClick = {
                                    // TODO: Implementar funcionalidad de compartir
                                    Toast.makeText(
                                        context,
                                        "Próximamente: Compartir anime",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Compartir",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Bottom Sheet para añadir/editar anime en la lista (Dialog personalizado sin drag)
    if (showAddToListSheet && animeDetail != null) {
        Dialog(
            onDismissRequest = { showAddToListSheet = false },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false // No respetar system bars
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(
                        onClick = { showAddToListSheet = false },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                contentAlignment = Alignment.BottomCenter
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.85f) // 85% de altura
                        .clickable(
                            onClick = { }, // No hacer nada al hacer clic en el contenido
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ),
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        // Drag handle visual (no funcional)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(32.dp)
                                    .height(4.dp)
                                    .background(
                                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                        RoundedCornerShape(2.dp)
                                    )
                            )
                        }

                        // Contenido del modal
                        com.yumedev.seijakulist.ui.screens.add_to_list.AddToListModalContent(
                            anime = animeDetail,
                            existingAnime = existingAnime,
                            isAdded = isAdded,
                            onDismiss = { showAddToListSheet = false },
                            onSave = { status, rating, startDate, endDate, priority, note ->
                                scope.launch {
                                    animeDetailViewModel.addAnimeToList(
                                        userScore = rating,
                                        userStatus = status ?: "Viendo",
                                        userOpinion = note,
                                        startDate = startDate,
                                        endDate = endDate,
                                        plannedPriority = priority,
                                        plannedNote = note
                                    )

                                    val statusEmoji = when (status) {
                                        "Viendo" -> "▶️"
                                        "Completado" -> "✅"
                                        "Pendiente" -> "⏳"
                                        "Abandonado" -> "❌"
                                        "Planeado" -> "📋"
                                        else -> "📺"
                                    }
                                    val message = if (isAdded)
                                        "$statusEmoji Anime actualizado en tu lista como '$status'"
                                    else
                                        "$statusEmoji Anime añadido a tu lista como '$status'"

                                    snackbarHostState.showSnackbar(
                                        message = message,
                                        duration = SnackbarDuration.Short
                                    )

                                    showAddToListSheet = false
                                }
                            },
                            onDelete = {
                                scope.launch {
                                    // TODO: Implement delete method
                                    snackbarHostState.showSnackbar("Anime eliminado de tu lista")
                                    showAddToListSheet = false
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

// ============================================================================
// TABS
// ============================================================================

enum class AnimeDetailTab(val title: String) {
    OVERVIEW("Resumen"),
    INFORMATION("Información"),
    CHARACTERS("Reparto")
}

@Composable
private fun AnimeDetailTabSelector(
    selectedTab: AnimeDetailTab,
    onTabSelected: (AnimeDetailTab) -> Unit,
    isAdded: Boolean,
    modifier: Modifier = Modifier
) {
    // Siempre 3 tabs según el diseño: Resumen, Información, Reparto
    val tabs = listOf(
        AnimeDetailTab.OVERVIEW,
        AnimeDetailTab.INFORMATION,
        AnimeDetailTab.CHARACTERS
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            shadowElevation = 0.dp
        ) {
            Row(modifier = Modifier.padding(4.dp)) {
                tabs.forEach { tab ->
                    AnimeTabItem(
                        text = tab.title,
                        isSelected = selectedTab == tab,
                        onClick = { onTabSelected(tab) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun AnimeTabItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected)
            MaterialTheme.colorScheme.primary
        else
            Color.Transparent,
        label = "Tab Background Color",
        animationSpec = tween(200)
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected)
            MaterialTheme.colorScheme.onPrimary
        else
            MaterialTheme.colorScheme.onSurfaceVariant,
        label = "Tab Text Color",
        animationSpec = tween(200)
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontFamily = if (isSelected) PoppinsBold else PoppinsMedium,
            fontSize = 13.sp,
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ============================================================================
// HEADER
// ============================================================================

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun AnimeDetailHeader(
    animeDetail: AnimeDetail?,
    isAdded: Boolean,
    onAddToListClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope?,
    animatedVisibilityScope: AnimatedVisibilityScope?,
    animeId: Int?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        // PORTADA (a la izquierda, más grande)
        Card(
            modifier = Modifier
                .width(150.dp)
                .height(215.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            val imageModifier = if (sharedTransitionScope != null &&
                animatedVisibilityScope != null && animeId != null
            ) {
                with(sharedTransitionScope) {
                    Modifier
                        .sharedElement(
                            sharedContentState = rememberSharedContentState(
                                key = "anime-image-$animeId"
                            ),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                }
            } else {
                Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
            }

            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(animeDetail?.images ?: "")
                    .size(Size.ORIGINAL)
                    .crossfade(false)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = imageModifier
            )
        }

        // INFORMACIÓN A LA DERECHA (alineada al bottom)
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Título principal
                Text(
                    text = animeDetail?.title ?: "",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 22.sp,
                    fontFamily = PoppinsBold,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 26.sp
                )

                // Título japonés
                if (!animeDetail?.titleJapanese.isNullOrBlank()) {
                    Text(
                        text = animeDetail.titleJapanese,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        fontFamily = PoppinsRegular,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Type y Status badges
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Type badge
                    AnimeTypeBadgePDF(type = animeDetail?.typeAnime ?: "")

                    // Status badge
                    AnimeStatusBadgePDF(status = animeDetail?.status ?: "")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón "Añadir a mi lista" o "Editar"
            Button(
                onClick = onAddToListClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Icon(
                    imageVector = if (isAdded) Icons.Default.Edit else Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isAdded) "Editar" else "Añadir a mi lista",
                    fontSize = 14.sp,
                    fontFamily = PoppinsBold
                )
            }
        }
    }
}

// ============================================================================
// TRAILER SECTION
// ============================================================================

@Composable
private fun AnimeTrailerSection(
    embedUrl: String,
    youtubeId: String?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Thumbnail con botón para abrir en YouTube
        Card(
            onClick = {
                if (youtubeId != null) {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/watch?v=$youtubeId")
                    )
                    context.startActivity(intent)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Thumbnail de YouTube
                AsyncImage(
                    model = "https://img.youtube.com/vi/$youtubeId/maxresdefault.jpg",
                    contentDescription = "Trailer thumbnail",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Overlay oscuro
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                )

                // Botón de play grande (negro transparente)
                Surface(
                    modifier = Modifier.align(Alignment.Center),
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.7f),
                    shadowElevation = 8.dp
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Ver trailer en YouTube",
                        tint = Color.White,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(48.dp)
                    )
                }

                // Badge "YouTube" pegado a la esquina superior derecha
                Surface(
                    modifier = Modifier.align(Alignment.TopEnd),
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 12.dp,
                        bottomStart = 6.dp,
                        bottomEnd = 0.dp
                    ),
                    color = Color(0xFFFF0000) // Rojo de YouTube
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "YouTube",
                            fontFamily = PoppinsBold,
                            fontSize = 10.sp,
                            color = Color.White
                        )
                    }
                }

                // Badge "TRAILER" pegado a la esquina superior izquierda
                Surface(
                    modifier = Modifier.align(Alignment.TopStart),
                    shape = RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 6.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 6.dp
                    ),
                    color = Color.Black.copy(alpha = 0.7f)
                ) {
                    Text(
                        text = "TRAILER",
                        fontFamily = PoppinsBold,
                        fontSize = 11.sp,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

// ============================================================================
// STATS ROW
// ============================================================================

@Composable
private fun AnimeStatsRow(
    animeDetail: AnimeDetail?,
    modifier: Modifier = Modifier
) {
    // Solo 3 estadísticas según el diseño: Score, Ranking, Usuarios
    val stats = remember(animeDetail) {
        buildList {
            animeDetail?.let {
                if (it.score > 0) {
                    add(
                        StatData(
                            icon = Icons.Default.Star,
                            value = String.format("%.1f", it.score),
                            label = "SCORE",
                            type = StatType.SCORE
                        )
                    )
                }
                if (it.rank != null && it.rank > 0) {
                    add(
                        StatData(
                            icon = Icons.Default.EmojiEvents,
                            value = "#${it.rank}",
                            label = "RANKING",
                            type = StatType.RANK
                        )
                    )
                }
                if (it.scoreBy != null && it.scoreBy > 0) {
                    add(
                        StatData(
                            icon = Icons.Default.People,
                            value = formatNumber(it.scoreBy),
                            label = "USUARIOS",
                            type = StatType.USERS
                        )
                    )
                }
            }
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        stats.forEachIndexed { index, stat ->
            AnimeStatCard(
                statData = stat,
                index = index,
                statsCount = stats.size,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// Clase de datos para las estadísticas
private data class StatData(
    val icon: ImageVector,
    val value: String,
    val label: String,
    val type: StatType
)

// Enum para tipos de estadísticas con colores específicos
private enum class StatType {
    SCORE, EPISODES, RANK, USERS
}

// Stats badges según el diseño - limpio y minimalista
@Composable
private fun AnimeStatCard(
    statData: StatData,
    index: Int,
    statsCount: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Icono con valor (para score muestra estrella)
            if (statData.type == StatType.SCORE) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = statData.value,
                        fontFamily = PoppinsBold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            } else {
                // Valor grande sin icono
                Text(
                    text = statData.value,
                    fontFamily = PoppinsBold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }

            // Label abajo (pequeño, en mayúsculas)
            Text(
                text = statData.label,
                fontFamily = PoppinsRegular,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                letterSpacing = 0.5.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ============================================================================
// TAB CONTENTS
// ============================================================================

@Composable
private fun AnimeOverviewTab(
    animeDetail: AnimeDetail?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Trailer embebido (si existe)
        animeDetail!!.trailer?.let { trailer ->
            trailer.embedUrl?.let { embedUrl ->
                AnimeTrailerSection(
                    embedUrl = embedUrl,
                    youtubeId = trailer.youtubeId
                )
            }
        }

        // Synopsis
        if (!animeDetail?.synopsis.isNullOrBlank()) {
            val clipboardManager = LocalClipboardManager.current
            var expanded by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Header con título y botones de acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Sinopsis",
                        fontSize = 16.sp,
                        fontFamily = PoppinsBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Botones de acción
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Botón de copiar
                        IconButton(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(animeDetail.synopsis))
                                Toast.makeText(
                                    context,
                                    "Sinopsis copiada",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copiar sinopsis",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Botón de traducir
                        IconButton(
                            onClick = {
                                val synopsis = animeDetail.synopsis
                                val textToTranslate = if (synopsis.length > 2000) {
                                    synopsis.substring(0, 2000) + "..."
                                } else {
                                    synopsis
                                }
                                val encodedText = URLEncoder.encode(textToTranslate, "UTF-8")
                                val url = "https://translate.google.com/m?sl=en&tl=es&q=$encodedText"

                                val customTabsIntent = CustomTabsIntent.Builder()
                                    .setShowTitle(true)
                                    .build()
                                customTabsIntent.launchUrl(context, Uri.parse(url))
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Translate,
                                contentDescription = "Traducir sinopsis",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
                val hasOverflow = textLayoutResult?.hasVisualOverflow ?: false

                Text(
                    text = animeDetail.synopsis,
                    fontFamily = PoppinsRegular,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = if (expanded) Int.MAX_VALUE else 6,
                    overflow = TextOverflow.Ellipsis,
                    onTextLayout = { textLayoutResult = it },
                    modifier = Modifier.animateContentSize()
                )

                if (hasOverflow || expanded) {
                    TextButton(
                        onClick = { expanded = !expanded },
                        modifier = Modifier.align(Alignment.Start)
                    ) {
                        Text(
                            text = if (expanded) "ver menos" else "ver más",
                            fontFamily = PoppinsMedium,
                            fontSize = 13.sp
                        )
                        Icon(
                            imageVector = if (expanded)
                                Icons.Default.ExpandLess
                            else
                                Icons.Default.ExpandMore,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        // Genres
        if (!animeDetail?.genres.isNullOrEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Géneros",
                    fontFamily = PoppinsBold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    animeDetail.genres.filterNotNull().forEach { genre ->
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerHighest
                        ) {
                            Text(
                                text = genre.name ?: "",
                                fontFamily = PoppinsMedium,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        // Demographics - mismo estilo que géneros
        if (!animeDetail?.demographics.isNullOrEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Demografía",
                    fontFamily = PoppinsBold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    animeDetail.demographics.filterNotNull().forEach { demographic ->
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerHighest
                        ) {
                            Text(
                                text = demographic.name ?: "",
                                fontFamily = PoppinsMedium,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun AnimeInformationTab(
    animeDetail: AnimeDetail?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Basic info - filas simples con dividers
        SimpleInfoRow("Título en inglés", animeDetail?.titleEnglish?.ifBlank { "—" } ?: "—")
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 12.dp),
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
        )

        SimpleInfoRow("Título japonés", animeDetail?.titleJapanese?.ifBlank { "—" } ?: "—")
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 12.dp),
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
        )

        SimpleInfoRow("Tipo", formatAnimeType(animeDetail?.typeAnime ?: ""))
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 12.dp),
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
        )

        // Estado con dot indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Estado",
                fontFamily = PoppinsRegular,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(
                            when (animeDetail?.status) {
                                "RELEASING" -> Color(0xFF00A8FF)
                                "FINISHED" -> Color(0xFF7EE787)
                                else -> MaterialTheme.colorScheme.onSurface
                            }
                        )
                )
                Text(
                    text = formatAnimeStatus(animeDetail?.status ?: ""),
                    fontFamily = PoppinsBold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.End
                )
            }
        }

        if (!animeDetail?.aired.isNullOrBlank()) {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
            SimpleInfoRow("Emisión", animeDetail.aired)
        }

        if (animeDetail?.episodes != null && animeDetail.episodes > 0) {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
            SimpleInfoRow("Episodios", "${animeDetail.episodes}${if (animeDetail.episodes > 1000) "+" else ""}")
        }

        if (!animeDetail?.duration.isNullOrBlank()) {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
            SimpleInfoRow("Duración", animeDetail.duration)
        }

        if (!animeDetail?.rating.isNullOrBlank()) {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
            SimpleInfoRow("Clasificación", animeDetail.rating)
        }

        if (!animeDetail?.source.isNullOrBlank()) {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
            SimpleInfoRow("Fuente", formatAnimeSource(animeDetail.source))
        }

        if (animeDetail?.score != null && animeDetail.score > 0) {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
            SimpleInfoRow("Puntuación", "${animeDetail.score} / 10")
        }

        if (animeDetail?.scoreBy != null && animeDetail.scoreBy > 0) {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
            SimpleInfoRow("Valoraciones", "${formatNumber(animeDetail.scoreBy)} usuarios")
        }

        // Studios
        if (!animeDetail?.studios.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Estudio",
                    fontFamily = PoppinsBold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    animeDetail.studios.filterNotNull()
                        .filter { !it.nameStudio.isNullOrBlank() }
                        .forEach { studio ->
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = MaterialTheme.colorScheme.surfaceContainerHigh
                            ) {
                                Text(
                                    text = studio.nameStudio ?: "",
                                    fontFamily = PoppinsMedium,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                                )
                            }
                        }
                }
            }
        }

        // Producers
        if (!animeDetail?.producers.isNullOrEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Productores",
                    fontFamily = PoppinsBold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    animeDetail.producers.filterNotNull()
                        .filter { !it.name.isNullOrBlank() }
                        .forEach { producer ->
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = MaterialTheme.colorScheme.surfaceContainerHigh
                            ) {
                                Text(
                                    text = producer.name ?: "",
                                    fontFamily = PoppinsMedium,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                                )
                            }
                        }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// Fila simple de información sin card
@Composable
private fun SimpleInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontFamily = PoppinsRegular,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            fontFamily = PoppinsBold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun AnimeCharactersTab(
    characters: List<AnimeCharactersDetail>,
    isLoading: Boolean,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var showAll by remember { mutableStateOf(false) }
    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    // Auto-focus cuando se activa la búsqueda
    LaunchedEffect(isSearching) {
        if (isSearching) {
            focusRequester.requestFocus()
        }
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Barra de búsqueda
        AnimatedVisibility(
            visible = !isLoading && characters.isNotEmpty(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            if (isSearching) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .focusRequester(focusRequester),
                    placeholder = {
                        Text(
                            text = "Buscar personaje...",
                            fontFamily = PoppinsRegular,
                            fontSize = 14.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            if (searchQuery.isEmpty()) {
                                isSearching = false
                            } else {
                                searchQuery = ""
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cerrar",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(50),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    ),
                    textStyle = TextStyle(
                        fontFamily = PoppinsMedium,
                        fontSize = 14.sp
                    )
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Reparto principal",
                        fontFamily = PoppinsBold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = { isSearching = true }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                characters.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.People,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                            modifier = Modifier.size(64.dp)
                        )
                        Text(
                            text = "Sin información de reparto",
                            fontFamily = PoppinsBold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Este anime aún no tiene personajes registrados",
                            fontFamily = PoppinsRegular,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }

                else -> {
                    // Filtrar personajes según búsqueda
                    val filteredCharacters = if (searchQuery.isBlank()) {
                        characters
                    } else {
                        characters.filter { character ->
                            character.nameCharacter?.contains(searchQuery, ignoreCase = true) == true
                        }
                    }

                    // Mostrar mensaje si no hay resultados
                    if (filteredCharacters.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                    modifier = Modifier.size(48.dp)
                                )
                                Text(
                                    text = "No se encontraron personajes",
                                    fontFamily = PoppinsBold,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Intenta con otro nombre",
                                    fontFamily = PoppinsRegular,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    } else {
                        // Mostrar 9 personajes o todos según el estado
                        val displayedCharacters = if (showAll || searchQuery.isNotBlank()) {
                            filteredCharacters
                        } else {
                            filteredCharacters.take(9)
                        }

                        Column(
                            modifier = Modifier.imePadding(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            displayedCharacters.forEach { character ->
                                CharacterListItem(
                                    character = character,
                                    onClick = {
                                        character.idCharacter?.let { id ->
                                            navController.navigate("${AppDestinations.CHARACTER_DETAIL_ROUTE}/$id")
                                        }
                                    }
                                )
                                if (character != displayedCharacters.last()) {
                                    HorizontalDivider(
                                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                                    )
                                }
                            }

                            // Botón "Ver todo el reparto" / "Ver menos" si hay más de 9 personajes y no está buscando
                            if (filteredCharacters.size > 9 && searchQuery.isBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedButton(
                                    onClick = { showAll = !showAll },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = if (showAll) "Ver menos" else "Ver todo el reparto",
                                        fontFamily = PoppinsMedium,
                                        fontSize = 14.sp
                                    )
                                }
                            }

                            // Spacer adicional para que el contenido no quede tapado por el teclado
                            Spacer(modifier = Modifier.height(200.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CharacterListItem(
    character: AnimeCharactersDetail,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val characterName = character.nameCharacter ?: "Desconocido"
    val characterRole = when (character.role) {
        "Main" -> "Protagonista"
        "Supporting" -> "Principal"
        else -> character.role
    }
    val characterImage = character.imageCharacter?.jpg?.imageUrl

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar e información del personaje (izquierda)
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            // Avatar circular con imagen o inicial
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                contentAlignment = Alignment.Center
            ) {
                if (!characterImage.isNullOrBlank()) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(characterImage)
                            .size(Size.ORIGINAL)
                            .crossfade(true)
                            .build(),
                        contentDescription = characterName,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = characterName.first().uppercase(),
                        fontFamily = PoppinsBold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Nombre y rol
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = characterName,
                    fontFamily = PoppinsBold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = characterRole,
                    fontFamily = PoppinsRegular,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Información del actor de voz (derecha) - placeholder por ahora
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Actor de voz (placeholder)
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "Actor de voz",
                    fontFamily = PoppinsBold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Japonés",
                    fontFamily = PoppinsRegular,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Avatar del actor con inicial (placeholder)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "A",
                    fontFamily = PoppinsBold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

// ============================================================================
// COMPONENTS
// ============================================================================

@Composable
private fun AnimeInfoCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                fontFamily = PoppinsBold,
                fontSize = 21.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                letterSpacing = 0.3.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            content()
        }
    }
}

@Composable
private fun AnimeGenreChip(genre: String) {
    FilterChip(
        selected = false,
        onClick = { },
        label = {
            Text(
                text = genre,
                fontFamily = PoppinsMedium,
                fontSize = 12.sp
            )
        },
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
private fun AnimeDemographicChip(demographic: String) {
    FilterChip(
        selected = true,
        onClick = { },
        label = {
            Text(
                text = demographic,
                fontFamily = PoppinsBold,
                fontSize = 12.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.People,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        },
        shape = RoundedCornerShape(8.dp),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Composable
private fun AnimeInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val gradientColors = listOf(
        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
        MaterialTheme.colorScheme.surfaceContainerHigh
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = gradientColors,
                        startX = 0f,
                        endX = 400f // El gradiente termina rápido, antes del texto
                    )
                )
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Label - lado izquierdo
                Text(
                    text = label,
                    fontFamily = PoppinsMedium,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f, fill = false)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Value - lado derecho con énfasis
                Text(
                    text = value,
                    fontFamily = PoppinsBold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.End,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
            }
        }
    }
}

// Badges tipo pill - diseño redondeado y moderno
@Composable
private fun AnimeTypeBadgePDF(type: String) {
    if (type.isBlank()) return

    // Colores según el tipo
    val color = when (type) {
        "TV" -> Color(0xFF1E88E5)
        "MOVIE" -> Color(0xFF9C27B0)
        "OVA" -> Color(0xFF00BCD4)
        "ONA" -> Color(0xFF009688)
        "SPECIAL" -> Color(0xFFFF9800)
        "MUSIC" -> Color(0xFFE91E63)
        else -> Color(0xFF1E88E5)
    }

    Surface(
        shape = RoundedCornerShape(50), // Completamente redondeado (pill)
        color = color.copy(alpha = 0.2f),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.5.dp,
            color = color.copy(alpha = 0.5f)
        )
    ) {
        Text(
            text = formatAnimeType(type),
            fontFamily = PoppinsBold,
            fontSize = 11.sp,
            color = color,
            letterSpacing = 0.3.sp,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun AnimeStatusBadgePDF(status: String) {
    if (status.isBlank()) return

    val (text, color) = when (status) {
        "FINISHED" -> "Finalizado" to Color(0xFF7EE787)
        "RELEASING" -> "En emisión" to Color(0xFF00A8FF)
        "NOT_YET_RELEASED" -> "Próximamente" to Color(0xFF79C0FF)
        "CANCELLED" -> "Cancelado" to Color(0xFFFF7B72)
        "HIATUS" -> "En pausa" to Color(0xFFFF9800)
        else -> status to Color(0xFF757575)
    }

    Surface(
        shape = RoundedCornerShape(50), // Completamente redondeado (pill)
        color = color.copy(alpha = 0.2f),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.5.dp,
            color = color.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Dot indicator
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Text(
                text = text,
                fontFamily = PoppinsBold,
                fontSize = 11.sp,
                color = color,
                letterSpacing = 0.3.sp
            )
        }
    }
}

@Composable
private fun AnimeBroadcastBadge(broadcast: String) {
    if (broadcast.isBlank()) return

    val color = MaterialTheme.colorScheme.primary

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.12f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = color
            )
            Text(
                text = broadcast,
                fontFamily = PoppinsBold,
                fontSize = 11.sp,
                color = color,
                letterSpacing = 0.3.sp
            )
        }
    }
}

@Composable
private fun AnimeEmptyState(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = message,
                fontFamily = PoppinsMedium,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun AnimeErrorState(
    message: String,
    onRetry: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(64.dp)
            )

            Text(
                text = "Error al cargar",
                fontFamily = PoppinsBold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = message,
                fontFamily = PoppinsRegular,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = onBack) {
                    Text("Volver", fontFamily = PoppinsMedium)
                }
                Button(onClick = onRetry) {
                    Icon(Icons.Default.Refresh, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Reintentar", fontFamily = PoppinsBold)
                }
            }
        }
    }
}

// ============================================================================
// HELPERS
// ============================================================================

private fun formatAnimeType(type: String): String = when (type) {
    "TV" -> "TV"
    "MOVIE" -> "Película"
    "OVA" -> "OVA"
    "ONA" -> "ONA"
    "SPECIAL" -> "Especial"
    "MUSIC" -> "Música"
    else -> type
}

private fun formatAnimeStatus(status: String): String = when (status) {
    "FINISHED" -> "Finalizado"
    "RELEASING" -> "En emisión"
    "NOT_YET_RELEASED" -> "Próximamente"
    "CANCELLED" -> "Cancelado"
    "HIATUS" -> "En pausa"
    else -> status
}

private fun formatAnimeSource(source: String): String = when (source) {
    "Manga" -> "Manga"
    "Light novel" -> "Light Novel"
    "Visual novel" -> "Visual Novel"
    "Original" -> "Original"
    "Game" -> "Videojuego"
    "Novel" -> "Novela"
    "Web manga" -> "Web Manga"
    "4-koma manga" -> "4-koma"
    else -> source
}

private fun formatNumber(number: Int): String {
    return when {
        number >= 1_000_000 -> String.format("%.1fM", number / 1_000_000.0)
        number >= 1_000 -> String.format("%.1fK", number / 1_000.0)
        else -> number.toString()
    }
}

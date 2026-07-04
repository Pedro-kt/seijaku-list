package com.yumedev.seijakulist.ui.screens.detail

import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
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
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null
) {
    // ViewModels state
    val animeDetail by animeDetailViewModel.animeDetail.collectAsState()
    val isLoading by animeDetailViewModel.isLoading.collectAsState()
    val errorMessage by animeDetailViewModel.errorMessage.collectAsState()

    val isAdded by animeDetailViewModel.isAdded.collectAsState()
    val existingAnime by animeDetailViewModel.existingAnime.collectAsState()

    // UI State
    var selectedTab by remember { mutableStateOf(AnimeDetailTab.OVERVIEW) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val listState = rememberLazyListState()
    val context = LocalContext.current
    val view = androidx.compose.ui.platform.LocalView.current

    // Configurar status bar y navigation bar para esta pantalla
    DisposableEffect(Unit) {
        val window = (context as? android.app.Activity)?.window
        val originalStatusBarColor = window?.statusBarColor
        val originalNavigationBarColor = window?.navigationBarColor

        window?.let {
            WindowCompat.setDecorFitsSystemWindows(it, false)
            it.statusBarColor = android.graphics.Color.TRANSPARENT
            it.navigationBarColor = android.graphics.Color.TRANSPARENT
        }

        onDispose {
            // Restaurar el estado original al salir
            window?.let {
                WindowCompat.setDecorFitsSystemWindows(it, true)
                originalStatusBarColor?.let { color ->
                    it.statusBarColor = color
                }
                originalNavigationBarColor?.let { color ->
                    it.navigationBarColor = color
                }
            }
        }
    }

    // FAB visibility based on scroll
    val showFab by remember {
        derivedStateOf { listState.firstVisibleItemIndex >= 1 }
    }

    // Handle open sheet parameter
    LaunchedEffect(openSheet, isLoading, animeDetail) {
        if (openSheet && !isLoading && animeDetail != null) {
            navController.navigate("${AppDestinations.ADD_TO_LIST_ROUTE}/${animeDetail!!.malId}")
        }
    }

    // Set initial tab if provided
    LaunchedEffect(initialTab) {
        selectedTab = when (initialTab) {
            1 -> AnimeDetailTab.PRODUCTION
            2 -> AnimeDetailTab.TRACKING
            else -> AnimeDetailTab.OVERVIEW
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
                                    animeDetail?.let {
                                        navController.navigate("${AppDestinations.ADD_TO_LIST_ROUTE}/${it.malId}")
                                    }
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
                                    animeDetail?.let {
                                        navController.navigate("${AppDestinations.ADD_TO_LIST_ROUTE}/${it.malId}")
                                    }
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
                        // BANNER CON BLUR + HEADER ENCIMA (scrollea con el contenido)
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
                                                .height(statusBarHeight + 280.dp)
                                        ) {
                                            // Banner image con blur
                                            AsyncImage(
                                                model = banner,
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .blur(20.dp),
                                                contentScale = ContentScale.Crop
                                            )

                                            // Gradient overlay (oscuro gradual + transición al background)
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .background(
                                                        Brush.verticalGradient(
                                                            colors = listOf(
                                                                Color.Black.copy(alpha = 0.2f),
                                                                Color.Black.copy(alpha = 0.3f),
                                                                Color.Black.copy(alpha = 0.4f),
                                                                Color.Black.copy(alpha = 0.5f),
                                                                MaterialTheme.colorScheme.background.copy(alpha = 0.7f),
                                                                MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
                                                                MaterialTheme.colorScheme.background
                                                            )
                                                        )
                                                    )
                                            )
                                        }
                                    }
                                }

                                // Header con portada e información principal (ENCIMA del banner)
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = statusBarHeight + 56.dp) // Espacio para status bar + botón atrás
                                ) {
                                    AnimeDetailHeader(
                                        animeDetail = animeDetail,
                                        isAdded = isAdded,
                                        onAddToListClick = {
                                            animeDetail?.let {
                                                navController.navigate("${AppDestinations.ADD_TO_LIST_ROUTE}/${it.malId}")
                                            }
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
                                    AnimeDetailTab.PRODUCTION -> {
                                        AnimeProductionTab(animeDetail = animeDetail)
                                    }
                                    AnimeDetailTab.TRACKING -> {
                                        // Tab de seguimiento - solo visible cuando está en lista
                                        if (isAdded && existingAnime != null) {
                                            AnimeTrackingTab(anime = existingAnime)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // TOP BAR CON BOTÓN DE ATRÁS (transparente, sobre el banner)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(statusBarHeight + 56.dp)
                            .align(Alignment.TopCenter)
                    ) {
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
                    }
                }
            }
        }
    }
}

// ============================================================================
// TABS
// ============================================================================

enum class AnimeDetailTab {
    OVERVIEW,
    PRODUCTION,
    TRACKING
}

@Composable
private fun AnimeDetailTabSelector(
    selectedTab: AnimeDetailTab,
    onTabSelected: (AnimeDetailTab) -> Unit,
    isAdded: Boolean,
    modifier: Modifier = Modifier
) {
    // Tabs dinámicos según si está en lista o no
    val tabs = if (isAdded) {
        // Estado B: En lista - 3 tabs (Resumen, Producción, Seguimiento)
        listOf(
            AnimeDetailTab.OVERVIEW to Icons.Default.Description,
            AnimeDetailTab.PRODUCTION to Icons.Default.Business,
            AnimeDetailTab.TRACKING to Icons.Default.Assignment
        )
    } else {
        // Estado A: No en lista - 2 tabs (Resumen, Producción)
        listOf(
            AnimeDetailTab.OVERVIEW to Icons.Default.Description,
            AnimeDetailTab.PRODUCTION to Icons.Default.Business
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            shadowElevation = 0.dp
        ) {
            Row(modifier = Modifier.padding(6.dp)) {
                tabs.forEach { (tab, icon) ->
                    AnimeTabItem(
                        icon = icon,
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
    icon: ImageVector,
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
    val iconColor by animateColorAsState(
        targetValue = if (isSelected)
            MaterialTheme.colorScheme.onPrimary
        else
            MaterialTheme.colorScheme.onSurfaceVariant,
        label = "Tab Icon Color",
        animationSpec = tween(200)
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = iconColor
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

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // SECCIÓN PRINCIPAL: Portada + Info
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // PORTADA
            Card(
                modifier = Modifier
                    .width(100.dp)
                    .height(145.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
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

            // INFORMACIÓN PRINCIPAL
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(145.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // TÍTULOS Y BADGES
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Título principal
                    Text(
                        text = animeDetail?.title ?: "",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp,
                        fontFamily = PoppinsBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 20.sp
                    )

                    // Título japonés (más pequeño y sutil)
                    if (!animeDetail?.titleJapanese.isNullOrBlank()) {
                        Text(
                            text = animeDetail.titleJapanese,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            fontSize = 11.sp,
                            fontFamily = PoppinsRegular,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Type y Status badges (estilo PDF: radio 8dp, 15% fondo, 45% borde)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Type badge
                        AnimeTypeBadgePDF(type = animeDetail?.typeAnime ?: "")

                        // Status badge
                        AnimeStatusBadgePDF(status = animeDetail?.status ?: "")
                    }
                }

                // BOTÓN "Añadir" o "Editar" (sin espaciado excesivo)
                Button(
                    onClick = onAddToListClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = if (isAdded) Icons.Default.Edit else Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isAdded) "Editar" else "Añadir",
                        fontSize = 13.sp,
                        fontFamily = PoppinsBold
                    )
                }
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

        // Descripción
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Toca para ver en YouTube",
                fontFamily = PoppinsRegular,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Botón secundario
            TextButton(
                onClick = {
                    if (youtubeId != null) {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://www.youtube.com/watch?v=$youtubeId")
                        )
                        context.startActivity(intent)
                    }
                },
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.OpenInNew,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Abrir",
                    fontSize = 11.sp,
                    fontFamily = PoppinsMedium
                )
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
    // Lista de estadísticas con su índice para animaciones escalonadas
    val stats = remember(animeDetail) {
        buildList {
            animeDetail?.let {
                if (it.score != null && it.score > 0) {
                    add(
                        StatData(
                            icon = Icons.Default.Star,
                            value = String.format("%.1f", it.score),
                            label = "Score",
                            type = StatType.SCORE
                        )
                    )
                }
                if (it.episodes != null && it.episodes > 0) {
                    add(
                        StatData(
                            icon = Icons.Default.Tv,
                            value = "${it.episodes}",
                            label = "Episodios",
                            type = StatType.EPISODES
                        )
                    )
                }
                if (it.rank != null && it.rank > 0) {
                    add(
                        StatData(
                            icon = Icons.Default.EmojiEvents,
                            value = "#${it.rank}",
                            label = "Ranking",
                            type = StatType.RANK
                        )
                    )
                }
                if (it.scoreBy != null && it.scoreBy > 0) {
                    add(
                        StatData(
                            icon = Icons.Default.People,
                            value = formatNumber(it.scoreBy),
                            label = "Usuarios",
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
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
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

// Stats badges según el PDF - diseño compacto y limpio
@Composable
private fun AnimeStatCard(
    statData: StatData,
    index: Int,
    statsCount: Int,
    modifier: Modifier = Modifier
) {
    // Color de acento según el tipo
    val accentColor = when (statData.type) {
        StatType.SCORE -> Color(0xFF1E88E5) // Primary
        StatType.EPISODES -> Color(0xFF7EE787) // Tertiary
        StatType.RANK -> Color(0xFF79C0FF) // Secondary
        StatType.USERS -> Color(0xFF1E88E5)
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            // Label arriba (pequeño, en mayúsculas, color de acento)
            Text(
                text = statData.label.uppercase(),
                fontFamily = PoppinsBold,
                fontSize = 9.sp,
                color = accentColor.copy(alpha = 0.8f),
                letterSpacing = 0.5.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            // Valor grande abajo
            Text(
                text = statData.value,
                fontFamily = PoppinsBold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface,
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

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .animateContentSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Header con título y botones de acción
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Sinopsis",
                            fontSize = 21.sp,
                            fontFamily = PoppinsBold,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            letterSpacing = 0.3.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        // Botones de acción
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Botón de copiar
                            FilledTonalIconButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(animeDetail.synopsis))
                                    Toast.makeText(
                                        context,
                                        "Sinopsis copiada al portapapeles",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copiar sinopsis",
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Botón de traducir
                            FilledTonalIconButton(
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
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Translate,
                                    contentDescription = "Traducir sinopsis",
                                    modifier = Modifier.size(18.dp)
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
                        lineHeight = 23.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = if (expanded) TextAlign.Justify else TextAlign.Start,
                        maxLines = if (expanded) Int.MAX_VALUE else 6,
                        overflow = TextOverflow.Ellipsis,
                        onTextLayout = { textLayoutResult = it }
                    )

                    if (hasOverflow || expanded) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            FilledTonalButton(
                                onClick = { expanded = !expanded },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = if (expanded)
                                        Icons.Default.ExpandLess
                                    else
                                        Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (expanded) "Ver menos" else "Ver más",
                                    fontFamily = PoppinsBold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Genres
        if (!animeDetail?.genres.isNullOrEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Géneros",
                        fontFamily = PoppinsBold,
                        fontSize = 21.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        letterSpacing = 0.3.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
                    )

                    LazyRow(
                        modifier = Modifier.height(55.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(animeDetail.genres.filterNotNull()) { genre ->
                            CompactGenreCard(genreName = genre.name ?: "")
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }

        // Demographics
        if (!animeDetail?.demographics.isNullOrEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Demografía",
                        fontFamily = PoppinsBold,
                        fontSize = 21.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        letterSpacing = 0.3.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
                    )

                    LazyRow(
                        modifier = Modifier.height(55.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(animeDetail.demographics.filterNotNull()) { demographic ->
                            CompactDemographicCard(demographicName = demographic.name ?: "")
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }

        // Background
        if (!animeDetail?.background.isNullOrBlank()) {
            AnimeInfoCard(title = "Contexto") {
                var expanded by remember { mutableStateOf(false) }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = animeDetail.background,
                        fontFamily = PoppinsRegular,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = if (expanded) Int.MAX_VALUE else 4,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (animeDetail.background.length > 150) {
                        TextButton(
                            onClick = { expanded = !expanded }
                        ) {
                            Text(
                                text = if (expanded) "Menos" else "Más",
                                fontFamily = PoppinsMedium,
                                fontSize = 12.sp
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
private fun AnimeProductionTab(
    animeDetail: AnimeDetail?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Basic info
        AnimeInfoRow("Título en inglés", animeDetail?.titleEnglish?.ifBlank { "—" } ?: "—")
        AnimeInfoRow("Título japonés", animeDetail?.titleJapanese?.ifBlank { "—" } ?: "—")
        AnimeInfoRow("Tipo", formatAnimeType(animeDetail?.typeAnime ?: ""))
        AnimeInfoRow("Estado", formatAnimeStatus(animeDetail?.status ?: ""))

        if (animeDetail?.episodes != null && animeDetail.episodes > 0) {
            AnimeInfoRow("Episodios", "${animeDetail.episodes}")
        }

        if (!animeDetail?.aired.isNullOrBlank()) {
            AnimeInfoRow("Emisión", animeDetail.aired)
        }

        if (!animeDetail?.duration.isNullOrBlank()) {
            AnimeInfoRow("Duración", animeDetail.duration)
        }

        if (!animeDetail?.rating.isNullOrBlank()) {
            AnimeInfoRow("Clasificación", animeDetail.rating)
        }

        if (!animeDetail?.source.isNullOrBlank()) {
            AnimeInfoRow("Fuente", formatAnimeSource(animeDetail.source))
        }

        if (animeDetail?.score != null && animeDetail.score > 0) {
            AnimeInfoRow("Puntuación", "${animeDetail.score}/10")
        }

        if (animeDetail?.scoreBy != null && animeDetail.scoreBy > 0) {
            AnimeInfoRow("Valoraciones", "${animeDetail.scoreBy} usuarios")
        }

        // Studios - en formato pill con colores
        if (!animeDetail?.studios.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            AnimeInfoCard(title = "Estudios") {
                // Paleta de colores para los chips
                val chipColors = listOf(
                    Color(0xFF2196F3), // Azul
                    Color(0xFF9C27B0), // Púrpura
                    Color(0xFF00BCD4), // Cian
                    Color(0xFFFF5722), // Naranja rojizo
                    Color(0xFF4CAF50), // Verde
                    Color(0xFFFF9800), // Naranja
                    Color(0xFFE91E63), // Rosa
                    Color(0xFF009688)  // Teal
                )

                androidx.compose.foundation.layout.FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    animeDetail.studios.filterNotNull()
                        .filter { !it.nameStudio.isNullOrBlank() }
                        .forEachIndexed { index, studio ->
                            val chipColor = chipColors[index % chipColors.size]

                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = chipColor.copy(alpha = 0.15f),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    chipColor.copy(alpha = 0.5f)
                                )
                            ) {
                                Text(
                                    text = studio.nameStudio ?: "",
                                    fontFamily = PoppinsBold,
                                    fontSize = 12.sp,
                                    color = chipColor,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                                )
                            }
                        }
                }
            }
        }

        // Producers - en formato pill con colores
        if (!animeDetail?.producers.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            AnimeInfoCard(title = "Productores") {
                // Paleta de colores para los chips
                val chipColors = listOf(
                    Color(0xFF2196F3), // Azul
                    Color(0xFF9C27B0), // Púrpura
                    Color(0xFF00BCD4), // Cian
                    Color(0xFFFF5722), // Naranja rojizo
                    Color(0xFF4CAF50), // Verde
                    Color(0xFFFF9800), // Naranja
                    Color(0xFFE91E63), // Rosa
                    Color(0xFF009688)  // Teal
                )

                androidx.compose.foundation.layout.FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    animeDetail.producers.filterNotNull()
                        .filter { !it.name.isNullOrBlank() }
                        .forEachIndexed { index, producer ->
                            val chipColor = chipColors[index % chipColors.size]

                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = chipColor.copy(alpha = 0.15f),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    chipColor.copy(alpha = 0.5f)
                                )
                            ) {
                                Text(
                                    text = producer.name ?: "",
                                    fontFamily = PoppinsBold,
                                    fontSize = 12.sp,
                                    color = chipColor,
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
private fun AnimeTrackingTab(
    anime: AnimeEntityDomain?,
    modifier: Modifier = Modifier
) {
    val sdf = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            .apply { timeZone = java.util.TimeZone.getTimeZone("UTC") }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Fechas de seguimiento
        AnimeInfoCard(title = "Seguimiento") {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Fecha de inicio
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "Inicio",
                                fontSize = 12.sp,
                                fontFamily = PoppinsRegular,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = anime?.startDate?.let { sdf.format(it) } ?: "Sin fecha",
                                fontSize = 14.sp,
                                fontFamily = PoppinsBold,
                                color = if (anime?.startDate != null)
                                    MaterialTheme.colorScheme.onSurface
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                        }
                    }
                }

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                )

                // Fecha de finalización
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "Finalización",
                                fontSize = 12.sp,
                                fontFamily = PoppinsRegular,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = anime?.endDate?.let { sdf.format(it) } ?: "Sin fecha",
                                fontSize = 14.sp,
                                fontFamily = PoppinsBold,
                                color = if (anime?.endDate != null)
                                    MaterialTheme.colorScheme.onSurface
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }
        }

        // Prioridad del plan (solo si está Planeado)
        if (anime?.userStatus == "Planeado") {
            AnimeInfoCard(title = "Prioridad del plan") {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Prioridad actual
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Prioridad",
                            fontSize = 14.sp,
                            fontFamily = PoppinsRegular,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        val priorityColor = when (anime.plannedPriority) {
                            "Alta" -> Color(0xFFEF5350)
                            "Media" -> Color(0xFFFFCA28)
                            "Baja" -> Color(0xFF66BB6A)
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (anime.plannedPriority != null)
                                priorityColor.copy(alpha = 0.15f)
                            else
                                MaterialTheme.colorScheme.surfaceContainerHighest
                        ) {
                            Text(
                                text = anime.plannedPriority ?: "Sin definir",
                                fontSize = 14.sp,
                                fontFamily = PoppinsBold,
                                color = if (anime.plannedPriority != null) priorityColor
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Nota del plan
                    if (!anime.plannedNote.isNullOrBlank()) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                        )
                        Text(
                            text = "Nota",
                            fontSize = 14.sp,
                            fontFamily = PoppinsRegular,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = anime.plannedNote,
                            fontSize = 14.sp,
                            fontFamily = PoppinsRegular,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Mi Reseña
        if (!anime?.userOpiniun.isNullOrEmpty()) {
            AnimeInfoCard(title = "Mi Reseña") {
                Text(
                    text = anime.userOpiniun,
                    textAlign = TextAlign.Justify,
                    fontSize = 14.sp,
                    fontFamily = PoppinsRegular,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
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

// Badges con estilo del PDF: radio 8dp, color al 15% fondo, 45% borde
@Composable
private fun AnimeTypeBadgePDF(type: String) {
    if (type.isBlank()) return

    // Colores según el PDF
    val color = when (type) {
        "TV" -> Color(0xFF1E88E5) // Primary
        "MOVIE" -> Color(0xFF9C27B0)
        "OVA" -> Color(0xFF00BCD4)
        "ONA" -> Color(0xFF009688)
        "SPECIAL" -> Color(0xFFFF9800)
        "MUSIC" -> Color(0xFFE91E63)
        else -> Color(0xFF1E88E5)
    }

    Surface(
        shape = RoundedCornerShape(8.dp), // Radio 8dp según PDF
        color = color.copy(alpha = 0.15f), // 15% fondo
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = color.copy(alpha = 0.45f) // 45% borde
        )
    ) {
        Text(
            text = formatAnimeType(type),
            fontFamily = PoppinsBold,
            fontSize = 10.sp,
            color = color,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun AnimeStatusBadgePDF(status: String) {
    if (status.isBlank()) return

    val (text, color) = when (status) {
        "FINISHED" -> "Finalizado" to Color(0xFF7EE787) // Tertiary del PDF
        "RELEASING" -> "En emisión" to Color(0xFF00A8FF) // Viendo del PDF
        "NOT_YET_RELEASED" -> "Próximamente" to Color(0xFF79C0FF) // Secondary del PDF
        "CANCELLED" -> "Cancelado" to Color(0xFFFF7B72) // Error del PDF
        "HIATUS" -> "En pausa" to Color(0xFFFF9800)
        else -> status to Color(0xFF757575)
    }

    Surface(
        shape = RoundedCornerShape(8.dp), // Radio 8dp según PDF
        color = color.copy(alpha = 0.15f), // 15% fondo
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = color.copy(alpha = 0.45f) // 45% borde
        )
    ) {
        Text(
            text = text,
            fontFamily = PoppinsBold,
            fontSize = 10.sp,
            color = color,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
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

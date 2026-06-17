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
import com.yumedev.seijakulist.domain.models.AnimeCharactersDetail
import com.yumedev.seijakulist.domain.models.AnimeDetail
import com.yumedev.seijakulist.ui.components.LoadingScreen
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
    animeCharacterDetailViewModel: AnimeCharacterDetailViewModel = hiltViewModel(),
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null
) {
    // ViewModels state
    val animeDetail by animeDetailViewModel.animeDetail.collectAsState()
    val isLoading by animeDetailViewModel.isLoading.collectAsState()
    val errorMessage by animeDetailViewModel.errorMessage.collectAsState()

    val animeCharacters by animeCharacterDetailViewModel.characters.collectAsState()
    val charactersLoading by animeCharacterDetailViewModel.isLoading.collectAsState()

    val isAdded by animeDetailViewModel.isAdded.collectAsState()
    val existingAnime by animeDetailViewModel.existingAnime.collectAsState()

    // UI State
    var selectedTab by remember { mutableStateOf(AnimeDetailTab.OVERVIEW) }
    var showAddToListSheet by remember { mutableStateOf(false) }
    val addToListSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
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
    LaunchedEffect(openSheet, isLoading) {
        if (openSheet && !isLoading && animeDetail != null) {
            showAddToListSheet = true
        }
    }

    // Set initial tab if provided
    LaunchedEffect(initialTab) {
        selectedTab = when (initialTab) {
            1 -> AnimeDetailTab.CHARACTERS
            2 -> AnimeDetailTab.PRODUCTION
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
                        ExtendedFloatingActionButton(
                            onClick = { showAddToListSheet = true },
                            icon = {
                                Icon(
                                    imageVector = if (isAdded) Icons.Default.Edit else Icons.Default.Favorite,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp) // Icono más grande
                                )
                            },
                            text = {
                                Text(
                                    text = if (isAdded) "Editar en lista" else "Añadir a lista",
                                    fontFamily = PoppinsBold,
                                    fontSize = 16.sp, // Texto más grande
                                    letterSpacing = 0.2.sp
                                )
                            },
                            containerColor = if (isAdded)
                                MaterialTheme.colorScheme.secondaryContainer
                            else
                                MaterialTheme.colorScheme.primary,
                            contentColor = if (isAdded)
                                MaterialTheme.colorScheme.onSecondaryContainer
                            else
                                MaterialTheme.colorScheme.onPrimary,
                            shape = RoundedCornerShape(20.dp), // Más redondeado
                            elevation = FloatingActionButtonDefaults.elevation(
                                defaultElevation = 8.dp, // Mayor elevación
                                pressedElevation = 12.dp,
                                hoveredElevation = 10.dp
                            ),
                            modifier = Modifier
                                .height(60.dp) // Más alto
                                .padding(bottom = 8.dp)
                        )
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
                                        onAddToListClick = { showAddToListSheet = true },
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
                                onTabSelected = { selectedTab = it }
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
                                    AnimeDetailTab.CHARACTERS -> {
                                        LaunchedEffect(Unit) {
                                            if (animeCharacters.isEmpty() && !charactersLoading) {
                                                animeId?.let {
                                                    animeCharacterDetailViewModel.loadAnimeCharacters(it)
                                                }
                                            }
                                        }
                                        AnimeCharactersTab(
                                            characters = animeCharacters,
                                            isLoading = charactersLoading,
                                            navController = navController
                                        )
                                    }
                                    AnimeDetailTab.PRODUCTION -> {
                                        AnimeProductionTab(animeDetail = animeDetail)
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

            // Bottom sheet para añadir/editar en lista
            if (showAddToListSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showAddToListSheet = false },
                    sheetState = addToListSheetState
                ) {
                    AddToListBottomSheetContent(
                        anime = animeDetail,
                        existingAnime = existingAnime,
                        isAdded = isAdded,
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
                                showAddToListSheet = false

                                val message = if (isAdded) "Anime actualizado" else "Anime añadido a tu lista"
                                snackbarHostState.showSnackbar(message)
                            }
                        },
                        onDelete = {
                            scope.launch {
                                // TODO: Implement delete method
                                showAddToListSheet = false
                                snackbarHostState.showSnackbar("Anime eliminado de tu lista")
                            }
                        },
                        onDismiss = { showAddToListSheet = false }
                    )
                }
            }
        }
    }
}

// ============================================================================
// BOTTOM SHEET (TODO: Implementar UI completa)
// ============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddToListBottomSheetContent(
    anime: AnimeDetail?,
    existingAnime: com.yumedev.seijakulist.domain.models.AnimeEntityDomain?,
    isAdded: Boolean,
    onSave: (String?, Float, Long?, Long?, String?, String) -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    // Estado del sheet
    var sheetStatus by remember { mutableStateOf(existingAnime?.userStatus) }
    var sheetRating by remember { mutableStateOf(existingAnime?.userScore ?: 0f) }
    var sheetOpinion by remember { mutableStateOf(existingAnime?.userOpiniun ?: "") }
    var sheetStartDate by remember { mutableStateOf(existingAnime?.startDate) }
    var sheetEndDate by remember { mutableStateOf(existingAnime?.endDate) }
    var sheetPlannedPriority by remember { mutableStateOf(existingAnime?.plannedPriority) }
    var sheetPlannedNote by remember { mutableStateOf(existingAnime?.plannedNote ?: "") }

    var sheetShowStartPicker by remember { mutableStateOf(false) }
    var sheetShowEndPicker by remember { mutableStateOf(false) }

    val statusList = listOf(
        "Viendo" to Icons.Default.PlayArrow,
        "Completado" to Icons.Default.CheckCircle,
        "Pendiente" to Icons.Default.WatchLater,
        "Abandonado" to Icons.Default.Close,
        "Planeado" to Icons.Default.EventNote
    )
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    // DatePicker Dialogs
    if (sheetShowStartPicker) {
        DatePickerDialog(
            onDismissRequest = { sheetShowStartPicker = false },
            confirmButton = {
                TextButton(onClick = { sheetShowStartPicker = false }) {
                    Text("OK", fontFamily = PoppinsMedium)
                }
            },
            dismissButton = {
                TextButton(onClick = { sheetShowStartPicker = false }) {
                    Text("Cancelar", fontFamily = PoppinsRegular)
                }
            }
        ) {
            val pickerState = rememberDatePickerState(initialSelectedDateMillis = sheetStartDate)
            DatePicker(state = pickerState)
            sheetStartDate = pickerState.selectedDateMillis
        }
    }

    if (sheetShowEndPicker) {
        DatePickerDialog(
            onDismissRequest = { sheetShowEndPicker = false },
            confirmButton = {
                TextButton(onClick = { sheetShowEndPicker = false }) {
                    Text("OK", fontFamily = PoppinsMedium)
                }
            },
            dismissButton = {
                TextButton(onClick = { sheetShowEndPicker = false }) {
                    Text("Cancelar", fontFamily = PoppinsRegular)
                }
            }
        ) {
            val pickerState = rememberDatePickerState(initialSelectedDateMillis = sheetEndDate)
            DatePicker(state = pickerState)
            sheetEndDate = pickerState.selectedDateMillis
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Contenido scrolleable
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
        // Estado - Card con grid compacto y con iconos
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "¿Cuál es el estado?",
                        fontFamily = PoppinsBold,
                        fontSize = 21.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        letterSpacing = 0.3.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.height(200.adp()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(statusList) { (status, icon) ->
                        val isSelected = sheetStatus == status
                        val statusColor = getAnimeStatusColor(status)

                        Card(
                            onClick = { sheetStatus = if (isSelected) null else status },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) statusColor.copy(alpha = 0.15f)
                                else MaterialTheme.colorScheme.surfaceContainerHighest
                            ),
                            modifier = Modifier.height(60.adp()),
                            shape = RoundedCornerShape(14.dp),
                            border = if (isSelected)
                                androidx.compose.foundation.BorderStroke(2.dp, statusColor)
                            else null,
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = if (isSelected) 4.dp else 1.dp
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(22.adp()),
                                    tint = if (isSelected) statusColor else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    status,
                                    fontSize = 12.asp(),
                                    fontFamily = if (isSelected) PoppinsBold else PoppinsRegular,
                                    color = if (isSelected) statusColor else MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }

        // Prioridad y Nota (solo si Planeado) - Card
        AnimatedVisibility(
            visible = sheetStatus == "Planeado",
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Prioridad del plan",
                            fontFamily = PoppinsBold,
                            fontSize = 21.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            letterSpacing = 0.3.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    val priorities = listOf(
                        "Alta" to Color(0xFFEF5350),
                        "Media" to Color(0xFFFFCA28),
                        "Baja" to Color(0xFF66BB6A)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        priorities.forEach { (priority, color) ->
                            val isSel = sheetPlannedPriority == priority
                            Card(
                                onClick = { sheetPlannedPriority = if (isSel) null else priority },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.adp()),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSel) color.copy(alpha = 0.2f)
                                                   else MaterialTheme.colorScheme.surfaceContainerHighest
                                ),
                                border = if (isSel) androidx.compose.foundation.BorderStroke(2.dp, color) else null,
                                elevation = CardDefaults.cardElevation(defaultElevation = if (isSel) 3.dp else 1.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        priority,
                                        fontSize = 14.asp(),
                                        fontFamily = if (isSel) PoppinsBold else PoppinsRegular,
                                        color = if (isSel) color else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = sheetPlannedNote,
                        onValueChange = { sheetPlannedNote = it },
                        placeholder = {
                            Text(
                                "¿Por qué lo tenés planeado?",
                                fontFamily = PoppinsRegular,
                                fontSize = 13.asp()
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        maxLines = 3,
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontFamily = PoppinsRegular,
                            fontSize = 14.asp()
                        )
                    )
                }
            }
        }

        // Calificación (solo si no es Planeado) - Card
        AnimatedVisibility(
            visible = sheetStatus != null && sheetStatus != "Planeado",
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Tu calificación",
                                fontFamily = PoppinsBold,
                                fontSize = 21.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                letterSpacing = 0.3.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        if (sheetRating > 0) {
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color(0xFFFFD700).copy(alpha = 0.15f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        String.format("%.1f", sheetRating),
                                        fontSize = 16.asp(),
                                        fontFamily = PoppinsBold,
                                        color = Color(0xFFFFD700)
                                    )
                                    Text(
                                        "/ 10",
                                        fontSize = 12.asp(),
                                        fontFamily = PoppinsRegular,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    RatingBar(
                        rating = sheetRating,
                        onRatingChange = { sheetRating = it },
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }

        // Opinión - Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Tu opinión (opcional)",
                        fontFamily = PoppinsBold,
                        fontSize = 21.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        letterSpacing = 0.3.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                OutlinedTextField(
                    value = sheetOpinion,
                    onValueChange = { sheetOpinion = it },
                    placeholder = {
                        Text(
                            "Comparte qué te pareció este anime...",
                            fontFamily = PoppinsRegular,
                            fontSize = 13.asp()
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.adp()),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 4,
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontFamily = PoppinsRegular,
                        fontSize = 14.asp()
                    )
                )
            }
        }

        // Fechas - Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Fechas (opcional)",
                        fontFamily = PoppinsBold,
                        fontSize = 21.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        letterSpacing = 0.3.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                val canStart = sheetStatus != "Planeado"
                val canEnd = sheetStatus == "Completado"

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Fecha inicio
                    Card(
                        onClick = { if (canStart) sheetShowStartPicker = true },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (canStart) MaterialTheme.colorScheme.surface
                                           else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (canStart) 1.dp else 0.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(20.adp()),
                                tint = if (canStart) MaterialTheme.colorScheme.primary
                                      else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                            )
                            Text(
                                sheetStartDate?.let { dateFormat.format(it) } ?: "Inicio",
                                fontSize = 13.asp(),
                                fontFamily = PoppinsMedium,
                                color = if (canStart) MaterialTheme.colorScheme.onSurface
                                       else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Fecha final
                    Card(
                        onClick = { if (canEnd) sheetShowEndPicker = true },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (canEnd) MaterialTheme.colorScheme.surface
                                           else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (canEnd) 1.dp else 0.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(20.adp()),
                                tint = if (canEnd) MaterialTheme.colorScheme.primary
                                      else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                            )
                            Text(
                                sheetEndDate?.let { dateFormat.format(it) } ?: "Final",
                                fontSize = 13.asp(),
                                fontFamily = PoppinsMedium,
                                color = if (canEnd) MaterialTheme.colorScheme.onSurface
                                       else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Botón eliminar (solo si está añadido)
                if (isAdded) {
                    OutlinedButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.adp()),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.5.dp,
                            MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = null,
                            modifier = Modifier.size(20.adp())
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Eliminar",
                            fontSize = 15.asp(),
                            fontFamily = PoppinsBold
                        )
                    }
                }

                // Botón guardar
                Button(
                    onClick = {
                        val selectedStatus = sheetStatus
                        if (selectedStatus != null) {
                            val scoreToPass = if (selectedStatus == "Planeado") 0f else sheetRating
                            val priorityToPass = if (selectedStatus == "Planeado") sheetPlannedPriority else null
                            val noteToPass = if (selectedStatus == "Planeado" && sheetPlannedNote.isNotBlank())
                                sheetPlannedNote
                            else if (sheetOpinion.isNotBlank())
                                sheetOpinion
                            else
                                ""

                            onSave(
                                selectedStatus,
                                scoreToPass,
                                sheetStartDate,
                                sheetEndDate,
                                priorityToPass,
                                noteToPass
                            )
                        }
                    },
                    enabled = sheetStatus != null,
                    modifier = Modifier
                        .weight(if (isAdded) 1f else 1f)
                        .height(56.adp()),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 3.dp,
                        pressedElevation = 6.dp
                    )
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(20.adp())
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (isAdded) "Guardar" else "Agregar",
                        fontSize = 15.asp(),
                        fontFamily = PoppinsBold
                    )
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
    CHARACTERS,
    PRODUCTION
}

@Composable
private fun AnimeDetailTabSelector(
    selectedTab: AnimeDetailTab,
    onTabSelected: (AnimeDetailTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(
        AnimeDetailTab.OVERVIEW to Icons.Default.Description,
        AnimeDetailTab.CHARACTERS to Icons.Default.People,
        AnimeDetailTab.PRODUCTION to Icons.Default.Business
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 20.dp, top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(modifier = Modifier.padding(4.dp)) {
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
        label = "Tab Background Color"
    )
    val iconColor by animateColorAsState(
        targetValue = if (isSelected)
            MaterialTheme.colorScheme.onPrimary
        else
            MaterialTheme.colorScheme.onSurfaceVariant,
        label = "Tab Icon Color"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.adp()),
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
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // SECCIÓN PRINCIPAL: Portada + Info
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // PORTADA
            Card(
                modifier = Modifier
                    .width(120.adp())
                    .height(180.adp()),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                            .clip(RoundedCornerShape(12.dp))
                    }
                } else {
                    Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
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
                    .height(180.adp()),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // TÍTULOS Y ESTADO
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Título principal
                    Text(
                        text = animeDetail?.title ?: "",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.asp(),
                        fontFamily = PoppinsBold,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 22.asp()
                    )

                    // Título japonés
                    if (!animeDetail?.titleJapanese.isNullOrBlank()) {
                        Text(
                            text = animeDetail.titleJapanese,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.asp(),
                            fontFamily = PoppinsRegular,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Type y Status badges
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Type badge
                        AnimeTypeBadge(type = animeDetail?.typeAnime ?: "")

                        // Status badge
                        AnimeStatusBadge(status = animeDetail?.status ?: "")
                    }

                    // Broadcast (si está en emisión)
                    if (animeDetail?.status == "Currently Airing" && animeDetail.broadcast != null) {
                        AnimeBroadcastBadge(broadcast = animeDetail.broadcast.fullString ?: "")
                    }
                }

                // BOTÓN DE ACCIÓN MEJORADO
                Button(
                    onClick = onAddToListClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp), // Más alto
                    shape = RoundedCornerShape(14.dp), // Más redondeado
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isAdded)
                            MaterialTheme.colorScheme.secondaryContainer
                        else
                            MaterialTheme.colorScheme.primary
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp, // Mayor elevación
                        pressedElevation = 8.dp
                    )
                ) {
                    Icon(
                        imageVector = if (isAdded) Icons.Default.Edit else Icons.Default.Favorite,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp) // Icono más grande
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isAdded) "Editar" else "Añadir",
                        fontSize = 15.sp, // Texto más grande
                        fontFamily = PoppinsBold,
                        letterSpacing = 0.3.sp
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

@Composable
private fun AnimeStatCard(
    statData: StatData,
    index: Int,
    statsCount: Int,
    modifier: Modifier = Modifier
) {
    // Estados de animación
    var isPressed by remember { mutableStateOf(false) }

    // Animación de entrada escalonada (más sutil)
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale"
    )

    // Animación de aparición
    val animatedAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(
            durationMillis = 350,
            delayMillis = index * 60,
            easing = FastOutSlowInEasing
        ),
        label = "alpha"
    )

    val animatedOffset by animateFloatAsState(
        targetValue = 0f,
        animationSpec = tween(
            durationMillis = 450,
            delayMillis = index * 60,
            easing = FastOutSlowInEasing
        ),
        label = "offset"
    )

    // Color de acento más sutil
    val accentColor = when (statData.type) {
        StatType.SCORE -> MaterialTheme.colorScheme.primary
        StatType.EPISODES -> MaterialTheme.colorScheme.tertiary
        StatType.RANK -> MaterialTheme.colorScheme.secondary
        StatType.USERS -> MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)
    }

    // Ajustar tamaños según cantidad de stats
    val labelFontSize = when (statsCount) {
        1, 2 -> 10.sp
        3 -> 9.sp
        else -> 9.sp // 4 o más - reducido para que quepa
    }

    val valueFontSize = when (statsCount) {
        1 -> 22.sp
        2 -> 20.sp
        3 -> 18.sp
        else -> 15.sp // 4 o más - reducido para que quepa
    }

    val horizontalPadding = when (statsCount) {
        1, 2 -> 16.dp
        3 -> 10.dp
        else -> 4.dp // 4 o más - muy reducido para que quepa
    }

    val verticalPadding = when (statsCount) {
        1, 2 -> 12.dp
        else -> 10.dp
    }

    val letterSpacing = when (statsCount) {
        1, 2 -> 0.8.sp
        3 -> 0.6.sp
        else -> 0.3.sp // 4 o más - muy reducido para que quepa
    }

    Card(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                alpha = animatedAlpha
                translationY = animatedOffset * 12f
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    }
                )
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding, vertical = verticalPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Etiqueta destacada arriba
            Text(
                text = statData.label.uppercase(),
                fontFamily = PoppinsBold,
                fontSize = labelFontSize,
                color = accentColor,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = letterSpacing,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            // Valor principal grande
            Text(
                text = statData.value,
                fontFamily = PoppinsBold,
                fontSize = valueFontSize,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.4).sp,
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
private fun AnimeCharactersTab(
    characters: List<AnimeCharactersDetail>,
    isLoading: Boolean,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        when {
            isLoading -> {
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
            }
            characters.isEmpty() -> {
                AnimeEmptyState(message = "No hay personajes disponibles")
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.heightIn(max = 2000.dp),
                    userScrollEnabled = false
                ) {
                    items(characters, key = { it.idCharacter ?: 0 }) { character ->
                        AnimeCharacterCard(
                            character = character,
                            onClick = {
                                character.idCharacter?.let {
                                    navController.navigate("character_detail_route/$it")
                                }
                            }
                        )
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

        // Studios
        if (!animeDetail?.studios.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            AnimeInfoCard(title = "Estudios") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    animeDetail.studios.filterNotNull()
                        .filter { !it.nameStudio.isNullOrBlank() }
                        .forEach { studio ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Business,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = studio.nameStudio ?: "",
                                    fontFamily = PoppinsMedium,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                }
            }
        }

        // Producers
        if (!animeDetail?.producers.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            AnimeInfoCard(title = "Productores") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    animeDetail.producers.filterNotNull()
                        .filter { !it.name.isNullOrBlank() }
                        .forEach { producer ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Movie,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = producer.name ?: "",
                                    fontFamily = PoppinsMedium,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                }
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
private fun AnimeCharacterCard(
    character: AnimeCharactersDetail,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Colores según el rol
    val roleColor = when (character.role.lowercase()) {
        "main" -> Color(0xFFFF5722) // Naranja rojizo - protagonista
        "supporting" -> Color(0xFF2196F3) // Azul - soporte
        else -> Color(0xFF9E9E9E) // Gris - otro
    }

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            // Character image - muy compacto para 3 columnas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.62f) // Reducido más para 3 columnas
            ) {
                AsyncImage(
                    model = character.imageCharacter?.jpg?.imageUrl,
                    contentDescription = character.nameCharacter,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Gradiente en la parte inferior para mejor legibilidad del nombre
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.75f)
                                )
                            )
                        )
                )

                // Role badge mejorado - icono + texto compacto
                if (character.role.isNotBlank()) {
                    val (icon, roleText) = when (character.role.lowercase()) {
                        "main" -> "★" to "Main"
                        "supporting" -> "◆" to "Supp"
                        else -> "•" to character.role.take(4)
                    }

                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(4.dp),
                        shape = RoundedCornerShape(4.dp),
                        color = roleColor.copy(alpha = 0.9f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = icon,
                                fontFamily = PoppinsBold,
                                fontSize = 9.sp,
                                color = Color.White
                            )
                            Text(
                                text = roleText,
                                fontFamily = PoppinsBold,
                                fontSize = 9.sp,
                                color = Color.White,
                                letterSpacing = 0.2.sp
                            )
                        }
                    }
                }

                // Character name sobre la imagen - en la parte inferior
                Text(
                    text = character.nameCharacter ?: "Unknown",
                    fontFamily = PoppinsBold,
                    fontSize = 9.sp,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 11.sp,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(6.dp)
                )
            }
        }
    }
}

@Composable
private fun AnimeInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontFamily = PoppinsMedium,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = value,
                fontFamily = PoppinsBold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun AnimeTypeBadge(type: String) {
    if (type.isBlank()) return

    // Mapeo de tipos de anime con colores específicos (AniList format)
    val (icon, color) = when (type) {
        "TV" -> Icons.Default.Tv to Color(0xFF2196F3) // Azul
        "MOVIE" -> Icons.Default.Movie to Color(0xFF9C27B0) // Púrpura
        "OVA" -> Icons.Default.PlayCircle to Color(0xFF00BCD4) // Cian
        "ONA" -> Icons.Default.PlayCircle to Color(0xFF009688) // Teal
        "SPECIAL" -> Icons.Default.Star to Color(0xFFFF9800) // Naranja
        "MUSIC" -> Icons.Default.MusicNote to Color(0xFFE91E63) // Rosa
        else -> Icons.Default.Tv to Color(0xFF2196F3) // Azul por defecto
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.15f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = color
            )
            Text(
                text = formatAnimeType(type),
                fontFamily = PoppinsBold,
                fontSize = 11.sp,
                color = color,
                letterSpacing = 0.3.sp
            )
        }
    }
}

@Composable
private fun AnimeStatusBadge(status: String) {
    if (status.isBlank()) return

    // Mapeo completo de todos los estados posibles de AniList API (igual que manga)
    val (text, icon, color) = when (status) {
        "FINISHED" -> Triple(
            "Finalizado",
            Icons.Default.CheckCircle,
            Color(0xFF4CAF50) // Verde - completado
        )
        "RELEASING" -> Triple(
            "En emisión",
            Icons.Default.FiberManualRecord,
            Color(0xFF2196F3) // Azul - futuro
        )
        "NOT_YET_RELEASED" -> Triple(
            "Próximamente",
            Icons.Default.Schedule,
            Color(0xFFFF5722) // Rojo/Naranja - en vivo
        )
        "CANCELLED" -> Triple(
            "Cancelado",
            Icons.Default.Cancel,
            Color(0xFFF44336) // Rojo - cancelado
        )
        "HIATUS" -> Triple(
            "En pausa",
            Icons.Default.Pause,
            Color(0xFFFF9800) // Naranja - pausado
        )
        else -> Triple(
            status,
            Icons.Default.Info,
            Color(0xFF757575) // Gris - desconocido
        )
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.15f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(if (status == "RELEASING") 10.dp else 14.dp),
                tint = color
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

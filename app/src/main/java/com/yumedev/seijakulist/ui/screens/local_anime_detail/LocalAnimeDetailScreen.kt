package com.yumedev.seijakulist.ui.screens.local_anime_detail

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.yumedev.seijakulist.domain.models.AnimeEntityDomain
import com.yumedev.seijakulist.ui.components.confirm_dialog.ConfirmChangePlannedDialog
import com.yumedev.seijakulist.ui.screens.detail.components.shared.CompactGenreCard
import com.yumedev.seijakulist.ui.screens.detail.components.shared.RatingBar
import com.yumedev.seijakulist.ui.theme.*
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.text.SimpleDateFormat
import android.widget.Toast
import java.util.Locale

/**
 * Pantalla de detalle del anime LOCAL (de la lista del usuario)
 * Con el diseño visual de AnimeDetailScreen pero funcionalidades locales
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeDetailScreenLocal(
    navController: NavController,
    viewModel: LocalAnimeDetailViewModel = hiltViewModel(),
    animeId: Int
) {
    val anime by viewModel.anime.collectAsState()
    val isSharing by viewModel.isSharing.collectAsState()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // Estados de edición
    var showEditSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()

    // Estados del BottomSheet
    val statusList = listOf("Viendo", "Completado", "Pendiente", "Abandonado", "Planeado")
    var sheetStatus by remember { mutableStateOf<String?>(null) }
    var sheetRating by remember { mutableFloatStateOf(0f) }
    var sheetOpinion by remember { mutableStateOf("") }
    var sheetStartDate by remember { mutableStateOf<Long?>(null) }
    var sheetEndDate by remember { mutableStateOf<Long?>(null) }
    var sheetPlannedPriority by remember { mutableStateOf<String?>(null) }
    var sheetPlannedNote by remember { mutableStateOf("") }
    var sheetShowStartPicker by remember { mutableStateOf(false) }
    var sheetShowEndPicker by remember { mutableStateOf(false) }
    var sheetShowChangePlannedDialog by remember { mutableStateOf(false) }
    var sheetPendingNewStatus by remember { mutableStateOf<String?>(null) }

    // Estados para date pickers de la pantalla principal
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    val startDatePickerState = rememberDatePickerState()
    val endDatePickerState = rememberDatePickerState()
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    // Tab state
    var selectedTab by remember { mutableStateOf(LocalAnimeDetailTab.MY_TRACKING) }

    // Configurar status bar transparente
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
            window?.let {
                WindowCompat.setDecorFitsSystemWindows(it, true)
                originalStatusBarColor?.let { color -> it.statusBarColor = color }
                originalNavigationBarColor?.let { color -> it.navigationBarColor = color }
            }
        }
    }

    // Sincronizar date pickers con el anime actual
    LaunchedEffect(anime?.startDate) {
        startDatePickerState.selectedDateMillis = anime?.startDate
    }
    LaunchedEffect(anime?.endDate) {
        endDatePickerState.selectedDateMillis = anime?.endDate
    }

    // FAB visibility
    val showFab by remember {
        derivedStateOf { listState.firstVisibleItemIndex >= 1 }
    }

    when (val currentAnime = anime) {
        null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        else -> {
            // DatePickers principales
            if (showStartDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showStartDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.updateDates(
                                startDate = startDatePickerState.selectedDateMillis,
                                endDate = currentAnime.endDate
                            )
                            showStartDatePicker = false
                        }) { Text("OK") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showStartDatePicker = false }) { Text("Cancelar") }
                    }
                ) { DatePicker(state = startDatePickerState) }
            }

            if (showEndDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showEndDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.updateDates(
                                startDate = currentAnime.startDate,
                                endDate = endDatePickerState.selectedDateMillis
                            )
                            showEndDatePicker = false
                        }) { Text("OK") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showEndDatePicker = false }) { Text("Cancelar") }
                    }
                ) { DatePicker(state = endDatePickerState) }
            }

            Scaffold(
                containerColor = Color.Transparent,
                floatingActionButton = {
                    AnimatedVisibility(
                        visible = showFab,
                        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                    ) {
                        ExtendedFloatingActionButton(
                            onClick = { showEditSheet = true },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            text = {
                                Text(
                                    text = "Editar anime",
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
                        // BANNER CON BLUR + HEADER
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Banner image con blur (idéntico a la API)
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(statusBarHeight + 320.dp)
                                ) {
                                    // Banner image con blur
                                    AsyncImage(
                                        model = currentAnime.image,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .blur(25.dp),
                                        contentScale = ContentScale.Crop
                                    )

                                    // Gradient overlay (idéntico a la API)
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

                                // Header con portada e información (ENCIMA del banner)
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = statusBarHeight + 56.dp)
                                ) {
                                    LocalAnimeDetailHeader(
                                        anime = currentAnime,
                                        onEditClick = { showEditSheet = true }
                                    )
                                }
                            }
                        }

                        // Stats Cards
                        item {
                            LocalAnimeStatsRow(anime = currentAnime)
                        }

                        // Tab selector
                        item {
                            LocalAnimeDetailTabSelector(
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
                                label = "LocalAnimeTabContent"
                            ) { tab ->
                                when (tab) {
                                    LocalAnimeDetailTab.MY_TRACKING -> {
                                        LocalAnimeTrackingTab(
                                            anime = currentAnime,
                                            onEditStartDate = { showStartDatePicker = true },
                                            onEditEndDate = { showEndDatePicker = true },
                                            onClearStartDate = {
                                                viewModel.updateDates(null, currentAnime.endDate)
                                            },
                                            onClearEndDate = {
                                                viewModel.updateDates(currentAnime.startDate, null)
                                            },
                                            onEditPlannedPriority = { priority, note ->
                                                viewModel.updatePlannedPriorityAndNote(priority, note)
                                            }
                                        )
                                    }
                                    LocalAnimeDetailTab.OVERVIEW -> {
                                        LocalAnimeOverviewTab(anime = currentAnime)
                                    }
                                    LocalAnimeDetailTab.INFO -> {
                                        LocalAnimeInfoTab(anime = currentAnime)
                                    }
                                }
                            }
                        }
                    }

                    // TOP BAR CON BOTÓN DE ATRÁS Y COMPARTIR
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(statusBarHeight + 56.dp)
                            .align(Alignment.TopCenter)
                    ) {
                        // Botón atrás
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

                        // Botón compartir
                        Surface(
                            shape = CircleShape,
                            color = Color.Black.copy(alpha = 0.5f),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 8.dp, bottom = 8.dp)
                        ) {
                            IconButton(
                                onClick = { viewModel.shareAnime(context) },
                                enabled = !isSharing
                            ) {
                                if (isSharing) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        strokeWidth = 2.dp,
                                        color = Color.White
                                    )
                                } else {
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

            // MODAL DE EDICIÓN (Dialog personalizado sin drag - idéntico a la API)
            if (showEditSheet) {
                LaunchedEffect(showEditSheet) {
                    if (showEditSheet) {
                        sheetStatus = currentAnime.userStatus
                        sheetRating = currentAnime.userScore
                        sheetOpinion = currentAnime.userOpiniun ?: ""
                        sheetStartDate = currentAnime.startDate
                        sheetEndDate = currentAnime.endDate
                        sheetPlannedPriority = currentAnime.plannedPriority
                        sheetPlannedNote = currentAnime.plannedNote ?: ""
                    }
                }

                // Dialogs dentro del modal
                if (sheetShowChangePlannedDialog) {
                    ConfirmChangePlannedDialog(
                        newStatus = sheetPendingNewStatus ?: "",
                        onConfirm = {
                            sheetShowChangePlannedDialog = false
                            val newSt = sheetPendingNewStatus ?: return@ConfirmChangePlannedDialog
                            sheetPendingNewStatus = null
                            sheetStatus = newSt
                            viewModel.updateAnime(
                                status = newSt,
                                score = 0f,
                                opinion = sheetOpinion.ifBlank { null },
                                startDate = sheetStartDate,
                                endDate = sheetEndDate,
                                plannedPriority = null,
                                plannedNote = null
                            )
                            showEditSheet = false
                        },
                        onDismiss = {
                            sheetShowChangePlannedDialog = false
                            sheetPendingNewStatus = null
                        }
                    )
                }

                if (sheetShowStartPicker) {
                    DatePickerDialog(
                        onDismissRequest = { sheetShowStartPicker = false },
                        confirmButton = {
                            Button(onClick = { sheetShowStartPicker = false }) { Text("OK") }
                        },
                        dismissButton = {
                            Button(onClick = { sheetShowStartPicker = false }) { Text("Cancelar") }
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
                            Button(onClick = { sheetShowEndPicker = false }) { Text("OK") }
                        },
                        dismissButton = {
                            Button(onClick = { sheetShowEndPicker = false }) { Text("Cancelar") }
                        }
                    ) {
                        val pickerState = rememberDatePickerState(initialSelectedDateMillis = sheetEndDate)
                        DatePicker(state = pickerState)
                        sheetEndDate = pickerState.selectedDateMillis
                    }
                }

                Dialog(
                    onDismissRequest = { showEditSheet = false },
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
                                onClick = { showEditSheet = false },
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

                                // Contenido del modal (idéntico al de la API)
                                LocalAnimeEditModalContent(
                                    anime = currentAnime,
                                    onDismiss = { showEditSheet = false },
                                    onSave = { status, rating, startDate, endDate, priority, note ->
                                        val newStatus = status ?: return@LocalAnimeEditModalContent
                                        val wasPlanned = currentAnime.userStatus == "Planeado"
                                        val changingFromPlanned = wasPlanned && newStatus != "Planeado"
                                        val hadPriorityData = currentAnime.plannedPriority != null ||
                                                currentAnime.plannedNote?.isNotBlank() == true

                                        if (changingFromPlanned && hadPriorityData) {
                                            sheetPendingNewStatus = newStatus
                                            sheetShowChangePlannedDialog = true
                                        } else {
                                            val scoreToPass = if (newStatus == "Planeado") 0f else rating
                                            viewModel.updateAnime(
                                                status = newStatus,
                                                score = scoreToPass,
                                                opinion = note.ifBlank { null },
                                                startDate = startDate,
                                                endDate = endDate,
                                                plannedPriority = if (newStatus == "Planeado") priority else null,
                                                plannedNote = if (newStatus == "Planeado" && note.isNotBlank())
                                                    note else null
                                            )
                                            scope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = "Anime actualizado",
                                                    actionLabel = "OK",
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                            showEditSheet = false
                                        }
                                    },
                                    onDelete = {
                                        // TODO: Implementar eliminación
                                        showEditSheet = false
                                    }
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

enum class LocalAnimeDetailTab(val title: String) {
    MY_TRACKING("Mi Seguimiento"),
    OVERVIEW("Resumen"),
    INFO("Información")
}

@Composable
private fun LocalAnimeDetailTabSelector(
    selectedTab: LocalAnimeDetailTab,
    onTabSelected: (LocalAnimeDetailTab) -> Unit,
    modifier: Modifier = Modifier
) {
    // Tres tabs con texto (idéntico a la API)
    val tabs = listOf(
        LocalAnimeDetailTab.MY_TRACKING,
        LocalAnimeDetailTab.OVERVIEW,
        LocalAnimeDetailTab.INFO
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
                    LocalAnimeTabItem(
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
private fun LocalAnimeTabItem(
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

@Composable
private fun LocalAnimeDetailHeader(
    anime: AnimeEntityDomain,
    onEditClick: () -> Unit,
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
        // PORTADA (a la izquierda, más grande - idéntico a la API)
        Card(
            modifier = Modifier
                .width(150.dp)
                .height(215.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(anime.image)
                    .size(Size.ORIGINAL)
                    .crossfade(false)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
            )
        }

        // INFORMACIÓN A LA DERECHA (alineada al bottom - idéntico a la API)
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Título principal
                Text(
                    text = anime.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 22.sp,
                    fontFamily = PoppinsBold,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 26.sp
                )

                // Título japonés (si existe)
                if (!anime.titleJapanese.isNullOrBlank()) {
                    Text(
                        text = anime.titleJapanese,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        fontFamily = PoppinsRegular,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Type y Status badges (datos locales)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Type badge (si existe)
                    anime.typeAnime?.let { type ->
                        LocalAnimeTypeBadge(type = type)
                    }

                    // Status badge (si existe)
                    anime.status?.let { status ->
                        LocalAnimeStatusBadge(status = status)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón "Editar" (idéntico al de la API pero solo editar)
            Button(
                onClick = onEditClick,
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
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Editar",
                    fontSize = 14.sp,
                    fontFamily = PoppinsBold
                )
            }
        }
    }
}

// ============================================================================
// STATS ROW
// ============================================================================

@Composable
private fun LocalAnimeStatsRow(
    anime: AnimeEntityDomain,
    modifier: Modifier = Modifier
) {
    val stats = remember(anime) {
        buildList {
            // User Score
            if (anime.userScore > 0) {
                add(
                    StatData(
                        icon = Icons.Default.Star,
                        value = if (anime.userScore % 1.0 == 0.0) {
                            "${anime.userScore.toInt()}"
                        } else {
                            String.format("%.1f", anime.userScore)
                        },
                        label = "Mi Score",
                        type = StatType.USER_SCORE
                    )
                )
            }

            // Episodios vistos
            add(
                StatData(
                    icon = Icons.Default.Tv,
                    value = "${anime.episodesWatched}/${anime.totalEpisodes ?: "?"}",
                    label = "Episodios",
                    type = StatType.EPISODES
                )
            )

            // Veces visto
            if (anime.rewatchCount > 0) {
                add(
                    StatData(
                        icon = Icons.Default.Autorenew,
                        value = "${anime.rewatchCount}",
                        label = "Veces visto",
                        type = StatType.REWATCH
                    )
                )
            }

            // MAL Score (si existe)
            if (anime.score != null && anime.score > 0) {
                add(
                    StatData(
                        icon = Icons.Default.Star,
                        value = String.format("%.1f", anime.score),
                        label = "MAL Score",
                        type = StatType.MAL_SCORE
                    )
                )
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
            LocalAnimeStatCard(
                statData = stat,
                index = index,
                statsCount = stats.size,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

private data class StatData(
    val icon: ImageVector,
    val value: String,
    val label: String,
    val type: StatType
)

private enum class StatType {
    USER_SCORE, EPISODES, REWATCH, MAL_SCORE
}

@Composable
private fun LocalAnimeStatCard(
    statData: StatData,
    index: Int,
    statsCount: Int,
    modifier: Modifier = Modifier
) {
    // Diseño simple y limpio (idéntico a la API)
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
            if (statData.type == StatType.USER_SCORE || statData.type == StatType.MAL_SCORE) {
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
                text = statData.label.uppercase(),
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
private fun LocalAnimeTrackingTab(
    anime: AnimeEntityDomain,
    onEditStartDate: () -> Unit,
    onEditEndDate: () -> Unit,
    onClearStartDate: () -> Unit,
    onClearEndDate: () -> Unit,
    onEditPlannedPriority: (String?, String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val sdf = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            .apply { timeZone = java.util.TimeZone.getTimeZone("UTC") }
    }

    var showEditPlannedDialog by remember { mutableStateOf(false) }
    var editPlannedPriority by remember { mutableStateOf<String?>(null) }
    var editPlannedNote by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Fechas de seguimiento
        LocalAnimeInfoCard(title = "Seguimiento") {
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
                            modifier = Modifier.size(18.adp())
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "Inicio",
                                fontSize = 12.asp(),
                                fontFamily = PoppinsRegular,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = anime.startDate?.let { sdf.format(it) } ?: "Sin fecha",
                                fontSize = 14.asp(),
                                fontFamily = PoppinsBold,
                                color = if (anime.startDate != null)
                                    MaterialTheme.colorScheme.onSurface
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        if (anime.startDate != null) {
                            IconButton(
                                onClick = onClearStartDate,
                                modifier = Modifier.size(32.adp())
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Borrar fecha de inicio",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(16.adp())
                                )
                            }
                        }
                        IconButton(
                            onClick = onEditStartDate,
                            modifier = Modifier.size(32.adp())
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar fecha de inicio",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.adp())
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
                            modifier = Modifier.size(18.adp())
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "Finalización",
                                fontSize = 12.asp(),
                                fontFamily = PoppinsRegular,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = anime.endDate?.let { sdf.format(it) } ?: "Sin fecha",
                                fontSize = 14.asp(),
                                fontFamily = PoppinsBold,
                                color = if (anime.endDate != null)
                                    MaterialTheme.colorScheme.onSurface
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        if (anime.endDate != null) {
                            IconButton(
                                onClick = onClearEndDate,
                                modifier = Modifier.size(32.adp())
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Borrar fecha de finalización",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(16.adp())
                                )
                            }
                        }
                        IconButton(
                            onClick = onEditEndDate,
                            modifier = Modifier.size(32.adp())
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar fecha de finalización",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.adp())
                            )
                        }
                    }
                }
            }
        }

        // Prioridad del plan (solo si está Planeado)
        if (anime.userStatus == "Planeado") {
            if (showEditPlannedDialog) {
                AlertDialog(
                    onDismissRequest = { showEditPlannedDialog = false },
                    title = {
                        Text(
                            text = "Editar prioridad del plan",
                            fontFamily = PoppinsBold
                        )
                    },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(
                                text = "Prioridad",
                                fontFamily = PoppinsBold,
                                fontSize = 14.asp(),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            val priorities = listOf("Alta", "Media", "Baja")
                            val priorityColors = mapOf(
                                "Alta" to Color(0xFFEF5350),
                                "Media" to Color(0xFFFFCA28),
                                "Baja" to Color(0xFF66BB6A)
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                priorities.forEach { priority ->
                                    val isSelected = editPlannedPriority == priority
                                    Surface(
                                        onClick = {
                                            editPlannedPriority = if (isSelected) null else priority
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(40.adp()),
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (isSelected)
                                            priorityColors[priority] ?: MaterialTheme.colorScheme.primaryContainer
                                        else
                                            MaterialTheme.colorScheme.surfaceContainerHighest,
                                        shadowElevation = if (isSelected) 4.dp else 1.dp
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = priority,
                                                fontSize = 13.asp(),
                                                fontFamily = PoppinsRegular,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }

                            Text(
                                text = "Nota del plan",
                                fontFamily = PoppinsBold,
                                fontSize = 14.asp(),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            OutlinedTextField(
                                value = editPlannedNote,
                                onValueChange = { editPlannedNote = it },
                                placeholder = { Text("¿Por qué lo tenés planeado?") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.adp()),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    cursorColor = MaterialTheme.colorScheme.primary
                                ),
                                shape = RoundedCornerShape(10.dp),
                                maxLines = 4
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                onEditPlannedPriority(
                                    editPlannedPriority,
                                    editPlannedNote.ifBlank { null }
                                )
                                showEditPlannedDialog = false
                            }
                        ) {
                            Text("Guardar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showEditPlannedDialog = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }

            LocalAnimeInfoCard(title = "Prioridad del plan") {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Header con botón editar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = {
                                editPlannedPriority = anime.plannedPriority
                                editPlannedNote = anime.plannedNote ?: ""
                                showEditPlannedDialog = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar prioridad",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.adp())
                            )
                        }
                    }

                    // Prioridad actual
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Prioridad",
                            fontSize = 14.asp(),
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
                                fontSize = 14.asp(),
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
                            fontSize = 14.asp(),
                            fontFamily = PoppinsRegular,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = anime.plannedNote,
                            fontSize = 14.asp(),
                            fontFamily = PoppinsRegular,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Mi Reseña
        if (!anime.userOpiniun.isNullOrEmpty()) {
            LocalAnimeInfoCard(title = "Mi Reseña") {
                Text(
                    text = anime.userOpiniun,
                    textAlign = TextAlign.Justify,
                    fontSize = 14.asp(),
                    fontFamily = PoppinsRegular,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.asp()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun LocalAnimeOverviewTab(
    anime: AnimeEntityDomain,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Synopsis (idéntico a la API)
        if (!anime.synopsis.isNullOrBlank()) {
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
                                clipboardManager.setText(AnnotatedString(anime.synopsis))
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
                                val synopsis = anime.synopsis
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
                    text = anime.synopsis,
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

        // Genres (idéntico a la API)
        if (!anime.genres.isNullOrEmpty()) {
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

                val genresList = anime.genres.split(",").map { it.trim() }
                androidx.compose.foundation.layout.FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    genresList.forEach { genre ->
                        CompactGenreCard(
                            genreName = genre,
                            modifier = Modifier
                                .width(110.dp)
                                .height(40.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun LocalAnimeInfoTab(
    anime: AnimeEntityDomain,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Basic info - filas simples con dividers (idéntico a la API)
        if (!anime.titleEnglish.isNullOrBlank()) {
            SimpleInfoRow("Título en inglés", anime.titleEnglish)
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
        }

        if (!anime.titleJapanese.isNullOrBlank()) {
            SimpleInfoRow("Título japonés", anime.titleJapanese)
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
        }

        anime.typeAnime?.let {
            SimpleInfoRow("Tipo", it)
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
        }

        // Estado con dot indicator (idéntico a la API)
        anime.status?.let { status ->
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
                                when (status) {
                                    "RELEASING", "Currently Airing" -> Color(0xFF00A8FF)
                                    "FINISHED", "Finished" -> Color(0xFF7EE787)
                                    else -> MaterialTheme.colorScheme.onSurface
                                }
                            )
                    )
                    Text(
                        text = status,
                        fontFamily = PoppinsBold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.End
                    )
                }
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
        }

        anime.aired?.let {
            SimpleInfoRow("Emisión", it)
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
        }

        anime.totalEpisodes?.let {
            SimpleInfoRow("Episodios", "${it}${if (it > 1000) "+" else ""}")
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
        }

        anime.duration?.let {
            SimpleInfoRow("Duración", it)
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
        }

        anime.rating?.let {
            SimpleInfoRow("Clasificación", it)
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
        }

        anime.source?.let {
            SimpleInfoRow("Fuente", it)
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
        }

        anime.score?.let {
            SimpleInfoRow("Puntuación", "${it} / 10")
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
        }

        anime.scoreBy?.let {
            SimpleInfoRow("Valoraciones", "${it} usuarios")
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
        }

        anime.rank?.let {
            SimpleInfoRow("Ranking", "#${it}")
        }

        // Studios (idéntico a la API)
        if (!anime.studios.isNullOrEmpty()) {
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

                val studiosList = anime.studios.split(",").map { it.trim() }
                androidx.compose.foundation.layout.FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    studiosList.forEach { studio ->
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerHigh
                        ) {
                            Text(
                                text = studio,
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

// Fila simple de información sin card (copiado de la API)
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

// ============================================================================
// BOTTOM SHEET CONTENT
// ============================================================================

@Composable
private fun EditAnimeBottomSheetContent(
    statusList: List<String>,
    sheetStatus: String?,
    onStatusChange: (String?) -> Unit,
    sheetRating: Float,
    onRatingChange: (Float) -> Unit,
    sheetOpinion: String,
    onOpinionChange: (String) -> Unit,
    sheetStartDate: Long?,
    sheetEndDate: Long?,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit,
    sheetPlannedPriority: String?,
    onPlannedPriorityChange: (String?) -> Unit,
    sheetPlannedNote: String,
    onPlannedNoteChange: (String) -> Unit,
    dateFormat: SimpleDateFormat,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Título
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.Favorite,
                null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.adp())
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "Editar en mi lista",
                fontSize = 20.asp(),
                fontFamily = PoppinsBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Estado
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Estado",
                fontSize = 16.asp(),
                fontFamily = PoppinsBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(2),
                modifier = Modifier.height(180.adp()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(statusList.size) { index ->
                    val status = statusList[index]
                    val isSelected = sheetStatus == status
                    Surface(
                        onClick = { onStatusChange(if (isSelected) null else status) },
                        modifier = Modifier.height(50.adp()),
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) getAnimeStatusColor(status)
                        else MaterialTheme.colorScheme.surfaceContainerHighest,
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
                                if (isSelected) Icon(
                                    Icons.Default.CheckCircle,
                                    null,
                                    modifier = Modifier.size(16.adp()),
                                    tint = Color.Black
                                )
                                Text(
                                    status,
                                    fontSize = 14.asp(),
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

        // Prioridad + nota (solo si Planeado)
        AnimatedVisibility(
            visible = sheetStatus == "Planeado",
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Prioridad",
                    fontSize = 16.asp(),
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
                    priorities.forEach { p ->
                        val isSel = sheetPlannedPriority == p
                        Surface(
                            onClick = { onPlannedPriorityChange(if (isSel) null else p) },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.adp()),
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSel) priorityColors[p]
                                ?: MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surfaceContainerHighest,
                            shadowElevation = if (isSel) 4.dp else 1.dp
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    p,
                                    fontSize = 14.asp(),
                                    fontFamily = PoppinsRegular,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSel) Color.Black else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
                Text(
                    "Nota del plan (opcional)",
                    fontSize = 16.asp(),
                    fontFamily = PoppinsBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                OutlinedTextField(
                    value = sheetPlannedNote,
                    onValueChange = onPlannedNoteChange,
                    placeholder = { Text("¿Por qué lo tenés planeado?") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.adp()),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 4
                )
            }
        }

        // Calificación (solo si no es Planeado)
        AnimatedVisibility(
            visible = sheetStatus != null && sheetStatus != "Planeado",
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
                        "Calificación",
                        fontSize = 16.asp(),
                        fontFamily = PoppinsBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (sheetRating > 0) {
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
                                    Icons.Default.Star,
                                    null,
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(16.adp())
                                )
                                Text(
                                    String.format("%.1f", sheetRating),
                                    fontSize = 14.asp(),
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
                    Box(
                        modifier = Modifier.padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        RatingBar(rating = sheetRating, onRatingChange = onRatingChange)
                    }
                }
            }
        }

        // Opinión
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Opinión (opcional)",
                fontSize = 16.asp(),
                fontFamily = PoppinsBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            OutlinedTextField(
                value = sheetOpinion,
                onValueChange = onOpinionChange,
                placeholder = { Text("Comparte tu opinión...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.adp()),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp),
                maxLines = 5
            )
        }

        // Fechas
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Fechas (opcional)",
                fontSize = 16.asp(),
                fontFamily = PoppinsBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            val canStart = sheetStatus != "Planeado"
            val canEnd = sheetStatus == "Completado"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    onClick = { if (canStart) onStartDateClick() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(
                        alpha = if (canStart) 1f else 0.5f
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Default.CalendarToday,
                            null,
                            modifier = Modifier.size(18.adp()),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                alpha = if (canStart) 1f else 0.4f
                            )
                        )
                        Text(
                            sheetStartDate?.let { dateFormat.format(it) } ?: "Inicio",
                            fontSize = 12.asp(),
                            fontFamily = PoppinsRegular,
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = if (canStart) 1f else 0.4f
                            )
                        )
                    }
                }
                Surface(
                    onClick = { if (canEnd) onEndDateClick() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(
                        alpha = if (canEnd) 1f else 0.5f
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Default.CalendarToday,
                            null,
                            modifier = Modifier.size(18.adp()),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                alpha = if (canEnd) 1f else 0.4f
                            )
                        )
                        Text(
                            sheetEndDate?.let { dateFormat.format(it) } ?: "Final",
                            fontSize = 12.asp(),
                            fontFamily = PoppinsRegular,
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = if (canEnd) 1f else 0.4f
                            )
                        )
                    }
                }
            }
        }

        // Botón guardar
        Button(
            onClick = onSave,
            enabled = sheetStatus != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.adp()),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        ) {
            Icon(Icons.Default.Check, null, modifier = Modifier.size(20.adp()))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Guardar cambios", fontSize = 16.asp(), fontFamily = PoppinsBold)
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

// ============================================================================
// COMPONENTS
// ============================================================================

// Componente para info card (usado solo en tracking tab)
@Composable
private fun LocalAnimeInfoCard(
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
private fun LocalAnimeUserStatusBadge(status: String) {
    if (status.isBlank()) return

    val statusColor = getAnimeStatusColor(status)

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = statusColor.copy(alpha = 0.15f),
        border = androidx.compose.foundation.BorderStroke(1.dp, statusColor.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.FiberManualRecord,
                contentDescription = null,
                modifier = Modifier.size(10.dp),
                tint = statusColor
            )
            Text(
                text = status,
                fontFamily = PoppinsBold,
                fontSize = 11.sp,
                color = statusColor,
                letterSpacing = 0.3.sp
            )
        }
    }
}

@Composable
private fun LocalAnimeUserScoreBadge(score: Float) {
    if (score <= 0) return

    val scoreColor = Color(0xFFFFD700) // Dorado

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = scoreColor.copy(alpha = 0.15f),
        border = androidx.compose.foundation.BorderStroke(1.dp, scoreColor.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = scoreColor
            )
            Text(
                text = if (score % 1.0 == 0.0) {
                    "${score.toInt()}/10"
                } else {
                    "${String.format("%.1f", score)}/10"
                },
                fontFamily = PoppinsBold,
                fontSize = 11.sp,
                color = scoreColor,
                letterSpacing = 0.3.sp
            )
        }
    }
}

// Badges tipo pill - diseño redondeado y moderno (copiado de la API)
@Composable
private fun LocalAnimeTypeBadge(type: String) {
    if (type.isBlank()) return

    // Colores según el tipo
    val color = when (type) {
        "TV" -> Color(0xFF1E88E5)
        "MOVIE", "Movie" -> Color(0xFF9C27B0)
        "OVA" -> Color(0xFF00BCD4)
        "ONA" -> Color(0xFF009688)
        "SPECIAL", "Special" -> Color(0xFFFF9800)
        "MUSIC", "Music" -> Color(0xFFE91E63)
        else -> Color(0xFF1E88E5)
    }

    val displayText = when (type) {
        "TV" -> "TV"
        "MOVIE", "Movie" -> "Película"
        "OVA" -> "OVA"
        "ONA" -> "ONA"
        "SPECIAL", "Special" -> "Especial"
        "MUSIC", "Music" -> "Música"
        else -> type
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
            text = displayText,
            fontFamily = PoppinsBold,
            fontSize = 11.sp,
            color = color,
            letterSpacing = 0.3.sp,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun LocalAnimeStatusBadge(status: String) {
    if (status.isBlank()) return

    val (text, color) = when (status) {
        "FINISHED", "Finished" -> "Finalizado" to Color(0xFF7EE787)
        "RELEASING", "Currently Airing" -> "En emisión" to Color(0xFF00A8FF)
        "NOT_YET_RELEASED", "Not yet aired" -> "Próximamente" to Color(0xFF79C0FF)
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

// ============================================================================
// LOCAL ANIME EDIT MODAL CONTENT (idéntico al de la API)
// ============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocalAnimeEditModalContent(
    anime: AnimeEntityDomain,
    onDismiss: () -> Unit,
    onSave: (String?, Float, Long?, Long?, String?, String) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Estado del formulario
    var selectedStatus by remember { mutableStateOf<String?>(anime.userStatus) }
    var rating by remember { mutableFloatStateOf(anime.userScore) }
    var opinion by remember { mutableStateOf(anime.userOpiniun ?: "") }
    var startDate by remember { mutableStateOf(anime.startDate) }
    var endDate by remember { mutableStateOf(anime.endDate) }
    var plannedPriority by remember { mutableStateOf(anime.plannedPriority) }
    var plannedNote by remember { mutableStateOf(anime.plannedNote ?: "") }

    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    // DatePicker Dialogs
    if (showStartPicker) {
        DatePickerDialog(
            onDismissRequest = { showStartPicker = false },
            confirmButton = {
                TextButton(onClick = { showStartPicker = false }) {
                    Text("OK", fontFamily = PoppinsMedium)
                }
            },
            dismissButton = {
                TextButton(onClick = { showStartPicker = false }) {
                    Text("Cancelar", fontFamily = PoppinsRegular)
                }
            }
        ) {
            val pickerState = rememberDatePickerState(initialSelectedDateMillis = startDate)
            DatePicker(state = pickerState)
            startDate = pickerState.selectedDateMillis
        }
    }

    if (showEndPicker) {
        DatePickerDialog(
            onDismissRequest = { showEndPicker = false },
            confirmButton = {
                TextButton(onClick = { showEndPicker = false }) {
                    Text("OK", fontFamily = PoppinsMedium)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEndPicker = false }) {
                    Text("Cancelar", fontFamily = PoppinsRegular)
                }
            }
        ) {
            val pickerState = rememberDatePickerState(initialSelectedDateMillis = endDate)
            DatePicker(state = pickerState)
            endDate = pickerState.selectedDateMillis
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // HEADER FIJO (adaptado para AnimeEntityDomain)
            LocalModalHeader(
                anime = anime,
                onDismiss = onDismiss
            )

            // CONTENIDO SCROLLEABLE
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 160.dp), // Espacio para el botón + navigation bar
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Spacer(modifier = Modifier.height(4.dp))

                // Selector de Estado
                com.yumedev.seijakulist.ui.screens.add_to_list.StatusSelector(
                    selectedStatus = selectedStatus,
                    onStatusSelected = { selectedStatus = it }
                )

                // Contenido condicional según estado
                when (selectedStatus) {
                    "Planeado" -> {
                        com.yumedev.seijakulist.ui.screens.add_to_list.PlannedSection(
                            priority = plannedPriority,
                            note = plannedNote,
                            onPriorityChanged = { plannedPriority = it },
                            onNoteChanged = { plannedNote = it }
                        )
                    }
                    null -> {
                        // Estado vacío, no mostrar nada
                    }
                    else -> {
                        // Viendo, Completado, Pendiente, Abandonado
                        com.yumedev.seijakulist.ui.screens.add_to_list.RatingSection(
                            rating = rating,
                            onRatingChanged = { rating = it }
                        )

                        com.yumedev.seijakulist.ui.screens.add_to_list.OpinionSection(
                            opinion = opinion,
                            onOpinionChanged = { opinion = it }
                        )

                        com.yumedev.seijakulist.ui.screens.add_to_list.DatesSection(
                            startDate = startDate,
                            endDate = endDate,
                            canSelectEndDate = selectedStatus == "Completado",
                            onStartDateClick = { showStartPicker = true },
                            onEndDateClick = { if (selectedStatus == "Completado") showEndPicker = true },
                            dateFormat = dateFormat
                        )
                    }
                }
            }
        }

        // BOTÓN FIJO EN EL BOTTOM
        com.yumedev.seijakulist.ui.screens.add_to_list.ActionButton(
            isAdded = true, // Siempre está añadido en local
            selectedStatus = selectedStatus,
            onSave = {
                val scoreToPass = if (selectedStatus == "Planeado") 0f else rating
                val priorityToPass = if (selectedStatus == "Planeado") plannedPriority else null
                val noteToPass = if (selectedStatus == "Planeado" && plannedNote.isNotBlank())
                    plannedNote
                else if (opinion.isNotBlank())
                    opinion
                else
                    ""

                onSave(
                    selectedStatus,
                    scoreToPass,
                    startDate,
                    endDate,
                    priorityToPass,
                    noteToPass
                )
            },
            onDelete = onDelete,
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

// Header adaptado para AnimeEntityDomain
@Composable
private fun LocalModalHeader(
    anime: AnimeEntityDomain,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Poster pequeño
            AsyncImage(
                model = anime.image,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            // Títulos
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = anime.title,
                    fontFamily = PoppinsBold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (!anime.titleJapanese.isNullOrBlank()) {
                    Text(
                        text = anime.titleJapanese,
                        fontFamily = PoppinsRegular,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Botón cerrar
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cerrar",
                    fontFamily = PoppinsMedium,
                    fontSize = 14.sp
                )
            }
        }

        // Divider
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
        )
    }
}

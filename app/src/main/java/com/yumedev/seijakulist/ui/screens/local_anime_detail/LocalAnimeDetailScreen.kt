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
    val editSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
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
                                // Banner image con blur
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(statusBarHeight + 280.dp)
                                ) {
                                    // Banner image con blur
                                    AsyncImage(
                                        model = currentAnime.image,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .blur(20.dp),
                                        contentScale = ContentScale.Crop
                                    )

                                    // Gradient overlay
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

                                // Header con portada e información
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = statusBarHeight + 56.dp)
                                ) {
                                    LocalAnimeDetailHeader(
                                        anime = currentAnime,
                                        onEditClick = { showEditSheet = true },
                                        onShareClick = { viewModel.shareAnime(context) },
                                        isSharing = isSharing
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

            // BOTTOM SHEET DE EDICIÓN
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

                // Dialogs dentro del sheet
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

                ModalBottomSheet(
                    onDismissRequest = { showEditSheet = false },
                    sheetState = editSheetState
                ) {
                    EditAnimeBottomSheetContent(
                        statusList = statusList,
                        sheetStatus = sheetStatus,
                        onStatusChange = { sheetStatus = it },
                        sheetRating = sheetRating,
                        onRatingChange = { sheetRating = it },
                        sheetOpinion = sheetOpinion,
                        onOpinionChange = { sheetOpinion = it },
                        sheetStartDate = sheetStartDate,
                        sheetEndDate = sheetEndDate,
                        onStartDateClick = { sheetShowStartPicker = true },
                        onEndDateClick = { sheetShowEndPicker = true },
                        sheetPlannedPriority = sheetPlannedPriority,
                        onPlannedPriorityChange = { sheetPlannedPriority = it },
                        sheetPlannedNote = sheetPlannedNote,
                        onPlannedNoteChange = { sheetPlannedNote = it },
                        dateFormat = dateFormat,
                        onSave = {
                            val newStatus = sheetStatus ?: return@EditAnimeBottomSheetContent
                            val wasPlanned = currentAnime.userStatus == "Planeado"
                            val changingFromPlanned = wasPlanned && newStatus != "Planeado"
                            val hadPriorityData = currentAnime.plannedPriority != null ||
                                    currentAnime.plannedNote?.isNotBlank() == true

                            if (changingFromPlanned && hadPriorityData) {
                                sheetPendingNewStatus = newStatus
                                sheetShowChangePlannedDialog = true
                            } else {
                                val scoreToPass = if (newStatus == "Planeado") 0f else sheetRating
                                viewModel.updateAnime(
                                    status = newStatus,
                                    score = scoreToPass,
                                    opinion = sheetOpinion.ifBlank { null },
                                    startDate = sheetStartDate,
                                    endDate = sheetEndDate,
                                    plannedPriority = if (newStatus == "Planeado") sheetPlannedPriority else null,
                                    plannedNote = if (newStatus == "Planeado" && sheetPlannedNote.isNotBlank())
                                        sheetPlannedNote else null
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
                        }
                    )
                }
            }
        }
    }
}

// ============================================================================
// TABS
// ============================================================================

enum class LocalAnimeDetailTab {
    MY_TRACKING,
    OVERVIEW,
    INFO
}

@Composable
private fun LocalAnimeDetailTabSelector(
    selectedTab: LocalAnimeDetailTab,
    onTabSelected: (LocalAnimeDetailTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(
        LocalAnimeDetailTab.MY_TRACKING to Icons.Default.Assignment,
        LocalAnimeDetailTab.OVERVIEW to Icons.Default.Description,
        LocalAnimeDetailTab.INFO to Icons.Default.Info
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
                    LocalAnimeTabItem(
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
private fun LocalAnimeTabItem(
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

@Composable
private fun LocalAnimeDetailHeader(
    anime: AnimeEntityDomain,
    onEditClick: () -> Unit,
    onShareClick: () -> Unit,
    isSharing: Boolean,
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
                        .clip(RoundedCornerShape(12.dp))
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
                        text = anime.title,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.asp(),
                        fontFamily = PoppinsBold,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 22.asp()
                    )

                    // Badges: User Status y User Score
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // User Status badge
                        LocalAnimeUserStatusBadge(status = anime.userStatus)

                        // User Score badge
                        LocalAnimeUserScoreBadge(score = anime.userScore)
                    }
                }

                // BOTÓN DE EDITAR
                Button(
                    onClick = onEditClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Editar",
                        fontSize = 15.sp,
                        fontFamily = PoppinsBold,
                        letterSpacing = 0.3.sp
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
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale"
    )

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

    val accentColor = when (statData.type) {
        StatType.USER_SCORE -> Color(0xFFFFD700) // Dorado para user score
        StatType.EPISODES -> MaterialTheme.colorScheme.tertiary
        StatType.REWATCH -> MaterialTheme.colorScheme.secondary
        StatType.MAL_SCORE -> MaterialTheme.colorScheme.primary
    }

    val labelFontSize = when (statsCount) {
        1, 2 -> 10.sp
        3 -> 9.sp
        else -> 9.sp
    }

    val valueFontSize = when (statsCount) {
        1 -> 22.sp
        2 -> 20.sp
        3 -> 18.sp
        else -> 15.sp
    }

    val horizontalPadding = when (statsCount) {
        1, 2 -> 16.dp
        3 -> 10.dp
        else -> 4.dp
    }

    val verticalPadding = when (statsCount) {
        1, 2 -> 12.dp
        else -> 10.dp
    }

    val letterSpacing = when (statsCount) {
        1, 2 -> 0.8.sp
        3 -> 0.6.sp
        else -> 0.3.sp
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
        // Sinopsis
        if (!anime.synopsis.isNullOrEmpty()) {
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
                    // Header
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
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Copiar
                            FilledTonalIconButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(anime.synopsis))
                                    Toast.makeText(
                                        context,
                                        "Sinopsis copiada",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copiar",
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Traducir
                            FilledTonalIconButton(
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
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Translate,
                                    contentDescription = "Traducir",
                                    modifier = Modifier.size(18.dp)
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

        // Géneros
        if (!anime.genres.isNullOrEmpty()) {
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

                    val genresList = anime.genres.split(",").map { it.trim() }
                    LazyRow(
                        modifier = Modifier.height(55.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(genresList.size) { index ->
                            CompactGenreCard(genreName = genresList[index])
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
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
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Otros títulos
        if (!anime.titleEnglish.isNullOrEmpty() || !anime.titleJapanese.isNullOrEmpty()) {
            LocalAnimeInfoCard(title = "Otros títulos") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (!anime.titleEnglish.isNullOrEmpty()) {
                        LocalAnimeInfoRow(label = "Inglés", value = anime.titleEnglish)
                    }
                    if (!anime.titleJapanese.isNullOrEmpty()) {
                        if (!anime.titleEnglish.isNullOrEmpty()) {
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                            )
                        }
                        LocalAnimeInfoRow(label = "Japonés", value = anime.titleJapanese)
                    }
                }
            }
        }

        // Información general
        LocalAnimeInfoCard(title = "Información general") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                anime.typeAnime?.let {
                    LocalAnimeInfoRow(label = "Tipo", value = it)
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )
                }
                anime.status?.let {
                    LocalAnimeInfoRow(label = "Estado", value = it)
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )
                }
                anime.totalEpisodes?.let {
                    LocalAnimeInfoRow(label = "Episodios", value = "$it")
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )
                }
                anime.duration?.let {
                    LocalAnimeInfoRow(label = "Duración", value = it)
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )
                }
                anime.season?.let {
                    LocalAnimeInfoRow(label = "Temporada", value = it)
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )
                }
                anime.year?.let {
                    LocalAnimeInfoRow(label = "Año", value = it)
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )
                }
                anime.aired?.let {
                    LocalAnimeInfoRow(label = "Transmitido", value = it)
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )
                }
                anime.rating?.let {
                    LocalAnimeInfoRow(label = "Rating", value = it)
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )
                }
                anime.source?.let {
                    LocalAnimeInfoRow(label = "Origen", value = it)
                }
            }
        }

        // Puntuaciones MAL
        if (anime.score != null || anime.scoreBy != null || anime.rank != null) {
            LocalAnimeInfoCard(title = "Puntuaciones MAL") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    anime.score?.let {
                        LocalAnimeInfoRow(label = "Puntuación", value = "$it/10")
                        if (anime.scoreBy != null || anime.rank != null) {
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                            )
                        }
                    }
                    anime.scoreBy?.let {
                        LocalAnimeInfoRow(label = "Puntuado por", value = "$it personas")
                        if (anime.rank != null) {
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                            )
                        }
                    }
                    anime.rank?.let {
                        LocalAnimeInfoRow(label = "Ranking", value = "#$it")
                    }
                }
            }
        }

        // Estudios
        if (!anime.studios.isNullOrEmpty()) {
            LocalAnimeInfoCard(title = "Estudios") {
                val chipColors = listOf(
                    Color(0xFF2196F3), Color(0xFF9C27B0), Color(0xFF00BCD4),
                    Color(0xFFFF5722), Color(0xFF4CAF50), Color(0xFFFF9800),
                    Color(0xFFE91E63), Color(0xFF009688)
                )

                val studiosList = anime.studios.split(",").map { it.trim() }
                androidx.compose.foundation.layout.FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    studiosList.forEachIndexed { index, studio ->
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
                                text = studio,
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
private fun LocalAnimeInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontFamily = PoppinsMedium,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f, fill = false)
        )
        Spacer(modifier = Modifier.width(16.dp))
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

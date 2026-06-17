package com.yumedev.seijakulist.ui.screens.detail

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import android.widget.Toast
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import java.net.URLEncoder
import coil.compose.AsyncImage
import com.yumedev.seijakulist.domain.models.AnimeCharactersDetail
import com.yumedev.seijakulist.domain.models.MangaDetail
import com.yumedev.seijakulist.ui.components.LoadingScreen
import com.yumedev.seijakulist.ui.screens.detail.components.shared.CompactGenreCard
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsMedium
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import com.yumedev.seijakulist.ui.theme.adp
import com.yumedev.seijakulist.ui.theme.asp

/**
 * Pantalla de detalle del manga
 * Diseño consistente con Material Design 3 y el resto de la app
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaDetailScreen(
    navController: NavController,
    mangaId: Int?,
    initialTab: Int = 0,
    mangaDetailViewModel: MangaDetailAniListViewModel = hiltViewModel(),
    mangaCharacterDetailViewModel: MangaCharacterDetailViewModel = hiltViewModel()
) {
    // ViewModels state
    val mangaDetail by mangaDetailViewModel.mangaDetail.collectAsState()
    val isLoading by mangaDetailViewModel.isLoading.collectAsState()
    val errorMessage by mangaDetailViewModel.errorMessage.collectAsState()

    val mangaCharacters by mangaCharacterDetailViewModel.characters.collectAsState()
    val charactersLoading by mangaCharacterDetailViewModel.isLoading.collectAsState()

    // UI State
    var selectedTab by remember { mutableStateOf(MangaDetailTab.OVERVIEW) }
    val focusManager = LocalFocusManager.current
    val listState = rememberLazyListState()

    // FAB visibility based on scroll
    val showFab by remember {
        derivedStateOf { listState.firstVisibleItemIndex >= 1 }
    }

    // Set initial tab if provided
    LaunchedEffect(initialTab) {
        selectedTab = when (initialTab) {
            1 -> MangaDetailTab.CHARACTERS
            2 -> MangaDetailTab.INFO
            else -> MangaDetailTab.OVERVIEW
        }
    }

    // Main content
    when {
        isLoading && mangaDetail == null -> {
            LoadingScreen()
        }

        errorMessage != null && mangaDetail == null -> {
            MangaErrorState(
                message = errorMessage ?: "Error desconocido",
                onRetry = { mangaId?.let { mangaDetailViewModel.loadMangaDetail(it) } },
                onBack = { navController.popBackStack() }
            )
        }

        mangaDetail != null -> {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Volver"
                                )
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background
                        ),
                        windowInsets = WindowInsets(0.dp)
                    )
                },
                floatingActionButton = {
                    AnimatedVisibility(
                        visible = showFab,
                        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                    ) {
                        ExtendedFloatingActionButton(
                            onClick = { /* TODO: Añadir a lista */ },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp) // Icono más grande
                                )
                            },
                            text = {
                                Text(
                                    text = "Añadir a lista",
                                    fontFamily = PoppinsBold,
                                    fontSize = 16.sp, // Texto más grande
                                    letterSpacing = 0.2.sp
                                )
                            },
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
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
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = { focusManager.clearFocus() })
                        }
                ) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            top = paddingValues.calculateTopPadding(),
                            bottom = paddingValues.calculateBottomPadding()
                        )
                    ) {
                        // Header con portada e información principal
                        item {
                            MangaDetailHeader(mangaDetail = mangaDetail)
                        }

                        // Stats Cards
                        item {
                            MangaStatsRow(mangaDetail = mangaDetail)
                        }

                        // Tab selector
                        item {
                            MangaDetailTabSelector(
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
                                label = "MangaTabContent"
                            ) { tab ->
                                when (tab) {
                                    MangaDetailTab.OVERVIEW -> {
                                        MangaOverviewTab(mangaDetail = mangaDetail)
                                    }
                                    MangaDetailTab.CHARACTERS -> {
                                        LaunchedEffect(Unit) {
                                            if (mangaCharacters.isEmpty() && !charactersLoading) {
                                                mangaId?.let {
                                                    mangaCharacterDetailViewModel.loadCharacters(it)
                                                }
                                            }
                                        }
                                        MangaCharactersTab(
                                            characters = mangaCharacters,
                                            isLoading = charactersLoading,
                                            navController = navController
                                        )
                                    }
                                    MangaDetailTab.INFO -> {
                                        MangaInfoTab(mangaDetail = mangaDetail)
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

// ============================================================================
// TABS
// ============================================================================

enum class MangaDetailTab {
    OVERVIEW,
    CHARACTERS,
    INFO
}

@Composable
private fun MangaDetailTabSelector(
    selectedTab: MangaDetailTab,
    onTabSelected: (MangaDetailTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(
        MangaDetailTab.OVERVIEW to Icons.Default.Description,
        MangaDetailTab.CHARACTERS to Icons.Default.People,
        MangaDetailTab.INFO to Icons.Default.Info
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
                    MangaTabItem(
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
private fun MangaTabItem(
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
private fun MangaDetailHeader(
    mangaDetail: MangaDetail?,
    modifier: Modifier = Modifier
) {
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
                    model = mangaDetail?.images ?: "",
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
                        text = mangaDetail?.title ?: "",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.asp(),
                        fontFamily = PoppinsBold,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 22.asp()
                    )

                    // Título japonés
                    if (!mangaDetail?.titleJapanese.isNullOrBlank()) {
                        Text(
                            text = mangaDetail.titleJapanese,
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
                        MangaTypeBadge(type = mangaDetail?.typeManga ?: "")

                        // Status badge
                        MangaStatusBadge(status = mangaDetail?.status ?: "")
                    }
                }

                // BOTÓN DE ACCIÓN MEJORADO
                Button(
                    onClick = { /* TODO: Añadir a lista */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp), // Más alto
                    shape = RoundedCornerShape(14.dp), // Más redondeado
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp, // Mayor elevación
                        pressedElevation = 8.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp) // Icono más grande
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Añadir",
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
// STATS ROW
// ============================================================================

@Composable
private fun MangaStatsRow(
    mangaDetail: MangaDetail?,
    modifier: Modifier = Modifier
) {
    // Lista de estadísticas con su índice para animaciones escalonadas
    val stats = remember(mangaDetail) {
        buildList {
            mangaDetail?.let {
                if (it.score != null && it.score > 0) {
                    add(
                        MangaStatData(
                            icon = Icons.Default.Star,
                            value = String.format("%.1f", it.score),
                            label = "Score",
                            type = MangaStatType.SCORE
                        )
                    )
                }
                if (it.chapters != null && it.chapters > 0) {
                    add(
                        MangaStatData(
                            icon = Icons.Default.Book,
                            value = "${it.chapters}",
                            label = "Capítulos",
                            type = MangaStatType.CHAPTERS
                        )
                    )
                }
                if (it.volumes != null && it.volumes > 0) {
                    add(
                        MangaStatData(
                            icon = Icons.Default.Collections,
                            value = "${it.volumes}",
                            label = "Volúmenes",
                            type = MangaStatType.VOLUMES
                        )
                    )
                }
                if (it.rank != null && it.rank > 0) {
                    add(
                        MangaStatData(
                            icon = Icons.Default.EmojiEvents,
                            value = "#${it.rank}",
                            label = "Ranking",
                            type = MangaStatType.RANK
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
            MangaStatCard(
                statData = stat,
                index = index,
                statsCount = stats.size,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// Clase de datos para las estadísticas del manga
private data class MangaStatData(
    val icon: ImageVector,
    val value: String,
    val label: String,
    val type: MangaStatType
)

// Enum para tipos de estadísticas del manga con colores específicos
private enum class MangaStatType {
    SCORE, CHAPTERS, VOLUMES, RANK
}

@Composable
private fun MangaStatCard(
    statData: MangaStatData,
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
        MangaStatType.SCORE -> MaterialTheme.colorScheme.primary
        MangaStatType.CHAPTERS -> MaterialTheme.colorScheme.tertiary
        MangaStatType.VOLUMES -> MaterialTheme.colorScheme.secondary
        MangaStatType.RANK -> MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)
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
private fun MangaOverviewTab(
    mangaDetail: MangaDetail?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Synopsis
        if (!mangaDetail?.synopsis.isNullOrBlank()) {
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
                                    clipboardManager.setText(AnnotatedString(mangaDetail.synopsis))
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
                                    val synopsis = mangaDetail.synopsis
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
                        text = mangaDetail.synopsis,
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
        if (!mangaDetail?.genres.isNullOrEmpty()) {
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
                        items(mangaDetail.genres.filterNotNull()) { genre ->
                            CompactGenreCard(genreName = genre.name ?: "")
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }

        // Authors
        if (!mangaDetail?.authors.isNullOrEmpty()) {
            MangaInfoCard(title = "Autores") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    mangaDetail.authors.forEach { author ->
                        MangaAuthorRow(
                            name = author.name,
                            role = author.role
                        )
                    }
                }
            }
        }

        // Background
        if (!mangaDetail?.background.isNullOrBlank()) {
            MangaInfoCard(title = "Contexto") {
                var expanded by remember { mutableStateOf(false) }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = mangaDetail.background,
                        fontFamily = PoppinsRegular,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = if (expanded) Int.MAX_VALUE else 4,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (mangaDetail.background.length > 150) {
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
private fun MangaCharactersTab(
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
                MangaEmptyState(message = "No hay personajes disponibles")
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.heightIn(max = 2000.dp),
                    userScrollEnabled = false
                ) {
                    items(characters, key = { it.idCharacter ?: 0 }) { character ->
                        MangaCharacterCard(
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
private fun MangaInfoTab(
    mangaDetail: MangaDetail?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Basic info
        MangaInfoRow("Título en inglés", mangaDetail?.titleEnglish?.ifBlank { "—" } ?: "—")
        MangaInfoRow("Título japonés", mangaDetail?.titleJapanese?.ifBlank { "—" } ?: "—")
        MangaInfoRow("Tipo", formatMangaType(mangaDetail?.typeManga ?: ""))
        MangaInfoRow("Estado", formatMangaStatus(mangaDetail?.status ?: ""))

        if (mangaDetail?.chapters != null && mangaDetail.chapters > 0) {
            MangaInfoRow("Capítulos", "${mangaDetail.chapters}")
        }

        if (mangaDetail?.volumes != null && mangaDetail.volumes > 0) {
            MangaInfoRow("Volúmenes", "${mangaDetail.volumes}")
        }

        if (!mangaDetail?.published.isNullOrBlank()) {
            MangaInfoRow("Publicación", mangaDetail.published)
        }

        if (mangaDetail?.score != null && mangaDetail.score > 0) {
            MangaInfoRow("Puntuación", "${mangaDetail.score}/10")
        }

        if (mangaDetail?.scoreBy != null && mangaDetail.scoreBy > 0) {
            MangaInfoRow("Valoraciones", "${mangaDetail.scoreBy} usuarios")
        }

        // Serializations
        if (!mangaDetail?.serializations.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            MangaInfoCard(title = "Publicado en") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    mangaDetail.serializations.forEach { serialization ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Newspaper,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = serialization.name,
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
private fun MangaInfoCard(
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
private fun MangaGenreChip(genre: String) {
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
private fun MangaAuthorRow(
    name: String,
    role: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                fontFamily = PoppinsBold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = role,
                fontFamily = PoppinsRegular,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Icon(
            imageVector = when {
                role.contains("Art", ignoreCase = true) -> Icons.Default.Brush
                role.contains("Story", ignoreCase = true) -> Icons.Default.Create
                else -> Icons.Default.Person
            },
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun MangaCharacterCard(
    character: AnimeCharactersDetail,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Character image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.75f)
            ) {
                AsyncImage(
                    model = character.imageCharacter?.jpg?.imageUrl,
                    contentDescription = character.nameCharacter,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Role badge
                if (character.role.isNotBlank()) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                        shape = RoundedCornerShape(6.dp),
                        color = when (character.role.lowercase()) {
                            "main" -> MaterialTheme.colorScheme.primary
                            "supporting" -> MaterialTheme.colorScheme.secondary
                            else -> MaterialTheme.colorScheme.tertiary
                        }
                    ) {
                        Text(
                            text = when (character.role.lowercase()) {
                                "main" -> "MAIN"
                                "supporting" -> "SUPP"
                                else -> character.role.take(4).uppercase()
                            },
                            fontFamily = PoppinsBold,
                            fontSize = 9.sp,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            // Character name
            Text(
                text = character.nameCharacter ?: "Unknown",
                fontFamily = PoppinsMedium,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@Composable
private fun MangaInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
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
private fun MangaTypeBadge(type: String) {
    if (type.isBlank()) return

    // Mapeo de tipos de manga con colores específicos
    val (icon, color) = when (type) {
        "MANGA" -> Icons.Default.Book to Color(0xFF2196F3) // Azul
        "NOVEL" -> Icons.Default.MenuBook to Color(0xFF9C27B0) // Púrpura
        "ONE_SHOT" -> Icons.Default.Description to Color(0xFF00BCD4) // Cian
        "MANHWA" -> Icons.Default.Book to Color(0xFFFF5722) // Naranja rojizo (Corea)
        "MANHUA" -> Icons.Default.Book to Color(0xFFF44336) // Rojo (China)
        else -> Icons.Default.Book to Color(0xFF2196F3) // Azul por defecto
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
                text = formatMangaType(type),
                fontFamily = PoppinsBold,
                fontSize = 11.sp,
                color = color,
                letterSpacing = 0.3.sp
            )
        }
    }
}

@Composable
private fun MangaStatusBadge(status: String) {
    if (status.isBlank()) return

    // Mapeo completo de todos los estados posibles de AniList API para manga
    val (text, icon, color) = when (status) {
        "FINISHED" -> Triple(
            "Completado",
            Icons.Default.CheckCircle,
            Color(0xFF4CAF50) // Verde - completado
        )
        "RELEASING" -> Triple(
            "En publicación",
            Icons.Default.FiberManualRecord,
            Color(0xFF2196F3) // Azul - activo
        )
        "NOT_YET_RELEASED" -> Triple(
            "Próximamente",
            Icons.Default.Schedule,
            Color(0xFF9C27B0) // Púrpura - futuro
        )
        "CANCELLED" -> Triple(
            "Cancelado",
            Icons.Default.Cancel,
            Color(0xFFF44336) // Rojo - error/cancelado
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
private fun MangaEmptyState(
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
private fun MangaErrorState(
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

private fun formatMangaType(type: String): String = when (type) {
    "MANGA" -> "Manga"
    "NOVEL" -> "Novela"
    "ONE_SHOT" -> "One-shot"
    "MANHWA" -> "Manhwa"
    "MANHUA" -> "Manhua"
    "DOUJINSHI" -> "Doujinshi"
    else -> type.lowercase().replaceFirstChar { it.uppercase() }
}

private fun formatMangaStatus(status: String): String = when (status) {
    "FINISHED" -> "Finalizado"
    "RELEASING" -> "En publicación"
    "NOT_YET_RELEASED" -> "Próximamente"
    "CANCELLED" -> "Cancelado"
    "HIATUS" -> "En pausa"
    else -> status
}

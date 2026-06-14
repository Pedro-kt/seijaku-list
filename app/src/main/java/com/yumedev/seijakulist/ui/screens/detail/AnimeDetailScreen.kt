package com.yumedev.seijakulist.ui.screens.detail

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yumedev.seijakulist.domain.models.AnimeDetail
import com.yumedev.seijakulist.ui.components.LoadingScreen
import com.yumedev.seijakulist.ui.screens.detail.components.header.AnimeDetailHeader
import com.yumedev.seijakulist.ui.screens.detail.components.tabs.*
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.adp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Pantalla de detalle del anime refactorizada
 *
 * Estructura modular:
 * - Header hero con imagen y información principal
 * - Sistema de tabs para organizar el contenido
 * - Tabs: Overview, Characters, Episodes, Production
 * - FAB para añadir/editar en lista
 * - Bottom sheet para gestionar lista
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun AnimeDetailScreen(
    navController: NavController,
    animeId: Int?,
    initialTab: Int = 0,
    openSheet: Boolean = false,
    animeDetailViewModel: AnimeDetailViewModel = hiltViewModel(),
    animeCharacterDetailViewModel: AnimeCharacterDetailViewModel = hiltViewModel(),
    animeThemesViewModel: AnimeThemesViewModel = hiltViewModel(),
    producerDetailViewModel: ProducerDetailViewModel = hiltViewModel(),
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null
) {
    // ViewModels state
    val animeDetail by animeDetailViewModel.animeDetail.collectAsState()
    val isLoading by animeDetailViewModel.isLoading.collectAsState()
    val errorMessage by animeDetailViewModel.errorMessage.collectAsState()

    val animeCharactersDetail by animeCharacterDetailViewModel.characters.collectAsState()
    val characterIsLoading by animeCharacterDetailViewModel.isLoading.collectAsState()
    val characterErrorMessage by animeCharacterDetailViewModel.errorMessage.collectAsState()

    val animeEpisodes by animeDetailViewModel.animeEpisodes.collectAsState()
    val isEpisodesLoading by animeDetailViewModel.isEpisodesLoading.collectAsState()
    val hasMoreEpisodes by animeDetailViewModel.hasMoreEpisodes.collectAsState()

    val isAdded by animeDetailViewModel.isAdded.collectAsState()
    val existingAnime by animeDetailViewModel.existingAnime.collectAsState()

    // UI State
    var selectedTab by rememberSaveable { mutableStateOf(AnimeDetailTab.OVERVIEW) }
    var showAddToListSheet by remember { mutableStateOf(false) }
    val addToListSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val lazyListState = rememberLazyListState()

    // FAB visibility based on scroll
    val showFab by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex >= 1 }
    }

    // Initialize data
    LaunchedEffect(animeId) {
        if (animeId != null) {
            animeThemesViewModel.animeThemes(animeId)
        }
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
            2 -> AnimeDetailTab.EPISODES
            3 -> AnimeDetailTab.PRODUCTION
            else -> AnimeDetailTab.OVERVIEW
        }
    }

    val displayImageUrl = animeDetail?.images ?: ""

    // Main content
    when {
        isLoading && animeDetail == null -> {
            LoadingScreen()
        }

        errorMessage != null && animeDetail == null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorMessage ?: "Error desconocido",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        animeDetail != null -> {
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
                    SnackbarHost(hostState = snackbarHostState)
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
                        state = lazyListState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            top = paddingValues.calculateTopPadding(),
                            bottom = paddingValues.calculateBottomPadding()
                        )
                    ) {
                        // Header
                        item {
                            AnimeDetailHeader(
                                animeDetail = animeDetail,
                                displayImageUrl = displayImageUrl,
                                isAdded = isAdded,
                                onAddToListClick = { showAddToListSheet = true },
                                sharedTransitionScope = sharedTransitionScope,
                                animatedVisibilityScope = animatedVisibilityScope,
                                animeId = animeId
                            )
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
                                label = "TabContent"
                            ) { tab ->
                                when (tab) {
                                    AnimeDetailTab.OVERVIEW -> {
                                        OverviewTab(animeDetail = animeDetail)
                                    }

                                    AnimeDetailTab.CHARACTERS -> {
                                        LaunchedEffect(Unit) {
                                            if (animeCharactersDetail.isEmpty() && !characterIsLoading) {
                                                animeId?.let {
                                                    animeCharacterDetailViewModel.loadAnimeCharacters(
                                                        it
                                                    )
                                                }
                                            }
                                        }
                                        CharactersTab(
                                            characters = animeCharactersDetail,
                                            isLoading = characterIsLoading,
                                            errorMessage = characterErrorMessage,
                                            navController = navController
                                        )
                                    }

                                    AnimeDetailTab.EPISODES -> {
                                        LaunchedEffect(Unit) {
                                            if (animeEpisodes.isEmpty() && !isEpisodesLoading) {
                                                animeId?.let {
                                                    animeDetailViewModel.loadAnimeEpisodes(it, false)
                                                }
                                            }
                                        }
                                        EpisodesTab(
                                            episodes = animeEpisodes,
                                            isLoading = isEpisodesLoading,
                                            hasMore = hasMoreEpisodes,
                                            onLoadMore = {
                                                animeId?.let {
                                                    animeDetailViewModel.loadAnimeEpisodes(it, true)
                                                }
                                            },
                                            onEpisodeClick = { episode ->
                                                // TODO: Mostrar detalles del episodio
                                            }
                                        )
                                    }

                                    AnimeDetailTab.PRODUCTION -> {
                                        ProductionTab(animeDetail = animeDetail)
                                    }
                                }
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

/**
 * Contenido del bottom sheet para añadir/editar anime en lista
 * TODO: Implementar la UI completa del bottom sheet
 */
@Composable
private fun AddToListBottomSheetContent(
    anime: AnimeDetail?,
    existingAnime: com.yumedev.seijakulist.domain.models.AnimeEntityDomain?,
    isAdded: Boolean,
    onSave: (String?, Float, Long?, Long?, String?, String) -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    // TODO: Implementar UI completa del bottom sheet
    // Por ahora, placeholder simple
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = if (isAdded) "Editar en lista" else "Añadir a lista",
            style = MaterialTheme.typography.headlineSmall,
            fontFamily = PoppinsBold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Funcionalidad de bottom sheet pendiente de implementación completa",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(100.dp))
    }
}

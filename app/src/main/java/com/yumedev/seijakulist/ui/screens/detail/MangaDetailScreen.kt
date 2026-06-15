package com.yumedev.seijakulist.ui.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yumedev.seijakulist.domain.models.MangaDetail
import com.yumedev.seijakulist.ui.components.LoadingScreen
import com.yumedev.seijakulist.ui.screens.detail.components.header.MangaDetailHeader
import com.yumedev.seijakulist.ui.screens.detail.components.tabs.*
import com.yumedev.seijakulist.ui.theme.PoppinsBold

/**
 * Pantalla de detalle del manga
 *
 * Estructura modular similar a AnimeDetailScreen:
 * - Header hero con imagen y información principal
 * - Sistema de tabs para organizar el contenido
 * - Tabs: Overview, Characters
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

    val mangaCharactersDetail by mangaCharacterDetailViewModel.characters.collectAsState()
    val characterIsLoading by mangaCharacterDetailViewModel.isLoading.collectAsState()

    // UI State
    var selectedTab by remember { mutableStateOf(MangaDetailTab.OVERVIEW) }
    val focusManager = LocalFocusManager.current
    val lazyListState = rememberLazyListState()

    // Set initial tab if provided
    LaunchedEffect(initialTab) {
        selectedTab = when (initialTab) {
            1 -> MangaDetailTab.CHARACTERS
            else -> MangaDetailTab.OVERVIEW
        }
    }

    val displayImageUrl = mangaDetail?.images ?: ""

    // Main content
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
    ) {
        when {
            isLoading && mangaDetail == null -> {
                LoadingScreen()
            }
            errorMessage != null && mangaDetail == null -> {
                ErrorMangaDetailContent(
                    errorMessage = errorMessage ?: "Error desconocido",
                    onRetry = { mangaId?.let { mangaDetailViewModel.loadMangaDetail(it) } },
                    onBack = { navController.popBackStack() }
                )
            }
            mangaDetail != null -> {
                MangaDetailContent(
                    mangaDetail = mangaDetail!!,
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    mangaCharactersDetail = mangaCharactersDetail,
                    characterIsLoading = characterIsLoading,
                    lazyListState = lazyListState,
                    navController = navController,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

/**
 * Contenido principal del detalle del manga
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MangaDetailContent(
    mangaDetail: MangaDetail,
    selectedTab: MangaDetailTab,
    onTabSelected: (MangaDetailTab) -> Unit,
    mangaCharactersDetail: List<com.yumedev.seijakulist.domain.models.AnimeCharactersDetail>,
    characterIsLoading: Boolean,
    lazyListState: androidx.compose.foundation.lazy.LazyListState,
    navController: NavController,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = mangaDetail.title,
                        fontFamily = PoppinsBold,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header con imagen y info principal
            item {
                MangaDetailHeader(mangaDetail = mangaDetail)
            }

            // Tabs
            item {
                MangaDetailTabs(
                    selectedTab = selectedTab,
                    onTabSelected = onTabSelected
                )
            }

            // Tab Content
            item {
                when (selectedTab) {
                    MangaDetailTab.OVERVIEW -> {
                        MangaOverviewTab(mangaDetail = mangaDetail)
                    }
                    MangaDetailTab.CHARACTERS -> {
                        CharactersTab(
                            characters = mangaCharactersDetail,
                            isLoading = characterIsLoading,
                            errorMessage = null,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

/**
 * Pantalla de error para detalles del manga
 */
@Composable
private fun ErrorMangaDetailContent(
    errorMessage: String = "Error desconocido",
    onRetry: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = onRetry) {
                Text("Reintentar")
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(onClick = onBack) {
                Text("Volver")
            }
        }
    }
}

/**
 * Enum para las tabs del detalle del manga
 */
enum class MangaDetailTab {
    OVERVIEW,
    CHARACTERS
}

/**
 * Componente de tabs para el detalle del manga
 */
@Composable
private fun MangaDetailTabs(
    selectedTab: MangaDetailTab,
    onTabSelected: (MangaDetailTab) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTab.ordinal,
        modifier = Modifier.fillMaxWidth()
    ) {
        Tab(
            selected = selectedTab == MangaDetailTab.OVERVIEW,
            onClick = { onTabSelected(MangaDetailTab.OVERVIEW) },
            text = { Text("Resumen") }
        )
        Tab(
            selected = selectedTab == MangaDetailTab.CHARACTERS,
            onClick = { onTabSelected(MangaDetailTab.CHARACTERS) },
            text = { Text("Personajes") }
        )
    }
}

package com.yumedev.seijakulist.ui.screens.my_mangas

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.yumedev.seijakulist.data.local.entities.MangaEntity
import com.yumedev.seijakulist.ui.components.CustomDialog
import com.yumedev.seijakulist.ui.components.DeleteMyAnime
import com.yumedev.seijakulist.ui.components.DialogType
import com.yumedev.seijakulist.ui.components.ViewMode
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import com.yumedev.seijakulist.ui.theme.asp
import com.yumedev.seijakulist.ui.theme.adp
import kotlinx.coroutines.launch

@Composable
fun MyMangasScreen(
    navController: NavController,
    viewModel: MyMangasViewModel = hiltViewModel(),
    isSearching: Boolean,
    onDismissSearch: () -> Unit,
    viewMode: ViewMode = ViewMode.GRID,
    sortOrder: com.yumedev.seijakulist.ui.components.SortOrder = com.yumedev.seijakulist.ui.components.SortOrder.NONE
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val savedMangas by viewModel.savedMangas.collectAsState()

    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val context = androidx.compose.ui.platform.LocalContext.current

    val statusManga = listOf("Leyendo", "Completado", "Pausado", "Abandonado", "Planeado")

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var mangaIdToDelete by remember { mutableStateOf(0) }

    // Estado de scroll para vista GRID
    val gridState = rememberLazyGridState()

    // Determinar si mostrar el botón de "volver arriba"
    val showScrollToTopButton by remember {
        derivedStateOf {
            gridState.firstVisibleItemIndex > 3
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            focusManager.clearFocus()
                        }
                    )
                }
        ) {
            when {
                isLoading -> {
                    LoadingContent()
                }
                savedMangas.isEmpty() -> {
                    EmptyMangaListState(
                        onNavigateToSearch = {
                            navController.navigate(AppDestinations.SEARCH_MANGA_ROUTE)
                        }
                    )
                }
                else -> {
                    // Búsqueda
                    AnimatedContent(
                        targetState = isSearching,
                        transitionSpec = {
                            (slideInVertically(animationSpec = tween(500)) + fadeIn())
                                .togetherWith(slideOutVertically(animationSpec = tween(500)) + fadeOut())
                                .using(SizeTransform(clip = false))
                        },
                        label = ""
                    ) { targetIsSearching ->
                        if (targetIsSearching) {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                placeholder = { Text("Buscar manga...") },
                                trailingIcon = {
                                    IconButton(onClick = {
                                        onDismissSearch()
                                        searchQuery = ""
                                    }) {
                                        Icon(Icons.Default.Close, contentDescription = "Cerrar búsqueda")
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                singleLine = true,
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = Color.Transparent,
                                    cursorColor = MaterialTheme.colorScheme.primary,
                                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                ),
                            )
                        } else {
                            Spacer(modifier = Modifier.height(0.dp))
                        }
                    }

                    // Calcular la cantidad de mangas por estado
                    val mangaCounts = remember(savedMangas) {
                        statusManga.associateWith { status ->
                            savedMangas.count { it.statusUser == status }
                        }
                    }

                    // Filtros
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(statusManga.size) { index ->
                            val filter = statusManga[index]
                            val isSelected = selectedFilter == filter
                            val count = mangaCounts[filter] ?: 0

                            val filterIcon = when (filter) {
                                "Leyendo" -> Icons.Default.MenuBook
                                "Completado" -> Icons.Default.CheckCircle
                                "Pausado" -> Icons.Default.Pause
                                "Abandonado" -> Icons.Default.Stop
                                "Planeado" -> Icons.Default.EventAvailable
                                else -> Icons.Default.Star
                            }

                            Surface(
                                onClick = {
                                    selectedFilter = if (isSelected) null else filter
                                },
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                else MaterialTheme.colorScheme.surfaceContainerHigh,
                                border = if (isSelected)
                                    BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                                else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = filterIcon,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.adp()),
                                        tint = if (isSelected) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                    Text(
                                        text = filter,
                                        fontFamily = if (isSelected) PoppinsBold else PoppinsRegular,
                                        fontSize = 13.asp(),
                                        color = if (isSelected) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                    )
                                    Text(
                                        text = count.toString(),
                                        fontFamily = if (isSelected) PoppinsBold else PoppinsRegular,
                                        fontSize = 12.asp(),
                                        color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                    )
                                }
                            }
                        }
                    }

                    // Mangas filtrados
                    val displayedMangas = remember(savedMangas, searchQuery, selectedFilter, sortOrder) {
                        val filtered = savedMangas.filter { manga ->
                            val matchesSearch = searchQuery.isBlank() || manga.title.contains(searchQuery, ignoreCase = true)
                            val matchesFilter = selectedFilter == null || manga.statusUser == selectedFilter
                            matchesSearch && matchesFilter
                        }

                        when (sortOrder) {
                            com.yumedev.seijakulist.ui.components.SortOrder.A_TO_Z -> filtered.sortedBy { it.title.lowercase() }
                            com.yumedev.seijakulist.ui.components.SortOrder.Z_TO_A -> filtered.sortedByDescending { it.title.lowercase() }
                            com.yumedev.seijakulist.ui.components.SortOrder.NONE -> filtered.sortedWith(
                                compareByDescending { it.statusUser == "Leyendo" }
                            )
                        }
                    }

                    // Empty state con filtro activo
                    if (displayedMangas.isEmpty() && (selectedFilter != null || searchQuery.isNotBlank())) {
                        EmptyStateByFilter(selectedFilter = selectedFilter)
                    } else {
                        // Solo vista GRID por ahora
                        LazyVerticalGrid(
                            state = gridState,
                            columns = GridCells.Fixed(3),
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                top = 8.dp,
                                bottom = 116.dp
                            )
                        ) {
                            items(displayedMangas) { manga ->
                                CompactMangaCard(
                                    manga = manga,
                                    statusColor = getMangaStatusColor(manga.statusUser),
                                    onMangaClick = {
                                        navController.navigate("${AppDestinations.MANGA_DETAIL_ROUTE}/${manga.malId}")
                                    },
                                    onDeleteConfirmed = {
                                        showDialog = true
                                        mangaIdToDelete = manga.malId
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Botón flotante de "volver arriba"
        if (!isLoading && savedMangas.isNotEmpty()) {
            AnimatedVisibility(
                visible = showScrollToTopButton,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 }),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 90.dp)
            ) {
                Surface(
                    onClick = {
                        scope.launch {
                            gridState.animateScrollToItem(0)
                        }
                    },
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shadowElevation = 6.dp,
                    tonalElevation = 3.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Volver arriba",
                            fontFamily = PoppinsBold,
                            fontSize = 13.asp(),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }

    // Modal de confirmación de eliminación
    if (showDialog) {
        CustomDialog(
            onDismissRequest = {
                showDialog = false
            },
            onConfirm = {
                viewModel.deleteManga(mangaIdToDelete)
                android.widget.Toast.makeText(
                    context,
                    "Manga eliminado de tu lista",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            },
            onDismiss = {
                // Solo cierra el diálogo
            },
            title = "Confirmar eliminación",
            message = "¿Estás seguro de que quieres eliminar este manga de tu lista?\n\nUna vez eliminado tendrás que volver a agregarlo de nuevo a tu lista.",
            confirmButtonText = "Eliminar",
            dismissButtonText = "Cancelar",
            type = DialogType.DELETE
        )
    }
}

@Composable
fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text("Cargando mangas...", fontFamily = PoppinsRegular)
        }
    }
}

@Composable
fun EmptyMangaListState(
    onNavigateToSearch: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "¡Tu lista está vacía!",
                fontFamily = PoppinsBold,
                fontSize = 28.asp(),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Comienza a construir tu colección de mangas favoritos. Busca y agrega tus primeros títulos para empezar a llevar el control de lo que lees.",
                fontFamily = PoppinsRegular,
                fontSize = 16.asp(),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                lineHeight = 24.asp()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onNavigateToSearch,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(56.adp()),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.size(24.adp())
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Buscar manga",
                    fontFamily = PoppinsBold,
                    fontSize = 18.asp()
                )
            }

            Text(
                text = "Explora miles de títulos disponibles",
                fontFamily = PoppinsRegular,
                fontSize = 14.asp(),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun EmptyStateByFilter(selectedFilter: String?) {
    val (title, description) = when (selectedFilter) {
        "Leyendo" -> Pair(
            "No estás leyendo ningún manga",
            "Comienza a leer un manga marcándolo como 'Leyendo' o busca nuevos títulos para agregar a tu lista."
        )
        "Completado" -> Pair(
            "Aún no has completado ningún manga",
            "Cuando termines de leer un manga aparecerá aquí. ¡Sigue disfrutando de tus lecturas!"
        )
        "Pausado" -> Pair(
            "No tienes mangas pausados",
            "Marca mangas como 'Pausado' cuando quieras leerlos pero aún no sea el momento indicado."
        )
        "Abandonado" -> Pair(
            "No has abandonado ningún manga",
            "Los mangas que decidas dejar de leer aparecerán aquí. ¡Esperemos que no sean muchos!"
        )
        "Planeado" -> Pair(
            "No tienes mangas planeados",
            "Planea los mangas que quieres leer próximamente y mantenlos organizados."
        )
        else -> Pair(
            "No se encontraron resultados",
            "Intenta ajustar tu búsqueda o filtros para encontrar lo que buscas."
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontFamily = PoppinsBold,
                fontSize = 22.asp(),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Text(
                text = description,
                fontFamily = PoppinsRegular,
                fontSize = 15.asp(),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                lineHeight = 22.asp()
            )
        }
    }
}

@Composable
fun CompactMangaCard(
    manga: MangaEntity,
    statusColor: Color,
    onMangaClick: () -> Unit,
    onDeleteConfirmed: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "compact_card_scale"
    )

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.adp())
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onMangaClick() }
                )
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Imagen
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.adp())
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(manga.imageUrl),
                        contentDescription = manga.title,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                // Contenido inferior
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(8.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = manga.title,
                        fontFamily = PoppinsBold,
                        fontSize = 12.asp(),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 14.asp()
                    )

                    // Progreso y score
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${manga.chaptersRead}/${manga.chapters ?: "?"}",
                                fontFamily = PoppinsRegular,
                                fontSize = 10.asp(),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            if (manga.userScore > 0f) {
                                Text(
                                    text = "•",
                                    fontFamily = PoppinsRegular,
                                    fontSize = 10.asp(),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                )
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(10.dp)
                                )
                                Text(
                                    text = String.format("%.1f", manga.userScore),
                                    fontFamily = PoppinsBold,
                                    fontSize = 10.asp(),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }

                        Surface(
                            shape = CircleShape,
                            color = statusColor.copy(alpha = 0.2f),
                            modifier = Modifier.size(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(1.dp)
                                    .background(statusColor, CircleShape)
                            )
                        }
                    }
                }
            }

            // Botón de eliminar
            DeleteMyAnime(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(6.dp),
                onDeleteConfirmed = onDeleteConfirmed
            )
        }
    }
}

@Composable
fun MangaListItem(
    manga: MangaEntity,
    onClick: () -> Unit,
    onUpdateProgress: (Int, Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Image
            AsyncImage(
                model = manga.imageUrl,
                contentDescription = manga.title,
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight(),
                contentScale = ContentScale.Crop
            )

            // Info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = manga.title,
                        fontFamily = PoppinsBold,
                        fontSize = 14.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Status chip
                        Surface(
                            color = getMangaStatusColor(manga.statusUser),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = manga.statusUser,
                                fontSize = 10.sp,
                                color = Color.White,
                                fontFamily = PoppinsRegular,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        // Type
                        Text(
                            text = manga.typeManga,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = PoppinsRegular
                        )
                    }
                }

                // Progress
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Capítulos: ${manga.chaptersRead}/${manga.chapters ?: "?"}",
                            fontSize = 11.sp,
                            fontFamily = PoppinsRegular,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (manga.userScore > 0) {
                            Text(
                                text = "★ ${manga.userScore}",
                                fontSize = 11.sp,
                                fontFamily = PoppinsBold,
                                color = Color(0xFFFFA000)
                            )
                        }
                    }

                    // +1 button
                    if (manga.statusUser == "Leyendo" && manga.chapters != null && manga.chaptersRead < manga.chapters) {
                        FilledTonalButton(
                            onClick = {
                                onUpdateProgress(manga.malId, manga.chaptersRead + 1)
                            },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("+1", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun getMangaStatusColor(status: String): Color {
    val isDarkTheme = androidx.compose.foundation.isSystemInDarkTheme()

    // Mapear estados de manga a estados de anime para usar los mismos colores
    val mappedStatus = when (status) {
        "Leyendo" -> "Viendo"
        "Pausado" -> "Pendiente"
        else -> status // "Completado", "Planeado", "Abandonado" son iguales
    }

    return com.yumedev.seijakulist.ui.theme.getAnimeStatusColor(mappedStatus, isDarkTheme)
}

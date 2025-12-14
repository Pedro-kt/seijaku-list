package com.yumedev.seijakulist.ui.screens.my_animes

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.yumedev.seijakulist.R
import com.yumedev.seijakulist.ui.components.AnimeStatusChip
import com.yumedev.seijakulist.ui.components.CustomDialog
import com.yumedev.seijakulist.ui.components.DeleteMyAnime
import com.yumedev.seijakulist.ui.components.DialogType
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import kotlinx.coroutines.launch

@Composable
fun MyAnimeListScreen(
    navController: NavController,
    viewModel: MyAnimeListViewModel = hiltViewModel(),
    isSearching: Boolean,
    onDismissSearch: () -> Unit,
    isGridView: Boolean = false,
    sortOrder: com.yumedev.seijakulist.ui.components.SortOrder = com.yumedev.seijakulist.ui.components.SortOrder.NONE
) {
    val savedAnimes by viewModel.savedAnimes.collectAsState()
    val savedAnimeStatusComplete by viewModel.savedAnimeStatusComplete.collectAsState()
    val savedAnimeStatusWatching by viewModel.savedAnimeStatusWatching.collectAsState()
    val savedAnimeStatusPending by viewModel.savedAnimeStatusPending.collectAsState()
    val savedAnimeStatusAbandoned by viewModel.savedAnimeStatusAbandoned.collectAsState()
    val savedAnimeStatusPlanned by viewModel.savedAnimeStatusPlanned.collectAsState()

    val RobotoRegular = FontFamily(
        Font(R.font.roboto_regular)
    )
    val RobotoBold = FontFamily(
        Font(R.font.roboto_bold, FontWeight.Bold)
    )

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val statusAnime = listOf("Viendo", "Completado", "Pendiente", "Abandonado", "Planeado")

    var collapsedFilter by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    var searchQuery by remember { mutableStateOf("") }
    var collapsedSearch by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    var isSortedAscending by remember { mutableStateOf(false) }

    val statusColors = mapOf(
        "Viendo" to Color(0xFF66BB6A),
        "Completado" to Color(0xFF42A5F5),
        "Pendiente" to Color(0xFFFFCA28),
        "Abandonado" to Color(0xFFEF5350),
        "Planeado" to Color(0xFF78909C)
    )

    LaunchedEffect(collapsedSearch) {
        if (collapsedSearch) {
            focusRequester.requestFocus()
        }
    }
    var showDialogStatus by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var animeIdToDelete by remember { mutableStateOf(0) }
    var selectedFilter by remember { mutableStateOf<String?>(null) }

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
        if (savedAnimes.isEmpty()) {
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
                    // Título principal
                    Text(
                        text = "¡Tu lista está vacía!",
                        fontFamily = RobotoBold,
                        fontSize = 28.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )

                    // Descripción
                    Text(
                        text = "Comienza a construir tu colección de animes favoritos. Busca y agrega tus primeros títulos para empezar a llevar el control de lo que ves.",
                        fontFamily = RobotoRegular,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Botón mejorado
                    Button(
                        onClick = {
                            navController.navigate(AppDestinations.SEARCH_ANIME_ROUTE)
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(56.dp),
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
                            imageVector = Icons.Default.AddCircleOutline,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Buscar anime",
                            fontFamily = RobotoBold,
                            fontSize = 18.sp
                        )
                    }

                    // Texto secundario
                    Text(
                        text = "Explora miles de títulos disponibles",
                        fontFamily = RobotoRegular,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {

            AnimatedContent(
                targetState = isSearching,
                transitionSpec = {
                    // Anima la entrada y salida del contenido.
                    (slideInVertically(animationSpec = tween(500)) + fadeIn())
                        .togetherWith(slideOutVertically(animationSpec = tween(500)) + fadeOut())
                        .using(
                            // Anima el tamaño del contenedor principal para un efecto más suave.
                            SizeTransform(clip = false)
                        )
                }
            ) { targetIsSearching ->
                if (targetIsSearching) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Buscar anime...") },
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
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = 0.7f
                            )
                        ),
                    )
                } else {
                    Spacer(modifier = Modifier.height(0.dp))
                }
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(statusAnime.size) { index ->
                    val filter = statusAnime[index]
                    val isSelected = selectedFilter == filter

                    val filterIcon = when(filter) {
                        "Viendo" -> Icons.Default.RemoveRedEye
                        "Completado" -> Icons.Default.CheckCircle
                        "Pendiente" -> Icons.Default.Schedule
                        "Abandonado" -> Icons.Default.Stop
                        "Planeado" -> Icons.Default.EventAvailable
                        else -> Icons.Default.Star
                    }

                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedFilter = if (isSelected) null else filter
                        },
                        label = {
                            Text(
                                text = filter,
                                fontFamily = if (isSelected) RobotoBold else RobotoRegular,
                                fontSize = 14.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = filterIcon,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            labelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            iconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = Color.Transparent,
                            selectedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            borderWidth = 0.dp,
                            selectedBorderWidth = 1.dp
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            val displayedAnimes = remember(savedAnimes, searchQuery, selectedFilter, sortOrder) {
                val filtered = savedAnimes.filter { anime ->
                    (searchQuery.isBlank() || anime.title.contains(
                        searchQuery,
                        ignoreCase = true
                    )) &&
                            (selectedFilter == null || anime.statusUser == selectedFilter)
                }

                when (sortOrder) {
                    com.yumedev.seijakulist.ui.components.SortOrder.A_TO_Z -> filtered.sortedBy { it.title.lowercase() }
                    com.yumedev.seijakulist.ui.components.SortOrder.Z_TO_A -> filtered.sortedByDescending { it.title.lowercase() }
                    com.yumedev.seijakulist.ui.components.SortOrder.NONE -> filtered
                }
            }

            if (isGridView) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
                ) {
                    items(displayedAnimes) { anime ->
                        CompactAnimeCard(
                            anime = anime,
                            statusColor = statusColors[anime.statusUser] ?: Color.Gray,
                            onAnimeClick = {
                                navController.navigate("${AppDestinations.ANIME_DETAIL_LOCAL_ROUTE}/${anime.malId}")
                            },
                            onDeleteConfirmed = {
                                showDialog = true
                                animeIdToDelete = anime.malId
                            },
                            viewModel = viewModel
                        )
                    }
                }
            } else {
                LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
            ) {
                items(displayedAnimes) { anime ->
                    var visible by rememberSaveable { mutableStateOf(false) }
                    var isPressed by remember { mutableStateOf(false) }

                    LaunchedEffect(Unit) {
                        visible = true
                    }

                    val scale by animateFloatAsState(
                        targetValue = if (isPressed) 0.97f else 1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        ),
                        label = "card_scale"
                    )

                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(animationSpec = tween(500)) + slideInHorizontally(
                            animationSpec = tween(500)
                        )
                    ) {
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
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
                                        onTap = {
                                            navController.navigate("${AppDestinations.ANIME_DETAIL_LOCAL_ROUTE}/${anime.malId}")
                                        }
                                    )
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                            ),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 3.dp,
                                pressedElevation = 6.dp
                            )
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Row(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    // Imagen compacta
                                    Box(
                                        modifier = Modifier
                                            .width(110.dp)
                                            .fillMaxHeight()
                                    ) {
                                        Image(
                                            painter = rememberAsyncImagePainter(anime.imageUrl),
                                            contentDescription = anime.title,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)),
                                            contentScale = ContentScale.Crop
                                        )

                                        // Score badge compacto
                                        Surface(
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .padding(4.dp),
                                            shape = RoundedCornerShape(6.dp),
                                            color = Color(0xFF1E1E1E).copy(alpha = 0.9f)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                                                horizontalArrangement = Arrangement.spacedBy(2.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Star,
                                                    contentDescription = null,
                                                    tint = Color(0xFFFFD700),
                                                    modifier = Modifier.size(10.dp)
                                                )
                                                Text(
                                                    text = String.format("%.1f", anime.userScore),
                                                    color = Color.White,
                                                    fontSize = 10.sp,
                                                    fontFamily = RobotoBold
                                                )
                                            }
                                        }
                                    }

                                    // Contenido compacto
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                            .padding(start = 12.dp, top = 10.dp, bottom = 10.dp, end = 8.dp),
                                        verticalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        // Título y estado
                                        Column(
                                            verticalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(
                                                text = anime.title,
                                                fontFamily = RobotoBold,
                                                fontSize = 14.sp,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis
                                            )

                                            // Status chip compacto
                                            AnimeStatusChip(
                                                status = anime.statusUser,
                                                statusColor = statusColors[anime.statusUser] ?: Color.Gray,
                                                onStatusSelected = { action ->
                                                    viewModel.handleUserAction(anime.malId, action)
                                                },
                                                episodesWatched = anime.episodesWatched,
                                                totalEpisodes = anime.totalEpisodes,
                                                animeTitle = anime.title
                                            )
                                        }

                                        // Progreso compacto
                                        Column(
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "${anime.episodesWatched}/${anime.totalEpisodes} eps",
                                                    fontFamily = RobotoRegular,
                                                    fontSize = 11.sp,
                                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                                )

                                                if (anime.statusUser == "Viendo" && anime.totalEpisodes > 0 && anime.episodesWatched < anime.totalEpisodes) {
                                                    IconButton(
                                                        onClick = {
                                                            val newEpisodesWatched = anime.episodesWatched + 1
                                                            viewModel.updateEpisodesWatched(anime.malId, newEpisodesWatched)
                                                        },
                                                        modifier = Modifier.size(28.dp),
                                                        colors = IconButtonDefaults.iconButtonColors(
                                                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                                                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                                        )
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.AddCircleOutline,
                                                            contentDescription = "Marcar episodio",
                                                            modifier = Modifier.size(16.dp)
                                                        )
                                                    }
                                                }
                                            }

                                            LinearProgressIndicator(
                                                progress = {
                                                    if (anime.totalEpisodes > 0) {
                                                        anime.episodesWatched.toFloat() / anime.totalEpisodes.toFloat()
                                                    } else 0f
                                                },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(4.dp)
                                                    .clip(RoundedCornerShape(4.dp)),
                                                color = MaterialTheme.colorScheme.primary,
                                                trackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                                                strokeCap = ProgressIndicatorDefaults.LinearStrokeCap
                                            )
                                        }
                                    }
                                }

                                DeleteMyAnime(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(8.dp),
                                    onDeleteConfirmed = {
                                        showDialog = true
                                        animeIdToDelete = anime.malId
                                    }
                                )

                                if (showDialog) {
                                    CustomDialog(
                                        onDismissRequest = {
                                            showDialog = false
                                        },
                                        onConfirm = {
                                            viewModel.deleteAnimeToList(animeIdToDelete)
                                            scope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = "Anime eliminado de tu lista",
                                                    actionLabel = "Deshacer",
                                                    duration = SnackbarDuration.Long
                                                )
                                            }
                                        },
                                        onDismiss = {
                                            // Solo cierra el diálogo
                                        },
                                        title = "Confirmar eliminación",
                                        message = "¿Estás seguro de que quieres eliminar este anime de tu lista?\n\nUna vez eliminado tendrás que volver a agregarlo de nuevo a tu lista.",
                                        confirmButtonText = "Eliminar",
                                        dismissButtonText = "Cancelar",
                                        type = DialogType.DELETE
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
fun CompactAnimeCard(
    anime: com.yumedev.seijakulist.data.local.entities.AnimeEntity,
    statusColor: Color,
    onAnimeClick: () -> Unit,
    onDeleteConfirmed: () -> Unit,
    viewModel: MyAnimeListViewModel
) {
    val RobotoBold = FontFamily(
        Font(R.font.roboto_bold, FontWeight.Bold)
    )
    val RobotoRegular = FontFamily(
        Font(R.font.roboto_regular)
    )

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
            .height(220.dp)
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
                    onTap = { onAnimeClick() }
                )
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp,
            pressedElevation = 6.dp
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Imagen
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(anime.imageUrl),
                        contentDescription = anime.title,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                        contentScale = ContentScale.Crop
                    )

                    // Score badge
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp),
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF1E1E1E).copy(alpha = 0.9f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = String.format("%.1f", anime.userScore),
                                color = Color.White,
                                fontSize = 11.sp,
                                fontFamily = RobotoBold
                            )
                        }
                    }
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
                        text = anime.title,
                        fontFamily = RobotoBold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 14.sp
                    )

                    // Progreso
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${anime.episodesWatched}/${anime.totalEpisodes}",
                            fontFamily = RobotoRegular,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )

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
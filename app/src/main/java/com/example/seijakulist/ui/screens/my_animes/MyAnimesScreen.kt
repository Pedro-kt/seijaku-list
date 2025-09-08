package com.example.seijakulist.ui.screens.my_animes

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ElevatedFilterChip
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
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
import com.example.seijakulist.R
import com.example.seijakulist.ui.components.AnimeStatusChip
import com.example.seijakulist.ui.components.DeleteMyAnime
import com.example.seijakulist.ui.navigation.AppDestinations
import kotlinx.coroutines.launch

@Composable
fun MyAnimeListScreen(
    navController: NavController,
    viewModel: MyAnimeListViewModel = hiltViewModel(),
    isSearching: Boolean,
    onDismissSearch: () -> Unit
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Parece que no hay animes aquí todavía. ¿Por qué no añades tu primer anime?",
                    fontFamily = RobotoRegular,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                ElevatedButton(
                    onClick = {
                        navController.navigate(AppDestinations.SEARCH_ANIME_ROUTE)
                    },
                    modifier = Modifier.padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 16.dp
                    )
                ) {
                    Text(text = "Buscar anime!", color = Color.White)
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
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 8.dp)
            ) {
                items(statusAnime) { filter ->
                    val isSelected = selectedFilter == filter

                    ElevatedFilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedFilter = if (isSelected) null else filter
                        },
                        label = {
                            Text(
                                text = filter,
                                color = if (isSelected) {
                                    MaterialTheme.colorScheme.onPrimary
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                        },
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (isSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.surfaceContainerHigh
                            }
                        ),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                        )
                    )
                }
            }

            val displayedAnimes = remember(savedAnimes, searchQuery, selectedFilter) {
                savedAnimes.filter { anime ->
                    (searchQuery.isBlank() || anime.title.contains(
                        searchQuery,
                        ignoreCase = true
                    )) &&
                            (selectedFilter == null || anime.statusUser == selectedFilter)
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp)
            ) {
                items(displayedAnimes) { anime ->
                    var visible by rememberSaveable { mutableStateOf(false) }

                    LaunchedEffect(Unit) {
                        visible = true
                    }

                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(animationSpec = tween(500)) + slideInHorizontally(
                            animationSpec = tween(500)
                        )
                    ) {
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate("${AppDestinations.ANIME_DETAIL_LOCAL_ROUTE}/${anime.malId}")
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(160.dp)
                                ) {
                                    Box() {
                                        Image(
                                            painter = rememberAsyncImagePainter(anime.imageUrl),
                                            contentDescription = anime.title,
                                            modifier = Modifier
                                                .height(160.dp)
                                                .width(110.dp)
                                                .clip(RoundedCornerShape(16.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                        Row(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(topEnd = 16.dp))
                                                .background(color = Color.Black.copy(alpha = 0.8f))
                                                .height(24.dp)
                                                .wrapContentWidth()
                                                .align(Alignment.BottomStart),
                                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = "Puntuacion",
                                                tint = Color.White,
                                                modifier = Modifier
                                                    .padding(start = 6.dp)
                                                    .size(12.dp)
                                            )
                                            Text(
                                                text = String.format("%.1f", anime.userScore),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                textAlign = TextAlign.Start,
                                                modifier = Modifier
                                                    .wrapContentWidth()
                                                    .padding(end = 6.dp),
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontFamily = RobotoBold
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Column(
                                        modifier = Modifier
                                            .fillMaxHeight(),
                                        verticalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = anime.title,
                                            fontFamily = RobotoBold,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier
                                                .padding(end = 40.dp)
                                        )
                                        AnimeStatusChip(
                                            status = anime.statusUser,
                                            statusColor = statusColors[anime.statusUser]
                                                ?: Color.Gray,
                                            onStatusSelected = { action ->
                                                viewModel.handleUserAction(anime.malId, action)
                                            },
                                            episodesWatched = anime.episodesWatched,
                                            totalEpisodes = anime.totalEpisodes,
                                            animeTitle = anime.title
                                        )
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Ep vistos: ${anime.episodesWatched}/${anime.totalEpisodes}",
                                                fontFamily = RobotoRegular,
                                                color = MaterialTheme.colorScheme.onSurface,
                                            )
                                            Button(
                                                modifier = Modifier
                                                    .padding(end = 10.dp),
                                                onClick = {
                                                    val newEpisodesWatched =
                                                        anime.episodesWatched + 1
                                                    viewModel.updateEpisodesWatched(
                                                        anime.malId,
                                                        newEpisodesWatched
                                                    )
                                                },
                                                enabled = anime.statusUser == "Viendo" && anime.totalEpisodes > 0 && anime.episodesWatched < anime.totalEpisodes
                                            ) {
                                                Text(text = "+1")
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
                                                .clip(
                                                    RoundedCornerShape(
                                                        bottomStart = 16.dp,
                                                        bottomEnd = 16.dp
                                                    )
                                                ),
                                            color = MaterialTheme.colorScheme.inversePrimary,
                                            trackColor = MaterialTheme.colorScheme.outline,
                                            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                                        )
                                    }
                                }

                                DeleteMyAnime(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(16.dp),
                                    onDeleteConfirmed = {
                                        showDialog = true
                                        animeIdToDelete = anime.malId
                                    }
                                )

                                if (showDialog) {
                                    AlertDialog(
                                        onDismissRequest = {
                                            showDialog = false
                                        },
                                        title = {
                                            Text(text = "Confirmar eliminación")
                                        },
                                        text = {
                                            Text(
                                                text = "¿Estás seguro de que quieres eliminar este anime de tu lista? \n" +
                                                        "Una vez eliminado tendras que volver a agregarlo de nuevo a tu lista"
                                            )
                                        },
                                        confirmButton = {
                                            TextButton(
                                                onClick = {
                                                    showDialog = false

                                                    viewModel.deleteAnimeToList(animeIdToDelete)
                                                    scope.launch {
                                                        val result = snackbarHostState.showSnackbar(
                                                            message = "Anime eliminado de tu lista",
                                                            actionLabel = "Deshacer",
                                                            duration = SnackbarDuration.Long
                                                        )
                                                    }
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = MaterialTheme.colorScheme.primary,
                                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                                )
                                            ) {
                                                Text("Eliminar")
                                            }
                                        },
                                        dismissButton = {
                                            TextButton(
                                                onClick = {
                                                    showDialog = false
                                                }
                                            ) {
                                                Text("Cancelar")
                                            }
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
}
package com.example.seijakulist.ui.screens.my_animes

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.seijakulist.ui.components.ArrowBackTopAppBar
import com.example.seijakulist.ui.components.BottomNavItemScreen
import com.example.seijakulist.ui.components.DeleteMyAnime
import com.example.seijakulist.ui.components.FilterTopAppBar
import com.example.seijakulist.ui.components.LoadingScreen
import com.example.seijakulist.ui.navigation.AppDestinations

@Composable
fun MyAnimeListScreen(
    navController: NavController,
    viewModel: MyAnimeListViewModel = hiltViewModel()
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
    var selectedFilter by remember { mutableStateOf<String?>(null) }

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

    Scaffold(
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        },
        containerColor = Color(0xff121211),
        topBar = {
            AnimatedContent(
                targetState = collapsedSearch,
                label = "search bar animation",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFF121211))
                    .statusBarsPadding()
            ) { isSearchExpanded ->
                if (isSearchExpanded) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        placeholder = { Text("Buscar...") },
                        singleLine = true,
                        shape = RoundedCornerShape(50.dp),
                        leadingIcon = {
                            IconButton(onClick = {
                                collapsedSearch = false
                                searchQuery = ""
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Cerrar búsqueda"
                                )
                            }
                        },
                        trailingIcon = {
                            if (searchQuery.isNotBlank()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Limpiar"
                                    )
                                }
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.White.copy(alpha = 0.5f),
                            unfocusedIndicatorColor = Color.White.copy(alpha = 0.3f),
                            cursorColor = Color.White,
                            focusedLabelColor = Color.White.copy(alpha = 0.7f),
                            unfocusedLabelColor = Color.White.copy(alpha = 0.5f)
                        ),
                    )
                } else {
                    Column(modifier = Modifier.background(color = Color(0xFF121211))) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .statusBarsPadding()
                                .background(color = Color(0xFF121211))
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ArrowBackTopAppBar(navController)
                            Text(
                                text = "Mis animes",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(bottom = 8.dp)
                                    .weight(1f),
                                fontFamily = RobotoBold
                            )
                            FilterTopAppBar(
                                onSearchClick = {
                                    collapsedSearch = !collapsedSearch
                                },
                                onSortClick = {
                                    isSortedAscending = !isSortedAscending
                                },
                                savedAnimes = savedAnimes
                            )
                        }
                        if (savedAnimes.isEmpty()) {
                            HorizontalDivider()
                        }
                    }
                }
            }

        },
        bottomBar = {
            BottomNavItemScreen(navController)
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    actionContentColor = Color.Black,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF121211))
                .padding(innerPadding),
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
                        color = Color.White.copy(alpha = 0.6f)
                    )
                    Button(
                        onClick = {
                            navController.navigate(AppDestinations.SEARCH_ANIME_ROUTE)
                        },
                        modifier = Modifier.padding(top = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xff7226ff),
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Buscar anime!")
                    }
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF121211))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Filtrar por:",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    IconButton(onClick = { collapsedFilter = !collapsedFilter }) {
                        Icon(
                            imageVector = if (collapsedFilter) {
                                Icons.Default.ArrowDropDown
                            } else {
                                Icons.AutoMirrored.Filled.ArrowRight
                            },
                            contentDescription = "Icono de filtrar por",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                if (collapsedFilter) {
                    LazyRow(
                        modifier = Modifier
                            .background(Color(0xFF121211))
                            .padding(horizontal = 8.dp)
                    ) {
                        items(statusAnime) { filter ->
                            val isSelected = selectedFilter == filter

                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    selectedFilter = if (isSelected) null else filter
                                },
                                label = {
                                    Text(filter)
                                },
                                modifier = Modifier.padding(end = 8.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = Color(0xFF050505),
                                    labelColor = Color.White,
                                    selectedContainerColor = Color(0xff7226ff),
                                    selectedLabelColor = Color.White
                                ),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = Color.White
                                ),
                                trailingIcon = {
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Seleccionado",
                                            tint = Color.White
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
                HorizontalDivider()


                val displayedAnimes by remember(selectedFilter, searchQuery, isSortedAscending) {
                    derivedStateOf {
                        val filteredByStatus = when (selectedFilter) {
                            "Viendo" -> savedAnimeStatusWatching
                            "Completado" -> savedAnimeStatusComplete
                            "Pendiente" -> savedAnimeStatusPending
                            "Abandonado" -> savedAnimeStatusAbandoned
                            "Planeado" -> savedAnimeStatusPlanned
                            else -> savedAnimes
                        }

                        val filteredBySearch = if (searchQuery.isBlank()) {
                            filteredByStatus
                        } else {
                            filteredByStatus.filter { anime ->
                                anime.title.contains(searchQuery, ignoreCase = true)
                            }
                        }
                        if (isSortedAscending) {
                            filteredBySearch.sortedBy { it.title }
                        } else {
                            filteredBySearch.sortedByDescending { it.title }
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .background(Color(0xFF121211))
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    items(displayedAnimes) { anime ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    navController.navigate("${AppDestinations.ANIME_DETAIL_LOCAL_ROUTE}/${anime.malId}")
                                },
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF202020)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(anime.imageUrl),
                                        contentDescription = anime.title,
                                        modifier = Modifier
                                            .height(145.dp)
                                            .width(100.dp)
                                            .clip(RoundedCornerShape(16.dp)),
                                        contentScale = ContentScale.Crop
                                    )

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Text(
                                            text = anime.title,
                                            fontFamily = RobotoBold,
                                            color = Color.White,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.padding(end = 40.dp)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)

                                        ) {
                                            StarWithScore(anime.userScore)
                                            VerticalDivider(
                                                modifier = Modifier
                                                    .height(20.dp)
                                                    .width(1.dp),
                                                color = Color.White
                                            )
                                            Text(
                                                text = anime.statusUser,
                                                fontFamily = RobotoRegular,
                                                color = Color.White
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .size(12.dp)
                                                    .clip(CircleShape)
                                                    .background(statusColors[anime.statusUser]!!)
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text(
                                            text = "Ep: 0/12",
                                            fontFamily = RobotoRegular,
                                            color = Color.White
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                DeleteMyAnime(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(16.dp),
                                    viewModel,
                                    anime.malId,
                                    snackbarHostState = snackbarHostState,
                                    scope = scope
                                )
                            }
                        }

                    }
                }
            }

        }
    }
}

@Composable
fun StarWithScore(score: Float) {
    val RobotoRegular = FontFamily(
        Font(R.font.roboto_regular)
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = score.toString(),
            fontFamily = RobotoRegular,
            color = Color.White
        )
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Puntuacion",
            tint = Color.Yellow,
            modifier = Modifier.size(16.dp)
        )
    }
}

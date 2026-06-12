package com.yumedev.seijakulist.ui.screens.profile

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yumedev.seijakulist.data.local.entities.AnimeEntity
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import com.yumedev.seijakulist.ui.theme.PoppinsMedium
import androidx.compose.ui.graphics.vector.ImageVector
import com.yumedev.seijakulist.ui.screens.detail.SectionHeader
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.delay
import com.yumedev.seijakulist.ui.theme.adp
import com.yumedev.seijakulist.ui.theme.asp

// ─────────────────────────────────────────────────────────────────────────────
//  Datos de logros
// ─────────────────────────────────────────────────────────────────────────────
val achievements = listOf(
    Achievement("Pionero",                   "Te uniste en el primer mes de SeijakuList",         "Legendary", icon = Icons.Default.Explore),
    Achievement("Analista",                  "Escribiste tu primera reseña",                      "Normal",    icon = Icons.Default.Edit),
    Achievement("Crítico",                   "Escribiste 10 reseñas",                             "Rare",      icon = Icons.Default.RateReview),
    Achievement("Veterano",                  "Llevas 1 año en SeijakuList",                       "Epic",      icon = Icons.Default.Shield),
    Achievement("Coleccionista Principiante","Añadiste 10 animes a tu lista",                     "Normal",    icon = Icons.Default.BookmarkAdd),
    Achievement("Maratonista",               "Marcaste 50 episodios como vistos en una semana",   "Epic",      icon = Icons.Default.DirectionsRun),
    Achievement("Coleccionista Avanzado",    "Añadiste 50 animes a tu lista",                     "Rare",      icon = Icons.Default.LibraryBooks),
    Achievement("Coleccionista Experto",     "Añadiste 100 animes a tu lista",                    "Legendary", icon = Icons.Default.WorkspacePremium),
    Achievement("Maratonista Avanzado",      "Marcaste 100 episodios como vistos en una semana",  "Legendary", icon = Icons.Default.Bolt),
    Achievement("Coleccionista Master",      "Añadiste 200 animes a tu lista",                    "Legendary", icon = Icons.Default.Diamond)
)

// ─────────────────────────────────────────────────────────────────────────────
//  EmptyProfileScreen
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun EmptyProfileScreen(navController: NavController) {
    var screenVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { kotlinx.coroutines.delay(100); screenVisible = true }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            AnimatedVisibility(
                visible = screenVisible,
                enter = fadeIn(tween(600)) + scaleIn(initialScale = 0.9f, animationSpec = tween(600))
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(120.adp())) {
                    Box(
                        modifier = Modifier
                            .size(130.adp())
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f), Color.Transparent)
                                ),
                                shape = CircleShape
                            )
                    )
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(100.adp()),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            AnimatedVisibility(visible = screenVisible, enter = fadeIn(tween(600, delayMillis = 200))) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Crea tu cuenta",
                        fontSize = 26.asp(),
                        fontFamily = PoppinsBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Sincroniza tus animes en la nube",
                            fontSize = 14.asp(),
                            fontFamily = PoppinsRegular,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "Cambia de celular sin perder nada, desbloquea logros y accede a todas las características de Seijaku List",
                            fontSize = 13.asp(),
                            fontFamily = PoppinsRegular,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center,
                            lineHeight = 18.asp(),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            AnimatedVisibility(
                visible = screenVisible,
                enter = fadeIn(tween(600, delayMillis = 400)) + slideInVertically(
                    initialOffsetY = { 30 },
                    animationSpec = tween(600)
                )
            ) {
                Button(
                    onClick = { navController.navigate(AppDestinations.AUTH_ROUTE) },
                    modifier = Modifier.fillMaxWidth().height(54.adp()),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Comenzar", fontSize = 16.asp(), fontFamily = PoppinsBold)
                }
            }
            Spacer(modifier = Modifier.height(100.adp()))
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  ProfileView
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun ProfileView(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by profileViewModel.uiState.collectAsState()

    val averageScore: Double = uiState.allSavedAnimes
        .map { it.userScore }.filter { it > 0 }.average()
        .takeIf { !it.isNaN() } ?: 0.0
    val watchingAnimes  = uiState.allSavedAnimes.count { it.statusUser == "Viendo" }
    val pendingAnimes   = uiState.allSavedAnimes.count { it.statusUser == "Pendiente" }
    val abandonedAnimes = uiState.allSavedAnimes.count { it.statusUser == "Abandonado" }
    val plannedAnimes   = uiState.allSavedAnimes.count { it.statusUser == "Planeado" }

    if (uiState.userProfile == null) { EmptyProfileScreen(navController); return }
    if (uiState.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        return
    }

    val userProfile       = uiState.userProfile!!
    val username          = userProfile.username ?: "Nombre de usuario"
    val profilePictureUrl = userProfile.profilePictureUrl
    val userBio           = userProfile.bio ?: "Añade tu biografía"

    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset < 100
        }.collect { isAtTop -> profileViewModel.updateScrollPosition(isAtTop) }
    }

    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

    LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {

        // ── 1. Header Compacto ────────────────────────────────────────────────
        item {
            ProfileHeaderCompact(
                username          = username.split(" ").firstOrNull() ?: username,
                fullName          = username,
                memberSince       = "MIEMBRO DESDE 2026",
                watchingAnimes    = uiState.allSavedAnimes.filter { it.statusUser == "Viendo" },
                profilePictureUrl = profilePictureUrl,
                onEditClick       = { navController.navigate(AppDestinations.PROFILE_SETUP_ROUTE) },
                onAnimeClick      = { malId -> navController.navigate("${AppDestinations.ANIME_DETAIL_LOCAL_ROUTE}/$malId") }
            )
        }

        // ── 2. Tabs ───────────────────────────────────────────────────────────
        item {
            ProfileTabSelector(
                selectedTab   = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it },
                tabs          = listOf("Anime", "Manga", "General")
            )
        }

        // ── 3. Contenido según tab seleccionado ──────────────────────────────
        item {
            AnimatedContent(
                targetState = selectedTabIndex,
                label       = "TabAnimation",
                transitionSpec = {
                    fadeIn(tween(400)) + scaleIn(initialScale = 0.95f) togetherWith fadeOut(tween(400))
                }
            ) { targetIndex ->
                when (targetIndex) {
                    0 -> { // Tab Anime
                        Column {
                            // ── Resumen ───────────────────────────────────────
                            val totalMinutes = uiState.stats.totalMinutesWatched
                            val totalHours = totalMinutes / 60
                            val totalDays = totalHours / 24
                            val remainingHours = totalHours % 24

                            ResumenSection(
                                year              = 2026,
                                totalAnimes       = uiState.stats.totalAnimes,
                                averageScore      = uiState.stats.averageScore,
                                totalEpisodes     = uiState.stats.totalEpisodesWatched,
                                totalHours        = totalHours,
                                totalDaysAndHours = Pair(totalDays, remainingHours)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // ── Distribución ──────────────────────────────────
                            DistribucionSection(
                                totalAnimes = uiState.stats.totalAnimes,
                                items = listOf(
                                    DistributionItem("Planeado", plannedAnimes, Color(0xFF9C6FDE)),
                                    DistributionItem("Completado", uiState.stats.completedAnimes, Color(0xFF4CAF50)),
                                    DistributionItem("Pendiente", pendingAnimes, Color(0xFFFFA726)),
                                    DistributionItem("Viendo", watchingAnimes, Color(0xFF00A8FF)),
                                    DistributionItem("Abandonado", abandonedAnimes, Color(0xFFEF5350))
                                )
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // ── Géneros favoritos ─────────────────────────────
                            if (uiState.stats.genreStats.isNotEmpty()) {
                                val topGenres = uiState.stats.genreStats.entries
                                    .sortedByDescending { it.value }
                                    .take(3)
                                val totalAnimes = uiState.stats.totalAnimes
                                val genreColors = listOf(
                                    Color(0xFFEF5350), // Rojo
                                    Color(0xFF9C6FDE), // Morado
                                    Color(0xFFFFA726)  // Naranja
                                )

                                GenerosFavoritosSection(
                                    genres = topGenres.mapIndexed { index, (genre, count) ->
                                        GenreItem(
                                            name = genre,
                                            count = count,
                                            percentage = ((count.toFloat() / totalAnimes.toFloat()) * 100).toInt(),
                                            color = genreColors.getOrElse(index) { Color(0xFF00A8FF) }
                                        )
                                    }
                                )
                            }

                            // ── Top 5 ─────────────────────────────────────────
                            Spacer(modifier = Modifier.height(16.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.adp())
                            ) {
                                Row(
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    Text(
                                        text = "Top 5 Animes",
                                        fontFamily = PoppinsBold,
                                        fontSize = 20.asp(),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                    )
                                    Spacer(modifier = Modifier.width(8.adp()))
                                    Text(
                                        text = "Los 5 animes que más te gustan",
                                        fontFamily = PoppinsRegular,
                                        fontSize = 12.asp(),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                IconButton(
                                    onClick   = { navController.navigate(AppDestinations.SELECT_TOP5_ROUTE) },
                                    modifier  = Modifier.size(36.adp()).align(Alignment.CenterEnd)
                                ) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = "Editar Top 5",
                                        tint     = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.adp())
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            if (uiState.top5Animes.isNotEmpty()) {
                                AnimeTop5Showcase(
                                    animes        = uiState.top5Animes,
                                    navController = navController
                                )
                            } else {
                                Box(
                                    modifier          = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 32.dp),
                                    contentAlignment  = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Star,
                                            contentDescription = null,
                                            modifier = Modifier.size(48.adp()),
                                            tint     = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                        )
                                        Text(
                                            "Seleccioná tus 5 animes favoritos",
                                            fontSize = 16.asp(),
                                            fontFamily = PoppinsRegular,
                                            color      = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                        )
                                        FilledTonalButton(
                                            onClick = { navController.navigate(AppDestinations.SELECT_TOP5_ROUTE) }
                                        ) {
                                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.adp()))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("Elegir Top 5", fontFamily = PoppinsBold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    1 -> {
                        Column(
                            modifier            = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 48.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.MenuBook,
                                contentDescription = null,
                                modifier = Modifier.size(64.adp()),
                                tint     = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                            )
                            Text(
                                "¡Manga en camino!",
                                fontSize = 20.asp(),
                                fontFamily = PoppinsBold,
                                color      = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                "Estamos trabajando para que puedas trackear tus lecturas muy pronto.",
                                fontSize = 14.asp(),
                                fontFamily = PoppinsRegular,
                                color      = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign  = TextAlign.Center
                            )
                        }
                    }
                    2 -> { // Tab General
                        Column(
                            modifier            = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 48.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ShowChart,
                                contentDescription = null,
                                modifier = Modifier.size(64.adp()),
                                tint     = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                            )
                            Text(
                                "¡General en camino!",
                                fontSize = 20.asp(),
                                fontFamily = PoppinsBold,
                                color      = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                "Estamos trabajando para que puedas ver información general aquí!",
                                fontSize = 14.asp(),
                                fontFamily = PoppinsRegular,
                                color      = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign  = TextAlign.Center
                            )
                        }
                    }
                    else -> {
                        // Fallback
                        Box(modifier = Modifier.fillMaxWidth().padding(48.dp))
                    }
                }
                }
            }
        item { Spacer(modifier = Modifier.height(90.adp())) }
        }
    }

// ─────────────────────────────────────────────────────────────────────────────
//  ProfileHeader
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun ProfileHeader(
    username: String,
    userBio: String,
    profilePictureUrl: String?,
    onEditClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier             = Modifier.fillMaxWidth(),
            verticalAlignment    = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Avatar
            Surface(
                modifier        = Modifier.size(68.adp()),
                shape           = CircleShape,
                border          = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outlineVariant),
                shadowElevation = 4.dp
            ) {
                if (profilePictureUrl != null) {
                    AsyncImage(
                        model            = profilePictureUrl,
                        contentDescription = null,
                        contentScale     = ContentScale.Crop,
                        modifier         = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier         = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(36.adp()),
                            tint     = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            // Info
            Column(
                modifier            = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text          = username,
                    fontSize = 18.asp(),
                    fontFamily    = PoppinsBold,
                    letterSpacing = (-0.3).sp,
                    color         = MaterialTheme.colorScheme.onBackground,
                    maxLines      = 1,
                    overflow      = TextOverflow.Ellipsis
                )
                if (userBio.isNotBlank()) {
                    Text(
                        text       = userBio,
                        fontSize = 12.asp(),
                        fontFamily = PoppinsRegular,
                        color      = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines   = 2,
                        overflow   = TextOverflow.Ellipsis,
                        lineHeight = 16.asp()
                    )
                }
            }

            // Edit button
            OutlinedButton(
                onClick        = onEditClick,
                modifier       = Modifier.height(34.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp),
                shape          = RoundedCornerShape(10.dp),
                border         = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Text("Editar", fontSize = 12.asp(), fontFamily = PoppinsMedium)
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  ProfileStatsSection — minimalista, con respiración
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun ProfileStatsSection(
    totalAnimes: Int,
    completedAnimes: Int,
    totalEpisodes: Int,
    averageScore: Double,
    watchingAnimes: Int,
    pendingAnimes: Int,
    abandonedAnimes: Int,
    plannedAnimes: Int
) {
    val daysWatched = totalEpisodes * 24.0 / 1440.0
    val timeLabel = when {
        daysWatched >= 1  -> "${daysWatched.toInt()}d"
        totalEpisodes > 0 -> "${(daysWatched * 24).toInt()}h"
        else              -> "—"
    }
    val completionFraction = if (totalAnimes > 0) completedAnimes.toFloat() / totalAnimes.toFloat() else 0f
    val completionPct      = (completionFraction * 100).toInt()

    var progressVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { delay(200); progressVisible = true }
    val animatedProgress by animateFloatAsState(
        targetValue    = if (progressVisible) completionFraction else 0f,
        animationSpec  = tween(900, easing = EaseOutBack),
        label          = "progress"
    )

    Column(
        modifier            = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // ── Tres stats principales ─────────────────────────────────────────────
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard(value = "$totalAnimes",  label = "Animes", accentColor = Color(0xFF818CF8), modifier = Modifier.weight(1f))
            StatCard(
                value       = if (averageScore > 0) "%.1f".format(averageScore) else "—",
                label       = "Score",
                accentColor = Color(0xFFFBBF24),
                modifier    = Modifier.weight(1f)
            )
            StatCard(value = timeLabel, label = "Tiempo", accentColor = Color(0xFFF472B6), modifier = Modifier.weight(1f))
        }

        // ── Grid de estados ────────────────────────────────────────────────────
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusCard("Completado", completedAnimes, Color(0xFF4ADE80), Modifier.weight(1f))
                StatusCard("Viendo",     watchingAnimes,  Color(0xFF818CF8), Modifier.weight(1f))
            }
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusCard("Pendiente",  pendingAnimes,   Color(0xFFFBBF24), Modifier.weight(1f))
                StatusCard("Abandonado", abandonedAnimes, Color(0xFFF87171), Modifier.weight(1f))
            }
            StatusCard("Planeado", plannedAnimes, Color(0xFF6366F1), Modifier.fillMaxWidth())
        }

        // ── Barra de progreso de completados ───────────────────────────────────
        if (totalAnimes > 0) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text(
                        "$completedAnimes de $totalAnimes completados",
                        fontSize = 11.asp(),
                        fontFamily = PoppinsMedium,
                        color      = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "$completionPct%",
                        fontSize = 11.asp(),
                        fontFamily = PoppinsBold,
                        color      = MaterialTheme.colorScheme.primary
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(animatedProgress.coerceIn(0f, 1f))
                            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(2.dp))
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  StatCard — mini-card para cada stat principal
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun StatCard(
    value: String,
    label: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape    = RoundedCornerShape(12.dp),
        color    = MaterialTheme.colorScheme.surfaceContainer,
        border   = BorderStroke(1.dp, accentColor.copy(alpha = 0.18f))
    ) {
        Column(
            modifier            = Modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text          = value,
                fontSize = 20.asp(),
                fontFamily    = PoppinsBold,
                color         = MaterialTheme.colorScheme.onBackground,
                letterSpacing = (-0.5).sp,
                maxLines      = 1
            )
            Text(
                text       = label,
                fontSize = 11.asp(),
                fontFamily = PoppinsRegular,
                color      = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines   = 1
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  StatusCard — reemplaza el chip compacto anterior
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun StatusCard(label: String, count: Int, color: Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape    = RoundedCornerShape(12.dp),
        color    = color.copy(alpha = 0.08f),
        border   = BorderStroke(1.dp, color.copy(alpha = 0.2f))
    ) {
        Row(
            modifier              = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text       = label,
                fontSize = 13.asp(),
                fontFamily = PoppinsMedium,
                color      = MaterialTheme.colorScheme.onSurface,
                maxLines   = 1
            )
            Text(
                text       = "$count",
                fontSize = 22.asp(),
                fontFamily = PoppinsBold,
                color      = color,
                maxLines   = 1
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  CustomSeijakuTabSelector
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun CustomSeijakuTabSelector(tabs: List<String>, selectedTabIndex: Int, onTabSelected: (Int) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .height(48.adp()),
        shape  = CircleShape,
        color  = MaterialTheme.colorScheme.surfaceContainerHigh,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val tabWidth        = maxWidth / tabs.size
            val indicatorOffset by animateDpAsState(
                targetValue   = tabWidth * selectedTabIndex,
                animationSpec = spring(stiffness = Spring.StiffnessLow),
                label         = "indicator"
            )
            Box(
                modifier = Modifier
                    .offset(x = indicatorOffset)
                    .width(tabWidth)
                    .fillMaxHeight()
                    .padding(4.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            )
            Row(modifier = Modifier.fillMaxSize()) {
                tabs.forEachIndexed { index, title ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication        = null
                            ) { onTabSelected(index) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            title,
                            fontFamily = PoppinsBold,
                            fontSize = 14.asp(),
                            color      = if (selectedTabIndex == index)
                                MaterialTheme.colorScheme.onPrimary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  GenreFavoriteCard
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun GenreFavoriteCard(
    genre: String,
    count: Int,
    totalCount: Int,
    imageUrl: String?,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val percentage = if (totalCount > 0) ((count.toFloat() / totalCount) * 100).toInt() else 0
    ElevatedCard(
        modifier  = modifier.height(110.adp()),
        shape     = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (imageUrl != null) {
                AsyncImage(
                    model            = imageUrl,
                    contentDescription = null,
                    modifier         = Modifier.fillMaxSize(),
                    contentScale     = ContentScale.Crop
                )
            }
            Box(
                modifier = Modifier.fillMaxSize().background(
                    brush = Brush.verticalGradient(
                        colors = listOf(accentColor.copy(alpha = 0.55f), Color.Black.copy(alpha = 0.85f)),
                        startY = 0f,
                        endY   = Float.POSITIVE_INFINITY
                    )
                )
            )
            Text(
                text     = "$percentage%",
                fontSize = 11.asp(),
                fontFamily = PoppinsBold,
                color    = Color.White,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .background(accentColor.copy(alpha = 0.7f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 5.dp, vertical = 2.dp)
            )
            Column(
                modifier            = Modifier.align(Alignment.BottomStart).padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                Text(
                    genre,
                    fontSize = 12.asp(),
                    fontFamily = PoppinsBold,
                    color      = Color.White,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )
                Text(
                    if (count == 1) "$count anime" else "$count animes",
                    fontSize = 10.asp(),
                    fontFamily = PoppinsRegular,
                    color      = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  AnimeTop5Showcase - Diseño de podio 3D
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun AnimeTop5Showcase(animes: List<AnimeEntity>, navController: NavController) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { delay(80); visible = true }

    val alpha by animateFloatAsState(if (visible) 1f else 0f, tween(500), label = "showcase_a")
    val scale by animateFloatAsState(
        if (visible) 1f else 0.96f,
        spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow),
        label = "showcase_s"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .alpha(alpha)
            .scale(scale),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Podio 3D con los primeros 3 puestos
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            // Puesto 2 (izquierda, podio medio)
            if (animes.size >= 2) {
                PodiumPosition(
                    anime = animes[1],
                    position = 2,
                    podiumHeight = 50.adp(),
                    modifier = Modifier.weight(1f),
                    navController = navController
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            // Puesto 1 (centro, podio más alto)
            if (animes.isNotEmpty()) {
                PodiumPosition(
                    anime = animes[0],
                    position = 1,
                    podiumHeight = 80.adp(),
                    modifier = Modifier.weight(1f),
                    navController = navController
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            // Puesto 3 (derecha, podio más bajo)
            if (animes.size >= 3) {
                PodiumPosition(
                    anime = animes[2],
                    position = 3,
                    podiumHeight = 30.adp(),
                    modifier = Modifier.weight(1f),
                    navController = navController
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        // Menciones honoríficas (Puestos 4 y 5)
        if (animes.size >= 4) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header de menciones honoríficas
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        thickness = 1.dp,
                        color = Color.Gray.copy(alpha = 0.3f)
                    )
                    Text(
                        text = "MENCIONES HONORÍFICAS",
                        fontFamily = PoppinsBold,
                        fontSize = 11.asp(),
                        color = Color.Gray,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        thickness = 1.dp,
                        color = Color.Gray.copy(alpha = 0.3f)
                    )
                }

                // Cards de menciones
                HonorableMentionCard(
                    anime = animes[3],
                    position = 4,
                    navController = navController
                )

                if (animes.size >= 5) {
                    HonorableMentionCard(
                        anime = animes[4],
                        position = 5,
                        navController = navController
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  PodiumPosition - Posición del podio con efecto 3D (estilo Home)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun PodiumPosition(
    anime: AnimeEntity,
    position: Int,
    podiumHeight: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val (podiumColor, medalColor) = when (position) {
        1 -> Pair(Color(0xFFFFD700), Color(0xFFFFD700)) // Oro
        2 -> Pair(Color(0xFFC0C0C0), Color(0xFFC0C0C0)) // Plata
        3 -> Pair(Color(0xFFCD7F32), Color(0xFFCD7F32)) // Bronce
        else -> Pair(Color.Gray, Color.Gray)
    }

    // Altura fija para todas las imágenes
    val imageHeight = 200.adp()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        // Card del anime (estilo Home)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
                .clip(RoundedCornerShape(16.dp))
                .clickable {
                    navController.navigate("${AppDestinations.ANIME_DETAIL_LOCAL_ROUTE}/${anime.malId}")
                }
        ) {
            // Imagen de fondo completa
            AsyncImage(
                model = anime.imageUrl,
                contentDescription = anime.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            )

            // Gradiente superior para Score
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.adp())
                    .align(Alignment.TopCenter)
                    .background(
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0f to Color.Black.copy(alpha = 0.85f),
                                0.5f to Color.Black.copy(alpha = 0.5f),
                                1.0f to Color.Transparent
                            )
                        )
                    )
            )

            // Gradiente inferior para Título
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.adp())
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0f to Color.Transparent,
                                0.4f to Color.Black.copy(alpha = 0.6f),
                                1.0f to Color.Black.copy(alpha = 0.92f)
                            )
                        )
                    )
            )

            // Brillo especial para el primer lugar
            if (position == 1) {
                val infiniteTransition = androidx.compose.animation.core.rememberInfiniteTransition(label = "glow")
                val glowAlpha by infiniteTransition.animateFloat(
                    initialValue = 0.2f,
                    targetValue = 0.5f,
                    animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                        animation = tween(2000, easing = androidx.compose.animation.core.FastOutSlowInEasing),
                        repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
                    ),
                    label = "glow_alpha"
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    medalColor.copy(alpha = glowAlpha * 0.25f),
                                    Color.Transparent
                                ),
                                center = Offset(0.5f, 0.3f)
                            )
                        )
                )
            }

            // Score en la parte superior derecha
            if (anime.userScore > 0) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = anime.userScore.toString(),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 12.asp(),
                        fontFamily = PoppinsBold
                    )
                }
            }

            // Título en la parte inferior
            Text(
                text = anime.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontFamily = PoppinsMedium,
                fontSize = 13.asp(),
                lineHeight = 17.asp(),
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(horizontal = 10.dp, vertical = 10.dp)
            )
        }

        // Base del podio con número de posición (altura variable)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(podiumHeight),
            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
            color = podiumColor.copy(alpha = 0.9f),
            shadowElevation = 4.dp
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = position.toString(),
                    fontSize = 24.asp(),
                    fontFamily = PoppinsBold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  HonorableMentionCard - Card elegante para menciones honoríficas (puestos 4 y 5)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun HonorableMentionCard(
    anime: AnimeEntity,
    position: Int,
    navController: NavController
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.adp()),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        onClick = { navController.navigate("${AppDestinations.ANIME_DETAIL_LOCAL_ROUTE}/${anime.malId}") }
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Imagen con gradiente (estilo home)
            Box(
                modifier = Modifier
                    .width(70.adp())
                    .fillMaxHeight()
            ) {
                AsyncImage(
                    model = anime.imageUrl,
                    contentDescription = anime.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Gradiente overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colorStops = arrayOf(
                                    0.0f to Color.Black.copy(alpha = 0.4f),
                                    1.0f to Color.Black.copy(alpha = 0.7f)
                                )
                            )
                        )
                )
            }

            // Contenido
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Título
                Text(
                    text = anime.title,
                    fontSize = 14.asp(),
                    fontFamily = PoppinsBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )

                // Tipo, Géneros y Estado
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tipo (TV, Movie, OVA, etc.)
                    if (!anime.typeAnime.isNullOrBlank()) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF6366F1).copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = anime.typeAnime,
                                fontSize = 9.asp(),
                                fontFamily = PoppinsBold,
                                color = Color(0xFF6366F1),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // Géneros (máximo 2)
                    val genresList = anime.genres.split(",").map { it.trim() }.filter { it.isNotBlank() }
                    genresList.take(2).forEach { genre ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFF59E0B).copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = genre,
                                fontSize = 9.asp(),
                                fontFamily = PoppinsMedium,
                                color = Color(0xFFF59E0B),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Estado del usuario
                    if (anime.statusUser.isNotBlank()) {
                        val statusColor = when (anime.statusUser) {
                            "Viendo" -> Color(0xFF66BB6A)
                            "Completado" -> Color(0xFF42A5F5)
                            "Pendiente" -> Color(0xFFFFCA28)
                            "Abandonado" -> Color(0xFFEF5350)
                            "Planeado" -> Color(0xFF78909C)
                            else -> Color.Gray
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = statusColor.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = anime.statusUser,
                                fontSize = 9.asp(),
                                fontFamily = PoppinsBold,
                                color = statusColor,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                // Row inferior con score y badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Score
                    if (anime.userScore > 0) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = anime.userScore.toString(),
                                fontSize = 13.asp(),
                                fontFamily = PoppinsBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Badge "Mención honorífica"
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                        )
                    ) {
                        Text(
                            text = "Top ${position}",
                            fontSize = 10.asp(),
                            fontFamily = PoppinsBold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  AllAchievementsDialog
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun AllAchievementsDialog(achievements: List<Achievement>, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        ElevatedCard(
            modifier  = Modifier.fillMaxWidth(0.95f).fillMaxHeight(0.85f),
            shape     = RoundedCornerShape(24.dp),
            colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier              = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Color(0xFFFF9800), modifier = Modifier.size(22.adp()))
                        Text("Todos los logros", fontSize = 18.asp(), fontFamily = PoppinsBold, color = MaterialTheme.colorScheme.onSurface)
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.adp())) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                LazyColumn(
                    modifier        = Modifier.fillMaxSize(),
                    contentPadding  = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(achievements) { achievement ->
                        AchievementCard(achievement = achievement)
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Achievement + AchievementCard + AchievementDetailDialog
// ─────────────────────────────────────────────────────────────────────────────
data class Achievement(
    val name: String,
    val description: String,
    val typeAchievement: String,
    val unlockDate: String = "Mayo 2025",
    val reward: String = "Insignia exclusiva",
    val icon: ImageVector = Icons.Default.EmojiEvents
)

@Composable
private fun AchievementCard(achievement: Achievement, onClick: () -> Unit = {}) {
    val color = when (achievement.typeAchievement) {
        "Normal"    -> Color(0xFF4CAF50)
        "Rare"      -> Color(0xFF2196F3)
        "Epic"      -> Color(0xFF9C27B0)
        "Legendary" -> Color(0xFFFF9800)
        else        -> MaterialTheme.colorScheme.primary
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(12.dp),
        colors   = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        onClick  = onClick
    ) {
        Row(
            modifier              = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier         = Modifier
                    .size(44.adp())
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f))
                    .border(width = 1.5.dp, color = color, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = achievement.icon, contentDescription = null, tint = color, modifier = Modifier.size(22.adp()))
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        achievement.name,
                        fontSize = 14.asp(),
                        fontFamily = PoppinsBold,
                        color      = MaterialTheme.colorScheme.onSurface,
                        maxLines   = 1,
                        overflow   = TextOverflow.Ellipsis,
                        modifier   = Modifier.weight(1f, fill = false)
                    )
                    Surface(
                        shape  = RoundedCornerShape(6.dp),
                        color  = color.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, color.copy(alpha = 0.3f))
                    ) {
                        Text(
                            achievement.typeAchievement,
                            fontSize = 10.asp(),
                            fontFamily = PoppinsBold,
                            color      = color,
                            modifier   = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(
                    achievement.description,
                    fontSize = 12.asp(),
                    fontFamily = PoppinsRegular,
                    color      = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines   = 2,
                    overflow   = TextOverflow.Ellipsis,
                    lineHeight = 16.asp()
                )
            }
        }
    }
}

@Composable
private fun AchievementDetailDialog(achievement: Achievement, onDismiss: () -> Unit) {
    val color = when (achievement.typeAchievement) {
        "Normal"    -> Color(0xFF4CAF50)
        "Rare"      -> Color(0xFF2196F3)
        "Epic"      -> Color(0xFF9C27B0)
        "Legendary" -> Color(0xFFFF9800)
        else        -> MaterialTheme.colorScheme.primary
    }
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        ElevatedCard(
            modifier  = Modifier.fillMaxWidth(0.9f).wrapContentHeight(),
            shape     = RoundedCornerShape(24.dp),
            colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier            = Modifier.fillMaxWidth().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.adp())) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .size(120.adp())
                        .clip(CircleShape)
                        .background(Brush.radialGradient(colors = listOf(color.copy(alpha = 0.8f), color.copy(alpha = 0.4f))))
                        .border(width = 4.dp, color = color, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = achievement.icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(56.adp()))
                }
                Spacer(modifier = Modifier.height(24.dp))
                Surface(shape = RoundedCornerShape(12.dp), color = color.copy(alpha = 0.2f)) {
                    Text(achievement.typeAchievement, fontSize = 14.asp(), fontFamily = PoppinsBold, color = color, modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(achievement.name, fontSize = 24.asp(), fontFamily = PoppinsBold, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(12.dp))
                Text(achievement.description, fontSize = 16.asp(), fontFamily = PoppinsRegular, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center, lineHeight = 22.asp())
                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(modifier = Modifier.height(16.dp))
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CalendarToday, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.adp()))
                            Text("Desbloqueado", fontSize = 14.asp(), fontFamily = PoppinsRegular, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(achievement.unlockDate, fontSize = 14.asp(), fontFamily = PoppinsBold, color = MaterialTheme.colorScheme.onSurface)
                    }
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.adp()))
                            Text("Recompensa", fontSize = 14.asp(), fontFamily = PoppinsRegular, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(achievement.reward, fontSize = 14.asp(), fontFamily = PoppinsBold, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Text("Cerrar", fontFamily = PoppinsBold, fontSize = 16.asp())
                }
            }
        }
    }
}
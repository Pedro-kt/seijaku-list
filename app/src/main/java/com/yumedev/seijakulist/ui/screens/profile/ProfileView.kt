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
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.StrokeCap
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
import androidx.room.util.TableInfo
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.*
import com.yumedev.seijakulist.data.local.entities.AnimeEntity
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import com.yumedev.seijakulist.ui.screens.auth_screen.WrappedStatCard
import com.yumedev.seijakulist.ui.theme.PoppinsMedium
import androidx.compose.ui.graphics.vector.ImageVector
import com.yumedev.seijakulist.ui.screens.detail.SectionHeader

// ─────────────────────────────────────────────────────────────────────────────
//  Datos de prueba
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
            AnimatedVisibility(visible = screenVisible, enter = fadeIn(tween(600)) + scaleIn(initialScale = 0.9f, animationSpec = tween(600))) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(120.dp)) {
                    Box(modifier = Modifier.size(130.dp).background(brush = Brush.radialGradient(colors = listOf(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f), Color.Transparent)), shape = CircleShape))
                    Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(100.dp), tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            AnimatedVisibility(visible = screenVisible, enter = fadeIn(tween(600, delayMillis = 200))) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Crea tu cuenta", fontSize = 26.sp, fontFamily = PoppinsBold, color = MaterialTheme.colorScheme.onBackground)
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Sincroniza tus animes en la nube", fontSize = 14.sp, fontFamily = PoppinsRegular, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f), textAlign = TextAlign.Center)
                        Text("Cambia de celular sin perder nada, desbloquea logros y accede a todas las características de Seijaku List", fontSize = 13.sp, fontFamily = PoppinsRegular, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f), textAlign = TextAlign.Center, lineHeight = 18.sp, modifier = Modifier.padding(horizontal = 8.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            AnimatedVisibility(visible = screenVisible, enter = fadeIn(tween(600, delayMillis = 400)) + slideInVertically(initialOffsetY = { 30 }, animationSpec = tween(600))) {
                Button(onClick = { navController.navigate(AppDestinations.AUTH_ROUTE) }, modifier = Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) {
                    Text("Comenzar", fontSize = 16.sp, fontFamily = PoppinsBold)
                }
            }
            Spacer(modifier = Modifier.height(100.dp))
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

    if (uiState.userProfile == null) { EmptyProfileScreen(navController); return }
    if (uiState.isLoading) { Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }; return }

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

    val topGenre = uiState.stats.genreStats.maxByOrNull { it.value }?.key ?: "Sin género"
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

    LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {

        // ── 1. HEADER COMPLETO (avatar + logros + stats 6) ────────────────────
        item {
            ProfileHeader(
                username          = username,
                userBio           = userBio,
                profilePictureUrl = profilePictureUrl,
                onEditClick       = { navController.navigate(AppDestinations.PROFILE_SETUP_ROUTE) },
                totalAnimes       = uiState.stats.totalAnimes,
                completedAnimes   = uiState.stats.completedAnimes,
                totalEpisodes     = uiState.stats.totalEpisodesWatched,
                totalMangas       = 0,
                topGenre          = topGenre,
                averageScore      = averageScore,
                achievements      = achievements
            )
        }

        // ── 2. TAB + GÉNEROS ──────────────────────────────────────────────────
        item {
            Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                CustomSeijakuTabSelector(
                    tabs = listOf("Anime", "Manga"),
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = { selectedTabIndex = it }
                )
                Spacer(modifier = Modifier.height(16.dp))
                AnimatedContent(
                    targetState = selectedTabIndex,
                    label = "TabAnimation",
                    transitionSpec = { fadeIn(tween(400)) + scaleIn(initialScale = 0.95f) togetherWith fadeOut(tween(400)) }
                ) { targetIndex ->
                    if (targetIndex == 0) {
                        Column(
                            modifier = Modifier
                        ) {
                            if (uiState.stats.genreStats.isNotEmpty()) {
                                SectionHeader(
                                    title = "Géneros Favoritos",
                                    subtitle = "Resumen de los géneros que más te gustan"
                                )
                                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    val topGenres        = uiState.stats.genreStats.entries.sortedByDescending { it.value }.take(3)
                                    val totalGenreCounts = topGenres.sumOf { it.value }
                                    val genreColors      = listOf(Color(0xFF3B82F6), Color(0xFF8B5CF6), Color(0xFFEC4899))
                                    topGenres.forEachIndexed { index, (genre, count) ->
                                        val animeWithGenre = uiState.allSavedAnimes.firstOrNull { anime ->
                                            anime.genres.split(",").any { it.trim().equals(genre, ignoreCase = true) }
                                        }
                                        GenreFavoriteCard(genre = genre, count = count, totalCount = totalGenreCounts, imageUrl = animeWithGenre?.imageUrl, accentColor = genreColors.getOrElse(index) { Color(0xFF3B82F6) }, modifier = Modifier.fillMaxWidth())
                                        if (index < topGenres.size - 1) Spacer(modifier = Modifier.height(10.dp))
                                    }
                                }
                            }
                            Row(modifier = Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                SectionHeader(
                                    "Top 5 Animes",
                                    subtitle = "Los 5 animes que más te gustan"
                                ) {
                                    IconButton(onClick = { navController.navigate(AppDestinations.SELECT_TOP5_ROUTE) }, modifier = Modifier.size(36.dp)) {
                                        Icon(Icons.Default.Edit, contentDescription = "Editar Top 5", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                                    }
                                }
                            }
                            if (uiState.top5Animes.isNotEmpty()) {
                                LazyRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)) {
                                    items(uiState.top5Animes.size) { index ->
                                        AnimeTop5Card(anime = uiState.top5Animes[index], position = index + 1, navController = navController)
                                    }
                                }
                            } else {
                                Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 32.dp), contentAlignment = Alignment.Center) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                        Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
                                        Text("Selecciona tus 5 animes favoritos", fontSize = 16.sp, fontFamily = PoppinsRegular, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                        FilledTonalButton(onClick = { navController.navigate(AppDestinations.SELECT_TOP5_ROUTE) }) {
                                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("Elegir Top 5", fontFamily = PoppinsBold)
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("¡Manga en camino!", fontSize = 20.sp, fontFamily = PoppinsBold, color = MaterialTheme.colorScheme.onBackground)
                            Text("Estamos trabajando para que puedas trackear tus lecturas muy pronto.", fontSize = 14.sp, fontFamily = PoppinsRegular, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.height(90.dp)) }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  ProfileHeader — banner completo con avatar, logros y 6 stats
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun ProfileHeader(
    username: String,
    userBio: String,
    profilePictureUrl: String?,
    totalAnimes: Int,
    completedAnimes: Int,
    totalEpisodes: Int,
    totalMangas: Int,
    topGenre: String,
    averageScore: Double,
    achievements: List<Achievement>,
    onEditClick: () -> Unit
) {
    val totalPosible = 20
    val progress = achievements.size.toFloat() / totalPosible.toFloat()
    var showAllAchievements by rememberSaveable { mutableStateOf(false) }

    if (showAllAchievements) {
        AllAchievementsDialog(achievements = achievements, onDismiss = { showAllAchievements = false })
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Avatar con anillo de progreso ──────────────────────────────────
            Box(contentAlignment = Alignment.Center) {
                Surface(
                    modifier = Modifier.size(104.dp),
                    shape = CircleShape,
                    border = BorderStroke(3.dp, MaterialTheme.colorScheme.background),
                    shadowElevation = 10.dp
                ) {
                    if (profilePictureUrl != null) {
                        AsyncImage(
                            model = profilePictureUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(54.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ── Nombre y bio ───────────────────────────────────────────────────
            Text(
                text = username,
                fontSize = 24.sp,
                fontFamily = PoppinsBold,
                letterSpacing = (-0.5).sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            if (userBio.isNotBlank()) {
                Text(
                    text = userBio,
                    fontSize = 13.sp,
                    fontFamily = PoppinsRegular,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 40.dp, vertical = 4.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            TextButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Editar perfil", fontSize = 12.sp, fontFamily = PoppinsMedium)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Stats: 6 datos en una sola tira ───────────────────────────────
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.5f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 14.dp, horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    VerticalStat("Animes", totalAnimes.toString())
                    VerticalDivider()
                    VerticalStat("Completados", completedAnimes.toString())
                    VerticalDivider()
                    VerticalStat("Episodios", totalEpisodes.toString())
                    VerticalDivider()
                    VerticalStat("Mangas", totalMangas.toString())
                    VerticalDivider()
                    VerticalStat("Score", if (averageScore > 0) "%.1f".format(averageScore) else "—")
                    VerticalDivider()
                    VerticalStat("Género", topGenre.take(8))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Logros ────────────────────────────────────────────────────────
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.6f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
                    // Header + emojis + "Ver todos" en una sola fila
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = Color(0xFFFF9800),
                                modifier = Modifier.size(16.dp)
                            )
                            Text("Logros", fontFamily = PoppinsBold, fontSize = 13.sp)
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFFF9800).copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "${achievements.size}/$totalPosible",
                                    fontSize = 10.sp,
                                    fontFamily = PoppinsBold,
                                    color = Color(0xFFFF9800),
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }

                        if (achievements.isNotEmpty()) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                achievements.take(5).forEach { achievement ->
                                    val color = when (achievement.typeAchievement) {
                                        "Legendary" -> Color(0xFFFF9800)
                                        "Epic"      -> Color(0xFF9C27B0)
                                        "Rare"      -> Color(0xFF2196F3)
                                        else        -> Color(0xFF4CAF50)
                                    }
                                    Box(
                                        modifier = Modifier
                                            .size(30.dp)
                                            .clip(CircleShape)
                                            .background(color.copy(alpha = 0.15f))
                                            .border(1.dp, color.copy(alpha = 0.35f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = achievement.icon,
                                            contentDescription = achievement.name,
                                            tint = color,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }

                        TextButton(
                            onClick = { showAllAchievements = true },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                        ) {
                            Text("Ver todos", fontSize = 11.sp, fontFamily = PoppinsMedium)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().height(5.dp).clip(CircleShape),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun VerticalStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 15.sp, fontFamily = PoppinsBold)
        Text(text = label, fontSize = 10.sp, fontFamily = PoppinsRegular, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun VerticalDivider() {
    Box(modifier = Modifier.width(1.dp).height(30.dp).background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)))
}

// ─────────────────────────────────────────────────────────────────────────────
//  BannerStatCard — stat card adaptada al fondo del banner
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun BannerStatCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(color.copy(alpha = 0.22f))
            .border(1.dp, color.copy(alpha = 0.55f), RoundedCornerShape(14.dp))
            .padding(vertical = 10.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            Text(text = value, fontSize = 15.sp, fontFamily = PoppinsBold, color = MaterialTheme.colorScheme.onSurface, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(text = label, fontSize = 10.sp, fontFamily = PoppinsRegular, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
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
            modifier = Modifier.fillMaxWidth(0.95f).fillMaxHeight(0.85f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Color(0xFFFF9800), modifier = Modifier.size(22.dp))
                        Text("Todos los logros", fontSize = 18.sp, fontFamily = PoppinsBold, color = MaterialTheme.colorScheme.onSurface)
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(achievements) { achievement ->
                        AchievementCard(achievement = achievement)
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  CustomSeijakuTabSelector
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun CustomSeijakuTabSelector(tabs: List<String>, selectedTabIndex: Int, onTabSelected: (Int) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp).height(48.dp),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val tabWidth = maxWidth / tabs.size
            val indicatorOffset by animateDpAsState(targetValue = tabWidth * selectedTabIndex, animationSpec = spring(stiffness = Spring.StiffnessLow), label = "indicator")
            Box(modifier = Modifier.offset(x = indicatorOffset).width(tabWidth).fillMaxHeight().padding(4.dp).background(MaterialTheme.colorScheme.primary, CircleShape))
            Row(modifier = Modifier.fillMaxSize()) {
                tabs.forEachIndexed { index, title ->
                    Box(modifier = Modifier.weight(1f).fillMaxHeight().clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onTabSelected(index) }, contentAlignment = Alignment.Center) {
                        Text(title, fontFamily = PoppinsBold, fontSize = 14.sp, color = if (selectedTabIndex == index) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
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
private fun GenreFavoriteCard(genre: String, count: Int, totalCount: Int, imageUrl: String?, accentColor: Color, modifier: Modifier = Modifier) {
    val percentage = if (totalCount > 0) ((count.toFloat() / totalCount) * 100).toInt() else 0
    ElevatedCard(modifier = modifier.fillMaxWidth().height(80.dp), shape = RoundedCornerShape(12.dp), elevation = CardDefaults.cardElevation(defaultElevation = 6.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (imageUrl != null) AsyncImage(model = imageUrl, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            Box(modifier = Modifier.fillMaxSize().background(brush = Brush.horizontalGradient(colors = listOf(Color.Black.copy(alpha = 0.85f), Color.Black.copy(alpha = 0.75f), accentColor.copy(alpha = 0.7f), accentColor.copy(alpha = 0.5f)))))
            Row(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(genre, fontSize = 24.sp, fontFamily = PoppinsBold, color = Color.White, modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.width(12.dp))
                Column(horizontalAlignment = Alignment.End) {
                    Text("$percentage%", fontSize = 28.sp, fontFamily = PoppinsBold, color = Color.White)
                    Text(if (count == 1) "$count guardado" else "$count guardados", fontSize = 12.sp, fontFamily = PoppinsRegular, color = Color.White.copy(alpha = 0.9f))
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  AnimeTop5Card
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun AnimeTop5Card(anime: AnimeEntity, position: Int, navController: NavController) {
    val infiniteTransition = androidx.compose.animation.core.rememberInfiniteTransition(label = "shimmer")
    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 0.6f,
        animationSpec = androidx.compose.animation.core.infiniteRepeatable(animation = tween(2000, easing = androidx.compose.animation.core.FastOutSlowInEasing), repeatMode = androidx.compose.animation.core.RepeatMode.Reverse),
        label = "shimmerAlpha"
    )
    val (bgColor, textColor) = when (position) {
        1 -> Color(0xFFFFD700) to Color.Black; 2 -> Color(0xFFC0C0C0) to Color.Black
        3 -> Color(0xFFCD7F32) to Color.White; 4 -> Color(0xFF6366F1) to Color.White
        5 -> Color(0xFFEC4899) to Color.White
        else -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
    }
    val borderModifier = if (position <= 3) Modifier.border(width = 2.dp, brush = Brush.linearGradient(colors = listOf(bgColor.copy(alpha = 0.8f), bgColor.copy(alpha = 0.4f), bgColor.copy(alpha = 0.8f))), shape = RoundedCornerShape(16.dp)) else Modifier
    Card(modifier = Modifier.width(160.dp).height(230.dp).then(borderModifier), shape = RoundedCornerShape(16.dp), onClick = { navController.navigate("${AppDestinations.ANIME_DETAIL_LOCAL_ROUTE}/${anime.malId}") }) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(model = anime.imageUrl, contentDescription = anime.title, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(colors = listOf(Color.Black.copy(alpha = 0.3f), Color.Transparent, Color.Transparent, Color.Black.copy(alpha = 0.6f), Color.Black.copy(alpha = 0.85f)))))
            if (position <= 3) Box(modifier = Modifier.fillMaxSize().background(Brush.radialGradient(colors = listOf(bgColor.copy(alpha = shimmerAlpha * 0.15f), Color.Transparent), center = androidx.compose.ui.geometry.Offset(0f, 0f), radius = 800f)))
            Surface(modifier = Modifier.align(Alignment.TopStart).padding(10.dp), shape = RoundedCornerShape(8.dp), color = bgColor, shadowElevation = if (position <= 3) 6.dp else 4.dp) { Text("#$position", fontSize = 14.sp, fontFamily = PoppinsBold, color = textColor, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) }
            if (anime.userScore > 0) {
                Surface(modifier = Modifier.align(Alignment.TopEnd).padding(10.dp), shape = RoundedCornerShape(8.dp), color = Color.Black.copy(alpha = 0.7f)) {
                    Row(modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(14.dp))
                        Text("${anime.userScore}", fontSize = 12.sp, fontFamily = PoppinsBold, color = Color.White)
                    }
                }
            }
            Column(modifier = Modifier.align(Alignment.BottomStart).fillMaxWidth().padding(10.dp)) { Text(anime.title, fontSize = 13.sp, fontFamily = PoppinsBold, color = Color.White, maxLines = 2, overflow = TextOverflow.Ellipsis, lineHeight = 16.sp) }
        }
    }
}

// ────────────────────────────────────────────|─────────────────────────────────
//  Achievement + AchievementCard + AchievementDetailDialog
// ─────────────────────────────────────────────────────────────────────────────
data class Achievement(val name: String, val description: String, val typeAchievement: String, val unlockDate: String = "Mayo 2025", val reward: String = "Insignia exclusiva", val icon: ImageVector = Icons.Default.EmojiEvents)

@Composable
private fun AchievementCard(achievement: Achievement, onClick: () -> Unit = {}) {
    val color = when (achievement.typeAchievement) {
        "Normal" -> Color(0xFF4CAF50); "Rare" -> Color(0xFF2196F3)
        "Epic" -> Color(0xFF9C27B0); "Legendary" -> Color(0xFFFF9800)
        else -> MaterialTheme.colorScheme.primary
    }
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), onClick = onClick) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(color.copy(alpha = 0.15f)).border(width = 1.5.dp, color = color, shape = CircleShape), contentAlignment = Alignment.Center) {
                Icon(imageVector = achievement.icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(achievement.name, fontSize = 14.sp, fontFamily = PoppinsBold, color = MaterialTheme.colorScheme.onSurface, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f, fill = false))
                    Surface(shape = RoundedCornerShape(6.dp), color = color.copy(alpha = 0.15f), border = BorderStroke(1.dp, color.copy(alpha = 0.3f))) { Text(achievement.typeAchievement, fontSize = 10.sp, fontFamily = PoppinsBold, color = color, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)) }
                }
                Text(achievement.description, fontSize = 12.sp, fontFamily = PoppinsRegular, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis, lineHeight = 16.sp)
            }
        }
    }
}

@Composable
private fun AchievementDetailDialog(achievement: Achievement, onDismiss: () -> Unit) {
    val color = when (achievement.typeAchievement) {
        "Normal" -> Color(0xFF4CAF50); "Rare" -> Color(0xFF2196F3)
        "Epic" -> Color(0xFF9C27B0); "Legendary" -> Color(0xFFFF9800)
        else -> MaterialTheme.colorScheme.primary
    }
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        ElevatedCard(modifier = Modifier.fillMaxWidth(0.9f).wrapContentHeight(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
            Column(modifier = Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) { IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) { Icon(Icons.Default.Close, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) } }
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier.size(120.dp).clip(CircleShape).background(Brush.radialGradient(colors = listOf(color.copy(alpha = 0.8f), color.copy(alpha = 0.4f)))).border(width = 4.dp, color = color, shape = CircleShape), contentAlignment = Alignment.Center) {
                    Icon(imageVector = achievement.icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(56.dp))
                }
                Spacer(modifier = Modifier.height(24.dp))
                Surface(shape = RoundedCornerShape(12.dp), color = color.copy(alpha = 0.2f)) { Text(achievement.typeAchievement, fontSize = 14.sp, fontFamily = PoppinsBold, color = color, modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) }
                Spacer(modifier = Modifier.height(16.dp))
                Text(achievement.name, fontSize = 24.sp, fontFamily = PoppinsBold, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(12.dp))
                Text(achievement.description, fontSize = 16.sp, fontFamily = PoppinsRegular, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center, lineHeight = 22.sp)
                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(modifier = Modifier.height(16.dp))
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.CalendarToday, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp)); Text("Desbloqueado", fontSize = 14.sp, fontFamily = PoppinsRegular, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                        Text(achievement.unlockDate, fontSize = 14.sp, fontFamily = PoppinsBold, color = MaterialTheme.colorScheme.onSurface)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp)); Text("Recompensa", fontSize = 14.sp, fontFamily = PoppinsRegular, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                        Text(achievement.reward, fontSize = 14.sp, fontFamily = PoppinsBold, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) { Text("Cerrar", fontFamily = PoppinsBold, fontSize = 16.sp) }
            }
        }
    }
}
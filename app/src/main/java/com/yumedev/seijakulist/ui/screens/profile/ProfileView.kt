package com.yumedev.seijakulist.ui.screens.profile

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.yumedev.seijakulist.R
import com.yumedev.seijakulist.data.local.entities.AnimeEntity
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import com.yumedev.seijakulist.ui.components.DonutChart
import com.yumedev.seijakulist.ui.components.PieChartData
import com.yumedev.seijakulist.ui.screens.auth_screen.WrappedStatCard

// Datos de prueba
val favoriteAnimes = listOf("Shingeki no Kyojin", "Steins;Gate", "Fullmetal Alchemist: Brotherhood", "Hunter x Hunter", "Vinland Saga")
val favoriteCharacters = listOf("Levi Ackerman", "Rintarou Okabe", "Edward Elric", "Killua Zoldyck", "Askeladd")
val achievements = listOf(
    Achievement("Pionero", "Te uniste en el primer mes de SeijakuList", "Legendary"),
    Achievement("Analista", "Escribiste tu primera reseña", "Normal"),
    Achievement("Crítico", "Escribiste 10 reseñas", "Rare"),
    Achievement("Veterano", "Llevas 1 año en SeijakuList", "Epic"),
    Achievement("Coleccionista Principiante", "Añadiste 10 animes a tu lista", "Normal"),
    Achievement("Maratonista", "Marcaste 50 episodios como vistos en una semana", "Epic"),
    Achievement("Coleccionista Avanzado", "Añadiste 50 animes a tu lista", "Rare"),
    Achievement("Coleccionista Experto", "Añadiste 100 animes a tu lista", "Legendary"),
    Achievement("Maratonista Avanzado", "Marcaste 100 episodios como vistos en una semana", "Legendary"),
    Achievement("Coleccionista Master", "Añadiste 200 animes a tu lista", "Legendary")
)

@Composable
private fun EmptyProfileScreen(navController: NavController) {
    var screenVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(100)
        screenVisible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Ilustración animada
            androidx.compose.animation.AnimatedVisibility(
                visible = screenVisible,
                enter = androidx.compose.animation.fadeIn(androidx.compose.animation.core.tween(600)) +
                        androidx.compose.animation.scaleIn(
                            initialScale = 0.9f,
                            animationSpec = androidx.compose.animation.core.tween(600)
                        )
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(120.dp)
                ) {
                    // Círculo decorativo
                    Box(
                        modifier = Modifier
                            .size(130.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                        Color.Transparent
                                    )
                                ),
                                shape = CircleShape
                            )
                    )

                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(100.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Título simple
            androidx.compose.animation.AnimatedVisibility(
                visible = screenVisible,
                enter = androidx.compose.animation.fadeIn(androidx.compose.animation.core.tween(600, delayMillis = 200))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Crea tu cuenta",
                        fontSize = 26.sp,
                        fontFamily = PoppinsBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Sincroniza tus animes en la nube",
                            fontSize = 14.sp,
                            fontFamily = PoppinsRegular,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "Cambia de celular sin perder nada, desbloquea logros y accede a todas las características de SeijakuList",
                            fontSize = 13.sp,
                            fontFamily = PoppinsRegular,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón principal
            androidx.compose.animation.AnimatedVisibility(
                visible = screenVisible,
                enter = androidx.compose.animation.fadeIn(androidx.compose.animation.core.tween(600, delayMillis = 400)) +
                        androidx.compose.animation.slideInVertically(
                            initialOffsetY = { 30 },
                            animationSpec = androidx.compose.animation.core.tween(600)
                        )
            ) {
                Button(
                    onClick = { navController.navigate(AppDestinations.AUTH_ROUTE) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Comenzar",
                        fontSize = 16.sp,
                        fontFamily = PoppinsBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun FeatureItem(
    icon: ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(24.dp)
            )
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontFamily = PoppinsBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                fontSize = 14.sp,
                fontFamily = PoppinsRegular,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ProfileView(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by profileViewModel.uiState.collectAsState()

    val averageScore: Double = uiState.allSavedAnimes
        .map { it.userScore } // Filtra los nulos y crea una lista de números
        .filter { it > 0 }          // Opcional: Ignora los que son 0 si eso significa "sin puntaje"
        .average()                  // Calcula el promedio automáticamente
        .takeIf { !it.isNaN() } ?: 0.0 // Si la lista estaba vacía, devuelve 0.0 en vez de NaN

    // 1. Muestra una pantalla mejorada si el perfil aún es nulo.
    if (uiState.userProfile == null) {
        EmptyProfileScreen(navController)
    } else if (uiState.isLoading) {
        CircularProgressIndicator()
    }

    // 2. Accede a los datos del perfil directamente del uiState.
    val userProfile = uiState.userProfile
    val username = userProfile?.username ?: "Nombre de usuario"
    val profilePictureUrl = userProfile?.profilePictureUrl
    val userBio = userProfile?.bio ?: "Añade tu biografia!"

    if (userProfile != null) {
        val listState = androidx.compose.foundation.lazy.rememberLazyListState()

        // Actualizar el ViewModel de manera más reactiva usando snapshotFlow
        androidx.compose.runtime.LaunchedEffect(listState) {
            androidx.compose.runtime.snapshotFlow {
                listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset < 100
            }.collect { isAtTop ->
                profileViewModel.updateScrollPosition(isAtTop)
            }
        }

        // Detectar si estamos en la parte superior (header visible) para animaciones locales
        val isAtTop = remember {
            androidx.compose.runtime.derivedStateOf {
                listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset < 100
            }
        }

        // Animar el color del header basado en la posición del scroll
        val headerColor by androidx.compose.animation.animateColorAsState(
            targetValue = if (isAtTop.value) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.background
            },
            animationSpec = androidx.compose.animation.core.tween(200),
            label = "headerColor"
        )

        // Animar el color del texto basado en la posición del scroll
        val textColor by androidx.compose.animation.animateColorAsState(
            targetValue = if (isAtTop.value) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.onBackground
            },
            animationSpec = androidx.compose.animation.core.tween(200),
            label = "textColor"
        )

        val topGenre: String = uiState.stats.genreStats
            .maxByOrNull { it.value } // Busca el que tenga el valor más alto
            ?.key ?: "Sin género"    // Toma la llave (el nombre) o un valor por defecto

        // Al inicio del Composable, junto a los otros remember
        val statsVisible = rememberSaveable { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            statsVisible.value = true
        }

        var selectedTabIndex by rememberSaveable { mutableStateOf(0) }
        val tabs = listOf("Anime", "Manga")

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState
        ) {
            // Header con perfil
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
                    color = headerColor,
                    shadowElevation = 4.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp)
                    ) {
                        // Botón de editar perfil flotante
                        Surface(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(16.dp)
                                .zIndex(1f),
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primary,
                            shadowElevation = 4.dp,
                            onClick = { navController.navigate(AppDestinations.PROFILE_SETUP_ROUTE) }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Editar perfil",
                                    modifier = Modifier.size(18.dp),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                                Text(
                                    text = "Editar",
                                    fontFamily = PoppinsBold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }

                        // Contenido del header
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 32.dp, bottom = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Foto de perfil
                            Surface(
                                modifier = Modifier.size(120.dp),
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.surface,
                                shadowElevation = 8.dp
                            ) {
                                if (profilePictureUrl != null) {
                                    AsyncImage(
                                        model = profilePictureUrl,
                                        contentDescription = "Foto de perfil",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .border(
                                                width = 4.dp,
                                                color = MaterialTheme.colorScheme.primaryContainer,
                                                shape = CircleShape
                                            ),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("user_default.lottie"))
                                    LottieAnimation(
                                        composition = composition,
                                        iterations = LottieConstants.IterateForever,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .border(
                                                width = 4.dp,
                                                color = MaterialTheme.colorScheme.primaryContainer,
                                                shape = CircleShape
                                            ),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Nombre de usuario
                            Text(
                                text = username,
                                fontSize = 28.sp,
                                fontFamily = PoppinsBold,
                                color = textColor
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Bio
                            Text(
                                text = userBio,
                                fontSize = 15.sp,
                                fontFamily = PoppinsRegular,
                                color = textColor.copy(alpha = 0.8f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 32.dp),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            // Estadísticas - Diseño compacto
            item {
                AnimatedVisibility(
                    visible = statsVisible.value,
                    enter = fadeIn(animationSpec = tween(800)) +
                            slideInVertically(
                                initialOffsetY = { -it / 3 }, // El valor negativo hace que venga de ARRIBA
                                animationSpec = tween(800, easing = EaseOutBack) // EaseOutBack da un pequeño rebote al final
                            )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            WrappedStatCard(
                                icon = Icons.Default.Tv,
                                value = uiState.stats.totalAnimes.toString(),
                                label = "Total de animes",
                                color = Color(0xFF8B5CF6),
                                modifier = Modifier.weight(1f)
                            )
                            WrappedStatCard(
                                icon = Icons.Default.Check,
                                value = uiState.stats.completedAnimes.toString(),
                                label = "completados",
                                color = Color(0xFF10B981),
                                modifier = Modifier.weight(1f)
                            )
                            WrappedStatCard(
                                icon = Icons.Default.PlayArrow,
                                value = uiState.stats.totalEpisodesWatched.toString(),
                                label = "episodios",
                                color = Color(0xFF06B6D4),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // Segunda fila
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            WrappedStatCard(
                                icon = Icons.Default.Book,
                                value = "0",
                                label = "Total de mangas",
                                color = Color(0xFFFF6B6B),
                                modifier = Modifier.weight(1f)
                            )
                            WrappedStatCard(
                                icon = Icons.Default.Favorite,
                                value = topGenre,
                                label = "top género",
                                color = Color(0xFFFF8E53),
                                modifier = Modifier.weight(1f)
                            )
                            WrappedStatCard(
                                icon = Icons.Default.Star,
                                value = "%.1f".format(averageScore),
                                label = "promedio",
                                color = Color(0xFFFFD700),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            item {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {

                    CustomSeijakuTabSelector(
                        tabs = listOf("Anime", "Manga"),
                        selectedTabIndex = selectedTabIndex,
                        onTabSelected = { selectedTabIndex = it }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AnimatedContent(
                        targetState = selectedTabIndex,
                        label = "TabAnimation",
                        transitionSpec = {
                            fadeIn(tween(400)) + scaleIn(initialScale = 0.95f) togetherWith fadeOut(tween(400))
                        }
                    ) { targetIndex ->
                        if (targetIndex == 0) {
                            // --- VISTA DE ANIME (Aquí recuperamos tus stats) ---
                            if (uiState.stats.genreStats.isNotEmpty()) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp)
                                ) {
                                    Text(
                                        text = "Géneros Favoritos",
                                        fontSize = 18.sp,
                                        fontFamily = PoppinsBold,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // Lógica de cálculo que se había perdido
                                    val topGenres = uiState.stats.genreStats
                                        .entries
                                        .sortedByDescending { it.value }
                                        .take(3)

                                    val totalGenreCounts = topGenres.sumOf { it.value }
                                    val genreColors = listOf(Color(0xFF3B82F6), Color(0xFF8B5CF6), Color(0xFFEC4899))

                                    // Pintar las tarjetas de nuevo
                                    topGenres.forEachIndexed { index, (genre, count) ->
                                        val animeWithGenre = uiState.allSavedAnimes.firstOrNull { anime ->
                                            anime.genres.split(",").any { it.trim().equals(genre, ignoreCase = true) }
                                        }

                                        GenreFavoriteCard(
                                            genre = genre,
                                            count = count,
                                            totalCount = totalGenreCounts,
                                            imageUrl = animeWithGenre?.imageUrl,
                                            accentColor = genreColors.getOrElse(index) { Color(0xFF3B82F6) },
                                            modifier = Modifier.fillMaxWidth()
                                        )

                                        if (index < topGenres.size - 1) {
                                            Spacer(modifier = Modifier.height(10.dp))
                                        }
                                    }
                                }
                            }
                        } else {
                            // --- VISTA DE MANGA (Coming Soon) ---
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "¡Manga en camino!",
                                    fontSize = 20.sp,
                                    fontFamily = PoppinsBold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    text = "Estamos trabajando para que puedas trackear tus lecturas muy pronto.",
                                    fontSize = 14.sp,
                                    fontFamily = PoppinsRegular,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            // Top 5 Animes
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Top 5 Animes",
                        fontSize = 20.sp,
                        fontFamily = PoppinsBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    IconButton(
                        onClick = { navController.navigate(AppDestinations.SELECT_TOP5_ROUTE) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar Top 5",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                if (uiState.top5Animes.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        items(uiState.top5Animes.size) { index ->
                            AnimeTop5Card(
                                anime = uiState.top5Animes[index],
                                position = index + 1,
                                navController = navController
                            )
                        }
                    }
                } else {
                    // Placeholder cuando no hay top 5
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            )
                            Text(
                                text = "Selecciona tus 5 animes favoritos",
                                fontSize = 16.sp,
                                fontFamily = PoppinsRegular,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            FilledTonalButton(onClick = { navController.navigate(AppDestinations.SELECT_TOP5_ROUTE) }) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Elegir Top 5", fontFamily = PoppinsBold)
                            }
                        }
                    }
                }
            }

            // Logros
            item {
                var expanded by rememberSaveable { mutableStateOf(false) }
                var selectedAchievement by remember { mutableStateOf<Achievement?>(null) }
                val achievementsToShow = if (expanded) achievements else achievements.take(5)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Logros",
                            fontSize = 20.sp,
                            fontFamily = PoppinsBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "${achievements.size} desbloqueados",
                            fontSize = 13.sp,
                            fontFamily = PoppinsRegular,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    achievementsToShow.forEach { achievement ->
                        AchievementCard(
                            achievement = achievement,
                            onClick = { selectedAchievement = achievement }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Diálogo de detalles del logro
                    selectedAchievement?.let { achievement ->
                        AchievementDetailDialog(
                            achievement = achievement,
                            onDismiss = { selectedAchievement = null }
                        )
                    }

                    if (achievements.size > 5) {
                        FilledTonalButton(
                            onClick = { expanded = !expanded },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = if (expanded) "Mostrar menos" else "Mostrar más logros",
                                fontFamily = PoppinsBold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
private fun StatChip(
    icon: ImageVector,
    value: String,
    label: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Label e icono
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )

            Text(
                text = label,
                fontSize = 14.sp,
                fontFamily = PoppinsRegular,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Valor
        Text(
            text = value,
            fontSize = 16.sp,
            fontFamily = PoppinsBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun InfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = label,
                fontSize = 13.sp,
                fontFamily = PoppinsRegular,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                fontSize = 16.sp,
                fontFamily = PoppinsBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun FavoriteCard(title: String, position: Int) {
    ElevatedCard(
        modifier = Modifier
            .width(180.dp)
            .height(240.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceContainerHigh,
                            MaterialTheme.colorScheme.surfaceContainer
                        )
                    )
                )
        ) {
            // Badge de posición
            if (position <= 3) {
                val (icon, bgColor) = when (position) {
                    1 -> "🥇" to Color(0xFFFFD700)
                    2 -> "🥈" to Color(0xFFC0C0C0)
                    else -> "🥉" to Color(0xFFCD7F32)
                }
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .size(40.dp),
                    shape = CircleShape,
                    color = bgColor.copy(alpha = 0.2f)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = icon,
                            fontSize = 20.sp
                        )
                    }
                }
            } else {
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "#$position",
                        fontSize = 14.sp,
                        fontFamily = PoppinsBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Título centrado
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontFamily = PoppinsBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 22.sp
                )
            }
        }
    }
}


@Composable
private fun AnimeTop5Card(anime: AnimeEntity, position: Int, navController: NavController) {
    // Animación sutil de brillo
    val infiniteTransition = androidx.compose.animation.core.rememberInfiniteTransition(label = "shimmer")
    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = androidx.compose.animation.core.infiniteRepeatable(
            animation = androidx.compose.animation.core.tween(2000, easing = androidx.compose.animation.core.FastOutSlowInEasing),
            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
        ),
        label = "shimmerAlpha"
    )

    // Badge de posición y colores
    val (bgColor, textColor) = when (position) {
        1 -> Color(0xFFFFD700) to Color.Black  // Oro
        2 -> Color(0xFFC0C0C0) to Color.Black  // Plata
        3 -> Color(0xFFCD7F32) to Color.White  // Bronce
        4 -> Color(0xFF6366F1) to Color.White  // Índigo
        5 -> Color(0xFFEC4899) to Color.White  // Rosa
        else -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
    }

    // Borde especial solo para top 3
    val borderModifier = if (position <= 3) {
        Modifier.border(
            width = 2.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    bgColor.copy(alpha = 0.8f),
                    bgColor.copy(alpha = 0.4f),
                    bgColor.copy(alpha = 0.8f)
                )
            ),
            shape = RoundedCornerShape(16.dp)
        )
    } else {
        Modifier
    }

    Card(
        modifier = Modifier
            .width(160.dp)
            .height(230.dp)
            .then(borderModifier),
        shape = RoundedCornerShape(16.dp),
        onClick = {
            navController.navigate("${AppDestinations.ANIME_DETAIL_LOCAL_ROUTE}/${anime.malId}")
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Imagen del anime
            AsyncImage(
                model = anime.imageUrl,
                contentDescription = anime.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradiente overlay mejorado
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.3f),
                                Color.Transparent,
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.6f),
                                Color.Black.copy(alpha = 0.85f)
                            )
                        )
                    )
            )

            // Efecto de brillo sutil para top 3
            if (position <= 3) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    bgColor.copy(alpha = shimmerAlpha * 0.15f),
                                    Color.Transparent,
                                    Color.Transparent
                                ),
                                center = androidx.compose.ui.geometry.Offset(0f, 0f),
                                radius = 800f
                            )
                        )
                )
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp),
                shape = RoundedCornerShape(8.dp),
                color = bgColor,
                shadowElevation = if (position <= 3) 6.dp else 4.dp
            ) {
                Text(
                    text = "#$position",
                    fontSize = 14.sp,
                    fontFamily = PoppinsBold,
                    color = textColor,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            // Puntuación del usuario en la esquina superior derecha
            if (anime.userScore > 0) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Black.copy(alpha = 0.7f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${anime.userScore}",
                            fontSize = 12.sp,
                            fontFamily = PoppinsBold,
                            color = Color.White
                        )
                    }
                }
            }

            // Título en la parte inferior con mejor legibilidad
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = anime.title,
                    fontSize = 13.sp,
                    fontFamily = PoppinsBold,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

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
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Imagen de fondo
            if (imageUrl != null) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Gradiente overlay de negro a color
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.85f),
                                Color.Black.copy(alpha = 0.75f),
                                accentColor.copy(alpha = 0.7f),
                                accentColor.copy(alpha = 0.5f)
                            )
                        )
                    )
            )

            // Contenido
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Nombre del género
                Text(
                    text = genre,
                    fontSize = 24.sp,
                    fontFamily = PoppinsBold,
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Estadísticas
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "$percentage%",
                        fontSize = 28.sp,
                        fontFamily = PoppinsBold,
                        color = Color.White
                    )
                    Text(
                        text = if (count == 1) "$count guardado" else "$count guardados",
                        fontSize = 12.sp,
                        fontFamily = PoppinsRegular,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }
    }
}

@Composable
fun CustomSeijakuTabSelector(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val density = LocalDensity.current

    // Contenedor principal estilo "Pastilla"
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .height(48.dp),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val tabWidth = maxWidth / tabs.size

            // Indicador animado (Fondo de la pestaña seleccionada)
            val indicatorOffset by animateDpAsState(
                targetValue = tabWidth * selectedTabIndex,
                animationSpec = spring(stiffness = Spring.StiffnessLow),
                label = "indicator"
            )

            Box(
                modifier = Modifier
                    .offset(x = indicatorOffset)
                    .width(tabWidth)
                    .fillMaxHeight()
                    .padding(4.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            )

            // Textos de las pestañas
            Row(modifier = Modifier.fillMaxSize()) {
                tabs.forEachIndexed { index, title ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null // Quitamos el ripple feo de Android
                            ) { onTabSelected(index) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            fontFamily = PoppinsBold,
                            fontSize = 14.sp,
                            color = if (selectedTabIndex == index)
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

data class Achievement(
    val name: String,
    val description: String,
    val typeAchievement: String,
    val unlockDate: String = "Mayo 2025",
    val reward: String = "Insignia exclusiva"
)

@Composable
private fun AchievementCard(
    achievement: Achievement,
    onClick: () -> Unit = {}
) {
    val (color, emoji) = when (achievement.typeAchievement) {
        "Normal" -> Color(0xFF4CAF50) to "🏆"
        "Rare" -> Color(0xFF2196F3) to "💎"
        "Epic" -> Color(0xFF9C27B0) to "⭐"
        "Legendary" -> Color(0xFFFF9800) to "👑"
        else -> MaterialTheme.colorScheme.primary to "🏆"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Icono más compacto
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f))
                    .border(
                        width = 1.5.dp,
                        color = color,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emoji,
                    fontSize = 22.sp
                )
            }

            // Detalles del logro
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = achievement.name,
                        fontSize = 14.sp,
                        fontFamily = PoppinsBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    // Badge de rareza más pequeño
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = color.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, color.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = achievement.typeAchievement,
                            fontSize = 10.sp,
                            fontFamily = PoppinsBold,
                            color = color,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Text(
                    text = achievement.description,
                    fontSize = 12.sp,
                    fontFamily = PoppinsRegular,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
@Composable
private fun AchievementDetailDialog(
    achievement: Achievement,
    onDismiss: () -> Unit
) {
    val (color, emoji) = when (achievement.typeAchievement) {
        "Normal" -> Color(0xFF4CAF50) to "🏆"
        "Rare" -> Color(0xFF2196F3) to "💎"
        "Epic" -> Color(0xFF9C27B0) to "⭐"
        "Legendary" -> Color(0xFFFF9800) to "👑"
        else -> MaterialTheme.colorScheme.primary to "🏆"
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Botón cerrar
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Icono grande del logro
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    color.copy(alpha = 0.8f),
                                    color.copy(alpha = 0.4f)
                                )
                            )
                        )
                        .border(
                            width = 4.dp,
                            color = color,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = emoji,
                        fontSize = 56.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Badge de rareza
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = color.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = achievement.typeAchievement,
                        fontSize = 14.sp,
                        fontFamily = PoppinsBold,
                        color = color,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Nombre del logro
                Text(
                    text = achievement.name,
                    fontSize = 24.sp,
                    fontFamily = PoppinsBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Descripción
                Text(
                    text = achievement.description,
                    fontSize = 16.sp,
                    fontFamily = PoppinsRegular,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                Spacer(modifier = Modifier.height(16.dp))

                // Información adicional
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Fecha de desbloqueo
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Desbloqueado",
                                fontSize = 14.sp,
                                fontFamily = PoppinsRegular,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = achievement.unlockDate,
                            fontSize = 14.sp,
                            fontFamily = PoppinsBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Recompensa
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Recompensa",
                                fontSize = 14.sp,
                                fontFamily = PoppinsRegular,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = achievement.reward,
                            fontSize = 14.sp,
                            fontFamily = PoppinsBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón de cerrar
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Cerrar",
                        fontFamily = PoppinsBold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

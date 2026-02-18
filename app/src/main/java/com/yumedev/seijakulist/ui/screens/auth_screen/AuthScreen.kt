package com.yumedev.seijakulist.ui.screens.auth_screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.yumedev.seijakulist.R
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AuthScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    settingsViewModel: com.yumedev.seijakulist.ui.screens.configuration.SettingsViewModel
) {
    val currentTheme by settingsViewModel.themeMode.collectAsState()
    var showThemeNotification by remember { mutableStateOf(false) }
    var notificationTheme by remember { mutableStateOf<com.yumedev.seijakulist.ui.theme.ThemeMode?>(null) }
    var previousTheme by remember { mutableStateOf<com.yumedev.seijakulist.ui.theme.ThemeMode?>(null) }

    // Páginas del onboarding
    val pages = remember {
        listOf(
            OnboardingPage(
                title = "Bienvenido a\nSeijaku List",
                description = "Guarda y organiza todos tus animes en un solo lugar",
                icon = Icons.Outlined.Favorite,
                color = Color(0xFF6366F1)
            ),
            OnboardingPage(
                title = "Nunca Olvides\nDónde Te Quedaste",
                description = "Marca episodios vistos y sincroniza tu progreso en la nube",
                icon = Icons.Outlined.Collections,
                color = Color(0xFF8B5CF6)
            ),
            OnboardingPage(
                title = "Descubre Tus\nEstadísticas",
                description = "Analiza tus gustos y el tiempo invertido en tu pasión",
                icon = Icons.Outlined.Analytics,
                color = Color(0xFFEC4899)
            ),
            OnboardingPage(
                title = "Comparte Tu\nTop 5",
                description = "Crea tu lista de favoritos y muéstrala al mundo",
                icon = Icons.Outlined.Share,
                color = Color(0xFF06B6D4)
            ),
            OnboardingPage(
                title = "Y Mucho más...",
                description = "Todo lo que necesitas para vivir tu experiencia anime al máximo",
                icon = Icons.Outlined.AutoAwesome,
                color = Color(0xFFA855F7)
            )
        )
    }

    val pagerState = rememberPagerState(pageCount = { pages.size + 1 }) // +1 para la página de acciones
    val scope = rememberCoroutineScope()

    // Mostrar notificación solo cuando el usuario cambia el tema activamente
    LaunchedEffect(currentTheme) {
        if (previousTheme != null && previousTheme != currentTheme) {
            notificationTheme = currentTheme
            showThemeNotification = true
            delay(2000)
            showThemeNotification = false
        }
        previousTheme = currentTheme
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Gradiente de fondo para la página de welcome (cubre pantalla completa desde arriba)
        val welcomeAlpha by animateFloatAsState(
            targetValue = if (pagerState.currentPage >= pages.size) 1f else 0f,
            animationSpec = tween(300),
            label = "welcomeGradientAlpha"
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(welcomeAlpha)
                .background(
                    Brush.verticalGradient(
                        0.0f to MaterialTheme.colorScheme.primary,
                        0.30f to MaterialTheme.colorScheme.tertiary.copy(alpha = 0.85f),
                        0.55f to MaterialTheme.colorScheme.background,
                        1.0f to MaterialTheme.colorScheme.background
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Paginador horizontal
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                if (page < pages.size) {
                    // Páginas de onboarding
                    OnboardingPageContent(
                        page = pages[page],
                        pageIndex = page,
                        totalPages = pages.size
                    )
                } else {
                    // Página final con acciones
                    ActionsPage(navController)
                }
            }

            // Indicadores y botones de navegación
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Indicadores de página
                if (pagerState.currentPage < pages.size) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(bottom = 24.dp)
                    ) {
                        repeat(pages.size) { index ->
                            PageIndicator(
                                isActive = index == pagerState.currentPage,
                                color = pages[pagerState.currentPage.coerceIn(0, pages.size - 1)].color
                            )
                        }
                    }

                    // Botón siguiente/comenzar
                    Button(
                        onClick = {
                            scope.launch {
                                if (pagerState.currentPage < pages.size) {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = pages[pagerState.currentPage].color
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = if (pagerState.currentPage == pages.size - 1) "Comenzar" else "Siguiente",
                                fontFamily = PoppinsBold,
                                fontSize = 16.sp
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Botón saltar
                    if (pagerState.currentPage < pages.size - 1) {
                        TextButton(
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(pages.size)
                                }
                            },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text(
                                text = "Saltar",
                                fontFamily = PoppinsRegular,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Notificación de cambio de tema (encima del pager)
        AnimatedVisibility(
            visible = showThemeNotification,
            enter = slideInVertically(
                initialOffsetY = { -it },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            ) + fadeIn(tween(300)),
            exit = slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            ) + fadeOut(tween(300)),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 8.dp)
        ) {
            notificationTheme?.let { theme ->
                ThemeChangeNotification(theme = theme)
            }
        }

        // Toggle de tema en la esquina superior derecha (encima del pager)
        if (pagerState.currentPage < pages.size) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(end = 16.dp)
            ) {
                ThemeToggleButton(
                    currentTheme = currentTheme,
                    onThemeChange = { settingsViewModel.setThemeMode(it) }
                )
            }
        }
    }
}

@Composable
private fun OnboardingPageContent(
    page: OnboardingPage,
    pageIndex: Int,
    totalPages: Int
) {
    var isVisible by remember { mutableStateOf(false) }
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")

    // Animación de flotación suave
    val floatingOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floating"
    )

    LaunchedEffect(Unit) {
        delay(150)
        isVisible = true
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Visual específico para cada página con flotación
            AnimatedVisibility(
                visible = isVisible,
                enter = scaleIn(
                    initialScale = 0.8f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                ) + fadeIn(tween(600, delayMillis = 100))
            ) {
                Box(
                    modifier = Modifier.graphicsLayer {
                        translationY = floatingOffset
                    }
                ) {
                    when (pageIndex) {
                        0 -> AnimeCardPreview(page.color)
                        1 -> ProgressTrackerPreview(page.color)
                        2 -> StatsChartPreview(page.color)
                        3 -> Top5Preview(page.color)
                        4 -> AndMorePreview(page.color)
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Título
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(
                    initialOffsetY = { 30 },
                    animationSpec = tween(600, delayMillis = 300, easing = FastOutSlowInEasing)
                ) + fadeIn(tween(600, delayMillis = 300))
            ) {
                Text(
                    text = page.title,
                    fontFamily = PoppinsBold,
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp,
                    letterSpacing = 0.3.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Descripción con icono decorativo
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(
                    initialOffsetY = { 30 },
                    animationSpec = tween(600, delayMillis = 450, easing = FastOutSlowInEasing)
                ) + fadeIn(tween(600, delayMillis = 450))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Línea decorativa
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(3.dp)
                            .background(
                                page.color.copy(alpha = 0.5f),
                                RoundedCornerShape(2.dp)
                            )
                    )

                    Text(
                        text = page.description,
                        fontFamily = PoppinsRegular,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp,
                        letterSpacing = 0.2.sp
                    )
                }
            }
        }
    }
}

// Página 1: Preview con 3 cards de anime (Frieren al frente, K-On y Haruhi atrás)
@Composable
private fun AnimeCardPreview(accentColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
        contentAlignment = Alignment.Center
    ) {
        // Card trasera izquierda - K-On!
        Surface(
            modifier = Modifier
                .size(width = 130.dp, height = 190.dp)
                .offset(x = (-90).dp, y = 15.dp)
                .graphicsLayer {
                    rotationZ = -8f
                    alpha = 0.45f
                },
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 6.dp
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://cdn.myanimelist.net/images/anime/10/76120l.jpg")
                    .crossfade(true)
                    .build(),
                contentDescription = "K-On!",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Card trasera derecha - Haruhi Suzumiya
        Surface(
            modifier = Modifier
                .size(width = 130.dp, height = 190.dp)
                .offset(x = 90.dp, y = 15.dp)
                .graphicsLayer {
                    rotationZ = 8f
                    alpha = 0.45f
                },
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 6.dp
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://cdn.myanimelist.net/images/anime/1470/137929l.jpg")
                    .crossfade(true)
                    .build(),
                contentDescription = "Haruhi Suzumiya",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Card principal al frente - Frieren
        Surface(
            modifier = Modifier.size(width = 170.dp, height = 240.dp),
            shape = RoundedCornerShape(20.dp),
            shadowElevation = 16.dp,
            tonalElevation = 2.dp
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://cdn.myanimelist.net/images/anime/1015/138006l.jpg")
                        .crossfade(true)
                        .build(),
                    contentDescription = "Frieren",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Gradiente oscuro en la parte inferior
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.75f)
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color.Black.copy(alpha = 0.6f)
                        ) {
                            Text(
                                text = "TV",
                                fontFamily = PoppinsBold,
                                fontSize = 9.sp,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = accentColor
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "9.3",
                                    fontFamily = PoppinsBold,
                                    fontSize = 11.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Sousou no Frieren",
                            fontFamily = PoppinsBold,
                            fontSize = 13.sp,
                            color = Color.White
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFF4CAF50).copy(alpha = 0.85f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .background(Color.White, CircleShape)
                                    )
                                    Text(
                                        text = "Viendo",
                                        fontFamily = PoppinsBold,
                                        fontSize = 10.sp,
                                        color = Color.White
                                    )
                                }
                            }

                            Text(
                                text = "18/28",
                                fontFamily = PoppinsRegular,
                                fontSize = 10.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        }
    }
}

// Página 2: 3 tarjetas horizontales estilo "Mis Animes"
@Composable
private fun ProgressTrackerPreview(accentColor: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "progress")

    val progress by infiniteTransition.animateFloat(
        initialValue = 0.62f,
        targetValue = 0.72f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "progress_animation"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        contentAlignment = Alignment.Center
    ) {
        // Tarjeta trasera superior - My Dress-Up Darling
        OnboardingAnimeCard(
            imageUrl = "https://cdn.myanimelist.net/images/anime/1179/119897l.jpg",
            title = "My Dress-Up Darling",
            type = "TV",
            year = "2022",
            episodesWatched = 12,
            totalEpisodes = 12,
            status = "Completado",
            accentColor = accentColor,
            modifier = Modifier
                .offset(y = (-100).dp)
                .graphicsLayer {
                    alpha = 0.4f
                    scaleX = 0.9f
                    scaleY = 0.9f
                }
        )

        // Tarjeta trasera inferior - Bocchi the Rock!
        OnboardingAnimeCard(
            imageUrl = "https://cdn.myanimelist.net/images/anime/1448/127956l.jpg",
            title = "Bocchi the Rock!",
            type = "TV",
            year = "2022",
            episodesWatched = 10,
            totalEpisodes = 12,
            status = "Completado",
            accentColor = accentColor,
            modifier = Modifier
                .offset(y = 100.dp)
                .graphicsLayer {
                    alpha = 0.4f
                    scaleX = 0.9f
                    scaleY = 0.9f
                }
        )

        // Tarjeta principal - Shikanoko
        OnboardingAnimeCard(
            imageUrl = "https://cdn.myanimelist.net/images/anime/1094/143324l.jpg",
            title = "Shikanoko Nokonoko\nKoshitantan",
            type = "TV",
            year = "2024",
            episodesWatched = 8,
            totalEpisodes = 12,
            status = "Viendo",
            accentColor = accentColor,
            progress = progress,
            modifier = Modifier
        )
    }
}

@Composable
private fun OnboardingAnimeCard(
    imageUrl: String,
    title: String,
    type: String,
    year: String,
    episodesWatched: Int,
    totalEpisodes: Int,
    status: String,
    accentColor: Color,
    modifier: Modifier = Modifier,
    progress: Float? = null
) {
    val episodeProgress = if (totalEpisodes > 0) {
        (episodesWatched.toFloat() / totalEpisodes.toFloat()).coerceIn(0f, 1f)
    } else 0f

    ElevatedCard(
        modifier = modifier
            .width(400.dp)
            .height(150.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Imagen
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp))
                )

                // Score badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color.Black.copy(alpha = 0.7f),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(11.dp)
                        )
                        Text(
                            text = "8.5",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontFamily = PoppinsBold
                        )
                    }
                }
            }

            // Contenido
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(start = 14.dp, top = 10.dp, bottom = 10.dp, end = 10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = title,
                        fontFamily = PoppinsBold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        lineHeight = 16.sp
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = type,
                            fontFamily = PoppinsRegular,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Text(
                            text = "•",
                            fontFamily = PoppinsRegular,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                        Text(
                            text = year,
                            fontFamily = PoppinsRegular,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }

                    // Episodios
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = accentColor
                        )
                        Text(
                            text = "$episodesWatched/$totalEpisodes",
                            fontFamily = PoppinsBold,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "eps",
                            fontFamily = PoppinsRegular,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }

                    // Barra de progreso
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(progress ?: episodeProgress)
                                .clip(RoundedCornerShape(3.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            accentColor,
                                            accentColor.copy(alpha = 0.7f)
                                        )
                                    )
                                )
                        )
                    }
                }

                val statusColor = when (status) {
                    "Viendo" -> Color(0xFF4CAF50)
                    "Completado" -> Color(0xFF06B6D4)
                    else -> accentColor
                }

                // Status chip
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = statusColor.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(statusColor, CircleShape)
                        )
                        Text(
                            text = status,
                            fontFamily = PoppinsBold,
                            fontSize = 10.sp,
                            color = statusColor
                        )
                    }
                }
            }
        }
    }
}

// Página 3: Estilo Wrapped/Recap
@Composable
private fun StatsChartPreview(accentColor: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "stats")

    val numberPulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "number_pulse"
    )

    Column(
        modifier = Modifier.width(300.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Número grande central
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.scale(numberPulse)
        ) {
            Text(
                text = "127",
                fontFamily = PoppinsBold,
                fontSize = 72.sp,
                color = accentColor,
                letterSpacing = (-3).sp,
                lineHeight = 72.sp
            )
            Text(
                text = "animes registrados",
                fontFamily = PoppinsRegular,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Fila de stat cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            WrappedStatCard(
                icon = Icons.Default.Timer,
                value = "342h",
                label = "vistas",
                color = accentColor,
                modifier = Modifier.weight(1f)
            )
            WrappedStatCard(
                icon = Icons.Default.LocalFireDepartment,
                value = "14",
                label = "días racha",
                color = Color(0xFFFF6B6B),
                modifier = Modifier.weight(1f)
            )
            WrappedStatCard(
                icon = Icons.Default.Favorite,
                value = "Acción",
                label = "top género",
                color = Color(0xFFFF8E53),
                modifier = Modifier.weight(1f)
            )
        }

        // Segunda fila
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            WrappedStatCard(
                icon = Icons.Default.PlayArrow,
                value = "2.4k",
                label = "episodios",
                color = Color(0xFF06B6D4),
                modifier = Modifier.weight(1f)
            )
            WrappedStatCard(
                icon = Icons.Default.Check,
                value = "89",
                label = "completados",
                color = Color(0xFF10B981),
                modifier = Modifier.weight(1f)
            )
            WrappedStatCard(
                icon = Icons.Default.Star,
                value = "8.2",
                label = "promedio",
                color = Color(0xFFFFD700),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun WrappedStatCard(
    icon: ImageVector,
    value: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.1f),
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontFamily = PoppinsBold,
                fontSize = 16.sp,
                color = color,
                letterSpacing = (-0.5).sp
            )
            Text(
                text = label,
                fontFamily = PoppinsRegular,
                fontSize = 9.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }
    }
}

// Página 4: Top 5 estilo podium con covers grandes
@Composable
private fun Top5Preview(accentColor: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Podium: #2, #1, #3
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.Bottom
        ) {
            // #2 - Oregairu (izquierda, más bajo)
            PodiumCard(
                imageUrl = "https://cdn.myanimelist.net/images/anime/11/75376l.jpg",
                title = "Oregairu",
                rank = 2,
                medalColor = Color(0xFFC0C0C0),
                accentColor = accentColor,
                imageHeight = 140,
                modifier = Modifier.weight(1f)
            )

            // #1 - K-On! Movie (centro, más alto)
            PodiumCard(
                imageUrl = "https://cdn.myanimelist.net/images/anime/5/76233l.jpg",
                title = "K-On! Movie",
                rank = 1,
                medalColor = Color(0xFFFFD700),
                accentColor = accentColor,
                imageHeight = 180,
                modifier = Modifier.weight(1f)
            )

            // #3 - Natsu e no Tunnel (derecha, más bajo)
            PodiumCard(
                imageUrl = "https://cdn.myanimelist.net/images/anime/1462/125397l.jpg",
                title = "Natsu e no\nTunnel",
                rank = 3,
                medalColor = Color(0xFFCD7F32),
                accentColor = accentColor,
                imageHeight = 130,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PodiumCard(
    imageUrl: String,
    title: String,
    rank: Int,
    medalColor: Color,
    accentColor: Color,
    imageHeight: Int,
    modifier: Modifier = Modifier
) {
    val isFirst = rank == 1

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Cover del anime
        Box(contentAlignment = Alignment.TopCenter) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight.dp),
                shape = RoundedCornerShape(16.dp),
                shadowElevation = if (isFirst) 12.dp else 6.dp,
                tonalElevation = if (isFirst) 2.dp else 0.dp
            ) {
                Box {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Gradiente inferior sutil
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.5f)
                                    )
                                )
                            )
                    )
                }
            }

            // Medalla flotante arriba
            Surface(
                modifier = Modifier
                    .offset(y = (-12).dp)
                    .size(if (isFirst) 30.dp else 24.dp),
                shape = CircleShape,
                color = medalColor,
                shadowElevation = 6.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$rank",
                        fontFamily = PoppinsBold,
                        fontSize = if (isFirst) 15.sp else 12.sp,
                        color = Color.White
                    )
                }
            }
        }

        // Título debajo
        Text(
            text = title,
            fontFamily = PoppinsBold,
            fontSize = if (isFirst) 12.sp else 10.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 2,
            lineHeight = 13.sp
        )
    }
}

// Página 5: "Y mucho más" - features grid
@Composable
private fun AndMorePreview(accentColor: Color) {
    data class FeatureItem(val icon: ImageVector, val label: String, val color: Color)

    val features = remember {
        listOf(
            FeatureItem(Icons.Outlined.Search, "Buscador", Color(0xFF6366F1)),
            FeatureItem(Icons.Outlined.People, "Personajes", Color(0xFFEC4899)),
            FeatureItem(Icons.Outlined.Palette, "Personaliza", Color(0xFF8B5CF6)),
            FeatureItem(Icons.Outlined.CloudSync, "Cloud Sync", Color(0xFF06B6D4)),
            FeatureItem(Icons.Outlined.Notifications, "Alertas", Color(0xFFFF6B6B)),
            FeatureItem(Icons.AutoMirrored.Outlined.TrendingUp, "Tendencias", Color(0xFF10B981))
        )
    }

    Column(
        modifier = Modifier
            .width(280.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Grid de features 3x2
        for (row in features.chunked(3)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                row.forEach { feature ->
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(90.dp),
                        shape = RoundedCornerShape(18.dp),
                        color = feature.color.copy(alpha = 0.08f),
                        tonalElevation = 1.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Surface(
                                modifier = Modifier.size(36.dp),
                                shape = CircleShape,
                                color = feature.color.copy(alpha = 0.15f)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = feature.icon,
                                        contentDescription = null,
                                        tint = feature.color,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = feature.label,
                                fontFamily = PoppinsBold,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionsPage(navController: NavController) {
    var headerVisible by remember { mutableStateOf(false) }
    var cardVisible by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "welcome_float")
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logo_float"
    )

    LaunchedEffect(Unit) {
        delay(100)
        headerVisible = true
        delay(250)
        cardVisible = true
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // ── Header con gradiente (igual que Login/Register) ───────────────
        WelcomeHeader(
            headerVisible = headerVisible,
            floatOffset = floatOffset
        )

        // ── Surface card (igual que Login/Register) ───────────────────────
        WelcomeActionsCard(
            cardVisible = cardVisible,
            navController = navController
        )
    }
}

@Composable
private fun WelcomeHeader(
    headerVisible: Boolean,
    floatOffset: Float
) {
    // Sin fondo — el gradiente ya viene del Box exterior de AuthScreen
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        contentAlignment = Alignment.Center
    ) {
        // Círculos decorativos (visibles sobre el gradiente exterior)
        Box(
            modifier = Modifier
                .size(220.dp)
                .align(Alignment.TopEnd)
                .offset(x = 70.dp, y = (-50).dp)
                .background(Color.White.copy(alpha = 0.07f), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-40).dp, y = 30.dp)
                .background(Color.White.copy(alpha = 0.06f), CircleShape)
        )

        // Logo + título (centrado por contentAlignment)
        AnimatedVisibility(
            visible = headerVisible,
            enter = fadeIn(tween(700)) + scaleIn(
                initialScale = 0.6f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.graphicsLayer { translationY = floatOffset }
            ) {
                // Glow + logo
                Box(contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.25f),
                                        Color.Transparent
                                    )
                                ),
                                CircleShape
                            )
                    )
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Seijaku List",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(20.dp))
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Seijaku List",
                    fontFamily = PoppinsBold,
                    fontSize = 30.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "Tu lista de anime y mangas, siempre contigo",
                    fontFamily = PoppinsRegular,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.82f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun WelcomeActionsCard(
    cardVisible: Boolean,
    navController: NavController
) {
    AnimatedVisibility(
        visible = cardVisible,
        enter = fadeIn(tween(500)) + slideInVertically(
            initialOffsetY = { 60 },
            animationSpec = tween(500, easing = FastOutSlowInEasing)
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-24).dp),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = MaterialTheme.colorScheme.background,
            shadowElevation = 0.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "¡Comienza tu aventura!",
                    fontFamily = PoppinsBold,
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Únete a miles de fans del anime",
                    fontFamily = PoppinsRegular,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Botón Crear Cuenta (gradiente)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(color = MaterialTheme.colorScheme.primary)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { navController.navigate(AppDestinations.REGISTER_ROUTE) },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.PersonAdd,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Crear cuenta",
                            fontFamily = PoppinsBold,
                            fontSize = 16.sp,
                            color = Color.White,
                            letterSpacing = 0.3.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Botón Iniciar Sesión (outline)
                OutlinedButton(
                    onClick = { navController.navigate(AppDestinations.LOGIN_ROUTE) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.5.dp,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Login,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Iniciar sesión",
                            fontFamily = PoppinsBold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 0.3.sp
                        )
                    }
                }

                // Explorar sin cuenta
                Spacer(Modifier.weight(1f))

                TextButton(
                    onClick = {
                        navController.navigate(AppDestinations.HOME) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Explorar sin cuenta",
                        fontFamily = PoppinsRegular,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun PageIndicator(
    isActive: Boolean,
    color: Color
) {
    val width by animateDpAsState(
        targetValue = if (isActive) 32.dp else 8.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "indicatorWidth"
    )

    Box(
        modifier = Modifier
            .height(8.dp)
            .width(width)
            .background(
                color = if (isActive) color else MaterialTheme.colorScheme.surfaceContainerHighest,
                shape = CircleShape
            )
    )
}

@Composable
private fun ThemeToggleButton(
    currentTheme: com.yumedev.seijakulist.ui.theme.ThemeMode,
    onThemeChange: (com.yumedev.seijakulist.ui.theme.ThemeMode) -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "themeButtonScale",
        finishedListener = {
            if (isPressed) isPressed = false
        }
    )

    val rotation by animateFloatAsState(
        targetValue = if (isPressed) 180f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "themeButtonRotation"
    )

    // Determinar icono y descripción según el tema actual
    val (icon, description) =
        when (currentTheme) {
        com.yumedev.seijakulist.ui.theme.ThemeMode.SYSTEM -> Icons.Outlined.PhoneAndroid to "Sistema"
        com.yumedev.seijakulist.ui.theme.ThemeMode.LIGHT -> Icons.Outlined.LightMode to "Claro"
        com.yumedev.seijakulist.ui.theme.ThemeMode.DARK -> Icons.Outlined.DarkMode to "Oscuro"
        com.yumedev.seijakulist.ui.theme.ThemeMode.JAPANESE -> Icons.Outlined.Star to "Japonés"
    }

    Surface(
        modifier = Modifier
            .size(48.dp)
            .scale(scale),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.8f),
        tonalElevation = 4.dp,
        shadowElevation = 2.dp
    ) {
        IconButton(
            onClick = {
                isPressed = true
                // Ciclar al siguiente tema
                val nextTheme = when (currentTheme) {
                    com.yumedev.seijakulist.ui.theme.ThemeMode.SYSTEM -> com.yumedev.seijakulist.ui.theme.ThemeMode.LIGHT
                    com.yumedev.seijakulist.ui.theme.ThemeMode.LIGHT -> com.yumedev.seijakulist.ui.theme.ThemeMode.DARK
                    com.yumedev.seijakulist.ui.theme.ThemeMode.DARK -> com.yumedev.seijakulist.ui.theme.ThemeMode.JAPANESE
                    com.yumedev.seijakulist.ui.theme.ThemeMode.JAPANESE -> com.yumedev.seijakulist.ui.theme.ThemeMode.SYSTEM
                }
                onThemeChange(nextTheme)
            },
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = description,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer {
                        rotationZ = rotation
                    }
            )
        }
    }
}

@Composable
private fun ThemeChangeNotification(theme: com.yumedev.seijakulist.ui.theme.ThemeMode) {
    val themeName = when (theme) {
        com.yumedev.seijakulist.ui.theme.ThemeMode.SYSTEM -> "Sistema"
        com.yumedev.seijakulist.ui.theme.ThemeMode.LIGHT -> "Claro"
        com.yumedev.seijakulist.ui.theme.ThemeMode.DARK -> "Oscuro"
        com.yumedev.seijakulist.ui.theme.ThemeMode.JAPANESE -> "Japonés"
    }

    Surface(
        modifier = Modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = 4.dp,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Palette,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Tema cambiado a: $themeName",
                fontFamily = PoppinsBold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                letterSpacing = 0.2.sp
            )
        }
    }
}
